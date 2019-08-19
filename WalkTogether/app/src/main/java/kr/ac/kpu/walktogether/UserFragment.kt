package kr.ac.kpu.walktogether


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_user.view.*

//import kotlinx.android.synthetic



class UserFragment : Fragment() {

    val PICK_PROFILE_FROM_ALBUM = 10
    // Firebase
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null

    //private String destinationUid;
    var uid: String? = null
    var currentUserUid: String? = null

    var fragmentView: View? = null

    var followListenerRegistration: ListenerRegistration? = null
    var followingListenerRegistration: ListenerRegistration? = null
    var imageprofileListenerRegistration: ListenerRegistration? = null
    var recyclerListenerRegistration: ListenerRegistration? = null





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{




        fragmentView = inflater.inflate(R.layout.activity_user, container, false)
        super.onCreate(savedInstanceState)

        // Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()



        currentUserUid = auth?.currentUser?.uid

        if (arguments != null) {

            uid = arguments!!.getString("destinationUid")

            // <로그아웃 버튼 클릭시> 본인 계정인 경우 로그아웃, Toolbar 기본으로 설정
            if (uid != null && uid == currentUserUid) {

                fragmentView!!.account_btn_follow_signout1.text = getString(R.string.signout)
                fragmentView?.account_btn_follow_signout1?.setOnClickListener {
                    startActivity(Intent(activity, SignupActivity::class.java))  //로그인 화면으로 화면 바꿔주기
                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    auth?.signOut() //로그아웃시키는 함수 signOut

                }
            }

        }


        // Profile Image Click Listener <프로필 사진 클릭시> 사진 바꾸기

        //fragmentView?.account_iv_profile?.setOnClickListener {
        //    if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        //앨범 오픈
        //        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        //        photoPickerIntent.type = "image/*"
        //        activity!!.startActivityForResult(photoPickerIntent, PICK_PROFILE_FROM_ALBUM)
        //    }
        //}

        //fragmentView?.account_recyclerview?.layoutManager = GridLayoutManager(activity!!, 3)
        //fragmentView?.account_recyclerview?.adapter = UserFragmentRecyclerViewAdapter()


        return fragmentView


    }

    override fun onResume() {
        super.onResume()
        getProfileImage()
    }

    fun getProfileImage() {
        imageprofileListenerRegistration = firestore?.collection("profileImages")?.document(uid!!)
            ?.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                if (documentSnapshot?.data != null) {
                    val url = documentSnapshot?.data!!["image"]
                    Glide.with(activity)
                        .load(url)
                        .apply(RequestOptions().circleCrop()).into(fragmentView!!.account_iv_profile1)
                }

            }

    }



    override fun onStop() {
        super.onStop()
        followListenerRegistration?.remove()
        followingListenerRegistration?.remove()
        imageprofileListenerRegistration?.remove()
        recyclerListenerRegistration?.remove()
    }


}
