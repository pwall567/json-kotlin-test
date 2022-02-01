# json-kotlin-test

[![Build Status](https://travis-ci.com/pwall567/json-kotlin-test.svg?branch=master)](https://travis-ci.com/github/pwall567/json-kotlin-test)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v1.5.20&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v1.5.20)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.json/json-kotlin-test?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.json%22%20AND%20a:%22json-kotlin-test%22)

Library for testing Kotlin JSON applications

This library provides a convenient way of testing applications that produce JSON results.
It uses a Kotlin DSL to describe the expected JSON values, and produces detailed error messages in the case of failure.

## Contents

- [Quick Start](#quick-start)
- [User Guide](#user-guide)
- [Reference](#reference)
- [Dependency Specification](#dependency-specification)




## Quick Start

### Example

In a test class:
```kotlin
    @Test fun `should produce correct JSON name data`() {
        val json = functionUnderTest()
        // json is (for example):
        // {"data":[{"id":1234,"surname":"Walker","givenNames":["Richard"]}]}
        expectJSON(json) {
            property("data") {
                item(0) {
                    property("id", 1234)
                    property("surname", "Walker")
                    property("givenNames") {
                        count(1)
                        item(0, "Richard")
                    }
                    propertyAbsent("birthDate")
                }
            }
        }
    }
```

### The Problem

Many Kotlin applications return their result in the form of a JSON object, or in some cases, as an array of JSON
objects.
When we attempt to test these applications, we run into a simple problem - there may be many valid JSON representations
of the same object.
The properties of a JSON object do not have a pre-determined order, and given a valid set of properties, any sequence of
those properties is equally valid.

Also, the JSON specification allows for whitespace to be added at many points in the JSON string, without affecting the
meaning of the content.

All of this means that it is not possible to test the results of a function returning JSON by simply performing a string
comparison on the output.
We need a means of checking the data content of the JSON, regardless of the formatting.

Many developers avoid the problem by deserializing the JSON into the internal form of the object for comparison, but
that is not always a useful option.
And there are libraries that will check an arbitrary JSON string, but they are mostly written in and for Java, and they
do not take advantage of the capabilities of Kotlin.

### The Solution

The `json-kotlin-test` library allows the expected content of a JSON object to be described in a simple, intuitive form,
allowing functions returning JSON to be tested easily.

In many cases, the expected values will be known precisely, and the basic forms of comparison make a good starting
point:
```kotlin
    property("id", 1234)
    property("surname", "Walker")
```
This checks that the current node is an object with a property named "id" having the value 1234, and another property
"surname" with the value "Walker".

Nested objects may be checked by:
```kotlin
    property("details") {
        // tests on the nested object
    }
```

And array items can similarly be checked, either as primitive values or as nested objects or arrays.

The `json-kotlin-test` library makes testing simple cases easy and clear, and at the same time provides functionality to
meet very broad and complex testing requirements.






## User Guide

First, some terminology:
- a JSON **object** is the form of JSON structure enclosed in curly braces (` { } `) and containing a set of name-value
pairs
- these pairs are called **properties**, and each has a property name,  and a value which may be a primitive value or a
nested object or array
- a JSON **array** is an ordered collection of values (possibly nested objects or arrays) enclosed in square brackets
(` [ ] `)
- an **array item** is a member of an array
- a JSON **value** is any of the possible JSON data types: a string, a number, a boolean, the value "null" or a nested
object or array

A function or service returning a JSON result will usually return an object or an array of objects, but according to the
JSON specification the string representation of any JSON value is a valid JSON string.
The `json-kotlin-test` library provides facilities for testing all forms of JSON results.

### Invoking the Tests

The set of tests on a JSON string is enclosed in a call to the [`expectJSON`](#expectjson) function.
This parses the JSON into an internal form and then performs the tests in the supplied lambda.
```kotlin
    expectJSON(stringToBeTested) {
        // tests...
    }
```

If any of the tests fail an `AssertionError` will be thrown with a detailed error message, usually including expected
and actual values.

### Testing JSON Properties

The [`property`](#property) function declares a test (or a group of tests) to be performed on a property of an object.
There are several forms of the `property` function; they all take a property name (`String`) as the first parameter, and
an expected value or a lambda as the second.

Some examples:
```kotlin
        property("name", "William")
        property("id", 12345678)
        property("open", true)
        property("details") {
            // tests on nested object or array
        }
```

### Testing Array Items

The [`item`](#item) function declares a test (or a group of tests) to be performed on a item of an array.
As with `property`, there are several forms of the `item` function, each taking an index (`Int`) as the first parameter,
and an expected value or a lambda as the second.

Some examples:
```kotlin
        index(0, "bear")
        index(27, -1)
        index(1, true)
        index(4) {
            // tests on nested object or array
        }
```

### Testing Primitive JSON Values

In rare cases a JSON string consists of a primitive value (string, number, boolean or `null`).
The [`value`](#value) function allows tests to be applied to the single primitive value, and it takes a single
parameter, the expected value or lambda.

Some examples:
```kotlin
        value("success")
        value(0)
        value(false)
        value(null)
```

### Named Tests

The lambda parameter of the `property`, `item` or `value` tests normally takes the form of a set of tests applied to a
nested object or array, but it can also specify a named lambda, as in the following examples:
```kotlin
        property("account", integer)
        item(0, string)
        property("id", uuid)
        property("created", offsetDateTime)
```

These functions test that a property or item meets a particular requirement, without specifying the exact value.
In the above tests, the property "account" is checked to have an integer value, item 0 of an array contains a string,
property "id" contains a string formatted as a UUID and property "created" contains a string following the pattern of
the `OffsetDateTime` class.

See the [Reference](#reference) section for a full list of these [General Test Lambdas](#general-test-lambdas).

### Floating Point

Floating point numbers are those with a decimal point, or using the scientific notation (e.g. 1.5e20).
Many decimal floating point numbers can not be represented with complete accuracy in a binary floating point system, so
the `json-kotlin-test` library converts all such numbers to `BigDecimal`.
This means that tests on floating point numbers must use `BigDecimal`, or `ClosedRange<BigDecimal>`, or
`Collection<BigDecimal>`.

If a comparison using a `BigDecimal` is performed against an `Int` or a `Long`, the value will be "widened" to
`BigDecimal` before the test is performed.

One unusual feature of the `BigDecimal` class is that the `equals` comparison requires that both the value and the scale
of the number must be equal, but this library uses `compareTo` to compare the values regardless of scale.
If it is important to confirm that a certain number of decimal digits are present (for example, in a money value), the
[`scale`](#scale) function may be used to test the number of digits after the decimal point.
An `Int` or a `Long` will always be considered as having zero decimal places.

### Custom Deserialization

This library looks only at the input JSON, and does not take into account any custom deserializations that may be
applied to the JSON when it is used in other situations.
Custom name annotations, and even the conversion of dates from strings to `LocalDate` (for example) are not applied.

### Check for `null`

Checking a value for `null`, e.g. `property("note", null)` will check that the named property **is present** in the JSON
string and has the value `null`.

In some cases, the fact that a property is not present in the JSON may be taken as being equivalent to the property
being present with a `null` value.
If it is important to distinguish between a `null` property and an omitted property, there are functions which test
specifically for the presence or absence of a property:

To test that a property...                   | Use
-------------------------------------------- | ------------------------------
...is present and is not null                | `property("name", nonNull)`
...is present and is null                    | `property("name", null)`
...is present whether null or not            | [`propertyPresent("name")`](#propertypresent)
...is not present                            | [`propertyAbsent("name")`](#propertyabsent)
...is not present, or is present and is null | [`propertyAbsentOrNull("name")`](#propertyabsentornull)

Instead of checking all of the properties that are expected not to be present, it may often be simpler to use the
[`count`](#count) function to check the number of properties that **are** present.

### Check for Member of Collection

You can check a value as being a member of a `Collection`.
For example:
```kotlin
    property("quality", setOf("good", "bad"))
```
This test will succeed if the value of the property is one of the members of the set.

The `Collection` must be of the appropriate type for the value being checked, and each of the functions `value`,
`property` and `item` has an overloaded version that takes a `Collection`.

### Check for Value in Range

You can also check a value as being included in a range (`IntRange`, `LongRange` or `ClosedRange`).
For example:
```kotlin
    property("number", 1000..9999)
```

As with `Collection`, the range must be of the appropriate type, and each of the functions `value`, `property` and
`item` has an overloaded version that takes a range.

### Check for String Length

The [`length`](#length) function is available to check the length of a string.
If, for example, you expect a string to contain between 5 and 20 characters, but you don't need to check the exact
contents, you can specify:
```kotlin
    property("name", length(5..20))
```
The length may be specified as an integer value or an `IntRange`.

### Check against `Regex`

It is also possible to check a string against a `Regex`, for example:
```kotlin
    property("name", Regex("^[A-Za-z]+$"))
```

### Multiple Possibilities

You can also check for a value matching one of a number of possibilities.
The [`oneOf`](#oneof) function takes any number of lambdas (as `vararg` parameters) and executes them in turn until it
finds one that matches.
```kotlin
    property("elapsedTime") {
        oneOf(duration, integer)
    }
```

To simplify the use of lambdas in the `oneOf` list, the [`test`](#test) function creates a lambda representing any of
the available types of test:
```kotlin
    property("response") {
        oneOf(test(null), test(setOf("YES", "NO")))
    }
```

Of course, the full lambda syntax can be used to describe complex combinations.
In the following case, the JSON string may be either `{"data":27}` or `{"error":nnn}`, where _nnn_ is a number between
0 and 999.
```kotlin
    expectJSON(json) {
        oneOf({
            property("data", 27)
        },{
            property("error", 0..999)
        })
    }
```

### Custom Tests

In any of the tests that take a lambda parameter, the lambda is not restricted to the functions provided by the library;
the full power of Kotlin is available to create tests of any complexity.

The "receiver" for the lambda is an object describing the current node in the JSON structure, and the value is available
as a `val` named [`node`](#node-nodeasstring-nodeasint-nodeaslong-nodeasdecimal-nodeasboolean-nodeasobject-nodeasarray).
The type of `node` is `Any?`, but in practice it will be one of:

- `String`
- `Int`
- `Long`
- `BigDecimal`
- `Boolean`
- `List<Any?>`
- `Map<String, Any?>`
- `null`

(In the case of `Map` or `List`, the type parameter `Any?` will itself be one of the above.)

There are also conversion functions, each of which takes the form of a `val` with a custom accessor.
These accessors either return the node in the form requested, or throw an `AssertionError` with a detailed error
message.

Accessor        | Type
--------------- | -----------
`nodeAsString`  | `String`
`nodeAsInt`     | `Int`
`nodeAsLong`    | `Long`
`nodeAsDecimal` | `BigDecimal`
`nodeAsBoolean` | `Boolean`
`nodeAsArray`   | `List<*>`
`nodeAsObject`  | `Map<*, *>`

To report errors, the [`error`](#error) function will create an `AssertionError` with the message provided, prepending
the path information for the current node in the JSON.
The [`showNode`](#shownode) function can be used to display the actual node value in the error message.

Example:
```kotlin
    expectJSON(jsonString) {
        property("abc") {
            if (nodeAsInt.rem(3) != 0)
                error("Value not divisible by 3 - ${showNode()}")
        }
    }
```


## Reference

### The `import` Statement

Perhaps the most complex part of `json-kotlin-test` is the import statement:
```kotlin
import net.pwall.json.test.JSONExpect.Companion.expectJSON
```
If the IDE is configured to include the `json-kotlin-test` library, it will often include the `import` automatically
when you enter the name of the test function.

### `expectJSON`

The `expectJSON` function introduces the set of tests to be performed on the JSON.
It takes two parameters:
- a `String` containing the JSON to be examined
- a lambda, specifying the tests to be performed on the JSON

Example:
```kotlin
    expectJSON(stringOfJSON) {
        property("data") {
            // tests ...
        }
    }
```

### `property`

Tests the value of a property of an object.
In all cases, the first parameter is the name of the property; the second parameter varies according to the test being
performed.

Signature                                 | Check that the property...
----------------------------------------- | ----------------------------------------------------------------
`property(String, String?)`               | ...is equal to a `String` or `null`
`property(String, Int)`                   | ...is equal to an `Int`
`property(String, Long)`                  | ...is equal to a `Long`
`property(String, BigDecimal)`            | ...is equal to a `BigDecimal`
`property(String, Boolean)`               | ...is equal to a `Boolean`
`property(String, Regex)`                 | ...is a `String` matching the given `Regex`
`property(String, IntRange)`              | ...is in a given range
`property(String, LongRange)`             | ...is in a given range
`property(String, ClosedRange<*>)`        | ...is in a given range
`property(String, Collection<*>)`         | ...is in a given collection
`property(String, JSONExpect.() -> Unit)` | ...satisfies the given lambda

In the case of a `ClosedRange` or `Collection`, the parameter type must be `Int`, `Long`, `BigDecimal` or `String`,
although in practice a range of `Int` or `Long` would be more likely to use `IntRange` or `LongRange` respectively.

Only the test for `String` has a signature that allows `null` values; this is to avoid compile-time ambiguity on tests
against `null`.
This does not mean that only `String` properties can be tested for `null` - a `null` property in the JSON is typeless
so a test for `null` would work, regardless of the type that the property would otherwise hold.

The last function signature in the list is the one that specifies a lambda - as is usual in Kotlin, when the last
parameter is a lambda it is usually written outside the parentheses of the function call.
This is the pattern followed when the lambda is an inline set of tests to be applied to the property, but this function
signature is also used for the [general test lambdas](#general-test-lambdas), or the [`length`](#length) or
[`scale`](#scale) functions.

Examples:
```kotlin
        property("id", 12345)
        property("name", "William")
        property("count", 0..9999)
        property("amount", decimal)
        property("reference", uuid)
        property("address", length(1..80))
        property("code", setOf("AAA", "PQR", "XYZ"))
        property("details") {
            // nested tests
        }
```


### `item`

Tests the value of an array item.
In all cases, the first parameter is the index of the array item (must be non-negative).
The second parameter varies according to the test being performed.

Signature                          | Check that the array item...
---------------------------------- | ----------------------------------------------------------------
`item(Int, String?)`               | ...is equal to a `String` or `null`
`item(Int, Int)`                   | ...is equal to an `Int`
`item(Int, Long)`                  | ...is equal to a `Long`
`item(Int, BigDecimal)`            | ...is equal to a `BigDecimal`
`item(Int, Boolean)`               | ...is equal to a `Boolean`
`item(Int, Regex)`                 | ...is a `String` matching the given `Regex`
`item(Int, IntRange)`              | ...is in a given range
`item(Int, LongRange)`             | ...is in a given range
`item(Int, ClosedRange<*>)`        | ...is in a given range
`item(Int, Collection<*>)`         | ...is in a given collection
`item(Int, JSONExpect.() -> Unit)` | ...satisfies the given lambda

The notes following [`property`](#property) describing the options for the second parameter apply equally to `item`.

Examples:
```kotlin
        item(0, 22)
        item(5, "William")
        item(7, decimal)
        item(7, scale(0..2))
        item(1, uuid)
        item(0) {
            // nested tests
        }
```

### `value`

This function takes one parameter, which varies according to the test being performed.

Signature                      | Check that the value...
------------------------------ | ----------------------------------------------------------------
`value(String?)`               | ...is equal to a `String` or `null`
`value(Int)`                   | ...is equal to an `Int`
`value(Long)`                  | ...is equal to a `Long`
`value(BigDecimal)`            | ...is equal to a `BigDecimal`
`value(Boolean)`               | ...is equal to a `Boolean`
`value(Regex)`                 | ...is a `String` matching the given `Regex`
`value(IntRange)`              | ...is in a given range
`value(LongRange)`             | ...is in a given range
`value(ClosedRange<*>)`        | ...is in a given range
`value(Collection<*>)`         | ...is in a given collection
`value(JSONExpect.() -> Unit)` | ...satisfies the given lambda

The notes following [`property`](#property) describing the options for the second parameter apply equally to the sole
parameter of `value`.

Examples:
```kotlin
        value(0..9999)
        value(decimal)
        value(scale(0..2))
        value(localDate)
```

### `length`

This is used to check the length of a `String` property, array item or value.
The parameter may be an `Int` or an `IntRange`.

Examples:
```kotlin
        property("name", length(1..40))
        item(0, length(12))
```

### `scale`

The scale of a `BigDecimal` may be checked, if it is required that a decimal number have a specific scale.
If the number is parsed as an `Int` or a `Long`, the scale will be zero.
The parameter may be an `Int` or an `IntRange`.

Examples:
```kotlin
        property("amount", scale(1..2))
        item(0, scale(0))
```

### `count`

Tests the count of properties or array items (the length of the array).
The parameter may be an `Int` or an `IntRange`.

Examples:
```kotlin
        property("options") {
            count(2)
            item(0, "A")
            item(1, "B")
        }
```

### `oneOf`

Test each of the parameters in turn, exiting when one of them matches the node successfully.
It takes a variable number of parameters, each of which is a lambda (the [`test`](#test) function may be used to create
lambdas for this purpose).

Examples:
```kotlin
        property("id") {
            oneOf(integer, uuid)
        }
        property("result") {
            oneof(test(0..99999), test("ERROR"))
        }
```

### `test`

Creates a lambda, principally for use with the [`oneOf`](#oneof) function.
It takes one parameter, which varies according to the type of test.

Signature                     | Create a test for...
----------------------------- | -------------------------------------------------------------
`test(String?)`               | ...value equal to a `String` or `null`
`test(Int)`                   | ...value equal to an `Int`
`test(Long)`                  | ...value equal to a `Long`
`test(BigDecimal)`            | ...value equal to a `BigDecimal`
`test(Boolean)`               | ...value equal to a `Boolean`
`test(Regex)`                 | ...value a `String` matching the given `Regex`
`test(IntRange)`              | ...value in a given range
`test(LongRange)`             | ...value in a given range
`test(ClosedRange<*>)`        | ...value in a given range
`test(Collection<*>)`         | ...value in a given collection

The notes following [`property`](#property) describing the options for the second parameter apply equally to the sole
parameter of `test`.

For examples see the [`oneOf`](#oneof) function.

### `propertyAbsent`

Tests that no property with the specified name is present in the object.
It takes one parameter - the property name (`String`).

Examples:
```kotlin
        property("controls") {
            property("openingDate", localDate)
            propertyAbsent("closingDate")
        }
```

### `propertyAbsentOrNull`

Tests that no property with the specified name is present in the object, or if one is present, that it is `null`.
It takes one parameter - the property name (`String`).

Examples:
```kotlin
        property("controls") {
            property("openingDate", localDate)
            propertyAbsentOrNull("closingDate")
        }
```

### `propertyPresent`

Tests that a property with the specified name is present in the object, regardless of the value. 
It takes one parameter - the property name (`String`).

Examples:
```kotlin
        property("book") {
            propertyPresent("author")
        }
```

### `node`, `nodeAsString`, `nodeAsInt`, `nodeAsLong`, `nodeAsDecimal`, `nodeAsBoolean`, `nodeAsObject`, `nodeAsArray`

In custom tests the current node can be accessed by one of the following:

`val` name      | Type
--------------- | ------------
`node`          | `Any?`
`nodeAsString`  | `String`
`nodeAsInt`     | `Int`
`nodeAsLong`    | `Long`
`nodeAsDecimal` | `BigDecimal`
`nodeAsBoolean` | `Boolean`
`nodeAsObject`  | `Map<*, *>`
`nodeAsArray`   | `List<*>`

If the node as not of the required type, an `AssertionError` will be thrown.

### `error`

In custom tests the `error` function will output the message given, prepended with the path information for the current
node.
It takes one parameter - the message (`String`).

### `showNode`

The `showNode` function may be used to format the node for inclusion in an error message.
It takes no parameters.

### General Test Lambdas

These may be used with the form of [`property`](#property), [`item`](#item) or [`value`](#value) that takes a lambda
parameter.
They are useful when only the general nature of the value is to be tested, and the actual value is not important.

Name             | Tests that the value is...
---------------- | -----------------------------------------------------------------------------------
`nonNull`        | ...non-null
`string`         | ...a `String`
`integer`        | ...an `Int`
`longInteger`    | ...a `Long`
`decimal`        | ...a `BigDecimal` (a number with an optional decimal fraction)
`uuid`           | ...a `String` containing a valid UUID
`localDate`      | ...a `String` containing a valid `LocalDate`
`localDateTime`  | ...a `String` containing a valid `LocalDateTime`
`localTime`      | ...a `String` containing a valid `LocalTime`
`offsetDateTime` | ...a `String` containing a valid `OffsetDateTime`
`offsetTime`     | ...a `String` containing a valid `OffsetTime`
`zonedDateTime`  | ...a `String` containing a valid `ZonedDateTime`
`year`           | ...a `String` containing a valid `Year`
`yearMonth`      | ...a `String` containing a valid `YearMonth`
`monthDay`       | ...a `String` containing a valid `MonthDay`
`duration`       | ...a `String` containing a valid `Duration`
`period`         | ...a `String` containing a valid `Period`

Consistent with the widening of numbers in the tests against `Long` and `BigDecimal` values, the `longInteger` test
passes if the value is `Int` or `Long`, and the `decimal` test passes if the value is `Int` or `Long` or `BigDecimal`.


## Dependency Specification

The latest version of the library is 1.1.4, and it may be obtained from the Maven Central repository.
(The following dependency declarations assume that the library will be included for test purposes; this is
expected to be its principal use.)

### Maven
```xml
    <dependency>
      <groupId>net.pwall.json</groupId>
      <artifactId>json-kotlin-test</artifactId>
      <version>1.1.4</version>
      <scope>test</scope>
    </dependency>
```
### Gradle
```groovy
    testImplementation 'net.pwall.json:json-kotlin-test:1.1.4'
```
### Gradle (kts)
```kotlin
    testImplementation("net.pwall.json:json-kotlin-test:1.1.4")
```

Peter Wall

2022-02-02
