package com.example.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.weather.R
import com.example.weather.api.WeatherAPIService
import com.example.weather.api.WeatherAppAPIClient
import com.example.weather.vo.CurrentWeather
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewViewModel
    private lateinit var weatherRepository: WeatherRepository

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val Permission_if = 100
    var locationLongitute: Double = 0.0
    var locationLatitute: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

    }

    @SuppressLint("SetTextI18n")
    fun bind (it: CurrentWeather){
        address.text = it.name + ", " + it.sys.country
        updated_at.text = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(it.dt.toLong()*1000))
        status.text = it.weather[0].description
        temp.text = it.main.temp.toString() +"°C"
        temp_min.text = "Min Temp: " + it.main.tempMin.toString() + "°C"
        temp_max.text = "Max Temp: " + it.main.tempMax.toString() + "°C"
        sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(it.sys.sunrise.toLong()*1000))
        sunset.text =  SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(it.sys.sunset.toLong()*1000))
        wind.text = it.wind.speed.toString()
        pressure.text = it.main.pressure.toString()
        humidity.text = it.main.humidity.toString()

        loader.visibility = View.GONE
        mainContainer.visibility = View.VISIBLE
    }

    private fun getViewModel(latitude: Double, longitude: Double): MainViewViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewViewModel(weatherRepository,latitude, longitude) as T
            }
        })[MainViewViewModel::class.java]
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if (location == null){
                        NewLocationData()
                    } else {
                        locationLatitute = location.latitude
                        locationLongitute = location.longitude
                        Log.d("LocLat", locationLatitute.toString())
                        Log.d("LocLong", locationLongitute.toString())
                        //CITY = getCityName(locationLatitute, locationLongitute)
                        //Log.d("Location123", CITY)


                        val apiService: WeatherAPIService = WeatherAppAPIClient.invoke()
                        weatherRepository = WeatherRepository(apiService)

                        viewModel = getViewModel(locationLatitute, locationLongitute)
                        viewModel.weather.observe(this, {
                            bind(it)
                        })
                        viewModel.networkState.observe(this, {

                        })
                    }
                }
            } else {
                Toast.makeText(this,"Please Enable your location", Toast.LENGTH_SHORT).show()
            }
        } else {
            RequestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult){
            var lastLocation: Location = locationResult.lastLocation

        }
    }

    // Checking permission for location
    private fun CheckPermission() : Boolean{
        return ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    private fun RequestPermission(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                Permission_if
        )
    }

    private fun isLocationEnabled(): Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if(requestCode == Permission_if){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug", "You Have the permission")
            }
        }
    }
}