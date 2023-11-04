package RayTracing.HitTable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Ray;

import java.util.ArrayList;

public class HitList implements HitTable {

    public ArrayList<HitTable> objects;

    public HitList(){
        objects = new ArrayList<>();
    }

    public void add(HitTable object)
    {
        objects.add(object);
    }

    public void clear()
    {
        objects.clear();
    }

    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        boolean hit_anything = false;
        for (HitTable object : objects) {
            if (object.hit(r, t, rec)) {
                t = new Interval(t.min(), rec.t);
                hit_anything = true;
            }
        }
        return hit_anything;
    }
}
