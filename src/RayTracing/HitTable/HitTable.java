package RayTracing.HitTable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Ray;


public interface HitTable {
    boolean hit(Ray r, Interval t, HitRecord rec);
}
