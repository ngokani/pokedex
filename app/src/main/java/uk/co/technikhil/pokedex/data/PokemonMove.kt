package uk.co.technikhil.pokedex.data

import com.google.gson.annotations.SerializedName

data class PokemonMove(
    @SerializedName("move") val move: Move
)

data class Move(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("accuracy") val accuracy: Int?,
    @SerializedName("pp") val powerPoints: Int?,
    @SerializedName("power") val power: Int?,
    @SerializedName("damage_class") val damageClass: DamageClass?
)

