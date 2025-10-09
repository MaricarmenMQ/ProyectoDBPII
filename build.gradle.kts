<<<<<<< HEAD
// Top-level build file where you can add configuration options common to all sub-projects/modules.
=======
>>>>>>> 659aa94513e4e27a1d883ea9b2add7cdac420627
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
<<<<<<< HEAD
}
=======
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
>>>>>>> 659aa94513e4e27a1d883ea9b2add7cdac420627
