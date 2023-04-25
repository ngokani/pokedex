package uk.co.technikhil.pokedex.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.technikhil.pokedex.data.Pokemon
import uk.co.technikhil.pokedex.data.PokemonListResponse

interface PokeApi {

    @GET("pokemon?limit=151")
    suspend fun getPokemonList(@Query("offset") offset: Int): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): Pokemon
}