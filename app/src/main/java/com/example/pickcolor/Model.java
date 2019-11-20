package com.example.pickcolor;

import android.graphics.Color;


public class Model extends MainActivity {

    public String getComplementaryColor(int colorToInvert) {
        String name = "";
        float hsv[] = new float[3];
        int r = (colorToInvert >> 16) & 0xFF;
        int g = (colorToInvert >> 8) & 0xFF;
        int b = (colorToInvert) & 0xFF;
        Color.RGBToHSV(r, g, b, hsv);
        if (hsv[1] < 0.1 && hsv[2] > 0.9) {
            name = "White";
        } else if (hsv[2] < 0.1) {
            name = "Black";
        } else {
            float deg = hsv[0];
            if (deg >= 0 && deg < 15) {
                name = "Red";
            } else if (deg >= 15 && deg < 45) {
                name = "Orange";
            } else if (deg >= 45 && deg < 65) {
                name = "Yellow";
            } else if (deg >= 65 && deg < 180) {
                name = "Green";
            } else if (deg >= 180 && deg < 210) {
                name = "Cyan";
            } else if (deg >= 210 && deg < 270) {
                name = "Blue";
            } else if (deg >= 270 && deg < 300) {
                name = "Purple";
            } else if (deg >= 300 && deg < 330) {
                name = "Pink";
            } else {
                name = "Red";
            }
        }
        return name;
    }


}
