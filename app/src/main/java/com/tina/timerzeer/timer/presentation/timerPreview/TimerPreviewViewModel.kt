package com.tina.timerzeer.timer.presentation.timerPreview

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tina.timerzeer.core.data.dataStore.DataStoreFields
import com.tina.timerzeer.core.data.repository.SettingsRepository
import com.tina.timerzeer.core.theme.backgrounds
import com.tina.timerzeer.core.theme.endingAnimations
import com.tina.timerzeer.core.theme.fontStyles
import com.tina.timerzeer.timer.data.mapper.minusDay
import com.tina.timerzeer.timer.data.mapper.minusHour
import com.tina.timerzeer.timer.data.mapper.minusMinute
import com.tina.timerzeer.timer.data.mapper.minusSecond
import com.tina.timerzeer.timer.data.mapper.plusDay
import com.tina.timerzeer.timer.data.mapper.plusHour
import com.tina.timerzeer.timer.data.mapper.plusMinute
import com.tina.timerzeer.timer.data.mapper.plusSecond
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TimerPreviewViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _timerPreviewState = MutableStateFlow(TimerPreviewState())
    val timerPreviewState: StateFlow<TimerPreviewState> =
        _timerPreviewState
    private var timerJob: Job? = null


    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collectLatest { settings ->
                _timerPreviewState.update {
                    it.copy(
                        currentAnimation = settings[intPreferencesKey(
                            DataStoreFields.ENDING_ANIMATION.name
                        )]?: endingAnimations.keys.first(),
                        currentBackground = settings[intPreferencesKey(
                            name = DataStoreFields.BACKGROUND.name
                        )]?: backgrounds.keys.first(),
                        currentFontStyle = settings[intPreferencesKey(
                            name = DataStoreFields.FONT_STYLE.name
                        )]?: fontStyles.keys.first()
                    )
                }
            }
        }
    }

    fun onUserAction(action: TimerPreviewIntent) {
        when (action) {
            is TimerPreviewIntent.OnStopwatchTitleChange -> {
                _timerPreviewState.update {
                    it.copy(timerTitle = action.name)
                }
            }

            is TimerPreviewIntent.OnModeChange -> {
                _timerPreviewState.update {
                    if (it.mode != action.mode) {
                        it.copy(mode = action.mode)
                    } else it
                }
            }

            is TimerPreviewIntent.OnCountDownTitleChange -> {
                _timerPreviewState.update {
                    it.copy(countdownTitle = action.name)
                }
            }

            TimerPreviewIntent.OnHourDecrease -> {
                _timerPreviewState.update { it.copy(countDownInitTime = it.countDownInitTime.minusHour()) }
            }

            TimerPreviewIntent.OnHourIncrease -> {
                _timerPreviewState.update { it.copy(countDownInitTime = it.countDownInitTime.plusHour()) }
            }

            TimerPreviewIntent.OnMinutesDecrease -> {
                _timerPreviewState.update { it.copy(countDownInitTime = it.countDownInitTime.minusMinute()) }
            }

            TimerPreviewIntent.OnMinutesIncrease -> {
                _timerPreviewState.update { it.copy(countDownInitTime = it.countDownInitTime.plusMinute()) }
            }

            TimerPreviewIntent.OnSecondDecrease -> {
                _timerPreviewState.update { it.copy(countDownInitTime = it.countDownInitTime.minusSecond()) }
            }

            TimerPreviewIntent.OnSecondIncrease -> {
                _timerPreviewState.update { it.copy(countDownInitTime = it.countDownInitTime.plusSecond()) }
            }

            TimerPreviewIntent.OnDayDecrease -> {
                _timerPreviewState.update { it.copy(countDownInitTime = it.countDownInitTime.minusDay()) }
            }

            TimerPreviewIntent.OnDayIncrease -> {
                _timerPreviewState.update { it.copy(countDownInitTime = it.countDownInitTime.plusDay()) }
            }

            is TimerPreviewIntent.SetDate -> {
                _timerPreviewState.update { it.copy(countDownInitTime = action.countDownInitTimer) }
            }

            is TimerPreviewIntent.SetEndingAnimation -> {
                viewModelScope.launch {
                    settingsRepository.saveEndingAnimation(action.endingAnimation)
                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}
