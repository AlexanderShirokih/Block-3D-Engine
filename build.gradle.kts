plugins {
    base
    kotlin("jvm") version "1.3.72" apply false
}

allprojects {
    group = "ru.aleshi"
    version = "0.1-dev"

    repositories {
        mavenCentral()
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}