package com.github.aeoliux.violet.api

public object Endpoints {
    public val authBase = "https://api.librus.pl/OAuth/Authorization"
    public val authStep1 = authBase + "?client_id=46&response_type=code&scope=mydata"
    public val authStep2 = authBase + "?client_id=46"
    public val authStep3 = authBase + "/2FA?client_id=46"

    public val apiEndpoint = "https://synergia.librus.pl/gateway/api/2.0/"

    public fun url(data: String): String {
        return apiEndpoint + data
    }
}