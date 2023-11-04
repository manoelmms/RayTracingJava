package RayTracing.Material;

import RayTracing.Color;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Radiance;
import RayTracing.Ray;
import RayTracing.Vec3;

public class Metal implements Material{

    Color albedo;
    double fuzz;

    public Metal(Color color, double f){
        albedo = color;
        fuzz = f;
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Radiance rad) {
        Vec3 unitVector = Vec3.unitVector(r.dir());
        Vec3 reflected = Vec3.reflect(unitVector, rec.normal);
        Vec3 randV = Vec3.randomInUnitSphere();
        Vec3 fuzzV =  new Vec3(fuzz).multiply(randV);

        rad.scattered = new Ray(rec.p,reflected.add(fuzzV));
        rad.attenuation = albedo;

        return (rad.scattered.dir().dot(rec.normal) > 0);
    }
}
