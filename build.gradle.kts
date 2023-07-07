import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.devtools.ksp") version "1.9.0-1.0.11"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.serialization") version "1.9.0"
    id("dev.schlaubi.mikbot.gradle-plugin") version "3.19.8"
}

group = "net.stckoverflw"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}

mikbotPlugin {
    description.set("Simple Bansystem Plugin")
    provider.set("StckOverflw")
    license.set("MIT")
}
