package com.example.ex2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ex2.auth.IntroActivity
import com.example.ex2.data.SmokingData
import com.example.ex2.data.UserData
import com.example.ex2.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=FirebaseAuth.getInstance()

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("userData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val UserDataFromFB=snapshot.getValue(UserData::class.java)

                    userdata_value.setText("이메일 : "+UserDataFromFB?.user_email
                            +"\n아이디 : "+UserDataFromFB?.user_id
                            +"\n이름 : "+UserDataFromFB?.user_name)
                }
            })

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("smokingData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val SmokingDataFromFB=snapshot.getValue(SmokingData::class.java)

                    smokingdata_value.setText("금연일 : "+SmokingDataFromFB?.start_date
                            +"\n금연한 기간 : "+SmokingDataFromFB?.day_count+"일"
                            +"\n금연한 담배 갯수 : "+SmokingDataFromFB?.total_smoking_count+"개비"
                            +"\n절약한 금액 : "+SmokingDataFromFB?.money_saved+"원"
                            +"\n연장된 수명 : "+SmokingDataFromFB?.life_saved+"분")
                }
            })

        button_logout.setOnClickListener {
            auth?.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        button_signout.setOnClickListener {
            val uid= FirebaseUtils.getUid()
            FirebaseDatabase.getInstance().getReference(uid).removeValue()
            auth?.currentUser!!.delete()
            val intent = Intent(this, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        /*button_inputdata.setOnClickListener{
            val intent = Intent(this, InputdataActivity::class.java)
            startActivity(intent)
        }

        button_ranking.setOnClickListener{
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }*/
    }
}