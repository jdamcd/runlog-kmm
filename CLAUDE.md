# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

RunLog is a Kotlin Multiplatform Mobile (KMM) Strava client with thin iOS (SwiftUI) and Android (Jetpack Compose) UI layers. The shared Kotlin modules provide business logic, API integration, and data persistence.

## Common Commands

### Build & Lint
```bash
# Format all code (Swift + Kotlin)
./check

# Android: Build and run tests
./gradlew assembleDebug testDebugUnitTest

# Android: Run specific test
./gradlew :android:testDebugUnitTest --tests "com.jdamcd.runlog.ui.training.TrainingViewModelTest"

# Kotlin formatting
./gradlew spotlessApply

# Swift formatting
swiftformat .

# Check formatting without applying
./gradlew spotlessCheck
```

### iOS Development
```bash
# Build iOS app
xcodebuild -project ios/RunLog.xcodeproj -scheme RunLog -sdk iphonesimulator

# Run iOS tests
xcodebuild test -project ios/RunLog.xcodeproj \
  -scheme RunLog \
  -sdk iphonesimulator \
  -destination 'platform=iOS Simulator,name=iPhone 17,OS=26.1'
```

### Shared Module Tests
```bash
# Run all shared module tests
./gradlew :shared:main:allTests
./gradlew :shared:api:allTests
./gradlew :shared:database:allTests

# Run specific platform tests
./gradlew :shared:main:testDebugUnitTest  # Android-specific
./gradlew :shared:main:iosSimulatorArm64Test  # iOS-specific
```

## Architecture

### Module Structure & Boundaries

```
shared/main      → Business logic layer (repositories, interactors)
├─→ shared/api       → Strava API client (Ktor + OAuth)
├─→ shared/database  → SQLDelight persistence layer
└─→ shared/utils     → Logging, formatting, shared utilities

android/         → Jetpack Compose UI (MVVM, Hilt bridges to Koin)
ios/             → SwiftUI UI (MVVM, accesses Koin via IosDI bridge)
```

**Key Principle**: Shared modules expose platform-agnostic interfaces (`StravaLogin`, `StravaActivity`, `StravaProfile`) with domain models (`ActivityCard`, `ActivityDetails`, `AthleteProfile`). Platform apps consume these via dependency injection.

### Dependency Injection

**Koin (Multiplatform)**:
- Central config: `shared/main/.../SharedModule.kt` loads all submodules
- Platform-specific modules use `expect`/`actual` pattern for platform bindings (e.g., database drivers, token storage)
- iOS accesses via `IosDI()` bridge class exposed to Swift
- Android bridges Koin → Hilt in `android/.../AppModule.kt`

### Data Flow Pattern

**Repository Pattern**: Coordinates API + DB access
- Example: `ActivityRepository.refresh()` → `StravaApi.activities()` → `ActivityMapper.summaryApiToDb()` → `ActivityDao.insert()`
- Reactivity via SQLDelight flows: `dao.latestActivitiesFlow()` emits on DB changes

**Mapper Pattern**: Transforms between layers
- API DTOs (`ApiSummaryActivity`) → DB entities (`Activity`) → UI models (`ActivityCard`)
- Keeps concerns separated; mappers in `shared/main/mappers/`

### Platform-Specific Implementations

Use `expect`/`actual` for platform differences:
- **Token Storage**: `PersistingUserState`
  - Android: SharedPreferences (requires Context)
  - iOS: NSUserDefaults
- **Database Drivers**: `DatabaseModule.kt` has platform-specific `actualModule()`
  - Android: AndroidSqliteDriver
  - iOS: NativeSqliteDriver
- **Formatting**: `FormatExtensions.kt` delegates to platform-specific `FormatActual.kt`

### Key Libraries

- **Networking**: Ktor Client 3.3.2 (auto-refreshes OAuth tokens via bearer auth plugin)
- **Database**: SQLDelight 2.1.0 (reactive flows, coroutines extensions)
- **DI**: Koin 4.1.0 (multiplatform), Hilt 2.56.1 (Android-only)
- **Testing**: Kotest 6.0.3, MockK 1.14.6, Turbine 1.2.1 (Flow testing)

## Database Schema

**Activity Table**: Caches 100 latest activities
- Fields: id, name, isPrivate, type, subtype, distance, duration, pace, start, mapPolyline

**Athlete + RunStats**: Profile with aggregate stats
- `Athlete` table: id, username, name, imageUrl, isUser, lastUpdated
- `RunStats` table: recent/year/all distance and pace (keyed by athlete ID)
- `athleteWithStats` view: denormalized join for efficient queries

## Local Development Setup

Required API keys in `local.properties`:
```properties
com.jdamcd.runlog.client_id=<Strava OAuth Client ID>
com.jdamcd.runlog.client_secret=<Strava OAuth Secret>
com.jdamcd.runlog.mapbox_token=<Mapbox Static Maps Token>
```

Register at:
- Strava API: https://strava.com/settings/api
- Mapbox: https://www.mapbox.com

BuildKonfig plugin loads these at compile time (not runtime).

## Testing Strategy

- **Common Tests**: `commonTest/` directories run on all platforms (DAOs, mappers, utilities)
- **Android Unit Tests**: `androidUnitTest/` uses MockK for repository/interactor tests
- **iOS Tests**: `iosTest/` for platform-specific logic (e.g., NSUserDefaults wrapper)
- **Flow Testing**: Use Turbine to test reactive streams in repositories

When adding features:
1. Write common tests for shared business logic
2. Use platform-specific tests for platform bindings
3. Mock dependencies via Koin test modules or MockK

## Code Style

- **Kotlin**: Ktlint via Spotless (config in `.editorconfig`)
- **Swift**: SwiftFormat (config in `.swiftformat`)
- Run `./check` before committing to format all code and run tests

## iOS Framework Integration

Shared module compiles to `RunLogShared.framework`:
- Initialize Koin in Swift: `SharedModuleKt.doInitKoin()`
- Access shared classes: `IosDI().stravaLogin()`, `IosDI().stravaActivity()`, etc.
- Bridge Kotlin coroutines to Swift async/await or Combine
- Observe Flow-based repositories using `asyncStream()` or custom wrappers
