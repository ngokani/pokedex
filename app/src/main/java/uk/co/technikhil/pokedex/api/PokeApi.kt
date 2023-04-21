package uk.co.technikhil.pokedex.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.technikhil.pokedex.data.Pokemon
import uk.co.technikhil.pokedex.data.PokemonListResponse

interface PokeApi {

    @GET("pokemon")
    fun getPokemonList(@Query("offset") offset: Int, @Query("limit") limit: Int = 100): Single<PokemonListResponse>

    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Single<Pokemon>

    @GET("pokemon/{name}")
    fun getPokemon(@Path("name") name: String): Single<Pokemon>
}