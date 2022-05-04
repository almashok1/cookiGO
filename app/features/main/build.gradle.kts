plugins {
    id("general.config")
}

dependencies {
    api(project(Modules.Features.HOME))
    api(project(Modules.Features.ACCOUNT))
    api(project(Modules.Features.FAVOURITES))
    api(project(Modules.Features.CHECKLIST))
    api(project(Modules.Features.SEARCH))
}