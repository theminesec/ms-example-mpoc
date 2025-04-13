@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://developer.huawei.com/repo/") }
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://developer.huawei.com/repo/") }

        val GITHUB_USERNAME: String? by settings
        val GITHUB_TOKEN: String? by settings
        requireNotNull(GITHUB_USERNAME) {
            """
                Please set your MineSec Github credential in `gradle.properties`.
                On local machine,
                ** DO NOT **
                ** DO NOT **
                ** DO NOT **
                Do not put it in the project's file. (and accidentally commit and push)
                ** DO **
                Do set it in your machine's global (~/.gradle/gradle.properties)
            """.trimIndent()
        }
        requireNotNull(GITHUB_TOKEN)
        println("github username: $GITHUB_USERNAME")

        maven {
            name = "gprInternal"
            url = uri("https://maven.pkg.github.com/theminesec/ms-registry-internal")
            credentials {
                username = GITHUB_USERNAME
                password = GITHUB_TOKEN
            }
        }
        maven {
            name = "gprClient"
            url = uri("https://maven.pkg.github.com/theminesec/ms-registry-client")
            credentials {
                username = GITHUB_USERNAME
                password = GITHUB_TOKEN
            }
        }
    }
}

rootProject.name = "ms-example-mpoc"
include(":app")
 