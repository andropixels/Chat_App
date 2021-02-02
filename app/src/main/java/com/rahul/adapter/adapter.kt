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

class adapter(val context: Context,val list: MutableList<userdata>):RecyclerView.Adapter<adapter.userviewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userviewholder {

        val view=LayoutInflater.from(context).inflate(R.layout.contactlist_item,parent,false)
        return userviewholder(view)
    }

    override fun onBindViewHolder(holder: userviewholder, position: Int) {

        val pos=list.get(position)
        holder.number.text=pos.phonenumber
        holder.username.text=pos.name




    }

    override fun getItemCount(): Int {
             return list.size
    }

    class userviewholder(view: View):RecyclerView.ViewHolder(view){

        val username=view.findViewById<TextView>(R.id.name)
        val number=view.findViewById<TextView>(R.id.number)

    }



}
