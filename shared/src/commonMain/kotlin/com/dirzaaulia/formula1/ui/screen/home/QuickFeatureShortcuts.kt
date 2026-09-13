package com.dirzaaulia.formula1.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.theme.DarkCard
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextSilver

@Composable
fun QuickFeatureShortcuts(
    onTelemetryClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onStandingsClick: () -> Unit,
    isCompact: Boolean = false
) {
    if (isCompact) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShortcutCard(
                index = "01",
                title = "PIT WALL TELEMETRY",
                subtitle = "Live sector deltas, speed traps & timing table",
                icon = Icons.Default.Speed,
                onClick = onTelemetryClick
            )
            ShortcutCard(
                index = "02",
                title = "2026 CALENDAR",
                subtitle = "24 Grand Prix schedule & sprint weekends",
                icon = Icons.Default.CalendarMonth,
                onClick = onCalendarClick
            )
            ShortcutCard(
                index = "03",
                title = "CHAMPIONSHIP STANDINGS",
                subtitle = "Driver & constructor rankings, points & wins",
                icon = Icons.Default.Leaderboard,
                onClick = onStandingsClick
            )
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ShortcutCard(
                index = "01",
                title = "PIT WALL TELEMETRY",
                subtitle = "Live sector deltas, speed trap speeds & full circuit radar",
                icon = Icons.Default.Speed,
                onClick = onTelemetryClick,
                modifier = Modifier.weight(1f)
            )
            ShortcutCard(
                index = "02",
                title = "2026 CALENDAR",
                subtitle = "24 Grand Prix schedule, sprint formats & circuit specs",
                icon = Icons.Default.CalendarMonth,
                onClick = onCalendarClick,
                modifier = Modifier.weight(1f)
            )
            ShortcutCard(
                index = "03",
                title = "CHAMPIONSHIP STANDINGS",
                subtitle = "Driver & constructor rankings, margins & head-to-head records",
                icon = Icons.Default.Leaderboard,
                onClick = onStandingsClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ShortcutCard(
    index: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF0D0F18).copy(alpha = 0.6f),
        border = BorderStroke(1.dp, Color(0xFF202434).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = index,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = F1Red
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = MonoWhite,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = TextSilver,
                    maxLines = 1
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

