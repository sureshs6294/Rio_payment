package app.riopay.Paypal

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import app.riopay.R
import kotlinx.android.synthetic.main.pay_confirmation.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.json.JSONException
import org.json.JSONObject

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_confirmation)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Payment Confirmation")
        val intent = intent

        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(12))
                .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
/*
        if(intent!=null) {
            try {
                val jsonDetails = JSONObject(intent.getStringExtra("PaymentDetails"))
                //Displaying payment details
                showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"))
            } catch (e: JSONException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
*/
    }

    @Throws(JSONException::class)
    private fun showDetails(jsonDetails: JSONObject, paymentAmounts: String) {
        //Showing the details from json object
        paymentId .text=(jsonDetails.getString("id"))
        paymentStatus.text = jsonDetails.getString("state")
        paymentAmount.text= "$paymentAmount USD"
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item)
    }

}