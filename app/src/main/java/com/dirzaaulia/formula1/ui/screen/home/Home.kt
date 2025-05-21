package com.dirzaaulia.formula1.ui.screen.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dirzaaulia.formula1.R
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToRace: () -> Unit,
    navigateToStandings: () -> Unit,
    navigateToAppInfo: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        AnimatedLazyColumn(
            modifier = Modifier.padding(padding),
            isAnimationFinished = viewModel.isInitialAnimationFinished,
            navigateToRace = {
                viewModel.isInitialAnimationFinished = true
                navigateToRace.invoke()
            },
            navigateToStandings = {
                viewModel.isInitialAnimationFinished = true
                navigateToStandings.invoke()
            },
            navigateToAppInfo = {
                viewModel.isInitialAnimationFinished = true
                navigateToAppInfo.invoke()
            }
        )
    }
}

data class MenuItem(
    val id: Int,
    @StringRes
    val text: Int,
    @DrawableRes
    val image: Int,
    val navigation: () -> Unit
)

@Composable
fun AnimatedLazyColumn(
    modifier: Modifier = Modifier,
    isAnimationFinished: Boolean,
    navigateToRace: () -> Unit,
    navigateToStandings: () -> Unit,
    navigateToAppInfo: () -> Unit
) {
    val menu = listOf(
        MenuItem(1, R.string.race, R.drawable.chequered_flag, navigateToRace),
        MenuItem(2,R.string.standings, R.drawable.result, navigateToStandings),
        MenuItem(3, R.string.app_info, R.drawable.app_info, navigateToAppInfo),
    )
    val visibleStates = remember { mutableStateListOf<Boolean>().apply { addAll(List(menu.size) { isAnimationFinished }) } }
    val lazyListState = rememberLazyListState()
    var isAnimationFinished by remember { mutableStateOf(isAnimationFinished) }

    LaunchedEffect(key1 = menu) {
        if (!isAnimationFinished) {
            menu.forEachIndexed { index, item ->
                delay(item.id * 500L)
                visibleStates[index] = true
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(state = lazyListState) {
            itemsIndexed(items = menu, key = { _, item -> item.id }) { index, item ->
                AnimatedItem(
                    item = item,
                    isVisible = visibleStates[index],
                )
            }
        }
    }
}

@Composable
fun AnimatedItem(
    item: MenuItem,
    isVisible: Boolean,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 1000)
        ) + expandVertically(expandFrom = Alignment.Top, animationSpec = tween(durationMillis = 1000))
    ) {
        Card(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
                .clickable { item.navigation.invoke() }
        ) {
            Text(
                text = stringResource(item.text),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            Image(
                painter = painterResource(id = item.image),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.height(300.dp),
                alignment = Alignment.BottomEnd
            )
        }
    }
}