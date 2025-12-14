package com.github.aeoliux

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform