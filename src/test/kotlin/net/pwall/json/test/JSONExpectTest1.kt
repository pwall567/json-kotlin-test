/*
 * @(#) JSONExpectTest1.kt
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

class JSONExpectTest1 {

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

}
