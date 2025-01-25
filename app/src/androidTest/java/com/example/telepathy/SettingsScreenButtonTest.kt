package com.example.telepathy.presentation.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.telepathy.BaseTestSetup
import com.example.telepathy.R
import com.example.telepathy.presentation.navigation.AnimatedNavHost
import com.example.telepathy.presentation.viewmodels.SharedViewModel
import com.example.telepathy.presentation.viewmodels.GenericViewModelFactory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenButtonTest: BaseTestSetup() {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testEditProfileButtonClickNavigatesToEditProfile() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "settingsscreen",
                currentScreen = mutableStateOf("settingsscreen")
            )
        }

        composeRule.onNodeWithText("Edit Profile").performClick()

        composeRule.onNodeWithTag("EditScreen").assertExists()
    }

    @Test
    fun testFilmButtonClickNavigatesToFilm() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "settingsscreen",
                currentScreen = mutableStateOf("settingsscreen")
            )
        }

        composeRule.onNodeWithText("Lunch secret film").performClick()

        composeRule.onNodeWithTag("VideoScreen").assertExists()
    }

    @Test
    fun testSetPinButtonClickNavigatesToSetNewPin() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "settingsscreen",
                currentScreen = mutableStateOf("settingsscreen")
            )
        }

        composeRule.onNodeWithText("Set PIN").performClick()

        composeRule.onNodeWithTag("PinScreen").assertExists()
    }

}
