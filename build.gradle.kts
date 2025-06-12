plugins {
    kotlin("jvm") version "2.1.20"
}

group = "org.vexon"
version = "1.0.0"

val imguiVersion = "1.86.10"
val lwjglVersion = "3.3.6"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.spair:imgui-java-binding:$imguiVersion")
    implementation("io.github.spair:imgui-java-lwjgl3:$imguiVersion")

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")

    runtimeOnly("org.lwjgl:lwjgl::natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-opengl::natives-windows")

    runtimeOnly("io.github.spair:imgui-java-natives-windows:${imguiVersion}")

    runtimeOnly("org.lwjgl:lwjgl::natives-linux")
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-linux")
    runtimeOnly("org.lwjgl:lwjgl-opengl::natives-linux")

    runtimeOnly("io.github.spair:imgui-java-natives-linux:$imguiVersion")


    runtimeOnly("org.lwjgl:lwjgl::natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-glfw::natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-opengl::natives-macos")

    runtimeOnly("io.github.spair:imgui-java-natives-macos:${imguiVersion}")
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