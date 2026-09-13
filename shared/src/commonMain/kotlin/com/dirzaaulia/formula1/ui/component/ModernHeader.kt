package com.dirzaaulia.formula1.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.navigation.NavTab
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurfaceLight
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TextPure
import com.dirzaaulia.formula1.theme.TextSilver

@Composable
fun ModernHeader(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    selectedSeason: Int = 2026,
    onSeasonChanged: (Int) -> Unit = {},
    isCompact: Boolean = false
) {
    // Editorial Floating Top Bar — Transparent, seamless overlay matching reference UI
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xD007090E),
                        Color(0x7007090E),
                        Color.Transparent
                    )
                )
            )
    ) {
        if (isCompact) {
            // Mobile Clean Header: Elegant branding only, no duplicate navigation tabs (bottom bar handles navigation)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Modern Clean Brand: PitWall (Option 1)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onTabSelected(NavTab.HOME) }
                ) {
                    Text(
                        text = "Pit",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Wall",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = F1Red,
                        letterSpacing = (-0.5).sp
                    )
                }

                // Clean Status / Season Indicator on right
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0x22FFFFFF),
                    border = BorderStroke(1.dp, Color(0x33FFFFFF))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(F1Red)
                        )
                        Text(
                            text = "$selectedSeason SEASON",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFFCAD1E0),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        } else {
            // Desktop / Widescreen Editorial Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // LEFT: Clean Modern PitWall Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onTabSelected(NavTab.HOME) }
                ) {
                    Text(
                        text = "Pit",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Wall",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = F1Red,
                        letterSpacing = (-0.5).sp
                    )
                }

                // CENTER: Editorial Navigation Links (Clean sans-serif typography, no bulky icons)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(36.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavTab.entries.forEach { tab ->
                        val selected = currentTab == tab
                        val textColor = if (selected) Color.White else Color(0xFFA0A5B5)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { onTabSelected(tab) }
                                .padding(horizontal = 4.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tab.title,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp,
                                color = textColor,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            if (selected) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(F1Red)
                                        .border(0.5.dp, Color.White, CircleShape)
                                )
                            } else {
                                Spacer(Modifier.size(4.dp))
                            }
                        }
                    }
                }

                // RIGHT: Counter-balance spacer
                Box(modifier = Modifier.width(140.dp), contentAlignment = Alignment.CenterEnd) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x22FFFFFF),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(F1Red)
                            )
                            Text(
                                text = "$selectedSeason SEASON",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFCAD1E0),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
