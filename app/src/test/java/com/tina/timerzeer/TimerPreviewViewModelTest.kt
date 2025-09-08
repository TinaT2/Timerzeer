package com.tina.timerzeer

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
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
    fun `OnStopwatchTitleChange updates stopwatchTitle`() = runTest {
        viewModel.onUserAction(TimerPreviewIntent.OnStopwatchTitleChange("Workout"))
        assertEquals("Workout", viewModel.timerPreviewState.first().stopwatchTitle)
    }

    @Test
    fun `OnCountDownTitleChange updates countdownTitle`() = runTest {
        viewModel.onUserAction(TimerPreviewIntent.OnCountDownTitleChange("Timer"))
        assertEquals("Timer", viewModel.timerPreviewState.first().countdownTitle)
    }

    @Test
    fun `OnModeChange updates mode only if different`() = runTest {
        assertEquals(TimerMode.STOPWATCH, viewModel.timerPreviewState.value.mode)

        viewModel.onUserAction(TimerPreviewIntent.OnModeChange(TimerMode.COUNTDOWN))
        assertEquals(TimerMode.COUNTDOWN, viewModel.timerPreviewState.value.mode)

        val before = viewModel.timerPreviewState.value
        viewModel.onUserAction(TimerPreviewIntent.OnModeChange(TimerMode.COUNTDOWN))
        val after = viewModel.timerPreviewState.value

        assertEquals(before, after)
    }

    @Test
    fun `SetDate updates countDownInitTime`() = runTest {
        val newTime = 12345L
        viewModel.onUserAction(TimerPreviewIntent.SetDate(newTime))
        assertEquals(newTime, viewModel.timerPreviewState.first().countDownInitTime)
    }

    @Test
    fun `OnHourIncrease and OnHourDecrease work`() = runTest {
        val before = viewModel.timerPreviewState.value.countDownInitTime

        viewModel.onUserAction(TimerPreviewIntent.OnHourIncrease)
        assertEquals(before.plusHour(), viewModel.timerPreviewState.first().countDownInitTime)

        viewModel.onUserAction(TimerPreviewIntent.OnHourDecrease)
        assertEquals(before, viewModel.timerPreviewState.first().countDownInitTime)
    }

    @Test
    fun `OnMinutesIncrease and OnMinutesDecrease work`() = runTest {
        val before = viewModel.timerPreviewState.value.countDownInitTime

        viewModel.onUserAction(TimerPreviewIntent.OnMinutesIncrease)
        assertEquals(before.plusMinute(), viewModel.timerPreviewState.first().countDownInitTime)

        viewModel.onUserAction(TimerPreviewIntent.OnMinutesDecrease)
        assertEquals(before, viewModel.timerPreviewState.first().countDownInitTime)
    }

    @Test
    fun `OnSecondIncrease and OnSecondDecrease work`() = runTest {
        val before = viewModel.timerPreviewState.value.countDownInitTime

        viewModel.onUserAction(TimerPreviewIntent.OnSecondIncrease)
        assertEquals(before.plusSecond(), viewModel.timerPreviewState.first().countDownInitTime)

        viewModel.onUserAction(TimerPreviewIntent.OnSecondDecrease)
        assertEquals(before, viewModel.timerPreviewState.first().countDownInitTime)
    }

    @Test
    fun `OnDayIncrease and OnDayDecrease work`() = runTest {
        val before = viewModel.timerPreviewState.value.countDownInitTime

        viewModel.onUserAction(TimerPreviewIntent.OnDayIncrease)
        assertEquals(before.plusDay(), viewModel.timerPreviewState.first().countDownInitTime)

        viewModel.onUserAction(TimerPreviewIntent.OnDayDecrease)
        assertEquals(before, viewModel.timerPreviewState.first().countDownInitTime)
    }

    @Test
    fun `SetEndingAnimation calls repository`() = runTest {
        val animationId = 5
        viewModel.onUserAction(TimerPreviewIntent.SetEndingAnimation(animationId))
        testScheduler.advanceUntilIdle()
        coVerify { settingsRepository.saveEndingAnimation(animationId) }
    }

    @Test
    fun `SetBackground calls repository`() = runTest {
        val bgId = 2
        viewModel.onUserAction(TimerPreviewIntent.SetBackground(bgId))
        testScheduler.advanceUntilIdle()
        coVerify { settingsRepository.saveBackgroundTheme(bgId) }
    }

    @Test
    fun `SetStyle calls repository`() = runTest {
        val styleId = 7
        viewModel.onUserAction(TimerPreviewIntent.SetStyle(styleId))
        testScheduler.advanceUntilIdle()
        coVerify { settingsRepository.saveFontStyle(styleId) }
    }

    @Test
    fun `OnTimerStarted calls timerRepository update`() = runTest {
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
