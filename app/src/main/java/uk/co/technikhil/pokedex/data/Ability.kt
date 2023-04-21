package uk.co.technikhil.pokedex.data

import com.google.gson.annotations.SerializedName

data class Ability (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
