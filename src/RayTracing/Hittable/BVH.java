package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Ray;

import java.util.ArrayList;
import java.util.List;

import static RayTracing.Utility.randomInt;

public class BVH implements Hittable{
    Hittable left;
    Hittable right;
    AABB box;
    Hittable object;

    public BVH(List<Hittable> objects){
        box = new AABB();
        int axis = randomInt(0,2);
        int objectSpan = objects.size();

        if (objectSpan == 1)
        {
            object = objects.get(0);
            box = object.boundingBox();
        }else if (objectSpan == 2)
        {
            if (boxLessThan(objects.get(0), objects.get(1), axis)) {
                left = objects.get(0);
                right = objects.get(1);
            } else {
                left = objects.get(1);
                right = objects.get(0);
            }
            box = new AABB(left.boundingBox(), right.boundingBox());
        }else
        {
            objects.sort((a, b) -> {
                if(boxLessThan(a, b, axis)) // if a < b
                    return 1;
                else if (boxGreaterThan(a, b, axis)) // if a > b
                    return -1;
                else
                    return 0;
            });

            int mid = objectSpan/2;
            left = new BVH(objects.subList(0, mid));
            right = new BVH(objects.subList(mid, objectSpan));
            box = new AABB(left.boundingBox(), right.boundingBox());
        }
    }

    boolean boxLessThan(Hittable a, Hittable b, int axis_index) {
        return a.boundingBox().axis(axis_index).min() < b.boundingBox().axis(axis_index).min();
    }

    boolean boxGreaterThan(Hittable a, Hittable b, int axis_index) {
        return a.boundingBox().axis(axis_index).min() > b.boundingBox().axis(axis_index).min();
    }

    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        if (object != null)
            return object.hit(r, t, rec);

        if (!box.hit(r, t))
            return false;

        rec.t = t.max();
        boolean hit_left = left.hit(r, new Interval(t.min(), rec.t), rec);
        boolean hit_right = right.hit(r, new Interval(t.min(), rec.t), rec);

        return hit_left || hit_right;
    }

    @Override
    public AABB boundingBox() {
        return box;
    }
}
