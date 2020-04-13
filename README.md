# json-kotlin-test

Library for testing Kotlin JSON applications

## Background

This library provides a convenient way of testing applications that produce JSON results.
It uses a Kotlin DSL to describe the expected JSON values, and produces detailed error messages in the case of failure.

## Quick Start

In a test class:
```kotlin
    @Test fun `should produce correct JSON`() {
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
If any of the tests fails, an `AssertionException` is thrown, with a detailed message describing the problem.

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

## Dependency Specification

The latest version of the library is 0.1, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.json</groupId>
      <artifactId>json-kotlin-test</artifactId>
      <version>0.1</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.json:json-kotlin-test:0.1'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.json:json-kotlin-test:0.1")
```

Peter Wall

2020-04-13
