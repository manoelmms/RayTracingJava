package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Ray;

import java.util.ArrayList;

public class HitList implements Hittable {
    public ArrayList<Hittable> objects;
    AABB box;

    public HitList(){
        objects = new ArrayList<>();
        box = new AABB();
    }

    public HitList(Hittable object){
        objects = new ArrayList<>();
        objects.add(object);
        box = new AABB();
    }

    public void add(Hittable object)
    {
        objects.add(object);
        box = new AABB(box, object.boundingBox());
    }

    public void clear()
    {
        objects.clear();
    }

    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        boolean hit_anything = false;
        for (Hittable object : objects) {
            if (object.hit(r, t, rec)) {
                t = new Interval(t.min(), rec.t);
                hit_anything = true;
            }
        }
        return hit_anything;
    }

    @Override
    public AABB boundingBox() {
        return box;
    }
}
