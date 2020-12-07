plugins {
    kotlin("jvm")
}

val lwjglVersion = "3.2.3"

val lwjglNatives = org.gradle.internal.os.OperatingSystem.current().run {
    if (isLinux || isWindows) "natives-${familyName}"
    else throw Error("Unrecognized or unsupported Operating system.")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.lwjgl:lwjgl-nanovg:$lwjglVersion")
    runtimeOnly("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
}
