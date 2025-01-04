package com.mk.cartchekertestapp

import com.mk.cartchekertestapp.models.CartInfoCallBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface MyApiService {
    @POST("{cartNumber}")
    suspend fun getCurtInfo(@Path("cartNumber") cartNumber: String): Response<CartInfoCallBody>

}