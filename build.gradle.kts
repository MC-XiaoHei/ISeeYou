import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    kotlin("jvm") version "1.9.20"
}

group = "cn.xor7"
version = "1.3.8"

val commandAPIVer = "10.1.2"

repositories {
    mavenLocal()
    maven("https://jitpack.io/")
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://maven.aliyun.com/repository/public")
    maven("https://repo.leavesmc.org/releases")
    maven("https://repo.leavesmc.org/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.grim.ac/snapshots")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    //anticheat dependencies
    compileOnly(files("libs/ThemisAPI_0.15.3.jar"))
    compileOnly(files("libs/Matrix_7.15.7.jar"))
    compileOnly(files("libs/VulcanAPI.jar"))
    compileOnly(files("libs/LightAntiCheat.jar"))
    compileOnly(files("libs/SpartanAPI.jar"))
    implementation("ac.grim.grimac:GrimAPI:1.0.0-8ad0b94")
    compileOnly("com.github.Elikill58:Negativity:2.7.1")
    //other dependencies
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    compileOnly("org.leavesmc.leaves:leaves-api:1.21.8-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:${commandAPIVer}")
    implementation("dev.jorel:commandapi-bukkit-kotlin:${commandAPIVer}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("net.jodah:expiringmap:0.5.11")
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand(
            mapOf(
                "version" to version,
            )
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.withType<ShadowJar> {
    relocate("dev.jorel.commandapi", "cn.xor7.iseeyou.commandapi")
    minimize()
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    mergeServiceFiles()
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
}

kotlin {
    jvmToolchain(21)
}