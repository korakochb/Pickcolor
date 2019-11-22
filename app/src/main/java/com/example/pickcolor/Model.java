package com.example.pickcolor;

import android.graphics.Color;



public class Model  {

    private int red;
    private int green;
    private int blue;
    private int x;
    private int y;
    private int pixel;
    private String color;

    public Model() {
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPixel() {
        return pixel;
    }

    public void setPixel(int pixel) {
        this.pixel = pixel;
    }

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
