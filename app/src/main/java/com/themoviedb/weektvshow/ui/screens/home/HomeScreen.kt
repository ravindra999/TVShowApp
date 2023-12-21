package com.themoviedb.weektvshow.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.themoviedb.weektvshow.R
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShow
import com.themoviedb.weektvshow.ui.shared.*
import com.themoviedb.weektvshow.ui.theme.dimen
import com.themoviedb.weektvshow.utils.getImageByPath
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardClick: (Int) -> Unit
) {
    val tvShows = viewModel.tvShows.collectAsLazyPagingItems()
    val netWorkStatus = getNetworkStatus()

    val uiState = when {
        tvShows.loadState.source.refresh == LoadState.Loading -> {
            UiState.Loading
        }
        tvShows.itemCount == 0 -> {
            if (!netWorkStatus) {
                viewModel.getLocalListOfTvShows()
                UiState.Success(tvShows)
            }
            UiState.Empty
        }
        else -> {
            UiState.Success(tvShows)
        }
    }

    HomeScreen(
        uiState = uiState,
        onCardClick = onCardClick,
        onRefresh = { viewModel.getTredingTvShows() },
        onCache = {
            viewModel.cacheTvShows(it)
        }
    )
}

@Composable
fun HomeScreen(
    uiState: UiState?,
    onCardClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onCache: (TvShow) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = LocalContext.current.getString(R.string.app_name))
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                elevation = 10.dp
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ListAndFilter(
                uiState,
                onCardClick,
                onRefresh,
                onCache
            )
        }
    }
}

@Composable
private fun ListAndFilter(
    uiState: UiState?,
    onCardClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onCache: (TvShow) -> Unit
) {

    Column {
        HomeScreenContent(
            uiState = uiState,
            onRefresh = onRefresh,
            onCache = onCache,
            onCardClick = onCardClick
        )
    }
}

@Composable
private fun HomeScreenContent(
    uiState: UiState?,
    onCardClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onCache: (TvShow) -> Unit
) {
    val context = LocalContext.current
    val status = getNetworkStatus()

    PageWithState<LazyPagingItems<TvShow>>(
        uiState = uiState
    ) {
        TvShowList(it, onRefresh, onCache) { id ->
            if (status) {
                onCardClick(id)
                return@TvShowList
            }
            Toast.makeText(
                context,
                context.getString(R.string.cant_see_details_without_internet_text),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Composable
private fun TvShowList(
    tvShows: LazyPagingItems<TvShow>,
    onRefresh: () -> Unit,
    onCache: (TvShow) -> Unit,
    onCardClick: (Int) -> Unit
) {

    val windowSize = rememberWindowSize()
    var refreshing by remember { mutableStateOf(false) }
    val amountOfGrids =
        if (
            windowSize.screenWidthInfo is WindowSizeClass.WindowType.Compact ||
            windowSize.screenHeightInfo is WindowSizeClass.WindowType.Medium
        ) 2 else 4

    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(2000)
            refreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(refreshing),
        onRefresh = {
            onRefresh()
            refreshing = true
        },
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(amountOfGrids),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            items(tvShows.itemCount) { i ->
                onCache(tvShows[i]!!)
                TvShowListItem(tvShow = tvShows[i]!!, onCardClick = onCardClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TvShowListItem(
    modifier: Modifier = Modifier,
    tvShow: TvShow,
    onCardClick: (Int) -> Unit
) {

    val windowSize = rememberWindowSize()
    val paddingValue = MaterialTheme.dimen.small

    Card(
        modifier = modifier
            .padding(paddingValue)
            .wrapContentSize(align = Alignment.Center),
        shape = RoundedCornerShape(MaterialTheme.dimen.borderRounded),
        elevation = MaterialTheme.dimen.elevationNormal,
        onClick = { onCardClick(tvShow.id) }
    ) {
        Column {
            tvShow.poster_path?.let { ImageSection(imageUrl = it) }
            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))
            TitleSection(tvShowName = tvShow.name, windowSize = windowSize)
            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))
        }
    }
}


@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    windowSize: WindowSizeClass,
    tvShowName: String
) {

    val windowSizeCondition = windowSize.screenWidthInfo is WindowSizeClass.WindowType.Compact
    val fontWeight =
        if (windowSizeCondition) FontWeight.Normal else FontWeight.Bold
    val fontTitleSize =
        if (windowSizeCondition) MaterialTheme.typography.subtitle1.fontSize else MaterialTheme.typography.h5.fontSize

    Text(
        modifier = modifier.fillMaxWidth(),
        text = tvShowName.capitalize(Locale.current),
        fontWeight = fontWeight,
        color = MaterialTheme.colors.onBackground,
        textAlign = TextAlign.Center,
        fontSize = fontTitleSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ImageSection(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    val context = LocalContext.current

    SubcomposeAsyncImage(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center),
        model = ImageRequest.Builder(context)
            .data(imageUrl.getImageByPath())
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_image_error_placeholder)
            .crossfade(true)
            .build(),
        contentDescription = null,
        loading = { LinearProgressIndicator() },
        contentScale = ContentScale.FillHeight
    )
}
