plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.resource.factory.paper)
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.kotlin.stdlib.jdk8)
    implementation(project(":core"))
    implementation(project(":v26_3"))
}

kotlin {
    jvmToolchain(25)
}

val paperLibraryFile = layout.buildDirectory.file("generated/paper-library")
val paperLibraryContent =
    libs.bundles.paper.library
        .get()
        .joinToString("\n") { it.toString() }

val generatePaperLibrary =
    tasks.register("generatePaperLibrary") {
        inputs.property("content", paperLibraryContent)
        outputs.file(paperLibraryFile)
        doLast {
            val file = paperLibraryFile.get().asFile
            file.parentFile.mkdirs()
            file.writeText(paperLibraryContent)
        }
    }

tasks.jar {
    archiveClassifier.set("dev")
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    dependsOn(generatePaperLibrary)
    from(paperLibraryFile)
    archiveBaseName.set("ClientCompass")
    archiveClassifier.set("")
    destinationDirectory.set(rootProject.layout.projectDirectory.dir("_dist"))
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

paperPluginYaml {
    name = "ClientCompass"
    version = project.version.toString()
    main = "kr.modless.compass.ClientCompass"
    loader = "kr.modless.compass.ClientCompassPluginLoader"
    author = "murinn@Hwaryuh"
    description = "just for fun ㅋㅋ"
    apiVersion = "26.3"
}
