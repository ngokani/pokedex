package uk.co.technikhil.pokedex.ui.details

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import uk.co.technikhil.pokedex.data.Pokemon
import uk.co.technikhil.pokedex.util.InstantExecutorExtension
import uk.co.technikhil.pokedex.util.MainDispatcherExtension
import uk.co.technikhil.pokedex.util.TestObserver

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherExtension::class, InstantExecutorExtension::class)
class PokemonDetailsViewModelTest {

    private val mockPokemon = mock<Pokemon>()
    private val pokemonDetailsRepository = mock<PokemonDetailsRepository> {
        onBlocking { getPokemon(any()) } doReturn mockPokemon
    }

    lateinit var sut: PokemonDetailsViewModel

    @BeforeEach
    fun setUp() {

        sut = PokemonDetailsViewModel(pokemonDetailsRepository)
    }

    @Test
    fun `WHEN the network call is a success THEN it is emitted with results`() {

        val observer = TestObserver(sut.viewState)
        try {

            sut.viewState.observeForever(observer)

            sut.getPokemonDetails("test")

            assertTrue(observer.wasEmitted(PokemonDetailsNetworkState.Success(mockPokemon)))
        } finally {
            sut.viewState.removeObserver(observer)
        }
    }

    @Test
    fun `WHEN the network call fails THEN it is emitted`() = runTest {

        val exception = HttpException(
            Response.error<Pokemon>(
                500,
                ResponseBody.create(MediaType.get("application/json"), "")
            )
        )
        whenever(pokemonDetailsRepository.getPokemon(any())).thenThrow(exception)
        val observer = TestObserver(sut.viewState)
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

        val observer = TestObserver(sut.viewState)
        try {

            sut.viewState.observeForever(observer)

            sut.getPokemonDetails("test")

            assertTrue(observer.wasEmitted(PokemonDetailsNetworkState.Loading))
        } finally {
            sut.viewState.removeObserver(observer)
        }
    }
}