package uk.co.technikhil.pokedex.ui.home

import uk.co.technikhil.pokedex.data.PokemonResult

sealed class PokemonListNetworkState {
    class Success(val pokemonResult: List<PokemonResult>) : PokemonListNetworkState()
    object Failed : PokemonListNetworkState()
    object Loading : PokemonListNetworkState()
}