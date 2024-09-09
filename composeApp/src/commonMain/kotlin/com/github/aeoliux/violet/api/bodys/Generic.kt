package com.github.aeoliux.violet.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class IdAndUrl(val Id: UInt, val Url: String)

@Serializable
data class IdAsStringAndUrl(val Id: String, val Url: String)