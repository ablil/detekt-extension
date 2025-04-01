# detekt custom rule 

Opinionated rule set for Detekt, primarily for Spring boot application.

## Get started

```groovy
plugins {
    id 'io.gitlab.arturbosch.detekt' version '<detekt-version>'
}

repositories {
    mavenCentral()
    maven {
        name = "GithubPackage"
        url = uri("https://maven.pkg.github.com/ablil/detekt-extension")
        credentials {
            username = project.findProperty("gpr.username") as String ?: System.getenv('GITHUB_ACTOR')
            password = project.findProperty("gpr.password") as String ?: System.getenv('GITHUB_TOKEN')
        }
    }
}

dependencies {
    detektPlugins('com.github.ablil.detekt:detekt-custom-rule:<version>')
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
