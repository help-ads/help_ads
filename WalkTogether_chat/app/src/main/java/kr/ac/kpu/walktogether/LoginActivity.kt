package kr.ac.kpu.walktogether

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener{
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            Log.d("Login","Attempt login with email/pw: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val userId = FirebaseAuth.getInstance().currentUser
                    moveMainPage(userId)
                    Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(applicationContext, "로그인 실패(${it.message})", Toast.LENGTH_SHORT).show()
                }
            //.addOnCompleteListener()
            //.add
        }
        back_to_register_textview.setOnClickListener{
            finish()
        }
    }

    fun moveMainPage(user: FirebaseUser?) {
        // 유저가 로그인에 성공하여 메인엑티비티로 넘어가는 부분
        if(user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
