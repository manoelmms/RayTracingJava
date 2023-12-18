package RayTracing.Texture;

import RayTracing.Color;
import RayTracing.Point;

public class Noise implements Texture{
    Perlin noise;
    double scale;

    public Noise(double scale) {
        this.scale = scale;
        noise = new Perlin();
    }

    public Noise(){
        noise = new Perlin();
    }

    @Override
    public Color value(double u, double v, Point p) {
        Point s = p.multiply(scale);
        return new Color(1).multiply(0.5).multiply(1+Math.sin(s.z + 10 * noise.turbulence(s)));
    }
}
