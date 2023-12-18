package RayTracing.HitInfo;

import RayTracing.Material.Material;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec3;

public class HitRecord {
    public double t;
    public Point p;
    public double u;
    public double v;
    public Vec3 normal;
    public boolean frontFace;
    public Material mat;


    public void setFaceNormal(Ray r, Vec3 outward_normal)
    {
        frontFace = r.dir().dot(outward_normal) < 0;
        normal = frontFace ? outward_normal : outward_normal.negative();
    }
}
