package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Material.Material;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec3;

import static java.lang.Math.*;

public class Quad implements Hittable{
    Point q;
    Vec3 u, v, w;
    Vec3 normal;
    double d;
    Material mat;
    AABB box;

    public Quad(Point startPoint, Vec3 uSideLength, Vec3 vSideLength, Material m){
        q = startPoint;
        u = uSideLength;
        v = vSideLength;
        Vec3 n = u.cross(v);
        w = n.divide(n.dot(n));
        normal = Vec3.unitVector(n);
        d = normal.dot(q);
        mat = m;

        box = new AABB(q, q.add(u).add(v)).pad();
    }

    public static HitList box(Point a, Point b, Material mat){
        HitList sides = new HitList();

        // Construct the two opposite vertices with the minimum and maximum coordinates.
        Point min = new Point(min(a.x, b.x), min(a.y, b.y), min(a.z, b.z));
        Point max = new Point(max(a.x, b.x), max(a.y, b.y), max(a.z, b.z));

        Vec3 dx = new Vec3(max.x-min.x, 0, 0);
        Vec3 dy = new Vec3(0, max.y-min.y, 0);
        Vec3 dz = new Vec3(0, 0, max.z-min.z);

        sides.add(new Quad(new Point(min.x, min.y, max.z),  dx,            dy,            mat));
        sides.add(new Quad(new Point(max.x, min.y, max.z),  dz.negative(), dy,            mat));
        sides.add(new Quad(new Point(max.x, min.y, min.z),  dx.negative(), dy,            mat));
        sides.add(new Quad(new Point(min.x, min.y, min.z),  dz,            dy,            mat));
        sides.add(new Quad(new Point(min.x, max.y, max.z),  dx,            dz.negative(), mat));
        sides.add(new Quad(new Point(min.x, min.y, min.z),  dx,            dz,            mat));

        return sides;
    }

    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        double denom = normal.dot(r.dir());
        if (abs(denom) < 1e-8)
            return false;

        double dt = (d - normal.dot(r.orig())) / denom;
        if (!t.contains(dt))
            return false;

        Point intersection = r.at(dt);
        Vec3 planarHitptVector = intersection.minus(q);
        double alpha = w.dot(planarHitptVector.cross(v));
        double beta = w.dot(u.cross(planarHitptVector));

        if ((alpha < 0) || (1 < alpha) || (beta < 0) || (1 < beta))
            return false;

        rec.u = alpha;
        rec.v = beta;
        rec.t = dt;
        rec.p = intersection;
        rec.mat = mat;
        rec.setFaceNormal(r, normal);

        return true;
    }

    @Override
    public AABB boundingBox() {
        return box;
    }
}