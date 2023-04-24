package com.androidvibes.mindbodytest.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomCard(itemName: String,
               countryCode: String = "",
               onCardClick: (countryCode: String) -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clickable {
            onCardClick(countryCode)
        }) {
        Row {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f), text = itemName
            )
        }

    }
}