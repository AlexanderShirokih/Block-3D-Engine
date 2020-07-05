plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "ru.aleshi.block3d.samples.SamplesMain"
}

dependencies {
    implementation(project(":core"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("org.lwjgl", "lwjgl-opengl", "3.2.3")
    implementation("org.lwjgl", "lwjgl-stb", "3.2.3")
}