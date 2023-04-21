package uk.co.technikhil.pokedex.domain

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import uk.co.technikhil.pokedex.api.PokeApi
import uk.co.technikhil.pokedex.data.PokemonResult
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val api: PokeApi
) {
    operator fun invoke(offset: Int = 0): Single<List<PokemonResult>> {
        return api.getPokemonList(offset)
            .flatMap { Single.just(it.pokemonResults) }
            .subscribeOn(Schedulers.io())
    }
}