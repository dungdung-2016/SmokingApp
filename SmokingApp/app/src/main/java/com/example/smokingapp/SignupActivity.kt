package com.example.smokingapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = Firebase.auth

        signup_okButton.setOnClickListener {
            createAccount(signupID.text.toString(),signupPassword.text.toString()
                ,userId.text.toString(),userName.text.toString())
        }
    }

    private fun createAccount(email: String, password: String, id: String, name: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //유저 정보 가져오기
                        val uid=FirebaseAuth.getInstance()?.currentUser?.uid.toString()
                        //데이터베이스 가져오기
                        val database= FirebaseDatabase.getInstance()
                        val myRef=database.getReference()
                        //유저 정보 저장
                        val dataInput=UserData(uid,email,id,name)
                        myRef.child("users").child(uid).child("userData").setValue(dataInput)

                        //firestore에 저장
                        user.uid=uid
                        user.user_name=name
                        user.user_id=id
                        user.user_email=email
                        val firestore=FirebaseFirestore.getInstance()
                        firestore?.collection("users")?.document(uid)?.set(user, SetOptions.merge())

                        //메시지 출력
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}