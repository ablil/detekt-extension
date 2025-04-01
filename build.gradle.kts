plugins {
    kotlin("jvm") version "2.1.20"
    `maven-publish`
}

group = "com.github.ablil.detekt"
version = "0.0.1"

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.8")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.8")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    systemProperty("compile-snippet-tests", project.hasProperty("compile-test-snippets"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GithubPackage"
            url = uri("https://maven.pkg.github.com/ablil/detekt-extension")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
