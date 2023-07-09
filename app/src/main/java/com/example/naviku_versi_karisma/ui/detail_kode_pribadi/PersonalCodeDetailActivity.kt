package com.example.naviku_versi_karisma.ui.detail_kode_pribadi

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.naviku_versi_karisma.R
import com.example.naviku_versi_karisma.data.local.Code
import com.example.naviku_versi_karisma.databinding.ActivityPersonalCodeDetailBinding
import com.example.naviku_versi_karisma.helper.ViewModelFactory
import com.example.naviku_versi_karisma.ui.kode_pribadi.PersonalCodeListActivity
import com.example.naviku_versi_karisma.ui.kodeku.Kodeku
import com.example.naviku_versi_karisma.ui.main.MainActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*

class PersonalCodeDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CODE = "extra_code"
    }

    private var _activityPersonalCodeDetailBinding: ActivityPersonalCodeDetailBinding? = null
    private val binding get() = _activityPersonalCodeDetailBinding

    private lateinit var personalCodeDetailViewModel: PersonalCodeDetailViewModel

    private var code: Code? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityPersonalCodeDetailBinding = ActivityPersonalCodeDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        personalCodeDetailViewModel = obtainViewModel(this@PersonalCodeDetailActivity)

        @Suppress("DEPRECATION")
        code = intent.getParcelableExtra(EXTRA_CODE)

        if (code != null) {
            code?.let { code ->
                binding?.tvCodeDesc?.text = code.name

                val writer = QRCodeWriter()
                try {
                    val hints = Hashtable<EncodeHintType, Any>()
                    hints[EncodeHintType.MARGIN] = 0 // Set margin ke 0
                    val bitMatrix = writer.encode(code.name, BarcodeFormat.QR_CODE, 512, 512, hints)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                        }
                    }
                    binding?.ivCodeImg?.setImageBitmap(bmp)
                } catch (e: WriterException) {
                    e.printStackTrace()
                }
            }
        }

//        binding?.btnDeleteCodeDetail?.setOnClickListener {
//            val alertDialogBuilder = AlertDialog.Builder(this@PersonalCodeDetailActivity)
//            alertDialogBuilder.setTitle("Konfirmasi")
//            alertDialogBuilder.setMessage("Apakah Anda yakin ingin menghapus kode ini?")
//            alertDialogBuilder.setPositiveButton("Hapus") { _, _ ->
//                personalCodeDetailViewModel.delete(code as Code)
//                showToast(getString(R.string.deleted))
//
//                val intent = Intent(this@PersonalCodeDetailActivity, PersonalCodeListActivity::class.java)
//                startActivity(intent)
//            }
//            alertDialogBuilder.setNegativeButton("Batal") { dialog, _ ->
//                dialog.dismiss()
//            }
//            val alertDialog = alertDialogBuilder.create()
//            alertDialog.show()
//        }

        binding?.btnDeleteCodeDetail?.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this@PersonalCodeDetailActivity)

            // Mengatur tampilan kustom dari layout XML
            val layoutInflater = LayoutInflater.from(this@PersonalCodeDetailActivity)
            val view = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
            alertDialogBuilder.setView(view)

            // Dapatkan referensi ke elemen UI dalam tampilan kustom Anda
            val btnPositive = view.findViewById<Button>(R.id.btnPositive)
            val btnNegative = view.findViewById<Button>(R.id.btnNegative)
            val dialogMessage = view.findViewById<TextView>(R.id.dialogMessage)

            // Atur teks judul dan pesan
            dialogMessage.text = "Apakah Anda yakin ingin menghapus kode ini?"

            val alertDialog = alertDialogBuilder.create()

            // Atur onClickListener untuk tombol positif
            btnPositive.setOnClickListener { view ->
                // Kode yang akan dijalankan saat tombol "Hapus" diklik
                personalCodeDetailViewModel.delete(code as Code)
                showToast(getString(R.string.deleted))

                val intent = Intent(this@PersonalCodeDetailActivity, PersonalCodeListActivity::class.java)
                startActivity(intent)

                alertDialog.dismiss()
            }

            // Atur onClickListener untuk tombol negatif
            btnNegative.setOnClickListener { view ->
                alertDialog.dismiss()
            }

            // Menampilkan AlertDialog dengan tampilan kustom
            alertDialog.show()
        }



        binding?.btnKembaliCodeList?.setOnClickListener {
            val intent = Intent(this@PersonalCodeDetailActivity, PersonalCodeListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): PersonalCodeDetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PersonalCodeDetailViewModel::class.java]
    }
}