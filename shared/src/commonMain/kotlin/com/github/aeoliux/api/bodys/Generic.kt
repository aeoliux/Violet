package com.github.aeoliux.api.bodys

import kotlinx.serialization.Serializable

@Serializable
data class IdAndUrl(val Id: Int, val Url: String)

@Serializable
data class IdAsStringAndUrl(val Id: String, val Url: String)