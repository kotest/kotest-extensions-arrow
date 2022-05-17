# kotest-extensions-arrow

Kotest extension for [Arrow](https://arrow-kt.io/).

See [docs](https://kotest.io/docs/assertions/arrow.html).

Please create issues on the main kotest [board](https://github.com/kotest/kotest/issues).

[![Build Status](https://github.com/kotest/kotest-extensions-arrow/workflows/master/badge.svg)](https://github.com/kotest/kotest-extensions-arrow/actions)
[<img src="https://img.shields.io/maven-central/v/io.kotest.extensions/kotest-assertions-arrow.svg?label=latest%20release"/>](http://search.maven.org/#search|ga|1|kotest-assertions-arrow)
![GitHub](https://img.shields.io/github/license/kotest/kotest-extensions-arrow)
[![kotest @ kotlinlang.slack.com](https://img.shields.io/static/v1?label=kotlinlang&message=kotest&color=blue&logo=slack)](https://kotlinlang.slack.com/archives/CT0G9SD7Z)
[<img src="https://img.shields.io/nexus/s/https/oss.sonatype.org/io.kotest.extensions/kotest-assertions-arrow.svg?label=latest%20snapshot"/>](https://oss.sonatype.org/content/repositories/snapshots/io/kotest/extensions/kotest-assertions-arrow/)

## How to use it

```kotlin
depedencies {
  implementation("io.kotest.extensions:kotest-assertions-arrow:<version>")
  implementation("io.kotest.extensions:kotest-assertions-arrow-fx-coroutines:<version>")
}
```

for property-based testing:

```kotlin
dependencies {
  implementation("io.kotest.extensions:kotest-property-arrow:<version>")
  // optional: the following includes optics related Laws
  implementation("io.kotest.extensions:kotest-property-arrow-optics:<version>")
}
```

Note:
Please add `io.arrow-kt:arrow-core:arrow-version`, `io.arrow-kt:arrow-fx-coroutines:arrow-version` or `io.arrow-kt:arrow-optics:arrow-version`, if they're missing in your classpath.
Otherwise, it will lead to unresolved Reference errors.
In the form of: "Cannot access class `arrow.core.Either` Check your module classpath for missing or conflicting dependencies."
The project is not shipping the arrow jars because this leads to dependency conflicts and further adjusting the dependency graph.

## Changelog

### 1.3.0

- Update to kotest 5.3.0
- Update kotlin to 1.6.21
- Add assertion module for arrow-fx-coroutines with combinators related to `Resource` and `ExitCase`
- Add `Either.rethrow`

### 1.2.5

- Upgrade to 5.2.3 and update kotlinx-coroutines to 1.6.1

### 1.2.4

- Upgrade to 5.2.1 and restores compatibilty with 5.2.X series https://github.com/kotest/kotest-extensions-arrow/pull/149

### 1.2.3

- fix linking error in native platforms [#140](https://github.com/kotest/kotest-extensions-arrow/issues/140)

### 1.2.2

* update kotest to 5.1.0
* update kotlin to 1.6.10
* publish missing multiplatform targets affecting `1.2.1` and `1.2.0`

### 1.2.1

* Added Arb.valid and Arb.invalid
* Added Arb.nel(arb, range) - a variant of Arb.nel(arb) that accepts a range parameter

### 1.2.0

* Upgrade to Arrow 1.0.1
* Multiplatform artifacts for `kotest-assertions-arrow`, `kotest-property-arrow` and `kotest-property-arrow-optics`
* `#2670` Replace explicit usage of eq with should be from kotest assertion core
* `testLaws` has `RootScope` as a receiver instead of `RootContext`

### 1.1.1

* removes deprecated members in `kotest-assertions-arrow`

### 1.1.0

**Note that from this release, the minimium requirements are Kotest 5.0+ and Kotlin 1.6**

* Update to Arrow 1.0.0
* fix Java 1.8 compatibility [#2437](https://github.com/kotest/kotest/issues/2437)
* Added `kotest-property-arrow` and `kotest-property-arrow-optics` for property-based testing with Arrow
* includes deprecation cycle of 1.0.3
* remove dependency to kotlinX-coroutines and kotest-property in `kotest-assertions-arrow`


### 1.0.3

* Update to Arrow 0.13.2
* Added a deprecation cycle for previous descriptors in 1.0.2 in favor of smart-casted variants in `io.kotest.assertions.arrow.core`
* Reorg existing functions to `io.kotest.assertions.arrow.core`
* Not leaking the arrow dependency into the runtime of users
* Added Arb<Either<A, B>, Arb<Validated<E, A>>

### 1.0.2

* Updated to arrow 0.13.1
* Updated option to support nullables
