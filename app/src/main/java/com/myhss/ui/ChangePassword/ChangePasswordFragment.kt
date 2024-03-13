package com.uk.myhss.ui.policies

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.myhss.Utils.UtilCommon
import com.myhss.ui.ChangePassword.Model.ChangePasswordResponse
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var rootLayout: RelativeLayout
    private lateinit var edit_old_password: TextInputEditText
    private lateinit var edit_new_password: TextInputEditText
    private lateinit var edit_confirm_password: TextInputEditText
    private lateinit var change_btn: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_password, container, false)
        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("ChangePasswordVC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "ChangePasswordVC",
            "ChangePasswordFragment"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        rootLayout = root.findViewById(R.id.rootLayout)
        edit_old_password = root.findViewById(R.id.edit_old_password)
        edit_new_password = root.findViewById(R.id.edit_new_password)
        edit_confirm_password = root.findViewById(R.id.edit_confirm_password)
        change_btn = root.findViewById(R.id.change_btn)

        change_btn.setOnClickListener(DebouncedClickListener {
            if (edit_old_password.text.toString().isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter Old Password", Snackbar.LENGTH_SHORT).show()
                edit_old_password.requestFocus()
                return@DebouncedClickListener
            } else if (edit_new_password.text.toString().isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter New Password", Snackbar.LENGTH_SHORT).show()
                edit_new_password.requestFocus()
                return@DebouncedClickListener
            } else if (!UtilCommon.isValidPassword(edit_new_password.text.toString())) {
                Snackbar.make(rootLayout, getString(R.string.valid_password), Snackbar.LENGTH_SHORT)
                    .show()
                edit_new_password.requestFocus()
                return@DebouncedClickListener
            } else if (edit_confirm_password.text.toString().isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter Confirm Password", Snackbar.LENGTH_SHORT)
                    .show()
                edit_confirm_password.requestFocus()
                return@DebouncedClickListener
            } else if (!UtilCommon.isValidPassword(edit_confirm_password.text.toString())) {
                Snackbar.make(
                    rootLayout,
                    getString(R.string.valid_confirm_password),
                    Snackbar.LENGTH_SHORT
                ).show()
                edit_confirm_password.requestFocus()
                return@DebouncedClickListener
            } else if (edit_new_password.text.toString() != edit_confirm_password.text.toString()) {
                Snackbar.make(
                    rootLayout,
                    getString(R.string.confirm_both_pass),
                    Snackbar.LENGTH_SHORT
                ).show()
            }else if (edit_new_password.text.toString() == edit_old_password.text.toString()) {
                Snackbar.make(
                    rootLayout,
                    getString(R.string.old_new_pass_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (Functions.isConnectingToInternet(requireContext())) {
                    ChangePassword()
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        return root
    }

    private fun ChangePassword() {
        change_btn.isEnabled = false
        val pd = CustomProgressBar(requireContext())
        pd.show()
        val call: Call<ChangePasswordResponse> =
            MyHssApplication.instance!!.api.userChangePassword(
                sessionManager.fetchUserID()!!,
                edit_old_password.text.toString(), edit_new_password.text.toString()
            )
        call.enqueue(object : Callback<ChangePasswordResponse> {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {

                if (response.code() == 200 && response.body() != null) {
                    if (response.body()?.status!!) {

                        val sharedPreferences = requireActivity().getSharedPreferences(
                            "production",
                            Context.MODE_PRIVATE
                        )
                        sessionManager.saveFIRSTNAME("")
                        sessionManager.saveSURNAME("")
                        sessionManager.saveUSERNAME("")
                        sessionManager.saveUserID("")
                        sessionManager.saveUSEREMAIL("")
                        sessionManager.saveUSERROLE("")
                        sessionManager.saveMEMBERID("")
                        sessionManager.saveSECURITYKEY("")
                        sessionManager.saveAuthToken("")
                        sessionManager.saveMIDDLENAME("")
                        sessionManager.saveSHAKHA_SANKHYA_AVG("")
                        sessionManager.saveSHAKHA_TAB("")
                        sessionManager.saveSHAKHANAME("")
                        sessionManager.savePOSTCODE("")
                        sessionManager.saveCOUNTRY("")
                        sessionManager.saveCITY("")
                        sessionManager.saveLineOne("")
                        sessionManager.saveGENDER("")
                        sessionManager.saveAGE("")
                        sessionManager.saveQUALIFICATIONAID("")
                        sessionManager.saveQUALIFICATION_VALUE("")
                        sessionManager.saveQUALIFICATION_VALUE_NAME("")
                        sessionManager.saveQUALIFICATION_PRO_BODY_RED_NO("")
                        sessionManager.saveQUALIFICATION_DATE("")
                        sessionManager.saveQUALIFICATION_FILE("")
                        sessionManager.saveQUALIFICATION_IS_DOC("")
                        sessionManager.saveDOB("")
                        sessionManager.saveVIBHAGNAME("")
                        sessionManager.saveSPOKKENLANGUAGE("")
                        sessionManager.saveMOBILENO("")
                        sessionManager.saveSECMOBILENO("")
                        sessionManager.saveOCCUPATIONNAME("")
                        sessionManager.saveADDRESS("")
                        sessionManager.saveGUAEMREMAIL("")
                        sessionManager.saveGUAEMRNAME("")
                        sessionManager.saveGUAEMRPHONE("")
                        sessionManager.saveGUAEMRRELATIONSHIP("")
                        sessionManager.saveSPOKKENLANGUAGEID("")
                        sessionManager.saveSPOKKENLANGUAGE("")
                        sessionManager.saveRELATIONSHIPNAME("")
                        sessionManager.saveRELATIONSHIPNAME_OTHER("")
                        sessionManager.saveNAGARID("")
                        sessionManager.saveDIETARY("")
                        sessionManager.saveDIETARYID("")
                        sessionManager.saveVIBHAGID("")
                        sessionManager.saveSTATE_IN_INDIA("")
                        sessionManager.saveSHAKHAID("")

                        sharedPreferences.edit().apply {
                            putString("FIRSTNAME", "")
                            putString("SURNAME", "")
                            putString("USERNAME", "")
                            putString("USERID", "")
                            putString("USEREMAIL", "")
                            putString("USERROLE", "")
                            putString("MEMBERID", "")
                            putString("SECURITYKEY", "")
                            putString("USERTOKEN", "")
                        }.apply()

                        val i = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(i)
                        (context as Activity).finishAffinity()

                    } else {
                        Functions.showAlertMessageWithOK(
                            requireContext(), "" + true,
                            response.body()?.message
                        )
                        change_btn.isEnabled = true
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        requireContext(), "Message",
                        getString(R.string.some_thing_wrong),
                    )
                    change_btn.isEnabled = true
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
                change_btn.isEnabled = true
            }
        })
    }
}