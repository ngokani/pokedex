package uk.co.technikhil.pokedex.data

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("base_experience") val experience: Int,
    @SerializedName("height") val dmHeight: Int,
    @SerializedName("weight") val hgWeight: Int,
    @SerializedName("moves") val moves: List<PokemonMove>
) {
    val imageUrl
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}