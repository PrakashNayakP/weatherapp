package com.androdocs.weather

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationRequest
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TestLooperManager
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val API: String = "53747bcb12fe5823f353a1822d10e531"
    val WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather"
    val CITY: String = "dhaka,bd"
    /* val MIN_TIME = 5000
    val MIN_DIST = 1000
    val PERMISSION_ID = 1010
    val REQUEST_CODE = 101
    var LOCATION_PROVIDER = LocationManager.GPS_PROVIDER

    var datetext:TextView=findViewById(R.id.datetime)
    var placetext:TextView=findViewById(R.id.placetext)
    var stateimage:ImageView=findViewById(R.id.stateimage)

     var mLocationManager:LocationManager?=null
     var mLocationListner : LocationRequest?=null */




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Weather)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        weatherTask().execute()

        /*if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)
*/
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.constained1).visibility = View.GONE
        }
        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                )
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+", "+sys.getString("country")
                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.placetext).text = address
                findViewById<TextView>(R.id.datetime).text =  updatedAtText
                findViewById<TextView>(R.id.temparature).text = temp
                findViewById<TextView>(R.id.minmax).text = tempMin
               /* findViewById<TextView>(R.id.minmax).text = tempMax*/
                findViewById<TextView>(R.id.preciption).text = windSpeed

                findViewById<TextView>(R.id.humidi).text = humidity
                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.constained1).visibility = View.VISIBLE
            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
            }
        }
    }

}