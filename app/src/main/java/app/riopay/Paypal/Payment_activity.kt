package app.riopay.Paypal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import app.riopay.R
import kotlinx.android.synthetic.main.payment.*
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import android.content.Intent
import java.math.BigDecimal
import com.paypal.android.sdk.i
import android.app.Activity
import com.paypal.android.sdk.e
import org.json.JSONException
import android.R.attr.data
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import org.jetbrains.anko.toast


class Payment_activity : AppCompatActivity() {
    val PAYPAL_REQUEST_CODE = 123
    var paymentAmount: String? = null

    //Paypal Configuration Object
    companion object {
        private val config = PayPalConfiguration()
                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(Paypal_config.PAYPAL_CLIENT_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        if (netInfo == null) run {
            toast("No internet connection")
        }
        else {
            btn_pay.setOnClickListener(View.OnClickListener {
                getPayment()
            })

            val intent = Intent(this, PayPalService::class.java)

            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

            startService(intent)
        }
    }

    override fun onDestroy() {
        stopService(Intent(this, PayPalService::class.java))
        super.onDestroy()
    }

    private fun getPayment() {
        //Getting the amount from editText
        paymentAmount = editTextAmount.getText().toString()

        //Creating a paypalpayment
        val payment = PayPalPayment(BigDecimal(paymentAmount), "USD", "Transfer amount",
                PayPalPayment.PAYMENT_INTENT_SALE)

        //Creating Paypal Payment activity intent
        val intent = Intent(this, PaymentActivity::class.java)

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       //If the result is from paypal
        if (requestCode === PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode === Activity.RESULT_OK) {
                //Getting the payment confirmation
                val confirm = data?.getParcelableArrayExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        val paymentDetails = confirm.toString()
                        Log.i("paymentExample", paymentDetails)

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(Intent(this, ConfirmationActivity::class.java)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount))

                    } catch (e: JSONException) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e)
                    }

                }
            } else if (resultCode === Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.")
            } else if (resultCode === PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item)
    }

}