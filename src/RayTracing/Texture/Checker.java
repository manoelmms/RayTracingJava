package RayTracing.Texture;

import RayTracing.Color;
import RayTracing.Point;
import static java.lang.Math.sin;
import static java.lang.Math.floor;

public class Checker implements Texture{
    Texture odd, even;
    double invScale;

    public Checker(double scale, Color c0, Color c1) {
        invScale = 1/scale;
        even = new SolidColor(c0);
        odd = new SolidColor(c1);
    }

    @Override
    public Color value(double u, double v, Point p) {
        double sum = (int)floor(invScale * p.x) + (int)floor(invScale * p.y) + (int)floor(invScale * p.z);

        boolean isEven = sum % 2 == 0;
        if(isEven){
            return even.value(u, v, p);
        }
        else{
            return odd.value(u, v, p);

        }
    }
}
