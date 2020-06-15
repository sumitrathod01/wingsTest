package com.example.wings.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wings.BuildConfig
import com.example.wings.R
import com.example.wings.network.ApiParams
import com.example.wings.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.techfirst.marksmentor.support.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var fcmToken: String = ""
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(p0: View?) {
        Const.hideKeyboard(this)
        when (p0!!.id) {
            R.id.Btn_submit -> {
                when {
                    !Const.isValidEmail(Edt_uname.text.toString()) -> {
                        Edt_uname.error = "Enter Valid Email Address!"
                        Edt_uname.requestFocus()
                    }

                    TextUtils.isEmpty(Edt_pass.text.toString()) && Edt_pass.text.toString().length < 6 -> {
                        Edt_uname.error = null
                        Edt_pass.error = "Enter Valid password!"
                        Edt_pass.requestFocus()
                    }
                    else -> {
                        Edt_pass.error = null
                        login()

                    }
                }
            }

            R.id.Txt_register -> {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
        }
    }

    private fun login() {

        val a1 = Const.showDialog(this, resources.getString(R.string.please_wait))
        a1!!.show()
        //creating a new user
        firebaseAuth!!.signInWithEmailAndPassword(
                Edt_uname.text.toString(),
                Edt_pass.text.toString()
            )
            .addOnCompleteListener(
                this
            ) { task ->
                //checking if success
                a1.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Login Success",
                        Toast.LENGTH_SHORT
                    ).show()

                    SharedPreferenceUtil.putValue(ApiParams.IS_LOGIN, true)
                    if (Edt_uname.text.toString() == BuildConfig.admin) {
                        SharedPreferenceUtil.putValue(ApiParams.TYPE, 1)
                        startActivity(Intent(this, EmployeeMainActivity::class.java))
                    } else {
                        SharedPreferenceUtil.putValue(ApiParams.TYPE, 2)
                        startActivity(Intent(this, EmployeeMainActivity::class.java))
                    }
                    SharedPreferenceUtil.save()
                } else {
                    Toast.makeText(this, "Please check Email or Password!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

}
