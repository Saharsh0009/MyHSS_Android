package com.uk.myhss.Guru_Dakshina_Regular

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.Guru_Dakshina_OneTime.Model.Get_Onetime.Get_Create_Onetime
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.myhss.Utils.InputFilterMinMax
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class GuruDakshinaRegularFourthActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
    private var User_Id: String = ""
    private var Member_Id: String = ""
    private var Amount: String = ""
    private var Is_Linked_Member: String = ""
    private var Gift_aid: String = ""
    private var Is_prunima_Dashina: String = ""
    private var Line_one: String = ""
    private var City: String = ""
    private var Country: String = ""
    private var Post_Code: String = ""
    private var Dashina: String = ""
    private var CardNumber: String = ""
    private var CardNumberGenerate: String = ""
    private var CardHolderName: String = ""
    private var Card_Expriation_Date: String = ""
    private var Card_Cvv: String = ""
    private var Card_CvvGenerate: String = ""

    private var One_Digit: String = ""
    private var Two_Digit: String = ""
    private var Three_Digit: String = ""
    private var Four_Digit: String = ""
    private var Fifth_Digit: String = ""

    private lateinit var edit_card_number: TextInputEditText
    private lateinit var expriation_no_txt: TextInputEditText
    private lateinit var expriation_ccv_txt: TextInputEditText
    private lateinit var edit_card_holder: TextInputEditText
    private lateinit var card_number_valid: ImageView
    private lateinit var expriation_icon: ImageView
    private lateinit var expriation_ccv_img: ImageView
    private lateinit var rootLayout: LinearLayout
    private lateinit var expriation_no_view: LinearLayout
    private lateinit var expriation_ccv_view: LinearLayout
    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout
    private lateinit var edit_payment: ImageView
    private lateinit var donate_amount_txt: TextView

    var listOfPattern = ArrayList<String>()

    private var ptVisa: String = "^4[0-9]{6,}$"
    private var ptMasterCard: String = "^5[1-5][0-9]{5,}$"
    private var ptAmeExp: String = "^3[47][0-9]{5,}$"
    private var ptDinClb: String = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$"
    private var ptDiscover: String = "^6(?:011|5[0-9]{2})[0-9]{3,}$"
    private var ptJcb: String = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$"

    val start = 5
    val end = 9

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_fourth)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("GuruDakshinaaVC")
        sessionManager.firebaseAnalytics.setUserProperty("GuruDakshinaaVC", "GuruDakshinaRegularFourthActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.one_time_dakshina)

        rootLayout = findViewById(R.id.rootLayout)
        expriation_no_view = findViewById(R.id.expriation_no_view)
        expriation_ccv_view = findViewById(R.id.expriation_ccv_view)
        back_layout = findViewById(R.id.back_layout)
        next_layout = findViewById(R.id.next_layout)
        edit_card_holder = findViewById(R.id.edit_card_holder)
        edit_card_number = findViewById(R.id.edit_card_number)
        expriation_ccv_txt = findViewById(R.id.expriation_ccv_txt)
        expriation_no_txt = findViewById(R.id.expriation_no_txt)
        card_number_valid = findViewById(R.id.card_number_valid)
        expriation_icon = findViewById(R.id.expriation_icon)
        expriation_ccv_img = findViewById(R.id.expriation_ccv_img)
        edit_payment = findViewById(R.id.edit_payment)
        donate_amount_txt = findViewById(R.id.donate_amount_txt)

        back_arrow.setOnClickListener {
            finish()
        }

        edit_payment.setOnClickListener {
            depositDialog()
        }

        if (intent.getStringExtra("Amount") != "") {
            donate_amount_txt.text = intent.getStringExtra("Amount")
        }

        listOfPattern.add(ptVisa)
        listOfPattern.add(ptMasterCard)
        listOfPattern.add(ptAmeExp)
        listOfPattern.add(ptDinClb)
        listOfPattern.add(ptDiscover)
        listOfPattern.add(ptJcb)

        card_number_valid.visibility = View.INVISIBLE
        expriation_icon.visibility = View.INVISIBLE
        expriation_ccv_img.visibility = View.INVISIBLE

        edit_card_number.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                card_number_valid.visibility = View.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
                card_number_valid.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable) {
                val text: String = edit_card_number.getText().toString()
                val textLength: Int = edit_card_number.text!!.length
                if (text.endsWith(" ") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {
                    edit_card_number.setSelection(edit_card_number.text!!.length)
                } else if (textLength == 4) {
                    edit_card_number.setSelection(edit_card_number.text!!.length)
                } else if (textLength == 5) {
                    edit_card_number.setText(
                        StringBuilder(text).insert(text.length - 1, " ").toString()
                    )
                    edit_card_number.setSelection(edit_card_number.text!!.length)
                } else if (textLength == 9) {
                    edit_card_number.setSelection(edit_card_number.text!!.length)
                } else if (textLength == 10) {
                    edit_card_number.setText(
                        StringBuilder(text).insert(text.length - 1, " ").toString()
                    )
                    edit_card_number.setSelection(edit_card_number.text!!.length)
                } else if (textLength == 14) {
                    edit_card_number.setSelection(edit_card_number.text!!.length)
                } else if (textLength == 15) {
                    edit_card_number.setText(
                        StringBuilder(text).insert(text.length - 1, " ").toString()
                    )
                    edit_card_number.setSelection(edit_card_number.text!!.length)
                } else if (textLength == 19) {
                    edit_card_number.setSelection(edit_card_number.text!!.length)
                    card_number_valid.visibility = View.VISIBLE
                }
            }
        })

        expriation_no_txt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                expriation_icon.visibility = View.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
                expriation_icon.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable) {
                val text: String = expriation_no_txt.getText().toString()
                val textLength: Int = expriation_no_txt.text!!.length
                if (text.endsWith(" ") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {
                    expriation_no_txt.setSelection(expriation_no_txt.text!!.length)
                } else if (textLength == 3) {
                    expriation_no_txt.setText(
                        StringBuilder(text).insert(text.length - 1, "/").toString()
                    )
                    expriation_no_txt.setSelection(expriation_no_txt.text!!.length)
                } else if (textLength == 7) {
                    expriation_no_txt.setSelection(expriation_no_txt.text!!.length)
                    expriation_icon.visibility = View.VISIBLE
                }
            }
        })

        expriation_ccv_txt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                expriation_ccv_img.visibility = View.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
                expriation_ccv_img.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable) {
                val text: String = expriation_ccv_txt.getText().toString()
                val textLength: Int = expriation_ccv_txt.text!!.length
                if (text.endsWith(" ") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {
                    expriation_ccv_txt.setSelection(expriation_ccv_txt.text!!.length)
                } else if (textLength == 3) {
                    expriation_ccv_txt.setSelection(expriation_ccv_txt.text!!.length)
                    expriation_ccv_img.visibility = View.VISIBLE
                }
            }
        })

        createOneDigitNumber()
        createTwoDigitNumber()
        createThreeDigitNumber()
        createFourDigitNumber()
        createFiveDigitNumber()

        back_layout.setOnClickListener {
            finish()
        }

        next_layout.setOnClickListener {

            if (edit_card_number.text.toString().isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter Card Number", Snackbar.LENGTH_SHORT).show()
            } else if (expriation_no_txt.text.toString().isEmpty()){
                Snackbar.make(rootLayout, "Please Enter Expriation No.", Snackbar.LENGTH_SHORT).show()
            } else if (expriation_ccv_txt.text.toString().isEmpty()){
                Snackbar.make(rootLayout, "Please Enter CCV No.", Snackbar.LENGTH_SHORT).show()
            } else if (edit_card_holder.text.toString().isEmpty()){
                Snackbar.make(rootLayout, "Please Enter Card Holder Name", Snackbar.LENGTH_SHORT).show()
            } else {

                Log.d("CARD", edit_card_number.text.toString())

                val partCard = edit_card_number.text.toString().split(" ".toRegex()).toTypedArray()
                println("one: " + partCard[0])
                println("two: " + partCard[1])
                println("three: " + partCard[2])
                println("four: " + partCard[3])

                CardNumberGenerate =
                    Four_Digit + partCard[0] + Three_Digit + partCard[1] + partCard[2] + partCard[3] + Fifth_Digit
//            var res = edit_card_number.text.toString().replace("[^0-9]".toRegex(), "")   For Replace use
                Log.d("CardNumberGenerate", CardNumberGenerate)

                Card_CvvGenerate = Two_Digit + expriation_ccv_txt.text.toString() + One_Digit
                Log.d("Card_CvvGenerate", Card_CvvGenerate)

                if (Functions.isConnectingToInternet(this@GuruDakshinaRegularFourthActivity)) {
                    User_Id = sessionManager.fetchUserID()!!
                    Member_Id = sessionManager.fetchMEMBERID()!!
                    Amount = "100"
                    Is_Linked_Member = "Individual"
                    Gift_aid = "yes"
                    Is_prunima_Dashina = "1"
                    Line_one = sessionManager.fetchLineOne()!!
                    City = sessionManager.fetchCITY()!!
                    Country = sessionManager.fetchCOUNTRY()!!
                    Post_Code = sessionManager.fetchPOSTCODE()!!
                    Dashina = getString(R.string.one_time)
                    CardNumber =
                        CardNumberGenerate //"4568"+edit_card_number.text.toString()+"89562"
                    CardHolderName = edit_card_holder.text.toString()
                    Card_Expriation_Date = expriation_no_txt.text.toString()
                    Card_Cvv = Card_CvvGenerate  //"95"+expriation_ccv_txt.text.toString()+"5"

                    myDonate(
                        User_Id,
                        Member_Id,
                        Amount,
                        Is_Linked_Member,
                        Gift_aid,
                        Is_prunima_Dashina,
                        Line_one,
                        City,
                        Country,
                        Post_Code,
                        Dashina,
                        CardNumber,
                        CardHolderName,
                        Card_Expriation_Date,
                        Card_Cvv
                    )
                } else {
                    Toast.makeText(
                        this@GuruDakshinaRegularFourthActivity,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun depositDialog() {
        // Deposit Dialog
        if (dialog == null) {
            dialog = Dialog(this, R.style.StyleCommonDialog)
        }
        dialog?.setContentView(R.layout.edit_dialog_diposit_money)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

        val edit_amount = dialog!!.findViewById(R.id.edit_amount) as TextView
        val btnOk = dialog!!.findViewById(R.id.btnOk) as TextView

        donate_amount_txt.text = edit_amount.text.toString()

        btnOk.setOnClickListener {
            if (edit_amount.text.toString().isNotEmpty()) {
                edit_amount.filters = arrayOf<InputFilter>(
                    InputFilterMinMax(
                        "1",
                        "10000"
                    )
                )
                if (Integer.valueOf(edit_amount.text.toString()) > 0 && Integer.valueOf(edit_amount.text.toString()) <= 1000) {
                    dialog?.dismiss()
                } else {
                    Snackbar.make(rootLayout, "Please enter amount 1-10000", Snackbar.LENGTH_SHORT).show()
                }

            } else {
                Snackbar.make(rootLayout, "Please enter amount", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun createOneDigitNumber(): Int {
        var oneDigitNumber  = ""
        val rangeList = {(0..9).random()}

        while(oneDigitNumber.length < 1)
        {
            val num = rangeList().toString()
            if (!oneDigitNumber.contains(num)) oneDigitNumber +=num
        }
        Log.d("oneDigitNumber", oneDigitNumber)
        One_Digit = oneDigitNumber

        return oneDigitNumber.toInt()
    }

    fun createTwoDigitNumber(): Int {
        var twoDigitNumber  = ""
        val rangeList = {(0..9).random()}

        while(twoDigitNumber.length < 2)
        {
            val num = rangeList().toString()
            if (!twoDigitNumber.contains(num)) twoDigitNumber +=num
        }
        Log.d("twoDigitNumber", twoDigitNumber)
        Two_Digit = twoDigitNumber

        return twoDigitNumber.toInt()
    }

    fun createThreeDigitNumber(): Int {
        var threeDigitNumber  = ""
        val rangeList = {(0..9).random()}

        while(threeDigitNumber.length < 3)
        {
            val num = rangeList().toString()
            if (!threeDigitNumber.contains(num)) threeDigitNumber +=num
        }
        Log.d("threeDigitNumber", threeDigitNumber)
        Three_Digit = threeDigitNumber

        return threeDigitNumber.toInt()
    }

    fun createFourDigitNumber(): Int {
        var fourDigitNumber  = ""
        val rangeList = {(0..9).random()}

        while(fourDigitNumber.length < 4)
        {
            val num = rangeList().toString()
            if (!fourDigitNumber.contains(num)) fourDigitNumber +=num
        }
        Log.d("FourDigitNumber", fourDigitNumber)
        Four_Digit = fourDigitNumber

        return fourDigitNumber.toInt()
    }

    fun createFiveDigitNumber(): Int {
        var fiveDigitNumber  = ""
        val rangeList = {(0..9).random()}

        while(fiveDigitNumber.length < 5)
        {
            val num = rangeList().toString()
            if (!fiveDigitNumber.contains(num)) fiveDigitNumber +=num
        }
        Log.d("fiveDigitNumber", fiveDigitNumber)
        Fifth_Digit = fiveDigitNumber

        return fiveDigitNumber.toInt()
    }

    private fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (Math.random() * (end - start + 1)).toInt() + start
    }

    fun String.insert(index: Int, string: String): String {
        return this.substring(0, index) + string + this.substring(index, this.length)
    }

    /*Guru_Payment_Donate API*/
    private fun myDonate(
        user_id: String,
        member_id: String,
        amount: String,
        is_linked_member: String,
        gift_aid: String,
        is_purnima_dakshina: String,
        line1: String,
        city: String,
        country: String,
        postal_code: String,
        dakshina: String,
        card_number: String,
        name: String,
        card_expiry: String,
        card_cvv: String
    ) {
        val pd =
            CustomProgressBar(this@GuruDakshinaRegularFourthActivity)
        pd.show()
        val call: Call<Get_Create_Onetime> =
            MyHssApplication.instance!!.api.get_create_onetime(
                user_id,
                member_id,
                amount,
                is_linked_member,
                gift_aid,
                is_purnima_dakshina,
                line1,
                city,
                country,
                postal_code,
                dakshina,
                card_number,
                name,
                card_expiry,
                card_cvv
            )
        call.enqueue(object : Callback<Get_Create_Onetime> {
            override fun onResponse(
                call: Call<Get_Create_Onetime>,
                response: Response<Get_Create_Onetime>
            ) {

                Log.d("status", response.body()?.status.toString())
                if (response.body()?.status!!) {

                    val data_get = response.body()!!.data!![0]
                    Log.d("giftAid", data_get.giftAid.toString())
                    Log.d("orderId", data_get.orderId.toString())
                    Log.d("paidAmount", data_get.paidAmount.toString())
                    Log.d("status", data_get.status.toString())

                    val alertBuilder = AlertDialog.Builder(this@GuruDakshinaRegularFourthActivity) // , R.style.dialog_custom

                    alertBuilder.setTitle(getString(R.string.payment_method))
                    alertBuilder.setMessage(response.body()?.message)
                    alertBuilder.setPositiveButton(
                        "OK"
                    ) { dialog, which ->
                        val i =
                            Intent(this@GuruDakshinaRegularFourthActivity, GuruDakshinaRegularCompleteActivity::class.java)
                        i.putExtra("giftAid", data_get.giftAid.toString())
                        i.putExtra("orderId", data_get.orderId.toString())
                        i.putExtra("paidAmount", data_get.paidAmount.toString())
                        i.putExtra("status", data_get.status.toString())
                        startActivity(i)
                    }
                    val alertDialog = alertBuilder.create()
                    alertDialog.show()

                } else {
                    Functions.displayMessage(this@GuruDakshinaRegularFourthActivity,response.body()?.message)
//                    Functions.showAlertMessageWithOK(
//                        this@GuruDakshinaRegularFourthActivity, "",
////                        "Message",
//                        response.body()?.message
//                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Create_Onetime>, t: Throwable) {
                Toast.makeText(this@GuruDakshinaRegularFourthActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }
}