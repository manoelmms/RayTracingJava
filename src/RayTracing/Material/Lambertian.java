package RayTracing.Material;

import RayTracing.Color;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Radiance;
import RayTracing.Ray;
import RayTracing.Vec3;

public class Lambertian implements Material{

    Color albedo;

    public Lambertian(Color color){
        albedo = color;
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Radiance rad) {
        Vec3 scatter_direction = rec.normal.add(Vec3.randomUnitVector());

        if(scatter_direction.nearZero())
            scatter_direction = rec.normal;

        rad.scattered = new Ray(rec.p, scatter_direction);
        rad.attenuation = albedo;

        return true;
    }
}
