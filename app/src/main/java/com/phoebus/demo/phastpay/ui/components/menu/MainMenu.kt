package com.phoebus.demo.phastpay.ui.components.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

data class MenuItem(
    val id: Int,
    val text: Int,
    val nav: () -> Unit
)

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    menuItems: List<MenuItem>
) {
    LazyVerticalGrid(
        modifier = modifier.padding(20.dp),
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(menuItems) { item ->
            MenuButton(title = stringResource(item.text), onClick = { item.nav() })
        }
    }
}

