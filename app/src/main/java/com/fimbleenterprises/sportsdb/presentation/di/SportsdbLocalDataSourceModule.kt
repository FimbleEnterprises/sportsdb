package com.fimbleenterprises.sportsdb.presentation.di
import com.fimbleenterprises.sportsdb.data.db.TeamsDAO
import com.fimbleenterprises.sportsdb.data.repository.dataSource.SportsdbLocalDataSource
import com.fimbleenterprises.sportsdb.data.repository.dataSourceImpl.SportsdbLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SportsdbLocalDataSourceModule {

    @Provides
    @Singleton
    fun providesSportsdbLocalDataSource(teamsDAO: TeamsDAO): SportsdbLocalDataSource {
        return SportsdbLocalDataSourceImpl(teamsDAO)
    }

}