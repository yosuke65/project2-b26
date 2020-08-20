package com.example.project2.di

import com.example.project2.ui.auth.login.LoginActivity
import com.example.project2.ui.auth.register.RegisterActivity
import com.example.project2.ui.main.property.addproperty.AddPropertyActivity
import com.example.project2.ui.main.property.property.PropertyActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class,RepositoryModule::class])
interface AppComponent {

    fun inject(addPropertyActivity: AddPropertyActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(propertyActivity: PropertyActivity)
}