package uk.co.technikhil.pokedex.data

import com.google.gson.annotations.SerializedName

data class DamageClass(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("moves") val moves: List<Move>?
)