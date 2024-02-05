plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

allprojects {
    group = "com.github.yashctn88.candlechartkmm"
    version = "0.0.2"
}

//subprojects {
//    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent
//
//    // Optionally configure plugin
//    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
//        version.set("1.0.1")
//    }
//}
//
//tasks.register<Copy>("setUpGitHooks") {
//    group = "help"
//    from("$rootDir/.hooks")
//    into("$rootDir/.git/hooks")
//}
