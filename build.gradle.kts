import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "1.9.0"
}

group = "cn.xor7"
version = "1.1.4"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://maven.aliyun.com/repository/public")
    maven("https://repo.leavesmc.top/releases")
    maven("https://repo.leavesmc.top/snapshots")
    maven("https://repo.codemc.org/repository/maven-public/")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    compileOnly(files("libs/ThemisAPI_0.15.3.jar"))
    compileOnly(files("libs/Matrix_7.7.15A.jar"))
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    compileOnly("top.leavesmc.leaves:leaves-api:1.20.4-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-bukkit-shade:9.3.0")
    implementation("dev.jorel:commandapi-bukkit-kotlin:9.3.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
}

val targetJavaVersion = 17
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
}

kotlin {
    jvmToolchain(17)
}