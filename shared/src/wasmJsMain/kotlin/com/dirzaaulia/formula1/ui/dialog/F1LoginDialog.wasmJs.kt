package com.dirzaaulia.formula1.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dirzaaulia.formula1.network.F1LiveTimingService
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurfaceElevated
import com.dirzaaulia.formula1.theme.MonoMuted
import com.dirzaaulia.formula1.theme.MonoSilver
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.PitchBlack
import kotlinx.browser.window

@Composable
actual fun F1LoginDialog(
    onDismiss: () -> Unit,
    onTokenReceived: (String) -> Unit
) {
    var tokenInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = PitchBlack,
            border = BorderStroke(1.dp, GlassBorderActive),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(F1Red),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MonoWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "OFFICIAL F1 TIMING GATEWAY",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Link free or paid F1 account session",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MonoSilver
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MonoSilver
                        )
                    }
                }

                // Instructions Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GlassSurfaceElevated,
                    border = BorderStroke(1.dp, Color(0x20FFFFFF))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "HOW TO CONNECT IN WEB BROWSER:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00E5FF)
                        )
                        Text(
                            text = "1. Log into your free account on formula1.com\n2. Open DevTools (F12) → Application → Cookies → formula1.com\n3. Copy the 'login-session' or 'user-session' cookie and paste below.",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MonoSilver,
                            lineHeight = 16.sp
                        )

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x30FFFFFF))
                                .clickable {
                                    window.open("https://account.formula1.com/#/en/login", "_blank")
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = null,
                                tint = MonoWhite,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "OPEN FORMULA1.COM LOGIN",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite
                            )
                        }
                    }
                }

                // Input Field
                OutlinedTextField(
                    value = tokenInput,
                    onValueChange = {
                        tokenInput = it
                        errorMessage = null
                    },
                    label = {
                        Text("Session Token / Cookie Value", fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                    },
                    placeholder = {
                        Text("Paste login-session cookie here", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = MonoMuted)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MonoWhite,
                        unfocusedTextColor = MonoWhite,
                        focusedBorderColor = F1Red,
                        unfocusedBorderColor = Color(0x40FFFFFF),
                        cursorColor = F1Red
                    ),
                    singleLine = true
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = F1Red,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x30FFFFFF)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "CANCEL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )
                    }

                    Button(
                        onClick = {
                            val clean = tokenInput.trim()
                            if (clean.length < 15) {
                                errorMessage = "Token is too short. Please copy full cookie value."
                            } else {
                                F1LiveTimingService.setSessionToken(clean)
                                onTokenReceived(clean)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = F1Red),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "CONNECT GATEWAY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )
                    }
                }
            }
        }
    }
}
