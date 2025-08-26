package com.example.pokedexapp.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class ForceDetailErrorInterceptor @Inject constructor() : Interceptor {

    private val forceErrorOnDetail = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url.toString()

        val isDetailRequest = requestUrl.contains("pokemon/") && !requestUrl.contains("limit=")

        if (forceErrorOnDetail && isDetailRequest) {
            println("Interceptor: Forcing 503 error for URL: $requestUrl")

            return Response.Builder()
                .code(503)
                .message("Service Unavailable")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(
                    "{ \"error\": \"Force update triggered for testing.\" }"
                        .toResponseBody("application/json".toMediaTypeOrNull())
                )
                .build()
        }

        return chain.proceed(request)
    }
}