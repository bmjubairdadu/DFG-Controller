# Startup and Splash Screen

DFG Controller uses a professional, animated startup experience built on top of the Android Native SplashScreen API.

## Implementation Details
- **API**: `androidx.core:core-splashscreen`
- **Theme**: `Theme.DFGController.Starting`
- **Animation**: 
    - The system splash screen appears immediately on app launch.
    - A custom **Exit Animation** is implemented in `MainActivity`.
    - When the application is ready, the logo fades out and scales up smoothly (500ms duration) with an `AnticipateInterpolator`, creating a professional "zoom-in" transition into the main dashboard.

## Logo Resource
- **Path**: `app/src/main/res/drawable/app_logo.png`
- **Usage**: Used as both the app icon and the splash screen center icon. It is centered and padded according to Android's adaptive icon guidelines to prevent cropping or stretching.

## Supported Versions
- **Android 12+**: Uses the full SplashScreen API with the animated transition.
- **Android 8.0 - 11**: Provides a backward-compatible static splash that transitions smoothly into the app.
