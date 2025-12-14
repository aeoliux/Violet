package com.github.aeoliux.api

object Endpoints {
    val authBase = "https://api.librus.pl/OAuth/Authorization"
    val authStep1 = authBase + "?client_id=46&response_type=code&scope=mydata"
    val authStep2 = authBase + "?client_id=46"
    val authStep3 = authBase + "/2FA?client_id=46"

    val apiEndpoint = "https://synergia.librus.pl/gateway/api/2.0/"

    fun url(data: String): String {
        return apiEndpoint + data
    }
}