/**
 * Nevis Mobile Authentication SDK Example App
 *
 * Copyright © 2023. Nevis Security AG. All rights reserved.
 */

package ch.nevis.exampleapp.coroutines.common.settings

/**
 * Interface declaration of application settings. It provides application level settings properties.
 */
interface Settings {
    /**
     * Specifies whether <a href="https://source.android.com/docs/security/features/biometric/measure#biometric-classes">
     * Class 2 (formerly weak)</a> biometric sensors are allowed if the biometric authenticator is selected.
     * By default the SDK will only allow to use Class 3 (formerly strong) sensors. Using Class 2 sensors is
     * less secure and discouraged. When a Class 2 sensor is used, the FIDO UAF keys are not protected by the
     * operating system by requiring user authentication.
     * If the SDK detects that only Class 3 (strong) biometric sensors are available in the mobile device,
     * even if Class 2 sensors are allowed, the FIDO UAF credentials will be protected by the operating system
     * by requiring user authentication.
     * However in some cases it may be acceptable for the sake of end-user convenience. Allowing Class 2
     * sensors will enable for instance the use of face recognition in some Samsung devices.
     *
     * @see ch.nevis.mobile.sdk.api.operation.authcloudapi.AuthCloudApiRegistration
     * @see ch.nevis.mobile.sdk.api.operation.outofband.OutOfBandRegistration
     * @see ch.nevis.mobile.sdk.api.operation.Registration.allowClass2Sensors
     */
    val allowClass2Sensors: Boolean
}
