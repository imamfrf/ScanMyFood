package com.scanmyfood.imamf.scanmyfood.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.ImageView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scanmyfood.imamf.scanmyfood.R
import com.scanmyfood.imamf.scanmyfood.api.DownloadWebPage
import com.scanmyfood.imamf.scanmyfood.pattern.SingletonFirebase
import kotlinx.android.synthetic.main.activity_food_fact.*
import java.text.SimpleDateFormat
import java.util.*


class FoodFact : AppCompatActivity() {

    private var img_header: ImageView? = null
    private lateinit var mSingletonFirebase: SingletonFirebase
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var formattedDate: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_fact)

        mSingletonFirebase = SingletonFirebase.getInstance()
        mFirebaseAuth = mSingletonFirebase.firebaseAuth
        mFirebaseDatabase = mSingletonFirebase.firebaseDatabase

        //today's date
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy")
        formattedDate = df.format(c)


        getFoodData(intent.getStringExtra("productName"))

        bt_makan.setOnClickListener(object : View.OnClickListener {
            var cal = 0.0f
            override fun onClick(v: View) {
                addHistory()
                mFirebaseDatabase.getReference("users").child(mFirebaseAuth.currentUser!!.uid).child("daily").child(formattedDate).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        cal = java.lang.Float.valueOf(dataSnapshot.value!!.toString())
                        val cal2 = cal + java.lang.Float.valueOf(tv_cals_detail.text.toString())
                        mFirebaseDatabase.getReference("users").child(mFirebaseAuth.currentUser!!.uid).child("daily").child(formattedDate).setValue(cal2)
                                .addOnSuccessListener {
                                    Snackbar.make(findViewById(android.R.id.content), "Kalori berhasil ditambahkan", Snackbar.LENGTH_SHORT).show()

                                    Handler().postDelayed({
                                        val i = Intent(this@FoodFact, MainActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    }, 1500)
                                }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            }
        })



        main_collapsing.setContentScrimColor(resources.getColor(R.color.primary_trans))
        main_collapsing.setStatusBarScrimColor(resources.getColor(R.color.primary_trans))

    }

    fun getFoodData(foodName: String){
        val strUrl = "https://api.myjson.com/bins/qmhdo"
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            DownloadWebPage(this, foodName).execute(strUrl)
            //DownloadWebPage(this).execute()
            //tv_fun_fact.setText("ok")

        } else {
            //tv_res.setText("ERROR")
            tv_fun_fact.setText("ERROR")
        }

    }

    fun addHistory(){
        mFirebaseDatabase.getReference("users").child(mFirebaseAuth.currentUser!!.uid).child("history")
                .child(intent.getStringExtra("productName")).setValue("scanned")
    }

}
