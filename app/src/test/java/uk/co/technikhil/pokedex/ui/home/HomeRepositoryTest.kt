package uk.co.technikhil.pokedex.ui.home

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
import uk.co.technikhil.pokedex.api.PokeApi
import uk.co.technikhil.pokedex.data.PokemonListResponse
import uk.co.technikhil.pokedex.data.PokemonResult
import uk.co.technikhil.pokedex.util.MainDispatcherExtension

@ExtendWith(MainDispatcherExtension::class)
class HomeRepositoryTest {

    // NOTE data classes cannot be mocked
    private val mockPokemonResult = PokemonResult("test name", "https://pokeapi.co/api/v2/test")
    private val mockPokemonResults = listOf(mockPokemonResult)
    private val mockPokemonListResponse = PokemonListResponse(1, null, null, mockPokemonResults)

    private val mockPokeApi = mock<PokeApi> {
        onBlocking { getPokemonList(any()) } doReturn mockPokemonListResponse
    }

    private lateinit var sut: HomeRepository

    @BeforeEach
    fun setUp() {
        sut = HomeRepository(mockPokeApi)
    }

    @Test
    fun `GIVEN the API returns successfully WHEN I get Pokemon list THEN a list of pokemon are return`(): Unit =
        runBlocking {

            val actual = sut.getPokemonList(0)

            assertEquals(mockPokemonListResponse, actual)
            verify(mockPokeApi).getPokemonList(0)
        }

    @Test
    fun `GIVEN the API returns an error WHEN I get Pokemon list THEN the error is returned`(): Unit =
        runBlocking {

            whenever(mockPokeApi.getPokemonList(0)).thenThrow(Throwable())

            val result = kotlin.runCatching {
                sut.getPokemonList(0)
            }

            assertTrue(result.isFailure)
            verify(mockPokeApi).getPokemonList(eq(0))
        }

    @Test
    fun `WHEN I get the same page twice THEN only one call is made to the API`(): Unit =
        runBlocking {

            sut.getPokemonList(0)
            sut.getPokemonList(0)

            verify(mockPokeApi, times(1)).getPokemonList(eq(0))
        }

    @Test
    fun `GIVEN I have two pages WHEN I get the Pokemon list for each THEN two calls are made to the API`(): Unit =
        runBlocking {
            sut.getPokemonList(0)
            sut.getPokemonList(1)

            verify(mockPokeApi, times(2)).getPokemonList(any())
        }
}