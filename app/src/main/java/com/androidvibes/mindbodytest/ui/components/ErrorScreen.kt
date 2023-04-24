package com.androidvibes.mindbodytest.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidvibes.mindbodytest.R
import com.androidvibes.mindbodytest.utils.color_Red1
import com.androidvibes.mindbodytest.utils.textStyle_Grey5_Sz_18sp
import com.androidvibes.mindbodytest.utils.textStyle_Grey5_Sz_22sp
import com.androidvibes.mindbodytest.utils.textStyle_White_Sz_16sp

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ErrorScreen(
        modifier = Modifier.fillMaxSize(),
        errorIc = R.drawable.ic_error_network,
        errorMessage = stringResource(R.string.no_internet_message_text),
        onRetryClick = {},
    )
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    @DrawableRes errorIc: Int,
    errorMessage: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(errorIc),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.oops_text),
            style = textStyle_Grey5_Sz_22sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = errorMessage,
            style = textStyle_Grey5_Sz_18sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = { onRetryClick() },
            modifier = Modifier
                .width(114.dp)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = color_Red1),
            shape = RoundedCornerShape(57.dp),
        ) {
            Text(
                text = stringResource(R.string.error_view_retry),
                textAlign = TextAlign.Center,
                style = textStyle_White_Sz_16sp,
            )
        }
    }
}