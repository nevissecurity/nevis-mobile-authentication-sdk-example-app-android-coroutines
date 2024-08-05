# Module nevis-mobile-authentication-example-coroutines-app

## Description

The example app demonstrating how to use the Nevis Mobile Authentication SDK in an Android mobile application.
The Nevis Mobile Authentication SDK allows you to integrate passwordless authentication to your existing mobile app, backed by the FIDO UAF 1.1 Standard.

Some SDK features demonstrated in this example app are:

- Using the SDK with the Nevis Authentication Cloud
- Registering with QR code & app link URIs
- Simulating in-band authentication after registration
- Deregistering a registered account
- Changing the PIN of the PIN authenticator
- Changing the device information

Please note that the example app only demonstrates a subset of the available SDK features. The main purpose is to demonstrate how the SDK can be used, not to cover all supported scenarios.

# Package ch.nevis.exampleapp.coroutines.application

This package contains a sub-class of `Application` to enable Dagger Hilt capabilities and to initialize logging.

# Package ch.nevis.exampleapp.coroutines.common.configuration

This package contains the configuration possibilities of the Nevis Mobile Authentication SDK.

# Package ch.nevis.exampleapp.coroutines.common.error

This package contains the error handling related classes.

# Package ch.nevis.exampleapp.coroutines.common.settings

This package contains the application settings possibilities.

# Package ch.nevis.exampleapp.coroutines.dagger

This package contains the main Dagger Hilt configuration module.

# Package ch.nevis.exampleapp.coroutines.data.cache

This package contains caching related implementations.

# Package ch.nevis.exampleapp.coroutines.data.dataSource

This package contains data source implementations.

# Package ch.nevis.exampleapp.coroutines.data.dataSource

This package contains model definitions.

# Package ch.nevis.exampleapp.coroutines.data.repository

This package contains repository implementations.

# Package ch.nevis.exampleapp.coroutines.data.retrofit

This package contains the `Retrofit` networking framework integration.

# Package ch.nevis.exampleapp.coroutines.domain.client

This package contains the `MobileAuthenticationClient` provider.

# Package ch.nevis.exampleapp.coroutines.domain.interaction

This package contains the default implementation of interaction related interfaces from the Nevis Mobile Authentication SDK, like account and authenticator selection and user verification.

# Package ch.nevis.exampleapp.coroutines.domain.interaction.password

This package contains the default implementation of password interaction related interfaces from the Nevis Mobile Authentication SDK, like changing, enrolling and verifying the password.

# Package ch.nevis.exampleapp.coroutines.domain.interaction.pin

This package contains the default implementation of PIN interaction related interfaces from the Nevis Mobile Authentication SDK, like changing, enrolling and verifying the PIN.

# Package ch.nevis.exampleapp.coroutines.domain.log

This package contains logging related implementations.

# Package ch.nevis.exampleapp.coroutines.domain.model.error

This package contains error handling related model definitions.

# Package ch.nevis.exampleapp.coroutines.domain.model.operation

This package contains operation related model definitions.

# Package ch.nevis.exampleapp.coroutines.domain.model.response

This package contains operation related response model definitions.

# Package ch.nevis.exampleapp.coroutines.domain.model.sdk

This package contains Nevis Mobile Authentication SDK related model definitions.

# Package ch.nevis.exampleapp.coroutines.domain.model.state

This package contains operation related state definitions.

# Package ch.nevis.exampleapp.coroutines.domain.repository

This package contains repository definitions.

# Package ch.nevis.exampleapp.coroutines.domain.usecase

This package contains use case definitions and implementations.

# Package ch.nevis.exampleapp.coroutines.domain.util

This package contains Nevis Mobile Authentication SDK related extension definitions.

# Package ch.nevis.exampleapp.coroutines.domain.validation

This package contains validation related logic.

# Package ch.nevis.exampleapp.coroutines.timber

This package contains the `Timber` logging framework integration.

# Package ch.nevis.exampleapp.coroutines.ui.authCloudRegistration

This package contains the view and view model definitions for the Auth Cloud API Registration operation.

# Package ch.nevis.exampleapp.coroutines.ui.base

This package contains the base view and view model definitions.

# Package ch.nevis.exampleapp.coroutines.ui.changeDeviceInformation

This package contains the view and view model definitions for the Change Device Information operation.

# Package ch.nevis.exampleapp.coroutines.ui.credential

This package contains the view and view model definitions for credential related operations, like change, enroll or verify a PIN or password.

# Package ch.nevis.exampleapp.coroutines.ui.credential.model

This package contains the model definitions for credential related operations.

# Package ch.nevis.exampleapp.coroutines.ui.credential.parameter

This package contains the navigation parameter definitions for credential related operations.

# Package ch.nevis.exampleapp.coroutines.ui.error

This package contains the view definition for error handling.

# Package ch.nevis.exampleapp.coroutines.ui.error.parameter

This package contains the navigation parameter definitions for error handling.

# Package ch.nevis.exampleapp.coroutines.ui.home

This package contains the view and view model definitions for the Home feature.

# Package ch.nevis.exampleapp.coroutines.ui.legacyLogin

This package contains the view and view model definitions for In-Band Registration operation.

# Package ch.nevis.exampleapp.coroutines.ui.main

This package contains the main activity and view model definitions.

# Package ch.nevis.exampleapp.coroutines.ui.qrReader

This package contains the view and view model definitions for Qr Code reading.

# Package ch.nevis.exampleapp.coroutines.ui.result

This package contains the view definition for displaying operation result.

# Package ch.nevis.exampleapp.coroutines.ui.result.parameter

This package contains the navigation parameter definitions for displaying operation result.

# Package ch.nevis.exampleapp.coroutines.ui.selectAccount

This package contains the view and view model definitions for account selection.

# Package ch.nevis.exampleapp.coroutines.ui.selectAccount.parameter

This package contains the navigation parameter definitions for account selection.

# Package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator

This package contains the view and view model definitions for authenticator selection.

# Package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.model

This package contains the model definitions for authenticator selection.

# Package ch.nevis.exampleapp.coroutines.ui.selectAuthenticator.parameter

This package contains the navigation parameter definitions for authenticator selection.

# Package ch.nevis.exampleapp.coroutines.ui.transactionConfirmation

This package contains the view and view model definitions for transaction confirmation.

# Package ch.nevis.exampleapp.coroutines.ui.transactionConfirmation.parameter

This package contains the navigation parameter definitions for transaction confirmation.

# Package ch.nevis.exampleapp.coroutines.ui.util

This package contains utility methods.

# Package ch.nevis.exampleapp.coroutines.ui.verifyUser

This package contains the view and view model definitions for user verification.

# Package ch.nevis.exampleapp.coroutines.ui.verifyUser.model

This package contains the model definitions for user verification.

# Package ch.nevis.exampleapp.coroutines.ui.verifyUser.parameter

This package contains the navigation parameter definitions for user verification.
