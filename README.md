![Nevis Logo](https://www.nevis.net/hubfs/Nevis/images/logotype.svg)

# Nevis Mobile Authentication SDK Android Example App

[![Main Branch Commit](https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-android-coroutines/actions/workflows/main.yml/badge.svg)](https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-android-coroutines/actions/workflows/main.yml)
[![Verify Pull Request](https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-android-coroutines/actions/workflows/pr.yml/badge.svg)](https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-android-coroutines/actions/workflows/pr.yml)

This repository contains the example app demonstrating how to use the Nevis Mobile Authentication SDK in an Android mobile application.
The Nevis Mobile Authentication SDK allows you to integrate passwordless authentication to your existing mobile app, backed by the FIDO UAF 1.1 Standard.

Some SDK features demonstrated in this example app are:

- Using the SDK with the Nevis Authentication Cloud
- Registering with QR code & app link URIs
- Simulating in-band authentication after registration
- Deregistering a registered account
- Changing the PIN of the PIN authenticator
- Changing the device information

Please note that the example app only demonstrates a subset of the available SDK features. The main purpose is to demonstrate how the SDK can be used, not to cover all supported scenarios.

## Getting Started

Before you start compiling and using the example applications please ensure you have the following ready:

### Server-side Environment

- In case you are planning to use [Authentication Cloud](https://docs.nevis.net/authcloud/) you need an _Authentication Cloud_ instance provided by Nevis and an [access key](https://docs.nevis.net/authcloud/access-app/access-key) to use it.
- In case you are planning to use an [Identity Suite](https://docs.nevis.net/nevislifetimesupport/) environment ensure that the environment is up and running and you have all necessary URLs and permissions to access it.

### Development Setup

- Android 6 or later, with API level 23
- Android 10 or later, with API level 29, for the biometric authenticator to work
- Android 11 or later, with API level 30, for the device passcode authenticator to work
- Android Studio 3.5.1 or later
- JDK 11
- Gradle 7.2 or later

### Github Account

SDK dependency used by this project are provided via [GitHub Packages](https://github.com/nevissecurity/nevis-mobile-authentication-sdk-android-package) that is used as a **Maven** repository. To access **GitHub Packages** a valid **GitHub** account and a **Personal Access Token** is needed. If you have not done it yet, please create a **Personal Access Token** with **Packages Read** permission. Once the **Personal Access Token** is created add the following properties to your global `gradle.properties` file (e.g.: `/Users/<YOUR USERNAME>/.gradle/gradle.properties`).

```properties
GITHUB_USERNAME=<YOUR USERNAME>
GITHUB_PERSONAL_ACCESS_TOKEN=<YOUR PERSONAL ACCESS TOKEN>
```

### Open the Project

Clone the example application GitHub repository and open it with Android Studio.

### Configuration

The example applications support two kinds of configuration: `authenticationCloud` and `identitySuite`. The following chapters describes how to change the base configuration to match your environment.
The configuration could be changed by modifying the [ApplicationModule](app/src/main/java/ch/nevis/exampleapp/coroutines/dagger/ApplicationModule.kt) file which describes the dependency injection related configuration using the [Dagger Hilt](https://dagger.dev/hilt/) library and the [AndroidManifest.xml](app/src/main/AndroidManifest.xml).

> **_NOTE_**
>
> Only _build-time_ configuration change is supported.

#### Authentication Cloud Configuration

Before being able to use the example application with your Authentication Cloud instance, you will need to update the configuration with the right host information.

Edit the [ApplicationModule](app/src/main/java/ch/nevis/exampleapp/coroutines/dagger/ApplicationModule.kt) file and replace the host name information with your Authentication Cloud instance in method `provideAuthenticationCloudConfiguration`.

#### Identity Suite Configuration

If you want to use `identitySuite` environment modify the configuration in method `provideIdentitySuiteConfiguration` and modify this part:

```kotlin
@Provides
@Singleton
fun provideConfigurationProvider(application: Application): ConfigurationProvider =
    ConfigurationProviderImpl(
        Environment.AUTHENTICATION_CLOUD,
        provideAuthenticationCloudConfiguration(application)
    )
```

to

```kotlin
@Provides
@Singleton
fun provideConfigurationProvider(application: Application): ConfigurationProvider =
    ConfigurationProviderImpl(
        Environment.IDENTITY_SUITE,
        provideIdentitySuiteConfiguration(application)
    )
```

#### Android Manifest XML

The example applications handle deep links and web links those contain a valid `dispatchTokenResponse` query parameter of an out-of-band operation. The related configuration located in the [AndroidManifest.xml](app/src/main/AndroidManifest.xml) for [MainActivity](app/src/main/java/ch/nevis/exampleapp/coroutines/ui/main/MainActivity.kt) with action `android.intent.action.VIEW`.

##### Web links

Change the `myinstance.mauth.nevis.cloud` host value in the following `intent-filter` with the right host information of your environment.

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />

    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />

    <data android:scheme="https" />
    <data android:host="myinstance.mauth.nevis.cloud" />
</intent-filter>
```

##### Deep links

Change the `myaccessapp` scheme value in the following `intent-filter` with the right scheme information of your environment.

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />

    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />

    <data android:scheme="myaccessapp" />
</intent-filter>
```

> **_NOTE_**:
>
> For more information about deep links, web links visit the official [Android guide](https://developer.android.com/training/app-links).

#### Facet ID

The FIDO server (i.e. nevisFIDO) must be configured with the facet ID(s) of your application(s). If the facet ID of your application is not referenced by the nevisFIDO configuration, the operations will fail with an **UNTRUSTED_FACET_ID** error.

By default the SDK assumes that the facet ID to be used is the one that follows the [FIDO UAF 1.1 Specifications](https://fidoalliance.org/specs/fido-uaf-v1.1-ps-20170202/fido-appid-and-facets-v1.1-ps-20170202.html#h4_determining-the-facetid-of-a-calling-application) that is the facet ID on Android should follow the `android:apk-key-hash:HASH_VALUE` format, where the `HASH_VALUE` is Base64 encoded SHA-256 hash of the APK signing certificate.

The facet ID can be calculated using the following code snippet:

```kotlin
import android.content.pm.PackageInfo
import android.util.Base64
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
object FacetIdCalculator {
	fun calculateFacetId(packageInfo: PackageInfo): String {
		val byteArrayInputStream = ByteArrayInputStream(packageInfo.signingInfo.signingCertificateHistory[0].toByteArray())
		val certificate = CertificateFactory.getInstance("X509").generateCertificate(byteArrayInputStream)
		val digest = MessageDigest.getInstance("SHA-256")
		return "android:apk-key-hash" + Base64.encodeToString(digest.digest(certificate.encoded), Base64.NO_PADDING or Base64.NO_WRAP)
	}
}
```

> **_NOTE_:**
>
> An alternative way to set a constant Facet ID is to call `facetId(String facetId)` method of `ch.nevis.mobile.sdk.api.Configuration.Builder` in methods `provideAuthenticationCloudConfiguration` and `provideIdentitySuiteConfiguration` in [ApplicationModule](app/src/main/java/ch/nevis/exampleapp/coroutines/dagger/ApplicationModule.kt) file.
>
> The value of the facet ID depends on the certificate used to build the application, which can change during the development, that is why this method has been introduced: by providing a constant facet ID and having
> it referenced in the server server configuration, temporary changes in the APK signing certificate do not require
> changes in the backend.
>
> This method must be used for development scenarios only. For production code do not invoke this method and configure the backend with the facet ID that can be calculated with the code snippet above. See the chapter _Application Facet ID and the nevisFIDO Backend Configuration_ of the SDK reference guide for more details.

### Build & run

Now you are ready to build and run the example app by choosing _Run > Run 'app'_ from Android Studio's menu or by clicking the Run button in your project’s toolbar.

If you want to build and install the application from the command-line from *nix systems, you can execute the following:

```
$ cd <example repository root>
$ ./gradlew installDebug
```

It will build the application and install it if there is an emulator running in your machine, or if a physical device with debug enabled is connected to your machine.

### Try it out

Now that the Android example app is up and running, it's time to try it out!

Check the [Using the Example Application](https://docs.nevis.net/mobilesdk/quickstart#using-the-example-application) of our quickstart guide for usage instructions.

## Used Components, Concepts

### Android Jetpack Navigation

The navigation between the views/fragments are handled and described using the [Android Jetpack Navigation](https://developer.android.com/guide/navigation) component. The Gradle plugin [Safe Args](https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args) is also used to pass data to navigation destination fragments with type safety.

### Dagger Hilt

Dependency Injection framework [Dagger Hilt](https://dagger.dev/hilt/) is used to inject an SDK instance and logger, view model, operation and delegate instances where necessary.

### MVVM

As this is the suggested and supported application architecture, [MVVM](https://developer.android.com/jetpack/guide) is used. Each view is implemented as a `androidx.fragment.app.Fragment` and each view has a `androidx.lifecycle.ViewModel` implementation if necessary.

### Kotlin

The application is written in [Kotlin](https://developer.android.com/kotlin).

### Kotlin coroutines

SDK operations are executed in async way using Kotlin [coroutines](https://developer.android.com/kotlin/coroutines), they are wrapped by and executed in `suspend` functions.

### Wrapping SDK Calls for Kotlin Coroutines

Nevis Mobile Authentication SDK Android library is a Java library. After a successful `MobileAuthenticationClient` initialization (for more details see next [chapter](#initialization)) the initialized client object can be obtained from `ClientProvider` and an operation can be executed by calling the proper method of the `MobileAuthenticationClient` and passing them the necessary input parameters. In the example application the default implementation of `ClientProvider` interface is injected by _Dagger_ into the use-case implementations.

E.g.: the next code snippet shows how to start an in-band authentication operation where

- `username` identifies the account we want to use for in-band authentication,
- `authenticatorSelector` is an implementation of `ch.nevis.mobile.sdk.api.operation.selection.AuthenticatorSelector` interaction callback interface that will be called by the SDK when authenticator selection is required to continue the operation,
- `pinUserVerifier` is an implementation of `ch.nevis.mobile.sdk.api.operation.userverification.PinUserVerifier` interaction callback interface that will be called by the SDK when PIN verification is required to continue the operation,
- `fingerprintUserVerifier` is an implementation of `ch.nevis.mobile.sdk.api.operation.userverification.FingerprintUserVerifier` interaction callback interface that will be called by the SDK when fingerprint verification is required to continue the operation,
- `biometricUserVerifier` is an implementation of `ch.nevis.mobile.sdk.api.operation.userverification.BiometricUserVerifier` interaction callback interface that will be called by the SDK when biometric verification is required to continue the operation,
- `onSuccess` is an implementation of `ch.nevis.mobile.sdk.api.util.Consumer` interaction callback interface that will be called by the SDK when the operation successfully completed,
- `onError` is an implementation of `ch.nevis.mobile.sdk.api.util.Consumer` interaction callback interface that will be called by the SDK when the operation stopped because an error occurred.

```kotlin
val client = clientProvider.get()
client?.operations().authentication()
    .username(username)
    .authenticatorSelector(authenticatorSelector)
    .pinUserVerifier(pinUserVerifier)
    .fingerprintUserVerifier(fingerprintUserVerifier)
    .biometricUserVerifier(biometricUserVerifier)
    .onSuccess(onSuccess)
    .onError(onError)
    .execute()
```

Because the SDK uses callback concept if we ran the code above in a coroutine scope then the process cannot be suspended, the call will return immediately and later a callback instance will be called by the SDK outside the coroutine scope and suspend function.
For Kotlin coroutines, suspend functions, SDK calls have to be wrapped into a [suspend cancellable coroutine](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/suspend-cancellable-coroutine.html).

The example application wraps the SDK calls in the use-case implementations in their `suspend fun execute(...)` functions. It runs the SDK call inside a `suspendCancellableCoroutine` block and caches the received `cancellableContinuation` object. When a interaction callback is called by the SDK the `cancellableContinuation` will be get from the cache and it will be resumed to resume the suspended block/function.

As an example check the [InBandAuthenticationUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/InBandAuthenticationUseCaseImpl.kt) and the [AuthenticationAuthenticatorSelectorImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/interaction/AuthenticationAuthenticatorSelectorImpl.kt).

## Integration Notes

In this section you can find hints about how the Nevis Mobile Authentication SDK is integrated into the example app.

- All SDK invocation is implemented as use-cases in the [usecase](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase) package.
- All SDK specific user interaction related protocol implementation can be found in the [handler](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/interaction) package.

### Initialization

The [InitializeClientUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/InitializeClientUseCaseImpl.kt) class is responsible for creating and initializing a `MobileAuthenticationClient` instance which is the entry point to the SDK. Later this instance can be used to start the different operations. The initialized `MobileAuthenticationClient` instance can be accessed via [ClientProviderImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/client/ClientProviderImpl.kt).

### Registration

Before being able to authenticate using the Nevis Mobile Authentication SDK, go through the registration process. Depending on the use-case, there are two types of registration: [in-app registration](#in-app-registration) and [out-of-band registration](#out-of-band-registration).

#### In-app registration

If the application is using a backend using the Nevis Authentication Cloud, the [AuthCloudApiRegistrationUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/AuthCloudApiRegistrationUseCaseImpl.kt) class will be used by passing the `enrollment` response or an `appLinkUri`.

When the backend used by the application does not use the Nevis Authentication Cloud the name of the user to be registered is passed to the [InBandRegistrationUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/InBandRegistrationUseCaseImpl.kt) class.
If authorization is required by the backend to register, provide an `AuthorizationProvider`. In the example app a `CookieAuthorizationProvider` is created from the cookies (see [LegacyLoginViewModel](app/src/main/java/ch/nevis/exampleapp/coroutines/ui/legacyLogin/LegacyLoginViewModel.kt) obtained by the [LoginUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/LoginUseCaseImpl.kt) class.

#### Out-of-band registration

When the registration is initiated in another device or application, the information required to process the operation is transmitted through a QR code or a link. After the payload obtained from the QR code or the link is decoded by [DecodePayloadUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/DecodePayloadUseCaseImpl.kt) the [ProcessOutOfBandPayloadUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/ProcessOutOfBandPayloadUseCaseImpl.kt) class processes the `OutOfBandPayload` starts the out-of-band operation.

### Authentication

Using the authentication operation, you can verify the identity of the user using an already registered authenticator. Depending on the use-case, there are two types of authentication: [in-app authentication](#in-app-authentication) and [out-of-band authentication](#out-of-band-authentication).

#### In-app authentication

For the application to trigger the authentication, the name of the user is provided to the [InBandAuthenticationUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/InBandAuthenticationUseCaseImpl.kt) class.

#### Out-of-band authentication

When the authentication is initiated in another device or application, the information required to process the operation is transmitted through a QR code or a link. After the payload obtained from the QR code or the link is decoded by [DecodePayloadUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/DecodePayloadUseCaseImpl.kt) the [ProcessOutOfBandPayloadUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/ProcessOutOfBandPayloadUseCaseImpl.kt) class starts the out-of-band operation.

#### Transaction confirmation

There are cases when specific information is to be presented to the user during the user verification process, known as transaction confirmation. The `AuthenticatorSelectionContext` and the `AccountSelectionContext` contain a byte array with the information. In the example app it is handled in the [AccountSelectorImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/interaction/AccountSelectorImpl.kt) class.

### Deregistration

The [DeregisterUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/DeregisterUseCaseImpl.kt) class is responsible for deregistering either a user or all of the registered users from the device.

### Other operations

#### Change PIN

The change PIN operation is implemented in the [StartChangePinUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/StartChangePinUseCaseImpl.kt) and [ChangePinUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/ChangePinUseCaseImpl.kt) classes with which you can modify the PIN of a registered PIN authenticator for a given user.

#### Change device information

During registration, the device information can be provided that contains the name identifying your device, and also the Firebase Cloud Messaging registration token. Updating both the name and the token is implemented in the [ChangeDeviceInformationUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/ChangeDeviceInformationUseCaseImpl.kt) class.

#### Get information

The following use cases are responsible for getting information with the help of `LocalData`:

- The [GetAccountsUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/GetAccountsUseCaseImpl.kt) class obtains the registered accounts.
- The [GetAuthenticatorsUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/GetAuthenticatorsUseCaseImpl.kt) class obtains the authenticator information.
- The [GetDeviceInformationUseCaseImpl](app/src/main/java/ch/nevis/exampleapp/coroutines/domain/usecase/GetDeviceInformationUseCaseImpl.kt) class obtains the device information.

© 2023 made with ❤ by Nevis
