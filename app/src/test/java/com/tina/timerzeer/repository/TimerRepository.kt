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

    @BeforeEach
    fun setup() {
        mockkStatic(ContextCompat::class)
        every { ContextCompat.startForegroundService(any(), any()) } returns mockk()

        mockkConstructor(Intent::class)
        every { anyConstructed<Intent>().setAction(any()) } returns mockk(relaxed = true)
    }

    @Test
    fun `init calls startService`() {
        TimerRepository(application)
        verify { ContextCompat.startForegroundService(application, any()) }
    }

    @Test
    fun `update sets elapsedTime`() = runTest {
        val repo = TimerRepository(application)
        repo.update(50L)
        assertEquals(50L, repo.timerState.first().elapsedTime)
    }

    @Test
    fun `update countdown mode with 0 triggers Stop`() = runTest {
        val repo = TimerRepository(application)
        repo.update(TimerMode.COUNTDOWN, "Test", 10L)
        repo.update(0L)
        val state = repo.timerState.first()
        assertTrue(state.isCountDownDone)
        assertEquals(0L, state.elapsedTime)
        assertFalse(state.isRunning) // because Stop was triggered
    }

    @Test
    fun `update with mode, title, initialTime updates state`() = runTest {
        val repo = TimerRepository(application)
        repo.update(TimerMode.STOPWATCH, "My Timer", 123L)
        val state = repo.timerState.first()
        assertEquals("My Timer", state.title)
        assertEquals(123L, state.initialTime)
        assertEquals(TimerMode.STOPWATCH, state.mode)
    }

    @Test
    fun `reset sets elapsedTime to 0`() = runTest {
        val repo = TimerRepository(application)
        repo.update(42L)
        repo.reset()
        assertEquals(0L, repo.timerState.first().elapsedTime)
    }

    @Test
    fun `onTimerIntent Start sets running true and calls startService`() = runTest {
        val repo = TimerRepository(application)
        repo.update(TimerMode.STOPWATCH, "t", 100L)
        repo.onTimerIntent(TimerIntent.Start)

        val state = repo.timerState.first()
        assertTrue(state.isRunning)
        assertEquals(100L, state.elapsedTime)
        verify { application.startService(any<Intent>()) }
    }

    @Test
    fun `onTimerIntent Start does nothing if already running`() = runTest {
        val repo = TimerRepository(application)
        repo.update(TimerMode.STOPWATCH, "t", 100L)
        repo.onTimerIntent(TimerIntent.Start)
        val before = repo.timerState.first()

        repo.onTimerIntent(TimerIntent.Start) // again
        val after = repo.timerState.first()

        assertEquals(before, after) // unchanged
    }

    @Test
    fun `onTimerIntent Pause sets running false`() = runTest {
        val repo = TimerRepository(application)
        repo.onTimerIntent(TimerIntent.Pause)
        val state = repo.timerState.first()
        assertFalse(state.isRunning)
        verify { application.startService(any<Intent>()) }
    }

    @Test
    fun `onTimerIntent Resume sets running true only if not running`() = runTest {
        val repo = TimerRepository(application)
        repo.onTimerIntent(TimerIntent.Resume)
        assertTrue(repo.timerState.first().isRunning)
        verify { ContextCompat.startForegroundService(application, any()) }
    }

    @Test
    fun `onTimerIntent Stop sets running false`() = runTest {
        val repo = TimerRepository(application)
        repo.onTimerIntent(TimerIntent.Stop)
        val state = repo.timerState.first()
        assertFalse(state.isRunning)
        verify { application.startService(any<Intent>()) }
    }

    @Test
    fun `onTimerIntent null does nothing`() = runTest {
        val repo = TimerRepository(application)
        val before = repo.timerState.first()
        repo.onTimerIntent(null)
        val after = repo.timerState.first()
        assertEquals(before, after)
    }
}
