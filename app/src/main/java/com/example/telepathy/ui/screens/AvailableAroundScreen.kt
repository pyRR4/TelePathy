package com.example.telepathy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.telepathy.ui.ScreenTemplate
import com.example.telepathy.R
import com.example.telepathy.clases.User
import com.example.telepathy.ui.CustomButton
import com.example.telepathy.ui.DividerWithImage
import com.example.telepathy.ui.Header


@Composable
fun AvailableAroundScreen(availableContacts: List<User>) {

    ScreenTemplate(
        navIcon = {
            DividerWithImage()
        },
        header = {
            Header(stringResource(R.string.available_around_you), modifier = Modifier.padding(bottom = 16.dp))
        },
        modifier = Modifier
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(availableContacts.size) { index ->
                val contact = availableContacts[index]
                CustomButton (
                    name = contact.name,
                    image = {
                        ButtonIcon(
                            image = painterResource(R.drawable.test1),
                            modifier = Modifier
                        )
                    },
                    backgroundColor = contact.color,
                    onClick = { /* Handle click */ }
                )
            }
        }
    }
}

