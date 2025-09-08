package com.tina.timerzeer

import android.app.Application
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.timer.data.repository.TimerRepository
import com.tina.timerzeer.timer.presentation.fullScreenTimer.FullScreenTimerViewModel
import com.tina.timerzeer.timer.presentation.fullScreenTimer.TimerFullScreenIntent
import com.tina.timerzeer.timer.presentation.fullScreenTimer.TimerIntent
import com.tina.timerzeer.timer.presentation.fullScreenTimer.TimerState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FullScreenTimerViewModelTest {

    private val application: Application = mockk(relaxed = true)
    private val repository: TimerRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: FullScreenTimerViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { repository.timerState } returns MutableStateFlow(TimerState(mode = TimerMode.STOPWATCH))
        viewModel = FullScreenTimerViewModel(application, repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun TestScope.startCollecting(): Job =
        launch(testDispatcher) { viewModel.fullState.collect { /* keep latest in .value */ } }

    @Test
    fun `Start delegates to repository`() = testScope.runTest {
        viewModel.onTimerIntent(TimerFullScreenIntent.Start)
        verify { repository.onTimerIntent(any()) }
    }

    @Test
    fun `Pause delegates to repository`() = testScope.runTest {
        viewModel.onTimerIntent(TimerFullScreenIntent.Pause)
        verify { repository.onTimerIntent(any()) }
    }

    @Test
    fun `Resume delegates to repository`() = testScope.runTest {
        viewModel.onTimerIntent(TimerFullScreenIntent.Resume)
        verify { repository.onTimerIntent(any()) }
    }

    @Test
    fun `Stop delegates to repository`() = testScope.runTest {
        viewModel.onTimerIntent(TimerFullScreenIntent.Stop)
        verify { repository.onTimerIntent(any()) }
    }

    @Test
    fun `Hide toggles hide and resets iconAppear after delay`() = runTest {
        val job = startCollecting()
        runCurrent() // let stateIn start

        assertFalse(viewModel.fullState.value.ui.hide)

        viewModel.onTimerIntent(TimerFullScreenIntent.Hide)
        runCurrent() // run the launched coroutine

        assertTrue(viewModel.fullState.value.ui.hide)

        advanceTimeBy(3000)
        runCurrent()

        assertFalse(viewModel.fullState.value.ui.iconAppear)
        job.cancel()
    }


    @Test
    fun `Lock toggles lock and resets iconAppear after delay`() = runTest {
        val job = startCollecting()
        runCurrent()

        assertFalse(viewModel.fullState.value.ui.lock)

        viewModel.onTimerIntent(TimerFullScreenIntent.Lock)
        runCurrent()

        assertTrue(viewModel.fullState.value.ui.lock)

        advanceTimeBy(3000)
        runCurrent()

        assertFalse(viewModel.fullState.value.ui.iconAppear)
        job.cancel()
    }

    @Test
    fun `IconAppear shows immediately then hides after 4s`() = runTest {
        val job = startCollecting()
        runCurrent()

        assertFalse(viewModel.fullState.value.ui.iconAppear)

        viewModel.onTimerIntent(TimerFullScreenIntent.IconAppear)
        runCurrent()

        assertTrue(viewModel.fullState.value.ui.iconAppear)

        advanceTimeBy(4000)
        runCurrent()

        assertFalse(viewModel.fullState.value.ui.iconAppear)
        job.cancel()
    }

    @Test
    fun `Start Pause Resume Stop are delegated to repository`() = runTest {
        val job = startCollecting()
        runCurrent()

        viewModel.onTimerIntent(TimerFullScreenIntent.Start)
        viewModel.onTimerIntent(TimerFullScreenIntent.Pause)
        viewModel.onTimerIntent(TimerFullScreenIntent.Resume)
        viewModel.onTimerIntent(TimerFullScreenIntent.Stop)
        runCurrent()

        verify { repository.onTimerIntent(TimerIntent.Start) }
        verify { repository.onTimerIntent(TimerIntent.Pause) }
        verify { repository.onTimerIntent(TimerIntent.Resume) }
        verify { repository.onTimerIntent(TimerIntent.Stop) }
        job.cancel()
    }
}
