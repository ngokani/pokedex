package uk.co.technikhil.pokedex.dependencies

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.technikhil.pokedex.api.PokeApi
import uk.co.technikhil.pokedex.utils.NetworkingConstants.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun providesPokeApi(retrofit: Retrofit): PokeApi = retrofit.create(PokeApi::class.java)
}
