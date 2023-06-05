/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.dagger

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import ch.nevis.exampleapp.coroutines.common.configuration.ConfigurationProvider
import ch.nevis.exampleapp.coroutines.common.configuration.ConfigurationProviderImpl
import ch.nevis.exampleapp.coroutines.common.configuration.Environment
import ch.nevis.exampleapp.coroutines.common.error.ErrorHandlerChain
import ch.nevis.exampleapp.coroutines.common.error.ErrorHandlerChainImpl
import ch.nevis.exampleapp.coroutines.common.settings.Settings
import ch.nevis.exampleapp.coroutines.common.settings.SettingsImpl
import ch.nevis.exampleapp.coroutines.data.cache.Cache
import ch.nevis.exampleapp.coroutines.data.cache.CacheImpl
import ch.nevis.exampleapp.coroutines.data.dataSource.LoginDataSource
import ch.nevis.exampleapp.coroutines.data.dataSource.LoginDataSourceImpl
import ch.nevis.exampleapp.coroutines.data.repository.LoginRepositoryImpl
import ch.nevis.exampleapp.coroutines.data.repository.OperationStateRepositoryImpl
import ch.nevis.exampleapp.coroutines.domain.client.ClientProvider
import ch.nevis.exampleapp.coroutines.domain.client.ClientProviderImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.*
import ch.nevis.exampleapp.coroutines.domain.log.SdkLogger
import ch.nevis.exampleapp.coroutines.domain.log.SdkLoggerImpl
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePinOperationState
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.LoginRepository
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.domain.usecase.*
import ch.nevis.mobile.sdk.api.Configuration
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider
import ch.nevis.mobile.sdk.api.operation.OperationError
import ch.nevis.mobile.sdk.api.operation.authcloudapi.AuthCloudApiError
import ch.nevis.mobile.sdk.api.operation.pin.PinChangeError
import ch.nevis.mobile.sdk.api.operation.pin.PinChanger
import ch.nevis.mobile.sdk.api.operation.pin.PinEnroller
import ch.nevis.mobile.sdk.api.operation.selection.AccountSelector
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerifier
import ch.nevis.mobile.sdk.api.util.Consumer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Runnable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URI
import javax.inject.Named
import javax.inject.Singleton

/**
 * Main Dagger Hilt configuration module of the example application.
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    //region Constants
    companion object {
        const val IN_BAND_AUTHENTICATION_USE_CASE_DEFAULT =
            "IN_BAND_AUTHENTICATION_USE_CASE_DEFAULT"
        const val IN_BAND_AUTHENTICATION_USE_CASE_FOR_DEREGISTRATION =
            "IN_BAND_AUTHENTICATION_USE_CASE_FOR_DEREGISTRATION"
    }
    //endregion

    //region Configuration
    @Suppress("DEPRECATION")
    @SuppressLint("PackageManagerGetSignatures")
    @Provides
    @Singleton
    fun provideAuthenticationCloudConfiguration(application: Application): Configuration {
        val packageInfo = application.packageManager.getPackageInfo(
            application.packageName,
            PackageManager.GET_SIGNATURES
        )
        return Configuration.authCloudBuilder()
            .packageInfo(packageInfo)
            .hostname("myinstance.mauth.nevis.cloud")
            .facetId("android:apk-key-hash:ch.nevis.mobile.authentication.sdk.android.example")
            .authenticationRetryIntervalInSeconds(15L)
            .build()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("PackageManagerGetSignatures")
    @Provides
    @Singleton
    fun provideIdentitySuiteConfiguration(application: Application): Configuration {
        val packageInfo = application.packageManager.getPackageInfo(
            application.packageName,
            PackageManager.GET_SIGNATURES
        )
        return Configuration.builder()
            .packageInfo(packageInfo)
            .baseUrl(URI.create("https://mycompany.com/"))
            .registrationRequestPath("/nevisfido/uaf/1.1/request/registration/")
            .registrationResponsePath("/nevisfido/uaf/1.1/registration/")
            .authenticationRequestPath("/auth/fidouaf")
            .authenticationResponsePath("/auth/fidouaf/authenticationresponse/")
            .deregistrationRequestPath("/nevisfido/uaf/1.1/request/deregistration/")
            .dispatchTargetResourcePath("/nevisfido/token/dispatch/targets/")
            .authenticationRetryIntervalInSeconds(15L)
            .build()
    }

    @Provides
    @Singleton
    fun provideConfigurationProvider(application: Application): ConfigurationProvider =
        ConfigurationProviderImpl(
            Environment.AUTHENTICATION_CLOUD,
            provideAuthenticationCloudConfiguration(application)
        )
    //endregion

    //region Client
    @Provides
    @Singleton
    fun provideClientProvider(): ClientProvider = ClientProviderImpl()
    //endregion

    //region Error Handling
    @Provides
    @Singleton
    fun provideErrorHandlerChain(): ErrorHandlerChain = ErrorHandlerChainImpl()
    //endregion

    //region Logger
    @Provides
    @Singleton
    fun provideSdkLogger(): SdkLogger = SdkLoggerImpl()
    //endregion

    //region Settings
    @Provides
    @Singleton
    fun provideSettings(@ApplicationContext context: Context): Settings = SettingsImpl(context)
    //endregion

    //region Data Sources
    @Provides
    fun provideLoginDataSource(retrofit: Retrofit): LoginDataSource =
        LoginDataSourceImpl(retrofit)
    //endregion


    //region Caches
    @Provides
    fun provideChangePinOperationStateCache(): Cache<ChangePinOperationState> =
        CacheImpl()

    @Provides
    fun provideUserInteractionOperationStateCache(): Cache<UserInteractionOperationState> =
        CacheImpl()
    //endregion

    //region Repositories
    @Provides
    @Singleton
    fun provideChangePinOperationStateRepository(cache: Cache<ChangePinOperationState>): OperationStateRepository<ChangePinOperationState> =
        OperationStateRepositoryImpl(cache)

    @Provides
    @Singleton
    fun provideUserInteractionOperationStateRepository(cache: Cache<UserInteractionOperationState>): OperationStateRepository<UserInteractionOperationState> =
        OperationStateRepositoryImpl(cache)

    @Provides
    fun provideLoginRepository(loginDataSource: LoginDataSource): LoginRepository =
        LoginRepositoryImpl(loginDataSource)
    //endregion

    //region Interaction
    @Provides
    fun provideAccountSelector(stateRepository: OperationStateRepository<UserInteractionOperationState>): AccountSelector =
        AccountSelectorImpl(stateRepository)

    @Provides
    fun provideAuthenticationAuthenticatorSelector(
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        settings: Settings
    ): AuthenticatorSelector = AuthenticationAuthenticatorSelectorImpl(stateRepository, settings)

    @Provides
    fun provideRegistrationAuthenticatorSelector(
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        settings: Settings
    ): AuthenticatorSelector = RegistrationAuthenticatorSelectorImpl(stateRepository, settings)

    @Provides
    fun providePinChanger(stateRepository: OperationStateRepository<ChangePinOperationState>): PinChanger =
        PinChangerImpl(stateRepository)

    @Provides
    fun providePinUserVerifier(stateRepository: OperationStateRepository<UserInteractionOperationState>): PinUserVerifier =
        PinUserVerifierImpl(stateRepository)

    @Provides
    fun providePinEnroller(stateRepository: OperationStateRepository<UserInteractionOperationState>): PinEnroller =
        PinEnrollerImpl(stateRepository)

    @Provides
    fun provideFingerprintUserVerifier(stateRepository: OperationStateRepository<UserInteractionOperationState>): FingerprintUserVerifier =
        FingerprintUserVerifierImpl(stateRepository)

    @Provides
    fun provideBiometricUserVerifier(stateRepository: OperationStateRepository<UserInteractionOperationState>): BiometricUserVerifier =
        BiometricUserVerifierImpl(stateRepository)

    @Provides
    fun provideOnSuccessAuthentication(stateRepository: OperationStateRepository<UserInteractionOperationState>): Consumer<AuthorizationProvider> =
        OnSuccessAuthenticationImpl(stateRepository)

    @Provides
    fun provideOnSuccessAuthenticationForDeregistration(stateRepository: OperationStateRepository<UserInteractionOperationState>): Consumer<AuthorizationProvider> =
        OnSuccessAuthenticationImpl(stateRepository, Operation.DEREGISTRATION)

    @Provides
    fun provideOnSuccessForUserInteractionOperation(stateRepository: OperationStateRepository<UserInteractionOperationState>): Runnable =
        OnSuccessImpl(stateRepository)

    @Provides
    fun provideOnSuccessForChangePinOperation(stateRepository: OperationStateRepository<ChangePinOperationState>): Runnable =
        OnSuccessImpl(stateRepository)

    @Provides
    fun provideOnErrorForUserInteractionOperation(
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        errorHandlerChain: ErrorHandlerChain
    ): Consumer<OperationError> = OnErrorImpl(stateRepository, errorHandlerChain)

    @Provides
    fun provideOnErrorForChangePinOperation(
        stateRepository: OperationStateRepository<ChangePinOperationState>,
        errorHandlerChain: ErrorHandlerChain
    ): Consumer<PinChangeError> = OnErrorImpl(stateRepository, errorHandlerChain)

    @Provides
    fun provideOnAuthCloudApiError(
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        errorHandlerChain: ErrorHandlerChain
    ): Consumer<AuthCloudApiError> = OnErrorImpl(stateRepository, errorHandlerChain)
    //endregion

    //region Use-cases
    @Provides
    @Singleton
    fun provideInitializeClientUseCase(
        clientProvider: ClientProvider,
        application: Application
    ): InitializeClientUseCase =
        InitializeClientUseCaseImpl(
            clientProvider,
            application
        )

    @Provides
    fun provideGetAccountsUseCase(clientProvider: ClientProvider): GetAccountsUseCase =
        GetAccountsUseCaseImpl(clientProvider)

    @Provides
    fun provideGetAuthenticatorsUseCase(clientProvider: ClientProvider): GetAuthenticatorsUseCase =
        GetAuthenticatorsUseCaseImpl(clientProvider)

    @Provides
    fun provideCreateDeviceInformationUseCase(@ApplicationContext context: Context): CreateDeviceInformationUseCase =
        CreateDeviceInformationUseCaseImpl(context)

    @Provides
    fun provideGetDeviceInformationUseCase(clientProvider: ClientProvider): GetDeviceInformationUseCase =
        GetDeviceInformationUseCaseImpl(clientProvider)

    @Provides
    fun provideChangeDeviceInformationUseCase(
        clientProvider: ClientProvider
    ): ChangeDeviceInformationUseCase = ChangeDeviceInformationUseCaseImpl(clientProvider)

    @Provides
    @Named(IN_BAND_AUTHENTICATION_USE_CASE_DEFAULT)
    fun provideInBandAuthenticationUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        errorHandlerChain: ErrorHandlerChain,
        pinUserVerifier: PinUserVerifier,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        settings: Settings
    ): InBandAuthenticationUseCase = InBandAuthenticationUseCaseImpl(
        clientProvider,
        stateRepository,
        provideAuthenticationAuthenticatorSelector(stateRepository, settings),
        pinUserVerifier,
        fingerprintUserVerifier,
        biometricUserVerifier,
        provideOnSuccessAuthentication(stateRepository),
        provideOnErrorForUserInteractionOperation(stateRepository, errorHandlerChain)
    )

    @Provides
    @Named(IN_BAND_AUTHENTICATION_USE_CASE_FOR_DEREGISTRATION)
    fun provideInBandAuthenticationUseCaseForDeregistration(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        errorHandlerChain: ErrorHandlerChain,
        pinUserVerifier: PinUserVerifier,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        settings: Settings
    ): InBandAuthenticationUseCase = InBandAuthenticationUseCaseImpl(
        clientProvider,
        stateRepository,
        provideAuthenticationAuthenticatorSelector(stateRepository, settings),
        pinUserVerifier,
        fingerprintUserVerifier,
        biometricUserVerifier,
        provideOnSuccessAuthenticationForDeregistration(stateRepository),
        provideOnErrorForUserInteractionOperation(stateRepository, errorHandlerChain)
    )

    @Provides
    fun provideSelectAccountUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): SelectAccountUseCase =
        SelectAccountUseCaseImpl(stateRepository)

    @Provides
    fun provideSelectAuthenticatorUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): SelectAuthenticatorUseCase =
        SelectAuthenticatorUseCaseImpl(stateRepository)

    @Provides
    fun provideSetPinUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): SetPinUseCase =
        SetPinUseCaseImpl(stateRepository)

    @Provides
    fun provideStartChangePinUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<ChangePinOperationState>,
        pinChanger: PinChanger,
        onError: Consumer<PinChangeError>
    ): StartChangePinUseCase = StartChangePinUseCaseImpl(
        clientProvider,
        stateRepository,
        pinChanger,
        provideOnSuccessForChangePinOperation(stateRepository),
        onError
    )

    @Provides
    fun provideChangePinUseCase(stateRepository: OperationStateRepository<ChangePinOperationState>): ChangePinUseCase =
        ChangePinUseCaseImpl(stateRepository)

    @Provides
    fun provideVerifyPinUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): VerifyPinUseCase =
        VerifyPinUseCaseImpl(stateRepository)

    @Provides
    fun provideVerifyFingerprintUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): VerifyFingerprintUseCase =
        VerifyFingerprintUseCaseImpl(stateRepository)

    @Provides
    fun provideVerifyBiometricUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): VerifyBiometricUseCase =
        VerifyBiometricUseCaseImpl(stateRepository)

    @Provides
    fun provideProcessOutOfBandPayloadUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        errorHandlerChain: ErrorHandlerChain,
        createDeviceInformationUseCase: CreateDeviceInformationUseCase,
        accountSelector: AccountSelector,
        pinEnroller: PinEnroller,
        pinUserVerifier: PinUserVerifier,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        settings: Settings
    ): ProcessOutOfBandPayloadUseCase = ProcessOutOfBandPayloadUseCaseImpl(
        clientProvider,
        stateRepository,
        createDeviceInformationUseCase,
        accountSelector,
        provideAuthenticationAuthenticatorSelector(stateRepository, settings),
        provideRegistrationAuthenticatorSelector(stateRepository, settings),
        pinEnroller,
        pinUserVerifier,
        fingerprintUserVerifier,
        biometricUserVerifier,
        provideOnSuccessAuthentication(stateRepository),
        provideOnSuccessForUserInteractionOperation(stateRepository),
        provideOnErrorForUserInteractionOperation(stateRepository, errorHandlerChain)
    )

    @Provides
    fun provideDecodePayloadUseCase(
        clientProvider: ClientProvider
    ): DecodePayloadUseCase =
        DecodePayloadUseCaseImpl(clientProvider)

    @Provides
    fun provideDeregisterUseCase(clientProvider: ClientProvider): DeregisterUseCase =
        DeregisterUseCaseImpl(clientProvider)

    @Provides
    fun provideAuthCloudApiRegistrationUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        createDeviceInformationUseCase: CreateDeviceInformationUseCase,
        pinEnroller: PinEnroller,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        onError: Consumer<AuthCloudApiError>,
        settings: Settings
    ): AuthCloudApiRegistrationUseCase = AuthCloudApiRegistrationUseCaseImpl(
        clientProvider,
        stateRepository,
        createDeviceInformationUseCase,
        provideRegistrationAuthenticatorSelector(stateRepository, settings),
        pinEnroller,
        fingerprintUserVerifier,
        biometricUserVerifier,
        provideOnSuccessForUserInteractionOperation(stateRepository),
        onError
    )

    @Provides
    fun provideLoginUseCase(loginRepository: LoginRepository): LoginUseCase =
        LoginUseCaseImpl(loginRepository)

    @Provides
    fun provideInBandRegistrationUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        createDeviceInformationUseCase: CreateDeviceInformationUseCase,
        pinEnroller: PinEnroller,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        errorHandlerChain: ErrorHandlerChain,
        settings: Settings
    ): InBandRegistrationUseCase = InBandRegistrationUseCaseImpl(
        clientProvider,
        stateRepository,
        createDeviceInformationUseCase,
        provideRegistrationAuthenticatorSelector(stateRepository, settings),
        pinEnroller,
        fingerprintUserVerifier,
        biometricUserVerifier,
        provideOnSuccessForUserInteractionOperation(stateRepository),
        provideOnErrorForUserInteractionOperation(stateRepository, errorHandlerChain)
    )

    @Provides
    fun provideCancelOperationUseCase(
        userInteractionOperationStateRepository: OperationStateRepository<UserInteractionOperationState>,
        changePinOperationStateRepository: OperationStateRepository<ChangePinOperationState>
    ): CancelOperationUseCase = CancelOperationUseCaseImpl(
        userInteractionOperationStateRepository,
        changePinOperationStateRepository
    )

    @Provides
    fun provideFinishOperationUseCase(
        userInteractionOperationStateRepository: OperationStateRepository<UserInteractionOperationState>,
        changePinOperationStateRepository: OperationStateRepository<ChangePinOperationState>
    ): FinishOperationUseCase = FinishOperationUseCaseImpl(
        userInteractionOperationStateRepository,
        changePinOperationStateRepository
    )
    //endregion

    //region Retrofit
    @Provides
    fun provideRetrofit(configurationProvider: ConfigurationProvider): Retrofit =
        Retrofit.Builder().baseUrl(configurationProvider.configuration.baseUrl().toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    //endregion
}
