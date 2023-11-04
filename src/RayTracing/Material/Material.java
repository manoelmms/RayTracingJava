package RayTracing.Material;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Radiance;
import RayTracing.Ray;

public interface Material {
    boolean scatter(Ray r, HitRecord rec, Radiance rad);
}

