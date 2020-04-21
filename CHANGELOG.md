# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

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
