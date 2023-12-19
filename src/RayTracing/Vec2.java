package RayTracing;

public class Vec2 {
    public double x, y;

    public Vec2(double e0, double e1) {
        x = e0;
        y = e1;
    }

    public Vec2(double s) {
        x = s;
        y = s;
    }

    public Vec2(double[] array){
        x = array[0];
        y = array[1];
    }

    public Vec2 negative() {
        return new Vec2(-x, -y);
    }

    public double[] toArray() {
        return new double[]{x, y};
    }

    public double lengthSquared() {
        return  x*x + y*y;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vec2 add(Vec2 v) {
        return new Vec2(x+v.x, y+v.y);
    }

    public Vec2 add(double s) {
        return new Vec2(x+s, y+s);
    }

    public Vec2 minus(Vec2 v) {
        return new Vec2(x-v.x, y-v.y);
    }

    public Vec2 minus(double s) {
        return new Vec2(x-s, y-s);
    }

    public Vec2 multiply(Vec2 v) {
        return new Vec2(x*v.x, y*v.y);
    }

    public Vec2 multiply(double s) {
        return new Vec2(x*s, y*s);
    }

    public Vec2 divide(Vec2 v) {
        return new Vec2(x/v.x, y/v.y);
    }

    public Vec2 divide(double s) {
        return new Vec2(x/s, y/s);
    }

    public double dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    public Vec2 cross(Vec2 v) {
        return new Vec2(x*v.y - v.x*y,
                        y*v.x - v.y*x);
    }

    public static Vec2 randomGaussian()
    {
        double u1    = Math.max(1e-38, Math.random());
        double u2    = Math.random();  // In [0, 1]
        double r     = Math.sqrt(-2.0 * Math.log(u1));
        double theta = 2 * Math.PI * u2;  // Random in [0, 2pi]
        return new Vec2(Math.cos(theta), Math.sin(theta)).multiply(r);
    }

    public static Vec2 random() {
        return new Vec2(Math.random(), Math.random());
    }
}
