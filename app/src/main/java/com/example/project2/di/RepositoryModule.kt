package com.example.project2.di

import com.example.project2.api.Endpoint
import com.example.project2.ui.auth.login.LoginRepository
import com.example.project2.ui.auth.register.RegisterRepository
import com.example.project2.ui.main.property.PropertyRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesAddPropertyRepository(endpoint: Endpoint): PropertyRepository {
        return PropertyRepository(
            endpoint
        )
    }

    @Provides
    @Singleton
    fun providesLoginRepository(endpoint: Endpoint):LoginRepository{
        return LoginRepository(endpoint)
    }

    @Provides
    @Singleton
    fun providesRegisterRepository(endpoint: Endpoint): RegisterRepository {
        return RegisterRepository(endpoint)
    }


}