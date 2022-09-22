package com.example.smokingapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_inputdata.*
import java.text.SimpleDateFormat
import java.util.*

class InputdataActivity : AppCompatActivity() {

    private var uid : String=""
    private var auth : FirebaseAuth? = null

    var dateString:String=""
    var calculateDate:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inputdata)

        auth = FirebaseAuth.getInstance()
        val firestore=FirebaseFirestore.getInstance()

        //현재 uid 값 가져오기
        uid=auth?.currentUser?.uid.toString()

        button_input.setOnClickListener{
            //database 사용
            val database=FirebaseDatabase.getInstance()
            val myRef=database.getReference()
            //smoking data 저장
            val start_date=dateString
            val day_count=calculateDate.toInt()
            var smoking_count=ciga_num.text.toString().toInt()
            var total_smoking_count=smoking_count*day_count
            var money_count=ciga_price.text.toString().toInt()
            var money_saved=money_count*total_smoking_count
            var life_saved=day_count*smoking_count*12

            val dataInput=SmokingData(start_date,day_count,smoking_count,total_smoking_count,money_count,money_saved,life_saved)

            myRef.child("users").child(uid).child("smokingData").setValue(dataInput)

            //firestore에 저장
            user.start_date=start_date
            user.day_count=day_count
            user.smoking_count=smoking_count
            user.total_smoking_count=total_smoking_count
            user.money_count=money_count
            user.money_saved=money_saved
            user.life_saved=life_saved

            firestore?.collection("users")?.document(uid)?.set(user, SetOptions.merge())


            val intent= Intent(this,MypageActivity::class.java)
            //MyActicity에 현재 uid값을 넣어줌
            intent.putExtra("uid",uid)
            startActivity(intent)
        }

        button_setdate.setOnClickListener{
            //오늘날짜
            val todaydate = Calendar.getInstance()
            //시작날짜
            val selectdate = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                result.setText(dateString)
                var formatter = SimpleDateFormat("yyyy년 mm월 dd일")
                val date = formatter.parse(dateString)
                calculateDate=((todaydate.time.time - date.time)/ (60 * 60 * 24 * 1000))-211
            }

            var dpd=DatePickerDialog(this, selectdate, todaydate.get(Calendar.YEAR),todaydate.get(Calendar.MONTH),todaydate.get(Calendar.DAY_OF_MONTH))
            dpd.datePicker.maxDate = System.currentTimeMillis() - 1000;
            dpd.show()
        }
    }
}
