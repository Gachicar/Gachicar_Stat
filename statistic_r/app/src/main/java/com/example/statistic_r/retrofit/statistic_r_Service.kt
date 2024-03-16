package com.example.statistic_r.retrofit
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface statistic_r_Service {
    @GET("/api/car/fuel")
    // fun getFuelData(@Query("fuelType") fuelType: String, @Query("oilStatus") oilStatus: Int): Call<statistic_r_Response>
    fun getFuelData(): Call<statistic_r_Response>
}