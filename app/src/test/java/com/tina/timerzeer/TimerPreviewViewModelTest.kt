package com.tina.timerzeer

import app.cash.turbine.test
import com.tina.timerzeer.core.data.repository.SettingsRepository
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.timer.data.mapper.plusDay
import com.tina.timerzeer.timer.data.mapper.plusHour
import com.tina.timerzeer.timer.data.mapper.plusMinute
import com.tina.timerzeer.timer.data.mapper.plusSecond
import com.tina.timerzeer.timer.data.repository.TimerRepository
import com.tina.timerzeer.timer.presentation.timerPreview.TimerPreviewIntent
import com.tina.timerzeer.timer.presentation.timerPreview.TimerPreviewViewModel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TimerPreviewViewModelTest {

    private val settingsRepository: SettingsRepository = mockk(relaxed = true)
    private val timerRepository: TimerRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: TimerPreviewViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TimerPreviewViewModel(settingsRepository, timerRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `OnStopwatchTitleChange updates stopwatchTitle`() = testScope.runTest {
        viewModel.timerPreviewState.test {
            skipItems(1)
            viewModel.onUserAction(TimerPreviewIntent.OnStopwatchTitleChange("Workout"))
            val state = awaitItem()
            assertEquals("Workout", state.stopwatchTitle)
        }
    }

    @Test
    fun `OnCountDownTitleChange updates countdownTitle`() = testScope.runTest {
        viewModel.timerPreviewState.test {
            skipItems(1)
            viewModel.onUserAction(TimerPreviewIntent.OnCountDownTitleChange("Timer"))
            val state = awaitItem()
            assertEquals("Timer", state.countdownTitle)
        }
    }

    @Test
    fun `OnModeChange updates mode only if different`() = testScope.runTest {
        viewModel.timerPreviewState.test {
            skipItems(1)
            // Change to COUNTDOWN
            viewModel.onUserAction(TimerPreviewIntent.OnModeChange(TimerMode.COUNTDOWN))
            val state1 = awaitItem()
            assertEquals(TimerMode.COUNTDOWN, state1.mode)

            // Change to same mode → should not emit new state
            viewModel.onUserAction(TimerPreviewIntent.OnModeChange(TimerMode.COUNTDOWN))
            expectNoEvents()
        }
    }

    @Test
    fun `SetDate updates countDownInitTime`() = testScope.runTest {
        val newTime = 12345L
        viewModel.timerPreviewState.test {
            skipItems(1)
            viewModel.onUserAction(TimerPreviewIntent.SetDate(newTime))
            val state = awaitItem()
            assertEquals(newTime, state.countDownInitTime)
        }
    }

    @Test
    fun `OnHourIncrease and OnHourDecrease work`() = testScope.runTest {
        viewModel.timerPreviewState.test {
            skipItems(1)
            val before = viewModel.timerPreviewState.value.countDownInitTime

            viewModel.onUserAction(TimerPreviewIntent.OnHourIncrease)
            assertEquals(before.plusHour(), awaitItem().countDownInitTime)

            viewModel.onUserAction(TimerPreviewIntent.OnHourDecrease)
            assertEquals(before, awaitItem().countDownInitTime)
        }
    }

    @Test
    fun `OnMinutesIncrease and OnMinutesDecrease work`() = testScope.runTest {
        viewModel.timerPreviewState.test {
            skipItems(1)
            val before = viewModel.timerPreviewState.value.countDownInitTime

            viewModel.onUserAction(TimerPreviewIntent.OnMinutesIncrease)
            assertEquals(before.plusMinute(), awaitItem().countDownInitTime)

            viewModel.onUserAction(TimerPreviewIntent.OnMinutesDecrease)
            assertEquals(before, awaitItem().countDownInitTime)
        }
    }

    @Test
    fun `OnSecondIncrease and OnSecondDecrease work`() = testScope.runTest {
        viewModel.timerPreviewState.test {
            skipItems(1)
            val before = viewModel.timerPreviewState.value.countDownInitTime

            viewModel.onUserAction(TimerPreviewIntent.OnSecondIncrease)
            assertEquals(before.plusSecond(), awaitItem().countDownInitTime)

            viewModel.onUserAction(TimerPreviewIntent.OnSecondDecrease)
            assertEquals(before, awaitItem().countDownInitTime)
        }
    }

    @Test
    fun `OnDayIncrease and OnDayDecrease work`() = testScope.runTest {
        viewModel.timerPreviewState.test {
            skipItems(1)
            val before = viewModel.timerPreviewState.value.countDownInitTime

            viewModel.onUserAction(TimerPreviewIntent.OnDayIncrease)
            assertEquals(before.plusDay(), awaitItem().countDownInitTime)

            viewModel.onUserAction(TimerPreviewIntent.OnDayDecrease)
            assertEquals(before, awaitItem().countDownInitTime)
        }
    }

    @Test
    fun `SetEndingAnimation calls repository`() = testScope.runTest {
        val animationId = 5
        viewModel.onUserAction(TimerPreviewIntent.SetEndingAnimation(animationId))
        testScheduler.advanceUntilIdle() // ensure coroutine runs
        coVerify { settingsRepository.saveEndingAnimation(animationId) }
    }

    @Test
    fun `SetBackground calls repository`() = testScope.runTest {
        val bgId = 2
        viewModel.onUserAction(TimerPreviewIntent.SetBackground(bgId))
        testScheduler.advanceUntilIdle()
        coVerify { settingsRepository.saveBackgroundTheme(bgId) }
    }

    @Test
    fun `SetStyle calls repository`() = testScope.runTest {
        val styleId = 7
        viewModel.onUserAction(TimerPreviewIntent.SetStyle(styleId))
        testScheduler.advanceUntilIdle()
        coVerify { settingsRepository.saveFontStyle(styleId) }
    }

    @Test
    fun `OnTimerStarted calls timerRepository update`() = testScope.runTest {
        viewModel.onUserAction(TimerPreviewIntent.OnTimerStarted)
        coVerify {
            timerRepository.update(
                mode = viewModel.timerPreviewState.value.mode,
                title = viewModel.timerPreviewState.value.getTitle(),
                initialTime = viewModel.timerPreviewState.value.countDownInitTime
            )
        }
    }
}
