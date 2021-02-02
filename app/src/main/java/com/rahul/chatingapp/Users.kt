package com.rahul.chatingapp

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rahul.adapter.useradapter
import com.rahul.dataclass.userdata
import com.rahul.dataclass.userlist

@Suppress("CAST_NEVER_SUCCEEDS")
class Users : AppCompatActivity() {
    lateinit var userlist:MutableList<userlist>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:useradapter
   lateinit var finallist:MutableList<userlist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
      val  phoneee:MutableList<String>
 val contactlist:MutableList<userlist>
    finallist= arrayListOf()
        phoneee= arrayListOf()
    contactlist= arrayListOf()
    userlist= arrayListOf()
        recyclerView=findViewById(R.id.userlist)

        adapter= useradapter(applicationContext,finallist)
        val phone: Cursor?=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
        while (phone!!.moveToNext()){
            val numberr=phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val name=phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            phoneee.add(numberr)


        }
        uploaduserdata(phoneee)




    }




    private fun uploaduserdata(list: ArrayList<String>) {

        val ref  = FirebaseDatabase.getInstance().getReference().child("users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (i in snapshot.children) {

                    val data = i.getValue(com.rahul.dataclass.userlist::class.java)

                           val phonelist=i.child("phonenumber").getValue()


                            if (list==phonelist){
                                userlist.add(list as userlist)
                                adapter.notifyDataSetChanged()
                            }
                }
                for( iterator:userlist in userlist)
                    
                recyclerView.isNestedScrollingEnabled()
                recyclerView.hasFixedSize()
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                adapter = useradapter(applicationContext!!, userlist)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    companion object{
        private const val TAG = "FragmentActivity"
    }
}