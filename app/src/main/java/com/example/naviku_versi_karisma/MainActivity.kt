package com.example.naviku_versi_karisma

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var CameraM : CameraManager
    private lateinit var flashButton:ImageButton
    private lateinit var sensorManager: SensorManager
    private lateinit var lightSensor: Sensor
    var isFlash = false
    private var flashMode = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashButton = findViewById(R.id.flashButt)
        CameraM = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//        flashButton.setOnClickListener{flashlightONorOFF(it)}
        flashButton.setOnClickListener{changeFlashMode(it)}

        // Inisialisasi sensor cahaya
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Register listener sensor cahaya
        sensorManager.registerListener(lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)


        val imageButtonKodeku: ImageButton = findViewById(R.id.imageButtonKodeku)
        imageButtonKodeku.setOnClickListener(this)


    }

    private val lightSensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Tidak diperlukan implementasi khusus
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                val lightValue = event.values[0]
                if (lightValue < 5) {
                    // Aktifkan mode flash secara otomatis
                    if (!isFlash) {
                        val cameraListId = CameraM.cameraIdList[0]
                        CameraM.setTorchMode(cameraListId, true)
                        isFlash = true
                    }
                } else {
                    // Nonaktifkan mode flash
                    if (isFlash) {
                        val cameraListId = CameraM.cameraIdList[0]
                        CameraM.setTorchMode(cameraListId, false)
                        isFlash = false
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeFlashMode(v: View?) {
        flashMode = (flashMode + 1) % 3

        // Set flash mode
        val cameraListId = CameraM.cameraIdList[0]
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val params = cameraManager.getCameraCharacteristics(cameraListId)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
        if (params != null) {
            when (flashMode) {
                0 -> {
                    CameraM.setTorchMode(cameraListId, false)
                    flashButton.setImageResource(R.drawable.flash_auto)
                    Toast.makeText(this, "Flash mode: AUTO", Toast.LENGTH_SHORT).show()

                }
                1 -> {

                    CameraM.setTorchMode(cameraListId, true)
                    flashButton.setImageResource(R.drawable.flash_on)
                    Toast.makeText(this, "Flash mode: ON", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    CameraM.setTorchMode(cameraListId, false)
                    flashButton.setImageResource(R.drawable.flash_off)
                    Toast.makeText(this, "Flash mode: OFF", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButtonKodeku -> {
                val halamanKodeku = Intent(this@MainActivity, Kodeku:: class.java)
                startActivity(halamanKodeku)
            }

        }
    }
}