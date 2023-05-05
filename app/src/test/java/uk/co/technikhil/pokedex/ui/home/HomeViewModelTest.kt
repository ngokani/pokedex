package uk.co.technikhil.pokedex.ui.home

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
import uk.co.technikhil.pokedex.data.PokemonListResponse
import uk.co.technikhil.pokedex.data.PokemonResult
import uk.co.technikhil.pokedex.util.InstantExecutorExtension
import uk.co.technikhil.pokedex.util.MainDispatcherExtension
import uk.co.technikhil.pokedex.util.TestObserver

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherExtension::class, InstantExecutorExtension::class)
class HomeViewModelTest {

    private val mockPokemonResult = PokemonResult("test name", "https://pokeapi.co/api/v2/test")
    private val mockPokemonResults = listOf(mockPokemonResult)
    private val mockPokemonListResponse = PokemonListResponse(1, null, null, mockPokemonResults)

    private val mockHomeRepository = mock<HomeRepository> {
        onBlocking { getPokemonList(any()) } doReturn mockPokemonListResponse
    }

    private lateinit var sut: HomeViewModel

    @BeforeEach
    fun setUp() {

        sut = HomeViewModel(mockHomeRepository)
    }

    @Test
    fun `GIVEN the page has loaded WHEN the network call is a success THEN it is emitted with results`() = runTest {

        val observer = TestObserver(sut.viewState)
        try {

            sut.viewState.observeForever(observer)

            sut.onViewCreated()

            assertTrue(observer.wasEmitted(PokemonListNetworkState.Success(mockPokemonResults)))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }

    @Test
    fun `GIVEN the page has loaded WHEN the network call fails THEN it is emitted`() = runTest {

        val exception = HttpException(
            Response.error<PokemonListResponse>(
                500, ResponseBody.create(
                    MediaType.get("application/json"), ""
                )
            )
        )
        whenever(mockHomeRepository.getPokemonList(any())).thenThrow(exception)
        val observer = TestObserver(sut.viewState)
        try {

            sut.viewState.observeForever(observer)

            sut.onViewCreated()

            assertTrue(observer.wasEmitted(PokemonListNetworkState.Failed))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }

    @Test
    fun `GIVEN the page has loaded WHEN the network call is ongoing THEN it is emitted`() {

        val observer = TestObserver(sut.viewState)
        try {

            sut.viewState.observeForever(observer)

            sut.onViewCreated()

            assertTrue(observer.wasEmitted(PokemonListNetworkState.Loading))

        } finally {
            sut.viewState.removeObserver(observer)
        }
    }
}