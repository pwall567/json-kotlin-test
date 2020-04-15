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

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.test.fail

import java.math.BigDecimal

import net.pwall.json.JSONTypeRef
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
            failOnValue(expected, obj)
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
            failOnValue(expected, obj)
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
            failOnValue(expected, obj)
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
            failOnValue(expected, obj)
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
     * Check the value as a member of an [IntRange].
     *
     * @param   expected        the [IntRange]
     * @throws  AssertionError  if the value is not within the [IntRange]
     */
    fun value(expected: IntRange) {
        if (obj !is Int)
            failOnType("integer")
        if (obj !in expected)
            failInRange(obj)
    }

    /**
     * Check the value as a member of a [LongRange].
     *
     * @param   expected        the [LongRange]
     * @throws  AssertionError  if the value is not within the [LongRange]
     */
    fun value(expected: LongRange) {
        if (obj !is Long)
            failOnType("long integer")
        if (obj !in expected)
            failInRange(obj)
    }

    /**
     * Check the value as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     *
     * @param   expected        the [ClosedRange]
     * @param   type            the type of the elements of the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Comparable<T>> valueInRange(expected: ClosedRange<T>, type: KType) {
        val itemClass = type.classifier as? KClass<*> ?: fail("${prefix}Can't determine type of ClosedRange")
        when (itemClass) {
            Int::class -> {
                if (obj !is Int)
                    failOnType("integer")
                if (obj as T !in expected)
                    failInRange(obj)
            }
            Long::class -> {
                if (obj !is Long)
                    failOnType("long integer")
                if (obj as T !in expected)
                    failInRange(obj)
            }
            BigDecimal::class -> {
                if (obj !is BigDecimal)
                    failOnType("decimal")
                if (obj as T !in expected)
                    failInRange(obj)
            }
            String::class -> {
                if (obj !is String)
                    failOnType("string")
                if (obj as T !in expected)
                    failInRange("\"$obj\"")
            }
            else -> fail("${prefix}Can't perform test using range of $type")
        }
    }

    /**
     * Check the value as a member of a [ClosedRange].
     *
     * @param   expected        the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    inline fun <reified T: Comparable<T>> value(expected: ClosedRange<T>) {
        valueInRange(expected, JSONTypeRef.create<T>(nullable = true).refType)
    }

    /**
     * Check the value as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   expected        the [Collection]
     * @param   type            the type of the elements of the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    fun <T> valueInCollection(expected: Collection<T>, type: KType) {
        val itemClass = type.classifier as? KClass<*> ?: fail("${prefix}Can't determine type of Collection")
        when (itemClass) {
            Int::class -> {
                if (obj != null && obj !is Int)
                    failOnType("integer")
                if (!expected.contains(obj))
                    failInCollection(obj)
            }
            Long::class -> {
                if (obj != null && obj !is Long)
                    failOnType("long integer")
                if (!expected.contains(obj))
                    failInCollection(obj)
            }
            BigDecimal::class -> {
                if (obj != null && obj !is BigDecimal)
                    failOnType("decimal")
                if (!expected.contains(obj))
                    failInCollection(obj)
            }
            String::class -> {
                if (obj != null && obj !is String)
                    failOnType("string")
                if (!expected.contains(obj))
                    failInCollection(if (obj == null) "null" else "\"$obj\"")
            }
            else -> fail("${prefix}Can't perform test using collection of $type")
        }
    }

    /**
     * Check the value as a member of a [Collection].
     *
     * @param   expected        the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    inline fun <reified T: Any> value(expected: Collection<T?>) {
        valueInCollection(expected, JSONTypeRef.create<T>(nullable = true).refType)
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
     * Check a property as a member of an [IntRange].
     *
     * @param   name            the property name
     * @param   expected        the [IntRange]
     * @throws  AssertionError  if the value is not within the [IntRange]
     */
    fun property(name: String, expected: IntRange) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).value(expected)
        }
    }

    /**
     * Check a property as a member of a [LongRange].
     *
     * @param   name            the property name
     * @param   expected        the [LongRange]
     * @throws  AssertionError  if the value is not within the [LongRange]
     */
    fun property(name: String, expected: LongRange) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).value(expected)
        }
    }

    /**
     * Check a property as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     *
     * @param   name            the property name
     * @param   expected        the [ClosedRange]
     * @param   type            the type of the elements of the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    fun <T: Comparable<T>> propertyInRange(name: String, expected: ClosedRange<T>, type: KType) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).valueInRange(expected, type)
        }
    }

    /**
     * Check a property as a member of a [ClosedRange].
     *
     * @param   name            the property name
     * @param   expected        the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    inline fun <reified T: Comparable<T>> property(name: String, expected: ClosedRange<T>) {
        propertyInRange(name, expected, JSONTypeRef.create<T>(nullable = true).refType)
    }

    /**
     * Check a property as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   name            the property name
     * @param   expected        the [Collection]
     * @param   type            the type of the elements of the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    fun <T> propertyInCollection(name: String, expected: Collection<T>, type: KType) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).valueInCollection(expected, type)
        }
    }

    /**
     * Check a property as a member of a [Collection].
     *
     * @param   name            the property name
     * @param   expected        the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    inline fun <reified T: Any> property(name: String, expected: Collection<T?>) {
        propertyInCollection(name, expected, JSONTypeRef.create<T>(nullable = true).refType)
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
     * Check an array item as a member of an [IntRange].
     *
     * @param   index           the array index
     * @param   expected        the [IntRange]
     * @throws  AssertionError  if the value is not within the [IntRange]
     */
    fun item(index: Int, expected: IntRange) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).value(expected)
        }
    }

    /**
     * Check an array item as a member of a [LongRange].
     *
     * @param   index           the array index
     * @param   expected        the [LongRange]
     * @throws  AssertionError  if the value is not within the [LongRange]
     */
    fun item(index: Int, expected: LongRange) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).value(expected)
        }
    }

    /**
     * Check an array item as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     *
     * @param   index           the array index
     * @param   expected        the [ClosedRange]
     * @param   type            the type of the elements of the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    fun <T: Comparable<T>> itemInRange(index: Int, expected: ClosedRange<T>, type: KType) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).valueInRange(expected, type)
        }
    }

    /**
     * Check an array item as a member of a [ClosedRange].
     *
     * @param   index           the array index
     * @param   expected        the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    inline fun <reified T: Comparable<T>> item(index: Int, expected: ClosedRange<T>) {
        itemInRange(index, expected, JSONTypeRef.create<T>(nullable = true).refType)
    }

    /**
     * Check an array item as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   index           the array index
     * @param   expected        the [Collection]
     * @param   type            the type of the elements of the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    fun <T> itemInCollection(index: Int, expected: Collection<T>, type: KType) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).valueInCollection(expected, type)
        }
    }

    /**
     * Check an array item as a member of a [Collection].
     *
     * @param   index           the array index
     * @param   expected        the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    inline fun <reified T: Any> item(index: Int, expected: Collection<T?>) {
        itemInCollection(index, expected, JSONTypeRef.create<T>(nullable = true).refType)
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

    private fun failOnValue(expected: Any, actual: Any): Nothing {
        fail("${prefix}JSON value doesn't match - Expected $expected, was $actual")
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

    private fun failInCollection(actual: Any?) {
        fail("${prefix}JSON value not in collection - $actual")
    }

    private fun failInRange(actual: Any?) {
        fail("${prefix}JSON value not in range - $actual")
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
