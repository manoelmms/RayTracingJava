package RayTracing.Texture;

import RayTracing.Color;
import RayTracing.Point;

public class SolidColor implements Texture{
    Color color;

    public SolidColor(Color c) {
        color = c;
    }

    @Override
    public Color value(double u, double v, Point p) {
        return color;
    }
}
