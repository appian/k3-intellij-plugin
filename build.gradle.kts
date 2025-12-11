import org.jetbrains.intellij.platform.gradle.TestFrameworkType

buildscript {
  repositories {
    mavenCentral()
  }
}

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

dependencies {
  intellijPlatform {
    intellijIdea("2025.3")
    bundledPlugin("com.intellij.java")
    testFramework(TestFrameworkType.Platform)
  }
}

plugins {
  id("java")
  id("org.jetbrains.intellij.platform") version "2.10.5"
  id("org.jetbrains.grammarkit") version "2023.3.0.1"
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

intellijPlatform {
  pluginConfiguration {
    version = "1.6.4"
    name = "k"
    vendor {
      email = "opensource@appian.com"
      name = "Appian Corporation"
      url = "https://www.appian.com"
    }
  }
  buildSearchableOptions = false
}

grammarKit {
  jflexRelease = "1.9.2"
}

sourceSets {
  main {
    java.srcDir("gen")
    resources.srcDir("src/main/resources")
  }
}

tasks {
  generateLexer {
    sourceFile.set(file("src/main/resources/com/appian/intellij/k3/k.flex"))
    targetOutputDir.set(file("gen/com/appian/intellij/k3"))
    purgeOldFiles.set(true)
  }
  generateParser {
    sourceFile.set(file("src/main/resources/com/appian/intellij/k3/k.bnf"))
    targetRootOutputDir.set(file("gen"))
    pathToParser.set("com/appian/intellij/k3/parser/KParser.java")
    pathToPsiRoot.set("com/appian/intellij/k3/psi")
    purgeOldFiles.set(true)
  }
  processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  }
  compileJava {
    dependsOn(generateLexer, generateParser)
  }
}