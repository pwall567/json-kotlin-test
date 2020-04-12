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
        // json is (for example): {"data":[{"id":1234,"name":"Richard"}]}
        expectJSON(json) {
            property("data") {
                item(0) {
                    property("id", 1234)
                    property("name", "Richard")
                }
            }
        }
    }
```

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
