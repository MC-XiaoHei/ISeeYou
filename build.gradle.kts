@file:Suppress("SpellCheckingInspection")

import org.leavesmc.LeavesPluginJson.Load.BEFORE
import org.leavesmc.leavesPluginJson
import xyz.jpenilla.runtask.service.DownloadsAPIService
import xyz.jpenilla.runtask.service.DownloadsAPIService.Companion.registerIfAbsent

plugins {
    kotlin("jvm")
    alias(libs.plugins.leavesweightUserdev)
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.resourceFactory)
}

group = "cn.xor7.xiaohei"
version = "2.0.0"

val pluginJson = leavesPluginJson {
    main = "cn.xor7.xiaohei.icu.ISeeYouPlugin"
    authors.add("MC_XiaoHei")
    description = "record players in .mcpr format"
    website = "https://github.com/MC-XiaoHei/ISeeYou"
    foliaSupported = false // Currently, there is no any known folia forks supports photographer API.
    apiVersion = libs.versions.leavesApi.extractMCVersion()
    dependencies.server(
        name = "CommandAPI",
        joinClasspath = true,
        load = BEFORE,
    )
    listOf(
        "GrimAC",
        "LightAntiCheat",
        "Matrix",
        "Negativity",
        "Spartan",
        "Themis",
        "Vulcan",
    ).forEach {
        dependencies.server(
            name = it,
            joinClasspath = true,
            load = BEFORE,
            required = false,
        )
    }
    features.required.add("photographer")
}

val runServerPlugins = runPaper.downloadPluginsSpec {
    modrinth("commandapi", "epl0dnHR") // 10.1.2-Mojmap
    modrinth("luckperms", "v5.5.0-bukkit")
    modrinth("themis-anti-cheat", "0.17.6")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://repo.leavesmc.org/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.grim.ac/snapshots")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    apply `plugin dependencies`@{
        implementation(kotlin("stdlib-jdk8"))
        implementation(libs.configurateHocon)
        implementation(libs.configurateKotlin)
        compileOnly(libs.commandApiCore)
        compileOnly(libs.commandApiKotlin)
    }

    apply `anti-cheat apis`@{
        compileOnly(files("libs/ThemisAPI_0.15.3.jar"))
        compileOnly(files("libs/Matrix_7.15.7.jar"))
        compileOnly(files("libs/VulcanAPI.jar"))
        compileOnly(files("libs/LightAntiCheat.jar"))
        compileOnly(files("libs/SpartanAPI.jar"))
        compileOnly(libs.negativityApi)
        compileOnly(libs.grimApi)
    }

    apply `api and server source`@{
        compileOnly(libs.leavesApi)
        paperweight.devBundle(libs.leavesDevBundle)
    }
}

sourceSets {
    main {
        resourceFactory {
            factories(pluginJson.resourceFactory())
        }
    }
}

tasks {
    runServer {
        downloadsApiService.set(leavesDownloadApiService())
        downloadPlugins.from(runServerPlugins)
        minecraftVersion(libs.versions.leavesApi.extractMCVersion())
        systemProperty("file.encoding", Charsets.UTF_8.name())
    }

    withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.forkOptions.memoryMaximumSize = "6g"

        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }

    shadowJar {
        archiveFileName = "${project.name}-${version}.jar"
    }

    build {
        dependsOn(shadowJar)
    }
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

kotlin {
    compilerOptions.optIn.add("kotlin.io.path.ExperimentalPathApi")
    jvmToolchain(targetJavaVersion)
}

fun Provider<String>.extractMCVersion(): String {
    val versionString = this.get()
    val regex = Regex("""^(1\.\d+(?:\.\d+)?)""")
    return regex.find(versionString)?.groupValues?.get(1)
        ?: throw IllegalArgumentException("Cannot extract mcVersion from $versionString")
}

fun leavesDownloadApiService(): Provider<out DownloadsAPIService> = registerIfAbsent(project) {
    downloadsEndpoint = "https://api.leavesmc.org/v2/"
    downloadProjectName = "leaves"
    buildServiceName = "leaves-download-service"
}