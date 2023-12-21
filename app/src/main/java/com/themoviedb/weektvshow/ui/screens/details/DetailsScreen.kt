package com.themoviedb.weektvshow.ui.screens.details

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.themoviedb.weektvshow.R
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShow
import com.themoviedb.weektvshow.data.network.models.tvshowdetails.ShortSeason
import com.themoviedb.weektvshow.data.network.models.tvshowdetails.TvShowDetails
import com.themoviedb.weektvshow.data.room.entities.FavoriteTvShow
import com.themoviedb.weektvshow.ui.shared.*
import com.themoviedb.weektvshow.ui.theme.dimen
import com.themoviedb.weektvshow.utils.getImageByPath

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsScreenViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onCardClick: (Int) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isInFavorites by viewModel.isInFavorites.collectAsStateWithLifecycle()

    DetailsScreen(
        viewModel = viewModel,
        uiState = uiState,
        isInFavorites = isInFavorites,
        onBackPressed = onBackPressed,
        onCardClick = onCardClick,
        onFavButtonClick = { tvShowDetails ->
            insertToFavsEvent(viewModel, tvShowDetails, isInFavorites)
        }
    )
}

@Composable
fun DetailsScreen(
    viewModel: DetailsScreenViewModel,
    modifier: Modifier = Modifier,
    uiState: UiState?,
    isInFavorites: Boolean,
    onFavButtonClick: (TvShowDetails) -> Unit,
    onCardClick: (Int) -> Unit,
    onBackPressed: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val windowSize = rememberWindowSize()
    val whenItemVisible by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex >= 1
        }
    }

    val windowSizeCondition = windowSize.screenWidthInfo is WindowSizeClass.WindowType.Compact ||
            windowSize.screenHeightInfo is WindowSizeClass.WindowType.Medium
    val paddingValue =
        if (windowSizeCondition) MaterialTheme.dimen.mediumLarge else MaterialTheme.dimen.extraLarge

    DetailsPageWithState<TvShowDetails>(
        uiState = uiState,
    ) { tvShowDetails ->

        Scaffold(
            modifier = modifier,
            topBar = {
                AnimatedVisibility(
                    visible = whenItemVisible,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    AppBar(
                        titleText = tvShowDetails.name,
                        windowSize = windowSize,
                        onBackPressed = onBackPressed
                    )
                }
            }
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                state = lazyListState
            ) {
                item {
                    tvShowDetails.backdrop_path?.let {
                        HeaderSection(
                            modifier = Modifier,
                            windowSize = windowSize,
                            imageUrl = it,
                            name = tvShowDetails.name,
                            average = (tvShowDetails.vote_average / 2)
                        )
                    }
                }
                item {
                    DescriptionSection(
                        windowSize = windowSize,
                        tvShowDetails = tvShowDetails,
                        isInFavorites = isInFavorites,
                        onFavButtonClick = onFavButtonClick
                    )
                }
                item {
                    val descriptionLabelTextSize =
                        if (windowSizeCondition) MaterialTheme.typography.h6.fontSize else MaterialTheme.typography.h4.fontSize
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(MaterialTheme.dimen.medium)
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.seasons),
                            color = MaterialTheme.colors.primary,
                            fontSize = descriptionLabelTextSize
                        )
                        SeasonsSection(
                            windowSize = windowSize,
                            seasons = tvShowDetails.seasons
                        )
                    }
                }
                item {
                    val descriptionLabelTextSize =
                        if (windowSizeCondition) MaterialTheme.typography.h6.fontSize else MaterialTheme.typography.h4.fontSize
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(MaterialTheme.dimen.medium)
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.similarshow),
                            color = MaterialTheme.colors.primary,
                            fontSize = descriptionLabelTextSize
                        )
                        SimilarShowsSection(viewModel, onCardClick = onCardClick)
                    }
                }
            }
            if (!whenItemVisible) {
                BackButtonBar(
                    modifier = Modifier
                        .size(paddingValue)
                        .padding(MaterialTheme.dimen.small),
                    onBackPressed = onBackPressed
                )
            }
        }
    }
}

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    titleText: String,
    onBackPressed: () -> Unit,
    windowSize: WindowSizeClass
) {

    val windowSizeCondition = windowSize.screenWidthInfo is WindowSizeClass.WindowType.Compact
    val fontSize =
        if (windowSizeCondition) MaterialTheme.typography.h6.fontSize else MaterialTheme.typography.h4.fontSize
    val appBarHeight =
        if (windowSizeCondition) MaterialTheme.dimen.appBarNormal else MaterialTheme.dimen.appBarLarge
    val iconButtonSize =
        if (windowSizeCondition) MaterialTheme.dimen.mediumLarge else MaterialTheme.dimen.extraLarge
    val iconSize =
        if (windowSizeCondition) MaterialTheme.dimen.smallMedium else MaterialTheme.dimen.mediumLarge

    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .height(appBarHeight),
        title = {
            Text(
                text = titleText,
                color = Color.White,
                maxLines = 1,
                fontSize = fontSize,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.size(iconButtonSize),
                onClick = onBackPressed
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    average: Double,
    windowSize: WindowSizeClass
) {
    val context = LocalContext.current

    val windowSizeCondition = windowSize.screenWidthInfo is WindowSizeClass.WindowType.Compact

    val paddingValue =
        if (windowSizeCondition) MaterialTheme.dimen.large else MaterialTheme.dimen.extraLarge
    val textSize =
        if (windowSizeCondition) MaterialTheme.typography.h6.fontSize else MaterialTheme.typography.h4.fontSize

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {

        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f),
            model = ImageRequest.Builder(context)
                .data(imageUrl.getImageByPath())
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error_placeholder)
                .crossfade(true)
                .build(),
            contentDescription = name,
            alignment = Alignment.Center,
            loading = { LinearProgressIndicator() },
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier.padding(MaterialTheme.dimen.medium),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = name,
                fontSize = textSize,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))
            RatingBar(
                modifier = Modifier.size(paddingValue),
                rating = average,
                starsColor = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun DescriptionSection(
    tvShowDetails: TvShowDetails,
    isInFavorites: Boolean,
    onFavButtonClick: (TvShowDetails) -> Unit,
    windowSize: WindowSizeClass
) {
    val context = LocalContext.current
    val windowSizeCondition = windowSize.screenWidthInfo is WindowSizeClass.WindowType.Compact

    val descriptionTextSize =
        if (windowSizeCondition) MaterialTheme.typography.body1.fontSize else MaterialTheme.typography.h5.fontSize
    val descriptionLabelTextSize =
        if (windowSizeCondition) MaterialTheme.typography.h6.fontSize else MaterialTheme.typography.h4.fontSize

    Column(
        modifier = Modifier.padding(MaterialTheme.dimen.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        if (tvShowDetails.overview.isNotEmpty()) {

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.descrition),
                    color = MaterialTheme.colors.primary,
                    fontSize = descriptionLabelTextSize,
                    fontWeight = FontWeight.Bold
                )
                FavoritesButton(onFavButtonClick, tvShowDetails, isInFavorites, windowSize, context)
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))
            Text(text = tvShowDetails.overview, fontSize = descriptionTextSize)
            Spacer(modifier = Modifier.height(MaterialTheme.dimen.medium))
        } else {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                FavoritesButton(onFavButtonClick, tvShowDetails, isInFavorites, windowSize, context)
            }
        }
    }
}

@Composable
private fun FavoritesButton(
    onFavButtonClick: (TvShowDetails) -> Unit,
    tvShowDetails: TvShowDetails,
    isInFavorites: Boolean,
    windowSize: WindowSizeClass,
    context: Context
) {
    val addedTo = stringResource(id = R.string.added_to)
    val removeFrom = stringResource(id = R.string.remove_from)

    val windowSizeCondition = windowSize.screenWidthInfo is WindowSizeClass.WindowType.Compact
    val paddingValue =
        if (windowSizeCondition) MaterialTheme.dimen.smallMedium else MaterialTheme.dimen.mediumButtonSize

    val (isChecked, setChecked) = remember { mutableStateOf(isInFavorites) }

    IconToggleButton(
        modifier = Modifier.size(paddingValue),
        checked = isChecked,
        onCheckedChange = {
            setChecked(!isChecked)
            onFavButtonClick(tvShowDetails)
            val text = if (isChecked) removeFrom else addedTo
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        },
    ) {
        Icon(
            imageVector = if (isChecked) Icons.Outlined.Favorite else Icons.Default.FavoriteBorder,
            modifier = Modifier.size(paddingValue),
            tint = MaterialTheme.colors.primary,
            contentDescription = null
        )
    }
}

@Composable
fun SeasonsSection(
    seasons: List<ShortSeason>,
    windowSize: WindowSizeClass
) {
    seasons.forEach { season -> SeasonCard(season = season, windowSize = windowSize) }
}

@Composable
fun SeasonCard(
    windowSize: WindowSizeClass,
    season: ShortSeason
) {

    Card(
        modifier = Modifier
            .padding(MaterialTheme.dimen.small)
            .fillMaxWidth()
            .height(MaterialTheme.dimen.imageLarge),
        shape = RoundedCornerShape(MaterialTheme.dimen.borderRounded),
        elevation = MaterialTheme.dimen.elevationNormal,
    ) {
        Row {
            season.poster_path?.let {
                ImageSection(imageUrl = it)
            }
            ContentSection(season = season, windowSize = windowSize)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimilarCard(
    tvShow: TvShow,
    onCardClick: (Int) -> Unit
) {
    val paddingValue = MaterialTheme.dimen.small
    Card(
        modifier = Modifier
            .padding(paddingValue)
            .wrapContentSize(align = Alignment.Center),
        shape = RoundedCornerShape(MaterialTheme.dimen.borderRounded),
        elevation = MaterialTheme.dimen.elevationNormal,
        onClick = { onCardClick(tvShow.id) }
    ) {
        Column(modifier = Modifier
            .padding(paddingValue)
            .fillMaxWidth()
            .fillMaxHeight()
            .wrapContentSize(align = Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            tvShow.poster_path?.let {
                ImageSection(imageUrl = it)
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimen.medium))
            Text(text = tvShow.name, fontSize = MaterialTheme.typography.h6.fontSize, fontWeight = FontWeight.Bold,
                modifier = Modifier.width(150.dp)
                    .wrapContentSize(align = Alignment.Center))
            Spacer(modifier = Modifier.height(MaterialTheme.dimen.medium))
        }
    }
}

@Composable
private fun ContentSection(
    modifier: Modifier = Modifier,
    season: ShortSeason,
    windowSize: WindowSizeClass,
) {

    val sizeCondition = windowSize.screenWidthInfo is WindowSizeClass.WindowType.Compact ||
            windowSize.screenHeightInfo is WindowSizeClass.WindowType.Medium
    val titleFontSize =
        if (sizeCondition) MaterialTheme.typography.h6.fontSize else MaterialTheme.typography.h4.fontSize
    val overViewFontSize =
        if (sizeCondition) MaterialTheme.typography.body1.fontSize else MaterialTheme.typography.h6.fontSize

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimen.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = season.name,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            fontSize = titleFontSize,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            text = "${season.episode_count} ${stringResource(id = R.string.episodes)}",
            fontSize = overViewFontSize
        )
        Text(
            modifier = modifier.fillMaxWidth(),
            text = season.overview,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            fontSize = overViewFontSize
        )
    }
}

@Composable
private fun ImageSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
) {
    val context = LocalContext.current

    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxHeight()
            .width(MaterialTheme.dimen.extraExtraLarge),
        model = ImageRequest.Builder(context)
            .data(imageUrl.getImageByPath())
            .crossfade(true)
            .build(),
        contentDescription = null,
        alignment = Alignment.TopStart,
        loading = { LinearProgressIndicator() },
        contentScale = ContentScale.FillWidth
    )
}

private fun insertToFavsEvent(
    viewModel: DetailsScreenViewModel,
    tvShowDetails: TvShowDetails,
    isInFavorites: Boolean
) {
    viewModel.tvShowIsInFavorites(tvShowDetails.id)

    when (isInFavorites) {
        true -> {
            viewModel.deleteFavoriteTvShow(tvShowDetails.id)
        }

        false -> {
            tvShowDetails.poster_path?.let {
                FavoriteTvShow(
                    id = tvShowDetails.id,
                    name = tvShowDetails.name,
                    first_air_date = tvShowDetails.first_air_date,
                    overview = tvShowDetails.overview,
                    vote_average = tvShowDetails.vote_average,
                    poster_path = it
                )
            }?.let {
                viewModel.insertFavoriteTvShow(
                    it
                )
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SimilarShowsSection(viewModel: DetailsScreenViewModel, onCardClick: (Int) -> Unit) {
    val scrollState: LazyListState = rememberLazyListState()
    val tvShows = viewModel.tvShows.value
    Box {
        LazyRow(
            content = {
                tvShows?.forEach { tvshow ->
                    item {
                        SimilarCard(
                            tvShow = tvshow,
                            onCardClick = onCardClick
                        )
                    }
                }
            },
            state = scrollState,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        )
    }
}
