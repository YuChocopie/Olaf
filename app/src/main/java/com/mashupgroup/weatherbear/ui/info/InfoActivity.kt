package com.mashupgroup.weatherbear.ui.info

import android.os.Bundle
import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.mashupgroup.weatherbear.R
import kotlinx.android.synthetic.main.activity_info.*


class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        tvVersion.text = packageManager.getPackageInfo(packageName, 0).versionName

        tvOpenAppInfo.setOnClickListener {
            try {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val intent = Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                startActivity(intent)
            }
        }
    }
}
