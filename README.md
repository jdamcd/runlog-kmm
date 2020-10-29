# RunLog

RunLog is a work-in-progress KMM (Kotlin Multiplatform Mobile) Strava client for iOS and Android. All interactions with the [Strava API](https://developers.strava.com) are handled via the `shared` Kotlin module. The iOS and Android apps are mostly concerned with UI (views and MVVM view models), using SwiftUI and Jetpack Compose respectively.

## Local builds

Register a new [Strava API application](https://strava.com/settings/api) and add your OAuth client ID and secret to `local.properties` under the keys:
- `com.jdamcd.runlog.client_id`
- `com.jdamcd.runlog.client_secret` 

The OAuth redirect is configured in `StravaApi.kt` as `runlog-auth://jdamcd.com`. Keep the scheme, but use whatever domain you configured for the "Authorization Callback Domain".
