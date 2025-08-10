package com.tina.timerzeer.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerZeerError
import com.tina.timerzeer.core.domain.UnknownError
import com.tina.timerzeer.core.theme.MaxErrorCharacter
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeS
import com.tina.timerzeer.core.theme.SizeXS
import com.tina.timerzeer.core.theme.SizeXXS
import com.tina.timerzeer.core.theme.TimerzeerTheme
import com.tina.timerzeer.ui.components.LightDarkPreviews
import kotlin.math.max

@Composable
fun TimerInputField(
    value: String,
    placeholder: String,
    error: TimerZeerError? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    val borderColor = when {
        error != null -> colorScheme.error
        else -> colorScheme.secondary
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
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
                .background(
                    color = colorScheme.tertiary,
                    shape = RoundedCornerShape(RoundedCornerShapeNumber)
                ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = colorScheme.onPrimary,
                cursorColor = colorScheme.onPrimary,
                errorBorderColor = colorScheme.error,
                focusedTextColor = colorScheme.onPrimary,
                unfocusedTextColor = colorScheme.onPrimary
            ),
            isError = error != null,
            shape = RoundedCornerShape(RoundedCornerShapeNumber),
            textStyle = typography.bodyMedium
        )

        if (error != null) {
            Text(
                text = error.message.substring(0, max(error.message.length, MaxErrorCharacter)),
                color = colorScheme.error,
                style = typography.labelLarge,
                modifier = Modifier.padding(horizontal = SizeS, vertical = SizeXXS)
            )
        }
    }
}

@Composable
fun HeadlineMediumTextField(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = typography.headlineMedium,
        color = colorScheme.primary,
        modifier = modifier,
        textAlign = TextAlign.Center
    )

}

@Composable
fun CaptionTextField(text: String) {
    Text(text = text, style = typography.labelLarge, color = colorScheme.onSecondary)
}

@Composable
fun TimerOptionRow(
    text: String,
    leadingIcon: Int,
    trailingIcon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(RoundedCornerShapeNumber))
            .background(
                shape = RoundedCornerShape(RoundedCornerShapeNumber),
                color = colorScheme.tertiary
            )
            .clickable { onClick() }
            .padding(SizeS)
    ) {
        Icon(
            painter = painterResource(leadingIcon),
            contentDescription = stringResource(R.string.select_theme),
            tint = colorScheme.primary
        )

        Spacer(modifier = Modifier.width(SizeXS))

        Text(
            text = text,
            style = typography.bodyMedium,
            color = colorScheme.onSecondary,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(SizeXS))

        Icon(
            painter = painterResource(trailingIcon),
            contentDescription = null,
            tint = colorScheme.onSecondary
        )
    }
}


@LightDarkPreviews
@Composable
fun TimerOptionRowPreview() {
    TimerzeerTheme {
        TimerOptionRow(
            text = "Timer Option",
            leadingIcon = R.drawable.property_1_roller_brush,
            trailingIcon = R.drawable.property_1_chevron_right,
            onClick = {}
        )
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
            error = UnknownError,
            onValueChange = {}
        )
    }
}




