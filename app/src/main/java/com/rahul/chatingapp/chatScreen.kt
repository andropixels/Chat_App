package com.rahul.chatingapp


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rahul.adapter.adapter
import com.rahul.dataclass.userdata
import java.util.jar.Manifest

@Suppress("UNCHECKED_CAST")
class chatScreen : AppCompatActivity() {
    lateinit var logoutbtn:Button
    lateinit var list:ArrayList<userdata>
    lateinit var userlist:ArrayList<userdata>
    lateinit var finduserbtn:Button
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_screen)
        val  auth = FirebaseAuth.getInstance()
        logoutbtn=findViewById(R.id.button)
        finduserbtn=findViewById(R.id.finduserbtn)
        userlist= arrayListOf()

        logoutbtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }
        list= arrayListOf()
        recyclerView=findViewById(R.id.contactlist)
        recyclerView.isNestedScrollingEnabled()
        recyclerView.hasFixedSize()
        recyclerView.layoutManager=LinearLayoutManager(applicationContext)
        adapter= adapter(applicationContext,list)
        recyclerView.adapter=adapter

         val phone: Cursor?=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
            while (phone!!.moveToNext()){
                val name=phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val numberr=phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                list.add(userdata(name,numberr))
                adapter.notifyDataSetChanged()
                val pref=applicationContext.getSharedPreferences("pref", MODE_PRIVATE).edit()
                pref.putString("name",name).apply()

            }
        finduserbtn.setOnClickListener {

            val intent=Intent(applicationContext,Users::class.java)
            startActivity(intent)


        }





    }



    override fun onBackPressed() {
        super.onBackPressed()

        isDestroyed

    }

}


