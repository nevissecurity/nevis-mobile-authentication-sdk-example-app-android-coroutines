# Nevis Mobile Authentication SDK Android Example App (Kotlin Coroutines)

## Project Overview

A native Android (Kotlin) example app that shows how to integrate the Nevis Mobile Authentication SDK, a FIDO UAF 1.1 based SDK for passwordless authentication, **using Kotlin Coroutines**. It is meant as a learning resource and a source of integration code for customer apps. It is not a production-ready app.

Flows demonstrated:

- Client initialization (`MobileAuthenticationClientInitializer`)
- Registration
  - in-app with Authentication Cloud (`authCloudApiRegistration`, using an enrollment response or an `appLinkUri`)
  - in-app with an Identity Suite backend (`registration` with a `CookieAuthorizationProvider` after a username/password login)
  - out-of-band through a QR code, a deep link or pending operations
- Authentication: in-band, out-of-band (QR code / deep link), and transaction confirmation
- Deregistration of one account or all accounts, and deleting local authenticators
- Changing the PIN and the password
- Changing device information
- Fetching pending out-of-band operations
- Reading local data, `MetaData` (SDK version, facet ID, signing certificate) and device capabilities (FIDO UAF attestation support)

Paradigm: MVVM (Fragments + Jetpack `ViewModel`) on top of a **use case layer**. The SDK itself has a Java, callback-based API. This app wraps every SDK operation in a `suspend fun execute(...)` use case, built on `suspendCancellableCoroutine`, and ViewModels call these from `viewModelScope`. Dependency injection is done with Dagger Hilt. For a more direct, callback-style example with fewer layers, see the [plain Android example](https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-android).

The example covers only part of the SDK. For features that are not shown here, use the documentation at https://docs.nevis.net/mobilesdk. Don't infer SDK behavior from memory.

## SDK & Related Resources

How the SDK is consumed:

- Maven coordinates: `ch.nevis:nevis-mobile-authentication-sdk-android-debug`. It is declared as `debugImplementation` in `app/build.gradle`. The version comes from `NEVIS_MOBILE_AUTHENTICATION_SDK_VERSION` in `gradle.properties` (a dynamic `4.6.+` range).
- Repository: GitHub Packages at `https://maven.pkg.github.com/nevissecurity/nevis-mobile-authentication-sdk-android-package` (declared in the root `build.gradle`). It needs `GH_USERNAME` and `GH_PERSONAL_ACCESS_TOKEN` (a token with the `read:packages` scope) in `~/.gradle/gradle.properties`, `local.properties` or the environment.
- That public repository only hosts the **`debug` flavor**, which has no tamper guards (no emulator, debugger or root detection). Production apps must use the **`release` flavor** (the same artifact without the `-debug` suffix), which is distributed separately. See the [Android installation guide](https://docs.nevis.net/mobilesdk/guide/installation/android-installation) and [SDK flavors](https://docs.nevis.net/mobilesdk/guide/introduction#sdk-flavors).

Documentation (https://docs.nevis.net):

- [Quickstart: build the example and use it](https://docs.nevis.net/mobilesdk/quickstart)
- [Requirements](https://docs.nevis.net/mobilesdk/guide/requirements) and [device support](https://docs.nevis.net/mobilesdk/guide/device-support)
- [Configuration](https://docs.nevis.net/mobilesdk/guide/configuration) (Authentication Cloud, Identity Suite, custom backend, facet ID)
- [Initialization](https://docs.nevis.net/mobilesdk/guide/initialization)
- Operations: [registration](https://docs.nevis.net/mobilesdk/guide/operation/registration), [authentication](https://docs.nevis.net/mobilesdk/guide/operation/authentication), [deregistration](https://docs.nevis.net/mobilesdk/guide/operation/deregistration), [other operations](https://docs.nevis.net/mobilesdk/guide/operation/other-operations)
- [Error handling](https://docs.nevis.net/mobilesdk/guide/error-handling), [best practices](https://docs.nevis.net/mobilesdk/guide/best-practices), [troubleshooting](https://docs.nevis.net/mobilesdk/appendixes/troubleshooting)
- Integration scenarios: [Authentication Cloud](https://docs.nevis.net/mobilesdk/integration-scenarios/authentication-cloud), [Identity Suite](https://docs.nevis.net/mobilesdk/integration-scenarios/identity-suite)
- [Push support](https://docs.nevis.net/mobilesdk/guide/push_support) (not implemented in this example)
- [Android API reference (Javadoc)](https://docs.nevis.net/mobilesdk/api-references/javadoc/), [all API references](https://docs.nevis.net/mobilesdk/reference/api-references)
- [Android release notes](https://docs.nevis.net/mobilesdk/release_notes/android-release-notes)
- [Authentication Cloud documentation](https://docs.nevis.net/authcloud/)

Example apps on other platforms (same feature set, useful for comparison):

- Android, callback style: https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-android
- iOS: https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-ios
- iOS, RxSwift: https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-ios-rx
- Flutter: https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-flutter
- React Native: https://github.com/nevissecurity/nevis-mobile-authentication-sdk-example-app-react

## Prerequisites & Configuration

Backend: either a **Nevis Authentication Cloud** instance (with an access key to start enrollments), or an **Identity Suite** setup (nevisFIDO behind nevisProxy/nevisAuth). `README.md` § "Getting Started" explains how to set up either one.

All configuration is **build-time only**. To point the app at your own environment:

1. **Backend host.** Edit `app/src/main/java/ch/nevis/exampleapp/coroutines/dagger/ApplicationModule.kt`:
   - Authentication Cloud: replace `myinstance.mauth.nevis.cloud` in `provideAuthenticationCloudConfiguration()` (`Configuration.authCloudBuilder().hostname(...)`).
   - Identity Suite: set the host in `provideIdentitySuiteConfiguration()` (`Configuration.admin4PatternBuilder().hostname(...)`). Then switch `provideConfigurationProvider()` to `Environment.IDENTITY_SUITE` with `provideIdentitySuiteConfiguration()`. The constructor also takes `provideAuthenticatorAllowlist()` as its third argument; keep it there, even though the snippet in `README.md` omits it.
   - Custom endpoint paths: use `Configuration.builder()`. See [Configuration](https://docs.nevis.net/mobilesdk/guide/configuration).
2. **Identity Suite login (in-app registration only).** `data/retrofit/LoginEndPoint.kt` posts to `/auth/pwd` using nevisAuth form fields (`isiwebuserid` / `isiwebpasswd`). It is called through `LoginDataSource` → `LoginRepository` → `LoginUseCase`. Adapt it to your login flow. The base URL comes from the SDK `Configuration`.
3. **Deep links.** In `app/src/main/AndroidManifest.xml`, change the `myaccessapp` custom scheme in `MainActivity`'s `VIEW` intent filter to your scheme. Only URIs that carry a `dispatchTokenResponse` query parameter get processed (`MainActivity.processIntent` → Home screen → `OutOfBandOperationViewModel.decodeOutOfBandPayload`). For verified HTTPS App Links, add an `autoVerify` intent filter and host `assetlinks.json` on your domain.
4. **Facet ID.** nevisFIDO / Authentication Cloud must know the app's facet ID (`android:apk-key-hash:<base64 SHA-256 of the signing cert>`), or operations fail with `UNTRUSTED_FACET_ID`. The Home screen shows the current facet ID (from `MetaData`). Note that **every build type is signed with the local `debug` keystore**, so each developer machine produces a different facet ID. See [Facet ID](https://docs.nevis.net/mobilesdk/guide/configuration#facet-id).
5. **Package name.** `applicationId` / `namespace` is `ch.nevis.exampleapp.coroutines` (`app/build.gradle`). Change it when you adapt the app.
6. **Authenticators.** `provideAuthenticatorAllowlist()` in `ApplicationModule` sets which AAIDs (PIN, password, fingerprint, biometric, device passcode) can be offered. `res/values/settings.xml` → `allow_class2_sensors` controls `allowClass2Sensors` (face unlock on Class 2 devices).

## Build & Run Commands

`README.md` has the full setup (JDK, GitHub token, configuration). Quick reference:

```bash
./gradlew assembleDebug        # build
./gradlew installDebug         # build and install on a running emulator / connected device
./gradlew ktlintCheck          # Kotlin lint (rules in .editorconfig, version in ktlint-plugins.properties)
./gradlew ktlintFormat         # auto-fix most lint issues
./gradlew :dokkaGenerate       # KDoc → build/dokka
```

Requirements: JDK 17 and the Gradle wrapper in the repo. The build uses the Groovy DSL (`build.gradle`, `app/build.gradle`) without a version catalog; dependency versions are `ext` properties in the root `build.gradle`. If `GH_USERNAME` / `GH_PERSONAL_ACCESS_TOKEN` are missing, the build only prints a warning, and dependency resolution then fails. Only the **debug** variant builds: the SDK is a `debugImplementation` dependency, so release variants don't compile until you add the release flavor. The repo has no meaningful unit or instrumentation tests; to verify changes, run the app against a backend.

## Architecture

### Source layout (`app/src/main/java/ch/nevis/exampleapp/coroutines/`)

```
application/       ExampleApplication (@HiltAndroidApp, Timber setup)
dagger/            ApplicationModule: SDK Configuration, environment, allowlist, caches/repositories,
                   SDK interaction impls and every use case (DI)
common/
  configuration/   ConfigurationProvider(+Impl), Environment (AUTHENTICATION_CLOUD / IDENTITY_SUITE)
  error/           ErrorHandler + ErrorHandlerChain (cancel → default error screen)
  settings/        Settings (allowClass2Sensors from resources)
data/
  cache/           Cache(+Impl): in-memory holder for the current operation state
  repository/      OperationStateRepositoryImpl, LoginRepositoryImpl
  dataSource/, retrofit/, model/   Identity Suite login (demo only)
domain/
  client/          ClientProvider(+Impl): holds the initialized MobileAuthenticationClient
  usecase/         One use case per SDK step: suspend fun execute(...): Response
  interaction/     SDK callback implementations (selectors, verifiers, pin/, password/, OnSuccess*/OnErrorImpl);
                   they store the SDK handler in the operation state and resume the waiting coroutine
  model/state/     OperationState, UserInteractionOperationState, ChangePin/ChangePasswordOperationState
  model/response/  Response subtypes returned by use cases (e.g. SelectAuthenticatorResponse, EnrollPinResponse, ErrorResponse)
  repository/      OperationStateRepository, LoginRepository interfaces
  validation/      AuthenticatorValidator, PasswordPolicyImpl
  log/, util/      SDK log forwarding to the UI, authenticator helpers
ui/
  base/            OperationViewModel → CancellableOperationViewModel → OutOfBandOperationViewModel;
                   ResponseObserverFragment (maps Response → navigation)
  home/            HomeViewModel: init, accounts, metadata, pending ops, deregistration, PIN/password change
  qrReader/        CameraX + ML Kit QR scanning (demo scaffolding)
  authCloudRegistration/, legacyLogin/   in-app registration for each backend type
  selectAccount/, selectAuthenticator/, credential/, verifyUser/, transactionConfirmation/
  changeDeviceInformation/, result/, error/, main/ (MainActivity, deep link intake, on-screen log)
```

Navigation lives in `app/src/main/res/navigation/` (Jetpack Navigation + Safe Args).

### How SDK callbacks become coroutines

This is the key pattern of the repo. `README.md` § "Wrapping SDK Calls for Kotlin Coroutines" describes it too.

1. A ViewModel calls a use case from `viewModelScope`, for example `InBandAuthenticationUseCaseImpl.execute(username)`.
2. The use case creates an operation state (`UserInteractionOperationState`), stores the coroutine's `CancellableContinuation` in it, saves it in `OperationStateRepository`, starts the SDK operation, and suspends.
3. When the SDK calls an interaction callback (for example `AuthenticatorSelectorImpl.selectAuthenticator`), the callback stores the SDK **handler** in the state and **resumes the continuation** with a `Response` (for example `SelectAuthenticatorResponse`).
4. The ViewModel posts the `Response` to `responseLiveData`. `ResponseObserverFragment.processResponse` navigates to the matching screen.
5. The user's input goes to the next use case (for example `SelectAuthenticatorUseCaseImpl.execute(aaid)`). It saves a **new** continuation in the state, then calls the stored handler (`handler.aaid(aaid)`), and the cycle repeats.
6. `OnSuccessImpl` / `OnErrorImpl` resume the last continuation with `CompletedResponse` / `ErrorResponse` and reset the state.

Other details:

- **Three state types and repositories**: user interaction operations (registration, authentication, out-of-band, deregistration) share `UserInteractionOperationState`. PIN change and password change each have their own state type. All of them are provided in `ApplicationModule`.
- **Cancellation**: `CancelOperationUseCaseImpl` cancels whatever handler is pending. It is triggered from `CancellableOperationViewModel` on back press.
- **Named bindings**: there are two `AuthenticatorSelector` instances (`REGISTRATION_AUTHENTICATOR_SELECTOR` / `AUTHENTICATION_AUTHENTICATOR_SELECTOR`) and two in-band authentication use cases (`IN_BAND_AUTHENTICATION_USE_CASE_DEFAULT` / `IN_BAND_AUTHENTICATION_USE_CASE_FOR_DEREGISTRATION`, where the latter's `AuthorizationProvider` is used to deregister).
- **Initialization**: `InitializeClientUseCaseImpl` guards initialization with a `Semaphore`, so concurrent calls initialize the client only once. It stores the client in `ClientProviderImpl`.

## Integration Guidance

When moving code into your own app:

| If you want to… | Look at | Notes |
|---|---|---|
| Add the SDK dependency | root `build.gradle`, `app/build.gradle`, `gradle.properties` | Use the `release` flavor for release builds (`releaseImplementation`); see [installation](https://docs.nevis.net/mobilesdk/guide/installation/android-installation). |
| Configure the backend | `ApplicationModule` (`provide*Configuration`) | [Configuration](https://docs.nevis.net/mobilesdk/guide/configuration) |
| Wrap the SDK in coroutines | `domain/usecase/`, `domain/interaction/`, `domain/model/state/` | The continuation + handler-in-state pattern is reusable as is. Swap `LiveData` for `Flow` if you prefer. |
| Initialize the SDK once | `InitializeClientUseCaseImpl`, `ClientProviderImpl` | |
| Register with Authentication Cloud | `AuthCloudApiRegistrationUseCaseImpl`, `AuthCloudRegistrationViewModel` | Your backend gets the enrollment response / `appLinkUri` from Authentication Cloud. |
| Register with Identity Suite | `InBandRegistrationUseCaseImpl`, `LoginUseCaseImpl`, `LegacyLoginViewModel` | Replace the login call with your own; keep the `AuthorizationProvider` idea. |
| Handle QR codes / deep links | `DecodePayloadUseCaseImpl`, `ProcessOutOfBandPayloadUseCaseImpl`, `OutOfBandOperationViewModel`, `MainActivity.processIntent` | QR scanning (`ui/qrReader/`) is demo scaffolding. |
| Authenticate in-band | `InBandAuthenticationUseCaseImpl`, `SelectAccountViewModel` | |
| Pick the authenticator | `AuthenticatorSelectorImpl`, `SelectAuthenticatorUseCaseImpl`, `AuthenticatorValidatorImpl` | Reusable filtering logic; replace the UI. |
| Enroll, verify or change a PIN/password | `domain/interaction/pin/`, `password/`, `Set*/Verify*/Change*/StartChange*UseCaseImpl`, `CredentialViewModel` | Recoverable errors and protection status (lockout/cool-down) are surfaced through the responses. |
| Biometric / device passcode / fingerprint | `*UserVerifierImpl`, `Verify*UseCaseImpl`, `VerifyUserViewModel` | |
| Transaction confirmation | `AccountSelectorImpl`, `TransactionConfirmationUseCaseImpl` | [Transaction confirmation](https://docs.nevis.net/mobilesdk/concept/operations/transaction-confirmation) |
| Deregister / delete authenticators / device info | `DeregisterUseCaseImpl`, `DeleteAuthenticatorsUseCaseImpl`, `ChangeDeviceInformationUseCaseImpl` | |
| Pending out-of-band operations | `FetchPendingOperationsUseCaseImpl` | |
| Push notifications | Not implemented | See [Push support](https://docs.nevis.net/mobilesdk/guide/push_support). |

Demo-only code that should not be copied as is:

- the on-screen log (`domain/log/`, `LogRecyclerViewAdapter`)
- showing raw technical errors to users (see [Error handling](https://docs.nevis.net/mobilesdk/guide/error-handling))
- the hard-coded login endpoint
- the device-name generator
- the placeholder deep link scheme and host names

If something isn't covered here, check https://docs.nevis.net (guide + Javadoc) before you write code. Don't guess the API.

## Conventions & Gotchas

- **One continuation per step**: a continuation must be resumed exactly once. Every use case that hands control back to the SDK has to store its own continuation in the state **before** it calls the handler, otherwise the next callback resumes a stale continuation (`IllegalStateException`) or nothing at all. Follow the existing use cases when you add a step.
- **Handlers are single-use**: clear a handler from the state once it has been used, as the existing use cases do. On success or error, the state is reset via `OnSuccessImpl` / `OnErrorImpl`.
- **Only one operation at a time**: there is a single cached state per state type, so starting a second operation while one is running overwrites the first operation's state.
- **Minimum OS**: `minSdk` 24 (Android 7). The biometric authenticator needs API 29+, and the device passcode authenticator needs API 30+. `AuthenticatorValidator` hides authenticators that aren't supported by hardware/OS or aren't policy-compliant, and prefers the biometric authenticator over the legacy fingerprint one.
- **Device lock and enrollment**: biometric, fingerprint and device passcode authenticators only work if the user has a secure lock screen and enrolled biometrics.
- **Emulators** work only because the `debug` SDK flavor is used. The `release` flavor detects emulators, debuggers and root, and refuses to run there. On an emulator, fingerprints can be simulated in the extended controls. QR scanning needs a camera (a virtual scene or a webcam).
- **Facet ID mismatch** (`UNTRUSTED_FACET_ID`) is the most common setup error. The facet ID changes with the signing key: debug keystore, release key, Play App Signing.
- **SDK version alignment**: `NEVIS_MOBILE_AUTHENTICATION_SDK_VERSION` and `VERSION_NAME` in `gradle.properties` follow the SDK's major.minor version. When you upgrade, check the [release notes](https://docs.nevis.net/mobilesdk/release_notes/android-release-notes) for API changes.
- **Credentials as `CharArray`**: PINs and passwords are passed as `CharArray`, not `String`.
- **Code style**: Kotlin, ktlint enforced (`./gradlew ktlintCheck` must pass). KDoc on public members (Dokka `reportUndocumented`). Copyright header on new files.
- **Never commit secrets**: `local.properties` is git-ignored and must stay that way. Don't commit GitHub tokens or keystores that hold real signing keys either.
