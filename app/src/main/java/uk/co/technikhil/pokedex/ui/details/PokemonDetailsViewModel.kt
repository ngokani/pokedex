package uk.co.technikhil.pokedex.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonDetailsRepository: PokemonDetailsRepository
) : ViewModel() {

    private val _state = MutableLiveData<PokemonDetailsNetworkState>()
    val viewState: LiveData<PokemonDetailsNetworkState> = _state

    fun getPokemonDetails(pokemonName: String) {
        _state.value = PokemonDetailsNetworkState.Loading
        viewModelScope.launch {
            try {
                val pokemon = pokemonDetailsRepository.getPokemon(pokemonName)
                _state.value = PokemonDetailsNetworkState.Success(pokemon)
            } catch (_: HttpException) {
                _state.value = PokemonDetailsNetworkState.Failed
            }
        }
    }
}