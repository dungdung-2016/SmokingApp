package com.example.smokingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.activity_ranking.*


class RankingActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    private var firestore : FirebaseFirestore? = null

    val list_ranking=ArrayList<RankingData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        firestore=FirebaseFirestore.getInstance()

        /*list_sample.add("A")
        list_sample.add("B")
        list_sample.add("C")

        val list_adapter=MainListAdapter(this,list_sample)
        listview_id.adapter=list_adapter*/

        /*firestore?.collection("users")?.orderBy("total_smoking_count", Query.Direction.ASCENDING)

        firestore?.collection("users")?.get()?.addOnCompleteListener { task ->
            //작업이 성공적으로 마쳤을때
            if (task.isSuccessful) {
                //컬렉션 아래에 있는 모든 정보를 가져온다.
                for (document in task.result) {
                    //document.getData() or document.getId() 등등 여러 방법으로
                    //데이터를 가져올 수 있다.
                    val rankingdata=RankingData()
                    rankingdata.user_id=document.get("user_id").toString()
                    rankingdata.total_smoking_count=document.get("total_smoking_count").toString().toInt()
                    list_ranking.add(rankingdata)
                }
                //그렇지 않을때
            }
            else {
                task.exception
            }
        }*/
        FirebaseDatabase.getInstance().getReference().child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //성공했을때
                    for (item in snapshot.getChildren()) {
                        val rankingdata=RankingData()
                        rankingdata.user_id=item.child("userData")?.getValue(UserData::class.java)?.user_id.toString()
                        rankingdata.total_smoking_count=item.child("smokingData")?.getValue(SmokingData::class.java)?.total_smoking_count.toString().toInt()
                        list_ranking.add(rankingdata)
                    }
                    list_ranking.sortBy { it.total_smoking_count }
                    list_ranking.reverse()
                    val list_adapter=MainListAdapter(this@RankingActivity,list_ranking)
                    listview_id.adapter=list_adapter
                }
            })
    }
}