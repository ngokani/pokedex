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

    private val cacheList = mutableMapOf<Int, Pokemon>()

    fun getPokemon(pokemonId: Int): Single<Pokemon> {

        return if (!cacheList.contains(pokemonId)) {
            api.getPokemon(pokemonId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { cacheList[pokemonId] = it }
        } else {
            Single.just(cacheList[pokemonId]!!)
        }
    }

    fun clear() {
        cacheList.clear()
    }
}