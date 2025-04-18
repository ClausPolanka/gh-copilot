plugins {
    kotlin("jvm") version "2.0.0" // Or your desired Kotlin version
    id("io.ktor.plugin") version "2.3.12" // Use the latest Ktor plugin version
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Core
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    // Authentication
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    // HTML Templating with kotlinx.html
    implementation("io.ktor:ktor-server-html-builder-jvm")
    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.6") // Or your preferred logging implementation
    // Testing (Optional but recommended)
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation(kotlin("test-junit"))
}