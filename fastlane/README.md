fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android main

```sh
[bundle exec] fastlane android main
```

Build and tag the application

#### Options

 * **`host_name`**: Authentication Cloud host name/instance name the build application will communicate with.

 * **`custom_uri_scheme`**: Custom deep link URI scheme of the built application.         The application can be started using browser links those using this scheme.



### android pr

```sh
[bundle exec] fastlane android pr
```

Build the application



----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
