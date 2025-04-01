# detekt custom rule 

Opinionated rule set for Detekt, primarily for Spring boot application.

## Get started

```groovy
plugins {
    id 'io.gitlab.arturbosch.detekt' version '<detekt-version>'
}

dependencies {
    detektPlugins('com.github.ablil.detekt:detekt-custom-rule:<version>')
}
```

## Rules


**UseAssertJOnly**

AssertJ is a rich library with utilities assertion functions for Java/Kotlin, this rules ensure consistency on the codebase
by reporting any usage of assertions functions from any library other than AssertJ.

