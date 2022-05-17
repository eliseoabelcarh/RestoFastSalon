package com.restofast.salon.cashier.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.restofast.salon.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityCashier : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier)
    }
}