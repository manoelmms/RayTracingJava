package RayTracing.Material;

import RayTracing.Color;
import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Radiance;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Texture.SolidColor;
import RayTracing.Texture.Texture;

public class DiffuseLight implements Material{
    Texture emit;

    public DiffuseLight(Color c){
        this.emit = new SolidColor(c);
    }

    public DiffuseLight(Texture emit){
        this.emit = emit;
    }

    @Override
    public Color emitted(double u, double v, Point p) {
        return emit.value(u, v, p);
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Radiance rad) {
        return false;
    }
}
