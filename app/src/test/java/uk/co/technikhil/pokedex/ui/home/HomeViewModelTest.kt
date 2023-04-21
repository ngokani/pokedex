package uk.co.technikhil.pokedex.ui.home

import androidx.lifecycle.Observer
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.co.technikhil.pokedex.data.PokemonListResponse
import uk.co.technikhil.pokedex.data.PokemonResult
import uk.co.technikhil.pokedex.util.InstantExecutorExtension
import uk.co.technikhil.pokedex.util.RxSchedulerExtension

@ExtendWith(RxSchedulerExtension::class, InstantExecutorExtension::class)
class HomeViewModelTest {

    private val mockPokemonResult = PokemonResult("test name", "https://pokeapi.co/api/v2/test")
    private val mockPokemonResults = listOf(mockPokemonResult)
    private val mockPokemonListResponse = PokemonListResponse(1, null, null, mockPokemonResults)

    private val mockHomeRepository = mock<HomeRepository> {
        on { getPokemonList(any()) } doReturn Single.just(mockPokemonListResponse)
    }

    lateinit var sut: HomeViewModel

    @BeforeEach
    fun setUp() {

        sut = HomeViewModel(mockHomeRepository)
    }

    @Test
    fun `GIVEN the page has loaded WHEN the network call is a success THEN it is emitted with results`() {

        val observer: Observer<PokemonListNetworkState> = mock()
        try {

            sut.viewState.observeForever(observer)

            sut.onViewCreated()

            verify(observer, times(1)).onChanged(eq(PokemonListNetworkState.Success(mockPokemonResults)))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }

    @Test
    fun `GIVEN the page has loaded WHEN the network call fails THEN it is emitted`() {

        whenever(mockHomeRepository.getPokemonList(any())).thenReturn(Single.error(Throwable()))
        val observer: Observer<PokemonListNetworkState> = mock()
        try {

            sut.viewState.observeForever(observer)

            sut.onViewCreated()

            verify(observer).onChanged(eq(PokemonListNetworkState.Failed))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }

    @Test
    fun `GIVEN the page has loaded WHEN the network call is ongoing THEN it is emitted`() {

        val observer: Observer<PokemonListNetworkState> = mock()
        try {

            sut.viewState.observeForever(observer)

            sut.onViewCreated()

            verify(observer).onChanged(eq(PokemonListNetworkState.Loading))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }
}