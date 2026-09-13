# FormulaTrackr — System Architecture & Technical Specification

> **Authoritative Context Document for AI Models, LLMs, and Developers**  
> *Last Updated: 2026-09-11 | FormulaTrackr Kotlin Multiplatform Application*

---

## 1. Executive Summary & Vision

**FormulaTrackr** is a high-performance, modern Formula 1 tracking and telemetry application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**. The application provides Formula 1 fans and engineers with real-time race telemetry, historical race archives, live session tracking, championship standings, race schedules, and interactive circuit visualizations.

Target Platforms:
- **Android** (`:app` module): Native Android application targeting SDK 36 (minSdk 29) with edge-to-edge Compose UI, splash screen API, and Chucker network inspection.
- **Web (Wasm)** (`:wasmApp` module): Kotlin/Wasm (`wasmJs`) web target rendering the exact same Compose Multiplatform UI in modern browsers with near-native canvas performance.
- **Shared Core** (`:shared` module): 100% shared UI components, design tokens, navigation backstack, domain models, network repositories, and asset resolvers.

---

## 2. Technical Stack & Dependencies

| Category | Technology / Library | Version / Detail | Purpose |
| :--- | :--- | :--- | :--- |
| **Language** | Kotlin | `2.1.21` | Modern, concise, type-safe multiplatform language |
| **UI Framework** | Compose Multiplatform | `1.8.2` (BOM `2025.05.00`) | Declarative reactive UI shared across Android & Web |
| **Build Tooling** | Gradle & AGP | AGP `8.11.2`, Gradle `8.x` | Multi-module build management with Version Catalogs |
| **Serialization** | `kotlinx.serialization` | `1.8.1` | Multiplatform JSON serialization/deserialization |
| **Networking** | Ktor Client | `3.1.3` | OkHttp engine (Android), CIO/Js engine (Wasm) |
| **Image Loading** | Coil 3 Multiplatform | `3.2.0` | Asynchronous image loading with OkHttp network & SVG support |
| **Dependency Injection** | Hilt | `2.56.2` (Android `:app`) | Native Android dependency injection |
| **Design System** | Material 3 + Custom Obsidian/Glass | Material3 `1.4.0` | Premium dark mode, glassmorphism, carbon accents |
| **Typography** | Google Fonts (Outfit / Roboto Mono) | `1.8.1` | High-legibility race typography & monospace telemetry |
| **Paging** | AndroidX Paging 3 | `3.3.6` | Large dataset streaming (historical results) |
| **Network Debugging** | Chucker | `4.1.0` | On-device HTTP inspection for Android debug builds |

---

## 3. Project Architecture & Modularization

```
FormulaTrackr/
├── app/                                  # Android Application Module
│   ├── src/main/java/com/dirzaaulia/formula1/
│   │   ├── MainActivity.kt               # Entry activity, edge-to-edge, sets FormulaTrackrApp()
│   │   ├── Application.kt                # Application class with @HiltAndroidApp
│   │   ├── di/                           # Hilt DI modules (Network, Repository)
│   │   ├── model/                        # Legacy/Android-specific models
│   │   ├── navigation/                   # Android-specific routing helpers
│   │   └── ui/                           # Android legacy UI screens
│   └── build.gradle.kts
│
├── shared/                               # Core Kotlin Multiplatform Module
│   ├── src/
│   │   ├── commonMain/kotlin/com/dirzaaulia/formula1/
│   │   │   ├── FormulaTrackrApp.kt       # Universal root composable, responsive shell, modal host
│   │   │   ├── navigation/
│   │   │   │   ├── NavRoute.kt           # Sealed interface (Home, Telemetry, Calendar, Standings, Info, RaceDetail)
│   │   │   │   └── NavGraph.kt           # Multiplatform lightweight backstack navigator
│   │   │   ├── model/                    # Universal domain models
│   │   │   │   ├── Driver.kt             # Driver profile, code, number, nationality
│   │   │   │   ├── Team.kt               # Constructor, id, color, engine
│   │   │   │   ├── Race.kt               # Grand Prix schedule, session times, round
│   │   │   │   ├── RaceResult.kt         # Classification, points, laps, grid, status
│   │   │   │   ├── DriverStandings.kt    # Championship standings, points, wins
│   │   │   │   ├── ConstructorsStandings.kt
│   │   │   │   ├── Circuit.kt            # Circuit id, location, country, coordinates
│   │   │   │   └── TelemetryRow.kt       # Timing line (position, gap, sector times, tyre, speed)
│   │   │   ├── network/
│   │   │   │   ├── JolpicaService.kt     # Jolpica / Ergast F1 API client
│   │   │   │   └── NetworkRepository.kt  # Unified repository for standings, races, telemetry
│   │   │   ├── theme/
│   │   │   │   ├── Theme.kt              # FormulaTrackrTheme (Obsidian dark, Glass accents)
│   │   │   │   ├── Color.kt              # F1 Red (#E10600), Telemetry Purple (#9C27B0), Green (#00E676), Yellow (#FFD600)
│   │   │   │   └── Type.kt               # Outfit + Roboto Mono typography
│   │   │   ├── ui/
│   │   │   │   ├── component/            # ModernHeader, ModernFooter, MetricTile, ShimmerCard, VectorMap
│   │   │   │   ├── dialog/               # DriverDetailModal, ConstructorDetailModal
│   │   │   │   └── screen/
│   │   │   │       ├── home/             # Featured race hero, upcoming countdown, quick standings
│   │   │   │       ├── telemetry/        # TelemetryScreen (Live timing + Archive replay + Circuit view)
│   │   │   │       ├── calendar/         # Season calendar, weekend schedule, past race results
│   │   │   │       ├── race_detail/      # Detailed race weekend overview, results, weather
│   │   │   │       ├── standings/        # Driver and constructor championship tables
│   │   │   │       ├── app_info/         # App credits, API attribution, version info
│   │   │   │       └── splash/           # 5-red-lights starting gantry splash animation
│   │   │   └── util/
│   │   │       └── MediaAssets.kt        # CDN headshot URLs, team logos, circuit SVGs, country flags
│   │   ├── androidMain/                  # Android KMP engine bindings (OkHttp, Android resources)
│   │   └── wasmJsMain/                   # Wasm/JS KMP engine bindings (Ktor CIO/Js)
│   └── build.gradle.kts
│
├── wasmApp/                              # Web (Compose Wasm) Application Module
│   ├── src/wasmJsMain/kotlin/com/dirzaaulia/formula1/wasm/
│   │   └── Main.kt                       # Wasm entry point: ComposeViewport(document.body) { FormulaTrackrApp() }
│   ├── src/wasmJsMain/resources/         # index.html, styles
│   └── build.gradle.kts
│
├── docs/                                 # Documentation & ASO Guidelines
│   ├── APP_ARCHITECTURE.md               # System architecture & specs
│   └── PLAYSTORE_ASO_GUIDE.md            # Play Store publishing & ASO metadata
└── gradle/
    └── libs.versions.toml                # Centralized Gradle version catalog
```

---

## 4. Formula 1 Official Public Data Feeds & APIs

FormulaTrackr leverages a hybrid multi-tier API architecture to ensure 100% data fidelity, zero paywalls, and uninterrupted offline/archive accessibility:

```
+-----------------------------------------------------------------------------------+
|                            FORMULATRACKR DATA LAYER                               |
+-------------------------+--------------------------------+------------------------+
                          |                                |
        +-----------------+----------------+               |
        |                                  |               |
        v                                  v               v
+-----------------------+      +-----------------------+ +--------------------------+
|  F1 OFFICIAL STATIC   |      |      OPENF1 REST      | |    JOLPICA / ERGAST      |
|     JSON ARCHIVE      |      |     COMMUNITY API     | |        REST API          |
| livetiming.formula1.  |      |   api.openf1.org/v1   | |   api.jolpica.com/f1     |
|     com/static/       |      |                       | |                          |
+-----------------------+      +-----------------------+ +--------------------------+
| * SessionInfo.json    |      | * /location (X/Y)     | | * Historical Results     |
| * TimingData.json     |      | * /intervals (Gaps)   | | * Championship Standings |
| * TimingAppData.json  |      | * /laps & sectors     | | * Season Schedules       |
| * DriverList.json     |      | * /stints & tyres     | | * Driver/Team Bios       |
| * WeatherData.json    |      | * /position           | | * Qualifying Results     |
| * TrackStatus.json    |      | * /weather            | |                          |
+-----------------------+      +-----------------------+ +--------------------------+
```

### 4.1 F1 Official Public Static Feeds (`livetiming.formula1.com/static/`)
F1 publishes unauthenticated, public JSON endpoints for current live sessions and historic season archives.

* **Base URL**: `https://livetiming.formula1.com/static/`
* **Root Session Info**: `https://livetiming.formula1.com/static/SessionInfo.json`
  * Returns active meeting metadata, session status (`"Started"`, `"Finalised"`), circuit key, GMT offset.
* **Master Season Index**: `https://livetiming.formula1.com/static/{year}/Index.json`
  * Lists all Grand Prix meetings, session IDs, start/end timestamps, and file paths.
* **Per-Session Endpoint Schema**:
  Path pattern: `https://livetiming.formula1.com/static/{year}/{meeting_folder}/{session_folder}/`
  * `TimingData.json`: Real-time driver timing table lines. Contains `GapToLeader`, `IntervalToPositionAhead` (`{Value, Catching}`), `Position`, `RacingNumber`, `InPit`, `PitOut`, `NumberOfLaps`, `NumberOfPitStops`, `Sectors` (values, personal fastest, overall fastest, mini-segments).
  * `TimingAppData.json`: Tyre compounds (`Compound`: "SOFT", "MEDIUM", "HARD", "INTERMEDIATE", "WET"), tyre age (`TotalLaps`), pit stint breakdown per driver.
  * `DriverList.json`: Complete driver roster for the session: Broadcast name, TLA (3-letter acronym), racing number, team name, team colour hex code.
  * `CarData.z.json` & `Position.z.json`: High-frequency compressed telemetry (speed, RPM, gear, throttle, brake, DRS, X/Y/Z world coordinates). Encoded as zlib raw streams.
  * `TrackStatus.json`: Track flag conditions (`1` = All Clear / Green, `2` = Yellow Flag, `4` = Safety Car, `5` = Red Flag, `6` = Virtual Safety Car).
  * `WeatherData.json`: Air temperature, track temperature, humidity, pressure, rainfall, wind speed/direction.
  * `RaceControlMessages.json`: Stewards' notices, investigations, penalties, DRS enabled/disabled.
  * `LapCount.json`: Current lap and total laps.

### 4.2 OpenF1 REST Community API (`https://api.openf1.org/v1`)
OpenF1 is an open-source data pipeline that ingests F1 live feeds into clean REST JSON:
* **Endpoints**:
  * `/sessions?year=2026&session_type=Race`: Session keys and circuit parameters.
  * `/drivers?session_key={key}`: Driver roster with official colors and acronyms.
  * `/location?session_key={key}&driver_number={nr}`: Millisecond X, Y, Z track position coordinates (3.7 Hz resolution). Used for 2D track car rendering.
  * `/intervals?session_key={key}`: Gap to leader and interval to car ahead updated every lap/split.
  * `/laps?session_key={key}`: Lap durations, S1/S2/S3 sector times, segment flags, I1/I2 speed trap speeds.
  * `/stints?session_key={key}`: Compound history (Soft, Medium, Hard), stint lap count.
  * `/weather?session_key={key}`: Track temp, air temp, rain status.

### 4.3 Jolpica F1 API (`https://api.jolpica.com/ergast/f1`)
Open-source Ergast API replacement with full 1950–2026 coverage:
* Driver Championship Standings (`/{season}/driverstandings.json`)
* Constructor Championship Standings (`/{season}/constructorstandings.json`)
* Season Race Calendar (`/{season}.json`)
* Official Race Results (`/{season}/{round}/results.json`)

---

## 5. Telemetry & Timing Feature Specifications

### 5.1 Full-Width Responsive Timing Table (No Horizontal Scrolling)
The Telemetry Timing Table is engineered to fit 100% of mobile and desktop viewports without requiring horizontal scroll gestures:

* **Column Layout (Mobile Compact < 700dp)**:
  `[POS] [COLOR + TLA + NUM] [TYRE/AGE] [GAP TO LEADER / INTERVAL] [SECTORS / S1 S2 S3] [PIT/STATUS]`
* **Hierarchy**:
  1. **Position**: Monospace bold badge with position delta indicator (up green / down red).
  2. **Driver**: 3-letter abbreviation (`VER`, `NOR`, `ANT`), team color vertical bar, driver car number.
  3. **Tyre**: Colored compound pill (`S` Red, `M` Yellow, `H` White, `I` Green, `W` Blue) + lap age.
  4. **Interval / Gap**: Dynamic toggle between "Gap to Leader" and "Interval to Ahead". Monospace aligned.
  5. **Sectors**: 3 micro-pills representing Sector 1, 2, and 3:
     - Purple (`#9C27B0`): Overall fastest sector
     - Green (`#00E676`): Personal best sector
     - Yellow (`#FFD600`): Normal sector
  6. **Pit / Status**: "PIT", "OUT", "STOP", or "DNF" status tags.

### 5.2 Separate Interactive Circuit Visualization
* Circuit view is segregated into its own dedicated modular card above or adjacent to the timing board.
* Renders real vector track outlines using official circuit SVGs from `f1db/f1-circuits-svg`.
* Car markers:
  * In live/archive sessions with coordinate telemetry: Rendered using normalized track X/Y coordinates.
  * In fallback/lap-based replay: Computed via normalized track progress percentage based on lap completion time fraction.
  * Driver dots display driver number/TLA and constructor team color.

### 5.3 Live Timing vs Archive Race Modes
* **LIVE Mode**:
  - Automatically queries `https://livetiming.formula1.com/static/SessionInfo.json`.
  - When a Grand Prix session is live (`SessionStatus == "Started"`), activates high-frequency polling (2-3s interval).
  - If no session is actively running, displays an elegant "Next Session Countdown" and seamlessly offers the latest completed Grand Prix as an instant replay.
* **ARCHIVE Mode**:
  - Lets users choose any completed race from the season calendar.
  - Interactive playback controls: Play, Pause, Scrubbing Slider (Lap 1 to Final Lap), Speed Multiplier (1x, 2x, 5x).
  - Accurate pit stop entry/exit and position changes reconstructed from official session feeds.

---

## 6. Media & Asset Pipeline

FormulaTrackr does not bundle heavy static image assets in the application binary. Instead, it uses dynamic CDN resolvers in `MediaAssets.kt`:

### 6.1 Driver Headshots
* **Base URL**: Official Formula 1 CDN (`https://media.formula1.com`)
* **2026 Driver Updates**:
  * **Andrea Kimi Antonelli** (Mercedes):
    `https://media.formula1.com/image/upload/c_fill,w_720/q_auto/v1740000001/common/f1/2026/mercedes/andant01/2026mercedesandant01right.webp`
    *(Fallback: `d_driver_fallback_image.png/content/dam/fom-website/drivers/A/ANDANT01_Andrea_Kimi_Antonelli/andant01.png.transform/2col-retina/image.png`)*
  * **Arvid Lindblad** (Racing Bulls / Red Bull Junior):
    `https://media.formula1.com/image/upload/c_fill,w_720/q_auto/v1740000001/common/f1/2026/racingbulls/arvlin01/2026racingbullsarvlin01right.webp`
    *(Fallback: `d_driver_fallback_image.png/content/dam/fom-website/drivers/A/ARVLIN01_Arvid_Lindblad/arvlin01.png.transform/2col-retina/image.png`)*
  * All active grid drivers mapped by Driver Code, Driver ID, and name variations.

### 6.2 High-Resolution Constructor Logos
* Retrieved in retina quality:
  `https://media.formula1.com/content/dam/fom-website/teams/2025/{team-slug}-logo.png.transform/3col-retina/image.png`
* Covers Ferrari, McLaren, Mercedes, Red Bull Racing, Aston Martin, Alpine, Williams, Racing Bulls (RB), Haas, Kick Sauber, Audi, Cadillac.

### 6.3 Circuit Vector Geometry (SVG)
* Source: `https://raw.githubusercontent.com/f1db/f1-circuits-svg/main/circuits/detailed/white-outline/{circuit-slug}.svg`
* Clean, minimalist vector paths rendered with Coil SVG decoder or custom canvas path parsers.

### 6.4 Country Flags
* Source: `https://flagcdn.com/w320/{country_code}.png`

---

## 7. Design System & Theming Tokens

FormulaTrackr adheres to the **Obsidian Carbon & Glass** design language:

```kotlin
// Core Brand Colors
val F1Red              = Color(0xFFE10600)
val F1RedSubtle        = Color(0x28E10600)
val PitchBlack         = Color(0xFF050505)
val ObsidianVoid       = Color(0xFF0A0A0C)
val ObsidianSurface    = Color(0xFF121216)
val ObsidianSurfaceElevated = Color(0xFF1A1A22)

// Glassmorphism Tokens
val GlassSurface       = Color(0xB0121218)
val GlassSurfaceElevated = Color(0xD0181824)
val GlassBorder        = Color(0x22FFFFFF)
val GlassBorderActive  = Color(0x44E10600)

// Telemetry Status Colors
val TelemetryPurple    = Color(0xFFB026FF) // Overall fastest lap / sector
val TelemetryGreen     = Color(0xFF00E676) // Personal best lap / sector
val TelemetryYellow    = Color(0xFFFFD600) // Slower sector
val TelemetryBlue      = Color(0xFF2979FF) // Pit lane / out-lap

// Tyre Compound Colors
val TyreSoft           = Color(0xFFFF2A2A) // Red (C3-C5)
val TyreMedium         = Color(0xFFFFD600) // Yellow (C2-C4)
val TyreHard           = Color(0xFFFFFFFF) // White (C0-C2)
val TyreIntermediate   = Color(0xFF39B54A) // Green
val TyreWet            = Color(0xFF00A3E0) // Blue
```

---

## 8. Build, Run & Verification Commands

### 8.1 Android App
```bash
# Build Android debug APK
./gradlew :app:assembleDebug

# Install and run on connected device/emulator
./gradlew :app:installDebug

# Run unit tests
./gradlew :shared:testDebugUnitTest :app:testDebugUnitTest
```

### 8.2 Web (Wasm) App
```bash
# Build Wasm web distribution
./gradlew :wasmApp:wasmJsBrowserDistribution

# Development dev-server with hot reload
./gradlew :wasmApp:wasmJsBrowserDevelopmentRun
```

---

## 9. AI Model & Developer Guidelines (Checkpoint Instructions)

When extending, refactoring, or generating code for FormulaTrackr, all AI models and engineers must observe the following rules:

1. **Keep UI in `:shared`**: All new screens, dialogs, and components must reside in `shared/src/commonMain/` unless they require platform-specific Android/JVM APIs.
2. **Never break full-width timing**: The telemetry timing table must remain legible on screens as narrow as 360dp without horizontal scrolling. Use concise abbreviations and monospace fonts.
3. **Handle missing sessions gracefully**: Always check `SessionStatus` from `livetiming.formula1.com/static/SessionInfo.json`. When live feeds are unavailable, fallback to completed session replays with clear UI cues.
4. **Preserve driver and team asset resolution**: Always use `MediaAssets.kt` helper functions (`getDriverHeadshotUrl`, `getTeamLogoUrl`, `getCircuitMapUrl`) instead of hardcoding image URLs in UI files.
5. **No regressions on 2026 driver identities**: Ensure Andrea Kimi Antonelli (`ANDANT01` / Mercedes) and Arvid Lindblad (`ARVLIN01` / Racing Bulls) are always recognized.
