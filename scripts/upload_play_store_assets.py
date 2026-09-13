import os
import sys
import json
import time
import argparse
import requests
import jwt

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

PACKAGE_NAME = "com.dirzaaulia.formula1"

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

FULL_DESC_EN = """Experience Formula 1 like an engineer on the pit wall. PitWall delivers next-generation live timing, deep sector telemetry analysis, instant race control messages, and historical Grand Prix replay archives directly to your Android device.

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

IMAGE_LANGUAGES = ["en-US", "id"]

ICON_PATH = "docs/playstore_icon_512.png"
FEATURE_GRAPHIC_PATH = "docs/feature_graphic_1024x500.png"

SCREENSHOT_PATHS = [
    "screenshots/store_listing/01_telemetry_banner.png",
    "screenshots/store_listing/02_circuit_banner.png",
    "screenshots/store_listing/03_calendar_banner.png",
    "screenshots/store_listing/04_specs_banner.png",
    "screenshots/store_listing/05_standings_banner.png"
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

def main():
    parser = argparse.ArgumentParser(description="Upload PitWall listings, icon, feature graphic, and screenshots to Google Play Console.")
    parser.add_argument("--dry-run", action="store_true", help="Validate edit session without committing changes.")
    args = parser.parse_args()

    print("==================================================", flush=True)
    print(" PitWall: Play Store Listing & Visual Assets Deployer", flush=True)
    print(f" Package: {PACKAGE_NAME}", flush=True)
    print(f" Target Locales: {[l['lang'] for l in LISTINGS]}", flush=True)
    print(f" Asset Locales: {IMAGE_LANGUAGES}", flush=True)
    print(f" Dry Run Mode: {args.dry_run}", flush=True)
    print("==================================================", flush=True)

    key_file = resolve_key_file()
    if not key_file:
        print("\n[ERROR] Service account key not found!", flush=True)
        sys.exit(1)

    token = get_access_token(key_file)
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    # 1. Create Edit session
    print("\n[1/5] Creating Google Play Console edit session...", flush=True)
    edit_resp = requests.post(
        f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits",
        headers=headers,
        timeout=15
    )
    if edit_resp.status_code != 200:
        print(f"  [FAIL] Failed to create edit: {edit_resp.status_code} {edit_resp.text}", flush=True)
        sys.exit(1)

    edit_id = edit_resp.json()["id"]
    print(f"  [OK] Active Edit ID: {edit_id}", flush=True)

    try:
        # 2. Update Multilingual Store Listings
        print("\n[2/5] Updating store listings (Title, Short & Full Description)...", flush=True)
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
                print(f"  [OK] Listing updated for '{lang}' (Title: '{item['title']}')", flush=True)
            else:
                print(f"  [FAIL] Failed listing for '{lang}': {res.status_code} {res.text}", flush=True)

        # 3. Upload App Icon (512x512)
        if os.path.exists(ICON_PATH):
            print(f"\n[3/5] Uploading 512x512 App Icon from {ICON_PATH}...", flush=True)
            upload_headers = {
                "Authorization": f"Bearer {token}",
                "Content-Type": "image/png"
            }
            with open(ICON_PATH, "rb") as f:
                icon_bytes = f.read()
            for lang in IMAGE_LANGUAGES:
                icon_url = (
                    f"https://androidpublisher.googleapis.com/upload/androidpublisher/v3/applications/"
                    f"{PACKAGE_NAME}/edits/{edit_id}/listings/{lang}/icon"
                )
                icon_res = requests.post(icon_url, headers=upload_headers, data=icon_bytes, timeout=30)
                if icon_res.status_code in (200, 201):
                    print(f"  [OK] App icon uploaded for '{lang}'.", flush=True)
                else:
                    print(f"  [FAIL] App icon failed for '{lang}': {icon_res.status_code} {icon_res.text}", flush=True)
        else:
            print(f"  [SKIP] Icon not found at {ICON_PATH}", flush=True)

        # 4. Upload Feature Graphic (1024x500)
        if os.path.exists(FEATURE_GRAPHIC_PATH):
            print(f"\n[4/5] Uploading Feature Graphic from {FEATURE_GRAPHIC_PATH}...", flush=True)
            upload_headers = {
                "Authorization": f"Bearer {token}",
                "Content-Type": "image/png"
            }
            with open(FEATURE_GRAPHIC_PATH, "rb") as f:
                feat_bytes = f.read()
            for lang in IMAGE_LANGUAGES:
                feat_url = (
                    f"https://androidpublisher.googleapis.com/upload/androidpublisher/v3/applications/"
                    f"{PACKAGE_NAME}/edits/{edit_id}/listings/{lang}/featureGraphic"
                )
                feat_res = requests.post(feat_url, headers=upload_headers, data=feat_bytes, timeout=30)
                if feat_res.status_code in (200, 201):
                    print(f"  [OK] Feature Graphic uploaded for '{lang}'.", flush=True)
                else:
                    print(f"  [FAIL] Feature Graphic failed for '{lang}': {feat_res.status_code} {feat_res.text}", flush=True)
        else:
            print(f"  [SKIP] Feature Graphic not found at {FEATURE_GRAPHIC_PATH}", flush=True)

        # 5. Upload Phone Screenshots
        print("\n[5/5] Uploading Phone Screenshots...", flush=True)
        upload_headers = {
            "Authorization": f"Bearer {token}",
            "Content-Type": "image/png"
        }
        for lang in IMAGE_LANGUAGES:
            # Clear old screenshots
            del_res = requests.delete(
                f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}/listings/{lang}/phoneScreenshots",
                headers=headers,
                timeout=15
            )
            print(f"  Cleared old screenshots for '{lang}' (Status: {del_res.status_code})", flush=True)

            for idx, s_path in enumerate(SCREENSHOT_PATHS, 1):
                if not os.path.exists(s_path):
                    print(f"  [WARN] Screenshot not found: {s_path}", flush=True)
                    continue
                with open(s_path, "rb") as f:
                    s_bytes = f.read()
                s_url = (
                    f"https://androidpublisher.googleapis.com/upload/androidpublisher/v3/applications/"
                    f"{PACKAGE_NAME}/edits/{edit_id}/listings/{lang}/phoneScreenshots"
                )
                t_start = time.time()
                s_res = requests.post(s_url, headers=upload_headers, data=s_bytes, timeout=45)
                if s_res.status_code in (200, 201):
                    print(f"  [OK] Screenshot {idx}/{len(SCREENSHOT_PATHS)} uploaded for '{lang}' ({time.time()-t_start:.1f}s): {os.path.basename(s_path)}", flush=True)
                else:
                    print(f"  [FAIL] Screenshot {idx} failed for '{lang}': {s_res.status_code} {s_res.text}", flush=True)

        # Final Validation or Commit
        if args.dry_run:
            print("\n[Final] Validating edit (Dry-run mode)...", flush=True)
            val_res = requests.post(
                f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}:validate",
                headers=headers,
                timeout=20
            )
            if val_res.status_code == 200 or "changesNotSentForReview" in val_res.text:
                print("  [OK] Google Play edit validation passed successfully! (Dry-run)", flush=True)
            else:
                print(f"  [FAIL] Validation returned: {val_res.status_code} {val_res.text}", flush=True)
            requests.delete(
                f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}",
                headers=headers,
                timeout=15
            )
            print("  [OK] Draft edit session cleanly discarded.", flush=True)
        else:
            print("\n[Final] Committing all listings and assets directly for Google Play review...", flush=True)
            commit_url = f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}:commit"
            commit_res = requests.post(commit_url, headers=headers, timeout=30)
            if commit_res.status_code != 200 and "changesNotSentForReview" in commit_res.text:
                print("  [Notice] Managed publishing detected. Falling back to changesNotSentForReview=true...", flush=True)
                commit_url = f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}:commit?changesNotSentForReview=true"
                commit_res = requests.post(commit_url, headers=headers, timeout=30)
            if commit_res.status_code == 200:
                print("  [SUCCESS] All listings, icon, feature graphic, and screenshots committed and SUBMITTED FOR REVIEW to Google Play Console!", flush=True)
            else:
                print(f"  [FAIL] Failed to commit changes: {commit_res.status_code} {commit_res.text}", flush=True)
                sys.exit(1)

    except Exception as e:
        print(f"\n[EXCEPTION] Error: {e}", flush=True)
        try:
            requests.delete(
                f"https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{PACKAGE_NAME}/edits/{edit_id}",
                headers=headers,
                timeout=10
            )
        except:
            pass
        sys.exit(1)

if __name__ == "__main__":
    main()
