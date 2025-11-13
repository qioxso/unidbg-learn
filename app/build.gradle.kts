plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation(project(":utils"))
    implementation("commons-logging:commons-logging:1.2")
    implementation("org.scijava:native-lib-loader:2.5.0")
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")
    implementation("commons-io:commons-io:2.15.1")
    implementation("com.github.zhkl0228:unicorn:1.0.8")
    implementation("com.github.zhkl0228:keystone:0.9.4")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation(fileTree("libs") { include("*.jar") })
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "com.vro.app.AppKt"
}
