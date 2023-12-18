package RayTracing.Material;

import RayTracing.Color;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Radiance;
import RayTracing.Ray;
import RayTracing.Texture.SolidColor;
import RayTracing.Texture.Texture;
import RayTracing.Vec3;

import static RayTracing.Utility.randomDouble;

public class Dielectric implements Material{

    Texture albedo;
    double ir;

    public Dielectric(Color color, double irx){
        albedo = new SolidColor(color);
        ir = irx;
    }

    public Dielectric(Texture texture, double irx){
        albedo = texture;
        ir = irx;
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Radiance rad) {

        double refraction_ratio = rec.frontFace ? (1.0/ir):ir;

        Vec3 unit_direction = Vec3.unitVector(r.dir());
        double cos_theta = Math.min(unit_direction.negative().dot(rec.normal), 1.0);
        double sin_theta = Math.sqrt(1.0 - cos_theta*cos_theta);

        boolean cannot_refract = refraction_ratio*sin_theta > 1.0;
        boolean reflectance = reflectance(cos_theta, refraction_ratio) > randomDouble();

        Vec3 direction;
        if(cannot_refract || reflectance)
            direction = Vec3.reflect(unit_direction, rec.normal);
        else
            direction = Vec3.refract(unit_direction, rec.normal, refraction_ratio);

        rad.scattered = new Ray(rec.p, direction, r.time());
        rad.attenuation = albedo.value(rec.u, rec.v, rec.p);
        return true;
    }

    private double reflectance(double cosine, double ref_idx) {
        double r0 = (1.0-ref_idx) / (1.0+ref_idx);
        r0 = r0*r0;
        return r0+(1.0-r0)*Math.pow((1.0 - cosine),5.0);
    }
}
