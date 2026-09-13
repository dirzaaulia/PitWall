import os
import glob
from PIL import Image, ImageDraw

BG_COLOR = (5, 5, 8, 255) # PitchBlack / splash_background #050508

DENSITIES = {
    "mipmap-mdpi": (48, 108),
    "mipmap-hdpi": (72, 162),
    "mipmap-xhdpi": (96, 216),
    "mipmap-xxhdpi": (144, 324),
    "mipmap-xxxhdpi": (192, 432)
}

def clean_foreground(img_path):
    img = Image.open(img_path).convert('RGBA')
    w, h = img.size
    pix = img.load()
    out = Image.new('RGBA', (w, h), (0, 0, 0, 0))
    out_pix = out.load()
    
    for x in range(w):
        for y in range(h):
            r, g, b, a = pix[x, y]
            if a > 0:
                lum = (r + g + b) / 3.0
                if lum > 72:
                    car_alpha = min(255, max(0, int(round((lum - 66.0) / (255.0 - 66.0) * 255.0))))
                    out_pix[x, y] = (255, 255, 255, car_alpha)
    return out

def create_legacy_icon(fg_clean, size, is_round=False):
    icon = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    mask = Image.new('L', (size, size), 0)
    mask_draw = ImageDraw.Draw(mask)
    
    if is_round:
        mask_draw.ellipse((0, 0, size - 1, size - 1), fill=255)
    else:
        radius = int(size * 0.22)
        mask_draw.rounded_rectangle((0, 0, size - 1, size - 1), radius=radius, fill=255)
        
    bg = Image.new('RGBA', (size, size), BG_COLOR)
    icon.paste(bg, (0, 0), mask)
    
    # Scale fg to fit within safe zone (~66% of size)
    fg_w, fg_h = fg_clean.size
    # Find bounding box of car
    bbox = fg_clean.getbbox()
    if bbox:
        car_cropped = fg_clean.crop(bbox)
        target_w = int(size * 0.70)
        scale = target_w / car_cropped.width
        target_h = int(car_cropped.height * scale)
        car_resized = car_cropped.resize((target_w, target_h), Image.Resampling.LANCZOS)
        
        pos_x = (size - target_w) // 2
        pos_y = (size - target_h) // 2
        icon.paste(car_resized, (pos_x, pos_y), car_resized)
        
    return icon

def main():
    res_dir = os.path.join("app", "src", "main", "res")
    
    for folder, (legacy_size, fg_size) in DENSITIES.items():
        folder_path = os.path.join(res_dir, folder)
        fg_path = os.path.join(folder_path, "ic_launcher_foreground.webp")
        launcher_path = os.path.join(folder_path, "ic_launcher.webp")
        round_path = os.path.join(folder_path, "ic_launcher_round.webp")
        
        if os.path.exists(fg_path):
            cleaned_fg = clean_foreground(fg_path)
            cleaned_fg.save(fg_path, "WEBP", quality=100)
            print(f"Cleaned {fg_path} ({fg_size}x{fg_size})")
            
            # Create ic_launcher.webp
            legacy = create_legacy_icon(cleaned_fg, legacy_size, is_round=False)
            legacy.save(launcher_path, "WEBP", quality=100)
            print(f"Updated {launcher_path} ({legacy_size}x{legacy_size})")
            
            # Create ic_launcher_round.webp
            round_icon = create_legacy_icon(cleaned_fg, legacy_size, is_round=True)
            round_icon.save(round_path, "WEBP", quality=100)
            print(f"Updated {round_path} ({legacy_size}x{legacy_size})")
            
    # Also create a high-res 512x512 Play Store icon
    xxxhdpi_fg = os.path.join(res_dir, "mipmap-xxxhdpi", "ic_launcher_foreground.webp")
    if os.path.exists(xxxhdpi_fg):
        cleaned_fg = Image.open(xxxhdpi_fg)
        store_icon = create_legacy_icon(cleaned_fg, 512, is_round=False)
        os.makedirs("docs", exist_ok=True)
        store_icon.save("docs/playstore_icon_512.png", "PNG")
        store_icon.save("app/src/main/ic_launcher-playstore.png", "PNG")
        print("Generated docs/playstore_icon_512.png and app/src/main/ic_launcher-playstore.png (512x512)")

if __name__ == "__main__":
    main()
