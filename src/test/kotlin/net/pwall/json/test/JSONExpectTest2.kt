/*
 * @(#) JSONExpectTest2.kt
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

class JSONExpectTest2 {

    @Test
    fun `should fail on test of missing property`() {
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

}
