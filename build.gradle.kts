import net.minecraftforge.gradle.userdev.UserDevExtension

val modName: String by ext.properties
val modGroup: String by ext.properties
val modVersion: String by ext.properties
val mcVersion: String by ext.properties
val mcpChannel: String by ext.properties
val mcpVersion: String by ext.properties
val forgeVersion: String by ext.properties
val repositoryLink: String by ext.properties
val kotlinVersion: String by ext.properties
val annotationsVersion: String by ext.properties
val coroutinesVersion: String by ext.properties
val modDescription: String = "Kotlin helper library for Minecraft Forge."

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://maven.minecraftforge.net/")
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:5.1.+") {
            isChanging = true
        }
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
    }
}

plugins {
    java
    kotlin("jvm") version "1.5.30"
    id("net.kyori.blossom") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

apply(plugin = "net.minecraftforge.gradle")

version = modVersion
group = modGroup

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

configure<UserDevExtension> {

    mappings(mcpChannel,  mcpVersion)

}

repositories {
    mavenCentral()
}

val minecraft by configurations

dependencies {
    minecraft("net.minecraftforge:forge:$forgeVersion")
    shadow("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    shadow("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    shadow("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    shadow("org.jetbrains:annotations:$annotationsVersion")
    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
}

blossom {
    replaceTokenIn("src/main/kotlin/net/shadowfacts/forgelin/Forgelin.kt")
    replaceToken("@version@", modVersion)
}

tasks {

    shadowJar {
        archiveBaseName.set(modName)
        archiveClassifier.set("")
        archiveVersion.set(modVersion)
        configurations = listOf(project.configurations.shadow.get())

        exclude("module-info.class", "META-INF/maven/**", "META-INF/proguard/**", "META-INF/versions/**")

        finalizedBy("reobfJar")
    }

    artifacts {
        archives(shadowJar)
        shadow(shadowJar)
    }

    processResources {
        filesMatching("mcmod.info") {
            expand(
                "version" to modVersion,
                "mcversion" to mcVersion,
                "modname" to modName,
                "modid" to modName.toLowerCase(),
                "modname" to modName,
                "link" to repositoryLink,
                "description" to modDescription,
                "authorList" to arrayOf("shadowfacts", "Licious" )
            )
        }
    }



}
