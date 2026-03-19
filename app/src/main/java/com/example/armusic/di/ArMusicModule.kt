package com.example.armusic.di

import android.content.Context
import android.util.Log

import androidx.media3.exoplayer.ExoPlayer
import androidx.room.ProvidedTypeConverter
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.armusic.BuildConfig
import com.example.armusic.data.repository.GetSearchResult
import com.example.armusic.data.local.AppDatabase
import com.example.armusic.data.local.SongDao
import com.example.armusic.data.network.remote.ApiClientRetrofit
import com.example.armusic.data.repository.AudioDownloader
import com.example.armusic.data.repository.MyMusicImp
import com.example.armusic.data.repository.PlayerMusicRepoImp
import com.example.armusic.domain.repository.MusicPlayerRepository
import com.example.armusic.domain.repository.MyMusicRepository
import com.example.armusic.domain.repository.SongDownloaderRepository
import com.example.armusic.domain.repository.SongsRepository
import com.example.armusic.domain.usecase.DownloadSongUseCase
import com.example.armusic.domain.usecase.GetPlayListsUseCase
import com.example.armusic.domain.usecase.GetSongUseCase
import com.example.armusic.domain.usecase.PlaySongUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{
    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        impl: GetSearchResult
    ): SongsRepository

    @Binds
    @Singleton
    abstract fun bindMusicPlayerRepository(
        impl: PlayerMusicRepoImp
    ):MusicPlayerRepository

    @Binds
    @Singleton
    abstract fun bindMusicDownloaderRepository(
        impl: AudioDownloader //devuelvo esto
    ): SongDownloaderRepository // si piden esto

    @Binds
    @Singleton
    abstract fun bindListSongUseCase(
        impl: MyMusicImp
    ): MyMusicRepository

}

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule{


    @Provides
    @Singleton
    fun provideGetSongUseCase(
        songUseCase: SongsRepository
    ):GetSongUseCase{
        return GetSongUseCase(songUseCase)
    }

    @Provides
    @Singleton
    fun providePlaySongUseCase(
        playSongUseCase: MusicPlayerRepository
    ):PlaySongUseCase{
        return PlaySongUseCase(playSongUseCase)
    }
    @Provides
    @Singleton
    fun provideDownloadSongUseCase(
        downloadSongUseCase: SongDownloaderRepository
    ): DownloadSongUseCase { //devuelvo el caso de uso
        return DownloadSongUseCase(downloadSongUseCase)
    }

    @Provides
    @Singleton
    fun provideGetPlayListUseSong(
        myMusicRepository: MyMusicRepository
    ):GetPlayListsUseCase{
        return GetPlayListsUseCase(myMusicRepository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ArMusicModule {
    @Provides
    @Singleton
    @Named("baseUrl")
    fun providesUrlBase(): String = BuildConfig.PRIVATE_API_URL //URL of your api to get url-songs


    @Provides
    @Singleton
    fun provideRetrofit(@Named("baseUrl") baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    }

    @Provides
    @Singleton
    fun provideApiRetrofit(retrofit: Retrofit): ApiClientRetrofit {
        return retrofit.create(ApiClientRetrofit::class.java)
    }

    @Provides
    @Singleton
    fun getSearchResult(apiClientRetrofit: ApiClientRetrofit): GetSearchResult {
        return GetSearchResult(apiClientRetrofit)
    }


    @Provides
    @Singleton
    @Named("urlSong")
    fun provideSongUrl(): String = ""
    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context):ExoPlayer{
        return ExoPlayer.Builder(context).build()
    }

    /** ROOM**/
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context):AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "ArMusicDataBase"
        ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideSongDao(appDatabase: AppDatabase):SongDao{
        return appDatabase.songDao()
    }



}

@Module
@InstallIn(SingletonComponent::class)
object DowloadManager{
    /** OKHTTP **/

    @Provides

    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(3, 1, TimeUnit.NANOSECONDS))
            .protocols(listOf(Protocol.HTTP_1_1))
            .callTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()

                    .header("Connection", "close")
                    .header("Cache-Control", "no-cache")
                    .header("User-Agent", "OkHttp/4.11.0")
                    .removeHeader("Keep-Alive")
                    .header("Accept", "application/json")
                    .addHeader("Accept-Encoding", "identity")
                    .build()
                val response = chain.proceed(request)


                Log.d("REQUEST TO API", "request: ${request.url}")
                Log.d("RESPONSE FROM API", "Response: ${response.message}")

                response
            }
            .build()
    }
}