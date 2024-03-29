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
import kotlin.test.fail

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZonedDateTime
import java.util.UUID

import net.pwall.json.JSONSimple
import net.pwall.json.validation.JSONValidation

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

    /** The context node as [Int]. */
    val nodeAsInt: Int
        get() = if (node is Int) node else errorOnType("integer")

    /** The context node as [Long]. */
    val nodeAsLong: Long
        get() = when (node) {
            is Long -> node
            is Int -> node.toLong()
            else -> errorOnType("long integer")
        }

    /** The context node as [BigDecimal]. */
    val nodeAsDecimal: BigDecimal
        get() = when (node) {
            is BigDecimal -> node
            is Long -> BigDecimal(node)
            is Int -> BigDecimal(node)
            else -> errorOnType("decimal")
        }

    /** The context node as [Boolean]. */
    val nodeAsBoolean: Boolean
        get() = if (node is Boolean) node else errorOnType("boolean")

    /** The context node as [String]. */
    val nodeAsString: String
        get() = if (node is String) node else errorOnType("string")

    /** The context node as [Map]. */
    val nodeAsObject: Map<*, *>
        get() = if (node is Map<*, *>) node else errorOnType("object")

    /** The context node as [List]. */
    val nodeAsArray: List<*>
        get() = if (node is List<*>) node else errorOnType("array")

    /**
     * Check the value as an [Int].
     *
     * @param   expected        the expected [Int] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Int) {
        if (nodeAsInt != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Long].
     *
     * @param   expected        the expected [Long] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Long) {
        if (nodeAsLong != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [BigDecimal].
     *
     * @param   expected        the expected [BigDecimal] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: BigDecimal) {
        if (nodeAsDecimal.compareTo(expected) != 0)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Boolean].
     *
     * @param   expected        the expected [Boolean] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Boolean) {
        if (nodeAsBoolean != expected)
            errorOnValue(expected)
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
                if (nodeAsString != expected)
                    errorOnValue("\"$expected\"")
            }
        }
    }

    /**
     * Check the value as a [String] against a [Regex].
     *
     * @param   expected        the [Regex]
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Regex) {
        if (!(expected matches nodeAsString))
            error("JSON string doesn't match regex - Expected $expected, was ${showNode()}")
    }

    /**
     * Check the value as a member of an [IntRange].
     *
     * @param   expected        the [IntRange]
     * @throws  AssertionError  if the value is not within the [IntRange]
     */
    fun value(expected: IntRange) {
        if (nodeAsInt !in expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a member of a [LongRange].
     *
     * @param   expected        the [LongRange]
     * @throws  AssertionError  if the value is not within the [LongRange]
     */
    fun value(expected: LongRange) {
        if (nodeAsLong !in expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     *
     * @param   expected        the [ClosedRange]
     * @param   itemClass       the class of the elements of the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Comparable<T>> valueInRange(expected: ClosedRange<T>, itemClass: KClass<*>) {
        when (itemClass) {
            Int::class -> {
                if (nodeAsInt as T !in expected)
                    errorInRange()
            }
            Long::class -> {
                if (nodeAsLong as T !in expected)
                    errorInRange()
            }
            BigDecimal::class -> {
                if (nodeAsDecimal as T !in expected)
                    errorInRange()
            }
            String::class -> {
                if (nodeAsString as T !in expected)
                    errorInRange()
            }
            else -> error("Can't perform test using range of $itemClass")
        }
    }

    /**
     * Check the value as a member of a [ClosedRange].
     *
     * @param   expected        the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    inline fun <reified T: Comparable<T>> value(expected: ClosedRange<T>) {
        valueInRange(expected, T::class)
    }

    /**
     * Check the value as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   expected        the [Collection]
     * @param   itemClass       the class of the elements of the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> valueInCollection(expected: Collection<T?>, itemClass: KClass<*>) {
        if (node == null) {
            if (!expected.contains(null))
                errorInCollection()
        }
        else {
            when (itemClass) {
                Int::class -> {
                    if (!expected.contains(nodeAsInt as T))
                        errorInCollection()
                }
                Long::class -> {
                    if (!expected.contains(nodeAsLong as T))
                        errorInCollection()
                }
                BigDecimal::class -> {
                    if (!expected.contains(nodeAsDecimal as T))
                        errorInCollection()
                }
                String::class -> {
                    if (!expected.contains(nodeAsString as T))
                        errorInCollection()
                }
                else -> error("Can't perform test using collection of $itemClass")
            }
        }
    }

    /**
     * Check the value as a member of a [Collection].
     *
     * @param   expected        the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    inline fun <reified T: Any> value(expected: Collection<T?>) {
        valueInCollection(expected, T::class)
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
     * Check a property as a [String] against a [Regex].
     *
     * @param   name            the property name
     * @param   expected        the [Regex]
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Regex) {
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
     * @param   itemClass       the class of the elements of the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    fun <T: Comparable<T>> propertyInRange(name: String, expected: ClosedRange<T>, itemClass: KClass<*>) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).valueInRange(expected, itemClass)
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
        propertyInRange(name, expected, T::class)
    }

    /**
     * Check a property as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   name            the property name
     * @param   expected        the [Collection]
     * @param   itemClass       the class of the elements of the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    fun <T: Any> propertyInCollection(name: String, expected: Collection<T?>, itemClass: KClass<*>) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPath(it)).valueInCollection(expected, itemClass)
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
        propertyInCollection(name, expected, T::class)
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
     * Check a property as a [String] against a [Regex].
     *
     * @param   index           the array index
     * @param   expected        the [Regex]
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Regex) {
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
     * @param   itemClass       the class of the elements of the [ClosedRange]
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    fun <T: Comparable<T>> itemInRange(index: Int, expected: ClosedRange<T>, itemClass: KClass<*>) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).valueInRange(expected, itemClass)
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
        itemInRange(index, expected, T::class)
    }

    /**
     * Check an array item as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   index           the array index
     * @param   expected        the [Collection]
     * @param   itemClass       the class of the elements of the [Collection]
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    fun <T: Any> itemInCollection(index: Int, expected: Collection<T?>, itemClass: KClass<*>) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPath(it)).valueInCollection(expected, itemClass)
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
        itemInCollection(index, expected, T::class)
    }

    /**
     * Select an array item for nested tests.
     *
     * @param   index           the array index
     * @param   tests           the tests to be performed on the item
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
     * @throws  AssertionError  if the property is present
     */
    fun propertyAbsent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (nodeAsObject.containsKey(name))
            error("JSON property not absent - $name")
    }

    /**
     * Check that a property is absent from an object, or if present, is `null`.
     *
     * @param   name            the property name
     * @throws  AssertionError  if the property is present and not `null`
     */
    fun propertyAbsentOrNull(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (nodeAsObject[name] != null)
            error("JSON property not absent or null - $name")
    }

    /**
     * Check that a property is present in an object.
     *
     * @param   name            the property name
     * @throws  AssertionError  if the property is absent
     */
    fun propertyPresent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (!nodeAsObject.containsKey(name))
            error("JSON property not present - $name")
    }

    /**
     * Check that the value matches one of a number of tests.
     *
     * @param   tests           the tests (as lambdas)
     * @throws  AssertionError  if all of the tests fail
     */
    fun oneOf(vararg tests: JSONExpect.() -> Unit) {
        for (test in tests) {
            try {
                test.invoke(this)
                return
            }
            catch (_: AssertionError) {}
        }
        error("No successful test - value is ${showNode()}")
    }

    /**
     * Convert an [Int] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Int] value
     * @return              the lambda
     */
    fun test(expected: Int): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert a [Long] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Long] value
     * @return              the lambda
     */
    fun test(expected: Long): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert a [BigDecimal] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [BigDecimal] value
     * @return              the lambda
     */
    fun test(expected: BigDecimal): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert a [Boolean] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Boolean] value
     * @return              the lambda
     */
    fun test(expected: Boolean): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert a [String] or `null` equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [String] value (or `null`)
     * @return              the lambda
     */
    fun test(expected: String?): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert a [String] [Regex] check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [Regex]
     * @return              the lambda
     */
    fun test(expected: Regex): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert an [Int] range check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [IntRange]
     * @return              the lambda
     */
    fun test(expected: IntRange): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert a [Long] range check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [LongRange]
     * @return              the lambda
     */
    fun test(expected: LongRange): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert a [ClosedRange] check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [ClosedRange]
     * @return              the lambda
     */
    inline fun <reified T: Comparable<T>> test(expected: ClosedRange<T>): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /**
     * Convert a [Collection] check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [Collection]
     * @return              the lambda
     */
    inline fun <reified T: Any> test(expected: Collection<T>): JSONExpect.() -> Unit {
        return { value(expected) }
    }

    /** Check that a value is non-null. */
    val nonNull: JSONExpect.() -> Unit = {
        if (node == null)
            error("JSON item is null")
    }

    /** Check that a value is a string. */
    val string: JSONExpect.() -> Unit = {
        if (node !is String)
            errorOnType("string")
    }

    /** Check that a value is an integer. */
    val integer: JSONExpect.() -> Unit = {
        if (node !is Int)
            errorOnType("integer")
    }

    /** Check that a value is a long integer. */
    val longInteger: JSONExpect.() -> Unit = {
        if (!(node is Long || node is Int))
            errorOnType("long integer")
    }

    /** Check that a value is a decimal. */
    val decimal: JSONExpect.() -> Unit = {
        if (!(node is BigDecimal || node is Long || node is Int))
            errorOnType("decimal")
    }

    /** Check that a value is a boolean. */
    val boolean: JSONExpect.() -> Unit = {
        if (node !is Boolean)
            errorOnType("boolean")
    }

    /** Check that a string value is a valid [UUID]. */
    val uuid: JSONExpect.() -> Unit = {
        if (!JSONValidation.isUUID(nodeAsString))
            error("JSON string is not a UUID - ${showNode()}")
    }

    /** Check that a string value is a valid [LocalDate]. */
    val localDate: JSONExpect.() -> Unit = {
        try {
            LocalDate.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a LocalDate - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [LocalDateTime]. */
    val localDateTime: JSONExpect.() -> Unit = {
        try {
            LocalDateTime.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a LocalDateTime - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [LocalTime]. */
    val localTime: JSONExpect.() -> Unit = {
        try {
            LocalTime.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a LocalTime - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [OffsetDateTime]. */
    val offsetDateTime: JSONExpect.() -> Unit = {
        try {
            OffsetDateTime.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a OffsetDateTime - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [OffsetTime]. */
    val offsetTime: JSONExpect.() -> Unit = {
        try {
            OffsetTime.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a OffsetTime - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [ZonedDateTime]. */
    val zonedDateTime: JSONExpect.() -> Unit = {
        try {
            ZonedDateTime.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a ZonedDateTime - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [YearMonth]. */
    val yearMonth: JSONExpect.() -> Unit = {
        try {
            YearMonth.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a YearMonth - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [MonthDay]. */
    val monthDay: JSONExpect.() -> Unit = {
        try {
            MonthDay.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a MonthDay - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [Year]. */
    val year: JSONExpect.() -> Unit = {
        try {
            Year.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a Year - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [Duration]. */
    val duration: JSONExpect.() -> Unit = {
        try {
            Duration.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a Duration - ${showNode()}")
        }
    }

    /** Check that a string value is a valid [Period]. */
    val period: JSONExpect.() -> Unit = {
        try {
            Period.parse(nodeAsString)
        }
        catch (e: Exception) {
            error("JSON string is not a Period - ${showNode()}")
        }
    }

    /**
     * Check the length of a string value.
     *
     * @param   expected        the expected length
     * @throws  AssertionError  if the length is incorrect
     */
    fun length(expected: Int): JSONExpect.() -> Unit = {
        nodeAsString.let {
            if (it.length != expected)
                error("JSON string length doesn't match - Expected $expected, was ${it.length}")
        }
    }

    /**
     * Check the length of a string value as a range.
     *
     * @param   expected        the expected length as a range
     * @throws  AssertionError  if the length is incorrect
     */
    fun length(expected: IntRange): JSONExpect.() -> Unit = {
        nodeAsString.let {
            if (it.length !in expected)
                error("JSON string length doesn't match - Expected $expected, was ${it.length}")
        }
    }

    /**
     * Check the scale of a decimal value.
     *
     * @param   expected        the expected scale
     * @throws  AssertionError  if the scale is incorrect
     */
    fun scale(expected: Int): JSONExpect.() -> Unit = {
        nodeAsDecimal.let {
            if (it.scale() != expected)
                error("JSON decimal scale doesn't match - Expected $expected, was ${it.scale()}")
        }
    }

    /**
     * Check the scale of a decimal value as a range.
     *
     * @param   expected        the expected scale as a range
     * @throws  AssertionError  if the scale is incorrect
     */
    fun scale(expected: IntRange): JSONExpect.() -> Unit = {
        nodeAsDecimal.let {
            if (it.scale() !in expected)
                error("JSON decimal scale doesn't match - Expected $expected, was ${it.scale()}")
        }
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

    fun showNode() = when (node) {
        null -> "null"
        is String -> "\"$node\""
        is List<*> -> "[...]"
        is Map<*, *> -> "{...}"
        else -> node.toString()
    }

    private fun errorOnValue(expected: Any?): Nothing {
        error("JSON value doesn't match - Expected $expected, was ${showNode()}")
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

    private fun errorInCollection() {
        error("JSON value not in collection - ${showNode()}")
    }

    private fun errorInRange() {
        error("JSON value not in range - ${showNode()}")
    }

    private fun checkName(name: String): String = name.ifEmpty { error("JSON property name must not be empty") }

    private fun checkIndex(index: Int): Int =
            if (index >= 0) index else error("JSON array index must not be negative - $index")

    private fun getProperty(name: String): Any? = nodeAsObject.let {
        if (!it.containsKey(name))
            error("JSON property missing - $name")
        it[name]
    }

    private fun getItem(index: Int): Any? = nodeAsArray.let {
        if (index !in it.indices)
            error("JSON array index out of bounds - $index")
        it[index]
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
                JSONSimple.parse(json)
            }
            catch (e: Exception) {
                fail("Unable to parse JSON - ${e.message}")
            }
            JSONExpect(obj).tests()
        }

    }

}
