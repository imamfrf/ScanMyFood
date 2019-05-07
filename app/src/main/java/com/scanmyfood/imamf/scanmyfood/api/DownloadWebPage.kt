package com.scanmyfood.imamf.scanmyfood.api

import android.os.AsyncTask
import android.util.Log
import com.bumptech.glide.Glide
import com.scanmyfood.imamf.scanmyfood.Model.Food
import com.scanmyfood.imamf.scanmyfood.ui.FoodFact

import kotlinx.android.synthetic.main.activity_food_fact.*

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class DownloadWebPage(activity: FoodFact, foodName: String) : AsyncTask<String, Void, String>() {
    private val mActivity: WeakReference<FoodFact>
    private var foodName: String


    init {
        this.mActivity = WeakReference(activity)
        this.foodName = foodName
    }

    override fun doInBackground(vararg urls: String): String {
        try {
            return downloadUrl(urls[0])
        } catch (e: IOException) {
            return "URL Invalid"
        }

        Log.d("tes123", urls[0])

    }

    override fun onPostExecute(s: String) {
        val activity = mActivity.get()
        var id = ""
        var name = ""
        var calorie = ""
        var fat = ""
        var carb = ""
        var protein = ""
        var imgSrc = ""
        var funFact = ""
        Log.d("tes456", s)

        try {
            val jsonObj = JSONObject(s)
            val jsonArr = jsonObj.getJSONArray("result")

            Log.d("tes7", jsonArr.length().toString())

            for (i in 0..jsonArr.length()) {
                if (jsonArr.getJSONObject(i).getString("name") == foodName) {
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
            Log.d("TES1", jsonArr.length().toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        if (activity != null) {
            val food = Food.Builder()
                    .setId(id)
                    .setName(name)
                    .setCalorie(calorie)
                    .setFat(fat)
                    .setCarb(carb)
                    .setProtein(protein)
                    .setImg(imgSrc)
                    .setFunFact(funFact)
                    .create()

            activity.setSupportActionBar(activity.main_toolbar)
            activity.supportActionBar!!.setDisplayShowTitleEnabled(true)
            activity.supportActionBar!!.title = food.name
            activity.tv_cals_detail.text = food.calorie
            activity.tv_fat_detail.text = food.fat+" gr"
            activity.tv_carb_detail.text = food.carb+" gr"
            activity.tv_pro_detail.text = food.protein+" gr"
            activity.tv_fun_fact.text = food.funFact
            Glide.with(activity.applicationContext)
                    .load(food.img)
                    .into(activity.img_food_header!!)
        }

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
