package com.xeniac.jalalidatepickerdemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.xeniac.jalalidatepicker.JalaliDatePicker
import com.xeniac.jalalidatepickerdemo.ui.theme.JalaliDatePickerTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JalaliDatePickerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    JalaliDatePicker(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}