package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.entities

import android.icu.text.DateFormat
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.CaretLeft
import com.adamglin.phosphoricons.regular.CaretRight
import com.example.planify.main.common.themes.Locals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScrollableDateRow(
    pagerScope: CoroutineScope,
    scrollPagerState: PagerState,
    textFormat: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.dateScrollBarHeight)
            .border(
                width = 1.dp,
                color = Locals.extras.border,
                shape = RectangleShape
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .size(Locals.icons.mediumLower)
                .padding(horizontal = Locals.spacing.xs)
                .clickable(
                    onClick = {
                        pagerScope.launch{
                            scrollPagerState.animateScrollToPage(scrollPagerState.currentPage - 1)
                        }
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            imageVector = PhosphorIcons.Regular.CaretLeft,
            contentDescription = null
        )

        Text(
            text = textFormat,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )

        Icon(
            modifier = Modifier
                .size(Locals.icons.mediumLower)
                .padding(horizontal = Locals.spacing.xs)
                .clickable(
                    onClick = {
                        pagerScope.launch{
                            scrollPagerState.animateScrollToPage(scrollPagerState.currentPage + 1)
                        }
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            imageVector = PhosphorIcons.Regular.CaretRight,
            contentDescription = null
        )
    }
}