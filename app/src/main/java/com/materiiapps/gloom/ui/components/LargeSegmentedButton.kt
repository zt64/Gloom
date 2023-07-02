package com.materiiapps.gloom.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowScope.LargeSegmentedButton(
    icon: Any,
    iconDescription: String? = null,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
            .weight(1f)
            .padding(16.dp)
    ) {
        when (icon) {
            is ImageVector -> {
                Icon(
                    imageVector = icon,
                    contentDescription = iconDescription,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            is Painter -> {
                Icon(
                    painter = icon,
                    contentDescription = iconDescription,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            modifier = Modifier.basicMarquee(Int.MAX_VALUE)
        )
    }
}

@Composable
fun LargeSegmentedButtonRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) = Row(
    horizontalArrangement = Arrangement.spacedBy(2.dp),
    modifier = Modifier.clip(RoundedCornerShape(16.dp)).then(modifier),
    content = content
)