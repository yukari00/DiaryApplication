package com.example.memorizeeffectively

import android.content.Context
import android.media.Image
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.cards_list.view.*


class RecyclerviewAdapterList(val mValue : RealmResults<RealmModel>, var mListener : ItemClickListener?) : RecyclerView.Adapter<RecyclerviewAdapterList.MyRecycleViewHolder>(),
    View.OnClickListener {

    lateinit var item : RealmModel
    lateinit var itemImageChange : ImageView
    lateinit var itemImageEdit : ImageView
    lateinit var itemImageAlarm : ImageView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecycleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_list, parent, false)

        return MyRecycleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mValue.size
    }

    override fun onBindViewHolder(holder: MyRecycleViewHolder, position: Int) {
        item = mValue[position]!!

        holder.itemTextViewWord.text = when(item.statusWords){
            StatusWords.STATUS_WORD.name -> mValue[position]!!.word
            else -> mValue[position]!!.definition
        }

        holder.itemImageViewEdit.setOnClickListener(this)
        holder.itemImageViewChange.setOnClickListener(this)
        holder.itemImageViewAlarm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            itemImageEdit -> mListener!!.onItemClickEdit(item)
            itemImageChange -> mListener!!.onItemClickChange(item)
            itemImageAlarm -> mListener!!.onItemClickAlarm(item)
        }

    }



    inner class MyRecycleViewHolder(val view : View) : RecyclerView.ViewHolder(view){

    var itemTextViewWord = view.itemTextViewWord
    var itemImageViewChange = view.itemImageViewChange
    var itemImageViewEdit = view.itemImageViewEdit
    var itemImageViewAlarm = view.itemImageViewAlarm

    }

}

interface ItemClickListener {
    fun onItemClickChange(item : RealmModel)
    fun onItemClickEdit(item : RealmModel)
    fun onItemClickAlarm(item : RealmModel)
}
