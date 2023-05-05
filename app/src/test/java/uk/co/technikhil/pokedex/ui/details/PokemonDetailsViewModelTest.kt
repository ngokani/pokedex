package uk.co.technikhil.pokedex.ui.details

import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.co.technikhil.pokedex.data.Pokemon
import uk.co.technikhil.pokedex.util.InstantExecutorExtension
import uk.co.technikhil.pokedex.util.RxSchedulerExtension
import uk.co.technikhil.pokedex.util.TestObserver

@ExtendWith(RxSchedulerExtension::class, InstantExecutorExtension::class)
class PokemonDetailsViewModelTest {

    private val mockPokemon = mock<Pokemon>()
    private val pokemonDetailsRepository = mock<PokemonDetailsRepository> {
        on { getPokemon(any())} doReturn Single.just(mockPokemon)
    }

    lateinit var sut: PokemonDetailsViewModel

    @BeforeEach
    fun setUp() {

        sut = PokemonDetailsViewModel(pokemonDetailsRepository)
    }

    @Test
    fun `WHEN the network call is a success THEN it is emitted with results`() {

        val observer = TestObserver<PokemonDetailsNetworkState>()
        try {

            sut.viewState.observeForever(observer)

            sut.getPokemonDetails("test")

            assertTrue(observer.wasEmitted(PokemonDetailsNetworkState.Success(mockPokemon)))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }

    @Test
    fun `WHEN the network call fails THEN it is emitted`() {

        whenever(pokemonDetailsRepository.getPokemon(any())).thenReturn(Single.error(Throwable()))
        val observer = TestObserver<PokemonDetailsNetworkState>()
        try {

            sut.viewState.observeForever(observer)

            sut.getPokemonDetails("test")

            assertTrue(observer.wasEmitted(PokemonDetailsNetworkState.Failed))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }

    @Test
    fun `WHEN the network call is ongoing THEN it is emitted`() {

        val observer = TestObserver<PokemonDetailsNetworkState>()
        try {

            sut.viewState.observeForever(observer)

            sut.getPokemonDetails("test")

            assertTrue(observer.wasEmitted(PokemonDetailsNetworkState.Loading))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }
}