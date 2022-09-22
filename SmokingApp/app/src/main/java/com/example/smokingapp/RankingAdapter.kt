package com.example.smokingapp

import android.content.Context
import android.service.notification.NotificationListenerService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.listview_item.view.*

class MainListAdapter (val context: Context, val list_ranking: ArrayList<RankingData>): BaseAdapter(){
    override fun getCount(): Int {
        return list_ranking.size
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        //p0는 인덱스
        val view: View = LayoutInflater.from(context).inflate(R.layout.listview_item,null)

        view.list_sample_text.setText("${p0+1}등    id: ${list_ranking.get(p0).user_id}   금연한 담배 수: ${list_ranking.get(p0).total_smoking_count}")

        return view
    }

}