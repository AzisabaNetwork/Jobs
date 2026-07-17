plugins {
    `java-library`
    `maven-publish`
}

group = "Jobs"
version = "5.2.6.7-Azisaba"

defaultTasks("clean", "install")

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.momirealms.net/releases/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://jitpack.io")
    maven("https://nexus.neetgames.com/repository/maven-releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.bg-software.com/repository/api/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://repo.codemc.io/repository/EvenMoreFish/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
        exclude(group = "org.bukkit", module = "craftbukkit")
    }
    compileOnly("io.lumine:Mythic-Dist:5.6.1")
    compileOnly("com.github.placeholderapi:placeholderapi:2.11.6") {
        exclude(group = "me.rayzr522", module = "jsonmessage")
        exclude(group = "org.jetbrains", module = "annotations")
    }
    compileOnly("com.github.Zrips:CMILib:1.5.9.6")
    compileOnly("net.momirealms:custom-fishing:2.3.4")
    compileOnly("dev.rosewood:rosestacker:1.5.40")
    compileOnly("com.oheers.evenmorefish:even-more-fish-api:2.3.6")
    compileOnly(files("libs/mypet-3.12.jar"))
    compileOnly(files("libs/PyroFishingPro-4.9.1.jar"))

    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.2.051") {
        exclude(group = "com.sk89q.worldguard")
    }
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.12") {
        isTransitive = false
        exclude(group = "org.bukkit", module = "bukkit")
        exclude(group = "org.sk89q.bukkit", module = "bukkit-classloader-check")
        exclude(group = "com.sk89q", module = "commandbook")
        exclude(group = "org.bstats", module = "bstats-bukkit")
        exclude(group = "io.papermc", module = "paperlib")
    }
    compileOnly("com.sk89q.worldguard:worldguard-core:7.0.12") {
        isTransitive = false
    }
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.10") {
        isTransitive = false
        exclude(group = "org.sk89q.bukkit", module = "bukkit-classloader-check")
        exclude(group = "org.bstats", module = "bstats-bukkit")
        exclude(group = "io.papermc", module = "paperlib")
        exclude(group = "com.sk89q.worldedit.worldedit-libs", module = "bukkit")
        exclude(group = "net.java.truevfs", module = "truevfs-profile-default_2.13")
        exclude(group = "org.apache.logging.log4j", module = "log4j-slf4j-impl")
        exclude(group = "org.bukkit", module = "bukkit")
    }
    compileOnly("com.sk89q.worldedit:worldedit-core:7.3.10") {
        isTransitive = false
    }
    compileOnly("com.bgsoftware:WildStackerAPI:3.8.0")
    compileOnly("uk.antiperson.stackmob:StackMob:5.8.2")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.processResources {
    filesMatching("plugin.yml") {
        filter { line -> line.replace("\${project.version}", project.version.toString()) }
    }
}

tasks.jar {
    archiveFileName.set("Jobs${project.version}.jar")
}

publishing {
    repositories {
        maven {
            name = "repo"
            credentials(PasswordCredentials::class)
            url = uri(if (project.version.toString().endsWith("SNAPSHOT")) {
                project.findProperty("deploySnapshotURL")
                    ?: System.getProperty("deploySnapshotURL", "https://repo.azisaba.net/repository/maven-snapshots/")
            } else {
                project.findProperty("deployReleasesURL")
                    ?: System.getProperty("deployReleasesURL", "https://repo.azisaba.net/repository/maven-releases/")
            },
            )
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}