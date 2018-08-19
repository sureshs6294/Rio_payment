package app.riopay


import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.net.ConnectivityManager
import android.view.Window
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    var get_mbno: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        if (netInfo == null) run {
            toast("No internet connection")
        }
        else {
            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance()
            // auth!!.signOut()
            if (auth!!.getCurrentUser() != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            btn_login.setOnClickListener(View.OnClickListener {
                if (valid()) {
                    val intent = Intent(this, Phone_no_verification::class.java)
                    intent.putExtra("mobile", get_mbno)
                    startActivity(intent)
                }
            })
        }
    }
    private fun valid(): Boolean {
        var valid = true
        val no = login_no.text.toString()
        if (no.isEmpty() || no.length<10) {
            login_no.setError("Enter valid phone no")
            valid = false
        } else {
            get_mbno = login_no.text.toString()
        }

        return valid
    }
}
