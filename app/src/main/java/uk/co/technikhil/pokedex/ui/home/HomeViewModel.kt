package uk.co.technikhil.pokedex.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _state = MutableLiveData<PokemonListNetworkState>()
    val viewState: LiveData<PokemonListNetworkState> = _state

    fun onViewCreated() {
        viewModelScope.launch {
            _state.value = PokemonListNetworkState.Loading

            try {
                val pokemonList = homeRepository.getPokemonList()
                _state.value = PokemonListNetworkState.Success(pokemonList.pokemonResults)
            } catch (_: HttpException) {
                _state.value = PokemonListNetworkState.Failed
            }
        }
    }
}