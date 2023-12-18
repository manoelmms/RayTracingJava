package RayTracing.Material;

import RayTracing.Color;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Radiance;
import RayTracing.Point;
import RayTracing.Ray;

public interface Material {
    boolean scatter(Ray r, HitRecord rec, Radiance rad);

    default Color emitted(double u, double v, Point p){
        return new Color(0, 0, 0);
    }
}

