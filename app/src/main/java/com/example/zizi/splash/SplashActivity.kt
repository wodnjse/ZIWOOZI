/*
package com.example.zizi.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.zizi.R
import com.example.zizi.home.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 일정 시간 지연 이후 실행하기 위한 코드
        Handler(Looper.getMainLooper()).postDelayed({

            // 일정 시간이 지나면 MainActivity로 이동
            val intent= Intent( this,HomeFragment::class.java)
            startActivity(intent)

            // 이전 키를 눌렀을 때 스플래스 스크린 화면으로 이동을 방지하기 위해
            // 이동한 다음 사용안함으로 finish 처리
            finish()

        }, 3000) // 시간 3초 이후 실행
    }
}
*/

package com.example.zizi.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.zizi.R
import com.example.zizi.home.HomeFragment

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadSplashScreen()
    }

        private fun loadSplashScreen(){
            Handler().postDelayed({
                // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
                val intent = Intent(this, HomeFragment::class.java)
                startActivity(intent)
                finish()
            }, 1000)
    }
}