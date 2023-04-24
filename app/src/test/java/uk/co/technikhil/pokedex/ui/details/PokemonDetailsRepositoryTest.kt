package uk.co.technikhil.pokedex.ui.details

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
import uk.co.technikhil.pokedex.data.Pokemon
import uk.co.technikhil.pokedex.data.PokemonMove
import uk.co.technikhil.pokedex.util.MainDispatcherExtension

@ExtendWith(MainDispatcherExtension::class)
class PokemonDetailsRepositoryTest {

    private val mockPokemon = Pokemon(1, "test", 0, 1, 1, listOf<PokemonMove>())
    private val mockPokeApi = mock<PokeApi> {
        onBlocking { getPokemon(any()) } doReturn mockPokemon
    }
    lateinit var sut: PokemonDetailsRepository

    @BeforeEach
    fun setUp() {
        sut = PokemonDetailsRepository(mockPokeApi)
    }

    @Test
    fun `GIVEN the API returns successfully WHEN I get a Pokemon THEN a Pokemons details are returned`(): Unit =
        runBlocking {

            val actual = sut.getPokemon("test")

            assertEquals(mockPokemon, actual)
            verify(mockPokeApi).getPokemon(eq("test"))
        }

    @Test
    fun `GIVEN the API returns an error WHEN I get a Pokemon THEN the error is returned`(): Unit =
        runBlocking {

            whenever(mockPokeApi.getPokemon("test")).thenThrow(Throwable())

            val result = runCatching {
                sut.getPokemon("test")
            }

            assertTrue(result.isFailure)
            verify(mockPokeApi).getPokemon(eq("test"))
        }

    @Test
    fun `WHEN I get the same Pokemon twice THEN only one call is made to the API`(): Unit =
        runBlocking {

            sut.getPokemon("test")
            sut.getPokemon("test")

            verify(mockPokeApi, times(1)).getPokemon(eq("test"))
        }

    @Test
    fun `WHEN I get the details for two different Pokemons THEN two calls are made to the API`(): Unit =
        runBlocking {
            sut.getPokemon("test0")
            sut.getPokemon("test1")

            verify(mockPokeApi, times(2)).getPokemon(any())
        }
}