package app.riopay

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import app.riopay.Model.User
import android.text.TextUtils
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener



class MainActivity:AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private val TAG_FRAGMENT = "MainActivity"
    var mSelectedItem = 0
    var no:String?=null
    var id:String?=null
    var userId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        if (netInfo == null) run {
            toast("No internet connection")
        }
        else {
            auth = FirebaseAuth.getInstance()
            val user = auth!!.getCurrentUser();
            if (user != null) {
                Log.d("no", ">" + user.phoneNumber)
                Log.d("id", ">" + user.uid)
                no=user.phoneNumber
                id=user.uid
            }
            mFirebaseInstance = FirebaseDatabase.getInstance()
            mFirebaseDatabase = mFirebaseInstance!!.getReference("users")
            createUser(no.toString(), id.toString())
            loadFragment(Dashboard())
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

    }

    private fun createUser(name: String, email: String) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase!!.push().key
        }

        val user = User(name, email)
        mFirebaseDatabase!!.child(userId.toString()).setValue(user)

        addUserChangeListener()
    }

    private fun addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase!!.child(userId.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                // Check for null
                if (user == null) {
                    Log.e(TAG_FRAGMENT, "User data is null!")
                    return
                }
                Log.e(TAG_FRAGMENT, "User data is changed!" + user.name + ", " + user.email)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(TAG_FRAGMENT, "Failed to read user", error.toException())
            }
        })
    }
    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(TAG_FRAGMENT)
        transaction.commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment
        when (item.itemId) {

            R.id.navigation_dashboard -> {
                fragment = Dashboard()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_trans -> {
                fragment = Transcations()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                fragment = Profile()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        mSelectedItem = item.itemId
        false
    }

    override fun onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish()
        } else {
            getFragmentManager().popBackStack()
        }
    }

}