package app.riopay

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.riopay.Paypal.ConfirmationActivity
import app.riopay.Paypal.Payment_activity
import kotlinx.android.synthetic.main.transcation.*
import kotlinx.android.synthetic.main.transcation.view.*

class Transcations:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.transcation,container,false)
        view.card_view1.setOnClickListener(View.OnClickListener {

            val i=Intent(activity,Trans_list::class.java)
            activity?.startActivity(i)
        })
        view.card_view2.setOnClickListener(View.OnClickListener {
            val i=Intent(activity, Trans_activity::class.java)
            activity?.startActivity(i)
        })
        view.card_view3.setOnClickListener(View.OnClickListener {
            val i=Intent(activity, Payment_activity::class.java)
            activity?.startActivity(i)
        })

        return view
    }
}