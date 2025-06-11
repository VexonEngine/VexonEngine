plugins {
    kotlin("jvm") version "2.1.20"
}

group = "org.vexon"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

// Run the application
tasks.register<JavaExec>("run") {
    group = "application"
    description = "Runs the Kotlin application"
    mainClass.set("org.vexon.MainKt") // Adjust this to your main class
    classpath = sourceSets["main"].runtimeClasspath
}