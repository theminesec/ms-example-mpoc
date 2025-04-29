// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // package dependency into aar
        // classpath("com.huawei.agconnect:agcp:1.6.5.300")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //kotlin("android") version "1.9.21" apply false
    //kotlin("jvm") version "1.9.21" apply false
    kotlin("plugin.serialization") version "1.9.21" apply false
}