package com.tina.timerzeer.core.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerZeerError
import com.tina.timerzeer.core.domain.UnknownError
import com.tina.timerzeer.core.presentation.theme.LocalCustomColors
import com.tina.timerzeer.core.presentation.theme.MaxErrorCharacter
import com.tina.timerzeer.core.presentation.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.presentation.theme.SizeS
import com.tina.timerzeer.core.presentation.theme.SizeXS
import com.tina.timerzeer.core.presentation.theme.SizeXXS
import com.tina.timerzeer.core.presentation.theme.TimerzeerTheme
import kotlin.math.max

@Composable
fun TimerInputField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    error: TimerZeerError? = null,
    onValueChange: (String) -> Unit,
) {
    val customizedColors = LocalCustomColors.current

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                SmoothAnimatedContent(targetState = placeholder) {
                    Text(
                        it,
                        modifier = Modifier.fillMaxWidth(),
                        style = typography.bodyMedium.copy(textAlign = TextAlign.Center),
                        color = customizedColors.textColorDisabled
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .background(
                    color = customizedColors.rowBackground,
                    shape = RoundedCornerShape(RoundedCornerShapeNumber)
                ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = colorScheme.onPrimary,
                errorBorderColor = colorScheme.error,
                focusedTextColor = colorScheme.onPrimary,
                unfocusedTextColor = colorScheme.onPrimary
            ),
            isError = error != null,
            shape = RoundedCornerShape(RoundedCornerShapeNumber),
            textStyle = typography.bodyMedium.copy(textAlign = TextAlign.Center),
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
fun HeadlineSmallTextField(textId: Int, modifier: Modifier = Modifier, leadingIcon: Int? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        leadingIcon?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = stringResource(textId),
                tint = colorScheme.primary
            )
            Spacer(Modifier.width(SizeXS))
        }

        Text(
            text = stringResource(textId),
            style = typography.headlineSmall,
            color = colorScheme.secondary,
            modifier = modifier,
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun CaptionTextField(text: String) {
    val customColors = LocalCustomColors.current
    Text(text = text, style = typography.labelLarge, color = customColors.textColorDisabled)
}

@Composable
fun TextOptionButton(
    text: String,
    leadingIcon: Int,
    trailingIcon: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {

    val customColors = LocalCustomColors.current
    val textColor = if (enabled) customColors.textColorEnabled else customColors.textColorDisabled

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(RoundedCornerShapeNumber))
            .background(
                shape = RoundedCornerShape(RoundedCornerShapeNumber),
                color = customColors.rowBackground
            )
            .clickable { onClick() }
            .padding(SizeS),
        verticalAlignment = Alignment.CenterVertically
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
            color = textColor,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(SizeXS))

        Icon(
            painter = painterResource(trailingIcon),
            contentDescription = null,
            tint = textColor
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TimerInputFieldPlaceHolderCustomLibraryPreview() {
    TimerzeerTheme {
        TimerInputField(
            value = "",
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
            error = null,
            onValueChange = {}
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
fun TextOptionButtonEnabledPreview() {
    TimerzeerTheme {
        TextOptionButton(
            text = "Timer Option",
            leadingIcon = R.drawable.property_1_roller_brush,
            trailingIcon = R.drawable.property_1_chevron_right,
            enabled = true,
            onClick = {}
        )
    }
}

@LightDarkPreviews
@Composable
fun TextOptionButtonDisabledPreview() {
    TimerzeerTheme {
        TextOptionButton(
            text = "Timer Option",
            leadingIcon = R.drawable.property_1_roller_brush,
            trailingIcon = R.drawable.property_1_chevron_right,
            enabled = false,
            onClick = {}
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
            error = UnknownError,
            onValueChange = {}
        )
    }
}





