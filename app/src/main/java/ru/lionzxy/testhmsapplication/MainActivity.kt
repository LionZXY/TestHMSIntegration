package ru.lionzxy.testhmsapplication

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG = "PushDemoLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (isHmsAvailable(this)) {
            requestTokenHMS()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestTokenHMS() {
        Thread(
            Runnable {
                val startTime = System.currentTimeMillis()
                val appId = AGConnectServicesConfig.fromContext(this).getString("client/app_id")
                val pushtoken = HmsInstanceId.getInstance(this).getToken(appId, "HCM");
                showToken(pushtoken, System.currentTimeMillis() - startTime)
            }).start()
    }

    private fun showToken(pushtoken: String, diff: Long) {
        runOnUiThread {
            Toast.makeText(
                this,
                "Push-token: $pushtoken. Time: $diff",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    fun isHmsAvailable(context: Context): Boolean {
        val result =
            HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context)
        return ConnectionResult.SUCCESS == result
    }
}