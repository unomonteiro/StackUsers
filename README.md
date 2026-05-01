# StackUsers

Browse the top StackOverflow users by reputation and follow the ones you like —
state persists across sessions.

## Tech Stack

- Kotlin · Jetpack Compose · Material 3 · Hilt · Retrofit + Gson · Coil · DataStore Preferences

## Architecture

Three-layer Clean Architecture (`domain` / `data` / `ui`). The use case combines the
API response with live follow state from DataStore into a single `Flow<UsersUiState>`.
Follow/unfollow calls go directly to the repository from the ViewModel — no extra
use case wrapper needed for simple writes.

## Build & Test

```bash
./gradlew assembleDebug
./gradlew test
```

minSdk 24 (Android 7.0) · Android Studio Meerkat or later
