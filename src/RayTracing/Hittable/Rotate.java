package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec3;

import static java.lang.Math.*;

public class Rotate implements Hittable{

    Hittable obj;
    double sinTheta, cosTheta;
    int axis;
    AABB box;


    public Rotate(Hittable obj, double angle, int axis){
        this.obj = obj;
        this.axis = axis;
        double radians = Math.toRadians(angle);
        sinTheta = Math.sin(radians);
        cosTheta = Math.cos(radians);
        box = obj.boundingBox();
        Point min = new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Point max = new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    double x = i*box.x.max() + (1-i)*box.x.min();
                    double y = j*box.y.max() + (1-j)*box.y.min();
                    double z = k*box.z.max() + (1-k)*box.z.min();

                    Vec3 tester = new Vec3(x, y, z);
                    switch (axis) {
                        case 0 -> {
                            double newy =  cosTheta * y + sinTheta * z;
                            double newz = -sinTheta * y + cosTheta * z;
                            tester = new Vec3(x, newy, newz);
                        }
                        case 1 -> {
                            double newx =  cosTheta * x + sinTheta * z;
                            double newz = -sinTheta * x + cosTheta * z;
                            tester = new Vec3(newx, y, newz);
                        }
                        case 2 -> {
                            double newx =  cosTheta * x + sinTheta * y;
                            double newy = -cosTheta * x + cosTheta * y;
                            tester = new Vec3(newx, newy, z);
                        }
                    }


                    min = new Point(min(min.x, tester.x), min(min.y, tester.y), min(min.z, tester.z));
                    max = new Point(max(max.x, tester.x), max(max.y, tester.y), max(max.z, tester.z));

                }
            }
        }

        box = new AABB(min, max);
    }

    public boolean hit(Ray r, Interval t, HitRecord rec) {
        double[] origin = r.orig().toArray();
        double[] direction = r.dir().toArray();

        int r0 = 0, r1 = 0;
        switch (axis){
            case 0 -> {r0 = 1; r1 = 2;}
            case 1 -> {r0 = 0; r1 = 2;}
            case 2 -> {r0 = 0; r1 = 1;}
        }

        origin[r0] = cosTheta*r.orig().toArray()[r0] - sinTheta*r.orig().toArray()[r1];
        origin[r1] = sinTheta*r.orig().toArray()[r0] + cosTheta*r.orig().toArray()[r1];

        direction[r0] = cosTheta*r.dir().toArray()[r0] - sinTheta*r.dir().toArray()[r1];
        direction[r1] = sinTheta*r.dir().toArray()[r0] + cosTheta*r.dir().toArray()[r1];

        Ray rotated_r = new Ray(new Point(origin), new Vec3(direction), r.time());

        if (!obj.hit(rotated_r, t, rec))
            return false;

        double[] p = rec.p.toArray();
        p[r0] =  cosTheta*rec.p.toArray()[r0] + sinTheta*rec.p.toArray()[r1];
        p[r1] = -sinTheta*rec.p.toArray()[r0] + cosTheta*rec.p.toArray()[r1];

        double[] normal = rec.normal.toArray();
        normal[r0] =  cosTheta*rec.normal.toArray()[r0] + sinTheta*rec.normal.toArray()[r1];
        normal[r1] = -sinTheta*rec.normal.toArray()[r0] + cosTheta*rec.normal.toArray()[r1];

        rec.p = new Point(p);
        rec.normal = new Vec3(normal);

        return true;
    }

    @Override
    public AABB boundingBox() {
        return box;
    }

}
