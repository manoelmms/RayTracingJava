package RayTracing;

public record Ray(Point orig, Vec3 dir, double time) {

    public Point at(double t) {
        // P(t) = A + tB (Linear interpolation)
        return orig.add(dir.multiply(t));
    }
}
