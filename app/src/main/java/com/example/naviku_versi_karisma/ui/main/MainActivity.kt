package com.example.naviku_versi_karisma.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import com.example.naviku_versi_karisma.ui.kodeku.KodekuPage
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
    private val welcomeText = "Selamat Datang di Naviku\n " +
            "Memulai Pemindaian Kode Q R"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Naviku_versi_karisma)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set tts bahasa indo
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                // Set language to default
                tts.language = Locale("id", "ID")
                speakWelcomeMessage()
            }
        }

        //scanner
        barcodeView = findViewById(R.id.scannerView)
        output = findViewById(R.id.tv_output)

        //flash
        flashButton = findViewById(R.id.btn_flash)
        flashButton.setOnClickListener {
            if (isFlashOn) {
                flashModeOff()
            } else {
                flashModeOn()
            }
        }

        // untuk pindah halaman ke Kodeku
        val imageButtonKodeku: ImageButton = findViewById(R.id.btn_kodeku)
        imageButtonKodeku.setOnClickListener {
            // Aksi yang ingin dijalankan saat tombol di klik
            if (it.id == R.id.btn_kodeku) {
                val halamanKodekuPage = Intent(this@MainActivity, KodekuPage:: class.java)
                startActivity(halamanKodekuPage)
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
                    "Memulai Pemindaian Kode Q R")
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

    private fun speakWelcomeMessage() {
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (am.isEnabled) {
            val event = AccessibilityEvent.obtain()
            event.eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
            event.text.add(welcomeText)
            am.sendAccessibilityEvent(event)
        } else {
            tts.speak(welcomeText, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()

        // Mendaftar SensorEventListener untuk sensor cahaya
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        speakWelcomeMessage()
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

    private var isSpeaking = false
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                val lightValue = event.values[0]
                val darkThreshold = 1

                val isDark = lightValue <= darkThreshold

                if (!isFlashOn && isDark && !isSpeaking) {
                    speakInstruction("Kegelapan terdeteksi. Tekan tombol flash untuk menyalakan senter.")
                    startInstructionLoop("Kegelapan terdeteksi. Tekan tombol flash untuk menyalakan senter.")
                } else if (!isDark) {
                    stopInstructionLoop()
                }
            }
        }
    }

    private fun startInstructionLoop(instruction: String) {
        isSpeaking = true
        val handler = Handler()
        handler.postDelayed({
            speakInstruction(instruction)
            if (isSpeaking) {
                startInstructionLoop(instruction)
            } else {
                stopInstructionLoop()
            }
        }, 5000) // Delay selama 5 detik (5000 millis)
    }

    private fun stopInstructionLoop() {
        isSpeaking = false
    }

    private fun speakInstruction(instruction: String) {
        tts.speak(instruction, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}