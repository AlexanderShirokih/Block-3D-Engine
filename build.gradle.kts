plugins {
    base
    kotlin("jvm") version "1.4.10" apply false
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