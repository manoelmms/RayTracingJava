package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec3;

public class Translate implements Hittable{
    Hittable obj;
    Vec3 offset;
    AABB box;

    public Translate(Hittable obj, Point offset){
        this.obj = obj;
        this.offset = offset;
        box = obj.boundingBox().addOffset(offset);
    }

    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        Ray moved = new Ray(r.orig().minus(offset), r.dir(), r.time());
        if (!obj.hit(moved, t, rec))
            return false;
        rec.p.equalAdd(offset);
        return true;
    }

    @Override
    public AABB boundingBox() {
        return box;
    }
}
