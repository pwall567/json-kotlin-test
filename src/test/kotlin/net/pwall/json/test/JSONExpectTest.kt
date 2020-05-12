/*
 * @(#) JSONExpectTest.kt
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

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

import java.math.BigDecimal

import net.pwall.json.test.JSONExpect.Companion.expectJSON

class JSONExpectTest {

    @Test fun `should fail on invalid JSON`() {
        val json = "["
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 1)
            }
        }
        expect("Unable to parse JSON") { exception.message?.substringBefore(" - ") }
    }

    @Test fun `should test integer value`() {
        val json = "567"
        expectJSON(json) {
            value(567)
        }
    }

    @Test fun `should fail on incorrect test of integer value`() {
        val json = "567"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(568)
            }
        }
        expect("JSON value doesn't match - Expected 568, was 567") { exception.message }
    }

    @Test fun `should test long value`() {
        val json = "123456789123456789"
        expectJSON(json) {
            value(123456789123456789)
        }
    }

    @Test fun `should test string value`() {
        val json = "\"abc\""
        expectJSON(json) {
            value("abc")
        }
    }

    @Test fun `should test boolean value`() {
        val json = "true"
        expectJSON(json) {
            value(true)
        }
    }

    @Test fun `should test null value`() {
        val json = "null"
        expectJSON(json) {
            value(null)
        }
    }

    @Test fun `should test floating point value`() {
        val json = "0.01"
        expectJSON(json) {
            value(BigDecimal("0.01"))
        }
    }

    @Test fun `should test simple int property`() {
        val json = """{"abc":1}"""
        expectJSON(json) {
            property("abc", 1)
        }
    }

    @Test fun `should fail on incorrect test of simple int property`() {
        val json = """{"abc":1}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 2)
            }
        }
        expect("abc: JSON value doesn't match - Expected 2, was 1") { exception.message }
    }

    @Test fun `should test simple long property`() {
        val json = """{"abc":123456789123456789}"""
        expectJSON(json) {
            property("abc", 123456789123456789)
        }
    }

    @Test fun `should test simple floating point property`() {
        val json = """{"abc":0.099}"""
        expectJSON(json) {
            property("abc", BigDecimal("0.099"))
        }
    }

    @Test fun `should test simple boolean property`() {
        val json = """{"abc":true}"""
        expectJSON(json) {
            property("abc", true)
        }
    }

    @Test fun `should test simple int property specified as separate value`() {
        val json = """{"abc":1}"""
        expectJSON(json) {
            property("abc") {
                value(1)
            }
        }
    }

    @Test fun `should fail on incorrect test of simple property specified as separate value`() {
        val json = """{"abc":1}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    value(2)
                }
            }
        }
        expect("abc: JSON value doesn't match - Expected 2, was 1") { exception.message }
    }

    @Test fun `should test multiple properties`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            property("abc", 1)
            property("def", -8)
        }
    }

    @Test fun `should test number of properties`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            count(2)
            property("abc", 1)
            property("def", -8)
        }
    }

    @Test fun `should fail on incorrect test of number of properties`() {
        val json = """{"abc":1,"def":-8}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                count(3)
            }
        }
        expect("JSON count doesn't match - Expected 3, was 2") { exception.message }
    }

    @Test fun `should test number of properties as range`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            count(1..2)
            property("abc", 1)
            property("def", -8)
        }
    }

    @Test fun `should fail on incorrect test of number of properties as range`() {
        val json = """{"abc":1,"def":-8}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                count(3..5)
            }
        }
        expect("JSON count doesn't match - Expected 3..5, was 2") { exception.message }
    }

    @Test fun `should test int array item`() {
        val json = """[12345]"""
        expectJSON(json) {
            item(0, 12345)
        }
    }

    @Test fun `should fail on incorrect test of int array item`() {
        val json = """[12345]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, 12346)
            }
        }
        expect("[0]: JSON value doesn't match - Expected 12346, was 12345") { exception.message }
    }

    @Test fun `should test long array item`() {
        val json = """[123456789123456789]"""
        expectJSON(json) {
            item(0, 123456789123456789)
        }
    }

    @Test fun `should test floating point array item`() {
        val json = """[123.45]"""
        expectJSON(json) {
            item(0, BigDecimal("123.45"))
        }
    }

    @Test fun `should test boolean array item`() {
        val json = """[true]"""
        expectJSON(json) {
            item(0, true)
        }
    }

    @Test fun `should test string array item`() {
        val json = """["Hello!"]"""
        expectJSON(json) {
            item(0, "Hello!")
        }
    }

    @Test fun `should test null array item`() {
        val json = """[null]"""
        expectJSON(json) {
            item(0, null)
        }
    }

    @Test fun `should test multiple int array items`() {
        val json = """[12345,-27]"""
        expectJSON(json) {
            item(0, 12345)
            item(1, -27)
        }
    }

    @Test fun `should fail on incorrect test of multiple int array items`() {
        val json = """[12345,-27]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, 12345)
                item(1, -28)
            }
        }
        expect("[1]: JSON value doesn't match - Expected -28, was -27") { exception.message }
    }

    @Test fun `should test nested int array items`() {
        val json = """[[12345,-27],[44,55]]"""
        expectJSON(json) {
            item(0) {
                item(0, 12345)
                item(1, -27)
            }
            item(1) {
                item(0, 44)
                item(1, 55)
            }
        }
    }

    @Test fun `should fail on incorrect test of nested int array items`() {
        val json = """[[12345,-27],[44,55]]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0) {
                    item(0, 12345)
                    item(1, -27)
                }
                item(1) {
                    item(0, 45)
                    item(1, 55)
                }
            }
        }
        expect("[1][0]: JSON value doesn't match - Expected 45, was 44") { exception.message }
    }

    @Test fun `should test doubly nested int array items`() {
        val json = """[[[12345,-27],[44,55]]]"""
        expectJSON(json) {
            item(0) {
                item(0) {
                    item(0, 12345)
                    item(1, -27)
                }
                item(1) {
                    item(0, 44)
                    item(1, 55)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of doubly nested int array items`() {
        val json = """[[[12345,-27],[44,55]]]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0) {
                    item(0) {
                        item(0, 12345)
                        item(1, -27)
                    }
                    item(1) {
                        item(0, 45)
                        item(1, 55)
                    }
                }
            }
        }
        expect("[0][1][0]: JSON value doesn't match - Expected 45, was 44") { exception.message }
    }

    @Test fun `should test nested property`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                property("field", 99)
            }
        }
    }

    @Test fun `should fail on incorrect test of nested property`() {
        val json = """{"outer":{"field":99}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer") {
                    property("field", 98)
                }
            }
        }
        expect("outer.field: JSON value doesn't match - Expected 98, was 99") { exception.message }
    }

    @Test fun `should test doubly nested property`() {
        val json = """{"outer":{"middle":{"field":99}}}"""
        expectJSON(json) {
            property("outer") {
                property("middle") {
                    property("field", 99)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of doubly nested property`() {
        val json = """{"outer":{"middle":{"field":99}}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer") {
                    property("middle") {
                        property("field", 98)
                    }
                }
            }
        }
        expect("outer.middle.field: JSON value doesn't match - Expected 98, was 99") { exception.message }
    }

    @Test fun `should test property nested in array`() {
        val json = """[{"field":99}]"""
        expectJSON(json) {
            item(0) {
                property("field", 99)
            }
        }
    }

    @Test fun `should fail on incorrect test of property nested in array`() {
        val json = """[{"field":99}]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0) {
                    property("field", 98)
                }
            }
        }
        expect("[0].field: JSON value doesn't match - Expected 98, was 99") { exception.message }
    }

    @Test fun `should test array item nested in property`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        expectJSON(json) {
            property("primes") {
                item(0, 1)
                item(1, 2)
                item(2, 3)
                item(3, 5)
                item(4, 7)
                item(5, 11)
                item(6, 13)
                item(7, 17)
                item(8, 19)
                item(9, 23)
            }
        }
    }

    @Test fun `should fail on test of array item nested in property`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("primes") {
                    item(9, 27)
                }
            }
        }
        expect("primes[9]: JSON value doesn't match - Expected 27, was 23") { exception.message }
    }

    @Test fun `should fail on array index out of bounds`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("primes") {
                    item(10, 27)
                }
            }
        }
        expect("primes: JSON array index out of bounds - 10") { exception.message }
    }

    @Test fun `should test count of array items`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        expectJSON(json) {
            property("primes") {
                count(10)
                item(9, 23)
            }
        }
    }

    @Test fun `should fail on test of count of array items`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("primes") {
                    count(9)
                }
            }
        }
        expect("primes: JSON count doesn't match - Expected 9, was 10") { exception.message }
    }

    @Test fun `should test count of array items as range`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        expectJSON(json) {
            property("primes") {
                count(8..12)
                item(9, 23)
            }
        }
    }

    @Test fun `should fail on test of count of array items as range`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("primes") {
                    count(2..9)
                }
            }
        }
        expect("primes: JSON count doesn't match - Expected 2..9, was 10") { exception.message }
    }

    @Test fun `should fail on test of missing property`() {
        val json = """{"abc":1,"def":-8}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 1)
                property("ghi", 9)
            }
        }
        expect("JSON property missing - ghi") { exception.message }
    }

    @Test fun `should test for property absent`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            propertyAbsent("ghi")
        }
    }

    @Test fun `should test for property absent or null 1`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            propertyAbsentOrNull("ghi")
        }
    }

    @Test fun `should test for property absent or null 2`() {
        val json = """{"abc":1,"def":-8,"ghi":null}"""
        expectJSON(json) {
            propertyAbsentOrNull("ghi")
        }
    }

    @Test fun `should test for property present`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            propertyPresent("def")
        }
    }

    @Test fun `should test for property non-null`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            property("def", nonNull)
        }
    }

    @Test fun `should fail on incorrect test for property absent`() {
        val json = """{"abc":1,"def":-8}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                propertyAbsent("def")
            }
        }
        expect("JSON property not absent - def") { exception.message }
    }

    @Test fun `should fail on incorrect test for property absent or null`() {
        val json = """{"abc":1,"def":-8}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                propertyAbsentOrNull("def")
            }
        }
        expect("JSON property not absent or null - def") { exception.message }
    }

    @Test fun `should fail on incorrect test for property present`() {
        val json = """{"abc":1,"def":-8}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                propertyPresent("ghi")
            }
        }
        expect("JSON property not present - ghi") { exception.message }
    }

    @Test fun `should fail on incorrect test for property non-null 1`() {
        val json = """{"abc":1,"def":-8}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("ghi", nonNull)
            }
        }
        expect("JSON property missing - ghi") { exception.message }
    }

    @Test fun `should fail on incorrect test for property non-null 2`() {
        val json = """{"abc":1,"def":-8,"ghi":null}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("ghi", nonNull)
            }
        }
        expect("ghi: JSON item is null") { exception.message }
    }

    @Test fun `should test for nested property absent`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                propertyAbsent("missing")
            }
        }
    }

    @Test fun `should test for nested property absent or null`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                propertyAbsentOrNull("missing")
            }
        }
    }

    @Test fun `should test for nested property present`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                propertyPresent("field")
            }
        }
    }

    @Test fun `should test for nested property non-null`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                property("field", nonNull)
            }
        }
    }

    @Test fun `should fail on incorrect test for nested property absent`() {
        val json = """{"outer":{"field":99}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer") {
                    propertyAbsent("field")
                }
            }
        }
        expect("outer: JSON property not absent - field") { exception.message }
    }

    @Test fun `should fail on incorrect test for nested property absent or null`() {
        val json = """{"outer":{"field":99}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer") {
                    propertyAbsentOrNull("field")
                }
            }
        }
        expect("outer: JSON property not absent or null - field") { exception.message }
    }

    @Test fun `should fail on incorrect test for nested property present`() {
        val json = """{"outer":{"field":99}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer") {
                    propertyPresent("missing")
                }
            }
        }
        expect("outer: JSON property not present - missing") { exception.message }
    }

    @Test fun `should fail on incorrect test for nested property non-null 1`() {
        val json = """{"outer":{"field":99}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer") {
                    property("other", nonNull)
                }
            }
        }
        expect("outer: JSON property missing - other") { exception.message }
    }

    @Test fun `should fail on incorrect test for nested property non-null 2`() {
        val json = """{"outer":{"field":99,"other":null}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer") {
                    property("other", nonNull)
                }
            }
        }
        expect("outer.other: JSON item is null") { exception.message }
    }

    @Test fun `should fail when comparing object as value`() {
        val json = """{"outer":{"field":99}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer", """{"field":99}""")
            }
        }
        expect("outer: JSON type doesn't match - Expected string, was object") { exception.message }
    }

    @Test fun `should quote strings in match error message`() {
        val json = """["un","deux","trois"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(1, "un")
            }
        }
        expect("""[1]: JSON value doesn't match - Expected "un", was "deux"""") { exception.message }
    }

    @Test fun `should fail when comparing null to object`() {
        val json = """{"outer":{"field":99}}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer", null)
            }
        }
        expect("outer: JSON type doesn't match - Expected null, was object") { exception.message }
    }

    @Test fun `should fail when comparing null to array`() {
        val json = """{"outer":[1,2,3]}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("outer", null)
            }
        }
        expect("outer: JSON type doesn't match - Expected null, was array") { exception.message }
    }

    @Test fun `should fail when comparing null to string`() {
        val json = """{"abc":"Hello"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", null)
            }
        }
        expect("abc: JSON type doesn't match - Expected null, was string") { exception.message }
    }

    @Test fun `should fail when comparing null to integer`() {
        val json = """{"abc":123}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", null)
            }
        }
        expect("abc: JSON type doesn't match - Expected null, was integer") { exception.message }
    }

    @Test fun `should fail when comparing null to long`() {
        val json = """{"abc":123456789123456789}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", null)
            }
        }
        expect("abc: JSON type doesn't match - Expected null, was long integer") { exception.message }
    }

    @Test fun `should fail when comparing string to null`() {
        val json = """{"abc":null}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", "Hello")
            }
        }
        expect("abc: JSON type doesn't match - Expected string, was null") { exception.message }
    }

    @Test fun `should fail when comparing integer to null`() {
        val json = """{"abc":null}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 123)
            }
        }
        expect("abc: JSON type doesn't match - Expected integer, was null") { exception.message }
    }

    @Test fun `should fail when comparing integer to string`() {
        val json = """{"abc":"Hello"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 123)
            }
        }
        expect("abc: JSON type doesn't match - Expected integer, was string") { exception.message }
    }

    @Test fun `should fail when comparing string to integer`() {
        val json = """{"abc":123}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", "Hello")
            }
        }
        expect("abc: JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test int value as member of a collection`() {
        val json = "123"
        expectJSON(json) {
            value(setOf(123, 456, 789))
        }
    }

    @Test fun `should fail on incorrect test of int value as member of a collection`() {
        val json = "124"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf(123, 456, 789))
            }
        }
        expect("JSON value not in collection - 124") { exception.message }
    }

    @Test fun `should test null as member of a collection of int`() {
        val json = "null"
        expectJSON(json) {
            value(setOf(123, 456, 789, null))
        }
    }

    @Test fun `should fail on incorrect test of null as member of a collection of int`() {
        val json = "null"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf(123, 456, 789))
            }
        }
        expect("JSON value not in collection - null") { exception.message }
    }

    @Test fun `should test long value as member of a collection`() {
        val json = "123456789123456789"
        expectJSON(json) {
            value(setOf(123456789123456789, 0L))
        }
    }

    @Test fun `should fail on incorrect test of long value as member of a collection`() {
        val json = "123456789123456788"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf(123456789123456789, 0L))
            }
        }
        expect("JSON value not in collection - 123456789123456788") { exception.message }
    }

    @Test fun `should test null as member of a collection of long`() {
        val json = "null"
        expectJSON(json) {
            value(setOf(123456789123456789, 0L, null))
        }
    }

    @Test fun `should fail on incorrect test of null as member of a collection of long`() {
        val json = "null"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf(123456789123456789, 0L))
            }
        }
        expect("JSON value not in collection - null") { exception.message }
    }

    @Test fun `should test decimal value as member of a collection`() {
        val json = "9.99"
        expectJSON(json) {
            value(setOf(BigDecimal("9.99"), BigDecimal("19.99")))
        }
    }

    @Test fun `should fail on incorrect test of decimal value as member of a collection`() {
        val json = "29.99"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf(BigDecimal("9.99"), BigDecimal("19.99")))
            }
        }
        expect("JSON value not in collection - 29.99") { exception.message }
    }

    @Test fun `should test null as member of a collection of decimal`() {
        val json = "null"
        expectJSON(json) {
            value(setOf(BigDecimal("9.99"), BigDecimal("19.99"), null))
        }
    }

    @Test fun `should fail on incorrect test of null as member of a collection of decimal`() {
        val json = "null"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf(BigDecimal("9.99"), BigDecimal("19.99")))
            }
        }
        expect("JSON value not in collection - null") { exception.message }
    }

    @Test fun `should test string as member of a collection`() {
        val json = "\"abc\""
        expectJSON(json) {
            value(setOf("abc", "def", "ghi"))
        }
    }

    @Test fun `should fail on incorrect test of string as member of a collection`() {
        val json = "\"abcd\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf("abc", "def", "ghi"))
            }
        }
        expect("JSON value not in collection - \"abcd\"") { exception.message }
    }

    @Test fun `should test null as member of a collection of string`() {
        val json = "null"
        expectJSON(json) {
            value(setOf("abc", "def", "ghi", null))
        }
    }

    @Test fun `should fail on incorrect test of null as member of a collection of string`() {
        val json = "null"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf("abc", "def", "ghi"))
            }
        }
        expect("JSON value not in collection - null") { exception.message }
    }

    @Test fun `should test int property as member of a collection`() {
        val json = """{"abc":123}"""
        expectJSON(json) {
            property("abc", setOf(123, 456, 789))
        }
    }

    @Test fun `should fail on incorrect test of int property as member of a collection`() {
        val json = """{"abc":124}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", setOf(123, 456, 789))
            }
        }
        expect("abc: JSON value not in collection - 124") { exception.message }
    }

    @Test fun `should test null property as member of a collection of int`() {
        val json = """{"abc":null}"""
        expectJSON(json) {
            property("abc", setOf(123, 456, 789, null))
        }
    }

    @Test fun `should fail on incorrect test of null property as member of a collection of int`() {
        val json = """{"abc":null}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", setOf(123, 456, 789))
            }
        }
        expect("abc: JSON value not in collection - null") { exception.message }
    }

    @Test fun `should test string property as member of a collection`() {
        val json = """{"prop":"abc"}"""
        expectJSON(json) {
            property("prop", setOf("abc", "def", "ghi"))
        }
    }

    @Test fun `should fail on incorrect test of string property as member of a collection`() {
        val json = """{"prop":"abcd"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("prop", setOf("abc", "def", "ghi"))
            }
        }
        expect("prop: JSON value not in collection - \"abcd\"") { exception.message }
    }

    @Test fun `should test null property as member of a collection of string`() {
        val json = """{"prop":null}"""
        expectJSON(json) {
            property("prop", setOf("abc", "def", "ghi", null))
        }
    }

    @Test fun `should fail on incorrect test of null property as member of a collection of string`() {
        val json = """{"prop":null}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("prop", setOf("abc", "def", "ghi"))
            }
        }
        expect("prop: JSON value not in collection - null") { exception.message }
    }

    @Test fun `should test int array item as member of a collection`() {
        val json = "[123]"
        expectJSON(json) {
            item(0, setOf(123, 456, 789))
        }
    }

    @Test fun `should fail on incorrect test of int array item as member of a collection`() {
        val json = "[124]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, setOf(123, 456, 789))
            }
        }
        expect("[0]: JSON value not in collection - 124") { exception.message }
    }

    @Test fun `should test null array item as member of a collection of int`() {
        val json = "[null]"
        expectJSON(json) {
            item(0, setOf(123, 456, 789, null))
        }
    }

    @Test fun `should fail on incorrect test of null array item as member of a collection of int`() {
        val json = "[null]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, setOf(123, 456, 789))
            }
        }
        expect("[0]: JSON value not in collection - null") { exception.message }
    }

    @Test fun `should test string array item as member of a collection`() {
        val json = """["abc"]"""
        expectJSON(json) {
            item(0, setOf("abc", "def", "ghi"))
        }
    }

    @Test fun `should fail on incorrect test of string array item as member of a collection`() {
        val json = """["abcd"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, setOf("abc", "def", "ghi"))
            }
        }
        expect("[0]: JSON value not in collection - \"abcd\"") { exception.message }
    }

    @Test fun `should test null array item as member of a collection of string`() {
        val json = "[null]"
        expectJSON(json) {
            item(0, setOf("abc", "def", "ghi", null))
        }
    }

    @Test fun `should fail on incorrect test of null array item as member of a collection of string`() {
        val json = "[null]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, setOf("abc", "def", "ghi"))
            }
        }
        expect("[0]: JSON value not in collection - null") { exception.message }
    }

    @Test fun `should test int value as member of a range`() {
        val json = "123"
        expectJSON(json) {
            value(120..125)
        }
    }

    @Test fun `should fail on incorrect test of int value as member of a range`() {
        val json = "126"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(120..125)
            }
        }
        expect("JSON value doesn't match - Expected 120..125, was 126") { exception.message }
    }

    @Test fun `should test int property as member of a range`() {
        val json = """{"abc":123}"""
        expectJSON(json) {
            property("abc", 120..125)
        }
    }

    @Test fun `should fail on incorrect test of int property as member of a range`() {
        val json = """{"abc":126}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 120..125)
            }
        }
        expect("abc: JSON value doesn't match - Expected 120..125, was 126") { exception.message }
    }

    @Test fun `should test int array item as member of a range`() {
        val json = "[123]"
        expectJSON(json) {
            item(0, 120..125)
        }
    }

    @Test fun `should fail on incorrect test of int array item as member of a range`() {
        val json = "[126]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, 120..125)
            }
        }
        expect("[0]: JSON value doesn't match - Expected 120..125, was 126") { exception.message }
    }

    @Test fun `should test long value as member of a range`() {
        val json = "123456789123456789"
        expectJSON(json) {
            value(123456789123456780..123456789123456789)
        }
    }

    @Test fun `should fail on incorrect test of long value as member of a range`() {
        val json = "123456789123456790"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(0..123456789123456789)
            }
        }
        expect("JSON value doesn't match - Expected 0..123456789123456789, was 123456789123456790") {
            exception.message
        }
    }

    @Test fun `should test long property as member of a range`() {
        val json = """{"abc":123456789123456789}"""
        expectJSON(json) {
            property("abc", 123456789123456780..123456789123456789)
        }
    }

    @Test fun `should fail on incorrect test of long property as member of a range`() {
        val json = """{"abc":123456789123456790}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 0..123456789123456789)
            }
        }
        expect("abc: JSON value doesn't match - Expected 0..123456789123456789, was 123456789123456790") {
            exception.message
        }
    }

    @Test fun `should test long array item as member of a range`() {
        val json = "[123456789123456789]"
        expectJSON(json) {
            item(0, 123456789123456780..123456789123456789)
        }
    }

    @Test fun `should fail on incorrect test of long array item as member of a range`() {
        val json = "[123456789123456790]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, 0..123456789123456789)
            }
        }
        expect("[0]: JSON value doesn't match - Expected 0..123456789123456789, was 123456789123456790") {
            exception.message
        }
    }

    @Test fun `should test string value as member of a range`() {
        val json = "\"abcde\""
        expectJSON(json) {
            value("abcda".."abcdz")
        }
    }

    @Test fun `should fail on incorrect test of string value as member of a range`() {
        val json = "\"abcde\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value("abcdg".."abcdz")
            }
        }
        expect("JSON value not in range - \"abcde\"") { exception.message }
    }

    @Test fun `should test string property as member of a range`() {
        val json = """{"prop":"abcde"}"""
        expectJSON(json) {
            property("prop", "abcda".."abcdz")
        }
    }

    @Test fun `should fail on incorrect test of string property as member of a range`() {
        val json = """{"prop":"abcde"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("prop", "abcdg".."abcdz")
            }
        }
        expect("prop: JSON value not in range - \"abcde\"") { exception.message }
    }

    @Test fun `should test string array item as member of a range`() {
        val json = "[\"abcde\"]"
        expectJSON(json) {
            item(0, "abcda".."abcdz")
        }
    }

    @Test fun `should fail on incorrect test of string array item as member of a range`() {
        val json = """["abcde"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, "abcdg".."abcdz")
            }
        }
        expect("[0]: JSON value not in range - \"abcde\"") { exception.message }
    }

    @Test fun `should test decimal value as member of a range`() {
        val json = "27.25"
        expectJSON(json) {
            value(BigDecimal("27.00")..BigDecimal("27.50"))
        }
    }

    @Test fun `should fail on incorrect test of decimal value as member of a range`() {
        val json = "27.25"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(BigDecimal("27.50")..BigDecimal("27.99"))
            }
        }
        expect("JSON value not in range - 27.25") { exception.message }
    }

    @Test fun `should test decimal property as member of a range`() {
        val json = """{"prop":27.25}"""
        expectJSON(json) {
            property("prop", BigDecimal("27.00")..BigDecimal("27.50"))
        }
    }

    @Test fun `should fail on incorrect test of decimal property as member of a range`() {
        val json = """{"prop":27.25}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("prop", BigDecimal("27.50")..BigDecimal("27.99"))
            }
        }
        expect("prop: JSON value not in range - 27.25") { exception.message }
    }

    @Test fun `should test decimal array item as member of a range`() {
        val json = "[27.25]"
        expectJSON(json) {
            item(0, BigDecimal("27.00")..BigDecimal("27.50"))
        }
    }

    @Test fun `should fail on incorrect test of decimal array item as member of a range`() {
        val json = "[27.25]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, BigDecimal("27.50")..BigDecimal("27.99"))
            }
        }
        expect("[0]: JSON value not in range - 27.25") { exception.message }
    }

    @Test fun `should fail on test for value as member of a collection of other class`() {
        val json = "\"C\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(setOf('C'))
            }
        }
        expect("Can't perform test using collection of class kotlin.Char") { exception.message }
    }

    @Test fun `should fail on test for property as member of a collection of other class`() {
        val json = """{"abc":"C"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", setOf('C'))
            }
        }
        expect("abc: Can't perform test using collection of class kotlin.Char") { exception.message }
    }

    @Test fun `should fail on test for array item as member of a collection of other class`() {
        val json = """["C"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, setOf('C'))
            }
        }
        expect("[0]: Can't perform test using collection of class kotlin.Char") { exception.message }
    }

    @Test fun `should test that value is a string`() {
        val json = "\"I am a string\""
        expectJSON(json) {
            value(string)
        }
    }

    @Test fun `should fail on incorrect test that value is a string`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(string)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test that property is a string`() {
        val json = """{"abc":"I am a string"}"""
        expectJSON(json) {
            property("abc", string)
        }
    }

    @Test fun `should fail on incorrect test that property is a string`() {
        val json = """{"abc":12345}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", string)
            }
        }
        expect("abc: JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test that array item is a string`() {
        val json = """["I am a string"]"""
        expectJSON(json) {
            item(0, string)
        }
    }

    @Test fun `should fail on incorrect test that array item is a string`() {
        val json = """[12345]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, string)
            }
        }
        expect("[0]: JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test that value is an integer`() {
        val json = "12345"
        expectJSON(json) {
            value(integer)
        }
    }

    @Test fun `should fail on incorrect test that value is an integer`() {
        val json = "\"I am a string\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(integer)
            }
        }
        expect("JSON type doesn't match - Expected integer, was string") { exception.message }
    }

    @Test fun `should test that property is an integer`() {
        val json = """{"abc":12345}"""
        expectJSON(json) {
            property("abc", integer)
        }
    }

    @Test fun `should fail on incorrect test that property is an integer`() {
        val json = """{"abc":"I am a string"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", integer)
            }
        }
        expect("abc: JSON type doesn't match - Expected integer, was string") { exception.message }
    }

    @Test fun `should test that array item is an integer`() {
        val json = """[12345]"""
        expectJSON(json) {
            item(0, integer)
        }
    }

    @Test fun `should fail on incorrect test that array item is an integer`() {
        val json = """["I am a string"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, integer)
            }
        }
        expect("[0]: JSON type doesn't match - Expected integer, was string") { exception.message }
    }

    @Test fun `should test that value is a long integer`() {
        val json = "123456789123456789"
        expectJSON(json) {
            value(longInteger)
        }
    }

    @Test fun `should fail on incorrect test that value is a long integer`() {
        val json = "\"I am a string\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(longInteger)
            }
        }
        expect("JSON type doesn't match - Expected long integer, was string") { exception.message }
    }

    @Test fun `should test that property is a long integer`() {
        val json = """{"abc":123456789123456789}"""
        expectJSON(json) {
            property("abc", longInteger)
        }
    }

    @Test fun `should fail on incorrect test that property is a long integer`() {
        val json = """{"abc":"I am a string"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", longInteger)
            }
        }
        expect("abc: JSON type doesn't match - Expected long integer, was string") { exception.message }
    }

    @Test fun `should test that array item is a long integer`() {
        val json = """[123456789123456789]"""
        expectJSON(json) {
            item(0, longInteger)
        }
    }

    @Test fun `should fail on incorrect test that array item is a long integer`() {
        val json = """["I am a string"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, longInteger)
            }
        }
        expect("[0]: JSON type doesn't match - Expected long integer, was string") { exception.message }
    }

    @Test fun `should test that value is a decimal`() {
        val json = "0.5"
        expectJSON(json) {
            value(decimal)
        }
    }

    @Test fun `should fail on incorrect test that value is a decimal`() {
        val json = "\"I am a string\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(decimal)
            }
        }
        expect("JSON type doesn't match - Expected decimal, was string") { exception.message }
    }

    @Test fun `should test that property is a decimal`() {
        val json = """{"abc":0.5}"""
        expectJSON(json) {
            property("abc", decimal)
        }
    }

    @Test fun `should fail on incorrect test that property is a decimal`() {
        val json = """{"abc":"I am a string"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", decimal)
            }
        }
        expect("abc: JSON type doesn't match - Expected decimal, was string") { exception.message }
    }

    @Test fun `should test that array item is a decimal`() {
        val json = """[0.5]"""
        expectJSON(json) {
            item(0, decimal)
        }
    }

    @Test fun `should fail on incorrect test that array item is a decimal`() {
        val json = """["I am a string"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, decimal)
            }
        }
        expect("[0]: JSON type doesn't match - Expected decimal, was string") { exception.message }
    }

    @Test fun `should test that value is a boolean`() {
        val json = "true"
        expectJSON(json) {
            value(boolean)
        }
    }

    @Test fun `should fail on incorrect test that value is a boolean`() {
        val json = "\"I am a string\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(boolean)
            }
        }
        expect("JSON type doesn't match - Expected boolean, was string") { exception.message }
    }

    @Test fun `should test that property is a boolean`() {
        val json = """{"abc":true}"""
        expectJSON(json) {
            property("abc", boolean)
        }
    }

    @Test fun `should fail on incorrect test that property is a boolean`() {
        val json = """{"abc":"I am a string"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", boolean)
            }
        }
        expect("abc: JSON type doesn't match - Expected boolean, was string") { exception.message }
    }

    @Test fun `should test that array item is a boolean`() {
        val json = """[true]"""
        expectJSON(json) {
            item(0, boolean)
        }
    }

    @Test fun `should fail on incorrect test that array item is a boolean`() {
        val json = """["I am a string"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, boolean)
            }
        }
        expect("[0]: JSON type doesn't match - Expected boolean, was string") { exception.message }
    }

    @Test fun `should test string value as UUID`() {
        val json = "\"f347ab96-7f62-11ea-ba4e-27278a06d491\""
        expectJSON(json) {
            value(uuid)
        }
    }

    @Test fun `should fail on incorrect test of string value as UUID`() {
        val json = "\"not a UUID\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(uuid)
            }
        }
        expect("JSON string is not a UUID - \"not a UUID\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as UUID`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(uuid)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as UUID`() {
        val json = """{"abc":"f347ab96-7f62-11ea-ba4e-27278a06d491"}"""
        expectJSON(json) {
            property("abc", uuid)
        }
    }

    @Test fun `should fail on incorrect test of string property as UUID`() {
        val json = """{"abc":"not a UUID"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", uuid)
            }
        }
        expect("abc: JSON string is not a UUID - \"not a UUID\"") { exception.message }
    }

    @Test fun `should test string array item as UUID`() {
        val json = """["f347ab96-7f62-11ea-ba4e-27278a06d491"]"""
        expectJSON(json) {
            item(0, uuid)
        }
    }

    @Test fun `should fail on incorrect test of string array item as UUID`() {
        val json = """["not a UUID"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, uuid)
            }
        }
        expect("[0]: JSON string is not a UUID - \"not a UUID\"") { exception.message }
    }

    @Test fun `should test string value as LocalDate`() {
        val json = "\"2020-04-16\""
        expectJSON(json) {
            value(localDate)
        }
    }

    @Test fun `should fail on incorrect test of string value as LocalDate`() {
        val json = "\"not a LocalDate\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(localDate)
            }
        }
        expect("JSON string is not a LocalDate - \"not a LocalDate\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as LocalDate`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(localDate)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as LocalDate`() {
        val json = """{"abc":"2020-04-16"}"""
        expectJSON(json) {
            property("abc", localDate)
        }
    }

    @Test fun `should fail on incorrect test of string property as LocalDate`() {
        val json = """{"abc":"not a LocalDate"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", localDate)
            }
        }
        expect("abc: JSON string is not a LocalDate - \"not a LocalDate\"") { exception.message }
    }

    @Test fun `should test string array item as LocalDate`() {
        val json = """["2020-04-16"]"""
        expectJSON(json) {
            item(0, localDate)
        }
    }

    @Test fun `should fail on incorrect test of string array item as LocalDate`() {
        val json = """["not a LocalDate"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, localDate)
            }
        }
        expect("[0]: JSON string is not a LocalDate - \"not a LocalDate\"") { exception.message }
    }

    @Test fun `should test string value as LocalDateTime`() {
        val json = "\"2020-04-16T18:31:19.123\""
        expectJSON(json) {
            value(localDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as LocalDateTime`() {
        val json = "\"not a LocalDateTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(localDateTime)
            }
        }
        expect("JSON string is not a LocalDateTime - \"not a LocalDateTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as LocalDateTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(localDateTime)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as LocalDateTime`() {
        val json = """{"abc":"2020-04-16T18:31:19.123"}"""
        expectJSON(json) {
            property("abc", localDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as LocalDateTime`() {
        val json = """{"abc":"not a LocalDateTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", localDateTime)
            }
        }
        expect("abc: JSON string is not a LocalDateTime - \"not a LocalDateTime\"") { exception.message }
    }

    @Test fun `should test string array item as LocalDateTime`() {
        val json = """["2020-04-16T18:31:19.123"]"""
        expectJSON(json) {
            item(0, localDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as LocalDateTime`() {
        val json = """["not a LocalDateTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, localDateTime)
            }
        }
        expect("[0]: JSON string is not a LocalDateTime - \"not a LocalDateTime\"") { exception.message }
    }

    @Test fun `should test string value as LocalTime`() {
        val json = "\"18:31:19.123\""
        expectJSON(json) {
            value(localTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as LocalTime`() {
        val json = "\"not a LocalTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(localTime)
            }
        }
        expect("JSON string is not a LocalTime - \"not a LocalTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as LocalTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(localTime)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as LocalTime`() {
        val json = """{"abc":"18:31:19.123"}"""
        expectJSON(json) {
            property("abc", localTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as LocalTime`() {
        val json = """{"abc":"not a LocalTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", localTime)
            }
        }
        expect("abc: JSON string is not a LocalTime - \"not a LocalTime\"") { exception.message }
    }

    @Test fun `should test string array item as LocalTime`() {
        val json = """["18:31:19.123"]"""
        expectJSON(json) {
            item(0, localTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as LocalTime`() {
        val json = """["not a LocalTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, localTime)
            }
        }
        expect("[0]: JSON string is not a LocalTime - \"not a LocalTime\"") { exception.message }
    }

    @Test fun `should test string value as OffsetDateTime`() {
        val json = "\"2020-04-16T18:31:19.123+10:00\""
        expectJSON(json) {
            value(offsetDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as OffsetDateTime`() {
        val json = "\"not a OffsetDateTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(offsetDateTime)
            }
        }
        expect("JSON string is not a OffsetDateTime - \"not a OffsetDateTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as OffsetDateTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(offsetDateTime)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as OffsetDateTime`() {
        val json = """{"abc":"2020-04-16T18:31:19.123+10:00"}"""
        expectJSON(json) {
            property("abc", offsetDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as OffsetDateTime`() {
        val json = """{"abc":"not a OffsetDateTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", offsetDateTime)
            }
        }
        expect("abc: JSON string is not a OffsetDateTime - \"not a OffsetDateTime\"") { exception.message }
    }

    @Test fun `should test string array item as OffsetDateTime`() {
        val json = """["2020-04-16T18:31:19.123+10:00"]"""
        expectJSON(json) {
            item(0, offsetDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as OffsetDateTime`() {
        val json = """["not a OffsetDateTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, offsetDateTime)
            }
        }
        expect("[0]: JSON string is not a OffsetDateTime - \"not a OffsetDateTime\"") { exception.message }
    }

    @Test fun `should test string value as OffsetTime`() {
        val json = "\"18:31:19.123+10:00\""
        expectJSON(json) {
            value(offsetTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as OffsetTime`() {
        val json = "\"not a OffsetTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(offsetTime)
            }
        }
        expect("JSON string is not a OffsetTime - \"not a OffsetTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as OffsetTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(offsetTime)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as OffsetTime`() {
        val json = """{"abc":"18:31:19.123+10:00"}"""
        expectJSON(json) {
            property("abc", offsetTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as OffsetTime`() {
        val json = """{"abc":"not a OffsetTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", offsetTime)
            }
        }
        expect("abc: JSON string is not a OffsetTime - \"not a OffsetTime\"") { exception.message }
    }

    @Test fun `should test string array item as OffsetTime`() {
        val json = """["18:31:19.123+10:00"]"""
        expectJSON(json) {
            item(0, offsetTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as OffsetTime`() {
        val json = """["not a OffsetTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, offsetTime)
            }
        }
        expect("[0]: JSON string is not a OffsetTime - \"not a OffsetTime\"") { exception.message }
    }

    @Test fun `should test string value as ZonedDateTime`() {
        val json = "\"2020-04-16T18:31:19.123+10:00[Australia/Sydney]\""
        expectJSON(json) {
            value(zonedDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as ZonedDateTime`() {
        val json = "\"not a ZonedDateTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(zonedDateTime)
            }
        }
        expect("JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as ZonedDateTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(zonedDateTime)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as ZonedDateTime`() {
        val json = """{"abc":"2020-04-16T18:31:19.123+10:00[Australia/Sydney]"}"""
        expectJSON(json) {
            property("abc", zonedDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as ZonedDateTime`() {
        val json = """{"abc":"not a ZonedDateTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", zonedDateTime)
            }
        }
        expect("abc: JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") { exception.message }
    }

    @Test fun `should test string array item as ZonedDateTime`() {
        val json = """["2020-04-16T18:31:19.123+10:00[Australia/Sydney]"]"""
        expectJSON(json) {
            item(0, zonedDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as ZonedDateTime`() {
        val json = """["not a ZonedDateTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, zonedDateTime)
            }
        }
        expect("[0]: JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") { exception.message }
    }

    @Test fun `should test string value as YearMonth`() {
        val json = "\"2020-04\""
        expectJSON(json) {
            value(yearMonth)
        }
    }

    @Test fun `should fail on incorrect test of string value as YearMonth`() {
        val json = "\"not a YearMonth\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(yearMonth)
            }
        }
        expect("JSON string is not a YearMonth - \"not a YearMonth\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as YearMonth`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(yearMonth)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as YearMonth`() {
        val json = """{"abc":"2020-04"}"""
        expectJSON(json) {
            property("abc", yearMonth)
        }
    }

    @Test fun `should fail on incorrect test of string property as YearMonth`() {
        val json = """{"abc":"not a YearMonth"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", yearMonth)
            }
        }
        expect("abc: JSON string is not a YearMonth - \"not a YearMonth\"") { exception.message }
    }

    @Test fun `should test string array item as YearMonth`() {
        val json = """["2020-04"]"""
        expectJSON(json) {
            item(0, yearMonth)
        }
    }

    @Test fun `should fail on incorrect test of string array item as YearMonth`() {
        val json = """["not a YearMonth"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, yearMonth)
            }
        }
        expect("[0]: JSON string is not a YearMonth - \"not a YearMonth\"") { exception.message }
    }

    @Test fun `should test string value as MonthDay`() {
        val json = "\"--04-30\""
        expectJSON(json) {
            value(monthDay)
        }
    }

    @Test fun `should fail on incorrect test of string value as MonthDay`() {
        val json = "\"not a MonthDay\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(monthDay)
            }
        }
        expect("JSON string is not a MonthDay - \"not a MonthDay\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as MonthDay`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(monthDay)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as MonthDay`() {
        val json = """{"abc":"--04-30"}"""
        expectJSON(json) {
            property("abc", monthDay)
        }
    }

    @Test fun `should fail on incorrect test of string property as MonthDay`() {
        val json = """{"abc":"not a MonthDay"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", monthDay)
            }
        }
        expect("abc: JSON string is not a MonthDay - \"not a MonthDay\"") { exception.message }
    }

    @Test fun `should test string array item as MonthDay`() {
        val json = """["--04-30"]"""
        expectJSON(json) {
            item(0, monthDay)
        }
    }

    @Test fun `should fail on incorrect test of string array item as MonthDay`() {
        val json = """["not a MonthDay"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, monthDay)
            }
        }
        expect("[0]: JSON string is not a MonthDay - \"not a MonthDay\"") { exception.message }
    }

    @Test fun `should test string value as Year`() {
        val json = "\"2020\""
        expectJSON(json) {
            value(year)
        }
    }

    @Test fun `should fail on incorrect test of string value as Year`() {
        val json = "\"not a Year\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(year)
            }
        }
        expect("JSON string is not a Year - \"not a Year\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as Year`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(year)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as Year`() {
        val json = """{"abc":"2020"}"""
        expectJSON(json) {
            property("abc", year)
        }
    }

    @Test fun `should fail on incorrect test of string property as Year`() {
        val json = """{"abc":"not a Year"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", year)
            }
        }
        expect("abc: JSON string is not a Year - \"not a Year\"") { exception.message }
    }

    @Test fun `should test string array item as Year`() {
        val json = """["2020"]"""
        expectJSON(json) {
            item(0, year)
        }
    }

    @Test fun `should fail on incorrect test of string array item as Year`() {
        val json = """["not a Year"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, year)
            }
        }
        expect("[0]: JSON string is not a Year - \"not a Year\"") { exception.message }
    }

    @Test fun `should test string value as Duration`() {
        val json = "\"PT2H\""
        expectJSON(json) {
            value(duration)
        }
    }

    @Test fun `should fail on incorrect test of string value as Duration`() {
        val json = "\"not a Duration\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(duration)
            }
        }
        expect("JSON string is not a Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as Duration`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(duration)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as Duration`() {
        val json = """{"abc":"PT2H"}"""
        expectJSON(json) {
            property("abc", duration)
        }
    }

    @Test fun `should fail on incorrect test of string property as Duration`() {
        val json = """{"abc":"not a Duration"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", duration)
            }
        }
        expect("abc: JSON string is not a Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should test string array item as Duration`() {
        val json = """["PT2H"]"""
        expectJSON(json) {
            item(0, duration)
        }
    }

    @Test fun `should fail on incorrect test of string array item as Duration`() {
        val json = """["not a Duration"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, duration)
            }
        }
        expect("[0]: JSON string is not a Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should test string value as Period`() {
        val json = "\"P3M\""
        expectJSON(json) {
            value(period)
        }
    }

    @Test fun `should fail on incorrect test of string value as Period`() {
        val json = "\"not a Period\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(period)
            }
        }
        expect("JSON string is not a Period - \"not a Period\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as Period`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(period)
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as Period`() {
        val json = """{"abc":"P3M"}"""
        expectJSON(json) {
            property("abc", period)
        }
    }

    @Test fun `should fail on incorrect test of string property as Period`() {
        val json = """{"abc":"not a Period"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", period)
            }
        }
        expect("abc: JSON string is not a Period - \"not a Period\"") { exception.message }
    }

    @Test fun `should test string array item as Period`() {
        val json = """["P3M"]"""
        expectJSON(json) {
            item(0, period)
        }
    }

    @Test fun `should fail on incorrect test of string array item as Period`() {
        val json = """["not a Period"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, period)
            }
        }
        expect("[0]: JSON string is not a Period - \"not a Period\"") { exception.message }
    }

    @Test fun `should test string value against regex`() {
        val json = "\"abc\""
        expectJSON(json) {
            value(Regex("^[a-z]+$"))
        }
    }

    @Test fun `should fail on incorrect test of string value against regex`() {
        val json = "\"abc1\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(Regex("^[a-z]+$"))
            }
        }
        expect("JSON string doesn't match regex - Expected ^[a-z]+\$, was \"abc1\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value against regex`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(Regex("^[a-z]+$"))
            }
        }
        expect("JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should test string property against regex`() {
        val json = """{"prop":"abc"}"""
        expectJSON(json) {
            property("prop", Regex("^[a-z]+$"))
        }
    }

    @Test fun `should fail on incorrect test of string property against regex`() {
        val json = """{"prop":"abc1"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("prop", Regex("^[a-z]+$"))
            }
        }
        expect("prop: JSON string doesn't match regex - Expected ^[a-z]+\$, was \"abc1\"") { exception.message }
    }

    @Test fun `should test string array item against regex`() {
        val json = """["abc"]"""
        expectJSON(json) {
            item(0, Regex("^[a-z]+$"))
        }
    }

    @Test fun `should fail on incorrect test of string array item against regex`() {
        val json = """["abc1"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, Regex("^[a-z]+$"))
            }
        }
        expect("[0]: JSON string doesn't match regex - Expected ^[a-z]+\$, was \"abc1\"") { exception.message }
    }

    @Test fun `should test string value length`() {
        val json = "\"Hello!\""
        expectJSON(json) {
            value(length(6))
        }
    }

    @Test fun `should fail on incorrect test of string value length`() {
        val json = "\"Hello!\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(length(5))
            }
        }
        expect("JSON string length doesn't match - Expected 5, was 6") { exception.message }
    }

    @Test fun `should test string value length as a range`() {
        val json = "\"Hello!\""
        expectJSON(json) {
            value(length(4..10))
        }
    }

    @Test fun `should fail on incorrect test of string value length as a range`() {
        val json = "\"Hello!\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(length(1..5))
            }
        }
        expect("JSON string length doesn't match - Expected 1..5, was 6") { exception.message }
    }

    @Test fun `should test string property length`() {
        val json = """{"abc":"Hello!"}"""
        expectJSON(json) {
            property("abc", length(6))
        }
    }

    @Test fun `should fail on incorrect test of string property length`() {
        val json = """{"abc":"Hello!"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", length(5))
            }
        }
        expect("abc: JSON string length doesn't match - Expected 5, was 6") { exception.message }
    }

    @Test fun `should test string property length as a range`() {
        val json = """{"abc":"Hello!"}"""
        expectJSON(json) {
            property("abc", length(4..10))
        }
    }

    @Test fun `should fail on incorrect test of string property length as a range`() {
        val json = """{"abc":"Hello!"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", length(1..4))
            }
        }
        expect("abc: JSON string length doesn't match - Expected 1..4, was 6") { exception.message }
    }

    @Test fun `should test string array item length`() {
        val json = """["Hello!"]"""
        expectJSON(json) {
            item(0, length(6))
        }
    }

    @Test fun `should fail on incorrect test of string array item length`() {
        val json = """["Hello!"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, length(5))
            }
        }
        expect("[0]: JSON string length doesn't match - Expected 5, was 6") { exception.message }
    }

    @Test fun `should test string array item length as a range`() {
        val json = """["Hello!"]"""
        expectJSON(json) {
            item(0, length(4..10))
        }
    }

    @Test fun `should fail on incorrect test of string array item length as a range`() {
        val json = """["Hello!"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, length(1..4))
            }
        }
        expect("[0]: JSON string length doesn't match - Expected 1..4, was 6") { exception.message }
    }

    @Test fun `should test decimal value scale`() {
        val json = "0.02"
        expectJSON(json) {
            value(scale(2))
        }
    }

    @Test fun `should fail on incorrect test of decimal value scale`() {
        val json = "0.02"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(scale(3))
            }
        }
        expect("JSON decimal scale doesn't match - Expected 3, was 2") { exception.message }
    }

    @Test fun `should test decimal value scale as a range`() {
        val json = "0.02"
        expectJSON(json) {
            value(scale(1..3))
        }
    }

    @Test fun `should fail on incorrect test of decimal value scale as a range`() {
        val json = "0.02"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(scale(3..5))
            }
        }
        expect("JSON decimal scale doesn't match - Expected 3..5, was 2") { exception.message }
    }

    @Test fun `should test decimal property scale`() {
        val json = """{"abc":0.011}"""
        expectJSON(json) {
            property("abc", scale(3))
        }
    }

    @Test fun `should fail on incorrect test of decimal property scale`() {
        val json = """{"abc":0.011}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", scale(2))
            }
        }
        expect("abc: JSON decimal scale doesn't match - Expected 2, was 3") { exception.message }
    }

    @Test fun `should test decimal property scale as a range`() {
        val json = """{"abc":0.011}"""
        expectJSON(json) {
            property("abc", scale(1..3))
        }
    }

    @Test fun `should fail on incorrect test of decimal property scale as a range`() {
        val json = """{"abc":0.011}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", scale(4..5))
            }
        }
        expect("abc: JSON decimal scale doesn't match - Expected 4..5, was 3") { exception.message }
    }

    @Test fun `should test decimal array item scale`() {
        val json = """[0.5]"""
        expectJSON(json) {
            item(0, scale(1))
        }
    }

    @Test fun `should fail on incorrect test of decimal array item scale`() {
        val json = """[0.5]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, scale(2))
            }
        }
        expect("[0]: JSON decimal scale doesn't match - Expected 2, was 1") { exception.message }
    }

    @Test fun `should test decimal array item scale as a range`() {
        val json = """[0.5]"""
        expectJSON(json) {
            item(0, scale(0..3))
        }
    }

    @Test fun `should fail on incorrect test of decimal array item scale as a range`() {
        val json = """[0.5]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, scale(2..3))
            }
        }
        expect("[0]: JSON decimal scale doesn't match - Expected 2..3, was 1") { exception.message }
    }

    @Test fun `should fail with custom error message`() {
        val json = """{"abc":123}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    if (node !is String)
                        error("Custom error message $path = ${showNode()}")
                }
            }
        }
        expect("abc: Custom error message abc = 123") { exception.message }
    }

    @Test fun `should correctly access integer node`() {
        val json = """{"abc":123}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsInt != 123)
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to integer node`() {
        val json = """{"abc":"xyz"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsInt != 123)
                        error("Should not happen")
                }
            }
        }
        expect("abc: JSON type doesn't match - Expected integer, was string") { exception.message }
    }

    @Test fun `should correctly access long integer node`() {
        val json = """{"abc":123456789123456789}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsLong != 123456789123456789)
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to long integer node`() {
        val json = """{"abc":"xyz"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsLong != 123456789123456789)
                        error("Should not happen")
                }
            }
        }
        expect("abc: JSON type doesn't match - Expected long integer, was string") { exception.message }
    }

    @Test fun `should correctly access decimal node`() {
        val json = """{"abc":1.99}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsDecimal != BigDecimal("1.99"))
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to decimal node`() {
        val json = """{"abc":"xyz"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsDecimal != BigDecimal("1.99"))
                        error("Should not happen")
                }
            }
        }
        expect("abc: JSON type doesn't match - Expected decimal, was string") { exception.message }
    }

    @Test fun `should correctly access boolean node`() {
        val json = """{"abc":true}"""
        expectJSON(json) {
            property("abc") {
                if (!nodeAsBoolean)
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to boolean node`() {
        val json = """{"abc":"xyz"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    if (!nodeAsBoolean)
                        error("Should not happen")
                }
            }
        }
        expect("abc: JSON type doesn't match - Expected boolean, was string") { exception.message }
    }

    @Test fun `should correctly access string node`() {
        val json = """{"abc":"xyz"}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsString != "xyz")
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to string node`() {
        val json = """{"abc":123}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsString != "xyz")
                        error("Should not happen")
                }
            }
        }
        expect("abc: JSON type doesn't match - Expected string, was integer") { exception.message }
    }

    @Test fun `should correctly access nested object node`() {
        val json = """{"abc":{"xyz":0}}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsObject.size != 1)
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to nested object node`() {
        val json = """{"abc":123}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsObject.size != 1)
                        error("Should not happen")
                }
            }
        }
        expect("abc: JSON type doesn't match - Expected object, was integer") { exception.message }
    }

    @Test fun `should correctly access nested array node`() {
        val json = """{"abc":[0]}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsArray.size != 1)
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to nested array node`() {
        val json = """{"abc":123}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsArray.size != 1)
                        error("Should not happen")
                }
            }
        }
        expect("abc: JSON type doesn't match - Expected array, was integer") { exception.message }
    }

    @Test fun `should test integer value as long`() {
        val json = "0"
        expectJSON(json) {
            value(0L)
        }
    }

    @Test fun `should fail on incorrect test of integer value as long`() {
        val json = "1"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(0L)
            }
        }
        expect("JSON value doesn't match - Expected 0, was 1") { exception.message }
    }

    @Test fun `should test integer property as long`() {
        val json = """{"a":0}"""
        expectJSON(json) {
            property("a", 0L)
        }
    }

    @Test fun `should fail on incorrect test of integer property as long`() {
        val json = """{"a":1}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("a", 0L)
            }
        }
        expect("a: JSON value doesn't match - Expected 0, was 1") { exception.message }
    }

    @Test fun `should test integer array item as long`() {
        val json = "[0]"
        expectJSON(json) {
            item(0, 0L)
        }
    }

    @Test fun `should fail on incorrect test of integer array item as long`() {
        val json = "[1]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, 0L)
            }
        }
        expect("[0]: JSON value doesn't match - Expected 0, was 1") { exception.message }
    }

    @Test fun `should test integer value in long range`() {
        val json = "0"
        expectJSON(json) {
            value(0L..123456789999)
        }
    }

    @Test fun `should fail on incorrect test of integer value in long range`() {
        val json = "0"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(1L..123456789999)
            }
        }
        expect("JSON value doesn't match - Expected 1..123456789999, was 0") { exception.message }
    }

    @Test fun `should test integer property in long range`() {
        val json = """{"abc":27}"""
        expectJSON(json) {
            property("abc", 0L..123456789999)
        }
    }

    @Test fun `should fail on incorrect test of integer property in long range`() {
        val json = """{"abc":-1}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 0L..123456789999)
            }
        }
        expect("abc: JSON value doesn't match - Expected 0..123456789999, was -1") { exception.message }
    }

    @Test fun `should test integer array item in long range`() {
        val json = "[27]"
        expectJSON(json) {
            item(0, 0L..123456789999)
        }
    }

    @Test fun `should fail on incorrect test of integer array item in long range`() {
        val json = "[-1]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, 0L..123456789999)
            }
        }
        expect("[0]: JSON value doesn't match - Expected 0..123456789999, was -1") { exception.message }
    }

    @Test fun `should test integer value as decimal`() {
        val json = "0"
        expectJSON(json) {
            value(BigDecimal.ZERO)
        }
    }

    @Test fun `should fail on incorrect test of integer value as decimal`() {
        val json = "1"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(BigDecimal.ZERO)
            }
        }
        expect("JSON value doesn't match - Expected 0, was 1") { exception.message }
    }

    @Test fun `should test integer property as decimal`() {
        val json = """{"aaa":0}"""
        expectJSON(json) {
            property("aaa", BigDecimal.ZERO)
        }
    }

    @Test fun `should fail on incorrect test of integer property as decimal`() {
        val json = """{"aaa":1}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("aaa", BigDecimal.ZERO)
            }
        }
        expect("aaa: JSON value doesn't match - Expected 0, was 1") { exception.message }
    }

    @Test fun `should test integer array item as decimal`() {
        val json = "[0]"
        expectJSON(json) {
            item(0, BigDecimal.ZERO)
        }
    }

    @Test fun `should fail on incorrect test of integer array item as decimal`() {
        val json = "[1]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, BigDecimal.ZERO)
            }
        }
        expect("[0]: JSON value doesn't match - Expected 0, was 1") { exception.message }
    }

    @Test fun `should test integer value in decimal range`() {
        val json = "44"
        expectJSON(json) {
            value(BigDecimal.ZERO..BigDecimal(999))
        }
    }

    @Test fun `should fail on incorrect test of integer value in decimal range`() {
        val json = "-1"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(BigDecimal.ZERO..BigDecimal(999))
            }
        }
        expect("JSON value not in range - -1") { exception.message }
    }

    @Test fun `should test integer property in decimal range`() {
        val json = """{"abcde":44}"""
        expectJSON(json) {
            property("abcde", BigDecimal.ZERO..BigDecimal(999))
        }
    }

    @Test fun `should fail on incorrect test of integer property in decimal range`() {
        val json = """{"abcde":-2}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abcde", BigDecimal.ZERO..BigDecimal(999))
            }
        }
        expect("abcde: JSON value not in range - -2") { exception.message }
    }

    @Test fun `should test integer array item in decimal range`() {
        val json = "[578]"
        expectJSON(json) {
            item(0, BigDecimal.ZERO..BigDecimal(999))
        }
    }

    @Test fun `should fail on incorrect test of integer array item in decimal range`() {
        val json = "[-3]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, BigDecimal.ZERO..BigDecimal(999))
            }
        }
        expect("[0]: JSON value not in range - -3") { exception.message }
    }

    @Test fun `should test long integer value as decimal`() {
        val json = "1122334455667788"
        expectJSON(json) {
            value(BigDecimal(1122334455667788))
        }
    }

    @Test fun `should fail on incorrect test of long integer value as decimal`() {
        val json = "1122334455667788"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(BigDecimal.ZERO)
            }
        }
        expect("JSON value doesn't match - Expected 0, was 1122334455667788") { exception.message }
    }

    @Test fun `should test long integer property as decimal`() {
        val json = """{"aaa":123123123123}"""
        expectJSON(json) {
            property("aaa", BigDecimal(123123123123))
        }
    }

    @Test fun `should fail on incorrect test of long integer property as decimal`() {
        val json = """{"aaa":123123123123}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("aaa", BigDecimal.ZERO)
            }
        }
        expect("aaa: JSON value doesn't match - Expected 0, was 123123123123") { exception.message }
    }

    @Test fun `should test long integer array item as decimal`() {
        val json = "[1234567812345678]"
        expectJSON(json) {
            item(0, BigDecimal(1234567812345678))
        }
    }

    @Test fun `should fail on incorrect test of long integer array item as decimal`() {
        val json = "[1234567812345678]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, BigDecimal.ZERO)
            }
        }
        expect("[0]: JSON value doesn't match - Expected 0, was 1234567812345678") { exception.message }
    }

    @Test fun `should test long integer value in decimal range`() {
        val json = "9876543298765432"
        expectJSON(json) {
            value(BigDecimal.ZERO..BigDecimal(9999999999999999))
        }
    }

    @Test fun `should fail on incorrect test of long integer value in decimal range`() {
        val json = "9876543298765432"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(BigDecimal.ZERO..BigDecimal(999999999999999))
            }
        }
        expect("JSON value not in range - 9876543298765432") { exception.message }
    }

    @Test fun `should test long integer property in decimal range`() {
        val json = """{"abcde":1234567812345678}"""
        expectJSON(json) {
            property("abcde", BigDecimal.ZERO..BigDecimal(9999999999999999))
        }
    }

    @Test fun `should fail on incorrect test of long integer property in decimal range`() {
        val json = """{"abcde":-1234567812345678}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abcde", BigDecimal.ZERO..BigDecimal(9999999999999999))
            }
        }
        expect("abcde: JSON value not in range - -1234567812345678") { exception.message }
    }

    @Test fun `should test long integer array item in decimal range`() {
        val json = "[1122334455667788]"
        expectJSON(json) {
            item(0, BigDecimal.ZERO..BigDecimal(9999999999999999))
        }
    }

    @Test fun `should fail on incorrect test of long integer array item in decimal range`() {
        val json = "[-1122334455667788]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, BigDecimal.ZERO..BigDecimal(9999999999999999))
            }
        }
        expect("[0]: JSON value not in range - -1122334455667788") { exception.message }
    }

    @Test fun `should test scale of integer value as 0`() {
        val json = "1"
        expectJSON(json) {
            value(scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of integer value as 0`() {
        val json = "2"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(scale(2))
            }
        }
        expect("JSON decimal scale doesn't match - Expected 2, was 0") { exception.message }
    }

    @Test fun `should test scale of integer property as 0`() {
        val json = """{"abc":33}"""
        expectJSON(json) {
            property("abc", scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of integer property as 0`() {
        val json = """{"abc":33}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", scale(2))
            }
        }
        expect("abc: JSON decimal scale doesn't match - Expected 2, was 0") { exception.message }
    }

    @Test fun `should test scale of integer array item as 0`() {
        val json = "[2]"
        expectJSON(json) {
            item(0, scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of integer array item as 0`() {
        val json = "[2]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, scale(2))
            }
        }
        expect("[0]: JSON decimal scale doesn't match - Expected 2, was 0") { exception.message }
    }

    @Test fun `should test scale of long integer value as 0`() {
        val json = "1122334455667788"
        expectJSON(json) {
            value(scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of long integer value as 0`() {
        val json = "1122334455667788"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(scale(2))
            }
        }
        expect("JSON decimal scale doesn't match - Expected 2, was 0") { exception.message }
    }

    @Test fun `should test scale of long integer property as 0`() {
        val json = """{"abc":1122334455667788}"""
        expectJSON(json) {
            property("abc", scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of long integer property as 0`() {
        val json = """{"abc":1122334455667788}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", scale(2))
            }
        }
        expect("abc: JSON decimal scale doesn't match - Expected 2, was 0") { exception.message }
    }

    @Test fun `should test scale of long integer array item as 0`() {
        val json = "[1122334455667788]"
        expectJSON(json) {
            item(0, scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of long integer array item as 0`() {
        val json = "[1122334455667788]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, scale(2))
            }
        }
        expect("[0]: JSON decimal scale doesn't match - Expected 2, was 0") { exception.message }
    }

}
