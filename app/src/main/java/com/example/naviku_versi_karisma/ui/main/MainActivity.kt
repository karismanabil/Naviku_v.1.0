package com.example.naviku_versi_karisma.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.naviku_versi_karisma.ui.kodeku.Kodeku
import com.example.naviku_versi_karisma.R
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    //flash
    private lateinit var flashButton:ImageButton
    private var isFlashOn: Boolean = false
    //sensor
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var isDark: Boolean = false
    //scan
    private lateinit var barcodeView: DecoratedBarcodeView
    private var timer: Timer? = null
    private var isProcessing: Boolean = false
    private lateinit var output: TextView
    //tts
    private lateinit var tts: TextToSpeech
    //camera permission
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Naviku_versi_karisma)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set tts bahasa indo
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                // Set language to default
                tts.language = Locale("id", "ID")
            }
        }

        //scanner
        barcodeView = findViewById(R.id.scannerView)
        output = findViewById(R.id.output)

        //flash
        flashButton = findViewById(R.id.flashButt)
        flashButton.setOnClickListener {
            if (isFlashOn) {
                flashModeOff()
            } else {
                flashModeOn()
            }
        }

//         untuk pindah halaman ke Kodeku
        val imageButtonKodeku: ImageButton = findViewById(R.id.imageButtonKodeku)
        imageButtonKodeku.setOnClickListener {
            // Aksi yang ingin dijalankan saat tombol di klik
            if (it.id == R.id.imageButtonKodeku) {
                val halamanKodeku = Intent(this@MainActivity, Kodeku:: class.java)
                startActivity(halamanKodeku)
            }
        }

//         sensor
        // Inisialisasi SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Mendapatkan sensor cahaya
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
//
        //message untuk talkback (sementara)
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (am.isEnabled) {
            val event = AccessibilityEvent.obtain()
            event.eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
            event.text.add("Selamat Datang di Naviku\n " +
                    "Memulai Pemindaian QR Code")
            am.sendAccessibilityEvent(event)
        }

        //perizinan aplikasi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            // Izin kamera sudah diberikan
            startCamera()
        }

        //buat menghilangkan default text
        val scannerView: DecoratedBarcodeView = findViewById(R.id.scannerView)
        scannerView.statusView.text = ""

    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()

        // Mendaftar SensorEventListener untuk sensor cahaya
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
        timer?.cancel()
        timer = null

        // Melepas pendaftaran SensorEventListener
        sensorManager.unregisterListener(this)

    }

//    flash
    private fun flashModeOn() {
        barcodeView.setTorchOn()
        flashButton.setImageResource(R.drawable.flash_on) // Ganti dengan ikon flashmode on
        isFlashOn = true
        speakInstruction("Tombol flash diaktifkan")
    }

    private fun flashModeOff() {
        barcodeView.setTorchOff()
        flashButton.setImageResource(R.drawable.flash_off) // Ganti dengan ikon flashmode off
        isFlashOn = false
        speakInstruction("Tombol flash dinonaktifkan")
    }
//    flash close

//    scan
    private fun startCamera() {
        val linearLayout2: LinearLayout = findViewById(R.id.linearLayout2)
        val strip2: View = findViewById(R.id.strip2)
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                if (!isProcessing) {
                    isProcessing = true

                    result?.let {
                        val text = it.text
                        output.text = "${it.text}"
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

                        // Mengganti latar belakang LinearLayout menjadi hijau
                        linearLayout2.setBackgroundColor(ContextCompat.getColor(output.context,
                            R.color.green__success_unduh_zoom_2x
                        ))

                        // Mengganti warna latar belakang View strip2 menjadi hijau
                        strip2.setBackgroundColor(ContextCompat.getColor(output.context,
                            R.color.green__line
                        ))
                    }

                    // Menghentikan timer yang ada sebelumnya
                    timer?.cancel()

                    // Memulai timer untuk pemindaian ulang setelah 5 detik
                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                isProcessing = false
                                barcodeView.resume()
                            }

                        }
                    }, 3000)
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
        })
    }

//sensor
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Tidak digunakan
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                val lightValue = event.values[0]

                // Mengatur batas nilai cahaya yang menandakan kegelapan
                val darkThreshold = 1

                if (!isFlashOn && lightValue <= darkThreshold && !isDark) {
                    // Terdeteksi kegelapan, lakukan instruksi untuk mengaktifkan tombol flash

                    // Instruksi menggunakan TTS
                    speakInstruction("Kegelapan terdeteksi. Tekan tombol flash untuk menyalakan senter.")

                    // Atau instruksi menggunakan TextView
//                     output.text = "Kegelapan terdeteksi. Tekan tombol flash untuk menyalakan lampu kilat."

                    isDark = true
                } else if (lightValue > darkThreshold && isDark) {
                    // Terang, tidak perlu instruksi

                    // Atau instruksi menggunakan TextView
                    // output.text = ""

                    isDark = false
                }
            }
        }
    }

    private fun speakInstruction(instruction: String) {
        tts.speak(instruction, TextToSpeech.QUEUE_FLUSH, null, null)
    }
//sensor close

}