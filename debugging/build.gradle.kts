plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.lwjgl", "lwjgl-opengl", "3.2.3")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}