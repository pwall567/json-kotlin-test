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

import java.math.BigDecimal

import net.pwall.json.parseJSON

/**
 * Implementation class for `expectJSON()` function.
 *
 * @author  Peter Wall
 */
class JSONExpect private constructor(private val obj: Any?, private val pathInfo: String? = null) {

    /**
     * Check the value as an [Int].
     *
     * @param   expected        the expected [Int] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Int) {
        if (obj !is Int)
            failOnType("integer")
        if (obj != expected)
            failOnValue(expected.toString(), obj.toString())
    }

    /**
     * Check the value as a [Long].
     *
     * @param   expected        the expected [Long] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Long) {
        if (obj !is Long)
            failOnType("long integer")
        if (obj != expected)
            failOnValue(expected.toString(), obj.toString())
    }

    /**
     * Check the value as a [BigDecimal].
     *
     * @param   expected        the expected [BigDecimal] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: BigDecimal) {
        if (obj !is BigDecimal)
            failOnType("decimal")
        if (obj != expected)
            failOnValue(expected.toString(), obj.toString())
    }

    /**
     * Check the value as a [Boolean].
     *
     * @param   expected        the expected [Boolean] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Boolean) {
        if (obj !is Boolean)
            failOnType("boolean")
        if (obj != expected)
            failOnValue(expected.toString(), obj.toString())
    }

    /**
     * Check the value as a [String] or `null`.
     *
     * @param   expected        the expected [String] value (or `null`)
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: String?) {
        when (expected) {
            null -> {
                if (obj != null)
                    failOnType("null")
            }
            else -> {
                if (obj !is String)
                    failOnType("string")
                if (obj != expected)
                    failOnValue("\"$expected\"", "\"$obj\"")
            }
        }
    }

    /**
     * Check a property as an [Int].
     *
     * @param   name            the property name
     * @param   expected        the expected [Int] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Int) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Long].
     *
     * @param   name            the property name
     * @param   expected        the expected [Long] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Long) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).value(expected)
        }
    }

    /**
     * Check a property as a [BigDecimal].
     *
     * @param   name            the property name
     * @param   expected        the expected [BigDecimal] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: BigDecimal) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Boolean].
     *
     * @param   name            the property name
     * @param   expected        the expected [Boolean] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Boolean) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).value(expected)
        }
    }

    /**
     * Check a property as a [String] or `null`.
     *
     * @param   name            the property name
     * @param   expected        the expected [String] value (or `null`)
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: String?) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).value(expected)
        }
    }

    /**
     * Select a property for nested tests.
     *
     * @param   name            the property name
     * @param   tests           the tests to be performed on the property
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun property(name: String, tests: JSONExpect.() -> Unit) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).tests()
        }
    }

    /**
     * Check an array item as an [Int].
     *
     * @param   index           the array index
     * @param   expected        the expected [Int] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Int) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Long].
     *
     * @param   index           the array index
     * @param   expected        the expected [Long] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Long) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [BigDecimal].
     *
     * @param   index           the array index
     * @param   expected        the expected [BigDecimal] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: BigDecimal) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Boolean].
     *
     * @param   index           the array index
     * @param   expected        the expected [Boolean] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Boolean) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [String] or `null`.
     *
     * @param   index           the array index
     * @param   expected        the expected [String] value (or `null`)
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: String?) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).value(expected)
        }
    }

    /**
     * Select an array item for nested tests.
     *
     * @param   index           the array index
     * @param   tests           the tests to be performed on the property
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun item(index: Int, tests: JSONExpect.() -> Unit) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).tests()
        }
    }

    /**
     * Check the count of array items or object properties.
     *
     * @param   expected        the expected count
     * @throws  AssertionError  if the value is incorrect
     */
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

    /**
     * Check that a property is absent from an object.
     *
     * @param   name            the property name
     * @throws  AssertionError  if the value is incorrect
     */
    fun propertyAbsent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (obj !is Map<*, *>)
            fail("${prefix}Not a JSON object")
        if (obj.containsKey(name))
            fail("${prefix}JSON property not absent - $name")
    }

    private fun failOnValue(expectedString: String, actualString: String): Nothing {
        fail("${prefix}JSON value doesn't match - Expected $expectedString, was $actualString")
    }

    private fun failOnType(expected: String): Nothing {
        val type = when (obj) {
            null -> "null"
            is Int -> "integer"
            is Long -> "long integer"
            is BigDecimal -> "decimal"
            is String -> "string"
            is Boolean -> "boolean"
            is List<*> -> "array"
            is Map<*, *> -> "object"
            else -> "unknown"
        }
        fail("${prefix}JSON type doesn't match - Expected $expected, was $type")
    }

    private fun checkName(name: String): String =
            if (name.isNotEmpty()) name else fail("JSON property name must not be empty")

    private fun checkIndex(index: Int): Int = if (index >= 0) index else fail("JSON array index must not be negative")

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

    private fun propertyPath(name: String) = if (pathInfo != null) "$pathInfo.$name" else name

    private fun itemPath(index: Int) = if (pathInfo != null) "$pathInfo[$index]" else "[$index]"

    private val prefix: String
        get() = if (pathInfo != null) "$pathInfo: " else ""

    companion object {

        /**
         * Check that a JSON string matches the defined expectations.
         *
         * @param   json            the JSON string
         * @param   tests           the tests to be performed on the JSON
         * @throws  AssertionError  if any of the tests fail
         */
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
