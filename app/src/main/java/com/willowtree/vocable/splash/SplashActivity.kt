package com.willowtree.vocable.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.willowtree.vocable.MainActivity
import com.willowtree.vocable.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(
            this,
            SplashViewModelFactory(
                getString(R.string.category_123_id),
                getString(R.string.category_my_sayings_id)
            )
        ).get(SplashViewModel::class.java)

        viewModel.exitSplash.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}