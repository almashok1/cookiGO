plugins {
    id("general.config")
    id("androidx.navigation.safeargs.kotlin")
}

dependencies {
    implementation(project(Modules.Features.CHECKLIST))
}