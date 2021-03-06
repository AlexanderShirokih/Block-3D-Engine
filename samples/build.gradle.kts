plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "ru.aleshi.block3d.samples.SamplesMain"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":debugging"))
    implementation(project(":ui"))

    implementation(kotlin("stdlib-jdk8"))

    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
}