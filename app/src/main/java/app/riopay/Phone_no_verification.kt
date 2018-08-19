package app.riopay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.phone_verification.*
import java.util.concurrent.TimeUnit
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import org.jetbrains.anko.toast
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.support.design.widget.Snackbar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser






class Phone_no_verification : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    private var mVerificationId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone_verification)
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        val intent = intent
        val mobile = intent.getStringExtra("mobile")
        sendVerificationCode(mobile)

        buttonSignIn.setOnClickListener(View.OnClickListener {
            var code = editTextCode.text.toString()
            if (code.isEmpty() || code.length < 6) {
                editTextCode.setError("Enter valid code");
                editTextCode.requestFocus();
                return@OnClickListener
            }

            //verifying the code entered manually
            verifyVerificationCode(code);
        })
    }

    private fun sendVerificationCode(mobile: String?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            //Getting the code sent by SMS
            val code = phoneAuthCredential.smsCode

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code)
                //verifying the code
                verifyVerificationCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            toast(">"+e.message)
        }

        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)

            //storing the verification id that is sent to the user
            mVerificationId = s
        }
    }

    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId.toString(), code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth!!.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val user = task.result.user
                        //verification successful we will start the profile activity
                        val intent = Intent(this, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)

                    } else {
                        //verification unsuccessful.. display an error message

                        var message = "Somthing is wrong, we will fix it soon..."

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered..."
                            toast(message)
                        }

                    }
                })
    }

}