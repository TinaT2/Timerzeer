package com.tina.timerzeer.core.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerZeerError
import com.tina.timerzeer.core.theme.MaxErrorCharacter
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeS
import com.tina.timerzeer.core.theme.SizeXXS
import com.tina.timerzeer.core.theme.TimerzeerTheme
import com.tina.timerzeer.ui.components.LightDarkPreviews
import kotlin.math.max

@Composable
fun TimerInputField(
    value: String,
    placeholder: String,
    error: TimerZeerError? = null,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val borderColor = when {
        isError -> colorScheme.error
        else -> colorScheme.secondary
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    style = typography.bodyMedium,
                    color = colorScheme.onSecondary
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(RoundedCornerShapeNumber)
                )
                .background(color = colorScheme.tertiary, shape = RoundedCornerShape(RoundedCornerShapeNumber)),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = colorScheme.onPrimary,
                cursorColor = colorScheme.onPrimary,
                errorBorderColor = colorScheme.error,
                focusedTextColor = colorScheme.onPrimary,
                unfocusedTextColor = colorScheme.onPrimary
            ),
            isError = isError,
            shape = RoundedCornerShape(RoundedCornerShapeNumber),
            textStyle = typography.bodyMedium
        )

        if (isError) {
            Text(
                text = error?.message?.substring(0, max(error.message.length, MaxErrorCharacter))
                    ?: stringResource(R.string.error),
                color = colorScheme.error,
                style = typography.labelLarge,
                modifier = Modifier.padding(horizontal = SizeS, vertical = SizeXXS)
            )
        }
    }
}

@LightDarkPreviews
@Composable
fun TimerInputFieldPreview() {
    TimerzeerTheme {
        TimerInputField(
            value = "10",
            placeholder = "Enter time",
            error = null,
            isError = false,
            onValueChange = {}
        )
    }
}

@LightDarkPreviews
@Composable
fun TimerInputFieldErrorPreview() {
    TimerzeerTheme {
        TimerInputField(
            value = "error32",
            placeholder = "Enter time",
            error = null,
            isError = true,
            onValueChange = {}
        )
    }
}

@LightDarkPreviews
@Composable
fun TimerInputFieldPlaceHolderPreview() {
    TimerzeerTheme {
        TimerInputField(
            value = "",
            placeholder = "Enter time",
            error = null,
            isError = false,
            onValueChange = {}
        )
    }
}




