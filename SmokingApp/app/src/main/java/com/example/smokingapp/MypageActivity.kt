package com.example.smokingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_mypage.*


class MypageActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    private var firestore: FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        auth=FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()

        //마이페이지 사용자정보 가져오기
        FirebaseDatabase.getInstance().getReference().child("users").child(auth?.currentUser?.uid.toString())
            .child("userData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //성공했을때
                    val UserDataFromFB=snapshot.getValue(UserData::class.java)

                    userdata_value.setText("이메일 : "+UserDataFromFB?.user_email
                            +"\n아이디 : "+UserDataFromFB?.user_id
                            +"\n이름 : "+UserDataFromFB?.user_name)
                }
            })
        //마이페이지 금연정보 가져오기
        FirebaseDatabase.getInstance().getReference().child("users").child(auth?.currentUser?.uid.toString())
            .child("smokingData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //성공했을때
                    val SmokingDataFromFB=snapshot.getValue(SmokingData::class.java)

                    smokingdata_value.setText("금연일 : "+SmokingDataFromFB?.start_date
                            +"\n금연한 기간 : "+SmokingDataFromFB?.day_count+"일"
                            +"\n금연한 담배 갯수 : "+SmokingDataFromFB?.total_smoking_count+"개비"
                            +"\n절약한 금액 : "+SmokingDataFromFB?.money_saved+"원"
                            +"\n연장된 수명 : "+SmokingDataFromFB?.life_saved+"분")
                }
            })

        button_logout.setOnClickListener {
            // 로그인 화면으로
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            auth?.signOut()
        }

        button_signout.setOnClickListener {
            //회원가입 화면으로
            val intent = Intent(this, SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            val uid=auth?.currentUser?.uid.toString()
            //데이터베이스에서 삭제
            FirebaseDatabase.getInstance().getReference(uid).removeValue()
            //firestore에서 유저정보 삭제
            firestore?.collection("users")?.document(uid)?.delete()
            auth?.currentUser!!.delete()
        }

        button_inputdata.setOnClickListener{
            val intent = Intent(this, InputdataActivity::class.java)
            startActivity(intent)
        }

        button_ranking.setOnClickListener{
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
    }
}