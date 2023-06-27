package com.myhss.QRCode

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Point
import android.opengl.Visibility
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.*

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import android.widget.Toast
import com.google.zxing.BarcodeFormat

import com.google.zxing.MultiFormatWriter

import com.google.zxing.common.BitMatrix
import java.lang.IllegalArgumentException
import com.google.zxing.integration.android.IntentResult


class QRCodeFragment : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView

    private lateinit var qrCodeIV: ImageView
    private lateinit var idFirstname: TextInputEditText
    private lateinit var idMiddlename: TextInputEditText
    private lateinit var idLastname: TextInputEditText
    private lateinit var idEmail: TextInputEditText
    private lateinit var idPhone: TextInputEditText
    private lateinit var idRole: TextInputEditText
    private lateinit var generateQrBtn: Button
    private lateinit var scanQrBtn: Button

    /*QR Code Read/Scan*/
    lateinit var bitmap: Bitmap
//    lateinit var qrgEncoder: QRGEncoder

    private lateinit var mQrResultLauncher: ActivityResultLauncher<Intent>

    var EditTextValue: String? = ""
    var thread: Thread? = null
    val QRcodeWidth = 350

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_qrcode)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("QRCodeFragmentVC")
        sessionManager.firebaseAnalytics.setUserProperty("QRCodeFragmentVC", "QRCodeFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)

        header_title.text = getString(R.string.qr_code)

        back_arrow.setOnClickListener {
            val i = Intent(this@QRCodeFragment, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

        // initializing all variables.
        qrCodeIV = findViewById(R.id.idIVQrcode)
        idFirstname = findViewById(R.id.idFirstname)
        idMiddlename = findViewById(R.id.idMiddlename)
        idLastname = findViewById(R.id.idLastname)
        idEmail = findViewById(R.id.idEmail)
        idPhone = findViewById(R.id.idPhone)
        idRole = findViewById(R.id.idRole)
        generateQrBtn = findViewById(R.id.idBtnGenerateQR)
        scanQrBtn = findViewById(R.id.idBtnScanerQR)
        scanQrBtn.visibility = View.GONE

        Log.d("EVENT", intent.getStringExtra("EVENT")!!)
        Log.d("DISCRIPTION", intent.getStringExtra("DISCRIPTION")!!)
        Log.d("INFO", intent.getStringExtra("INFO")!!)

        idFirstname.setText(sessionManager.fetchFIRSTNAME())
        idMiddlename.setText(sessionManager.fetchMIDDLENAME())
        idLastname.setText(sessionManager.fetchSURNAME())
        idEmail.setText(sessionManager.fetchUSEREMAIL())
        idPhone.setText(sessionManager.fetchMOBILENO())
        idRole.setText(sessionManager.fetchUSERROLE() + "\n" + sessionManager.fetchSHAKHANAME())

        generateQrBtn.setOnClickListener {
//            if (TextUtils.isEmpty(idFirstname.text.toString())) {
                EditTextValue =
                    idRole.text.toString() + "\n" + idFirstname.text.toString() + "\n" + idMiddlename.text.toString() + "\n" + idLastname.text.toString() + "\n" + idEmail.text.toString() + "\n" + idPhone.text.toString() + "\n" + idEmail.text.toString() + "\n" + idPhone.text.toString() + "\n" + idEmail.text.toString() + "\n" + idPhone.text.toString()
                try {
                    bitmap = TextToImageEncode(EditTextValue)!!
                    qrCodeIV.setImageBitmap(bitmap)
                } catch (e: WriterException) {
                    e.printStackTrace()
                }
//            } else {
//                idFirstname.requestFocus()
//                Toast.makeText(
//                    this@QRCodeFragment,
//                    "Please Enter Your Scanned Test",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
            /*if (TextUtils.isEmpty(idFirstname.text.toString())) {

                // if the edittext inputs are empty then execute
                // this method showing a toast message.
                Toast.makeText(
                    this@QRCodeFragment,
                    "Enter some text to generate QR Code",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // below line is for getting
                // the windowmanager service.
                val manager: WindowManager? =
                    getSystemService(android.content.Context.WINDOW_SERVICE) as WindowManager?

                // initializing a variable for default display.
                val display: Display = manager!!.defaultDisplay

                // creating a variable for point which
                // is to be displayed in QR Code.
                val point: Point = Point()
                display.getSize(point)

                // getting width and
                // height of a point
                val width: Int = point.x
                val height: Int = point.y

                // generating dimension from width and height.
                var dimen: Int = if (width < height) width else height
                dimen = dimen * 3 / 4

                // setting this dimensions inside our qr code
                // encoder to generate our qr code.
                qrgEncoder =
                    QRGEncoder(idRole.text.toString()+"\n"+idFirstname.text.toString()
                        +"\n"+idMiddlename.text.toString()+"\n"+idLastname.text.toString()
                        +"\n"+idEmail.text.toString()+"\n"+idPhone.text.toString()
                        +"\n"+idEmail.text.toString()+"\n"+idPhone.text.toString()
                        +"\n"+idEmail.text.toString()+"\n"+idPhone.text.toString(), null, QRGContents.Type.TEXT, dimen)
                try {
                    // getting our qrcode in the form of bitmap.
                    bitmap = qrgEncoder.encodeAsBitmap()
                    // the bitmap is set inside our image
                    // view using .setimagebitmap method.
                    qrCodeIV.setImageBitmap(bitmap)
                    bitmap.addOverlayToCenter(bitmap)
                } catch (e: WriterException) {
                    // this method is called for
                    // exception handling.
                    Log.e("Tag", e.toString())
                }
            }*/
        }

        // Alternative to "onActivityResult", because that is "deprecated"
//        mQrResultLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                if (it.resultCode == Activity.RESULT_OK) {
//                    val result = IntentIntegrator.parseActivityResult(it.resultCode, it.data)
//
//                    if (result.contents != null) {
//                        // Do something with the contents (this is usually a URL)
//                        println("RESULT==>" + result.contents)
//                    }
//                }
//            }

        scanQrBtn.setOnClickListener {
//            startScanner()
            val integrator = IntentIntegrator(this@QRCodeFragment)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrator.setPrompt("Scan")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()
        }

    }

    @Throws(WriterException::class)
    fun TextToImageEncode(Value: String?): Bitmap? {
        val bitMatrix: BitMatrix
        bitMatrix = try {
            MultiFormatWriter().encode(
                Value,
                BarcodeFormat.QR_CODE,
                QRcodeWidth, QRcodeWidth, null
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
            return null
        }
        val bitMatrixWidth = bitMatrix.width
        val bitMatrixHeight = bitMatrix.height
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] =
                    if (bitMatrix[x, y]) resources.getColor(R.color.black) else resources.getColor(
                        R.color.white
                    )
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.e("Scan*******", "Cancelled scan")
            } else {
                Log.e("Scan", "Scanned")
//                tv_qr_readTxt.setText(result.contents)
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Start the QR Scanner
    private fun startScanner() {
        val scanner = IntentIntegrator(this)
        // QR Code Format
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        // Set Text Prompt at Bottom of QR code Scanner Activity
        scanner.setPrompt("QR Code Scanner Prompt Text")
        // Start Scanner (don't use initiateScan() unless if you want to use OnActivityResult)
        mQrResultLauncher.launch(scanner.createScanIntent())
    }

    fun Bitmap.addOverlayToCenter(overlayBitmap: Bitmap): Bitmap {

        val bitmap2Width = overlayBitmap.width
        val bitmap2Height = overlayBitmap.height
        val marginLeft = (this.width * 0.5 - bitmap2Width * 0.5).toFloat()
        val marginTop = (this.height * 0.5 - bitmap2Height * 0.5).toFloat()
        val canvas = Canvas(this)
        canvas.drawBitmap(this, Matrix(), null)
        canvas.drawBitmap(overlayBitmap, marginLeft, marginTop, null)
        return this
    }
}