package com.example.statistic_r.retrofit

data class statistic_r_Response(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val fuelType: String,
        val oilStatus: Int
    )
}