package com.dirzaaulia.formula1.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKtorClient(
        @ApplicationContext context: Context,
    ): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                addInterceptor(ChuckerInterceptor(context))
                config {
                    connectTimeout(timeout = 60L, unit = TimeUnit.SECONDS)
                    writeTimeout(timeout = 60L, unit = TimeUnit.SECONDS)
                    readTimeout(timeout = 60L, unit = TimeUnit.SECONDS)
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.BODY
            }

            install(Resources)
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "f1api.dev"
                }
            }
        }
    }
}