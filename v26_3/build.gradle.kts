plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.paperweight.userdev)
}

dependencies {
    implementation(project(":core"))
    compileOnly(libs.kotlin.stdlib.jdk8)
    paperweight.paperDevBundle(libs.versions.paper.get())
    testImplementation(libs.kotlin.stdlib.jdk8)
    testRuntimeOnly(libs.commons.lang3)
}

kotlin {
    jvmToolchain(25)
}

val waypointPacketCheck =
    tasks.register<JavaExec>("waypointPacketCheck") {
        group = "verification"
        classpath = sourceSets.test.get().runtimeClasspath
        mainClass.set("WaypointPacketCheck")
        workingDir =
            layout.buildDirectory
                .dir("waypoint-packet-check")
                .get()
                .asFile

        doFirst { workingDir.mkdirs() }
    }

tasks.check {
    dependsOn(waypointPacketCheck)
}
