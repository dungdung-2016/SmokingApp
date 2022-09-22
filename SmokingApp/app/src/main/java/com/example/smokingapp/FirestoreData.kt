package com.example.smokingapp

data class FirestoreData (
    var uid:String="",
    var user_email: String="",
    var user_id: String="",
    var user_name: String="",
    var start_date:String="",
    var day_count : Int=0,
    var smoking_count : Int=0,
    var total_smoking_count : Int=0,
    var money_count : Int=0,
    var money_saved: Int=0,
    var life_saved: Int=0
)

val user=FirestoreData()