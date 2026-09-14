package com.dirzaaulia.formula1.ui.dialog

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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

private const val F1_LOGIN_URL = "https://account.formula1.com/#/en/login"

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun F1LoginDialog(
    onDismiss: () -> Unit,
    onTokenReceived: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var statusMessage by remember { mutableStateOf("Connecting to official F1 portal...") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = PitchBlack,
            border = BorderStroke(1.dp, GlassBorderActive),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Surface(
                    color = GlassSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
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
                                    text = "OFFICIAL F1 LOGIN",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Free or F1 TV accounts supported",
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
                }

                // Security Notice Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x1800E5FF))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Your password is encrypted directly by formula1.com. PitWall never stores credentials.",
                        color = Color(0xFFB0ECFF),
                        fontSize = 9.5.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 13.sp
                    )
                }

                // WebView Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.White)
                ) {
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )

                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    databaseEnabled = true
                                    userAgentString = "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"
                                }

                                val cookieMgr = CookieManager.getInstance()
                                cookieMgr.setAcceptCookie(true)
                                cookieMgr.setAcceptThirdPartyCookies(this, true)

                                fun inspectCookies(url: String?) {
                                    val checkedUrl = url ?: F1_LOGIN_URL
                                    val cookieStr = cookieMgr.getCookie(checkedUrl)
                                        ?: cookieMgr.getCookie("https://account.formula1.com")
                                        ?: cookieMgr.getCookie("https://formula1.com")

                                    val token = extractF1CookieToken(cookieStr)
                                    if (token != null) {
                                        F1LiveTimingService.setSessionToken(token)
                                        onTokenReceived(token)
                                        onDismiss()
                                    }
                                }

                                webViewClient = object : WebViewClient() {
                                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                        isLoading = true
                                        statusMessage = "Loading F1 Login Portal..."
                                        inspectCookies(url)
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        isLoading = false
                                        inspectCookies(url)
                                    }

                                    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                                        inspectCookies(url)
                                    }

                                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                        inspectCookies(request?.url?.toString())
                                        return false
                                    }
                                }

                                loadUrl(F1_LOGIN_URL)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(PitchBlack.copy(alpha = 0.85f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = F1Red,
                                    modifier = Modifier.size(36.dp)
                                )
                                Text(
                                    text = statusMessage,
                                    color = MonoWhite,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun extractF1CookieToken(cookieHeader: String?): String? {
    if (cookieHeader.isNullOrBlank()) return null
    val cookies = cookieHeader.split(";").map { it.trim() }
    for (c in cookies) {
        val parts = c.split("=", limit = 2)
        if (parts.size == 2) {
            val key = parts[0].trim()
            val value = parts[1].trim()
            if (key.equals("login-session", ignoreCase = true) ||
                key.equals("user-session", ignoreCase = true) ||
                key.equals("ascender_session", ignoreCase = true)
            ) {
                if (value.length > 20) {
                    return value
                }
            }
        }
    }
    return null
}
