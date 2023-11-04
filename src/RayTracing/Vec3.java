package RayTracing;

import static RayTracing.Utility.randomDouble;
public sealed class Vec3 permits Point, Color {
    double x, y, z;

    public Vec3(double e0, double e1, double e2) {
        x = e0;
        y = e1;
        z = e2;
    }

    public Vec3(double s) {
        x = s;
        y = s;
        z = s;
    }

    public Vec3 negative() {
        return new Vec3(-x, -y, -z);
    }

    public double[] toArray() {
        return new double[]{x, y, z};
    }

    public double lengthSquared() {
        return  x*x + y*y + z*z;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public void equalAdd(Vec3 v) {
        x+=v.x;
        y+=v.y;
        z+=v.z;
    }
    public void equalAdd(double s) {
        x+=s;
        y+=s;
        z+=s;
    }

    public void equalMinus(Vec3 v) {
        x-=v.x;
        y-=v.y;
        z-=v.z;
    }

    public void equalMinus(double s) {
        x-=s;
        y-=s;
        z-=s;
    }

    public void equalMultiply(Vec3 v) {
        x*=v.x;
        y*=v.y;
        z*=v.z;
    }

    public void equalMultiply(double s) {
        x*=s;
        y*=s;
        z*=s;
    }

    public void equalDivide(Vec3 v) {
        x/=v.x;
        y/=v.y;
        z/=v.z;
    }

    public void equalDivide(double s) {
        x/=s;
        y/=s;
        z/=s;
    }

    public Vec3 add(Vec3 v) {
        return new Vec3(x+v.x, y+v.y, z+v.z);
    }

    public Vec3 add(double s) {
        return new Vec3(x+s, y+s, z+s);
    }

    public Vec3 minus(Vec3 v) {
        return new Vec3(x-v.x, y-v.y, z-v.z);
    }

    public Vec3 minus(double s) {
        return new Vec3(x-s, y-s, z-s);
    }

    public Vec3 multiply(Vec3 v) {
        return new Vec3(x*v.x, y*v.y, z*v.z);
    }

    public Vec3 multiply(double s) {
        return new Vec3(x*s, y*s, z*s);
    }

    public Vec3 divide(Vec3 v) {
        return new Vec3(x/v.x, y/v.y, z/v.z);
    }

    public Vec3 divide(double s) {
        return new Vec3(x/s, y/s, z/s);
    }

    public double dot(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vec3 cross(Vec3 v) {
        return new Vec3(y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
    }

    public Vec3 pow(Vec3 v) {
        return new Vec3(Math.pow(x,v.x), Math.pow(y,v.y), Math.pow(z,v.z));
    }

    public Vec3 pow(double d) {
        return new Vec3(Math.pow(x,d), Math.pow(y,d), Math.pow(z,d));
    }

    public static Vec3 abs(Vec3 v) {
        return new Vec3(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z));
    }

    public static Vec3 exp(Vec3 v) {
        return new Vec3(Math.exp(v.x), Math.exp(v.y), Math.exp(v.z));
    }

    public static Vec3 unitVector(Vec3 v) {
        return v.divide(v.length());
    }

    public static Vec3 random() {
        return new Vec3(randomDouble(), randomDouble(), randomDouble());
    }

    public static Vec3 random(double min, double max) {
        return new Vec3(randomDouble(min,max),
                randomDouble(min,max),
                randomDouble(min,max));
    }

    public static Vec3 randomInUnitSphere() {
        while(true) {
            Vec3 p = random(-1,1);
            if(p.lengthSquared() >= 1) continue;
            return p;
        }
    }

    public static Vec3 randomUnitVector() {
        return unitVector(randomInUnitSphere());
    }

    public static Vec3 randomInHemisphere(Vec3 normal) {
        Vec3 in_unit_sphere = randomInUnitSphere();
        if(in_unit_sphere.dot(normal) > 0.0)
            return in_unit_sphere;
        else
            return in_unit_sphere.negative();
    }

    public boolean nearZero() {
        double s = 1e-8; // Approx
        return (Math.abs(x) < s) && (Math.abs(y) < s) && (Math.abs(z) < s);
    }

    public static Vec3 reflect(Vec3 v, Vec3 n) {
        double vn = v.dot(n);
        return v.minus(n.multiply(2*vn));
    }

    public static Vec3 refract(Vec3 uv, Vec3 n, double etai_over_etat) {
        double cos_theta = Math.min(uv.negative().dot(n), 1.0);
        Vec3 r_out_perp = uv.add(n.multiply(cos_theta)).multiply(etai_over_etat);
        Vec3 r_out_parallel = n.multiply(-Math.sqrt(Math.abs(1.0 - r_out_perp.lengthSquared())));
        return r_out_perp.add(r_out_parallel);
    }

    public static Vec3 randomInUnitDisk() {
        while(true) {
            Vec3 p = new Vec3(randomDouble(-1,1), randomDouble(-1,1), 0);
            if(p.lengthSquared() >= 1) continue;
            return p;
        }
    }
}
