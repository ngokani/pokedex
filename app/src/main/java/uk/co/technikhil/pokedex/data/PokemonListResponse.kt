package uk.co.technikhil.pokedex.data

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val pokemonResults: List<PokemonResult>
)

data class PokemonResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) {

    val id
        get() = url.split("/".toRegex()).dropLast(1).last().toInt()

    val imageUrl: String
        get()  ="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}