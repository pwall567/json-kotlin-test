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

### `propertyAbsentOrNull`

In some cases, a `null` value in an object may be serialized as a property with the keyword value `"null"`, and in other
cases the property may be omitted.
Both forms are equivalent, and the `propertyAbsentOrNull` function tests for either case.

### `propertyPresent`

This is used to confirm that a property with the specified name is present in the current context, but does not check
the content.

### `nonNull`

This is used to confirm that a value is non-null, but does not check the content.

### `count`

This specifies the expected count of members of an array or object.
The expected count may be specified as an integer value or an `IntRange`.

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

### Check for String Length

From version 0.4 onwards, the `length` function is available to check the length of a string.
If, for example, you expect a string to contain between 5 and 20 characters, but you don't need to check the contents,
you can specify:
```kotlin
    property("name", length(5..20))
```
The length may be specified as an integer value or an `IntRange`.

### Check against `Regex`

Another new feature in version 0.4 is the ability to check a string against a `Regex`, for example:
```kotlin
    property("name", Regex("^[A-Za-z]+$"))
```

### Check for UUID

Also starting from version 0.4, if you wish to check that a string contains a UUID, but you are not concerned about the
content of the UUID, you can specify:
```kotlin
    property("id", uuid)
```

### Check for `java.time.xxx` classes

Again, starting from version 0.4, if you wish to check that a string contains a valid representation of one of the
`java.time.xxx` classes, but don't care about the actual value, you can specify:
```kotlin
    property("field1", localDate)
    property("field2", localDateTime)
    property("field3", localTime) // version 0.6 onwards
    property("field4", offsetDateTime)
    property("field5", offsetTime)
    property("field6", yearMonth)
    property("field7", monthDay) // version 0.6 onwards
    property("field8", year)
    property("field9", period)
    property("field10", duration)
```

## Dependency Specification

The latest version of the library is 0.6, and it may be obtained from the Maven Central repository.
(The following dependency declarations assume that the library will be included for test purposes; this is
expected to be its principal use.)

### Maven
```xml
    <dependency>
      <groupId>net.pwall.json</groupId>
      <artifactId>json-kotlin-test</artifactId>
      <version>0.6</version>
      <scope>test</scope>
    </dependency>
```
### Gradle
```groovy
    testImplementation 'net.pwall.json:json-kotlin-test:0.6'
```
### Gradle (kts)
```kotlin
    testImplementation("net.pwall.json:json-kotlin-test:0.6")
```

Peter Wall

2020-04-30
