import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.41") apply true
    id("org.jlleitschuh.gradle.ktlint").version("8.2.0")
    id("org.jlleitschuh.gradle.ktlint-idea").version("8.2.0")
    id("org.owasp.dependencycheck") version "5.2.0"
    id("io.gitlab.arturbosch.detekt").version("1.0.0-RC16")
    maven
    jacoco
    idea
    java
}

val newRelicVersion = "5.2.0"
val newRelic by configurations.creating {
    extendsFrom(configurations["implementation"])
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "jacoco")
    apply(plugin = "maven")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jlleitschuh.gradle.ktlint-idea")
    apply(plugin = "org.owasp.dependencycheck")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    dependencies {
        runtimeClasspath("com.newrelic.agent.java:newrelic-agent:$newRelicVersion")
    }

    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = false
            csv.isEnabled = false
            html.destination = file("$buildDir/jacocoHtml")
        }
    }

    ktlint {
        version.set("0.34.0")
        debug.set(true)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
        // additionalEditorconfigFile.set(file("/some/additional/.editorconfig"))
        kotlinScriptAdditionalPaths {
            include(fileTree("scripts/"))
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    tasks.test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

jacoco {
    toolVersion = "0.8.4"
}

allprojects {
    apply(plugin = "idea")
    group = "com.peya"
    version = "1.0.0"
    repositories {
        jcenter()
        mavenLocal()
        mavenCentral()
    }
}

tasks.getByName<Test>("test") {
    extensions.configure(JacocoTaskExtension::class) {
        isEnabled = true
        includes = listOf()
        excludes = listOf()
        excludeClassLoaders = listOf()
        isIncludeNoLocationClasses = true
        sessionId = "<auto-generated value>"
        isDumpOnExit = true
        classDumpDir = null
        output = JacocoTaskExtension.Output.FILE
        address = "localhost"
        port = 6300
        isJmx = false
    }
}

dependencies {
    newRelic("com.newrelic.agent.java:newrelic-agent:$newRelicVersion")
}

tasks.register<Copy>("copyNewRelic") {
    group = "build"
    description = "Add custom libraries to build our docker"

    from(configurations.get("newRelic").asPath)
    into("$buildDir/libs")
    rename { it.substring(0, it.indexOf("-")) + it.substring(it.lastIndexOf("."), it.length) }

    doLast {
        println("NewRelic copied!")
    }
}

tasks.getByName("assemble").dependsOn("copyNewRelic")
tasks.getByName("jacocoTestReport").dependsOn("test")
