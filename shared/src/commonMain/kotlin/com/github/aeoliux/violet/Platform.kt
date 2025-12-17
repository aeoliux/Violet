package com.github.aeoliux.violet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform