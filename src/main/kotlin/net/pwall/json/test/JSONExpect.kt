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
import java.util.UUID

import net.pwall.json.JSONTypeRef
import net.pwall.json.parseJSON

/**
 * Implementation class for `expectJSON()` function.
 *
 * @author  Peter Wall
 */
class JSONExpect private constructor(
        /** The context node. */
        val node: Any?,
        /** The context node path. */
        val path: String? = null) {

    /**
     * Check the value as an [Int].
     *
     * @param   expected        the expected [Int] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Int) {
        if (node !is Int)
            errorOnType("integer")
        if (node != expected)
            errorOnValue(expected, node)
    }

    /**
     * Check the value as a [Long].
     *
     * @param   expected        the expected [Long] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Long) {
        if (node !is Long)
            errorOnType("long integer")
        if (node != expected)
            errorOnValue(expected, node)
    }

    /**
     * Check the value as a [BigDecimal].
     *
     * @param   expected        the expected [BigDecimal] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: BigDecimal) {
        if (node !is BigDecimal)
            errorOnType("decimal")
        if (node != expected)
            errorOnValue(expected, node)
    }

    /**
     * Check the value as a [Boolean].
     *
     * @param   expected        the expected [Boolean] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Boolean) {
        if (node !is Boolean)
            errorOnType("boolean")
        if (node != expected)
            errorOnValue(expected, node)
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
                if (node != null)
                    errorOnType("null")
            }
            else -> {
                if (node !is String)
                    errorOnType("string")
                if (node != expected)
                    errorOnValue("\"$expected\"", "\"$node\"")
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
        if (node !is Int)
            errorOnType("integer")
        if (node !in expected)
            errorOnValue(expected, node)
    }

    /**
     * Check the value as a member of a [LongRange].
     *
     * @param   expected        the [LongRange]
     * @throws  AssertionError  if the value is not within the [LongRange]
     */
    fun value(expected: LongRange) {
        if (node !is Long)
            errorOnType("long integer")
        if (node !in expected)
            errorOnValue(expected, node)
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
        val itemClass = type.classifier as? KClass<*> ?: error("Can't determine type of ClosedRange")
        when (itemClass) {
            Int::class -> {
                if (node !is Int)
                    errorOnType("integer")
                if (node as T !in expected)
                    errorInRange(node)
            }
            Long::class -> {
                if (node !is Long)
                    errorOnType("long integer")
                if (node as T !in expected)
                    errorInRange(node)
            }
            BigDecimal::class -> {
                if (node !is BigDecimal)
                    errorOnType("decimal")
                if (node as T !in expected)
                    errorInRange(node)
            }
            String::class -> {
                if (node !is String)
                    errorOnType("string")
                if (node as T !in expected)
                    errorInRange("\"$node\"")
            }
            else -> error("Can't perform test using range of $type")
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
        val itemClass = type.classifier as? KClass<*> ?: error("Can't determine type of Collection")
        when (itemClass) {
            Int::class -> {
                if (node != null && node !is Int)
                    errorOnType("integer")
                if (!expected.contains(node))
                    errorInCollection(node)
            }
            Long::class -> {
                if (node != null && node !is Long)
                    errorOnType("long integer")
                if (!expected.contains(node))
                    errorInCollection(node)
            }
            BigDecimal::class -> {
                if (node != null && node !is BigDecimal)
                    errorOnType("decimal")
                if (!expected.contains(node))
                    errorInCollection(node)
            }
            String::class -> {
                if (node != null && node !is String)
                    errorOnType("string")
                if (!expected.contains(node))
                    errorInCollection(if (node == null) "null" else "\"$node\"")
            }
            else -> error("Can't perform test using collection of $type")
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
     * Apply pre-configured tests to the value.
     *
     * @param   tests           the tests
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun value(tests: JSONExpect.() -> Unit) {
        tests.invoke(this)
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
        val length = when (node) {
            is List<*> -> node.size
            is Map<*, *> -> node.size
            else -> error("JSON count check not on array or object")
        }
        if (length != expected)
            error("JSON count doesn't match - Expected $expected, was $length")
    }

    /**
     * Check the count of array items or object properties as a range.
     *
     * @param   expected        the expected range
     * @throws  AssertionError  if the value is incorrect
     */
    fun count(expected: IntRange) {
        require(expected.first >= 0) { "JSON array or object count must not be negative" }
        val length = when (node) {
            is List<*> -> node.size
            is Map<*, *> -> node.size
            else -> error("JSON count check not on array or object")
        }
        if (length !in expected)
            error("JSON count doesn't match - Expected $expected, was $length")
    }

    /**
     * Check that a property is absent from an object.
     *
     * @param   name            the property name
     * @throws  AssertionError  if the value is incorrect
     */
    fun propertyAbsent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (node !is Map<*, *>)
            error("Not a JSON object")
        if (node.containsKey(name))
            error("JSON property not absent - $name")
    }

    /**
     * Check that a property is absent from an object, or if present, is `null`.
     *
     * @param   name            the property name
     * @throws  AssertionError  if the value is incorrect
     */
    fun propertyAbsentOrNull(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (node !is Map<*, *>)
            error("Not a JSON object")
        if (node[name] != null)
            error("JSON property not absent or null - $name")
    }

    /** Check that a string value is a valid UUID. */
    val uuid: JSONExpect.() -> Unit = {
        if (node !is String)
            errorOnType("string")
        try {
            UUID.fromString(node)
        }
        catch (e: Exception) {
            error("JSON string is not a UUID")
        }
    }

    /**
     * Check the length of a string value.
     *
     * @param   expected        the expected length
     * @throws  AssertionError  if the length is incorrect
     */
    fun length(expected: Int): JSONExpect.() -> Unit = {
        if (node !is String)
            errorOnType("string")
        if (node.length != expected)
            error("JSON string length doesn't match - Expected $expected, was ${node.length}")
    }

    /**
     * Check the length of a string value as a range.
     *
     * @param   expected        the expected length as a range
     * @throws  AssertionError  if the length is incorrect
     */
    fun length(expected: IntRange): JSONExpect.() -> Unit = {
        if (node !is String)
            errorOnType("string")
        if (node.length !in expected)
            error("JSON string length doesn't match - Expected $expected, was ${node.length}")
    }

    /**
     * Report error, including context path.
     *
     * @param   message     the error message
     */
    fun error(message: String): Nothing {
        val context = if (path != null) "$path: " else ""
        fail("$context$message")
    }

    private fun errorOnValue(expected: Any, actual: Any): Nothing {
        error("JSON value doesn't match - Expected $expected, was $actual")
    }

    private fun errorOnType(expected: String): Nothing {
        val type = when (node) {
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
        error("JSON type doesn't match - Expected $expected, was $type")
    }

    private fun errorInCollection(actual: Any?) {
        error("JSON value not in collection - $actual")
    }

    private fun errorInRange(actual: Any?) {
        error("JSON value not in range - $actual")
    }

    private fun checkName(name: String): String =
            if (name.isNotEmpty()) name else fail("JSON property name must not be empty")

    private fun checkIndex(index: Int): Int = if (index >= 0) index else fail("JSON array index must not be negative")

    private fun getProperty(name: String): Any? {
        if (node !is Map<*, *>)
            fail("${propertyPath(name)}: Not a JSON object")
        if (!node.containsKey(name))
            fail("${propertyPath(name)}: JSON property missing")
        return node[name]
    }

    private fun getItem(index: Int): Any? {
        if (node !is List<*>)
            fail("${itemPath(index)}: Not a JSON array")
        if (index !in node.indices)
            fail("${itemPath(index)}: JSON array index out of bounds")
        return node[index]
    }

    private fun propertyPath(name: String) = if (path != null) "$path.$name" else name

    private fun itemPath(index: Int) = if (path != null) "$path[$index]" else "[$index]"

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
