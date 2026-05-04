# StackUsers

Shows the top 20 StackOverflow users ranked by reputation. you can follow any of them and 
persists between app restarts — everything is local, no backend needed.

## How it works

On launch it fetches the top 20 users from the Stack Exchange API and shows them in a list.
Each item has a follow/unfollow button. If you're offline or the request fails, you get an error
message and a retry button. 
Followed/Unfollowed users survive kills/restarts via DataStore.

## Install and run

Android Studio Meerkat or later, minSdk 24. no API keys needed, Stack Exchange is open for
read requests.

```bash
git clone https://github.com/unomonteiro/StackUsers.git
cd StackUsers
./gradlew assembleDebug
```


```bash
./gradlew test                   # unit tests
./gradlew connectedAndroidTest   # UI tests (needs device or emulator)
```

or open the project in Android Studio, connect a device or start an emulator, and hit run.

## Decisions

I went with three layers — domain, data, ui. the domain layer has no Android dependencies
at all which makes it straightforward to test. the use case is probably the most interesting
part: it combines the one-shot API call with the live DataStore flow so the list updates
reactively when follow state changes without hitting the network again.

`UsersUiState` is in the domain layer. I had it in ui first but that meant the use case was
importing from ui, which is the wrong dependency direction, so I moved it.

Follow/unfollow go straight from the ViewModel to the repository with no use case in between
there's no business logic involved so wrapping them felt like indirection for its own sake.

DataStore over SharedPreferences mostly because it handles concurrent writes safely and exposes
a Flow, which fits naturally into the rest of the setup.
