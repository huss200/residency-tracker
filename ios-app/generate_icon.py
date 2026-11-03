import os
import math
from PIL import Image, ImageDraw, ImageFont, ImageFilter

def create_app_icon(output_path):
    size = (1024, 1024)
    img = Image.new("RGBA", size, (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # 1. Royal Blue Gradient Background
    for y in range(1024):
        t = y / 1023.0
        # Gradient from deep midnight navy (14, 28, 64) -> royal blue (30, 64, 175) -> rich sapphire (29, 78, 216)
        r = int(14 * (1 - t)**2 + 30 * 2 * (1 - t) * t + 24 * t**2)
        g = int(28 * (1 - t)**2 + 64 * 2 * (1 - t) * t + 80 * t**2)
        b = int(64 * (1 - t)**2 + 175 * 2 * (1 - t) * t + 220 * t**2)
        draw.line([(0, y), (1023, y)], fill=(r, g, b, 255))

    # 2. Radial Soft Glow at Center-Top
    glow = Image.new("RGBA", size, (0, 0, 0, 0))
    glow_draw = ImageDraw.Draw(glow)
    center_x, center_y = 512, 440
    for radius in range(480, 0, -8):
        alpha = int((1 - (radius / 480.0)) * 65)
        glow_draw.ellipse(
            [center_x - radius, center_y - radius, center_x + radius, center_y + radius],
            fill=(96, 165, 250, alpha)
        )
    img = Image.alpha_composite(img, glow)
    draw = ImageDraw.Draw(img)

    # 3. Subtle Inner Vignette & Border
    draw.rounded_rectangle([24, 24, 1000, 1000], radius=180, outline=(255, 255, 255, 30), width=4)

    # 4. Passport / Academic Record Book Card
    # Shadow for Card
    card_shadow = Image.new("RGBA", size, (0, 0, 0, 0))
    cs_draw = ImageDraw.Draw(card_shadow)
    cs_draw.rounded_rectangle([270, 480, 754, 820], radius=44, fill=(0, 0, 0, 140))
    card_shadow = card_shadow.filter(ImageFilter.GaussianBlur(24))
    img = Image.alpha_composite(img, card_shadow)
    draw = ImageDraw.Draw(img)

    # Book / Passport Background (Deep Navy with Gold Accent)
    draw.rounded_rectangle([270, 470, 754, 810], radius=44, fill=(15, 23, 42, 245), outline=(234, 179, 8, 180), width=6)
    
    # Internal Passport/Record Page
    draw.rounded_rectangle([300, 500, 724, 780], radius=28, fill=(248, 250, 252, 255), outline=(203, 213, 225, 200), width=3)

    # Calendar / Stamp Grid inside page
    # Header bar on passport page
    draw.rounded_rectangle([300, 500, 724, 555], radius=28, fill=(37, 99, 235, 255))
    draw.rectangle([300, 530, 724, 555], fill=(37, 99, 235, 255)) # Square bottom corners of header

    # Mini Calendar Leaves / Grid Rows
    colors_dot = [(34, 197, 94), (59, 130, 246), (234, 179, 8), (168, 85, 247)]
    for row in range(3):
        y_pos = 585 + row * 55
        # Line represent trip
        draw.rounded_rectangle([330, y_pos, 694, y_pos + 36], radius=12, fill=(241, 245, 249, 255), outline=(226, 232, 240, 255), width=2)
        # Status pill indicator
        dot_color = colors_dot[row % len(colors_dot)]
        draw.ellipse([345, y_pos + 10, 361, y_pos + 26], fill=(*dot_color, 255))
        # Text placeholder bars
        draw.rounded_rectangle([375, y_pos + 12, 540, y_pos + 24], radius=6, fill=(148, 163, 184, 255))
        draw.rounded_rectangle([565, y_pos + 10, 675, y_pos + 26], radius=8, fill=(*dot_color, 45))
        draw.rounded_rectangle([580, y_pos + 13, 660, y_pos + 23], radius=5, fill=(*dot_color, 200))

    # Golden Official Stamp Circle on bottom right of the card
    stamp_x, stamp_y, stamp_r = 660, 740, 52
    draw.ellipse([stamp_x - stamp_r, stamp_y - stamp_r, stamp_x + stamp_r, stamp_y + stamp_r], outline=(202, 138, 4, 230), width=4)
    draw.ellipse([stamp_x - stamp_r + 6, stamp_y - stamp_r + 6, stamp_x + stamp_r - 6, stamp_y + stamp_r - 6], outline=(234, 179, 8, 160), width=2)
    # Mini Star in stamp
    draw.polygon([
        (stamp_x, stamp_y - 20), (stamp_x + 6, stamp_y - 6), (stamp_x + 20, stamp_y - 6),
        (stamp_x + 10, stamp_y + 4), (stamp_x + 14, stamp_y + 18), (stamp_x, stamp_y + 10),
        (stamp_x - 14, stamp_y + 18), (stamp_x - 10, stamp_y + 4), (stamp_x - 20, stamp_y - 6),
        (stamp_x - 6, stamp_y - 6)
    ], fill=(234, 179, 8, 240))

    # 5. Graduation Mortarboard (Cap)
    # Shadow for Mortarboard
    cap_shadow = Image.new("RGBA", size, (0, 0, 0, 0))
    cap_s_draw = ImageDraw.Draw(cap_shadow)
    # Cap skull base shadow
    cap_s_draw.polygon([(360, 360), (664, 360), (614, 460), (410, 460)], fill=(0, 0, 0, 150))
    # Cap diamond top shadow
    cap_s_draw.polygon([(512, 180), (840, 310), (512, 440), (184, 310)], fill=(0, 0, 0, 170))
    cap_shadow = cap_shadow.filter(ImageFilter.GaussianBlur(22))
    img = Image.alpha_composite(img, cap_shadow)
    draw = ImageDraw.Draw(img)

    # Skull Base (the cylindrical band under the cap)
    skull_pts = [(370, 340), (654, 340), (614, 435), (410, 435)]
    draw.polygon(skull_pts, fill=(15, 23, 42, 255))
    draw.line([(410, 435), (614, 435)], fill=(30, 41, 59, 255), width=4)
    # Gold headband ribbon
    draw.polygon([(390, 395), (634, 395), (620, 415), (404, 415)], fill=(234, 179, 8, 240))

    # Top Diamond of Mortarboard
    # Main diamond
    diamond_pts = [(512, 170), (832, 290), (512, 410), (192, 290)]
    draw.polygon(diamond_pts, fill=(30, 41, 59, 255))
    
    # 3D Highlight Top-Left edge
    draw.polygon([(512, 170), (512, 410), (192, 290)], fill=(51, 65, 85, 255)) # Left facet lighter
    draw.polygon([(512, 170), (832, 290), (512, 410)], fill=(15, 23, 42, 255)) # Right facet darker
    draw.line([(512, 170), (832, 290), (512, 410), (192, 290), (512, 170)], fill=(96, 165, 250, 160), width=4)

    # Center Cap Button
    button_center = (512, 290)
    draw.ellipse([button_center[0] - 18, button_center[1] - 18, button_center[0] + 18, button_center[1] + 18], fill=(234, 179, 8, 255), outline=(254, 240, 138, 255), width=3)

    # Golden Tassel Hanging Down
    # Ribbon curve from center to left edge
    tassel_pts = [(512, 290), (410, 310), (310, 350), (280, 400), (275, 470)]
    for i in range(len(tassel_pts) - 1):
        draw.line([tassel_pts[i], tassel_pts[i+1]], fill=(234, 179, 8, 255), width=8)

    # Tassel Band and Fringe
    draw.ellipse([265, 460, 285, 480], fill=(202, 138, 4, 255)) # Tassel ring
    # Fringe hairs
    draw.polygon([(262, 475), (288, 475), (300, 550), (250, 550)], fill=(234, 179, 8, 255))
    draw.polygon([(266, 475), (284, 475), (292, 545), (258, 545)], fill=(250, 204, 21, 255))

    # 6. Save final icon
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    img.save(output_path, "PNG")
    print(f"App icon successfully generated: {output_path} ({size[0]}x{size[1]})")

if __name__ == "__main__":
    out = os.path.join("ios-app", "ResidencyTracker", "Assets.xcassets", "AppIcon.appiconset", "icon-1024.png")
    create_app_icon(out)
