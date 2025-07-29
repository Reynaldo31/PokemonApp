package com.pokemonapp.feature_pokemon.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pokemonapp.core.ui.components.ErrorView
import com.pokemonapp.core.ui.components.LoadingView
import com.pokemonapp.domain.model.PokemonListItem
import com.pokemonapp.feature_pokemon.R
import com.pokemonapp.feature_pokemon.viewModels.PokemonListState
import com.pokemonapp.feature_pokemon.viewModels.PokemonListViewModel

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = hiltViewModel(),
    onPokemonClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showSearchResults by viewModel.showSearchResults.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refreshData() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding((WindowInsets.statusBars.asPaddingValues())),
        ) {
            ImageWelcome(
                modifier = Modifier
                    .padding(end = 15.dp, top = 5.dp, bottom = 8.dp)
                    .wrapContentSize(Alignment.CenterStart),
                R.drawable.iconpokedex
            )
            TextWelcome(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
            )
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
            )

            if (showSearchResults) {
                Text(
                    text = "Resultados de búsqueda:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = colorResource(id = R.color.grey2),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 8.dp)
                        .fillMaxWidth()
                )
            }

            when (val currentState = state) {
                is PokemonListState.Loading -> LoadingView()
                is PokemonListState.Error -> ErrorView(message = currentState.message)
                is PokemonListState.Success -> PokemonGrid(
                    pokemons = currentState.pokemons,
                    onPokemonClick = onPokemonClick
                )
            }
        }
    }
}

@Composable
fun ImageWelcome(
    modifier: Modifier = Modifier,
    imageRes: Int,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Logo Pokédex",
            modifier = modifier
                .size(200.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun TextWelcome(modifier: Modifier = Modifier) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF01243A))) {
                append("¡Hello, ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("welcome")
                }
                append("!")
            }
        },
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall.copy(
            color = Color(0xFF01243A),
            fontSize = 24.sp
        )
    )
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = Color.Gray
    val shape = RoundedCornerShape(25.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, shape)
            .border(width = 1.5.dp, color = borderColor, shape = shape)
            .background(Color.White, shape)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 20.dp)
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = "Search",
                        color = borderColor // color del borde
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Yellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Blue
                )
            }
        }
    }
}

@Composable
fun PokemonGrid(
    pokemons: List<PokemonListItem>,
    onPokemonClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.background(colorResource(id = R.color.background_default)),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pokemons) { pokemon ->
            PokemonCard(pokemon = pokemon, onClick = { onPokemonClick(pokemon.id) })
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: PokemonListItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = colorResource(R.color.grey1)
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally),
                error = painterResource(id = R.drawable.pokemon_placeholder),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.name_pokemon)
                ),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                pokemon.types.forEach { type ->
                    PokemonTypeChip(type = type)
                }
            }
        }
    }
}

@Composable
fun PokemonTypeChip(type: String) {
    val backgroundColor = when (type.lowercase()) {
        "fire" -> Color(0xFFFFA756)
        "water" -> Color(0xFF76BDFE)
        "grass" -> Color(0xFF49D0B0)
        "electric" -> Color(0xFFFFD76F)
        "psychic" -> Color(0xFFFF9FF3)
        "ice" -> Color(0xFF98D8D8)
        "dragon" -> Color(0xFF7038F8)
        "dark" -> Color(0xFF705848)
        "fairy" -> Color(0xFFEE99AC)
        "normal" -> Color(0xFFA8A878)
        "fighting" -> Color(0xFFC03028)
        "flying" -> Color(0xFFA890F0)
        "poison" -> Color(0xFFA040A0)
        "ground" -> Color(0xFFE0C068)
        "rock" -> Color(0xFFB8A038)
        "bug" -> Color(0xFFA8B820)
        "ghost" -> Color(0xFF705898)
        "steel" -> Color(0xFFB8B8D0)
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    Box(
        modifier = Modifier
            .padding(end = 4.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = type.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}


//Visualizar
@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun PokemonListScreenPreview() {
    MaterialTheme {
        PokemonListScreen(onPokemonClick = {})
    }
}

@Preview
@Composable
fun PokemonCardPreview() {
    MaterialTheme {
        PokemonCard(
            pokemon = PokemonListItem(
                id = 25,
                name = "Pikachu",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
                types = listOf("Electric")
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    MaterialTheme {
        SearchBar(query = "Pika", onQueryChange = {})
    }
}

@Preview
@Composable
fun TypeChipPreview() {
    Row {
        PokemonTypeChip(type = "Fire")
        PokemonTypeChip(type = "Water")
        PokemonTypeChip(type = "Grass")
    }
}