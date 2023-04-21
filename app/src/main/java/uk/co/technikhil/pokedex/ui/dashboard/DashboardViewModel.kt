package uk.co.technikhil.pokedex.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import uk.co.technikhil.pokedex.domain.GetPokemonListUseCase
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isFailure = MutableLiveData<Boolean>(false)
    val isFailure: LiveData<Boolean> = _isFailure

    fun onScreenShown() {
        disposables.add(
            getPokemonListUseCase()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                {
                    _isSuccess.value = true
                },
                {
                    _isFailure.value = true
                })
        )
    }

    fun onStop() {
        disposables.clear()
    }
}