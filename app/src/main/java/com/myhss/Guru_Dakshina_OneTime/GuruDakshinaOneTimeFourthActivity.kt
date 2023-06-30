package com.uk.myhss.Guru_Dakshina_OneTime

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import com.stripe.android.PaymentAuthConfig
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.uk.myhss.Main.HomeActivity
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class GuruDakshinaOneTimeFourthActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
    private lateinit var rootLayout: LinearLayout
    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout
    private lateinit var edit_payment: ImageView
    private lateinit var donate_amount_txt: TextView
    lateinit var cardInputWidget: CardMultilineWidget

    private lateinit var stripe: Stripe
    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentID: String
    private lateinit var paymentStatus: String
    private lateinit var paymentStatusReason: String
    private lateinit var paymentLauncher: PaymentLauncher

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
        back_arrow.setOnClickListener {
            finish()
        }
        edit_payment.setOnClickListener {
            depositDialog()
        }
        if (intent.getStringExtra("Amount") != "") {
            donate_amount_txt.text = intent.getStringExtra("Amount")
        }
        back_layout.setOnClickListener {
            finish()
        }

        cardInputWidget.setShouldShowPostalCode(false) // to hide and show postal code
        stripe = Stripe(this, PaymentConfiguration.getInstance(applicationContext).publishableKey)

        val paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        val uiCustomization =
            PaymentAuthConfig.Stripe3ds2UiCustomization.Builder().setLabelCustomization(
                PaymentAuthConfig.Stripe3ds2LabelCustomization.Builder().setTextFontSize(12)
                    .build()
            ).build()
        PaymentAuthConfig.init(
            PaymentAuthConfig.Builder().set3ds2Config(
                PaymentAuthConfig.Stripe3ds2Config.Builder().setTimeout(5)
                    .setUiCustomization(uiCustomization).build()
            ).build()
        )
        paymentLauncher = PaymentLauncher.Companion.create(
            this,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId,
            ::onPaymentResult
        )
        startCheckout()
    }

    private fun startCheckout() {
        next_layout.setOnClickListener {
//            paymentIntentClientSecret = "pi_3NNwkdSD1lLtRImz1m73TnGP_secret_a8xvRJGLE6VNWqrEpoXSxfgLF"
//            makeStripePayement(paymentIntentClientSecret)
            callApiForStripeData() //MyHSS API
//              callDemoMethodApiForSTripe() //Demo API work well
        }
    }

    private fun callDemoMethodApiForSTripe() {
        val pd = CustomProgressBar(this@GuruDakshinaOneTimeFourthActivity)
        pd.show()
        val queue = Volley.newRequestQueue(this)
        val url = "https://demo.codeseasy.com/apis/stripe/"
        val stringRequest: StringRequest = object : StringRequest(Method.POST,
            url,
            com.android.volley.Response.Listener<String> { response ->
                Log.e("Response : ", "Response is: $response")

                try {
                    val responseJson = JSONObject(response)
                    val paymentIntent = responseJson.getString("paymentIntent")
                    paymentIntentClientSecret = paymentIntent
                    makeStripePayement(paymentIntentClientSecret)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON parsing error
                    Log.e("Error Response Issue : ", "That didn't work! ${e.toString()}")
                }

            },
            com.android.volley.Response.ErrorListener { error ->
                Log.e("Error Response : ", "That didn't work!")
                error.printStackTrace()

            }) {
            override fun getParams(): Map<String, String>? {
                val paramV: MutableMap<String, String> = HashMap()
                paramV["authKey"] = "abc"
                return paramV
            }
        }
        queue.add(stringRequest)
        pd.dismiss()
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
        builderData.addFormDataPart("name", "TEST TEST")// need to replace with name
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
            Log.e("Param : ", "$key: $value")
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
                            Toast.makeText(
                                this@GuruDakshinaOneTimeFourthActivity,
                                "success : $message",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(
                                " payment intent key ",
                                "payment intent key : " + response.body()!!.info.payment_intent_client_secret
                            )
                            paymentIntentClientSecret =
                                response.body()!!.info.payment_intent_client_secret
                            paymentID = response.body()!!.dakshina_pay_intent_id
                            Log.e(
                                "Secret ",
                                "paymentIntentClientSecret : " + paymentIntentClientSecret
                            )
                            makeStripePayement(paymentIntentClientSecret)
                        } else {
                            Functions.showAlertMessageWithOK(
                                this@GuruDakshinaOneTimeFourthActivity, "", message.toString()
                            )
                        }
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaOneTimeFourthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<StripeDataModel>, t: Throwable) {
                Toast.makeText(this@GuruDakshinaOneTimeFourthActivity, t.message, Toast.LENGTH_LONG)
                    .show()
                pd.dismiss()
            }
        })
    }

    private fun makeStripePayement(paymentIntentClientSecretKey: String) {
        Log.e("SecretKey ", "SecretKey : " + paymentIntentClientSecretKey)
        DebugLog.e("SecretKey : " + paymentIntentClientSecretKey)
//        val paymentIntentClientSecretKey_1 = paymentIntentClientSecretKey.replace("^\"|\"$", "");
//        cardInputWidget.paymentMethodCreateParams?.let { params ->
//            val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
//                params,
//                paymentIntentClientSecretKey
//            )
//            stripe.confirmPayment(this, confirmParams)
//        }
        cardInputWidget.paymentMethodCreateParams?.let { params ->
            val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                params,
                paymentIntentClientSecretKey
            )
            lifecycleScope.launch {
                paymentLauncher.confirm(confirmParams)
            }
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

        btnOk.setOnClickListener {
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
        }
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {
        val message = when (paymentResult) {
            is PaymentResult.Completed -> {
                "Completed!"
                paymentStatus = "Completed"
                paymentStatusReason = ""

            }

            is PaymentResult.Canceled -> {
                "Canceled!"
                paymentStatus = "Canceled"
                paymentStatusReason = "User canceled the payment"
            }

            is PaymentResult.Failed -> {
                // This string comes from the PaymentIntent's error message.
                // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
                "Failed: " + paymentResult.throwable.message
                paymentStatus = "Failed"
                paymentStatusReason = paymentResult.throwable.message.toString()
            }
        }
        Toast.makeText(
            this, "Payment Result:" + message, Toast.LENGTH_LONG
        ).show()

        Log.e("Payment Result: ", " Result : $message")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onError(e: Exception) {
                Log.e("OnError : ", " Error : $e")
                paymentStatus = "Error"
                paymentStatusReason = "Unknown Error"
                savePaymentDataintoDB(e.toString())
            }

            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                if (paymentIntent.status == StripeIntent.Status.Succeeded) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    Log.e("Succeeded : ", " Response : " + gson.toJson(paymentIntent))
                    paymentStatus = "Completed"
                    savePaymentDataintoDB(gson.toJson(paymentIntent))
                } else if (paymentIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
                    Log.e("Failed : ", " Response : " + paymentIntent.lastErrorMessage?.orEmpty())
                    paymentStatus = "Error"
                    paymentStatusReason = paymentIntent.lastErrorMessage?.orEmpty().toString()
                    savePaymentDataintoDB(paymentIntent.lastErrorMessage?.orEmpty().toString())
                }
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
            Log.e("Param : ", "$key: $value")
        }

        val call: Call<JsonObject> =
            MyHssApplication.instance!!.api.postSaveStripePaymentData(requestBody)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>, response: Response<JsonObject>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.isSuccessful) {

                        var resp = ""
                        if (response.body().toString().startsWith("{")) {
                            resp = "[" + response.body().toString() + "]"
                        }
                        DebugLog.e(resp)
                        val jsonObj = JSONArray(resp)
                        val jsonObject = jsonObj.getJSONObject(0)
                        val status = jsonObject.get("status")
                        val message = jsonObject.get("message")
                        if (status == true) {
                            Toast.makeText(
                                this@GuruDakshinaOneTimeFourthActivity,
                                "success : $message",
                                Toast.LENGTH_SHORT
                            ).show()

                            val i = Intent(
                                this@GuruDakshinaOneTimeFourthActivity,
                                HomeActivity::class.java
                            )
                            startActivity(i)
                            finishAffinity()
                        } else {
                            Functions.showAlertMessageWithOK(
                                this@GuruDakshinaOneTimeFourthActivity, "", message.toString()
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

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@GuruDakshinaOneTimeFourthActivity, t.message, Toast.LENGTH_LONG)
                    .show()
                pd1.dismiss()
            }
        })
    }
}

