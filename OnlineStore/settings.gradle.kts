pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven { url = uri ("https://maven.aliyun.com/repository/jcenter" )}
        maven { url = uri("https://www.jitpack.io" ) }
    }
}

rootProject.name = "OnlineStore"
include(":app")
include(":data")
include(":domain")
