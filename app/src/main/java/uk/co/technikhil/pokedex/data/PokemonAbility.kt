package uk.co.technikhil.pokedex.data

import com.google.gson.annotations.SerializedName

data class PokemonAbility(
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("slot") val slot: Int,
    @SerializedName("ability") val ability: Ability
)