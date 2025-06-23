package com.phoebus.demo.phastpay.ui.components.menu

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MenuButton(title: String, onClick: ()-> Unit){

    val coroutineScope = rememberCoroutineScope();

    val isSelected = remember { mutableStateOf(false) }

    val containerColor by animateColorAsState(
        targetValue = if (isSelected.value)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.primaryContainer, // muda de surface para primaryContainer
        animationSpec = tween(durationMillis = 200),
        label = ""
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected.value)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onPrimaryContainer, // para combinar com primaryContainer
        animationSpec = tween(durationMillis = 200),
        label = ""
    )

    Button(
        onClick = {
            if (!isSelected.value) { // Evita cliques repetidos antes da animação terminar
                isSelected.value = true

                coroutineScope.launch {
                    delay(200) // Aguarda a animação
                    isSelected.value = false
                    onClick() // Chama a ação após o delay
                }
            }
        },
        shape = RoundedCornerShape(40.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)

    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }

    }


}
