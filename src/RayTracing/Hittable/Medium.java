package RayTracing.Hittable;

import RayTracing.Color;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Material.Isotropic;
import RayTracing.Material.Material;
import RayTracing.Ray;
import RayTracing.Texture.Texture;
import RayTracing.Vec3;

import static RayTracing.Utility.randomDouble;

public class Medium implements Hittable{
    Hittable boundary;
    Material phaseFunction;
    double negInvDensity;

    public Medium(Hittable boundary, double density, Color c) {
        this.boundary = boundary;
        negInvDensity = -1.0 / density;
        phaseFunction = new Isotropic(c);
    }

    public Medium(Hittable boundary, double density, Texture t) {
        this.boundary = boundary;
        negInvDensity = -1.0 / density;
        phaseFunction = new Isotropic(t);
    }

    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        HitRecord point1 = new HitRecord(), point2 = new HitRecord();
        if (!boundary.hit(r, new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), point1))
            return false;

        if (!boundary.hit(r, new Interval(point1.t+0.0001, Double.POSITIVE_INFINITY), point2))
            return false;

        if (point1.t < t.min()) point1.t = t.min();
        if (point2.t > t.max()) point2.t = t.max();

        if (point1.t >= point2.t)
            return false;

        if (point1.t < 0)
            point1.t = 0;

        double rayLength = r.dir().length();
        double distanceInsideBoundary = (point2.t - point1.t) * rayLength;
        double hitDistance = negInvDensity * Math.log(randomDouble());

        if (hitDistance > distanceInsideBoundary)
            return false;

        rec.t = point1.t + hitDistance / rayLength;
        rec.p = r.at(rec.t);
        rec.normal = new Vec3(1,0,0);  // arbitrary
        rec.frontFace = true;     // also arbitrary
        rec.mat = phaseFunction;

        return true;
    }

    @Override
    public AABB boundingBox() {
        return boundary.boundingBox();
    }
}
