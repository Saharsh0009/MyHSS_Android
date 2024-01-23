package com.uk.myhss.Guru_Dakshina_OneTime

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.*

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.myhss.Guru_Dakshina_OneTime.Model.StripeDataModel
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.Utils.InputFilterMinMax
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.stripe.android.view.CardMultilineWidget
import com.uk.myhss.Utils.SessionManager
import okhttp3.MultipartBody
import okio.Buffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import androidx.activity.ComponentActivity
import com.myhss.Guru_Dakshina_OneTime.Model.Get_Onetime.OneTimeSuccess
import com.myhss.Utils.DebouncedClickListener
import com.stripe.android.PaymentAuthConfig


class GuruDakshinaOneTimeFourthActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
    private lateinit var rootLayout: LinearLayout
    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout
    private lateinit var edit_payment: ImageView
    private lateinit var donate_amount_txt: TextView

    private lateinit var cardInputWidget: CardMultilineWidget
    private lateinit var stripe: Stripe
    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentID: String
    private lateinit var paymentStatus: String
    private lateinit var paymentStatusReason: String
    private lateinit var pd3: CustomProgressBar


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_fourth)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("OneTimeDakshinaStep4VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "OneTimeDakshinaStep4VC", "GuruDakshinaOneTimeFourthActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.one_time_dakshina)

        rootLayout = findViewById(R.id.rootLayout)
        back_layout = findViewById(R.id.back_layout)
        next_layout = findViewById(R.id.next_layout)
        edit_payment = findViewById(R.id.edit_payment)
        donate_amount_txt = findViewById(R.id.donate_amount_txt)
        cardInputWidget = findViewById(R.id.cardInputWidget)

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })
        edit_payment.setOnClickListener(DebouncedClickListener {
            depositDialog()
        })
        if (intent.getStringExtra("Amount") != "") {
            donate_amount_txt.text = intent.getStringExtra("Amount")
        }
        back_layout.setOnClickListener(DebouncedClickListener {
            finish()
        })
        cardInputWidget.setShouldShowPostalCode(false) // to hide and show postal code
        startCheckout()
    }

    private fun startCheckout() {
        next_layout.setOnClickListener(DebouncedClickListener {
            if (Functions.isConnectingToInternet(this@GuruDakshinaOneTimeFourthActivity)) {
                if (cardInputWidget.validateAllFields()) {
                    callApiForStripeData() //MyHSS API
                } else {
                    Toast.makeText(this, "Please verify the card details.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    this@GuruDakshinaOneTimeFourthActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun callApiForStripeData() {
        val pd = CustomProgressBar(this@GuruDakshinaOneTimeFourthActivity)
        pd.show()

        val builderData: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builderData.addFormDataPart("user_id", sessionManager.fetchUserID()!!)
        builderData.addFormDataPart("member_id", sessionManager.fetchMEMBERID().toString())
        builderData.addFormDataPart("amount", donate_amount_txt.text.toString()!!)
        builderData.addFormDataPart(
            "is_linked_member", intent.getStringExtra("donating_dakshina")!!
        )  //"Individual"
        builderData.addFormDataPart("gift_aid", intent.getStringExtra("GIFTAID_ID")!!)
        builderData.addFormDataPart(
            "is_purnima_dakshina", intent.getStringExtra("giving_dakshina")!!
        )
        builderData.addFormDataPart("line1", sessionManager.fetchLineOne()!!)
        builderData.addFormDataPart("city", sessionManager.fetchCITY()!!)
        builderData.addFormDataPart("country", sessionManager.fetchCOUNTRY()!!)
        builderData.addFormDataPart("postal_code", sessionManager.fetchPOSTCODE()!!)
        builderData.addFormDataPart("dakshina", getString(R.string.one_time))
        builderData.addFormDataPart(
            "card_number", cardInputWidget.cardNumberEditText.text.toString()
        )
        builderData.addFormDataPart(
            "name",
            sessionManager.fetchFIRSTNAME().toString() + sessionManager.fetchSURNAME().toString()
        )// need to replace with name
        builderData.addFormDataPart(
            "card_expiry", cardInputWidget.expiryDateEditText.text.toString()
        )
        builderData.addFormDataPart("card_cvv", cardInputWidget.cvcEditText.text.toString())
        builderData.addFormDataPart("app", "1")

        val requestBody: MultipartBody = builderData.build()
        for (i in 0 until requestBody.size) {
            val part = requestBody.part(i)
            val key = part.headers?.get("Content-Disposition")?.substringAfter("name=\"")
                ?.substringBefore("\"")
            val value = part.body?.let { requestBody ->
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                buffer.readUtf8()
            } ?: ""
            DebugLog.e("Param : " + "$key: $value")
        }

        val call: Call<StripeDataModel> =
            MyHssApplication.instance!!.api.postOneTimeDakshinaStripe(requestBody)
        call.enqueue(object : Callback<StripeDataModel> {
            override fun onResponse(
                call: Call<StripeDataModel>, response: Response<StripeDataModel>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.isSuccessful) {

                        val status = response.body()!!.status
                        val message = response.body()!!.message
                        if (status == true) {
                            pd.dismiss()
                            Toast.makeText(
                                this@GuruDakshinaOneTimeFourthActivity,
                                "$message. Please wait until payment complete.",
                                Toast.LENGTH_LONG
                            ).show()
                            paymentIntentClientSecret =
                                response.body()!!.info.payment_intent_client_secret
                            paymentID = response.body()!!.dakshina_pay_intent_id
                            makeStripePayement(
                                paymentIntentClientSecret,
                                response.body()!!.info.publishableKey
                            )
                        } else {
                            pd.dismiss()
                            Functions.showAlertMessageWithOK(
                                this@GuruDakshinaOneTimeFourthActivity, "", message.toString()
                            )
                        }
                    }
                } else {
                    pd.dismiss()
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaOneTimeFourthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }

            }

            override fun onFailure(call: Call<StripeDataModel>, t: Throwable) {
//                Toast.makeText(this@GuruDakshinaOneTimeFourthActivity, t.message, Toast.LENGTH_LONG)
//                    .show()
                pd.dismiss()
                Functions.showAlertMessageWithOK(
                    this@GuruDakshinaOneTimeFourthActivity, "Message",
                    t.message,
                )
            }
        })
    }

    private fun makeStripePayement(paymentIntentClientSecretKey: String, sPublishableKey: String) {
        PaymentConfiguration.init(
            applicationContext,
            sPublishableKey
        )
        stripe = Stripe(this, PaymentConfiguration.getInstance(applicationContext).publishableKey)
        val uiCustomization = PaymentAuthConfig.Stripe3ds2UiCustomization.Builder()
            .setLabelCustomization(
                PaymentAuthConfig.Stripe3ds2LabelCustomization.Builder().setTextFontSize(12).build()
            ).build()
        PaymentAuthConfig.init(
            PaymentAuthConfig.Builder().set3ds2Config(
                PaymentAuthConfig.Stripe3ds2Config.Builder().setTimeout(5)
                    .setUiCustomization(uiCustomization).build()
            ).build()
        )

        pd3 = CustomProgressBar(this@GuruDakshinaOneTimeFourthActivity)
        pd3.show()
        cardInputWidget.paymentMethodCreateParams?.let { params ->
            val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                params, paymentIntentClientSecretKey
            )
            stripe.confirmPayment(this, confirmParams)
        }
    }

    fun depositDialog() {
        // Deposit Dialog
        if (dialog == null) {
            dialog = Dialog(this, R.style.StyleCommonDialog)
        }
        dialog?.setContentView(R.layout.edit_dialog_diposit_money)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.show()

        val edit_amount = dialog!!.findViewById(R.id.edit_amount) as TextView
        val btnOk = dialog!!.findViewById(R.id.btnOk) as TextView

        if (intent.getStringExtra("Amount") != "") {
            edit_amount.text = intent.getStringExtra("Amount")
        }

        btnOk.setOnClickListener(DebouncedClickListener {
            if (edit_amount.text.toString().isNotEmpty()) {
                edit_amount.filters = arrayOf<InputFilter>(
                    InputFilterMinMax(
                        "1", "10000"
                    )
                )
                if (Integer.valueOf(edit_amount.text.toString()) > 0 && Integer.valueOf(edit_amount.text.toString()) <= 10000) {
                    donate_amount_txt.text = edit_amount.text.toString()
                    dialog?.dismiss()
                } else {
                    Snackbar.make(rootLayout, "Please enter amount 1-10000", Snackbar.LENGTH_SHORT)
                        .show()
                }

            } else {
                Snackbar.make(rootLayout, "Please enter amount", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                if (paymentIntent.status == StripeIntent.Status.Succeeded) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    DebugLog.e("Succeeded : " + " Response : " + gson.toJson(paymentIntent))
                    paymentStatus = "Succeeded"
                    paymentStatusReason = ""
                    pd3.dismiss()
                    savePaymentDataintoDB(gson.toJson(paymentIntent))
                } else if (paymentIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
                    DebugLog.e("Failed : " + " Response : " + paymentIntent.lastErrorMessage?.orEmpty())
                    paymentStatus = "Error"
                    paymentStatusReason = paymentIntent.lastErrorMessage?.orEmpty().toString()
                    pd3.dismiss()
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaOneTimeFourthActivity,
                        "Failed Message",
                        "$paymentStatusReason"
                    )
                    savePaymentDataintoDB(paymentIntent.lastErrorMessage?.orEmpty().toString())
                }
            }

            override fun onError(e: Exception) {
                DebugLog.e("OnError : " + " Error : $e")
                paymentStatus = "Error"
                paymentStatusReason = "$e"
                pd3.dismiss()
                Functions.showAlertMessageWithOK(
                    this@GuruDakshinaOneTimeFourthActivity, "Error Message", "$e"
                )
                savePaymentDataintoDB(e.toString())
            }
        })
    }

    private fun savePaymentDataintoDB(toJson: String) {
        val pd1 = CustomProgressBar(this@GuruDakshinaOneTimeFourthActivity)
        pd1.show()

        val builderData: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builderData.addFormDataPart("payment_status", paymentStatus)
        builderData.addFormDataPart("dakshina_pay_intent_id", paymentID)
        builderData.addFormDataPart("payment_object", toJson)
        builderData.addFormDataPart("payment_status_reason", paymentStatusReason)
        builderData.addFormDataPart("gateway", "stripe")
        builderData.addFormDataPart("app", "1")

        val requestBody: MultipartBody = builderData.build()
        for (i in 0 until requestBody.size) {
            val part = requestBody.part(i)
            val key = part.headers?.get("Content-Disposition")?.substringAfter("name=\"")
                ?.substringBefore("\"")
            val value = part.body?.let { requestBody ->
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                buffer.readUtf8()
            } ?: ""
            DebugLog.e("Param : $key: $value")
        }

        val call: Call<OneTimeSuccess> =
            MyHssApplication.instance!!.api.postSaveStripePaymentData(requestBody)
        call.enqueue(object : Callback<OneTimeSuccess> {
            override fun onResponse(
                call: Call<OneTimeSuccess>, response: Response<OneTimeSuccess>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == true) {
                            val alertDialog: AlertDialog.Builder =
                                AlertDialog.Builder(this@GuruDakshinaOneTimeFourthActivity)
                            alertDialog.setTitle("Guru Dakshina Payment")
                            alertDialog.setMessage(response.body()!!.message)
                            alertDialog.setCancelable(false)
                            alertDialog.setPositiveButton(
                                "Okay"
                            ) { _, _ ->
                                val i = Intent(
                                    this@GuruDakshinaOneTimeFourthActivity,
                                    GuruDakshinaOneTimeCompleteActivity::class.java
                                )
                                i.putExtra("paidAmount", response.body()!!.details.paid_amount)
                                i.putExtra("orderId", response.body()!!.details.order_id)
                                i.putExtra("status", response.body()!!.details.status)
                                i.putExtra("giftAid", response.body()!!.details.gift_aid)
                                i.putExtra("screen", "1")
                                startActivity(i)
                                finishAffinity()
                            }

                            val alert: AlertDialog = alertDialog.create()
                            alert.setCanceledOnTouchOutside(false)
                            alert.show()
                        } else {
                            Functions.showAlertMessageWithOK(
                                this@GuruDakshinaOneTimeFourthActivity,
                                "",
                                response.body()!!.message
                            )
                        }
                        pd1.dismiss()
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaOneTimeFourthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd1.dismiss()
            }

            override fun onFailure(call: Call<OneTimeSuccess>, t: Throwable) {
                pd1.dismiss()
                Functions.showAlertMessageWithOK(
                    this@GuruDakshinaOneTimeFourthActivity,
                    "Guru Dakshina Payment Failure",
                    "$t.message"
                )
            }
        })
    }
}