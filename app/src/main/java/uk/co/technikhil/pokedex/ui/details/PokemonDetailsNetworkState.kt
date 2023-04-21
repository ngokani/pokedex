package uk.co.technikhil.pokedex.ui.details

import uk.co.technikhil.pokedex.data.Pokemon

sealed class PokemonDetailsNetworkState {
    class Success(val pokemonResult: Pokemon) : PokemonDetailsNetworkState()
    object Failed : PokemonDetailsNetworkState()
    object Loading : PokemonDetailsNetworkState()
}