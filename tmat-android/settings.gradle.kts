pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Tnk SDK 저장소
        maven { url = uri("https://repository.tnkad.net/repository/public/") }
    }
}
rootProject.name = "tnk_sample_tmat"
include(":app")
