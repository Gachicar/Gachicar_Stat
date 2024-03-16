package com.example.statistic_r

import java.text.SimpleDateFormat
import java.util.Date
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.statistic_r.databinding.ActivityMainBinding
import com.example.statistic_r.retrofit.statistic_r_Response
import com.example.statistic_r.retrofit.statistic_r_Service
import com.example.statistic_r.retrofit.RetrofitConnection

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Refresh 버튼에 대한 클릭 리스너 설정
        setRefreshButton()
    }

    private fun setRefreshButton() {
        binding.btnRefresh.setOnClickListener {
            getFuelQualityData()
        }
    }

    private fun getFuelQualityData() {
        // 레트로핏 객체를 이용하여 statistic_r_Service 인터페이스 구현체를 가져올 수 있습니다.
        val retrofitAPI = RetrofitConnection.getInstance().create(statistic_r_Service::class.java)

        // API 호출
        retrofitAPI.getFuelData()
            .enqueue(object : Callback<statistic_r_Response> {
                override fun onResponse(call: Call<statistic_r_Response>, response: Response<statistic_r_Response>) {
                    // 정상적인 Response가 왔다면 UI 업데이트
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "최신 정보 업데이트 완료!", Toast.LENGTH_SHORT).show()
                        // API 응답을 받아와서 UI 업데이트를 수행하는 함수를 호출합니다.
                        response.body()?.let { updateFuelUI(it) }
                    } else {
                        Toast.makeText(this@MainActivity, "업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<statistic_r_Response>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
    /**
     * @desc 가져온 데이터 정보를 바탕으로 화면을 업데이트한다.
     * */
    private fun updateFuelUI(fuelQualityData: statistic_r_Response) {
        val fuelData = fuelQualityData.data.oilStatus

        //수치 지정 (가운데 숫자)
        binding.tvCount.text = fuelData.toString()

        //실시간 시간반영
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        binding.tvCheckTime.text = dateFormatter.format(Date())

        when (fuelData) {
            in 85..100 -> {
                binding.tvTitle.text = "연료상태 : 좋음"
                binding.imgBg.setImageResource(R.drawable.bg_good)
            }

            in 50..84 -> {
                binding.tvTitle.text = "연료상태 : 보통"
                binding.imgBg.setImageResource(R.drawable.bg_soso)
            }

            in 25..49 -> {
                binding.tvTitle.text = "연료상태 : 나쁨"
                binding.imgBg.setImageResource(R.drawable.bg_bad)
            }

            else -> {
                binding.tvTitle.text = "연료상태 : 비상"
                binding.imgBg.setImageResource(R.drawable.bg_worst)
            }
        }
    }
}