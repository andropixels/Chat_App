package com.rahul.chatingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.common.SignInButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    var isCodeSent=false
    private var storedVerificationId: String? = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var otp:EditText
    private  lateinit var sendbtn:Button
    private lateinit var authbtn:Button
//    private lateinit var finduser:Button
    private lateinit var  phonenumber:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth= Firebase.auth
        otp=findViewById(R.id.code)
        sendbtn=findViewById(R.id.sendbtn)
        phonenumber=findViewById(R.id.phone)
        authbtn=findViewById(R.id.authbtn)
//        finduser=findViewById<Button>(R.id.finduserbtn)
        auth.setLanguageCode("en")

            isloggedin()
//
//        finduser.setOnClickListener {
//            startActivity(Intent(applicationContext,Users::class.java)) }
        sendbtn.setOnClickListener {

            //startActivity(Intent(this,MainActivity::class.java))



                val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91${phonenumber.text}")       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                PhoneAuthProvider.verifyPhoneNumber(options)



        }
        authbtn.setOnClickListener {
               auth()
        }



        callbacks=object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val  code=p0.smsCode
                if (code!=null){

                   otp.setText(code.toString())
                    signInWithPhoneAuthCredential(p0)


                }

            }


            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d(Companion.TAG, "onVerificationFailed: "+p0.localizedMessage)
                isCodeSent=false
                Toast.makeText(this@MainActivity,"ffailed",Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Log.d(TAG, "onCodeSent: $p0")
                otp.visibility= View.VISIBLE
                sendbtn.text = "Verify"
                    isCodeSent=true
                storedVerificationId=p0

            }
        }



    }

    private fun uploaduserdata(phonenumber:String) {

        val usermap:HashMap<String,Any>
        val currentuserid=FirebaseAuth.getInstance().currentUser!!.uid

        usermap=HashMap()
        usermap["id"]=currentuserid
        usermap["phonenumber"]=phonenumber
       val ref= FirebaseDatabase.getInstance().getReference().child("users")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                ref.child(currentuserid).setValue(usermap)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })






    }

    private fun auth(){

        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp.text.toString())
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        uploaduserdata(phonenumber.text.toString())

                        Log.d(TAG, "signInWithCredential:success")
                        Toast.makeText(this,"succesful",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@MainActivity,chatScreen::class.java))

                        val user = task.result?.user

                    } else {
                        Log.d(TAG, "signInWithCredential:invalid code")

                    }
                }
    }

    companion object {
        private const val TAG = "LoginScreenActivity"
    }


    private fun isloggedin(){
      if (auth.currentUser!=null){

          startActivity(Intent(applicationContext,chatScreen::class.java))
          finish()

      }

    }
}