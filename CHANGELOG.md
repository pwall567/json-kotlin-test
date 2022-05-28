# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [1.1.7] - 2022-05-29
### Changed
- `pom.xml`: bumped dependency version
gi
## [1.1.6] - 2022-05-01
### Changed
- `pom.xml`: bumped dependency version

## [1.1.5] - 2022-04-18
### Changed
- `pom.xml`: bumped dependency version

## [1.1.4] - 2022-02-02
### Changed
- `pom.xml`: bumped dependency version

## [1.1.3] - 2022-01-22
### Changed
- `pom.xml`: bumped dependency version

## [1.1.2] - 2022-01-21
### Changed
- `pom.xml`: bumped Kotlin version
- `pom.xml`: bumped dependency version

## [1.1.1] - 2021-09-16
### Changed
- `pom.xml`: bumped dependency version

## [1.1] - 2021-08-24
### Changed
- `pom.xml`: bumped dependency version

## [1.0] - 2021-05-04
### Changed
- `JSONExpect`: switched to use `JSONSimple.parse()` lightweight JSON parser
- `pom.xml`: bumped dependency, promoted to version 1.0

## [0.11] - 2021-05-04
### Changed
- `JSONExpect`: switched to use `JSONValidation.isUUID()` for UUID validation
- `pom.xml`: added `json-validation`

## [0.10] - 2021-04-19
### Changed
- `pom.xml`: updated `surefire` plugin to ensure tests are run in Maven
- `pom.xml`: bumped dependency
- `README`: general improvements

## [0.9] - 2020-11-01
### Changed
- `JSONExpect`: switched to use `toSimpleValue()` from `jsonutil` 4.1
- `pom.xml`: updated build dependency (Dokka)

## [0.8] - 2020-09-16
### Changed
- `JSONExpect`: added `showNode()`
- `JSONExpect`: added `oneOf()` and `test()`
- split unit tests into multiple classes
- updated to Kotlin 1.4.0

## [0.7] - 2020-05-10
### Changed
- `JSONExpect`: added new tests `string`, `integer`, `longInteger`, `decimal`, `boolean`
- `JSONExpect`: changed long and decimal tests to allow widening of int or long
- `JSONExpect`: added decimal scale tests
- `pom.xml`: updated dependency version of `jsonutil`
- `README.md`: greatly expanded

## [0.6] - 2020-04-30
### Changed
- `JSONExpect`: added `propertyPresent`, `nonNull`
- `JSONExpect`: added `localTime`, `monthDay`

## [0.5] - 2020-04-23
### Changed
- Removed dependency on `json-kotlin`

## [0.4.1] - 2020-04-21
### Changed
- `pom.xml`: updated dependency version

## [0.4] - 2020-04-16
### Changed
- `JSONExpect`: `count` may now be specified as an `IntRange`
- `JSONExpect`: `length` checks the length of a string
- `JSONExpect`: `uuid` checks a string as a UUID
- `JSONExpect`: `localDate` etc. checks a string as a `java.time.xxx` class
- `JSONExpect`: added checks against `Regex`

## [0.3] - 2020-04-16
### Changed
- `JSONExpect`: improved error messages
- `JSONExpect`: all tests now strongly-typed
- `JSONExpect`: added ability to specify value as a collection
- `JSONExpect`: added ability to specify value as an `IntRange`, `LongRange` or `ClosedRange`
- `JSONExpect`: added `propertyAbsentOrNull`

## [0.2] - 2020-04-13
### Changed
- `JSONExpect`: complete rewrite; much simpler and faster

## [0.1] - 2020-04-13
### Added
- `JSONExpect`: initial version
- `JSONExpectTest`: initial version
