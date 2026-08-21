package com.dfgcontroller.ui

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.dfgcontroller.MainActivity
import org.junit.Rule
import org.junit.Test

class DashboardTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testProfileSwitch() {
        // This test assumes root is available or mocked, and app starts at Dashboard
        composeTestRule.onNodeWithText("Performance").performClick()
        
        // Check if "Applying saved profile..." card appears or other UI changes
        // composeTestRule.onNodeWithText("Applying saved profile...").assertIsDisplayed()
    }
}
