package uk.co.technikhil.pokedex.ui.details

import uk.co.technikhil.pokedex.api.PokeApi
import uk.co.technikhil.pokedex.data.Pokemon
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonDetailsRepository @Inject constructor(
    private val api: PokeApi
) {

    private val cacheList = mutableMapOf<String, Pokemon>()

    suspend fun getPokemon(name: String): Pokemon {

        val pokemonName = name.lowercase()

        return if (!cacheList.contains(pokemonName)) {
            api.getPokemon(pokemonName).also {
                cacheList[pokemonName] = it
            }
        } else {
            cacheList[pokemonName]!!
        }
    }

    fun clear() {
        cacheList.clear()
    }
}