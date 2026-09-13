import os
import sys
import json
import time
import argparse
import subprocess
import requests
import jwt

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

PACKAGE_NAME = "com.dirzaaulia.formula1"
AAB_PATH = "app/build/outputs/bundle/release/app-release.aab"

KEY_CANDIDATES = [
    os.environ.get("PLAY_CONSOLE_KEY_FILE", ""),
    "fastlane/play-console-key.json",
    "D:/Android/FootballClips/fastlane/play-console-key.json"
]

APP_NAME = "PitWall: F1 Timing & Telemetry"

# Option 1 from docs/PLAYSTORE_ASO_GUIDE.md
SHORT_DESC_EN = "Next-gen live timing, telemetry deltas, sector speeds & race calendar for F1."
SHORT_DESC_ID = "Live timing F1 modern, telemetri pit wall real-time, jadwal balap & klasemen."
SHORT_DESC_ES = "¡Tiempos en vivo de F1, telemetría de pit wall, repeticiones y calendario!"
SHORT_DESC_PT = "Tempos em tempo real da F1, telemetria de pit wall, replay e classificação!"

FULL_DESC_EN = """Experience Formula 1 like an engineer on the pit wall. PitWall delivers professional live timing, deep sector telemetry analysis, instant race control messages, and historical Grand Prix replay archives directly to your Android device.

Whether you are tracking live weekend sessions or analyzing past race strategies, PitWall puts comprehensive circuit telemetry at your fingertips with zero clutter and blazing native performance.

⚡ KEY HIGHLIGHTS:

• ⏱️ Real-Time Live Timing & Pit Wall Telemetry
Monitor live driver positions, interval gaps to the car ahead, and delta to leader. Micro-sector split times with purple (overall fastest), green (personal best), and yellow indicators.

• 🏎️ Historic Session Replay Hub
Scrub and rewind through every lap of Grand Prix sessions with interactive replay controls. Compare lap times, speed traps, and tyre deg between championship rivals.

• 🛞 Live Tyre Stints & Weather Radar
Track tyre compounds (Soft, Medium, Hard, Inter, Wet), stint age in laps, pit stop durations, track temperature, air temp, and rain radar probability.

• 🚩 Instant Race Control Messages
Live safety car alerts (SC, VSC), yellow/red flags, track limit notices, and steward investigation updates displayed in full without truncation.

• 📅 2026 Race Calendar & Circuit Analytics
Start times automatically converted to your local timezone. Live weekend countdown timers, circuit layout diagrams, and track records.

• 🏆 Driver & Constructor Championship Standings
High-definition driver headshots and manufacturer liveries with real-time points progression.

---
LEGAL DISCLAIMER & COMPLIANCE:
This application is an independent, unofficial fan companion app and is not associated, affiliated, endorsed, or sponsored by Formula One Licensing B.V., Formula One Management, the FIA, or any Formula 1 team.

F1, FORMULA ONE, FORMULA 1, FIA FORMULA ONE WORLD CHAMPIONSHIP, GRAND PRIX, and related marks are registered trademarks of Formula One Licensing B.V. All team and driver identifiers are used strictly for descriptive fan identification purposes under fair use.

• Privacy Policy: https://f1.dirzaaulia.com/privacy
• Support: dirzaaulia@gmail.com
"""

FULL_DESC_ID = """Rasakan sensasi Formula 1 layaknya insinyur di pit wall. PitWall menghadirkan live timing modern, analisis telemetri sektor mendalam, pesan race control instan, dan arsip replay Grand Prix langsung ke perangkat Android Anda.

Pantau seluruh aksi balap akhir pekan atau pelajari strategi Grand Prix sebelumnya dengan data sirkuit terlengkap, tampilan pitch-black modern, dan performa gesit tanpa lag.

⚡ FITUR UTAMA:

• ⏱️ Live Timing & Telemetri Pit Wall Real-Time
Pantau posisi pembalap, interval gap antar mobil, dan selisih waktu ke pimpinan balap. Dilengkapi split sektor dengan indikator ungu (tercepat), hijau (rekor pribadi), dan kuning.

• 🏎️ Arsip Replay Telemetri Lap-by-Lap
Putar ulang jalannya balapan lap demi lap dengan slider interaktif. Bandingkan catatan waktu, speed trap, dan degradasi ban antar pembalap papan atas.

• 🛞 Strategi Ban & Cuaca Sirkuit Langsung
Pantau kompon ban aktif (Soft, Medium, Hard, Inter, Wet), umur ban per lap, durasi pit stop, suhu aspal, suhu udara, dan peluang hujan.

• 🚩 Pesan Resmi Race Control Instan
Pemberitahuan Safety Car (SC, VSC), bendera kuning/merah, pelanggaran batas lintasan (track limits), dan investigasi steward secara penuh tanpa terpotong.

• 📅 Kalender Balap 2026 & Spesifikasi Sirkuit
Jadwal sesi otomatis dikonversi ke zona waktu lokal Anda (WIB/WITA/WIT). Dilengkapi hitung mundur akhir pekan balap dan diagram sirkuit.

• 🏆 Klasemen Pembalap & Konstruktor
Profil lengkap pembalap, foto HD resmi, serta klasemen konstruktor dengan livery tim terkini.

---
DISCLAIMER & KEPATUHAN HAK CIPTA:
Aplikasi ini adalah aplikasi buatan penggemar independen dan tidak terafiliasi, didukung, disponsori, atau terkait secara resmi dengan Formula One Licensing B.V., Formula One Management, FIA, maupun tim Formula 1 manapun.

F1, FORMULA ONE, FORMULA 1, FIA FORMULA ONE WORLD CHAMPIONSHIP, GRAND PRIX, dan merek terkait adalah merek dagang terdaftar milik Formula One Licensing B.V. Semua nama tim dan pembalap digunakan murni sebagai referensi deskriptif penggemar di bawah prinsip fair use.

• Kebijakan Privasi: https://f1.dirzaaulia.com/privacy
• Kontak Bantuan: dirzaaulia@gmail.com
"""

FULL_DESC_ES = """Vive la Fórmula 1 como un ingeniero en el pit wall. PitWall ofrece tiempos en vivo profesionales, telemetría detallada de sectores, mensajes de control de carrera y repeticiones de sesiones históricas directamente en tu dispositivo Android.

⚡ CARACTERÍSTICAS PRINCIPALES:
• ⏱️ Tiempos en vivo y telemetría en tiempo real
• 🏎️ Centro de repetición interactivo vuelta a vuelta
• 🛞 Estrategia de neumáticos y radar meteorológico de pista
• 🚩 Mensajes instantáneos de control de carrera (SC, VSC, límites de pista)
• 📅 Calendario 2026 en tu zona horaria local
• 🏆 Clasificación de pilotos y constructores

---
AVISO LEGAL:
Esta aplicación es una app no oficial de fanáticos y no está afiliada ni respaldada por Formula One Licensing B.V. F1 es una marca registrada de Formula One Licensing B.V.
• Política de Privacidad: https://f1.dirzaaulia.com/privacy
"""

FULL_DESC_PT = """Experimente a Fórmula 1 como um engenheiro no pit wall. O PitWall oferece cronometragem ao vivo profissional, telemetria detalhada de setores, avisos de controle de corrida e replays históricos diretamente no seu Android.

⚡ PRINCIPAIS RECURSOS:
• ⏱️ Tempos ao vivo e telemetria em tempo real
• 🏎️ Replay volta a volta de sessões de corrida
• 🛞 Desgaste de pneus e meteorologia do circuito
• 🚩 Mensagens oficiais de controle de prova
• 📅 Calendário de corridas 2026 no seu fuso horário
• 🏆 Classificação de pilotos e construtores

---
AVISO LEGAL:
Este aplicativo é uma criação independente de fãs e não possui afiliação oficial com a Formula One Licensing B.V. F1 é marca registrada de Formula One Licensing B.V.
• Política de Privacidade: https://f1.dirzaaulia.com/privacy
"""

RELEASE_NOTES = [
    {
        "language": "id",
        "text": """🏁 PitWall: F1 Timing & Telemetry resmi meluncur!

✨ Tampilan Baru: Antarmuka Pit Wall bernuansa dark Obsidian Void yang dirancang untuk layar OLED.
⏱️ Live Timing & Replay: Pantau telemetri real-time, gap interval, sektor timing, dan replay balap lap-by-lap.
🏎️ Sirkuit & Spesifikasi: Kalender balap 2026 dengan konversi zona waktu lokal otomatis dan layout sirkuit resmi.
🚀 Navigasi Mulus: Transisi navigasi back native yang responsif dan gesit."""
    },
    {
        "language": "en-US",
        "text": """🏁 PitWall: F1 Timing & Telemetry is now live!

✨ Pit-Wall UI: Sleek Obsidian Void dark theme tailored for high-contrast OLED displays.
⏱️ Live Timing & Replay: Real-time telemetry, sector split times, tyre compound tracking, and lap-by-lap replay archives.
🏎️ 2026 Race Calendar: Automatic local timezone conversion and detailed circuit technical specifications.
🚀 Smooth Navigation: Native back gestures and fluid pop transitions across all screens."""
    }
]

LISTINGS = [
    {
        "lang": "en-US",
        "title": APP_NAME,
        "short": SHORT_DESC_EN,
        "full": FULL_DESC_EN
    },
    {
        "lang": "id",
        "title": APP_NAME,
        "short": SHORT_DESC_ID,
        "full": FULL_DESC_ID
    },
    {
        "lang": "es-419",
        "title": APP_NAME,
        "short": SHORT_DESC_ES,
        "full": FULL_DESC_ES
    },
    {
        "lang": "es-ES",
        "title": APP_NAME,
        "short": SHORT_DESC_ES,
        "full": FULL_DESC_ES
    },
    {
        "lang": "pt-BR",
        "title": APP_NAME,
        "short": SHORT_DESC_PT,
        "full": FULL_DESC_PT
    }
]

def resolve_key_file():
    for path in KEY_CANDIDATES:
        if path and os.path.exists(path):
            return path
    env_data = os.environ.get("PLAY_CONSOLE_JSON_KEY")
    if env_data:
        tmp_path = "fastlane/play-console-key.json"
        os.makedirs(os.path.dirname(tmp_path), exist_ok=True)
        with open(tmp_path, "w", encoding="utf-8") as f:
            f.write(env_data)
        return tmp_path
    return None

def get_access_token(key_path):
    with open(key_path, "r", encoding="utf-8") as f:
        key_data = json.load(f)
        
    iat = int(time.time())
    payload = {
        "iss": key_data["client_email"],
        "sub": key_data["client_email"],
        "aud": "https://oauth2.googleapis.com/token",
        "iat": iat,
        "exp": iat + 3600,
        "scope": "https://www.googleapis.com/auth/androidpublisher"
    }
    
    encoded_jwt = jwt.encode(payload, key_data["private_key"], algorithm="RS256")
    resp = requests.post("https://oauth2.googleapis.com/token", data={
        "grant_type": "urn:ietf:params:oauth:grant-type:jwt-bearer",
        "assertion": encoded_jwt
    }, timeout=15)
    
    if resp.status_code != 200:
        print(f"Failed to authenticate with Google OAuth: {resp.status_code} {resp.text}", flush=True)
        sys.exit(1)
        
    return resp.json()["access_token"]

def build_aab():
    print("\n[Build] Building Release App Bundle (.aab)...", flush=True)
    version_code = str(int(time.time() - 1700000000))
    cmd = [
        "powershell",
        "-Command",
        f".\\gradlew.bat :app:bundleRelease -PVERSION_CODE={version_code}"
    ]
    t0 = time.time()
    res = subprocess.run(cmd, capture_output=True, text=True)
    if res.returncode != 0:
        print("  [FAIL] Gradle build failed:", flush=True)
        print(res.stderr or res.stdout, flush=True)
        sys.exit(1)
    print(f"  [OK] Release AAB successfully generated with Version Code: {version_code} in {time.time()-t0:.1f}s", flush=True)
    return version_code

def main():
    parser = argparse.ArgumentParser(description="Deploy PitWall AAB and update Store Listings on Google Play Console.")
    parser.add_argument("--track", default="production", choices=["internal", "alpha", "beta", "production"], help="Play Console track to release to.")
    parser.add_argument("--build", action="store_true", help="Automatically trigger gradle bundleRelease before deploying.")
    parser.add_argument("--skip-listings", action="store_true", help="Skip updating multilingual listings and descriptions.")
    parser.add_argument("--dry-run", action="store_true", help="Validate edit session without committing to Play Store.")
    args = parser.parse_args()

    print("==================================================", flush=True)
    print(f" PitWall: F1 Timing & Telemetry - Play Store Deployer", flush=True)
    print(f" Target Track: {args.track.upper()}", flush=True)
    print(f" Dry Run Mode: {args.dry_run}", flush=True)
    print("==================================================", flush=True)

    key_file = resolve_key_file()
    if not key_file:
        print("\n[ERROR] Google Play service account key not found!", flush=True)
        print("Please place 'play-console-key.json' in 'fastlane/' or set 'PLAY_CONSOLE_KEY_FILE'.", flush=True)
        sys.exit(1)

    print(f"[Auth] Using Service Account Key: {key_file}", flush=True)
    token = get_access_token(key_file)
    headers = {"Authorization": f"Bearer {token}"}

    if args.build or not os.path.exists(AAB_PATH):
        build_aab()

    if not os.path.exists(AAB_PATH):
        print(f"\n[ERROR] AAB file not found at: {AAB_PATH}", flush=True)
        sys.exit(1)

    # 1. Create Play Store Edit
    print("\n[1/4] Creating Google Play Console Edit session...", flush=True)
    edit_resp = requests.post(
        f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits",
        headers=headers,
        timeout=15
    )
    if edit_resp.status_code != 200:
        print(f"  [FAIL] Error creating edit: {edit_resp.status_code} {edit_resp.text}", flush=True)
        sys.exit(1)

    edit_id = edit_resp.json()["id"]
    print(f"  [OK] Active Edit ID: {edit_id}", flush=True)

    try:
        # 2. Upload the Release AAB Bundle
        aab_size_mb = os.path.getsize(AAB_PATH) / (1024 * 1024)
        print(f"\n[2/4] Uploading release App Bundle ({aab_size_mb:.1f} MB)...", flush=True)
        upload_url = (
            f"https://androidpublisher.googleapis.com/upload/androidpublisher/v3/applications/"
            f"{PACKAGE_NAME}/edits/{edit_id}/bundles"
        )
        upload_headers = {
            "Authorization": f"Bearer {token}",
            "Content-Type": "application/octet-stream"
        }
        t0 = time.time()
        with open(AAB_PATH, "rb") as f:
            aab_data = f.read()
        upload_resp = requests.post(upload_url, headers=upload_headers, data=aab_data, timeout=120)

        if upload_resp.status_code not in (200, 201):
            print(f"  [FAIL] Failed to upload AAB: {upload_resp.status_code} {upload_resp.text}", flush=True)
            sys.exit(1)

        bundle_info = upload_resp.json()
        version_code = bundle_info.get("versionCode")
        sha256 = bundle_info.get("sha256")
        print(f"  [OK] Successfully uploaded AAB in {time.time()-t0:.1f}s!", flush=True)
        print(f"       Version Code: {version_code}", flush=True)
        print(f"       SHA256: {sha256}", flush=True)

        # 3. Assign to Track
        print(f"\n[3/4] Assigning Version Code {version_code} to '{args.track}' track...", flush=True)
        track_url = (
            f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/"
            f"{PACKAGE_NAME}/edits/{edit_id}/tracks/{args.track}"
        )
        track_payload = {
            "track": args.track,
            "releases": [
                {
                    "name": f"1.0.{version_code}",
                    "versionCodes": [str(version_code)],
                    "status": "completed",
                    "releaseNotes": RELEASE_NOTES
                }
            ]
        }
        track_resp = requests.put(track_url, headers=headers, json=track_payload, timeout=20)
        if track_resp.status_code not in (200, 201):
            print(f"  [FAIL] Failed to assign track: {track_resp.status_code} {track_resp.text}", flush=True)
            sys.exit(1)
        print(f"  [OK] Assigned release 1.0.{version_code} to '{args.track}' track with multilingual release notes.", flush=True)

        # 4. Update Multilingual Listings
        if not args.skip_listings:
            print("\n[4/4] Updating store listings for all languages...", flush=True)
            for item in LISTINGS:
                lang = item["lang"]
                payload = {
                    "language": lang,
                    "title": item["title"],
                    "shortDescription": item["short"],
                    "fullDescription": item["full"]
                }
                res = requests.put(
                    f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}/listings/{lang}",
                    headers=headers,
                    json=payload,
                    timeout=15
                )
                if res.status_code in (200, 201):
                    print(f"  [OK] Updated listing for '{lang}' successfully.", flush=True)
                else:
                    print(f"  [FAIL] Failed to update listing for '{lang}': {res.status_code} {res.text}", flush=True)

        # 5. Commit or Validate Edit
        if args.dry_run:
            print("\n[Final] Validating edit (Dry-run mode)...", flush=True)
            val_res = requests.post(
                f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}:validate",
                headers=headers,
                timeout=20
            )
            if val_res.status_code == 200 or "changesNotSentForReview" in val_res.text:
                print("  [OK] Google Play deployment validation passed successfully! (Dry-run)", flush=True)
            else:
                print(f"  [FAIL] Validation returned: {val_res.status_code} {val_res.text}", flush=True)
            requests.delete(
                f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}",
                headers=headers,
                timeout=10
            )
            print("  [OK] Draft edit session discarded cleanly.", flush=True)
        else:
            print(f"\n[Final] Committing release directly for Google Play review ('{args.track}' track)...", flush=True)
            commit_url = f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}:commit"
            commit_res = requests.post(commit_url, headers=headers, timeout=30)
            if commit_res.status_code != 200 and "changesNotSentForReview" in commit_res.text:
                print("  [Notice] Managed publishing detected. Falling back to changesNotSentForReview=true...", flush=True)
                commit_url = f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}:commit?changesNotSentForReview=true"
                commit_res = requests.post(commit_url, headers=headers, timeout=30)
            if commit_res.status_code == 200:
                print(f"  [SUCCESS] All changes and AAB bundle ({version_code}) published and SUBMITTED FOR REVIEW on '{args.track.upper()}' track!", flush=True)
            else:
                print(f"  [FAIL] Failed to commit changes: {commit_res.status_code} {commit_res.text}", flush=True)
                sys.exit(1)

    except Exception as e:
        print(f"\n[EXCEPTION] Error during deployment: {e}", flush=True)
        try:
            requests.delete(
                f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}",
                headers=headers,
                timeout=10
            )
            print("  Discarded draft edit session.", flush=True)
        except:
            pass
        sys.exit(1)

if __name__ == "__main__":
    main()
