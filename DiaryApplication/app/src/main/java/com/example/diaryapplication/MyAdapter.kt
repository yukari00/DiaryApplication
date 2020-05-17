package com.example.diaryapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.cards_weekly.view.*

class MyAdapter(private val myDataset: List<DiaryData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    inner class MyViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cards_weekly, parent, false) as View

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.mView.textViewDetail.text = myDataset[position].detail
        holder.mView.textViewDate.text = myDataset[position].date


        holder.mView.setOnClickListener {
            //Todo クリック処理　→　
        }
    }

    override fun getItemCount() = 20
}