plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = AppConfig.jdk
    targetCompatibility = AppConfig.jdk
}