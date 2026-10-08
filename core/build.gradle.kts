plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.kotlin.stdlib.jdk8)
    testImplementation(libs.paper.api)
    testImplementation(libs.kotlin.stdlib.jdk8)
}

kotlin {
    jvmToolchain(25)
}

val sessionCheck =
    tasks.register<JavaExec>("sessionCheck") {
        group = "verification"
        classpath = sourceSets.test.get().runtimeClasspath
        mainClass.set("CompassSessionCheck")
    }

tasks.check {
    dependsOn(sessionCheck)
}
