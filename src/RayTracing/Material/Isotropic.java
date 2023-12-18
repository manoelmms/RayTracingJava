package RayTracing.Material;

import RayTracing.Color;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Radiance;
import RayTracing.Hittable.Hittable;
import RayTracing.Ray;
import RayTracing.Texture.SolidColor;
import RayTracing.Texture.Texture;
import RayTracing.Vec3;

public class Isotropic implements Material{
    Texture albedo;

    public Isotropic(Color albedo){
        this.albedo = new SolidColor(albedo);
    }

    public Isotropic(Texture albedo){
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Radiance rad) {
        rad.scattered = new Ray(rec.p, Vec3.randomUnitVector(), r.time());
        rad.attenuation = albedo.value(rec.u, rec.v, rec.p);
        return true;
    }
}
