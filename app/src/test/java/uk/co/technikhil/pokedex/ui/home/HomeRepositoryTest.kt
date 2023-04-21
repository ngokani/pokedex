package uk.co.technikhil.pokedex.ui.home

import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.*
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
import uk.co.technikhil.pokedex.util.RxSchedulerExtension

@ExtendWith(RxSchedulerExtension::class)
class HomeRepositoryTest {

    // NOTE data classes cannot be mocked
    private val mockPokemonResult = PokemonResult("test name", "https://pokeapi.co/api/v2/test")
    private val mockPokemonResults = listOf(mockPokemonResult)
    private val mockPokemonListResponse = PokemonListResponse(1, null, null, mockPokemonResults)

    private val mockPokeApi = mock<PokeApi> {
        on { getPokemonList(any()) } doReturn Single.just(mockPokemonListResponse)
    }

    lateinit var sut: HomeRepository

    @BeforeEach
    fun setUp() {
        sut = HomeRepository(mockPokeApi)
    }

    @Test
    fun `GIVEN the API returns successfully WHEN I get Pokemon list THEN a list of pokemon are return`() {

        sut.getPokemonList(0).test()
            .assertNoErrors()
            .assertValue(mockPokemonListResponse)
            .assertComplete()

        verify(mockPokeApi).getPokemonList(0)
    }

    @Test
    fun `GIVEN the API returns an error WHEN I get Pokemon list THEN the error is returned`() {

        whenever(mockPokeApi.getPokemonList(0)).thenReturn(Single.error(Throwable()))

        sut.getPokemonList(0).test()
            .assertError(Throwable()::class.java)
            .assertNotComplete()

        verify(mockPokeApi).getPokemonList(eq(0))
    }

    @Test
    fun `WHEN I get the same page twice THEN only one call is made to the API`() {

        sut.getPokemonList(0).test().assertComplete()
        sut.getPokemonList(0).test().assertComplete()

        verify(mockPokeApi, times(1)).getPokemonList(eq(0))
    }

    @Test
    fun `GIVEN I have two pages WHEN I get the Pokemon list for each THEN two calls are made to the API`() {
        sut.getPokemonList(0).test().assertComplete()
        sut.getPokemonList(1).test().assertComplete()

        verify(mockPokeApi, times(2)).getPokemonList(any())
    }
}