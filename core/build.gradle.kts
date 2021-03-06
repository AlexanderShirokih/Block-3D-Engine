import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.10"
}

val lwjglVersion = "3.2.3"
val lwjglNatives = OperatingSystem.current().run {
    if (isLinux || isWindows) "natives-${familyName}"
    else throw Error("Unrecognized or unsupported Operating system.")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)

    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("com.charleskorn.kaml:kaml:0.26.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")

    implementation("im.bci:pngdecoder:0.13")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}