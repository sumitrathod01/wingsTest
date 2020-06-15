package com.example.wings.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wings.R
import com.example.wings.utils.Const
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.Edt_pass
import kotlinx.android.synthetic.main.activity_login.Edt_uname
import kotlinx.android.synthetic.main.activity_register.*


class RegistrationActivity : AppCompatActivity(), View.OnClickListener {

    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
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
                        registerUser()
                    }
                }
            }

        }
    }


    private fun registerUser() {

        val a1 = Const.showDialog(this, resources.getString(R.string.please_wait))
        a1!!.show()
        //creating a new user
        firebaseAuth!!.createUserWithEmailAndPassword(
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
                        "Registration Success\nPlease Login now...",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed\nPlease Retry...", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId === android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
