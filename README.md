# PokedexApp
A modern Android Pokedex app. Displays the Pokemon list in a grid view, and shows type, size, weight, and animated stats on the detail screen.

---

## Demo
<img height="480" alt="Video Project 1 (1)" src="https://github.com/user-attachments/assets/55b8e88c-b15b-4b6a-a9b1-e7878b3319db" />

---

## Screenshots
| List Screen | Detail Screen | Base Stats |
|:---:|:---:|:---:|
| ![List](https://github.com/user-attachments/assets/88f9fc6a-51ca-4cbb-bfcd-8213b32352e6) | ![Detail](https://github.com/user-attachments/assets/315038c1-3bee-4064-8561-f5e0ec1e0647) | ![Stats](https://github.com/user-attachments/assets/e6bf3f54-cf77-4c85-a3fd-403b9bd8cfe4) |

---

## Features
- **Pokemon list** — 2-column grid with pagination for infinite scroll
- **Pokemon detail** — image, name, type badges, size and weight info
- **Dynamic theme color** — dominant color extraction from the Pokemon sprite (Palette API)
- **Base Stats bottom sheet** — animated stat bars (HP, ATK, DEF, SPD, S.ATK, S.DEF)
- **Error handling** — Snackbar shown for loading, retry, and pagination errors
- **Environment support** — development, staging, and production product flavors
- **Developer tools** — Chucker (debug), HTTP logging, Bearer token auth

---

## Tech Stack
| Category | Technology |
|----------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Networking | Retrofit, OkHttp |
| Image loading | Coil |
| Navigation | Navigation Compose |
| Async | Kotlin Coroutines, Flow |

---

## Architecture
The app is organized using a layered Clean Architecture structure:
```
UI (Compose Screens + ViewModels)
        ↓
Domain (Models + Repository Interface)
        ↓
Data (Repository Impl + Remote API + DTOs)
```
**Data flow:** `ViewModel` → `Repository` → `ApiService` → the API response is converted into UI state through a `Resource<T>` wrapper.

---

## Screens

### Pokemon List
- Fetches a paginated Pokemon list from the API
- Type info is retrieved per card via a detail request
- Card backgrounds are colored using the dominant color extracted from the sprite
- Automatically loads more items as the user nears the end of the list

### Pokemon Detail
- Detailed information for the selected Pokemon
- Top bar and gradient background adapt to the sprite's color
- The "Base Stats" button opens/closes a bottom sheet
- Stat bars are displayed with a sequential animation

---

## Project Structure
```
app/src/main/java/com/example/pokedexapp/
├── data/
│   ├── model/              # API response models
│   ├── remote/             # ApiService, interceptors
│   └── repository/         # Repository implementation
├── domain/
│   ├── model/              # Domain models
│   └── repository/         # Repository interface
├── di/                     # Hilt modules
├── navigation/             # NavGraph, route definitions
├── ui/
│   ├── screen/
│   │   ├── list/           # List screen + ViewModel
│   │   └── detail/         # Detail screen + ViewModel + components
│   └── theme/              # Colors, typography, theme
├── util/                   # Constants, Resource, DominantColorExtractor
└── viewmodel/              # BaseViewModel
```

---

## API
The app uses the Pokemon API:
- `GET /pokemon?limit=&offset=` — Pokemon list
- `GET /pokemon/{id}` — Pokemon detail
