package com.example.k_login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener{
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            Log.d("Login","Attempt login with email/pw: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                //.addOnCompleteListener()
                //.add
                .addOnCompleteListener{

                    if(!it.isSuccessful) return@addOnCompleteListener

                    val intent = Intent(this, NextActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener{
                    Log.d("RegisterActivity","Failed to login: ${it.message}")
                    Toast.makeText(this, "로그인이 실패되었습니다.: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        back_to_register_textview.setOnClickListener{
            finish()
        }
    }
}