package app.riopay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class Trans_list:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.translist)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Transcation List")
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item)
    }

}