package RayTracing.Material;

import RayTracing.Color;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Radiance;
import RayTracing.Ray;
import RayTracing.Texture.SolidColor;
import RayTracing.Texture.Texture;
import RayTracing.Vec3;

public class Lambertian implements Material{

    Texture albedo;

    public Lambertian(Color color){
        albedo = new SolidColor(color);
    }

    public Lambertian(Texture texture){
        albedo = texture;
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Radiance rad) {
        Vec3 scatter_direction = rec.normal.add(Vec3.randomUnitVector());

        if(scatter_direction.nearZero())
            scatter_direction = rec.normal;

        rad.scattered = new Ray(rec.p, scatter_direction, r.time());
        rad.attenuation = albedo.value(rec.u, rec.v, rec.p);

        return true;
    }
}
