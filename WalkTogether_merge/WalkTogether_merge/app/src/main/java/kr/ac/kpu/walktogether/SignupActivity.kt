package kr.ac.kpu.walktogether

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        register_button_register.setOnClickListener {  //회원가입 버튼 누르기
            performRegister()
        }

        already_have_account_textview.setOnClickListener{  //이미 회원가입했을 때 버튼 누르는 경우

            //launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener {  //사진 고르는 버튼튼
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null)
        selectedPhotoUri = data?.data//data.data
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

        selectphoto_imageview_register.setImageBitmap(bitmap)

        selectphoto_button_register.alpha = 0f
    }

    private fun performRegister(){
        val name = username_edittext_register.text.toString()  //입력한 이름
        val email = email_edittext_register.text.toString() //입력한 이메일
        val password = password_edittext_register.text.toString()


        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "이메일 또는 비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        //Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                uploadImageToFirebaseStorage()

                //intent.putExtra 로 Exam 클래스의 변수 myExam을 저장
                //val accountIntent = Intent(this, UserFragment::class.java)
                //accountIntent.putExtra("examKey", myExam)
                //startActivity(accountIntent)

                //회원가입 성공시다음페이지로
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener{
                Toast.makeText(this, "회원가입이 실패되었습니다.: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }


    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","File Location: $it")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
        //.addOnFailureListener {
        //do some logging here
        //}
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        // val uid = FirebaseAuth.getInstance().uid ?: "" 이 코드가 동작하지 않아서 아래 코드로 대체
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, username_edittext_register.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("SignupActivity","Finally we saved the user to Firebase Database")
                  // 인텐트를 넘기는 부분은 이해를 잘 못해서 일단 생략
//                val intent = Intent(this, NextActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
            }
    }

}

class User(val uid: String, val username: String, val profileImageUrl: String){
    constructor() : this("","","")
}