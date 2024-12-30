// Define the Kotlin version variable at the top
plugins {
    id("com.android.application") version "8.7.2" apply false
    id("com.android.library") version "8.7.2" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}