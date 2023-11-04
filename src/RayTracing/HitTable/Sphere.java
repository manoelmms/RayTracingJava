package RayTracing.HitTable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Material.Material;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec3;

public class Sphere implements HitTable{

    Point center;
    double radius;
    Material mat;

    public Sphere(Point center, double radius, Material mat){
        this.center = center;
        this.radius = radius;
        this.mat = mat;
    }

    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        Point oc = r.orig().minus(center);
        double a = r.dir().lengthSquared();
        double half_b = oc.dot(r.dir());
        double c = oc.lengthSquared() - radius*radius;

        double discriminant = half_b*half_b - a*c;

        if(discriminant < 0)
            return false;

        double sqrt_discriminant = Math.sqrt(discriminant);
        double root = (-half_b - sqrt_discriminant) / a;

        if(root < t.min() || t.max() < root)
        {
            root = (-half_b + sqrt_discriminant) / a;
            if(root < t.min() || t.max() < root)
                return false;
        }

        rec.t = root;
        rec.p = r.at(root);
        Vec3 outward_normal = rec.p.minus(center).divide(radius);
        rec.set_face_normal(r, outward_normal);
        rec.mat = mat;

        return true;
    }
}
