plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.0-Beta1"
    id("com.github.ben-manes.versions") version "0.52.0"
    idea
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
}

tasks.test {
    useJUnitPlatform()
}