#!/bin/bash

if [[ -z "$PROJECT_PATH" ]]; then
  PROJECT_PATH=$(git rev-parse --show-toplevel)
fi

CARD_DIR="$PROJECT_PATH/imgtest"
EMPTY="$PROJECT_PATH/frontend2/static/nuo/card/empty.png"
FONT="$PROJECT_PATH/script/JetBrainsMono-Bold.ttf"
SYMBOLFONT="$PROJECT_PATH/script/fontawesome-webfont.ttf"

mkdir -p "$CARD_DIR"

declare -A colors
colors["red"]="#ff392f"
colors["blue"]="#0059ff"
colors["green"]="#29f33a"
colors["yellow"]="#ffd100"

convert "$EMPTY" -fill black \
  -draw "roundrectangle 10,10 245,370 10,10" \
  -fill red \
  -stroke white \
  -strokewidth 12 \
  -draw "translate 127,190 skewX -20 ellipse 0,0 105,140 0,360" \
  -fill white \
  -stroke black \
  -strokewidth 2 \
  -gravity Center \
  -font "$FONT" \
  -pointsize 100 \
  -draw "rotate -20 text 0,0 NUO" \
  "$CARD_DIR/back.png"

for color in red blue green yellow; do
    c=${color:0:1}
    for number in $(seq 0 9); do
        convert "$EMPTY" -fill "${colors[${color}]}" \
            -draw "roundrectangle 10,10 245,370 10,10" \
            -fill white \
            -stroke white \
            -strokewidth 12 \
            -draw "translate 127,190 skewX -20 ellipse 0,0 105,140 0,360" \
            -fill "${colors[${color}]}" \
            -stroke black \
            -strokewidth 2 \
            -gravity Center \
            -font "$FONT" \
            -pointsize 175 \
            -draw "text 0,0 '$number'" \
            "$CARD_DIR/${c}$number.png"
    done
            convert "$EMPTY" -fill "${colors[${color}]}" \
            -draw "roundrectangle 10,10 245,370 10,10" \
            -fill white \
            -stroke white \
            -strokewidth 12 \
            -draw "translate 127,190 skewX -20 ellipse 0,0 105,140 0,360" \
            -fill "${colors[${color}]}" \
            -stroke black \
            -strokewidth 2 \
            -gravity Center \
            -font "$SYMBOLFONT" \
            -pointsize 175 \
            -draw "text 0,0 ''" \
            "$CARD_DIR/${c}s.png"
            
                        convert "$EMPTY" -fill "${colors[${color}]}" \
            -draw "roundrectangle 10,10 245,370 10,10" \
            -fill white \
            -stroke white \
            -strokewidth 12 \
            -draw "translate 127,190 skewX -20 ellipse 0,0 105,140 0,360" \
            -fill "${colors[${color}]}" \
            -stroke black \
            -strokewidth 2 \
            -gravity Center \
            -font "$SYMBOLFONT" \
            -pointsize 175 \
            -draw "text 0,0 ''" \
            "$CARD_DIR/${c}r.png"

                                    convert "$EMPTY" -fill "${colors[${color}]}" \
            -draw "roundrectangle 10,10 245,370 10,10" \
            -fill white \
            -stroke white \
            -strokewidth 12 \
            -draw "translate 127,190 skewX -20 ellipse 0,0 105,140 0,360" \
            -fill "${colors[${color}]}" \
            -stroke black \
            -strokewidth 2 \
            -gravity Center \
            -font "$FONT" \
            -pointsize 130 \
            -draw "text 0,0 '+2'" \
            "$CARD_DIR/${c}d.png"

    convert "$EMPTY" -fill black \
  -draw "roundrectangle 10,10 245,370 10,10" \
  -stroke white \
  -strokewidth 12 \
  -fill "${colors["red"]}" \
  -draw "path 'M 127,190 L 127,63 A 105,140 0 0,1 239,190'" \
  -fill "${colors["blue"]}" \
  -draw "path 'M 127,190 L 239,190 A 105,140 0 0,1 127,364'" \
  -fill "${colors["green"]}" \
  -draw "path 'M 127,190 L 127,364 A 105,140 0 0,1 16,190'" \
  -fill "${colors["yellow"]}" \
  -draw "path 'M 127,190 L 16,190 A 105,140 0 0,1 127,63'" \
  -fill white \
  -stroke black \
  -strokewidth 2 \
  -gravity Center \
  -font "$FONT" \
  -pointsize 80 \
  -draw "text 0,0 Wild" \
  "$CARD_DIR/xw.png"

      convert "$EMPTY" -fill black \
  -draw "roundrectangle 10,10 245,370 10,10" \
  -stroke white \
  -strokewidth 12 \
  -fill "${colors["red"]}" \
  -draw "path 'M 127,190 L 127,63 A 105,140 0 0,1 239,190'" \
  -fill "${colors["blue"]}" \
  -draw "path 'M 127,190 L 239,190 A 105,140 0 0,1 127,364'" \
  -fill "${colors["green"]}" \
  -draw "path 'M 127,190 L 127,364 A 105,140 0 0,1 16,190'" \
  -fill "${colors["yellow"]}" \
  -draw "path 'M 127,190 L 16,190 A 105,140 0 0,1 127,63'" \
  -fill white \
  -stroke black \
  -strokewidth 2 \
  -gravity Center \
  -font "$FONT" \
  -pointsize 130 \
  -draw "text 0,0 '+4'" \
  "$CARD_DIR/xx.png"
done