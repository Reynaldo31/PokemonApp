package com.pokemonapp.feature_pokemon.ui.detail

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pokemonapp.core.ui.components.ErrorView
import com.pokemonapp.core.ui.components.LoadingView
import com.pokemonapp.domain.model.Pokemon
import com.pokemonapp.domain.model.Stat
import com.pokemonapp.feature_pokemon.R
import com.pokemonapp.feature_pokemon.ui.list.PokemonTypeChip
import com.pokemonapp.feature_pokemon.viewModels.PokemonDetailState
import com.pokemonapp.feature_pokemon.viewModels.PokemonDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = colorResource(id = R.color.background_default),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = (state as? PokemonDetailState.Success)?.pokemon?.name?.replaceFirstChar { it.uppercase() }
                                ?: "Pokémon Details",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = colorResource(id = R.color.name_pokemon),
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = (state as? PokemonDetailState.Success)?.pokemon?.id?.let {
                                "#${
                                    it.toString().padStart(3, '0')
                                }"
                            } ?: "",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = colorResource(id = R.color.grey1)
                            ),
                            modifier = Modifier.padding(end = 15.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colorResource(id = R.color.name_pokemon)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.background_default),
                    titleContentColor = colorResource(id = R.color.name_pokemon),
                    actionIconContentColor = colorResource(id = R.color.name_pokemon)
                )
            )
        }
    ) { innerPadding ->
        when (val currentState = state) {
            is PokemonDetailState.Loading -> LoadingView(modifier = Modifier.padding(innerPadding))
            is PokemonDetailState.Error -> ErrorView(
                message = currentState.message,
                modifier = Modifier.padding(innerPadding)
            )

            is PokemonDetailState.Success -> PokemonDetailContent(
                pokemon = currentState.pokemon,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun PokemonDetailContent(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (pokemon.types.firstOrNull()?.lowercase()) {
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding((WindowInsets.statusBars.asPaddingValues()))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
                .padding(bottom = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        color = backgroundColor.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    pokemon.types.forEach { type ->
                        PokemonTypeChip(type = type)
                    }
                }
            }

            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 30.dp)
                    .offset(y = (-70).dp),
                error = painterResource(id = R.drawable.pokemon_placeholder)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.background_default),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))
            //text description
            Text(
                text = pokemon.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = colorResource(id = R.color.grey3)
                ),
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Physical characteristics
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//Box Weight
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.iconweight),
                            contentDescription = "Weight",
                            modifier = Modifier.size(32.dp),
                            tint = colorResource(id = R.color.name_pokemon)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Columna con valor y texto
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "${pokemon.weight / 10.0} kg",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = colorResource(id = R.color.name_pokemon),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                            )
                            Text(
                                text = "Weight",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = colorResource(id = R.color.text_Physical2),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(color = colorResource(id = R.color.grey1).copy(alpha = 0.6f))
                )
//Box Weight
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.iconheight),
                            contentDescription = "Height",
                            modifier = Modifier.size(24.dp),
                            tint = colorResource(id = R.color.name_pokemon)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "${pokemon.height / 10.0} m",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = colorResource(id = R.color.name_pokemon),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Height",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = colorResource(id = R.color.text_Physical2)
                                )
                            )
                        }
                    }
                }
            }

            Text(
                text = "Stats",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = colorResource(id = R.color.name_pokemon),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            pokemon.stats.forEach { stat ->
                StatItem(
                    stat = stat,
                    primaryType = pokemon.types.firstOrNull()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Abilities",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = colorResource(id = R.color.name_pokemon),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column {
                pokemon.abilities.forEach { ability ->
                    Text(
                        text = ability.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(stat: Stat, primaryType: String? = null) {
    val progressColor = when (primaryType?.lowercase()) {
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
        else -> MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stat.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .width(120.dp)
                .padding(end = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .height(10.dp)
                .background(
                    color = progressColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(stat.value / 255f)
                    .fillMaxHeight()
                    .background(
                        color = progressColor,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }

        Text(
            text = stat.value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .width(40.dp)
                .padding(start = 4.dp),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold
        )
    }
}

//visualizacion
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PokemonDetailScreenPreview() {
    MaterialTheme {
        PokemonDetailContent(
            pokemon = Pokemon(
                id = 25,
                name = "pikachu",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                types = listOf("Electric"),
                height = 40,
                weight = 60,
                stats = listOf(
                    Stat("hp", 35),
                    Stat("attack", 55),
                    Stat("defense", 40),
                    Stat("special-attack", 50),
                    Stat("special-defense", 50),
                    Stat("speed", 90)
                ),
                abilities = listOf("static", "lightning-rod"),
                description = "It raises its tail to check its surroundings.\\nThe tail is sometimes struck by lightning\\nin this pose."

            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PokemonDetailScreenWithScaffoldPreview() {
    MaterialTheme {
        PokemonDetailScreen(
            pokemonId = 25,
            onBackClick = {}
        )
    }
}