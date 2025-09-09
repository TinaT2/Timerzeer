package com.tina.timerzeer.repository

import android.content.Intent
import androidx.core.content.ContextCompat
import com.tina.timerzeer.app.TimezeerApplication
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.timer.data.repository.TimerRepository
import com.tina.timerzeer.timer.presentation.fullScreenTimer.TimerIntent
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TimerRepositoryTest {

    private val application: TimezeerApplication = mockk(relaxed = true)
    private var repository: TimerRepository = TimerRepository(application)

    @BeforeEach
    fun setup() {
        mockkStatic(ContextCompat::class)
        every { ContextCompat.startForegroundService(any(), any()) } returns mockk()

        mockkConstructor(Intent::class)
        every { anyConstructed<Intent>().setAction(any()) } returns mockk(relaxed = true)
        repository = TimerRepository(application)
    }

    @Test
    fun `init calls startService`() {
        TimerRepository(application)
        verify { ContextCompat.startForegroundService(application, any()) }
    }

    @Test
    fun `update sets elapsedTime`() = runTest {
        repository.update(50L)
        assertEquals(50L, repository.timerState.first().elapsedTime)
    }

    @Test
    fun `update countdown mode with 0 triggers Stop`() = runTest {
        repository.update(TimerMode.COUNTDOWN, "Test", 10L)
        repository.update(0L)
        val state = repository.timerState.first()
        assertTrue(state.isCountDownDone)
        assertEquals(0L, state.elapsedTime)
        assertFalse(state.isRunning) // because Stop was triggered
    }

    @Test
    fun `update with mode, title, initialTime updates state`() = runTest {
        repository.update(TimerMode.STOPWATCH, "My Timer", 123L)
        val state = repository.timerState.first()
        assertEquals("My Timer", state.title)
        assertEquals(123L, state.initialTime)
        assertEquals(TimerMode.STOPWATCH, state.mode)
    }

    @Test
    fun `reset sets elapsedTime to 0`() = runTest {
        repository.update(42L)
        repository.reset()
        assertEquals(0L, repository.timerState.first().elapsedTime)
    }

    @Test
    fun `onTimerIntent Start does nothing if already running`() = runTest {
        repository.update(TimerMode.STOPWATCH, "t", 100L)
        repository.onTimerIntent(TimerIntent.Start)
        val before = repository.timerState.first()

        repository.onTimerIntent(TimerIntent.Start) // again
        val after = repository.timerState.first()

        assertEquals(before, after) // unchanged
    }

    @Test
    fun `onTimerIntent Pause sets running false`() = runTest {
        repository.onTimerIntent(TimerIntent.Pause)
        val state = repository.timerState.first()
        assertFalse(state.isRunning)
        verify { application.startService(any<Intent>()) }
    }

    @Test
    fun `onTimerIntent Resume sets running true only if not running`() = runTest {
        repository.onTimerIntent(TimerIntent.Resume)
        assertTrue(repository.timerState.first().isRunning)
        verify { ContextCompat.startForegroundService(application, any()) }
    }

    @Test
    fun `onTimerIntent Stop sets running false`() = runTest {
        repository.onTimerIntent(TimerIntent.Stop)
        val state = repository.timerState.first()
        assertFalse(state.isRunning)
        verify { application.startService(any<Intent>()) }
    }

    @Test
    fun `onTimerIntent null does nothing`() = runTest {
        val before = repository.timerState.first()
        repository.onTimerIntent(null)
        val after = repository.timerState.first()
        assertEquals(before, after)
    }

    @Test
    fun `Start sets elapsedTime to initialTime for countdown`() = runTest {
        val initialTime = 5000L

        // Set initial countdown state
        repository.update(TimerMode.COUNTDOWN, title = "Countdown", initialTime = initialTime)

        // Trigger Start
        repository.onTimerIntent(TimerIntent.Start)

        val state = repository.timerState.first()
        assertTrue(state.isRunning)
        assertEquals(initialTime, state.elapsedTime)
        assertFalse(state.isCountDownDone)

        // Verify service started
        verify { application.startService(any<Intent>()) }
    }

    @Test
    fun `Start sets elapsedTime to 0 for stopwatch`() = runTest {
        // Set initial stopwatch state with initialTime (should be ignored)
        repository.update(TimerMode.STOPWATCH, title = "Stopwatch", initialTime = 1000L)

        // Trigger Start
        repository.onTimerIntent(TimerIntent.Start)

        val state = repository.timerState.first()
        assertTrue(state.isRunning)
        assertEquals(0L, state.elapsedTime)
        assertFalse(state.isCountDownDone)

        // Verify service started
        verify { application.startService(any<Intent>()) }
    }
}
