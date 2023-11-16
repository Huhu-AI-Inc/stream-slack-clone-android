// Root module
include(":app")

// Other modules
include(":domain")
include(":data")
include(":common")
include(":commonui")
include(":navigator")

// Feature modules
include(":ui-dashboard")
include(":feat-onboarding")
include(":feat-chat")
include(":feat-channels")
include(":feat-chatcore")
include(":benchmark")

include(":HuhuAPIClient")
project(":HuhuAPIClient").projectDir = file("../huhu-api-generator-android/HuhuAPIClient")
include(":Network")
