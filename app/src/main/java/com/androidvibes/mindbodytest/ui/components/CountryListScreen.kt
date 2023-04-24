package com.androidvibes.mindbodytest.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidvibes.mindbodytest.data.models.Country

@Composable
fun CountryListContent(
    countryList: List<Country>,
    onItemClick: (countryCode: String) -> Unit
) {
    Column {
        LazyColumn(
            Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        ) {
            itemsIndexed(
                items = countryList,
                itemContent = { _, item ->
                    CustomCard(itemName = item.name,
                        countryCode = item.code,
                        onCardClick = {
                            onItemClick(it)
                        })
                })
        }
    }
}