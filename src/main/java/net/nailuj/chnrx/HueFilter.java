/*
 * Copyright (C) 2018 Zisis Relas, Florian Rost, Julian Blazek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.nailuj.chnrx;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

/**
 * HueFilter.java Zweck: Ermöglicht es ein Bild zu Filtern und dessen Hue des
 * HSB Werts anzupassen. Dazu wird jede Farbe zu HSB und wieder zu RGB
 * konvertiert.
 */
public class HueFilter extends RGBImageFilter {

    private float hsbvals[] = new float[3];
    float fgHue;
    float fgSaturation;
    float fgBrightness;

    public HueFilter(Color fg) {
        Color.RGBtoHSB(fg.getRed(), fg.getGreen(), fg.getBlue(), hsbvals);
        fgHue = hsbvals[0];
        fgSaturation = hsbvals[1];
        fgBrightness = hsbvals[2];
        canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        int alpha = (rgb >> 24) & 0xff;
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = (rgb) & 0xff;
        Color.RGBtoHSB(red, green, blue, hsbvals);
        float newHue = fgHue;
        float newSaturation = hsbvals[1] * fgSaturation;
        float newBrightness = hsbvals[2] * (hsbvals[1] * fgBrightness + (1 - hsbvals[1]));
        rgb = Color.HSBtoRGB(newHue, newSaturation, newBrightness);
        return (rgb & 0x00ffffff) | (alpha << 24);
    }
}
