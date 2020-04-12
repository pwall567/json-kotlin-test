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

class JSONExpect(private val json: String, private val config: JSONConfig = JSONConfig.defaultConfig) {

    private val tests = mutableListOf<AbstractTest>()

    fun value(expected: Any?) {
        tests.add(ValueTest(emptyList(), expected))
    }

    fun item(index: Int, expected: Any?) {
        require(index >= 0) { "JSON array index must not be negative" }
        tests.add(ValueTest(listOf(ArraySelector(index)), expected))
    }

    fun item(index: Int, configure: Nested.() -> Unit) {
        require(index >= 0) { "JSON array index must not be negative" }
        Nested(listOf(ArraySelector(index)), tests).configure()
    }

    fun property(name: String, expected: Any?) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        tests.add(ValueTest(listOf(ObjectSelector(name)), expected))
    }

    fun property(name: String, configure: Nested.() -> Unit) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        Nested(listOf(ObjectSelector(name)), tests).configure()
    }

    fun count(expected: Int) {
        require(expected >= 0) { "JSON array or object count must not be negative" }
        tests.add(LengthTest(emptyList(), expected))
    }

    fun propertyAbsent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        tests.add(AbsentTest(emptyList(), name))
    }

    fun check() {
        val obj = try {
            json.parseJSON<Any>(config)
        }
        catch (e: Exception) {
            fail("Unable to parse JSON - ${e.message}")
        }
        tests.forEach {
            it.performTest(obj)
        }
    }

    class Nested(private val selectors: List<Selector>, private val tests: MutableList<AbstractTest>) {

        fun value(expected: Any?) {
            tests.add(ValueTest(selectors, expected))
        }

        fun item(index: Int, expected: Any?) {
            require(index >= 0) { "JSON array index must not be negative" }
            tests.add(ValueTest(extendedSelectors(ArraySelector(index)), expected))
        }

        fun item(index: Int, configure: Nested.() -> Unit) {
            require(index >= 0) { "JSON array index must not be negative" }
            Nested(extendedSelectors(ArraySelector(index)), tests).configure()
        }

        fun property(name: String, expected: Any?) {
            require(name.isNotEmpty()) { "JSON property name must not be empty" }
            tests.add(ValueTest(extendedSelectors(ObjectSelector(name)), expected))
        }

        fun property(name: String, configure: Nested.() -> Unit) {
            require(name.isNotEmpty()) { "JSON property name must not be empty" }
            Nested(extendedSelectors(ObjectSelector(name)), tests).configure()
        }

        fun count(expected: Int) {
            require(expected >= 0) { "JSON array or object count must not be negative" }
            tests.add(LengthTest(selectors, expected))
        }

        fun propertyAbsent(name: String) {
            tests.add(AbsentTest(selectors, name))
        }

        private fun extendedSelectors(newSelector: Selector): List<Selector> = mutableListOf<Selector>().apply {
            addAll(selectors)
            add(newSelector)
        }

    }

    interface Selector {
        fun select(obj: Any?, selectors: List<Selector>): Any?
    }

    class ArraySelector(private val index: Int) : Selector {

        override fun select(obj: Any?, selectors: List<Selector>): Any? {
            if (obj !is List<*>)
                fail("${describeSelectors(selectors, this)}Not a JSON array")
            if (index !in obj.indices)
                fail("${describeSelectors(selectors, this)}JSON array index out of bounds")
            return obj[index]
        }

        override fun toString(): String = "[$index]"

    }

    class ObjectSelector(private val key: String) : Selector {

        override fun select(obj: Any?, selectors: List<Selector>): Any? {
            if (obj !is Map<*, *>)
                fail("${describeSelectors(selectors, this)}Not a JSON object")
            if (!obj.containsKey(key))
                fail("${describeSelectors(selectors, this)}JSON property missing")
            return obj[key]
        }

        override fun toString(): String = ".$key"

    }

    abstract class AbstractTest(val selectors: List<Selector>) {

        abstract fun performTest(obj: Any?)

        fun select(obj: Any?): Any? {
            var result = obj
            selectors.forEach { result = it.select(result, selectors) }
            return result
        }

    }

    class ValueTest(selectors: List<Selector>, private val expected: Any?) : AbstractTest(selectors) {

        override fun performTest(obj: Any?) {
            val value = select(obj)
            if (value != expected)
                fail("${describeSelectors(selectors)}JSON value doesn't match - Expected $expected, was $value")
        }

    }

    class LengthTest(selectors: List<Selector>, private val expected: Int) : AbstractTest(selectors) {

        override fun performTest(obj: Any?) {
            when (val selected = select(obj)) {
                is List<*> -> compareLength(selected.size)
                is Map<*, *> -> compareLength(selected.size)
                else -> fail("${describeSelectors(selectors)}JSON count check not on array or object")
            }
        }

        private fun compareLength(actual: Int) {
            if (actual != expected)
                fail("${describeSelectors(selectors)}JSON length doesn't match - Expected $expected, was $actual")
        }

    }

    class AbsentTest(selectors: List<Selector>, private val name: String) : AbstractTest(selectors) {

        override fun performTest(obj: Any?) {
            val selected = select(obj)
            if (selected !is Map<*, *>)
                fail("${describeSelectors(selectors)}Not a JSON object")
            if (selected.containsKey(name))
                fail("${describeSelectors(selectors)}JSON property not absent - $name")
        }

    }

    companion object {

        fun expectJSON(json: String, configure: JSONExpect.() -> Unit) {
            val jsonExpect = JSONExpect(json)
            jsonExpect.configure()
            jsonExpect.check()
        }

        internal fun describeSelectors(selectors: List<Selector>, selector: Selector? = null): String =
                StringBuilder().apply {
            for (s in selectors) {
                append(s)
                if (s === selector)
                    break
            }
            if (length > 0 && this[0] == '.')
                deleteCharAt(0)
            if (length > 0)
                append(": ")
        }.toString()

    }

}
