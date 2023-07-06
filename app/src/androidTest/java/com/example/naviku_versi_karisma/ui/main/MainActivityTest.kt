package com.example.naviku_versi_karisma.ui.main

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MainActivityTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var accessibilityManager: AccessibilityManager

    @Mock
    private lateinit var tts: TextToSpeech

    private val welcomeText = "Welcome!"

    private lateinit var testClass: TestClass

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testClass = TestClass(context, accessibilityManager, tts)
    }

    @Test
    fun testSpeakWelcomeMessage_WithAccessibilityEnabled() {
        Mockito.`when`(accessibilityManager.isEnabled).thenReturn(true)

        testClass.speakWelcomeMessage()

        Mockito.verify(accessibilityManager).isEnabled
        Mockito.verify(accessibilityManager).sendAccessibilityEvent(Mockito.any())
        Mockito.verify(tts, Mockito.never()).speak(Mockito.anyString(), Mockito.anyInt(), Mockito.any(), Mockito.any())
    }

    @Test
    fun testSpeakWelcomeMessage_WithAccessibilityDisabled() {
        Mockito.`when`(accessibilityManager.isEnabled).thenReturn(false)

        testClass.speakWelcomeMessage()

        Mockito.verify(accessibilityManager).isEnabled
        Mockito.verify(accessibilityManager, Mockito.never()).sendAccessibilityEvent(Mockito.any())
        Mockito.verify(tts).speak(welcomeText, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private inner class TestClass(
        private val context: Context,
        private val accessibilityManager: AccessibilityManager,
        private val tts: TextToSpeech
    ) {
        fun speakWelcomeMessage() {
            if (accessibilityManager.isEnabled) {
                val event = AccessibilityEvent.obtain()
                event.eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
                event.text.add(welcomeText)
                accessibilityManager.sendAccessibilityEvent(event)
            } else {
                tts.speak(welcomeText, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }
}