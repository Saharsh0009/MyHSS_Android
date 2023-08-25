package com.myhss.ui.policies

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager

class WebViewPolicies : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private var isLoaded: Boolean = false
    private var webURL = ""
    private lateinit var webView: WebView
    private lateinit var rootView: LinearLayout
    private lateinit var pd: CustomProgressBar

    private var CODE_CONTENT: String = MyHssApplication.BaseURL + "page/code-of-conduct/6"
    private var DATA_PROTECTION: String = MyHssApplication.BaseURL + "page/data-protection-policy/4"
    private var MEMBERSHIP: String = MyHssApplication.BaseURL + "page/hss-uk-membership-agreement/7"
    private var TERMS_CONDITION: String = MyHssApplication.BaseURL + "page/myhss-terms-conditions/2"
    private var PRIVACY_POLICY: String = MyHssApplication.BaseURL + "page/privacy-policy/1"

    @SuppressLint("SetJavaScriptEnabled", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_policies)

        sessionManager = SessionManager(this)
        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("WebViewPoliciesVC")
        sessionManager.firebaseAnalytics.setUserProperty("WebViewPoliciesVC", "WebViewPolicies")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        if (intent.getStringExtra("CODECONTENT") == "Code of Conduct") {
            header_title.text = "Code of Conduct"
            webURL = CODE_CONTENT
        } else if (intent.getStringExtra("DATAPROTECTION") == "Data Protection Policy") {
            header_title.text = "Data Protection Policy"
            webURL = DATA_PROTECTION
        } else if (intent.getStringExtra("MEMBERSHIP_A") == "Membership Agreement") {
            header_title.text = "Membership Agreement"
            webURL = MEMBERSHIP
        } else if (intent.getStringExtra("TERMSCONDITION") == "Terms & Conditions") {
            header_title.text = "Terms & Conditions"
            webURL = TERMS_CONDITION
        } else if (intent.getStringExtra("PRIVACYPOLICY") == "Privacy Policy") {
            header_title.text = "Privacy Policy"
            webURL = PRIVACY_POLICY
        }

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        webView = findViewById(R.id.webView)
        rootView = findViewById(R.id.rootView)

        webView.settings.javaScriptEnabled = true

        if (Functions.isConnectingToInternet(this@WebViewPolicies)) {
            loadWebView()
            return
        } else {
            Toast.makeText(
                this@WebViewPolicies,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        if (Functions.isConnectingToInternet(this@WebViewPolicies) && !isLoaded) loadWebView()
        super.onResume()
    }

    private fun loadWebView() {
        pd = CustomProgressBar(this@WebViewPolicies)
//        pd.show()
        webView.loadUrl(webURL)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                pd.show()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                isLoaded = true
                pd.dismiss()
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                isLoaded = false
                val errorMessage = "Got Error! $error"
                pd.dismiss()
                super.onReceivedError(view, request, error)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (this@WebViewPolicies != null && !this@WebViewPolicies.isFinishing && pd != null && pd.isShowing) {
            pd.cancel()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}