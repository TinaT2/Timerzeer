package com.tina.timerzeer.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.R
import com.tina.timerzeer.core.theme.SizeXL
import com.tina.timerzeer.core.theme.SizeXS
import com.tina.timerzeer.core.theme.SizeXXL
import com.tina.timerzeer.timer.presentation.components.LightDarkPreviews
import com.tina.timerzeer.timer.presentation.components.ThemedPreview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledDatePicker(onDateSelected: (Long) -> Unit, onDismiss: () -> Unit) {
    val datePickerState = rememberDatePickerState()
    val datePickerColors = DatePickerDefaults.colors(
        containerColor = colorScheme.background,
        headlineContentColor = colorScheme.primary,
        weekdayContentColor = colorScheme.onSecondary,
        subheadContentColor = colorScheme.onSecondary,
        yearContentColor = colorScheme.onPrimary,
        currentYearContentColor = colorScheme.primary,
        selectedYearContentColor = colorScheme.onPrimary,
        selectedYearContainerColor = colorScheme.primary,
        dayContentColor = colorScheme.onSecondary,
        disabledDayContentColor = colorScheme.onSecondary,
        selectedDayContentColor = colorScheme.onPrimary,
        selectedDayContainerColor = colorScheme.primary,
        todayContentColor = colorScheme.primary,
        todayDateBorderColor = colorScheme.secondary,
    )
    DatePickerDialog(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onDismissRequest = { onDismiss() },
        shape = RoundedCornerShape(SizeXXL),
        colors = datePickerColors,
        confirmButton = {
            Text(
                stringResource(R.string.confirm), Modifier
                    .padding(SizeXS)
                    .clickable {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(it)
                        }
                        onDismiss()
                    })
        },
        dismissButton = {
            Text(
                text = stringResource(R.string.cancel), modifier = Modifier
                    .padding(SizeXS)
                    .clickable {
                        onDismiss()
                    })
        },
    ) {
        DatePicker(
            colors = datePickerColors,
            state = datePickerState,
            title = null,
            headline = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HeadlineSmallTextField(
                        textId = R.string.set_date,
                        leadingIcon = R.drawable.property_1_calendar,
                        modifier = Modifier.padding(vertical = SizeXL)
                    )
                }
            },
            showModeToggle = false,
            modifier = Modifier.padding(0.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@LightDarkPreviews
@Composable
fun DatePickerDockedPreview() {
    ThemedPreview {
        StyledDatePicker({}, {})
    }
}