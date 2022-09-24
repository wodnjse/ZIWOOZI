package com.zizi.apps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.zizi.news.NewsPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zizi.apps.home.HomeFragment
import com.zizi.apps.mypage.MyPageFragment
import com.zizi.apps.store.StroePageFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val myPageFragment = MyPageFragment()
        val newPageFragment = NewsPageFragment()
        val stroePageFragment = StroePageFragment()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.myPage -> replaceFragment(myPageFragment)
                R.id.newPage -> replaceFragment(newPageFragment)
                R.id.storePage -> replaceFragment(stroePageFragment)
            }
            true
        }


    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer,fragment)
                commit()
            }
    }
}