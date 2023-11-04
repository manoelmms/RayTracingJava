package RayTracing.HitInfo;

import RayTracing.Color;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec3;

public class Radiance {
    public Color attenuation;
    public Ray scattered;

    public Radiance(){
        scattered = new Ray(new Point(0), new Vec3(0));
        attenuation = new Color(0);
    }
}
