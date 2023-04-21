package uk.co.technikhil.pokedex.ui.home

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import uk.co.technikhil.pokedex.api.PokeApi
import uk.co.technikhil.pokedex.data.PokemonListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val api: PokeApi
) {

    private val cacheList = mutableMapOf<Int, PokemonListResponse>()

    fun getPokemonList(offset: Int = 0): Single<PokemonListResponse> {

        return if (!cacheList.contains(offset)) {
            api.getPokemonList(offset)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { cacheList[offset] = it }
        } else {
            Single.just(cacheList[offset]!!)
        }
    }

    fun clear() {
        cacheList.clear()
    }
}