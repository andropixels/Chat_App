package com.rahul.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.chatingapp.R
import com.rahul.dataclass.userdata
import com.rahul.dataclass.userlist

class useradapter(val context: Context,val list :MutableList<userlist>):RecyclerView.Adapter<useradapter.userviewholder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userviewholder {
        val  view=LayoutInflater.from(context).inflate(R.layout.user_item,parent,false)
        return userviewholder(view)
    }

    override fun onBindViewHolder(holder: userviewholder, position: Int) {
          val pos=list[position]
        holder.text.setText(pos.phonenumber)

    }

    override fun getItemCount(): Int {

           return list.size

    }

    class userviewholder(view: View):RecyclerView.ViewHolder(view){
        val text=view.findViewById<TextView>(R.id.phonenumbe)
    }
}