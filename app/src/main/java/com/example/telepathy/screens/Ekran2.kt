package com.example.telepathy.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Ekran2(onNavigateToEkran1: () -> Unit) {
    Scaffold { contentPadding ->  // Użyj contentPadding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding) // Zastosowanie contentPadding
                .padding(16.dp)
        ) {
            Text("Ekran 2", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToEkran1) {
                Text("Powrót do Ekranu 1")
            }
        }
    }
}
