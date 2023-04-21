package uk.co.technikhil.pokedex.ui.details

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
import uk.co.technikhil.pokedex.data.Pokemon
import uk.co.technikhil.pokedex.util.RxSchedulerExtension

@ExtendWith(RxSchedulerExtension::class)
class PokemonRepositoryTest {

    private val mockPokemon = Pokemon(1, "test", 0, 1, 1)
    private val mockPokeApi = mock<PokeApi> {
        on { getPokemon(any()) } doReturn Single.just(mockPokemon)
    }
    lateinit var sut: PokemonRepository

    @BeforeEach
    fun setUp() {
        sut = PokemonRepository(mockPokeApi)
    }

    @Test
    fun `GIVEN the API returns successfully WHEN I get a Pokemon THEN a Pokemons details are returned`() {

        sut.getPokemon(0).test()
            .assertNoErrors()
            .assertValue(mockPokemon)
            .assertComplete()

        verify(mockPokeApi).getPokemon(0)
    }

    @Test
    fun `GIVEN the API returns an error WHEN I get a Pokemon THEN the error is returned`() {

        whenever(mockPokeApi.getPokemon(0)).thenReturn(Single.error(Throwable()))

        sut.getPokemon(0).test()
            .assertError(Throwable()::class.java)
            .assertNotComplete()

        verify(mockPokeApi).getPokemon(eq(0))
    }

    @Test
    fun `WHEN I get the same Pokemon twice THEN only one call is made to the API`() {

        sut.getPokemon(0).test().assertComplete()
        sut.getPokemon(0).test().assertComplete()

        verify(mockPokeApi, times(1)).getPokemon(eq(0))
    }

    @Test
    fun `WHEN I get the details for two different Pokemons THEN two calls are made to the API`() {
        sut.getPokemon(0).test().assertComplete()
        sut.getPokemon(1).test().assertComplete()

        verify(mockPokeApi, times(2)).getPokemon(any())
    }
}