package uk.co.technikhil.pokedex.ui.details

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import uk.co.technikhil.pokedex.api.PokeApi
import uk.co.technikhil.pokedex.data.Pokemon
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonDetailsRepository @Inject constructor(
    private val api: PokeApi
){

    private val cacheList = mutableMapOf<String, Pokemon>()

    fun getPokemon(name: String): Single<Pokemon> {

        val pokemonName = name.lowercase()

        return if (!cacheList.contains(pokemonName)) {
            api.getPokemon(pokemonName)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { cacheList[pokemonName] = it }
        } else {
            Single.just(cacheList[pokemonName]!!)
        }
    }

    fun clear() {
        cacheList.clear()
    }
}