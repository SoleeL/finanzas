pluginManagement {
    includeBuild("build-logic")
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
    }
}

rootProject.name = "finanzas"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

include(":core:common")
include(":core:database")
include(":core:model")
include(":core:ui")

include(":data:paymentaccount")
include(":data:transaction")

include(":domain:formatdate")
include(":domain:transactions")
include(":domain:transactions")
include(":domain:validation")
include(":domain:visualtransformation")

include(":feature:add")
include(":feature:cancelalert")
include(":feature:paymentaccounts")
include(":feature:paymentaccountcreate")
include(":feature:profile")
include(":feature:stats")
include(":feature:transactions")
include(":feature:transactioncreate")
