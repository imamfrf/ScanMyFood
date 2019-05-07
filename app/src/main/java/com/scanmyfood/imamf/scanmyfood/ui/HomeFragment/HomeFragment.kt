package com.scanmyfood.imamf.scanmyfood.ui.HomeFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper
import com.ibm.watson.developer_cloud.service.security.IamOptions
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions
import com.scanmyfood.imamf.scanmyfood.ui.FoodFact
import com.scanmyfood.imamf.scanmyfood.Model.Food
import com.scanmyfood.imamf.scanmyfood.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), homeHistoryListener {

    private lateinit var camHelper: CameraHelper
    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var list_item = ArrayList<Food>()
    private lateinit var formattedDate: String
    private lateinit var adapter: HomeHistoryAdapter
    private lateinit var recV_home: RecyclerView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       // val inflate = inflater.inflate(R.layout.fragment_home, null)

        val view: View = inflater!!.inflate(R.layout.fragment_home, null)

        //today's date
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy")
        formattedDate = df.format(c)

        Log.d("TGL", formattedDate)

        camHelper = CameraHelper(activity)

        view.viewDimScreen.visibility = View.GONE

        view.bt_scan.setOnClickListener {
            camHelper.dispatchTakePictureIntent() }

        recV_home = view.recV_home_history
        //recV_home.setHasFixedSize(true)
        recV_home.layoutManager = getReverseLinearLayoutManager()

        adapter = HomeHistoryAdapter(list_item, this@HomeFragment, this@HomeFragment)


        initData()
//        getUserData()

        // Inflate the layout for this fragment
        return view
    }

    fun initData(){
        db.getReference("users").child(auth.currentUser!!.uid).child("daily").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(formattedDate).value == null){
                    //tv_cal_consumed?.text = p1.child(formattedDate).value.toString()
                    db.getReference("users").child(auth.currentUser!!.uid).child("daily").child(formattedDate)
                            .setValue(0)
//                        tv_cal_consumed?.text = p1.child(formattedDate).value.toString()

                }
            }
        })

        getUserData()
    }

    fun getUserData(){

        db.getReference("users").child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var cal_consumed = 0f
                var cal_need = 0f
                for (p1 in p0.children){

                        if (p1.key == "nama"){
                            tv_greeting?.text = "Hai, "+p1.value+"!"
                        }
                        if (p1.key == "daily"){
                            tv_cal_consumed?.text = p1.child(formattedDate).value.toString()
                            cal_consumed = p1.child(formattedDate).value.toString().toFloat()
                        }
                        if (p1.key == "kebutuhanKalori"){
                            tv_cal_need?.text = "/ "+String.format("%.0f", p1.value)+" kal"
                            cal_need = p1.value.toString().toFloat()
                        }
                        if (p1.key == "history"){
                            for (p2 in p1.children){
                                getFoodData(p2.key.toString())
                                adapter.notifyDataSetChanged()
                            }
                        }





                }
                var percent = (cal_consumed / cal_need) * 100
                crpv.startAngle = 0f
                crpv.percent = percent

                tv_percent.text = "Kamu telah memenuhi "+String.format("%.0f", percent)+"% kebutuhan kalori mu"
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            //organic scan result
            if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
                //get image path after captured
                val photoFile = camHelper.getFile(resultCode)
                imgRecogntion(photoFile)
            }
        }
    }

    //Object detection using IBM Watson Visual Recognition
    fun imgRecogntion(file: File) {
        AsyncTask.execute {
            activity!!.runOnUiThread {
                //                        layout_pBar.setVisibility(View.VISIBLE);
                startLoadingIndicator()
            }
            val options = IamOptions.Builder()
                    .apiKey(resources.getString(R.string.api_key))
                    .build()

            val visualRecognition = VisualRecognition("2018-03-19", options)

            var classifyOptions: ClassifyOptions? = null
            try {
                classifyOptions = ClassifyOptions.Builder()
                        .imagesFile(file)
                        .classifierIds(Arrays.asList("DefaultCustomModel_1553789228"))
                        .build()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            val result = visualRecognition.classify(classifyOptions!!).execute()

            Log.d("nasgor", result.toString())


            if (result.images[0].classifiers[0].classes[0].className != null) {
                activity!!.runOnUiThread { stopLoadingIndicator() }
                val i = Intent(activity, FoodFact::class.java)
                i.putExtra("productName", result.images[0].classifiers[0].classes[0].className)
                startActivity(i)
            } else {
                Toast.makeText(activity, "Mohon maaf makanan belum dikenali", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getReverseLinearLayoutManager(): LinearLayoutManager {
        val reverseLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        reverseLinearLayoutManager.stackFromEnd = true
        return reverseLinearLayoutManager
    }


    private fun startLoadingIndicator() {
        viewDimScreen.visibility = View.VISIBLE
        aviLoadingIndicatorView.smoothToShow()
    }

    private fun stopLoadingIndicator() {
        viewDimScreen.visibility = View.GONE
        aviLoadingIndicatorView.smoothToHide()
    }

    override fun onItemClick(id: String?, name: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getFoodData(foodName: String){
        val strUrl = "https://api.myjson.com/bins/qmhdo"
        val connMgr = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            DownloadWebPage(foodName, this).execute(strUrl)
            //DownloadWebPage(this).execute()
            //tv_fun_fact.setText("ok")

        } else {
            Toast.makeText(context, "Load data error", Toast.LENGTH_SHORT)
        }

    }


    inner class DownloadWebPage(foodName: String, home: HomeFragment) : AsyncTask<String, Void, String>() {
        private var foodName: String
        private var home: HomeFragment

        init {
            this.foodName = foodName
            this.home = home
        }

        override fun doInBackground(vararg urls: String): String {
            try {
                return downloadUrl(urls[0])
            } catch (e: IOException) {
                return "URL Invalid"
            }

        }

        override fun onPostExecute(s: String) {
            var id = ""
            var name = ""
            var calorie = ""
            var fat = ""
            var carb = ""
            var protein = ""
            var imgSrc = ""
            var funFact = ""


            try {
                val jsonObj = JSONObject(s)
                val jsonArr = jsonObj.getJSONArray("result")

                Log.d("tes8", jsonArr.length().toString())
                for (i in 0..jsonArr.length()){
                    if (jsonArr.getJSONObject(i).getString("name") == foodName){
                        id = jsonArr.getJSONObject(i).getString("id")
                        name = jsonArr.getJSONObject(i).getString("name")
                        calorie = jsonArr.getJSONObject(i).getString("calorie")
                        fat = jsonArr.getJSONObject(i).getString("fat")
                        carb = jsonArr.getJSONObject(i).getString("carb")
                        protein = jsonArr.getJSONObject(i).getString("protein")
                        imgSrc = jsonArr.getJSONObject(i).getString("imgSrc")
                        funFact = jsonArr.getJSONObject(i).getString("funFact")
                    }
                }

                Log.d("TES1", jsonArr.toString())

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val food = Food(id, name, calorie, fat, carb, protein, imgSrc, funFact)
            home.list_item.add(food)
            home.adapter = HomeHistoryAdapter(home.list_item, this@HomeFragment, this@HomeFragment)
            home.recV_home.adapter = home.adapter
            home.adapter.notifyDataSetChanged()

        }

        @Throws(IOException::class)
        private fun downloadUrl(myUrl: String): String {
            var `is`: InputStream? = null
            val len = 10000

            try {
                val url = URL(myUrl)
                val conn = url.openConnection() as HttpURLConnection
                conn.readTimeout = 10000
                conn.connectTimeout = 15000
                conn.requestMethod = "GET"
                conn.doInput = true

                conn.connect()
                val response = conn.responseCode
                `is` = conn.inputStream

                return readIt(`is`, len)

            } finally {
                `is`?.close()
            }
        }

        @Throws(IOException::class, UnsupportedEncodingException::class)
        fun readIt(stream: InputStream?, len: Int): String {
            var reader: Reader? = null
            reader = InputStreamReader(stream, "UTF-8")
            val buffer = CharArray(len)
            reader.read(buffer)
            return String(buffer)


        }
    }

}





