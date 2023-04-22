package uk.co.technikhil.pokedex.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonDetailsRepository: PokemonDetailsRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _state = MutableLiveData<PokemonDetailsNetworkState>()
    val viewState: LiveData<PokemonDetailsNetworkState> = _state

    fun getPokemonDetails(pokemonName: String) {
        disposables.add(
            pokemonDetailsRepository.getPokemon(pokemonName)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _state.value = PokemonDetailsNetworkState.Loading }
                .subscribe({ result ->
                        _state.value = PokemonDetailsNetworkState.Success(result)
                    }, {
                        _state.value = PokemonDetailsNetworkState.Failed
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}