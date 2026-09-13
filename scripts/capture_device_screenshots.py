import os
import subprocess
import time

def run_adb(cmd: str):
    full_cmd = f"adb {cmd}"
    res = subprocess.run(full_cmd, shell=True, capture_output=True, text=True)
    return res.stdout.strip()

def tap(x: int, y: int, delay: float = 1.0):
    run_adb(f"shell input tap {x} {y}")
    time.sleep(delay)

def swipe(x1: int, y1: int, x2: int, y2: int, duration_ms: int = 350, delay: float = 1.0):
    run_adb(f"shell input swipe {x1} {y1} {x2} {y2} {duration_ms}")
    time.sleep(delay)

def capture_screenshot(destination_path: str):
    os.makedirs(os.path.dirname(destination_path), exist_ok=True)
    remote_path = "/sdcard/store_screen.png"
    run_adb(f"shell screencap -p {remote_path}")
    run_adb(f"pull {remote_path} \"{destination_path}\"")
    print(f"Captured: {destination_path}")

def main():
    print("=== Automated Device Screenshot Capturer (Motorola 1272x2772) ===")
    os.makedirs("screenshots/raw", exist_ok=True)

    # 1. Telemetry / Replay Tower
    print("Capturing 1: Telemetry Timing Tower...")
    tap(380, 2520, delay=1.2) # Tab 2: Telemetry
    tap(636, 950, delay=1.2)  # 2026 REPLAY button in standby
    tap(250, 445, delay=0.8)  # REPLAY toggle
    tap(165, 555, delay=0.8)  # TIMETABLE button to show timing tower
    tap(830, 555, delay=0.6)  # Advance lap (+1L)
    capture_screenshot("screenshots/raw/01_telemetry_tower.png")

    # 2. Circuit Radar GPS Tracker
    print("Capturing 2: Circuit Radar Tracker...")
    tap(325, 555, delay=1.2)  # CIRCUIT button
    capture_screenshot("screenshots/raw/02_circuit_radar.png")

    # 3. Calendar Tab
    print("Capturing 3: Racing Calendar...")
    tap(636, 2520, delay=1.2) # Tab 3: Calendar
    tap(150, 280, delay=0.8)  # Back button "< CALENDAR" if inside hub
    capture_screenshot("screenshots/raw/03_calendar.png")

    # 4. Circuit Specs & Race Hub
    print("Capturing 4: Circuit Specifications & Lap Record...")
    tap(850, 1120, delay=1.2) # Spanish Grand Prix "VIEW HUB ->"
    swipe(600, 2200, 600, 1100, 350, delay=1.2)
    capture_screenshot("screenshots/raw/04_circuit_specs.png")

    # 5. Championship Standings (Constructors)
    print("Capturing 5: Constructors Championship Standings...")
    tap(890, 2520, delay=1.2) # Tab 4: Standings
    tap(740, 560, delay=1.2)  # Constructors tab
    capture_screenshot("screenshots/raw/05_standings.png")

    print("\n[SUCCESS] All raw screenshots captured in screenshots/raw/")

if __name__ == "__main__":
    main()
