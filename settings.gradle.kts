rootProject.name = "cmmLibrary"
include(":sample:androidApp")
include(":kmmcandlechart")
include(":sample:shared")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven {
            val githubProperties = java.util.Properties()
            File(rootProject.projectDir, "github.properties").inputStream().use { input ->
                githubProperties.load(input)
            }
            url = uri("https://maven.pkg.github.com/yashctn88/candlestickschartkmm")
            credentials {
                username = "${githubProperties["gpr.usr"]}"
                password = "${githubProperties["gpr.key"]}"
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}
