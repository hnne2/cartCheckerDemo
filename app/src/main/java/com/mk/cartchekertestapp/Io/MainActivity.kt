package com.mk.cartchekertestapp.Io

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mk.cartchekertestapp.MyApiService
import com.mk.cartchekertestapp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var myApiService: MyApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val cartInfoFragment = CartInfoFragment.newInstance(
           firstWindow = 1
        )
        supportFragmentManager.beginTransaction().add(R.id.main,cartInfoFragment).commit()
    }
}