package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Ray;


public interface Hittable {
    boolean hit(Ray r, Interval t, HitRecord rec);

    AABB boundingBox();
}
