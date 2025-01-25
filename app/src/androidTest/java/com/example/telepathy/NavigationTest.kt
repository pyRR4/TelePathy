package com.example.telepathy.presentation.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.telepathy.BaseTestSetup
import org.junit.Rule
import org.junit.Test

class NavigationTest : BaseTestSetup() {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testNavigationToSettings() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "contactsscreen",
                currentScreen = mutableStateOf("contactsscreen")
            )
        }

        composeRule.onNodeWithTag("ContactsScreen").assertExists()

        composeRule.runOnIdle {
            navController?.navigate("settingsscreen")
        }

        composeRule.onNodeWithTag("SettingsScreen").assertExists()

    }

    @Test
    fun testNavigationToTalkScreen() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "contactsscreen",
                currentScreen = mutableStateOf("contactsscreen")
            )
        }

        composeRule.runOnIdle {
            navController?.navigate("talkscreen/1")
        }

        composeRule.onNodeWithTag("TalkScreen").assertExists()
    }

    @Test
    fun testNavigationToEditScreen() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "settingsscreen",
                currentScreen = mutableStateOf("settingsscreen")
            )
        }

        composeRule.runOnIdle {
            navController?.navigate("edit_profile")
        }

        composeRule.onNodeWithTag("EditScreen").assertExists()
    }

    @Test
    fun testNavigationToVideoScreen() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "settingsscreen",
                currentScreen = mutableStateOf("settingsscreen")
            )
        }

        composeRule.runOnIdle {
            navController?.navigate("videoPlayerScreen")
        }

        composeRule.onNodeWithTag("VideoScreen").assertExists()
    }
}
