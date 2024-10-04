package game.fabric.buildsrc

object Dependencies {
    const val androidCore = "androidx.core:core-ktx:${Versions.androidCore}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycle}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"
    const val viewModelExtension = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val navigation = "androidx.navigation:navigation-compose:${Versions.composeNavigation}"
    const val material = "androidx.compose.material:material:${Versions.material}"
    const val COMPOSE_UI = "androidx.compose.ui:ui:${Versions.COMPOSE}"
    const val COMPOSE_TOOL = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE}"
    const val COMPOSE_PREVIEW = "androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE}"
    const val HILT_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
    const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}"
    const val HILT_COMPOSE = "androidx.hilt:hilt-navigation-compose:${Versions.HILT_VIEWMODEL}"
}