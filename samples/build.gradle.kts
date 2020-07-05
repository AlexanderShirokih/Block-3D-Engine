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
}