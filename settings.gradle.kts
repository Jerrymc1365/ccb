import org.gradle.internal.declarativedsl.project.projectEvaluationSchema

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://files.minecraftforge.net/maven/") { name = "Forge" }
        maven(url = "https://maven.fabricmc.net/") { name = "Fabric" }
        maven(url = "https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven(url = "https://maven.kikugie.dev/snapshots") { name = "KikuGie" }
        maven(url = "https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven(url = "https://maven.parchmentmc.org") { name = "ParchmentMC" }
        maven(url = "https://maven.wagyourtail.xyz/snapshots") { name = "Unimined" }
        maven(url = "https://repo.spongepowered.org/maven/") { name = "Mixin" }
        maven(url = "https://maven.architectury.dev/") { name = "Architectury" }
        maven (url = "https://mcentral.firstdark.dev/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "+"/*"0.9.0"*/
    id("dev.kikugie.stonecutter") version "0.9"/*"0.8-alpha.12"*/
}

stonecutter {

    create(rootProject) {
        fun match(version: String, vararg loaders: String) = loaders.forEach {
            version(
                "$version+$it",
                version
            ).buildscript = "build.$it.gradle.kts"
            /*branch("api") {
                version(
                    "$version+$it",
                    version
                ).buildscript = "build.$it.gradle.kts"
            }*/
        }

        fun matchExtra(version: String, buildscript: String, vararg loaders: String) = loaders.forEach {
            version(
                "${version}$${buildscript}+$it",
                version
            ).buildscript = "build.$buildscript.gradle.kts"
        }

        match("1.21.1", "neoforge")
        vcsVersion = "1.21.1+neoforge"
    }
}
