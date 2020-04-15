# json-kotlin-test

Library for testing Kotlin JSON applications

## Background

This library provides a convenient way of testing applications that produce JSON results.
It uses a Kotlin DSL to describe the expected JSON values, and produces detailed error messages in the case of failure.

## Quick Start

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

## More Detail

Perhaps the most complex part of `json-kotlin-test` is the import statement:
```kotlin
import net.pwall.json.test.JSONExpect.Companion.expectJSON
```

With that in place, the rest is easy!
In a test, the `expectJSON` function takes two parameters: the JSON in string form, and lambda which can contain a
number of tests.
If any of the tests fails, an `AssertionError` is thrown, with a detailed message describing the problem.

### `property`

This function is used when the JSON is an object, and it selects a named property in that object.
There are two forms of the function; one which takes a value to be compared to the value in the JSON, and a second which
takes a lambda containing tests to be applied to members of the property.
Both of these forms are illustrated in the example above.

### `item`

This is used when the JSON is an array, and it selects an item from the array by index.
As with `property`, there are two forms of the function, both illustrated above.

### `propertyAbsent`

This is used to confirm that no property with the specified name is present in the current context.
It can often be simpler to specify the count (see below) of expected properties rather than enumerating the properties
expected to be missing.

### `count`

This specifies the expected count of members of an array or object.

### `value`

This is used when the JSON is just a simple value, e.g. a string, a number or a boolean.
It specifies the expected value to be checked against that JSON value.

## Important Notes

### Floating Point

All floating point numbers in the JSON (numbers with a decimal point or using scientific &ldquo;e&rdquo; notation) are
converted to `BigDecimal`, so comparisons must also use that class, e.g. `property("price":, BigDecimal("9.99"))`.

### Custom Deserialization

This library looks only at the input JSON, and does not take into account any custom deserializations that may be
applied to the JSON when it is used in other situations.
Custom name annotations, and even the conversion of dates from strings to `LocalDate` (for example) are not applied.

### Check for `null`

Checking a value for `null`, e.g. `property("note", null)` will check that the named property **is present** in the JSON
string and has the value `null`.

### Check for Member of Collection

Starting with version 0.3 of the library, you can check a value as being a member of a `Collection`.
For example:
```kotlin
    property("quality", setOf("good", "bad"))
```
This test will succeed if the value of the property is one of the members of the set.

The `Collection` must be of the appropriate type for the value being checked, and each of the functions `value`,
`property` and `item` has an overloaded version that takes a `Collection`.

### Check for Value in Range

Again starting with version 0.3 of the library, you can check a value as being included in a range (`IntRange`,
`LongRange` or `ClosedRange`).
For example:
```kotlin
    property("number", 1000..9999)
```

As with `Collection`, the range must be of the appropriate type, and each of the functions `value`, `property` and
`item` has an overloaded version that takes a range.

## Dependency Specification

The latest version of the library is 0.3, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.json</groupId>
      <artifactId>json-kotlin-test</artifactId>
      <version>0.3</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.json:json-kotlin-test:0.3'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.json:json-kotlin-test:0.3")
```

Peter Wall

2020-04-16
