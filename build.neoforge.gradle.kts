import kotlinx.coroutines.withTimeout
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.gradle.kotlin.dsl.assign
import java.time.LocalDateTime

plugins {
    id("net.neoforged.moddev")
    id ("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
    id("dev.kikugie.fletching-table.neoforge")
    id("maven-publish")
    id ("net.darkhax.curseforgegradle")
    id("com.modrinth.minotaur")
}

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = prop("mod.version")
        this["minecraft"] = prop("deps.minecraft")
        this["scminecraft"] = sc.current.version
        this["modgroup"] = prop("mod.group")
        this["modid"] = prop("mod.id")
        this["modname"] = prop("mod.name")
        this["displayurl"] = prop("display.url")
        this["authors"] = prop("display.authors")
        this["description"] = prop("display.description_base") + "\nMod run as ${prop("deps.minecraft")}"
        this["license"] = prop("display.license")
        this["license_text"] = prop("display.license_text")
        this["issues_track"] = prop("display.issues_track")
        this["update_json_url"] = prop("display.update_json_url")
        this["credits"] = prop("display.credits")
    }

    filesMatching(listOf(
        "fabric.mod.json",
        "META-INF/neoforge.mods.toml",
        "META-INF/mods.toml"
    )) {
        expand(props)
    }
}

fletchingTable {
    accessConverter.register(sourceSets["main"]) {
        // Access widener file relative to `src/main/resources`
        if (sc.eval(sc.current.version, ">= 1.20.1")){
            add(
                "${property("deps.minecraft")}-ccbtweaks.accesswidener",
                "META-INF/${property("deps.minecraft")}-accesstransformer.cfg"
            )
        }
    }
    j52j.register("main") {
        extension("json", "assets/**/*.json5")
        extension("mcmeta", "pack.json5")
    }
}
var loaderName = "neoforge"

val localTimeNow: LocalDateTime? = LocalDateTime.now()
/*val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
val date = current.format(formatter)*/
var isStable = false;
if (providers.environmentVariable("RELEASE_STAT").isPresent)
    isStable = providers.environmentVariable("RELEASE_STAT").get().equals("STABLE")
else isStable = env.RELEASE_STAT.orElse("UNSTABLE").equals("STABLE")
if (isStable)
    version = "${property("mod.version")}+${property("deps.minecraft")}-" + loaderName
else if (localTimeNow != null)
    version = "${property("mod.version")}+${property("deps.minecraft")}-" + loaderName + "+" + localTimeNow.year + "." + localTimeNow.month.value + "." + localTimeNow.dayOfMonth + "+" + localTimeNow.hour + "." + localTimeNow.minute + "." + localTimeNow.second + "+" + localTimeNow.nano
else version = "${property("mod.version")}+${property("deps.minecraft")}-" + loaderName + "+UNSTABLE"
base.archivesName = property("mod.id") as String
base.archivesName = property("mod.id") as String

println("sc:" + sc.current.version + "mc:" + property("deps.minecraft") as String + loaderName)

println("Is Release Stat: " + providers.environmentVariable("RELEASE_STAT").getOrElse("UNSTABLE"));
println("Is Stable: $isStable")

/*System.setProperty("systemProp.socks.proxyHost", "127.0.0.1")
System.setProperty("systemProp.socks.proxyPort", "10808")*/

jsonlang {
    languageDirectories = listOf("assets/${property("mod.id")}/lang")
    prettyPrint = true
}

neoForge {
    version = property("deps.neoforge") as String
    validateAccessTransformers = true

    accessTransformers.from("../../src/main/resources/META-INF/${sc.current.version}-accesstransformer.cfg")

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        register("client") {
            gameDirectory = file("run/")
            client()
            devLogin = true
        }
        register("server") {
            gameDirectory = file("run/")
            server()
        }

    }

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets["main"])
        }
    }
    sourceSets["main"].resources.srcDir("src/main/generated")
}

stonecutter {
//    dependencies["neoforge"] = property("deps.neoforge") as String

//    swaps["resource_location"] = when {
//        eval(current.version, "<1.21") -> "new ResourceLocation($1,$2);"
//        else -> "ResourceLocation.fromNamespaceAndPath($1,$2);"
//    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://maven.parchmentmc.org") {
        name = "ParchmentMC"
    }
    maven(url = "https://maven.izzel.io/releases/") {
        name = "IzzelAliz Maven"
    }
    maven(url = "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") {
        name = "Fuzs Mod Resources"
    }
    maven { url = uri("https://maven.bawnorton.com/releases") }

    // Mirror
    maven { url = uri("https://maven.enjarai.dev/mirrors") }

    maven { url = uri("https://jitpack.io") }

    maven { url = uri("https://maven.fallenbreath.me/releases") }

    maven(url = "https://api.modrinth.com/maven") {
        name = "Modrinth Maven"
    }
    maven ( url = "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/" )
    maven ( url = "https://maven.createmod.net" ) // Create, Ponder, Flywheel
    maven ( url = "https://maven.ithundxr.dev/snapshots" ) // Registrate
    maven ( url = "https://maven.mrjulsen.net" ) // DragonLib
    maven ( url = "https://maven.architectury.dev/")
}

dependencies {
    annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-common:${property("deps.mixinsquared")}")?.let { compileOnly(it) }
    jarJar("com.github.bawnorton.mixinsquared:mixinsquared-neoforge:${property("deps.mixinsquared")}")?.let { implementation(it) }

    implementation ("me.fallenbreath:conditional-mixin-neoforge:${property("deps.conditional")}")
    jarJar ("me.fallenbreath:conditional-mixin-neoforge:${property("deps.conditional")}")

    implementation("maven.modrinth:create-railways-navigator:${property("deps.createrailwaynagivator")}")

    implementation("com.simibubi.create:create-${property("deps.minecraft")}:${property("deps.create")}:slim") { isTransitive = false }
    implementation("net.createmod.ponder:ponder-neoforge:${property("deps.ponder")}+mc${property("deps.minecraft")}")
    compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${property("deps.minecraft")}:${property("deps.flywheel")}")
    runtimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${property("deps.minecraft")}:${property("deps.flywheel")}")
    implementation("com.tterrag.registrate:Registrate:${property("deps.registrate")}")

    implementation("de.mrjulsen.mcdragonlib:dragonlib-neoforge:${property("deps.minecraft")}-${property("deps.dragonlib")}") {
        exclude(group = "fuzs.forgeconfigapiport")
    }
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/mods.toml")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5 <26.1")) {
        JavaVersion.VERSION_21
    } else if (sc.eval(sc.current.version, ">=26.1")) {
        JavaVersion.VERSION_25
    } else {
        JavaVersion.VERSION_17
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat

    manifest {
        attributes(
            if (providers.environmentVariable("RELEASE_STAT").isPresent)
                Pair("ReleaseStat", providers.environmentVariable("RELEASE_STAT").get())
            else
                Pair("ReleaseStat" , env.RELEASE_STAT.orElse(""))
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = property("mod.group") as String
            artifactId = base.archivesName.get() + "-" + loaderName + "-MC" + property("deps.minecraft")
            var releaseStat: String = if (providers.environmentVariable("RELEASE_STAT").isPresent)
                providers.environmentVariable("RELEASE_STAT").get()
            else
                env.RELEASE_STAT.orElse("")
            if (releaseStat.equals("STABLE"))
                version =
                    project.version as String?
            else if (localTimeNow != null) {
                version =
                    property("mod.version") as String + "+" + property("deps.minecraft") as String + "-" + loaderName + "+" + localTimeNow.year + "." + localTimeNow.month.value + "." + localTimeNow.dayOfMonth + "+" + localTimeNow.hour + "." + localTimeNow.minute + "." + localTimeNow.second + "+" + localTimeNow.nano
            }
            else version =
                project.version as String? + "UNSTABLE"
            from(components["java"])
        }
    }
    repositories {
        maven {
            if (isStable)
                url = uri("https://repos.jerrya.top/releases/")
            else url = uri("https://repos.jerrya.top/snapshots/")

            credentials {
                if (providers.environmentVariable("MAVEN_USERNAME").isPresent)
                    username = providers.environmentVariable("MAVEN_USERNAME").get()
                else username = env.MAVEN_USERNAME.orNull()
                if (providers.environmentVariable("MAVEN_PWD").isPresent)
                    password = providers.environmentVariable("MAVEN_PWD").get()
                else password = env.MAVEN_PWD.orNull()
            }
        }
    }
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.jar.map { it.archiveFile.get() }

    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    type = BETA
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version} NeoForge"
    version = "${property("mod.version")}+${property("deps.minecraft")}-" + loaderName
    if (sc.eval(sc.current.version, "1.21.1"))
        changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    else changelog = ""

    modLoaders.add(loaderName)

    github {
        repository = "0x724a/ccb"
        if (providers.environmentVariable("GITHUB_API_TOKEN").isPresent) {
            accessToken = providers.environmentVariable("GITHUB_API_TOKEN")
        } else accessToken = env.GITHUB_API_TOKEN.orNull()
        commitish = "ccbtweaks/multi-dev"
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        if (providers.environmentVariable("CURSEFORGE_API_KEY").isPresent) {
            accessToken = providers.environmentVariable("CURSEFORGE_API_KEY")
        } else accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        if (sc.eval(sc.current.version, "!=1.21.5 <1.21.11"))
            optional("modern-ui")
    }

}

modrinth {
    println("sc:" + sc.current.version + "mc:" + property("deps.minecraft") as String + additionalVersions + "modrinth now.")

    projectId.set(property("publish.modrinth") as String)
    file = tasks.jar.map { it.archiveFile.get() }

    additionalFiles.add(tasks.named("sourcesJar"))

    if (providers.environmentVariable("MODRINTH_TOKEN").isPresent)
        token = providers.environmentVariable("MODRINTH_TOKEN")
    else token = env.MODRINTH_API_KEY.orNull()

    versionType.set("beta")
    versionName.set("${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version} NeoForge")
    versionNumber.set("${property("mod.version")}+${property("deps.minecraft")}-" + "neo")

    if (sc.eval(sc.current.version, "1.21.1"))
        changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    else changelog = ""

    loaders.add(loaderName)


    uploadFile.set(tasks.jar)

    additionalFiles.add(tasks.named("sourcesJar"))

    gameVersions.add(property("deps.minecraft") as String)
    gameVersions.addAll(additionalVersions)

    dependencies {
        if (sc.eval(sc.current.version, "!=1.21.5 <1.21.11"))
            optional.project("modern-ui")
    }
}

tasks.register<TaskPublishCurseForge>("curseforge") {

    group = "publishing"
    var mainFile = upload(project.property("publish.curseforge"), tasks.jar)

    if (providers.environmentVariable("CURSEFORGE_API_KEY").isPresent) {
        apiToken = providers.environmentVariable("CURSEFORGE_API_KEY")
    } else apiToken = env.CURSEFORGE_API_KEY.orNull()

    var verList = ArrayList<String>();

    verList.add(stonecutter.current.version)

    additionalVersions.forEach {
        v -> run {
            verList.add(v)
        }
    }

    verList.forEach { v -> mainFile.addGameVersion(v) }

    mainFile.addGameVersion()

    mainFile.releaseType = "beta"
    mainFile.changelogType = "markdown"
    //mainFile.addIncompatibility("")
    mainFile.addOptional("modern-ui")

    mainFile.displayName = "${project.property("mod.name")} ${project.property("mod.version")} for ${stonecutter.current.version} NeoForge"
    version = "${project.property("mod.version")}+${project.property("deps.minecraft")}-" + loaderName
    if (sc.eval(sc.current.version, "1.21.1"))
         mainFile.changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    else mainFile.changelog = ""

    mainFile.addModLoader(loaderName)

    // The sources JAR
    var sourcesFile = mainFile.withAdditionalFile(tasks.named("sourcesJar"))
    //sourcesFile.changelog = file('changelog.md')

    // The JavaDoc JAR
    /*var javadocFile = mainFile.withAdditionalFile(javadocJar)
    javadocFile.changelog = 'This is a test JavaDoc file!'
    javadocFile.displayName = "JavaDoc - ${version}"*/
}

/*modrinth {
    projectId = property("publish.modrinth") as String
    if (providers.environmentVariable("MODRINTH_TOKEN").isPresent)
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
    else accessToken = env.MODRINTH_API_KEY.orNull()
    minecraftVersions.add(stonecutter.current.version)
    minecraftVersions.addAll(additionalVersions)
    optional("modern-ui")
}*/
/*

curseforge {
    projectId = property("publish.curseforge") as String
    if (providers.environmentVariable("CURSEFORGE_API_KEY").isPresent) {
        accessToken = providers.environmentVariable("CURSEFORGE_API_KEY")
    } else accessToken = env.CURSEFORGE_API_KEY.orNull()
    minecraftVersions.add(stonecutter.current.version)
    minecraftVersions.addAll(additionalVersions)
    optional("modern-ui")
}
*/
