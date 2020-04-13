/*
 * @(#) JSONExpect.kt
 *
 * json-kotlin-test Library for testing Kotlin JSON applications
 * Copyright (c) 2020 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.pwall.json.test

import kotlin.test.fail

import net.pwall.json.JSONConfig
import net.pwall.json.parseJSON

class JSONExpect(private val obj: Any?, private val pathInfo: String? = null) {

    private val prefix: String
        get() = if (pathInfo != null) "$pathInfo: " else ""

    private fun propertyPath(name: String) = if (pathInfo != null) "$pathInfo.$name" else name

    private fun itemPath(index: Int) = if (pathInfo != null) "$pathInfo[$index]" else "[$index]"

    fun value(expected: Any?) {
        checkValue(obj, expected)
    }

    fun property(name: String, expected: Any?) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        JSONExpect(getProperty(name), propertyPath(name)).value(expected)
    }

    fun property(name: String, tests: JSONExpect.() -> Unit) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        JSONExpect(getProperty(name), propertyPath(name)).tests()
    }

    fun item(index: Int, expected: Any?) {
        require(index >= 0) { "JSON array index must not be negative" }
        JSONExpect(getItem(index), itemPath(index)).value(expected)
    }

    fun item(index: Int, tests: JSONExpect.() -> Unit) {
        require(index >= 0) { "JSON array index must not be negative" }
        JSONExpect(getItem(index), itemPath(index)).tests()
    }

    fun count(expected: Int) {
        require(expected >= 0) { "JSON array or object count must not be negative" }
        val length = when (obj) {
            is List<*> -> obj.size
            is Map<*, *> -> obj.size
            else -> fail("${prefix}JSON count check not on array or object")
        }
        if (length != expected)
            fail("${prefix}JSON length doesn't match - Expected $expected, was $length")
    }

    fun propertyAbsent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (obj !is Map<*, *>)
            fail("${prefix}Not a JSON object")
        if (obj.containsKey(name))
            fail("${prefix}JSON property not absent - $name")
    }

    private fun getProperty(name: String): Any? {
        if (obj !is Map<*, *>)
            fail("${propertyPath(name)}: Not a JSON object")
        if (!obj.containsKey(name))
            fail("${propertyPath(name)}: JSON property missing")
        return obj[name]
    }

    private fun getItem(index: Int): Any? {
        if (obj !is List<*>)
            fail("${itemPath(index)}: Not a JSON array")
        if (index !in obj.indices)
            fail("${itemPath(index)}: JSON array index out of bounds")
        return obj[index]
    }

    private fun checkValue(value: Any?, expected: Any?) {
        if (value != expected)
            fail("${prefix}JSON value doesn't match - Expected $expected, was $value")
    }

    companion object {

        fun expectJSON(json: String, tests: JSONExpect.() -> Unit) {
            val obj = try {
                json.parseJSON<Any>()
            }
            catch (e: Exception) {
                fail("Unable to parse JSON - ${e.message}")
            }
            JSONExpect(obj).tests()
        }

    }

}
