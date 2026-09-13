import os
from PIL import Image, ImageDraw, ImageFont, ImageFilter

def create_rounded_mask(size, radius):
    mask = Image.new('L', size, 0)
    draw = ImageDraw.Draw(mask)
    draw.rounded_rectangle([(0, 0), size], radius=radius, fill=255)
    return mask

def generate_feature_graphic(output_path: str = "docs/feature_graphic_1024x500.png"):
    WIDTH = 1024
    HEIGHT = 500
    
    # 1. Base Canvas - Deep Obsidian Carbon
    base = Image.new('RGBA', (WIDTH, HEIGHT), (8, 10, 14, 255))
    draw = ImageDraw.Draw(base)
    
    # Horizontal gradient
    for x in range(WIDTH):
        ratio = x / WIDTH
        r = int(9 * (1 - ratio) + 16 * ratio)
        g = int(11 * (1 - ratio) + 14 * ratio)
        b = int(16 * (1 - ratio) + 20 * ratio)
        draw.line([(x, 0), (x, HEIGHT)], fill=(r, g, b, 255))
        
    # 2. Ambient Red Glow & Dynamic Racing Lighting
    glow = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
    glow_draw = ImageDraw.Draw(glow)
    
    # Crimson glow from right side (behind device)
    glow_draw.ellipse([(650, 40), (1060, 480)], fill=(225, 6, 0, 85))
    # Subtle top-left cyan glow
    glow_draw.ellipse([(-100, -100), (450, 350)], fill=(6, 182, 212, 35))
    glow = glow.filter(ImageFilter.GaussianBlur(90))
    base = Image.alpha_composite(base, glow)
    
    # 3. Dynamic speed / apex curves in background
    curves = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
    c_draw = ImageDraw.Draw(curves)
    # Red accent stripe
    c_draw.line([(620, 520), (880, -20)], fill=(225, 6, 0, 60), width=4)
    c_draw.line([(640, 520), (900, -20)], fill=(255, 255, 255, 25), width=2)
    c_draw.line([(660, 520), (920, -20)], fill=(225, 6, 0, 40), width=6)
    base = Image.alpha_composite(base, curves)

    # 4. Device Mockup on Right Side (Perspective / Layered)
    telemetry_raw = "screenshots/raw/01_telemetry_tower.png"
    if os.path.exists(telemetry_raw):
        phone_ui = Image.open(telemetry_raw).convert('RGBA')
        
        # Scale phone to fit nicely on the right
        PHONE_W = 340
        aspect = phone_ui.height / phone_ui.width
        PHONE_H = int(PHONE_W * aspect)
        phone_resized = phone_ui.resize((PHONE_W, PHONE_H), Image.Resampling.LANCZOS)
        
        # Crop top portion of the phone (first 540 px)
        PHONE_CROP_H = 460
        phone_cropped = phone_resized.crop((0, 0, PHONE_W, PHONE_CROP_H))
        
        PHONE_RADIUS = 36
        phone_mask = create_rounded_mask((PHONE_W, PHONE_CROP_H), PHONE_RADIUS)
        
        phone_x = 640
        phone_y = 50
        
        # Phone drop shadow
        phone_shadow = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
        ps_draw = ImageDraw.Draw(phone_shadow)
        ps_draw.rounded_rectangle(
            [(phone_x - 8, phone_y + 12), (phone_x + PHONE_W + 8, phone_y + PHONE_CROP_H + 16)],
            radius=PHONE_RADIUS + 8,
            fill=(0, 0, 0, 230)
        )
        phone_shadow = phone_shadow.filter(ImageFilter.GaussianBlur(30))
        base = Image.alpha_composite(base, phone_shadow)
        
        # Paste phone
        base.paste(phone_cropped, (phone_x, phone_y), phone_mask)
        
        # Phone border outline
        stroke = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
        s_draw = ImageDraw.Draw(stroke)
        s_draw.rounded_rectangle(
            [(phone_x, phone_y), (phone_x + PHONE_W, phone_y + PHONE_CROP_H)],
            radius=PHONE_RADIUS,
            outline=(255, 255, 255, 50),
            width=2
        )
        base = Image.alpha_composite(base, stroke)

    # 5. App Icon & Branding on Left Side
    icon_path = "docs/playstore_icon_512.png"
    if os.path.exists(icon_path):
        icon = Image.open(icon_path).convert('RGBA')
        ICON_SIZE = 76
        icon = icon.resize((ICON_SIZE, ICON_SIZE), Image.Resampling.LANCZOS)
        icon_mask = create_rounded_mask((ICON_SIZE, ICON_SIZE), 18)
        
        # Icon shadow
        icon_shadow = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
        is_draw = ImageDraw.Draw(icon_shadow)
        is_draw.rounded_rectangle(
            [(60 - 3, 55 + 4), (60 + ICON_SIZE + 3, 55 + ICON_SIZE + 8)],
            radius=20,
            fill=(0, 0, 0, 180)
        )
        icon_shadow = icon_shadow.filter(ImageFilter.GaussianBlur(12))
        base = Image.alpha_composite(base, icon_shadow)
        
        base.paste(icon, (60, 55), icon_mask)
        
        # Icon border
        ib_layer = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
        ib_draw = ImageDraw.Draw(ib_layer)
        ib_draw.rounded_rectangle(
            [(60, 55), (60 + ICON_SIZE, 55 + ICON_SIZE)],
            radius=18,
            outline=(255, 255, 255, 60),
            width=1
        )
        base = Image.alpha_composite(base, ib_layer)

    # 6. Typography & Value Propositions
    font_bold = "C:/Windows/Fonts/segoeuib.ttf"
    font_semi = "C:/Windows/Fonts/seguisb.ttf"
    font_reg = "C:/Windows/Fonts/segoeui.ttf"
    
    brand_font = ImageFont.truetype(font_bold, 38)
    pill_font = ImageFont.truetype(font_bold, 18)
    title_font = ImageFont.truetype(font_bold, 44)
    desc_font = ImageFont.truetype(font_semi, 22)
    feature_font = ImageFont.truetype(font_reg, 19)
    
    text_layer = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
    t_draw = ImageDraw.Draw(text_layer)
    
    # Top badge pill next to icon
    pill_text = "2026 OFFICIAL LIVE SEASON"
    p_bbox = pill_font.getbbox(pill_text)
    pw = p_bbox[2] - p_bbox[0] + 36
    ph = p_bbox[3] - p_bbox[1] + 14
    px = 155
    py = 55
    t_draw.rounded_rectangle(
        [(px, py), (px + pw, py + ph)],
        radius=ph // 2,
        fill=(225, 6, 0, 45),
        outline=(225, 6, 0, 180),
        width=1
    )
    t_draw.text((px + 18, py + 5), pill_text, font=pill_font, fill=(255, 120, 120, 255))
    
    # Brand Name
    t_draw.text((155, 94), "PitWall", font=brand_font, fill=(255, 255, 255, 255))
    # Red accent on "Wall"
    p_len = brand_font.getbbox("Pit")[2] - brand_font.getbbox("Pit")[0]
    t_draw.text((155 + p_len + 1, 94), "Wall", font=brand_font, fill=(225, 6, 0, 255))
    
    # Main Headline
    t_draw.text((60, 158), "Next-Gen F1 Live Timing", font=title_font, fill=(255, 255, 255, 255))
    t_draw.text((60, 210), "& Telemetry Companion", font=title_font, fill=(225, 6, 0, 255))
    
    # Tagline
    t_draw.text(
        (60, 275),
        "Engineered for true Formula 1 enthusiasts & pit wall analysts.",
        font=desc_font,
        fill=(148, 163, 184, 255)
    )
    
    # 3 Feature Checkpoints with glowing bullets
    features = [
        ("Live Micro-Sector Splits, Intervals & Speed Traps", (225, 6, 0)),
        ("Historic Lap Replay with Interactive Slider Scrubbing", (6, 182, 212)),
        ("Global 2026 Race Calendar & Local Timezone Conversion", (16, 185, 129)),
    ]
    
    fy = 328
    for text, b_color in features:
        # Draw a sleek circular dot instead of text bullet
        dot_r = 5
        dot_y = fy + 12
        t_draw.ellipse([(62, dot_y - dot_r), (62 + dot_r * 2, dot_y + dot_r)], fill=(b_color[0], b_color[1], b_color[2], 255))
        t_draw.text((82, fy + 4), text, font=feature_font, fill=(226, 232, 240, 255))
        fy += 36
        
    # Legal note small
    t_draw.text(
        (60, 460),
        "Unofficial fan companion app • Not affiliated with FIA or Formula One Licensing B.V.",
        font=ImageFont.truetype(font_reg, 13),
        fill=(100, 116, 139, 255)
    )
    
    base = Image.alpha_composite(base, text_layer)
    
    # Save final PNG (RGB 1024x500)
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    final_img = base.convert('RGB')
    final_img.save(output_path, 'PNG', quality=95)
    print(f"Generated Feature Graphic: {output_path} (Size: {final_img.size})")

if __name__ == "__main__":
    generate_feature_graphic()
