package com.xeniac.jalalidatepickerdemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.xeniac.jalalidatepicker.JalaliDatePicker
import com.xeniac.jalalidatepicker.models.SelectedJalaliDate
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
                    CompositionLocalProvider(
                        LocalLayoutDirection provides LayoutDirection.Ltr
                    ) {
                        JalaliDatePickerDemo(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JalaliDatePickerDemo(
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 24.dp)
) {
    var selectedJalaliDate by remember {
        mutableStateOf(
            SelectedJalaliDate(
                year = 0,
                month = 0,
                day = 0
            )
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 40.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier.padding(contentPaddingValues)
    ) {
        JalaliDatePicker(
            onSelectedDateChange = {
                selectedJalaliDate = it
            },
            isSelectFromFutureEnabled = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = with(selectedJalaliDate) {
                "$year/$month/$day"
            },
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JalaliDatePickerDemoPreview() {
    JalaliDatePickerTheme {
        JalaliDatePickerDemo()
    }
}