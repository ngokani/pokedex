package uk.co.technikhil.pokedex.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.technikhil.pokedex.data.Pokemon
import uk.co.technikhil.pokedex.data.PokemonListResponse

interface PokeApi {

    @GET("pokemon?limit=151")
    fun getPokemonList(@Query("offset") offset: Int): Single<PokemonListResponse>

    @GET("pokemon/{name}")
    fun getPokemon(@Path("name") name: String): Single<Pokemon>
}