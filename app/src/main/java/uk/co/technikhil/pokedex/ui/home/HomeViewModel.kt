package uk.co.technikhil.pokedex.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _state = MutableLiveData<PokemonListNetworkState>()
    val viewState: LiveData<PokemonListNetworkState> = _state

    fun onViewCreated() {
        disposables.add(
            homeRepository.getPokemonList()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _state.value = PokemonListNetworkState.Loading }
                .flatMap { Single.just(it.pokemonResults) }
                .subscribe({ results ->
                    _state.value = PokemonListNetworkState.Success(results)
                }, {
                    _state.value = PokemonListNetworkState.Failed
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}