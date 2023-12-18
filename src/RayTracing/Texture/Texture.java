package RayTracing.Texture;

import RayTracing.Color;
import RayTracing.Point;

public interface Texture {
    Color value(double u, double v, Point p);
}
