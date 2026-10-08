plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktlint) apply false
}

val ktlintVersion = libs.versions.ktlint.get()

subprojects {
    group = "kr.modless.compass"
    version = "0.1.0"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    tasks.withType<Test>().configureEach {
        failOnNoDiscoveredTests.set(false)
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set(ktlintVersion)
        filter { exclude { it.file.path.contains("${File.separator}build${File.separator}") } }
    }
}

tasks.register("buildPlugin") {
    group = "build"
    dependsOn(":paper:shadowJar")
}

defaultTasks("buildPlugin")
