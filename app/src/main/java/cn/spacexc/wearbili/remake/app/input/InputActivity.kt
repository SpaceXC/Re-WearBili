package cn.spacexc.wearbili.remake.app.input

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.delay

const val PARAM_PREV_INPUT = "prevInput"
const val PARAM_INPUT = "prevInput"

class InputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prevInput = intent.getStringExtra(PARAM_PREV_INPUT) ?: ""

        setContent {
            val focusRequester = remember { FocusRequester() }
            val softKeyboard = LocalSoftwareKeyboardController.current

            val state = rememberTextFieldState(prevInput)

            LaunchedEffect(Unit) {
                delay(100) //延迟操作(关键点)
                focusRequester.requestFocus()
                softKeyboard?.show()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(10.dp)
            ) {
                BasicTextField(
                    state = state,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = wearbiliFontFamily,
                        color = Color.White
                    ),
                    cursorBrush = SolidColor(value = BilibiliPink),
                    onKeyboardAction = {
                        setResult(0, Intent().apply {
                            putExtra(PARAM_INPUT, state.text)
                        })
                        finish()
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .weight(1f)
                )
                Card(onClick = {
                    setResult(0, Intent().apply {
                        putExtra(PARAM_INPUT, state.text)
                    })
                    finish()
                }) {
                    Text(text = "OK")
                }
            }
        }
    }
}