package kr.ac.kpu.walktogether

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_account_textview.setOnClickListener{
            Log.d("MainActivity","Try to show login activity")

            //launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
    private fun performRegister(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "이메일 또는 비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password: $password")

        //Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                val userId = FirebaseAuth.getInstance().currentUser
                // 현재유저의 유저아이디와 함께 액티비티를 전환시키는 함수 호출
                moveMainPage(userId)

                // else if successful
                Log.d("Main", "Successfully created user with uid: ${it.result!!.user.uid}")
            }
            .addOnFailureListener{
                Log.d("Main","Failed to create user: ${it.message}")
                Toast.makeText(this, "회원가입이 실패되었습니다.: ${it.message}", Toast.LENGTH_SHORT).show()
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
