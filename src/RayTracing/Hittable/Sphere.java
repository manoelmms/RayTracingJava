package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Material.Material;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec3;

public class Sphere implements Hittable {

    Point center;
    double radius;
    Material mat;
    AABB box;

    public Sphere(Point center, double radius, Material mat){
        this.center = center;
        this.radius = radius;
        this.mat = mat;

        Point radiusPoint = new Point(radius, radius, radius);
        box = new AABB(center.minus(radiusPoint), center.add(radius));

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
        rec.setFaceNormal(r, outward_normal);
        getSphereUV(outward_normal, rec);
        rec.mat = mat;

        return true;
    }

    public void getSphereUV(Vec3 normal, HitRecord rec){
        double theta = Math.acos(-normal.y);
        double phi = Math.atan2(-normal.z, normal.x) + Math.PI;

        rec.u = phi/(2*Math.PI);
        rec.v = theta/Math.PI;
    }

    @Override
    public AABB boundingBox() {
        return box;
    }
}
