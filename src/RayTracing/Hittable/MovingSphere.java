package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Material.Material;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec3;

public class MovingSphere implements Hittable {

    Point center0, center1;
    Vec3 centerVec;
    double radius;
    Material mat;
    AABB box;

    public MovingSphere(Point c0, Point c1, double radius, Material mat){
        this.center0 = c0;
        this.center1 = c1;
        this.centerVec = c1.minus(c0);
        this.radius = radius;
        this.mat = mat;

        Point radiusPoint = new Point(radius, radius, radius);
        AABB box0 = new AABB(center0.minus(radiusPoint), center0.add(radiusPoint));
        AABB box1 = new AABB(center1.minus(radiusPoint), center1.add(radiusPoint));
        box = new AABB(box0, box1);
    }

    public Point center(double time){
        return center0.add(centerVec.multiply(time));
    }

    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        Point oc = r.orig().minus(center(r.time()));
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
        Vec3 outward_normal = rec.p.minus(center(r.time())).divide(radius);
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

