package RayTracing.Hittable;

import RayTracing.HitInfo.Interval;
import RayTracing.Point;
import RayTracing.Ray;

public class AABB {
    Interval x, y, z;

    AABB(){
        this.x = new Interval(0,0);
        this.y = new Interval(0,0);
        this.z = new Interval(0,0);
    }

    AABB(Interval x, Interval y, Interval z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    AABB(Point a, Point b){
        this.x = new Interval(Math.min(a.x, b.x), Math.max(a.x, b.x));
        this.y = new Interval(Math.min(a.y, b.y), Math.max(a.y, b.y));
        this.z = new Interval(Math.min(a.z, b.z), Math.max(a.z, b.z));
    }

    AABB(AABB a, AABB b){
        this.x = a.x.merge(b.x);
        this.y = a.y.merge(b.y);
        this.z = a.z.merge(b.z);
    }

    AABB pad(){
        double delta = 0.0001;
        Interval x = this.x.size() >= delta ? this.x : this.x.expand(delta);
        Interval y = this.y.size() >= delta ? this.y : this.y.expand(delta);
        Interval z = this.z.size() >= delta ? this.z : this.z.expand(delta);
        return new AABB(x, y, z);
    }

    Interval axis(int i){
        if(i == 1)
            return y;
        else if(i == 2)
            return z;
        else
            return x;
    }

    AABB addOffset(Point offset){
        return new AABB(x.addOffset(offset.x), y.addOffset(offset.y), z.addOffset(offset.z));
    }

    boolean greaterThan(AABB b, int axis){
        return axis(axis).min() > b.axis(axis).min();
    }

    boolean lessThan(AABB b, int axis){
        return axis(axis).max() < b.axis(axis).max();
    }

    // AABB Pixar`s algorithm
    boolean hit(Ray r, Interval t){
        for(int a = 0; a < 3; a++){
            double invD = 1.0 / r.dir().toArray()[a];
            double t0 = (axis(a).min() - r.orig().toArray()[a]) * invD;
            double t1 = (axis(a).max() - r.orig().toArray()[a]) * invD;
            if (invD < 0.0f){
                double s = t1;
                t1 = t0;
                t0 = s;
            }
            t = new Interval(Math.max(t0, t.min()), Math.min(t1, t.max()));
            if (t.max() <= t.min())
                return false;
        }
        return true;
    }
}
