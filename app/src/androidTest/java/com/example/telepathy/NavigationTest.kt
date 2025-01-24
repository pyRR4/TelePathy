package com.example.telepathy.presentation.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
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
    fun testNavigationToContactsScreen() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "availablescreen",
                currentScreen = mutableStateOf("availablescreen")
            )
        }

        composeRule.onNodeWithText("Available Around").assertExists()

        composeRule.runOnIdle {
            navController?.navigate("contactsscreen")
        }

        composeRule.onNodeWithText("Contacts Screen").assertExists()
    }

    @Test
    fun testNavigationToTalkScreen() {
        var navController: NavHostController? = null

        composeRule.setContent {
            navController = rememberNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "availablescreen",
                currentScreen = mutableStateOf("availablescreen")
            )
        }

        composeRule.runOnIdle {
            navController?.navigate("talkscreen/1")
        }

        composeRule.onNodeWithText("Talk with User 1").assertExists()
    }
}
