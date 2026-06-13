plugins {
    id("dev.kikugie.stonecutter") version "0.9"//
    id("dev.kikugie.fletching-table") version "+" apply false //
    id("dev.kikugie.fletching-table.fabric") version "+" apply false //
    id("dev.kikugie.fletching-table.neoforge") version "+" apply false //
    id("co.uzzu.dotenv.gradle") version "+" //
    id("fabric-loom") version "+" apply false //
    id("net.fabricmc.fabric-loom") version "+" apply false //
    id("net.neoforged.moddev") version "+" apply false //
    id("net.neoforged.moddev.legacyforge") version "+" apply false //
    id ("dev.kikugie.postprocess.jsonlang") version "2.1-beta.4" apply false //
    id("me.modmuss50.mod-publish-plugin") version "+" apply false //
    id("com.modrinth.minotaur") version "2.+" apply false
    id ("net.darkhax.curseforgegradle") version "1.1.15" apply false
    //id("io.freefair.lombok") version "9.1.0" apply false //
    //id("xyz.wagyourtail.unimined") version "1.0.0-SNAPSHOT" apply false
    //id("org.spongepowered.mixin") version "0.7.38" apply false
    //id ("dev.architectury.loom") version "1.3-SNAPSHOT" apply false
}

stonecutter active "1.21.1+neoforge"

stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('+'), "neoforge")
    filters.include("**/*.fsh", "**/*.vsh")
}

/*stonecutter tasks {
    order("publishModrinth")
    order("publishCurseforge")
}*/

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}


