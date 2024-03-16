package com.example.statistic_r

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.statistic_r.databinding.ActivityReportBinding
import com.example.statistic_r.retrofit.DriveReport
import com.example.statistic_r.retrofit.MostUser
import com.example.statistic_r.retrofit.statistic_r_Service
import com.example.statistic_r.retrofit.RetrofitConnection
import com.example.statistic_r.retrofit.statistic_r_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getReportData()
    }

    private fun getReportData() {
        val retrofitAPI = RetrofitConnection.getInstance().create(statistic_r_Service::class.java)

        // API 호출
        retrofitAPI.getDriveReport()
            .enqueue(object : Callback<DriveReport> {
                override fun onResponse(call: Call<DriveReport>, response: Response<DriveReport>) {
                    if (response.isSuccessful) {
                        // API 응답이 성공적으로 수신된 경우 UI 업데이트를 수행합니다.
                        response.body()?.let { updateReportUI(it) }
                    } else {
                        // API 응답이 실패한 경우 에러 메시지를 출력합니다.
                        Toast.makeText(this@ReportActivity, "리포트 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DriveReport>, t: Throwable) {
                    // API 호출이 실패한 경우 에러 메시지를 출력합니다.
                    Toast.makeText(this@ReportActivity, "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
    }

    private fun updateReportUI(reportQualityData: DriveReport) {
        val userName = reportQualityData.data.userName
        val carName = reportQualityData.data.car.carName
        val carNumber = reportQualityData.data.car.carNumber
        val departure = reportQualityData.data.departure
        val destination = reportQualityData.data.destination
        val driveTime = reportQualityData.data.driveTime
        val startTime = reportQualityData.data.startTime
        val endTime = reportQualityData.data.endTime

        // 리포트 작성
        val reportText = "User Name: $userName\n" +
                "Car Name: $carName\n" +
                "Car Number: $carNumber\n" +
                "Departure: $departure\n" +
                "Destination: $destination\n" +
                "Drive Time: $driveTime\n" +
                "Start Time: $startTime\n" +
                "End Time: $endTime"

        binding.tvReportTime.text = reportText
    }

}
