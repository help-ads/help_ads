package kr.ac.kpu.walktogether

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //BottomNavigation View
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.action_record     // 자동적으로 홈이 눌러지도록

    }

    fun setToolbarDefault() {
        toolbar_title_image.visibility = View.VISIBLE
        toolbar_btn_back.visibility = View.GONE
        toolbar_username.visibility = View.GONE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        setToolbarDefault()
        when(item.itemId) {
            R.id.action_record -> {
                val recordFragment = RecordFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, recordFragment).commit()
                return true
            }


            R.id.action_walking -> {
                val walkingFragment = WalkingFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, walkingFragment).commit()
                return true
            }

            R.id.action_account -> {
                val userFragment = UserFragment()
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                val bundle = Bundle()
                bundle.putString("destinationUid", uid)
                userFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, userFragment)
                    .commit()
                return true
            }


            R.id.action_message -> {

                return true
            }



        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)   // UserFragment에서 추가된 사진을 액티비티에서 가져옴


        /*
        super.onActivityResult(requestCode, resultCode, data)   // UserFragment에서 추가된 사진을 액티비티에서 가져옴
        // requestCode == PICK_PROFILE_FROM_ALBUM == 10
        if(requestCode == PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK) { // 선택한 이미지가 이곳으로 넘어옴
            var imageUri = data?.data

            //이미지가 쌓이지 않고 관리하기쉽게 하도록 uid로 넣어줌으로써 유저하나당 사진하나를 유지/ 편하게 관리 //
            var uid = FirebaseAuth.getInstance().currentUser!!.uid

            //받아온 이미지를 파이어베이스 스토리지로 넘겨줌
            //차일드는 경로 이름
            FirebaseStorage.getInstance().reference.child("userProfileImages")
                .child(uid).putFile(imageUri!!).addOnCompleteListener {
                        task ->
                    var url = task.result.downloadUrl.toString()
                    var map = HashMap<String, Any>()
                    map["image"] = url
                    FirebaseFirestore.getInstance().collection("profileImages").document(uid).set(map)
                }

        }
        */
    }

    // hideKeybord(view) 키보드를 숨겨주는 메서드
    private fun hideKeyboard(view: View) {
        view?.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
