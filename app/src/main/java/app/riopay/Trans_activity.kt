package app.riopay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class Trans_activity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transactivity)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Transcation")
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item)
    }

}