package game.fabric.buildsrc

object Dependencies {
    const val ANDROID_CORE = "androidx.core:core-ktx:${Versions.androidCore}"
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val LIFECYCLE_COMPOSE = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycle}"
    const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.activity}"
    const val VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val NAVIGATION_COMPOSE = "androidx.navigation:navigation-compose:${Versions.composeNavigation}"
    const val MATERIAL = "androidx.compose.material:material:${Versions.material}"
    const val COMPOSE_UI = "androidx.compose.ui:ui:${Versions.COMPOSE}"
    const val COMPOSE_TOOL = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE}"
    const val COMPOSE_PREVIEW = "androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE}"
    const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}"
    const val HILT_COMPOSE = "androidx.hilt:hilt-navigation-compose:${Versions.HILT_VIEWMODEL}"
    const val GOOGLE_PLAY_GAMES = "com.google.android.gms:play-services-games-v2:${Versions.GOOGLE_PLAY_GAMES}"
    const val GOOGLE_PLAY_GAMES_AUTH = "com.google.android.gms:play-services-auth:${Versions.GOOGLE_PLAY_GAMES_AUTH}"
    const val FIREBASE = "com.google.firebase:firebase-bom:${Versions.FIREBASE}"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics"
}