/*
 * @(#) JSONExpectTest4.kt
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

class JSONExpectTest4 {

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
