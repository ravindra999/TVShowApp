package com.themoviedb.weektvshow.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.themoviedb.weektvshow.R
import com.themoviedb.weektvshow.ui.screens.home.HomeScreenViewModel
import com.themoviedb.weektvshow.ui.theme.Orange500

@Composable
fun CustomTopAppBar(
    viewModel: HomeScreenViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultTopAppBar(
                onSearchClicked = {
                    viewModel.searchAppBarState.value =
                        SearchAppBarState.OPENED
                }
            )
        }
        else -> {
            SearchTopAppBar(
                text = searchTextState,
                onTextChange = { text ->
                    viewModel.searchTextState.value = text
                },
                onCloseClicked = {
                    viewModel.searchAppBarState.value =
                        SearchAppBarState.CLOSED
                    viewModel.searchTextState.value = ""
                    viewModel.getTredingTvShows()
                },
                onSearchClicked = {
                    if(it.isNotEmpty()) {
                        viewModel.getSearchQueryTVShows(it.toString())
                    } else {
                        viewModel.getTredingTvShows()
                    }
                }
            )
        }
    }
}

@Composable
fun DefaultTopAppBar(
    onSearchClicked: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(Orange500)

    ) {
        TopAppBar(
            modifier = Modifier.padding(top = 24.dp),
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            title = {
                Text(
                    text = context.getString(R.string.app_name),
                    color = Color.White
                )
            },
            actions = {
                AppBarActions(
                    onSearchClicked = onSearchClicked
                )
            }
        )
    }
}

@Composable
fun AppBarActions(
    onSearchClicked: () -> Unit
) {
    SearchAction(onSearchClicked = onSearchClicked)
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    val context = LocalContext.current
    IconButton(
        onClick = {
            onSearchClicked()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "search_icon",
            tint = Color.White
        )
    }
}




@Composable
fun SearchTopAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {

    var trailingIconState by remember {
        mutableStateOf(TrailingIconState.DELETE)
    }

    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(Orange500)
    ) {


        Surface(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxSize(),
            color = Color.Transparent,
            elevation = 0.dp
        ) {
            TextField(
                modifier = Modifier.fillMaxSize(),
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = stringResource(id = R.string.search),
                        color = Color.White
                    )
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(
                                id = R.string.search_icon
                            ),
                            tint = Color.White
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        when (trailingIconState) {
                            TrailingIconState.DELETE -> {
                                onTextChange("")
                                trailingIconState = TrailingIconState.CLOSE
                            }
                            TrailingIconState.CLOSE -> {
                                if (text.isNotEmpty()) {
                                    onTextChange("")
                                } else {
                                    onCloseClicked()
                                    trailingIconState = TrailingIconState.DELETE
                                }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.close_icon),
                            tint = Color.White
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClicked(text)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                )
            )
        }

    }
}

@Preview
@Composable
fun CustomAppBarPreview() {
    DefaultTopAppBar(
        onSearchClicked = {}
    )
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SearchTopAppBar(
        text = "",
        onTextChange = {},
        onCloseClicked = { },
        onSearchClicked = {}
    )
}














