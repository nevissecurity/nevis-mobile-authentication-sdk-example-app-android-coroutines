/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2022-2024. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.dagger

import android.content.Context
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
import ch.nevis.exampleapp.coroutines.domain.interaction.AccountSelectorImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.AuthenticatorSelectorImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.AuthenticatorSelectorOperation
import ch.nevis.exampleapp.coroutines.domain.interaction.BiometricUserVerifierImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.DevicePasscodeUserVerifierImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.FingerprintUserVerifierImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.OnErrorImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.OnSuccessAuthenticationImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.OnSuccessImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.password.PasswordChangerImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.password.PasswordEnrollerImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.password.PasswordUserVerifierImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.pin.PinChangerImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.pin.PinEnrollerImpl
import ch.nevis.exampleapp.coroutines.domain.interaction.pin.PinUserVerifierImpl
import ch.nevis.exampleapp.coroutines.domain.log.SdkLogger
import ch.nevis.exampleapp.coroutines.domain.log.SdkLoggerImpl
import ch.nevis.exampleapp.coroutines.domain.model.operation.Operation
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePasswordOperationState
import ch.nevis.exampleapp.coroutines.domain.model.state.ChangePinOperationState
import ch.nevis.exampleapp.coroutines.domain.model.state.UserInteractionOperationState
import ch.nevis.exampleapp.coroutines.domain.repository.LoginRepository
import ch.nevis.exampleapp.coroutines.domain.repository.OperationStateRepository
import ch.nevis.exampleapp.coroutines.domain.usecase.AuthCloudApiRegistrationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.AuthCloudApiRegistrationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.CancelOperationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.CancelOperationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangeDeviceInformationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangeDeviceInformationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangePasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangePasswordUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangePinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.ChangePinUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.CreateDeviceInformationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.CreateDeviceInformationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.DecodePayloadUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.DecodePayloadUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.DeleteAuthenticatorsUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.DeleteAuthenticatorsUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.DeregisterUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.DeregisterUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.FinishOperationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.FinishOperationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.GetAccountsUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.GetAccountsUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.GetAuthenticatorsUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.GetAuthenticatorsUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.GetDeviceInformationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.GetDeviceInformationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.InBandAuthenticationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.InBandAuthenticationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.InBandRegistrationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.InBandRegistrationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.InitializeClientUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.InitializeClientUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.LoginUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.LoginUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.ProcessOutOfBandPayloadUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.ProcessOutOfBandPayloadUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.SelectAccountUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.SelectAccountUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.SelectAuthenticatorUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.SelectAuthenticatorUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.SetPasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.SetPasswordUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.SetPinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.SetPinUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.StartChangePasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.StartChangePasswordUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.StartChangePinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.StartChangePinUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.TransactionConfirmationUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.TransactionConfirmationUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyBiometricUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyBiometricUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyDevicePasscodeUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyDevicePasscodeUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyFingerprintUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyFingerprintUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPasswordUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPasswordUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPinUseCase
import ch.nevis.exampleapp.coroutines.domain.usecase.VerifyPinUseCaseImpl
import ch.nevis.exampleapp.coroutines.domain.validation.AuthenticatorValidator
import ch.nevis.exampleapp.coroutines.domain.validation.AuthenticatorValidatorImpl
import ch.nevis.exampleapp.coroutines.domain.validation.PasswordPolicyImpl
import ch.nevis.mobile.sdk.api.Configuration
import ch.nevis.mobile.sdk.api.authorization.AuthorizationProvider
import ch.nevis.mobile.sdk.api.localdata.Authenticator.BIOMETRIC_AUTHENTICATOR_AAID
import ch.nevis.mobile.sdk.api.localdata.Authenticator.DEVICE_PASSCODE_AUTHENTICATOR_AAID
import ch.nevis.mobile.sdk.api.localdata.Authenticator.FINGERPRINT_AUTHENTICATOR_AAID
import ch.nevis.mobile.sdk.api.localdata.Authenticator.PASSWORD_AUTHENTICATOR_AAID
import ch.nevis.mobile.sdk.api.localdata.Authenticator.PIN_AUTHENTICATOR_AAID
import ch.nevis.mobile.sdk.api.operation.AuthenticationError
import ch.nevis.mobile.sdk.api.operation.OperationError
import ch.nevis.mobile.sdk.api.operation.authcloudapi.AuthCloudApiError
import ch.nevis.mobile.sdk.api.operation.password.PasswordChangeError
import ch.nevis.mobile.sdk.api.operation.password.PasswordChanger
import ch.nevis.mobile.sdk.api.operation.password.PasswordEnroller
import ch.nevis.mobile.sdk.api.operation.password.PasswordPolicy
import ch.nevis.mobile.sdk.api.operation.pin.PinChangeError
import ch.nevis.mobile.sdk.api.operation.pin.PinChanger
import ch.nevis.mobile.sdk.api.operation.pin.PinEnroller
import ch.nevis.mobile.sdk.api.operation.selection.AccountSelector
import ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector
import ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.DevicePasscodeUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.PasswordUserVerifier
import ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Runnable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.Consumer
import javax.inject.Named
import javax.inject.Singleton

/**
 * Main Dagger Hilt configuration module of the example application.
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    //region Constants
    /**
     * Collection of constants.
     */
    companion object {
        /**
         * The unique name of authenticator selector implementation for Registration operation.
         */
        const val REGISTRATION_AUTHENTICATOR_SELECTOR = "REGISTRATION_AUTHENTICATOR_SELECTOR"

        /**
         * The unique name of authenticator selector implementation for Authentication operation.
         */
        const val AUTHENTICATION_AUTHENTICATOR_SELECTOR = "AUTHENTICATION_AUTHENTICATOR_SELECTOR"

        /**
         * The unique name of default in-band authentication use case.
         */
        const val IN_BAND_AUTHENTICATION_USE_CASE_DEFAULT =
            "IN_BAND_AUTHENTICATION_USE_CASE_DEFAULT"

        /**
         * The unique name of in-band authentication use case used for Deregistration operation.
         */
        const val IN_BAND_AUTHENTICATION_USE_CASE_FOR_DEREGISTRATION =
            "IN_BAND_AUTHENTICATION_USE_CASE_FOR_DEREGISTRATION"
    }
    //endregion

    //region Configuration
    /**
     * Provides Auth Cloud specific configuration.
     *
     * @return The Auth Cloud specific configuration.
     */
    @Provides
    @Singleton
    fun provideAuthenticationCloudConfiguration(): Configuration {
        return Configuration.authCloudBuilder()
            .hostname("myinstance.mauth.nevis.cloud")
            .build()
    }

    /**
     * Provides Identity Suite specific configuration.
     *
     * @return The Identity Suite specific configuration.
     */
    @Provides
    @Singleton
    fun provideIdentitySuiteConfiguration(): Configuration {
        return Configuration.admin4PatternBuilder()
            .hostname("idsuite")
            .build()
    }

    /**
     * Provides the list of allowed authenticators.
     *
     * @return The list of allowed authenticators.
     */
    @Provides
    fun provideAuthenticatorAllowlist(): List<String> = listOf(
        PIN_AUTHENTICATOR_AAID,
        PASSWORD_AUTHENTICATOR_AAID,
        FINGERPRINT_AUTHENTICATOR_AAID,
        BIOMETRIC_AUTHENTICATOR_AAID,
        DEVICE_PASSCODE_AUTHENTICATOR_AAID
    )

    /**
     * Provides the configuration provider.
     *
     * @return The configuration provider.
     */
    @Provides
    @Singleton
    fun provideConfigurationProvider(): ConfigurationProvider =
        ConfigurationProviderImpl(
            Environment.AUTHENTICATION_CLOUD,
            provideAuthenticationCloudConfiguration(),
            provideAuthenticatorAllowlist()
        )
    //endregion

    //region Client
    /**
     * Provides the client provider.
     *
     * @return The client provider.
     */
    @Provides
    @Singleton
    fun provideClientProvider(): ClientProvider = ClientProviderImpl()
    //endregion

    //region Error Handling
    /**
     * Provides the error handler chain.
     *
     * @return The error handler chain.
     */
    @Provides
    @Singleton
    fun provideErrorHandlerChain(): ErrorHandlerChain = ErrorHandlerChainImpl()
    //endregion

    //region Logger
    /**
     * Provides the sdk logger.
     *
     * @return The sdk logger.
     */
    @Provides
    @Singleton
    fun provideSdkLogger(): SdkLogger = SdkLoggerImpl()
    //endregion

    //region Settings
    /**
     * Provides the application settings.
     *
     * @param context The Android [Context].
     * @return The application settings.
     */
    @Provides
    @Singleton
    fun provideSettings(@ApplicationContext context: Context): Settings = SettingsImpl(context)
    //endregion

    //region Validation
    /**
     * Provides the authenticator validator.
     *
     * @return The authenticator validator.
     */
    @Provides
    @Singleton
    fun provideAuthenticatorValidator(): AuthenticatorValidator = AuthenticatorValidatorImpl()

    /**
     * Provides the password policy.
     *
     * @param context The Android [Context].
     * @return The password policy.
     */
    @Provides
    fun providePasswordPolicy(@ApplicationContext context: Context): PasswordPolicy =
        PasswordPolicyImpl(context)
    //endregion

    //region Data Sources
    /**
     * Provides the login related data source.
     *
     * @param retrofit An instance of [Retrofit].
     * @return The login related data source.
     */
    @Provides
    fun provideLoginDataSource(retrofit: Retrofit): LoginDataSource =
        LoginDataSourceImpl(retrofit)
    //endregion


    //region Caches
    /**
     * Provides state cache for the PIN change operation.
     *
     * @return The state cache for the PIN change operation.
     */
    @Provides
    fun provideChangePinOperationStateCache(): Cache<ChangePinOperationState> =
        CacheImpl()

    /**
     * Provides state cache for the Password change operation.
     *
     * @return The state cache for the Password change operation.
     */
    @Provides
    fun provideChangePasswordOperationStateCache(): Cache<ChangePasswordOperationState> =
        CacheImpl()

    /**
     * Provides state cache for user interaction related operations.
     *
     * @return The state cache for user interaction related operations.
     */
    @Provides
    fun provideUserInteractionOperationStateCache(): Cache<UserInteractionOperationState> =
        CacheImpl()
    //endregion

    //region Repositories
    /**
     * Provides state repository for the PIN change operation.
     *
     * @param cache The state cache for the PIN change operation.
     * @return The state repository for the PIN change operation.
     */
    @Provides
    @Singleton
    fun provideChangePinOperationStateRepository(cache: Cache<ChangePinOperationState>): OperationStateRepository<ChangePinOperationState> =
        OperationStateRepositoryImpl(cache)

    /**
     * Provides state repository for the Password change operation.
     *
     * @param cache The state cache for the Password change operation.
     * @return The state repository for the Password change operation.
     */
    @Provides
    @Singleton
    fun provideChangePasswordOperationStateRepository(cache: Cache<ChangePasswordOperationState>): OperationStateRepository<ChangePasswordOperationState> =
        OperationStateRepositoryImpl(cache)

    /**
     * Provides state repository for user interaction related operations.
     *
     * @param cache The state cache for interaction related operations.
     * @return The state repository for user interaction related operations.
     */
    @Provides
    @Singleton
    fun provideUserInteractionOperationStateRepository(cache: Cache<UserInteractionOperationState>): OperationStateRepository<UserInteractionOperationState> =
        OperationStateRepositoryImpl(cache)

    /**
     * Provides repository for login feature.
     *
     * @param loginDataSource The login related data source implementation.
     * @return The repository for login feature.
     */
    @Provides
    fun provideLoginRepository(loginDataSource: LoginDataSource): LoginRepository =
        LoginRepositoryImpl(loginDataSource)
    //endregion

    //region Interaction
    /**
     * Provides the account selector.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The account selector.
     */
    @Provides
    fun provideAccountSelector(stateRepository: OperationStateRepository<UserInteractionOperationState>): AccountSelector =
        AccountSelectorImpl(stateRepository)

    /**
     * Provides the authenticator selector for registration operation.
     *
     * @param configurationProvider An instance of a [ConfigurationProvider] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param authenticatorValidator An instance of an [AuthenticatorValidator] interface implementation.
     * @param settings An instance of a [Settings] interface implementation.
     * @return The authenticator selector.
     */
    @Provides
    @Named(REGISTRATION_AUTHENTICATOR_SELECTOR)
    fun provideRegistrationAuthenticatorSelector(
        configurationProvider: ConfigurationProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        authenticatorValidator: AuthenticatorValidator,
        settings: Settings,
    ): AuthenticatorSelector =
        AuthenticatorSelectorImpl(
            configurationProvider,
            stateRepository,
            authenticatorValidator,
            settings,
            AuthenticatorSelectorOperation.REGISTRATION
        )

    /**
     * Provides the authenticator selector for authentication operation.
     *
     * @param configurationProvider An instance of a [ConfigurationProvider] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param authenticatorValidator An instance of an [AuthenticatorValidator] interface implementation.
     * @param settings An instance of a [Settings] interface implementation.
     * @return The authenticator selector.
     */
    @Provides
    @Named(AUTHENTICATION_AUTHENTICATOR_SELECTOR)
    fun provideAuthenticationAuthenticatorSelector(
        configurationProvider: ConfigurationProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        authenticatorValidator: AuthenticatorValidator,
        settings: Settings,
    ): AuthenticatorSelector =
        AuthenticatorSelectorImpl(
            configurationProvider,
            stateRepository,
            authenticatorValidator,
            settings,
            AuthenticatorSelectorOperation.AUTHENTICATION
        )

    /**
     * Provides the PIN changer.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePinOperationState].
     * @return The PIN changer.
     */
    @Provides
    fun providePinChanger(stateRepository: OperationStateRepository<ChangePinOperationState>): PinChanger =
        PinChangerImpl(stateRepository)

    /**
     * Provides the PIN user verifier.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The PIN user verifier.
     */
    @Provides
    fun providePinUserVerifier(stateRepository: OperationStateRepository<UserInteractionOperationState>): PinUserVerifier =
        PinUserVerifierImpl(stateRepository)

    /**
     * Provides the PIN enroller.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The PIN enroller.
     */
    @Provides
    fun providePinEnroller(stateRepository: OperationStateRepository<UserInteractionOperationState>): PinEnroller =
        PinEnrollerImpl(stateRepository)

    /**
     * Provides the Password changer.
     *
     * @param passwordPolicy An instance of [PasswordPolicy] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePasswordOperationState].
     * @return The Password changer.
     */
    @Provides
    fun providePasswordChanger(
        passwordPolicy: PasswordPolicy,
        stateRepository: OperationStateRepository<ChangePasswordOperationState>
    ): PasswordChanger =
        PasswordChangerImpl(passwordPolicy, stateRepository)

    /**
     * Provides the Password enroller.
     *
     * @param passwordPolicy An instance of [PasswordPolicy] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The Password enroller.
     */
    @Provides
    fun providePasswordEnroller(
        passwordPolicy: PasswordPolicy,
        stateRepository: OperationStateRepository<UserInteractionOperationState>
    ): PasswordEnroller =
        PasswordEnrollerImpl(passwordPolicy, stateRepository)

    /**
     * Provides the Password user verifier.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The Password user verifier.
     */
    @Provides
    fun providePasswordUserVerifier(stateRepository: OperationStateRepository<UserInteractionOperationState>): PasswordUserVerifier =
        PasswordUserVerifierImpl(stateRepository)

    /**
     * Provides the fingerprint user verifier.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The fingerprint user verifier.
     */
    @Provides
    fun provideFingerprintUserVerifier(stateRepository: OperationStateRepository<UserInteractionOperationState>): FingerprintUserVerifier =
        FingerprintUserVerifierImpl(stateRepository)

    /**
     * Provides the biometric user verifier.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The biometric user verifier.
     */
    @Provides
    fun provideBiometricUserVerifier(stateRepository: OperationStateRepository<UserInteractionOperationState>): BiometricUserVerifier =
        BiometricUserVerifierImpl(stateRepository)

    /**
     * Provides the device passcode user verifier.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The device passcode user verifier.
     */
    @Provides
    fun provideDevicePasscodeUserVerifier(stateRepository: OperationStateRepository<UserInteractionOperationState>): DevicePasscodeUserVerifier =
        DevicePasscodeUserVerifierImpl(stateRepository)

    /**
     * Provides the [Consumer] for successful authentication that accepts an [AuthorizationProvider] object.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The [Consumer] for for successful authentication that accepts an [AuthorizationProvider]
     *  object.
     */
    @Provides
    fun provideOnSuccessAuthentication(stateRepository: OperationStateRepository<UserInteractionOperationState>): Consumer<AuthorizationProvider> =
        OnSuccessAuthenticationImpl(stateRepository)

    /**
     * Provides the [Consumer] for successful authentication during deregistration that accepts an
     * [AuthorizationProvider] object.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The [Consumer] for successful authentication during Deregistration operation.
     */
    @Provides
    fun provideOnSuccessAuthenticationForDeregistration(stateRepository: OperationStateRepository<UserInteractionOperationState>): Consumer<AuthorizationProvider> =
        OnSuccessAuthenticationImpl(stateRepository, Operation.DEREGISTRATION)

    /**
     * Provides the [Runnable] implementation for successful user interaction operations.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The [Runnable] implementation for successful user interaction operations.
     */
    @Provides
    fun provideOnSuccessForUserInteractionOperation(stateRepository: OperationStateRepository<UserInteractionOperationState>): Runnable =
        OnSuccessImpl(stateRepository)

    /**
     * Provides the [Runnable] for successful PIN change operation.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePinOperationState].
     * @return The [Runnable] for successful PIN change operation.
     */
    @Provides
    fun provideOnSuccessForChangePinOperation(stateRepository: OperationStateRepository<ChangePinOperationState>): Runnable =
        OnSuccessImpl(stateRepository)

    /**
     * Provides the [Runnable] for successful password change operation.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePasswordOperationState].
     * @return The [Runnable] for successful password change operation.
     */
    @Provides
    fun provideOnSuccessForChangePasswordOperation(stateRepository: OperationStateRepository<ChangePasswordOperationState>): Runnable =
        OnSuccessImpl(stateRepository)

    /**
     * Provides the [Consumer] that accepts an [OperationError] object.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param errorHandlerChain An instance of [ErrorHandlerChain] interface implementation.
     * @return The [Consumer] that accepts an [OperationError] object.
     */
    @Provides
    fun provideOnErrorForUserInteractionOperation(
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        errorHandlerChain: ErrorHandlerChain
    ): Consumer<OperationError> = OnErrorImpl(stateRepository, errorHandlerChain)

    /**
     * Provides the [Consumer] that accepts a [PinChangeError] object.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePinOperationState].
     * @param errorHandlerChain An instance of [ErrorHandlerChain] interface implementation.
     * @return The [Consumer] that accepts a [PinChangeError] object.
     */
    @Provides
    fun provideOnErrorForChangePinOperation(
        stateRepository: OperationStateRepository<ChangePinOperationState>,
        errorHandlerChain: ErrorHandlerChain
    ): Consumer<PinChangeError> = OnErrorImpl(stateRepository, errorHandlerChain)

    /**
     * Provides the [Consumer] that accepts a [PasswordChangeError] object.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePasswordOperationState].
     * @param errorHandlerChain An instance of [ErrorHandlerChain] interface implementation.
     * @return The [Consumer] that accepts a [PasswordChangeError] object.
     */
    @Provides
    fun provideOnErrorForChangePasswordOperation(
        stateRepository: OperationStateRepository<ChangePasswordOperationState>,
        errorHandlerChain: ErrorHandlerChain
    ): Consumer<PasswordChangeError> = OnErrorImpl(stateRepository, errorHandlerChain)

    /**
     * Provides the [Consumer] that accepts an [AuthCloudApiError] object.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param errorHandlerChain An instance of [ErrorHandlerChain] interface implementation.
     * @return The [Consumer] that accepts an [AuthCloudApiError] object.
     */
    @Provides
    fun provideOnAuthCloudApiError(
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        errorHandlerChain: ErrorHandlerChain
    ): Consumer<AuthCloudApiError> = OnErrorImpl(stateRepository, errorHandlerChain)

    /**
     * Provides the [Consumer] that accepts an [AuthenticationError] object.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param errorHandlerChain An instance of [ErrorHandlerChain] interface implementation.
     * @return The [Consumer] that accepts an [AuthenticationError] object.
     */
    @Provides
    fun provideOnAuthenticationError(
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        errorHandlerChain: ErrorHandlerChain
    ): Consumer<AuthenticationError> = OnErrorImpl(stateRepository, errorHandlerChain)
    //endregion

    //region Use-cases
    /**
     * Provides use case for [ch.nevis.mobile.sdk.api.MobileAuthenticationClient] initialization.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @param context An Android [Context] object used for initializing [ch.nevis.mobile.sdk.api.MobileAuthenticationClient].
     * @return The use case for initializing the client.
     */
    @Provides
    @Singleton
    fun provideInitializeClientUseCase(
        clientProvider: ClientProvider,
        @ApplicationContext context: Context
    ): InitializeClientUseCase =
        InitializeClientUseCaseImpl(
            clientProvider,
            context
        )

    /**
     * Provides use case for retrieving the registered accounts.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @return The use case for retrieving the registered accounts.
     */
    @Provides
    fun provideGetAccountsUseCase(clientProvider: ClientProvider): GetAccountsUseCase =
        GetAccountsUseCaseImpl(clientProvider)

    /**
     * Provides use case for retrieving the authenticators.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @return The use case for retrieving the authenticators.
     */
    @Provides
    fun provideGetAuthenticatorsUseCase(clientProvider: ClientProvider): GetAuthenticatorsUseCase =
        GetAuthenticatorsUseCaseImpl(clientProvider)

    /**
     * Provides use case for delete local authenticators.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @return The use case for delete local authenticators.
     */
    @Provides
    fun provideDeleteAuthenticatorsUseCase(clientProvider: ClientProvider): DeleteAuthenticatorsUseCase =
        DeleteAuthenticatorsUseCaseImpl(clientProvider)

    /**
     * Provides use case for creating a new [ch.nevis.mobile.sdk.api.localdata.DeviceInformation] object.
     *
     * @param context The Android [Context].
     * @return The use case for creating a new [ch.nevis.mobile.sdk.api.localdata.DeviceInformation] object.
     */
    @Provides
    fun provideCreateDeviceInformationUseCase(@ApplicationContext context: Context): CreateDeviceInformationUseCase =
        CreateDeviceInformationUseCaseImpl(context)

    /**
     * Provides use case for retrieving the device information stored by the client.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @return The use case for retrieving the device information stored by the client.
     */
    @Provides
    fun provideGetDeviceInformationUseCase(clientProvider: ClientProvider): GetDeviceInformationUseCase =
        GetDeviceInformationUseCaseImpl(clientProvider)

    /**
     * Provides use case for starting a change device information operation.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @return The use case for starting a change device information operation.
     */
    @Provides
    fun provideChangeDeviceInformationUseCase(
        clientProvider: ClientProvider
    ): ChangeDeviceInformationUseCase = ChangeDeviceInformationUseCaseImpl(clientProvider)

    /**
     * Provides use case for transaction confirmation.
     *
     * @return The use case for transaction confirmation.
     */
    @Provides
    fun provideTransactionConfirmationUseCase(): TransactionConfirmationUseCase = TransactionConfirmationUseCaseImpl()

    /**
     * Provides use case for starting an in-band authentication operation.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @param stateRepository n instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param authenticatorSelector An instance of [AuthenticatorSelector] interface implementation.
     * @param pinUserVerifier An instance of [PinUserVerifier] interface implementation.
     * @param passwordUserVerifier An instance of [PasswordUserVerifier] interface implementation.
     * @param fingerprintUserVerifier An instance of [FingerprintUserVerifier] interface implementation.
     * @param biometricUserVerifier An instance of [BiometricUserVerifier] interface implementation.
     * @param devicePasscodeUserVerifier An instance of [DevicePasscodeUserVerifier] interface implementation.
     * @param onError An instance of a [Consumer] implementation that accepts an [AuthenticationError] object.
     * @return The use case for starting an in-band authentication operation.
     */
    @Provides
    @Named(IN_BAND_AUTHENTICATION_USE_CASE_DEFAULT)
    fun provideInBandAuthenticationUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        @Named(AUTHENTICATION_AUTHENTICATOR_SELECTOR)
        authenticatorSelector: AuthenticatorSelector,
        pinUserVerifier: PinUserVerifier,
        passwordUserVerifier: PasswordUserVerifier,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        devicePasscodeUserVerifier: DevicePasscodeUserVerifier,
        onError: Consumer<AuthenticationError>
    ): InBandAuthenticationUseCase = InBandAuthenticationUseCaseImpl(
        clientProvider,
        stateRepository,
        authenticatorSelector,
        pinUserVerifier,
        passwordUserVerifier,
        fingerprintUserVerifier,
        biometricUserVerifier,
        devicePasscodeUserVerifier,
        provideOnSuccessAuthentication(stateRepository),
        onError
    )

    /**
     * Provides use case for in-band authentication during deregistration.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param authenticatorSelector An instance of [AuthenticatorSelector] interface implementation.
     * @param pinUserVerifier An instance of [PinUserVerifier] interface implementation.
     * @param passwordUserVerifier An instance of [PasswordUserVerifier] interface implementation.
     * @param fingerprintUserVerifier An instance of [FingerprintUserVerifier] interface implementation.
     * @param biometricUserVerifier An instance of [BiometricUserVerifier] interface implementation.
     * @param devicePasscodeUserVerifier An instance of [DevicePasscodeUserVerifier] interface implementation.
     * @param onError An instance of a [Consumer] implementation that accepts an [AuthenticationError] object.
     * @return The use case for in-band authentication during deregistration.
     */
    @Provides
    @Named(IN_BAND_AUTHENTICATION_USE_CASE_FOR_DEREGISTRATION)
    fun provideInBandAuthenticationUseCaseForDeregistration(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        @Named(AUTHENTICATION_AUTHENTICATOR_SELECTOR)
        authenticatorSelector: AuthenticatorSelector,
        pinUserVerifier: PinUserVerifier,
        passwordUserVerifier: PasswordUserVerifier,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        devicePasscodeUserVerifier: DevicePasscodeUserVerifier,
        onError: Consumer<AuthenticationError>
    ): InBandAuthenticationUseCase = InBandAuthenticationUseCaseImpl(
        clientProvider,
        stateRepository,
        authenticatorSelector,
        pinUserVerifier,
        passwordUserVerifier,
        fingerprintUserVerifier,
        biometricUserVerifier,
        devicePasscodeUserVerifier,
        provideOnSuccessAuthenticationForDeregistration(stateRepository),
        onError
    )

    /**
     * Provides use case for selecting an account during an out-of-band authentication.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for selecting an account during an out-of-band authentication.
     */
    @Provides
    fun provideSelectAccountUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): SelectAccountUseCase =
        SelectAccountUseCaseImpl(stateRepository)

    /**
     * Provides use case for selecting an authenticator during an authentication or registration operation.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for selecting an authenticator during an authentication or registration operation.
     */
    @Provides
    fun provideSelectAuthenticatorUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): SelectAuthenticatorUseCase =
        SelectAuthenticatorUseCaseImpl(stateRepository)

    /**
     * Provides use case for setting a new PIN during a registration operation.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for setting a new PIN during a registration operation.
     */
    @Provides
    fun provideSetPinUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): SetPinUseCase =
        SetPinUseCaseImpl(stateRepository)

    /**
     * Provides use case for starting a change PIN operation.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePinOperationState].
     * @param pinChanger An instance of [PinChanger] interface implementation.
     * @param onError An instance of a [Consumer] implementation that accepts a [PinChangeError] object.
     * @return The use case for starting a change PIN operation.
     */
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

    /**
     * Provides use case for completing change PIN operation.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePinOperationState].
     * @return The use case for completing change PIN operation.
     */
    @Provides
    fun provideChangePinUseCase(stateRepository: OperationStateRepository<ChangePinOperationState>): ChangePinUseCase =
        ChangePinUseCaseImpl(stateRepository)

    /**
     * Provides use case for verifying the user using PIN authenticator.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for verifying the user using PIN authenticator.
     */
    @Provides
    fun provideVerifyPinUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): VerifyPinUseCase =
        VerifyPinUseCaseImpl(stateRepository)

    /**
     * Provides use case for setting a new password during a registration operation.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for setting a new password during a registration operation.
     */
    @Provides
    fun provideSetPasswordUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): SetPasswordUseCase =
        SetPasswordUseCaseImpl(stateRepository)

    /**
     * Provides use case for starting a change Password operation.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [ChangePasswordOperationState].
     * @param passwordChanger An instance of [PasswordChanger] interface implementation.
     * @param onError An instance of a [Consumer] implementation that accepts a [PasswordChangeError] object.
     * @return The use case for starting a change Password operation.
     */
    @Provides
    fun provideStartChangePasswordUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<ChangePasswordOperationState>,
        passwordChanger: PasswordChanger,
        onError: Consumer<PasswordChangeError>
    ): StartChangePasswordUseCase = StartChangePasswordUseCaseImpl(
        clientProvider,
        stateRepository,
        passwordChanger,
        provideOnSuccessForChangePasswordOperation(stateRepository),
        onError
    )

    /**
     * Provides use case for completing change Password operation.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may
     *  hold a [ChangePasswordOperationState].
     * @return The use case for completing change Password operation.
     */
    @Provides
    fun provideChangePasswordUseCase(stateRepository: OperationStateRepository<ChangePasswordOperationState>): ChangePasswordUseCase =
        ChangePasswordUseCaseImpl(stateRepository)

    /**
     * Provides use case for verifying the user using Password authenticator.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for verifying the user using Password authenticator.
     */
    @Provides
    fun provideVerifyPasswordUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): VerifyPasswordUseCase =
        VerifyPasswordUseCaseImpl(stateRepository)

    /**
     * Provides use case for verifying the user using fingerprint authenticator.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for verifying the user using fingerprint authenticator.
     */
    @Provides
    fun provideVerifyFingerprintUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): VerifyFingerprintUseCase =
        VerifyFingerprintUseCaseImpl(stateRepository)

    /**
     * Provides use case for verifying the user using biometric authenticator.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for verifying the user using biometric authenticator.
     */
    @Provides
    fun provideVerifyBiometricUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): VerifyBiometricUseCase =
        VerifyBiometricUseCaseImpl(stateRepository)

    /**
     * Provides use case for verifying the user using device passcode authenticator.
     *
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @return The use case for verifying the user using device passcode authenticator.
     */
    @Provides
    fun provideVerifyDevicePasscodeUseCase(stateRepository: OperationStateRepository<UserInteractionOperationState>): VerifyDevicePasscodeUseCase =
        VerifyDevicePasscodeUseCaseImpl(stateRepository)

    /**
     * Provides use case for starting a process out-of-band payload operation.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param createDeviceInformationUseCase An instance of [CreateDeviceInformationUseCase] interface implementation.
     * @param accountSelector An instance of [AccountSelector] interface implementation.
     * @param registrationAuthenticatorSelector An instance of [AuthenticatorSelector] interface implementation
     *  for Registration operation.
     * @param authenticationAuthenticatorSelector An instance of [AuthenticatorSelector] interface implementation
     *  for Authentication operation.
     * @param pinEnroller An instance of [PinEnroller] interface implementation.
     * @param passwordEnroller An instance of [PasswordEnroller] interface implementation.
     * @param pinUserVerifier An instance of [PinUserVerifier] interface implementation.
     * @param passwordUserVerifier An instance of [PasswordUserVerifier] interface implementation.
     * @param fingerprintUserVerifier An instance of [FingerprintUserVerifier] interface implementation.
     * @param biometricUserVerifier An instance of [BiometricUserVerifier] interface implementation.
     * @param devicePasscodeUserVerifier An instance of [DevicePasscodeUserVerifier] interface implementation.
     * @param onError An instance of a [Consumer] implementation that accepts an [OperationError] object.
     * @return The use case for starting a process out-of-band payload operation.
     */
    @Provides
    fun provideProcessOutOfBandPayloadUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        createDeviceInformationUseCase: CreateDeviceInformationUseCase,
        accountSelector: AccountSelector,
        @Named(REGISTRATION_AUTHENTICATOR_SELECTOR)
        registrationAuthenticatorSelector: AuthenticatorSelector,
        @Named(AUTHENTICATION_AUTHENTICATOR_SELECTOR)
        authenticationAuthenticatorSelector: AuthenticatorSelector,
        pinEnroller: PinEnroller,
        passwordEnroller: PasswordEnroller,
        pinUserVerifier: PinUserVerifier,
        passwordUserVerifier: PasswordUserVerifier,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        devicePasscodeUserVerifier: DevicePasscodeUserVerifier,
        onError: Consumer<OperationError>
    ): ProcessOutOfBandPayloadUseCase = ProcessOutOfBandPayloadUseCaseImpl(
        clientProvider,
        stateRepository,
        createDeviceInformationUseCase,
        accountSelector,
        registrationAuthenticatorSelector,
        authenticationAuthenticatorSelector,
        pinEnroller,
        passwordEnroller,
        pinUserVerifier,
        passwordUserVerifier,
        fingerprintUserVerifier,
        biometricUserVerifier,
        devicePasscodeUserVerifier,
        provideOnSuccessAuthentication(stateRepository),
        provideOnSuccessForUserInteractionOperation(stateRepository),
        onError
    )

    /**
     * Provides use case for decoding an out-of-band payload.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @return The use case for decoding an out-of-band payload.
     */
    @Provides
    fun provideDecodePayloadUseCase(
        clientProvider: ClientProvider
    ): DecodePayloadUseCase =
        DecodePayloadUseCaseImpl(clientProvider)

    /**
     * Provides use case for deregister registered accounts, authenticators.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @return The use case for deregister registered accounts, authenticators.
     */
    @Provides
    fun provideDeregisterUseCase(clientProvider: ClientProvider): DeregisterUseCase =
        DeregisterUseCaseImpl(clientProvider)

    /**
     * Provides use case for starting an Auth Cloud API registration operation.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param createDeviceInformationUseCase An instance of [CreateDeviceInformationUseCase] interface implementation.
     * @param authenticatorSelector An instance of [AuthenticatorSelector] interface implementation
     *  for Registration operation.
     * @param pinEnroller An instance of [PinEnroller] interface implementation.
     * @param passwordEnroller An instance of [PasswordEnroller] interface implementation.
     * @param fingerprintUserVerifier An instance of [FingerprintUserVerifier] interface implementation.
     * @param biometricUserVerifier An instance of [BiometricUserVerifier] interface implementation.
     * @param devicePasscodeUserVerifier An instance of [DevicePasscodeUserVerifier] interface implementation.
     * @param onError An instance of a [Consumer] implementation that accepts an [AuthCloudApiError] object.
     * @return The use case for starting an Auth Cloud API registration operation.
     */
    @Provides
    fun provideAuthCloudApiRegistrationUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        createDeviceInformationUseCase: CreateDeviceInformationUseCase,
        @Named(REGISTRATION_AUTHENTICATOR_SELECTOR)
        authenticatorSelector: AuthenticatorSelector,
        pinEnroller: PinEnroller,
        passwordEnroller: PasswordEnroller,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        devicePasscodeUserVerifier: DevicePasscodeUserVerifier,
        onError: Consumer<AuthCloudApiError>
    ): AuthCloudApiRegistrationUseCase = AuthCloudApiRegistrationUseCaseImpl(
        clientProvider,
        stateRepository,
        createDeviceInformationUseCase,
        authenticatorSelector,
        pinEnroller,
        passwordEnroller,
        fingerprintUserVerifier,
        biometricUserVerifier,
        devicePasscodeUserVerifier,
        provideOnSuccessForUserInteractionOperation(stateRepository),
        onError
    )

    /**
     * Provides use case for login.
     *
     * @param loginRepository An instance of a [LoginRepository] implementation that is used for the
     *  login process.
     * @return The use case for login.
     */
    @Provides
    fun provideLoginUseCase(loginRepository: LoginRepository): LoginUseCase =
        LoginUseCaseImpl(loginRepository)

    /**
     * Provides use case for starting an in-band registration operation.
     *
     * @param clientProvider An instance of [ClientProvider] interface implementation.
     * @param stateRepository An instance of an [OperationStateRepository] implementation that may hold
     *  a [UserInteractionOperationState].
     * @param createDeviceInformationUseCase An instance of [CreateDeviceInformationUseCase] interface implementation.
     * @param authenticatorSelector An instance of [AuthenticatorSelector] interface implementation
     *  for Registration operation.
     * @param pinEnroller An instance of [PinEnroller] interface implementation.
     * @param passwordEnroller An instance of [PasswordEnroller] interface implementation.
     * @param fingerprintUserVerifier An instance of [FingerprintUserVerifier] interface implementation.
     * @param biometricUserVerifier An instance of [BiometricUserVerifier] interface implementation.
     * @param devicePasscodeUserVerifier An instance of [DevicePasscodeUserVerifier] interface implementation.
     * @param onError An instance of a [Consumer] implementation that accepts an [OperationError] object.
     * @return The use case for starting an in-band registration operation.
     */
    @Provides
    fun provideInBandRegistrationUseCase(
        clientProvider: ClientProvider,
        stateRepository: OperationStateRepository<UserInteractionOperationState>,
        createDeviceInformationUseCase: CreateDeviceInformationUseCase,
        @Named(REGISTRATION_AUTHENTICATOR_SELECTOR)
        authenticatorSelector: AuthenticatorSelector,
        pinEnroller: PinEnroller,
        passwordEnroller: PasswordEnroller,
        fingerprintUserVerifier: FingerprintUserVerifier,
        biometricUserVerifier: BiometricUserVerifier,
        devicePasscodeUserVerifier: DevicePasscodeUserVerifier,
        onError: Consumer<OperationError>
    ): InBandRegistrationUseCase = InBandRegistrationUseCaseImpl(
        clientProvider,
        stateRepository,
        createDeviceInformationUseCase,
        authenticatorSelector,
        pinEnroller,
        passwordEnroller,
        fingerprintUserVerifier,
        biometricUserVerifier,
        devicePasscodeUserVerifier,
        provideOnSuccessForUserInteractionOperation(stateRepository),
        onError
    )

    /**
     * Provides use case for cancelling currently running operation.
     *
     * @param userInteractionOperationStateRepository An instance of an [OperationStateRepository]
     *  implementation that may hold an [UserInteractionOperationState].
     * @param changePinOperationStateRepository An instance of an [OperationStateRepository]
     *  implementation that may hold an [ChangePinOperationState].
     * @param changePasswordOperationStateRepository An instance of an [OperationStateRepository]
     *  implementation that may hold an [ChangePasswordOperationState].
     * @return The use case for cancelling currently running operation.
     */
    @Provides
    fun provideCancelOperationUseCase(
        userInteractionOperationStateRepository: OperationStateRepository<UserInteractionOperationState>,
        changePinOperationStateRepository: OperationStateRepository<ChangePinOperationState>,
        changePasswordOperationStateRepository: OperationStateRepository<ChangePasswordOperationState>
    ): CancelOperationUseCase = CancelOperationUseCaseImpl(
        userInteractionOperationStateRepository,
        changePinOperationStateRepository,
        changePasswordOperationStateRepository
    )

    /**
     * Provides use case for finishing any previously started operation.
     *
     * @param userInteractionOperationStateRepository An instance of an [OperationStateRepository]
     *  implementation that may hold an [UserInteractionOperationState].
     * @param changePinOperationStateRepository An instance of an [OperationStateRepository] implementation
     *  that may hold an [ChangePinOperationState].
     * @param changePasswordOperationStateRepository An instance of an [OperationStateRepository]
     *  implementation that may hold an [ChangePasswordOperationState].
     * @return The use case for finishing any previously started operation.
     */
    @Provides
    fun provideFinishOperationUseCase(
        userInteractionOperationStateRepository: OperationStateRepository<UserInteractionOperationState>,
        changePinOperationStateRepository: OperationStateRepository<ChangePinOperationState>,
        changePasswordOperationStateRepository: OperationStateRepository<ChangePasswordOperationState>
    ): FinishOperationUseCase = FinishOperationUseCaseImpl(
        userInteractionOperationStateRepository,
        changePinOperationStateRepository,
        changePasswordOperationStateRepository
    )
    //endregion

    //region Retrofit
    /**
     * Provides the [Retrofit] implementation.
     *
     * @param configurationProvider An instance of a [ConfigurationProvider] interface implementation.
     * @return The [Retrofit] implementation.
     */
    @Provides
    fun provideRetrofit(configurationProvider: ConfigurationProvider): Retrofit =
        Retrofit.Builder().baseUrl(configurationProvider.configuration.baseUrl().toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    //endregion
}
