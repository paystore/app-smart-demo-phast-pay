package com.phoebus.demo.phastpay.ui.components.filter

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.Typography
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.phoebus.demo.phastpay.R
import com.phoebus.demo.phastpay.ui.theme.AppSmartDemoPhastPayTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    initialTime: String,
    showState: Boolean,
    onCancel: () -> Unit,
    onConfirm: (Calendar, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var mode: DisplayMode by remember { mutableStateOf(DisplayMode.Picker) }

    val initialHour = initialTime.split(":").getOrNull(0)?.toIntOrNull() ?: 0
    val initialMinute = initialTime.split(":").getOrNull(1)?.toIntOrNull() ?: 0

    val timeState: TimePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
    )

    @SuppressLint("DefaultLocale")
    fun onConfirmClicked() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, timeState.hour)
        cal.set(Calendar.MINUTE, timeState.minute)
        cal.isLenient = false

        val formattedTime = String.format("%02d:%02d", timeState.hour, timeState.minute)

        onConfirm(cal, formattedTime)
    }

    if (showState) {
        PickerDialog(
            modifier = modifier,
            onDismissRequest = onCancel,
            title = {
                Text(
                    stringResource(R.string.filter_select_hour).uppercase(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            buttons = {
                DisplayModeToggleButton(
                    displayMode = mode,
                    onDisplayModeChange = { mode = it },
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onCancel) {
                    Text(stringResource(R.string.cancel))
                }
                TextButton(onClick = ::onConfirmClicked) {
                    Text(stringResource(R.string.ok))
                }
            },
        ) {
            val contentModifier = Modifier.padding(horizontal = 24.dp)
            AnimatedDisplayMode(mode, timeState, contentModifier)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayModeToggleButton(
    displayMode: DisplayMode,
    onDisplayModeChange: (DisplayMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (displayMode) {
        DisplayMode.Picker -> IconButton(
            modifier = modifier,
            onClick = { onDisplayModeChange(DisplayMode.Input) },
        ) {
            Icon(
                Icons.Default.Keyboard,
                contentDescription = "",
            )
        }

        DisplayMode.Input -> IconButton(
            modifier = modifier,
            onClick = { onDisplayModeChange(DisplayMode.Picker) },
        ) {
            Icon(
                Icons.Default.Schedule,
                contentDescription = "",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    BasicAlertDialog(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min),
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Title
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 24.dp)
                                .padding(top = 16.dp, bottom = 20.dp),
                        ) {
                            title()
                        }
                    }
                }
                CompositionLocalProvider(LocalContentColor provides AlertDialogDefaults.textContentColor) {
                    content()
                }
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                    ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp, end = 6.dp, start = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        ) {
                            buttons()
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedDisplayMode(
    mode: DisplayMode,
    timeState: TimePickerState,
    modifier: Modifier = Modifier,
) {
    TimePickerTheme {
        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }, label = ""
        ) { targetMode ->
            when (targetMode) {
                DisplayMode.Picker -> TimePicker(
                    modifier = modifier,
                    state = timeState,
                    colors = TimePickerDefaults.colors(
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                DisplayMode.Input -> TimeInput(modifier = modifier, state = timeState)
            }
        }
    }
}

@Composable
fun TimePickerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = Typography(
            displayLarge = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        )
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTimePicker() {
    var showPicker by remember { mutableStateOf(true) }

    AppSmartDemoPhastPayTheme{
        CustomTimePicker(
            initialTime = "12:30",
            showState = showPicker,
            onCancel = { showPicker = false },
            onConfirm = { calendar, formattedDate ->
                showPicker = false
            }
        )
    }
}