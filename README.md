# detekt custom rule 

Opinionated rule set for Detekt, primarily for Spring boot application.

## Get started

```groovy
plugins {
    id 'io.gitlab.arturbosch.detekt' version '<detekt-version>'
}

repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    detektPlugins('com.github.ablil:detekt-extension:<version>')
}
```

## Rules


**UseAssertJOnly**

AssertJ is a rich library with utilities assertion functions for Java/Kotlin, this rules ensure consistency on the codebase
by reporting any usage of assertions functions from any library other than AssertJ.

**DescriptiveTestFunctions**

To ensure having descriptive and expressive function names, this rules validate the test functions names against one of these patterns:
* *should* check something
* *should* check something *when* something happens
* *should* check something *given* something
* *given* something *then* check something
* *given* something *when* something happens *then* check something

**NoDataClassEntity**

This rule prevent usage of kotlin [data classes](https://kotlinlang.org/docs/data-classes.html) with JPA entities, for the following reasons:

* JPA needs *No-Args constructor* or at least an accessible one
* data class generate *toString* based on all fields, which might lead to a potential *N + 1* problem
* Most JPA provider expect the entity class to be *mutable*, to mutate them through lazy initialization, proxies ...

**ArgumentsOfSameType**

This rule reports call of method with parameters of same type but without named arguments, which quite 
often lead to confusion or swapping them by mistake.

**JPAEagerLoading**

This rule report usage of **eagerly** loading strategy for JPA association, since it's impact performance.

Read more about it here [eager fetching is a code smell](https://vladmihalcea.com/eager-fetching-is-a-code-smell/)

## Development

### How to add new rules

1. **define** rule class
2. write **test** for defined rule
3. add rule to **rule set provider**
4. update *config.yml* with the new rule and its parameters
5. update *README* description with new rule, include short description and describe parameters if required
6. push new vX.Y.Z tag to create new **Github release** (automated workflow)
