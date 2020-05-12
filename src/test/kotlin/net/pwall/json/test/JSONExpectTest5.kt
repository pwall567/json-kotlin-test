/*
 * @(#) JSONExpectTest5.kt
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

class JSONExpectTest5 {

    @Test fun `should test value as one of multiple possibilities`() {
        val json1 = "\"2020-05-12\""
        expectJSON(json1) {
            oneOf(localDate, test(42), test("NEVER"))
        }
        val json2 = "42"
        expectJSON(json2) {
            oneOf(localDate, test(42), test("NEVER"))
        }
        val json3 = "\"NEVER\""
        expectJSON(json3) {
            oneOf(localDate, test(42), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of value as one of multiple possibilities`() {
        val json = "\"INCORRECT\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(42), test("NEVER"))
            }
        }
        expect("No successful test - value is \"INCORRECT\"") { exception.message }
    }

    @Test fun `should test property as one of multiple possibilities`() {
        val json1 = """{"abc":"2020-05-12"}"""
        expectJSON(json1) {
            property("abc") {
                oneOf(localDate, test(42), test("NEVER"))
            }
        }
        val json2 = """{"abc":42}"""
        expectJSON(json2) {
            property("abc") {
                oneOf(localDate, test(42), test("NEVER"))
            }
        }
        val json3 = """{"abc":"NEVER"}"""
        expectJSON(json3) {
            property("abc") {
                oneOf(localDate, test(42), test("NEVER"))
            }
        }
    }

    @Test fun `should fail on incorrect test of property as one of multiple possibilities`() {
        val json = """{"abc":"INCORRECT"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc") {
                    oneOf(localDate, test(42), test("NEVER"))
                }
            }
        }
        expect("abc: No successful test - value is \"INCORRECT\"") { exception.message }
    }

    @Test fun `should test array item as one of multiple possibilities`() {
        val json1 = "[\"2020-05-12\"]"
        expectJSON(json1) {
            item(0) {
                oneOf(localDate, test(42), test("NEVER"))
            }
        }
        val json2 = "[42]"
        expectJSON(json2) {
            item(0) {
                oneOf(localDate, test(42), test("NEVER"))
            }
        }
        val json3 = "[\"NEVER\"]"
        expectJSON(json3) {
            item(0) {
                oneOf(localDate, test(42), test("NEVER"))
            }
        }
    }

    @Test fun `should fail on incorrect test of array item as one of multiple possibilities`() {
        val json = "[\"INCORRECT\"]"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0) {
                    oneOf(localDate, test(42), test("NEVER"))
                }
            }
        }
        expect("[0]: No successful test - value is \"INCORRECT\"") { exception.message }
    }

    @Test fun `should include long check as one of multiple possibilities`() {
        val json = "2233445566778899"
        expectJSON(json) {
            oneOf(localDate, test(2233445566778899), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of long check as one of multiple possibilities`() {
        val json = "0"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(2233445566778899), test("NEVER"))
            }
        }
        expect("No successful test - value is 0") { exception.message }
    }

    @Test fun `should include decimal check as one of multiple possibilities`() {
        val json = "1.5"
        expectJSON(json) {
            oneOf(localDate, test(BigDecimal("1.5")), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of decimal check as one of multiple possibilities`() {
        val json = "0.5"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(BigDecimal("1.5")), test("NEVER"))
            }
        }
        expect("No successful test - value is 0.5") { exception.message }
    }

    @Test fun `should include boolean check as one of multiple possibilities`() {
        val json = "true"
        expectJSON(json) {
            oneOf(localDate, test(true), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of boolean check as one of multiple possibilities`() {
        val json = "false"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(true), test("NEVER"))
            }
        }
        expect("No successful test - value is false") { exception.message }
    }

    @Test fun `should include null check as one of multiple possibilities`() {
        val json = "null"
        expectJSON(json) {
            oneOf(localDate, test(null), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of null check as one of multiple possibilities`() {
        val json = "0"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(null), test("NEVER"))
            }
        }
        expect("No successful test - value is 0") { exception.message }
    }

    @Test fun `should include Regex check as one of multiple possibilities`() {
        val json = "\"abcdef\""
        expectJSON(json) {
            oneOf(localDate, test(Regex("^[a-z]+$")), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of Regex check as one of multiple possibilities`() {
        val json = "\"0\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(Regex("^[a-z]+$")), test("NEVER"))
            }
        }
        expect("No successful test - value is \"0\"") { exception.message }
    }

    @Test fun `should include int range check as one of multiple possibilities`() {
        val json = "42"
        expectJSON(json) {
            oneOf(localDate, test(0..50), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of int range check as one of multiple possibilities`() {
        val json = "51"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(0..50), test("NEVER"))
            }
        }
        expect("No successful test - value is 51") { exception.message }
    }

    @Test fun `should include long range check as one of multiple possibilities`() {
        val json = "1122334455667700"
        expectJSON(json) {
            oneOf(localDate, test(0..1122334455667788), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of long range check as one of multiple possibilities`() {
        val json = "1122334455667799"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(0..1122334455667788), test("NEVER"))
            }
        }
        expect("No successful test - value is 1122334455667799") { exception.message }
    }

    @Test fun `should include decimal range check as one of multiple possibilities`() {
        val json = "2.5"
        expectJSON(json) {
            oneOf(localDate, test(BigDecimal.ZERO..BigDecimal("10.0")), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of decimal range check as one of multiple possibilities`() {
        val json = "-2.5"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(BigDecimal.ZERO..BigDecimal("10.0")), test("NEVER"))
            }
        }
        expect("No successful test - value is -2.5") { exception.message }
    }

    @Test fun `should include string range check as one of multiple possibilities`() {
        val json = "\"abc\""
        expectJSON(json) {
            oneOf(localDate, test("aaa".."zzz"), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of string range check as one of multiple possibilities`() {
        val json = "\"AAA\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test("aaa".."zzz"), test("NEVER"))
            }
        }
        expect("No successful test - value is \"AAA\"") { exception.message }
    }

    @Test fun `should include string collection check as one of multiple possibilities`() {
        val json = "\"alpha\""
        expectJSON(json) {
            oneOf(localDate, test(setOf("alpha", "beta", "gamma")), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of string collection check as one of multiple possibilities`() {
        val json = "\"delta\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf(localDate, test(setOf("alpha", "beta", "gamma")), test("NEVER"))
            }
        }
        expect("No successful test - value is \"delta\"") { exception.message }
    }

    @Test fun `should test complex combinations of multiple possibilities`() {
        val json1 = """{"data":27}"""
        expectJSON(json1) {
            oneOf({
                property("data", 27)
            },{
                property("error", 0..999)
            })
        }
        val json2 = """{"error":8}"""
        expectJSON(json2) {
            oneOf({
                property("data", 27)
            },{
                property("error", 0..999)
            })
        }
    }

    @Test fun `should fail on incorrect test of complex combinations of multiple possibilities`() {
        val json = """{"data":28}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                oneOf({
                    property("data", 27)
                },{
                    property("error", 0..999)
                })
            }
        }
        expect("No successful test - value is {...}") { exception.message }
    }

}
