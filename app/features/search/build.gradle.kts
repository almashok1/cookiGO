plugins {
    id("general.config")
    id("androidx.navigation.safeargs.kotlin")
}

dependencies {
    implementation(project(Modules.Features.USER_PREFERENCES))
    implementation(project(Modules.Features.RECIPE))
    implementation(project(Modules.Features.CHECKLIST))
}