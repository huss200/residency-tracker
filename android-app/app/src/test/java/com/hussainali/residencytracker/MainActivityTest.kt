package com.hussainali.residencytracker

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @Test
    fun mainActivity_configuresSecureWebViewAndBridge() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        assertTrue(activity.webView.settings.javaScriptEnabled)
        assertFalse(activity.webView.settings.allowFileAccess)
        assertFalse(activity.webView.settings.allowContentAccess)
    }
}
