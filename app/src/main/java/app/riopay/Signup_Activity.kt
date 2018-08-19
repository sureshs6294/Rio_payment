package app.riopay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sign_up.*
import android.content.Intent
import android.widget.Toast


class Signup_Activity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    var get_mail: String? = null
    var get_password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.sign_up)
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
        sign_up_button.setOnClickListener(View.OnClickListener {
            if (valid()) {
                auth!!.createUserWithEmailAndPassword(get_mail.toString(), get_password.toString())
                        .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                            Toast.makeText(this, "createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_SHORT).show()
                            if (!task.isSuccessful) {
                                Toast.makeText(this, "Authentication failed." + task.exception!!,
                                        Toast.LENGTH_SHORT).show()
                            } else {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        })
            }
        })
    }

    private fun valid(): Boolean {
        var valid = true
        val emails = email.text.toString()
        val pinno = password.text.toString()
        if (emails.isEmpty() || !isEmailValid(emails)) {
            email.setError("Enter proper Email")
            valid = false
        } else {
            get_mail = email.text.toString()
        }
        if (pinno.isEmpty()) {
            password.setError("Enter proper password")
            valid = false
        } else {
            get_password = password.text.toString()
        }
        return valid
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }


}