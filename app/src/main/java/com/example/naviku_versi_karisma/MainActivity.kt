package com.example.naviku_versi_karisma

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var CameraM : CameraManager
    private lateinit var flashButton:ImageButton
    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null
    private var sensorEventListener: SensorEventListener? = null
    private var flashMode = 0

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Naviku_versi_karisma)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //sharedd preference
        sharedPref = getSharedPreferences("FlashPrefs", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        flashMode = sharedPref.getInt("flashMode", 0)

        // flash
        flashButton = findViewById(R.id.flashButt)
        CameraM = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        flashButton.setOnClickListener{changeFlashMode(it)}

        // untuk pindah halaman
        val imageButtonKodeku: ImageButton = findViewById(R.id.imageButtonKodeku)
        imageButtonKodeku.setOnClickListener(this)


        // Atur ikon tombol flash sesuai dengan mode flash yang tersimpan
        when (flashMode) {
            0 -> {
                flashButton.setImageResource(R.drawable.flash_auto)
            }
            1 -> {
                flashButton.setImageResource(R.drawable.flash_off)
            }
            2 -> {
                flashButton.setImageResource(R.drawable.flash_on)
            }
        }

        //message
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (am.isEnabled) {
            val event = AccessibilityEvent.obtain()
            event.eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
            event.text.add("Flash akan otomatis menyala saat sensor mendeteksi kegelapan.\n " +
                    "Anda dapat mengatur mode flash dengan memencet tombol flash")
            am.sendAccessibilityEvent(event)
        }

    }


    override fun onResume() {
        super.onResume()
        // Mendapatkan instance dari SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Mendapatkan instance dari light sensor
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
        // Mendaftarkan listener untuk light sensor jika mode flash AUTO
        if (flashMode == 0) {
            registerLightSensorListener()
        }
    }

    override fun onPause() {
        super.onPause()
        // Mencopot listener saat activity tidak aktif
        if (flashMode == 0) {
            // Mencopot listener saat activity tidak aktif
            sensorManager?.unregisterListener(sensorEventListener)
        }
    }

    private fun registerLightSensorListener() {
        // Cek kondisi cahaya lingkungan
        sensorEventListener = object : SensorEventListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && event.sensor.type == Sensor.TYPE_LIGHT) {
                    val lightValue = event.values[0]
                    if (lightValue < 1) { // Ubah nilai 10 sesuai dengan kebutuhan
                        val cameraListId = CameraM.cameraIdList[0]
                        CameraM.setTorchMode(cameraListId, true)
                    } else {
                        val cameraListId = CameraM.cameraIdList[0]
                        CameraM.setTorchMode(cameraListId, false)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }
        sensorManager?.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeFlashMode(v: View?) {
        flashMode = (flashMode + 1) % 3

        // Simpan mode flash saat ini ke SharedPreferences
        editor.putInt("flashMode", flashMode)
        editor.apply()

        // Set flash mode
        val cameraListId = CameraM.cameraIdList[0]
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val params = cameraManager.getCameraCharacteristics(cameraListId)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
        if (params != null) {
            when (flashMode) {
                0 -> {
                    Toast.makeText(this, "Flash mode AUTO", Toast.LENGTH_SHORT).show()
                    flashButton.setImageResource(R.drawable.flash_auto)
                    registerLightSensorListener()
                }
                1 -> {
                    Toast.makeText(this, "Flash mode OFF", Toast.LENGTH_SHORT).show()
                    flashButton.setImageResource(R.drawable.flash_off)
                    CameraM.setTorchMode(cameraListId, false)
                    sensorManager?.unregisterListener(sensorEventListener)
                }
                2 -> {
                    Toast.makeText(this, "Flash mode ON", Toast.LENGTH_SHORT).show()
                    flashButton.setImageResource(R.drawable.flash_on)
                    CameraM.setTorchMode(cameraListId, true)
                    sensorManager?.unregisterListener(sensorEventListener)
                }
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButtonKodeku -> {
                val halamanKodeku = Intent(this@MainActivity, Kodeku:: class.java)
                startActivity(halamanKodeku)
            }

        }
    }

}