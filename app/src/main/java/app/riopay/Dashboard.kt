package app.riopay

import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dashboard.view.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class Dashboard : Fragment() {
    private var auth: FirebaseAuth? = null
    var no:String?=null
    var id:String?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dashboard,container,false)
        val conMgr = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
                no = user.phoneNumber
                id = user.uid
                view.tvNumber1.text=id
                view.tvNumber2.text=no

            }
        }
        return view
    }
}