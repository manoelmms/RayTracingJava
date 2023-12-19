package RayTracing.Hittable;

import RayTracing.*;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Material.Material;

import static RayTracing.Utility.infinity;
import static java.lang.Math.*;

public class Triangle implements Hittable{

    public Vec3[] vertices;
    public Vec2[] uvs;
    Vec3 normal, e1, e2;
    Material mat;

    public Triangle(Vec3[] vertices, Material material){
        this.vertices = vertices;
        this.mat = material;
        this.e1 = vertices[1].minus(vertices[0]);
        this.e2 = vertices[2].minus(vertices[0]);
        this.normal = Vec3.unitVector(e1.cross(e2));
    }

    public Triangle(Vec3[] vertices, Vec2[] uvs, Material material){
        this.vertices = vertices;
        this.uvs = uvs;
        this.mat = material;
        this.e1 = vertices[1].minus(vertices[0]);
        this.e2 = vertices[2].minus(vertices[0]);
        this.normal = Vec3.unitVector(e1.cross(e2));
    }

    private Vec2 calculateObjTextureUV(double u, double v){
        return  (uvs[2].multiply(u))
                .add(uvs[1].multiply(v))
                .add(uvs[0].multiply((1-u-v)));
    }

    private void convertToObjUV(HitRecord rec){
        if (uvs != null){
            Vec2 uv = calculateObjTextureUV(rec.u, rec.v);
            rec.u = uv.x;
            rec.v = uv.y;
        }
    }

    @Override
    public boolean hit(Ray r, Interval it, HitRecord rec) {
        if(abs(normal.dot(r.dir())) < 1e-8)
            return false;

        Vec3 s0  = r.orig().minus(vertices[0]);
        Vec3 s1 = r.dir().cross(e2);
        Vec3 s2 = s0.cross(e1);

        double s1e1 = s1.dot(e1);
        double t = s2.dot(e2) / s1e1;

        if (t < it.min() || t > it.max())
            return false;

        double b1 = s1.dot(s0) / s1e1;
        if (b1 > 0) {
            double b2 = s2.dot(r.dir()) / s1e1;
            if (b2 > 0) {
                if ((1-b1-b2) > 0) {
                    rec.t = t;
                    rec.u = b2;
                    rec.v = b1;
                    this.convertToObjUV(rec);
                    rec.p = r.at(t);
                    rec.setFaceNormal(r, normal);
                    rec.mat = mat;

                    return true;
                } else return false;
            } else return false;
        }
        return false;
    }

    @Override
    public AABB boundingBox() {
        Point min = new Point(infinity, infinity, infinity);
        Point max = new Point(-infinity, -infinity, -infinity);

        for (int i = 0; i < 3; ++i) {
            min = new Point(min(min.x, vertices[i].x), min(min.y, vertices[i].y), min(min.z, vertices[i].z));
            max = new Point(max(max.x, vertices[i].x), max(max.y, vertices[i].y), max(max.z, vertices[i].z));
        }
        return new AABB(min.minus(1e-8), max.add(1e-8));
    }
}
