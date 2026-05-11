package com.phoebus.demo.phastpay.di

import android.content.Context
import com.phoebus.demo.phastpay.data.repositories.DeviceRepository
import com.phoebus.phastpay.sdk.client.PhastPayClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PhastPayClientModule {
    @Singleton
    @Provides
    fun providePhastPayClient(
        @ApplicationContext context: Context
    ): PhastPayClient {
        return PhastPayClient(context)
    }

    @Singleton
    @Provides
    fun deviceRepository(
        @ApplicationContext context: Context
    ): DeviceRepository {
        return DeviceRepository(context)
    }

    @Singleton
    @Provides
    fun provideNetworkJson(): Json {
        return Json {
            // Aqui você centraliza as configurações:
            ignoreUnknownKeys = true // Se a API enviar campos novos, o seu app não quebra
            coerceInputValues = true // Se a API enviar null em campo não-nulo, usa o valor padrão
            encodeDefaults = true    // Garante que valores padrão do Kotlin apareçam no JSON
            isLenient = true         // Mais permissivo com formatos de string
        }
    }


}