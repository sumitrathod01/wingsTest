package com.example.wings.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.wings.R
import com.example.wings.network.ApiParams
import com.techfirst.marksmentor.support.utils.SharedPreferenceUtil

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val i: Intent = if (!SharedPreferenceUtil.getBoolean(ApiParams.IS_LOGIN, false)) {
                Intent(this@SplashActivity, LoginActivity::class.java)
            } else {
                if (SharedPreferenceUtil.getInt(ApiParams.TYPE, 1) == 1)
                    Intent(this@SplashActivity, AdminHomeActivity::class.java)
                else
                    Intent(this@SplashActivity, EmployeeMainActivity::class.java)
            }
            startActivity(i)
            finish()
        }, 2000)

    }
}
