package edu.ucne.regjugadores.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.regjugadores.data.database.JugadorDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideJugadorDatabase(@ApplicationContext appContext: Context)=
        Room.databaseBuilder(
            appContext,
            JugadorDB::class.java,
            "jugador_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideJugadorDao(jugadorDb: JugadorDB) = jugadorDb.jugadorDao()

    @Provides
    fun providePartidaDao(jugadorDb: JugadorDB) = jugadorDb.partidaDao()

    @Provides
    fun provideLogroDao(jugadorDb: JugadorDB) = jugadorDb.logroDao()
}