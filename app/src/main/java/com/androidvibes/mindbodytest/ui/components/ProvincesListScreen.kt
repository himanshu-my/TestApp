package com.androidvibes.mindbodytest.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidvibes.mindbodytest.data.models.Provinces

@Composable
fun ProvincesListContent(provincesList: List<Provinces>) {
    Column {
        LazyColumn(
            Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        ) {
            itemsIndexed(
                items = provincesList,
                itemContent = { _, item ->
                    CustomCard(itemName = item.name,
                        onCardClick = {})
                })
        }
    }
}