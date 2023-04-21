package uk.co.technikhil.pokedex.domain

import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.co.technikhil.pokedex.api.PokeApi
import uk.co.technikhil.pokedex.data.PokemonListResponse
import uk.co.technikhil.pokedex.data.PokemonResult
import uk.co.technikhil.pokedex.util.RxSchedulerExtension

@ExtendWith(RxSchedulerExtension::class)
class GetPokemonListUseCaseTest {

    // NOTE data classes cannot be mocked
    private val mockPokemonResult = PokemonResult("test name", "https://pokeapi.co/api/v2/test")
    private val mockPokemonResults = listOf(mockPokemonResult)
    private val mockPokemonListResponse = PokemonListResponse(1, null, null, mockPokemonResults)

    private val mockPokeApi = mock<PokeApi> {
        on { getPokemonList(any(), any()) } doReturn Single.just(mockPokemonListResponse)
    }

    lateinit var sut: GetPokemonListUseCase

    @BeforeEach
    fun setUp() {
        sut = GetPokemonListUseCase(mockPokeApi)
    }

    @Test
    fun `GIVEN the API returns successfully WHEN it is invoked THEN a list of pokemon are return`() {

        sut.invoke().test()
            .assertNoErrors()
            .assertValue(mockPokemonResults)
            .assertComplete()

        verify(mockPokeApi).getPokemonList(0)
    }

    @Test
    fun `GIVEN the API returns an error WHEN it is invoked THEN the error is returned`() {

        whenever(mockPokeApi.getPokemonList(0)).thenReturn(Single.error(Throwable()))

        sut.invoke().test()
            .assertError(Throwable()::class.java)
            .assertNotComplete()

        verify(mockPokeApi).getPokemonList(0)
    }
}