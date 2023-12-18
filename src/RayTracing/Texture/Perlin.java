package RayTracing.Texture;

import RayTracing.Vec3;

public class Perlin {
    static final int pointCount = 256;
    Vec3[] ranVec;
    int[] permX, permY, permZ;

    public Perlin(){
        ranVec = new Vec3[pointCount];
        for(int i = 0; i < pointCount; i++){
            ranVec[i] = Vec3.unitVector(Vec3.random(-1, 1));
        }
        permX = perlinGeneratePerm();
        permY = perlinGeneratePerm();
        permZ = perlinGeneratePerm();
    }

    static int[] perlinGeneratePerm(){
        int[] p = new int[pointCount];
        for(int i = 0; i < pointCount; i++){
            p[i] = i;
        }
        permute(p, pointCount);
        return p;
    }

    static void permute(int[] p, int n){
        for(int i = n-1; i > 0; i--){
            int target = (int)(Math.random() * (i+1));
            int tmp = p[i];
            p[i] = p[target];
            p[target] = tmp;
        }
    }

    static double trilinearInterp(double[][][] c, double u, double v, double w){
        double accum = 0;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                for(int k = 0; k < 2; k++){
                    accum += (i*u + (1-i)*(1-u)) *
                            (j*v + (1-j)*(1-v)) *
                            (k*w + (1-k)*(1-w)) * c[i][j][k];
                }
            }
        }
        return accum;
    }

    static double perlinInterp(Vec3[][][] c, double u, double v, double w) {
        double uu = u*u*(3-2*u);
        double vv = v*v*(3-2*v);
        double ww = w*w*(3-2*w);
        double accum = 0.0;

        for (int i=0; i < 2; i++)
            for (int j=0; j < 2; j++)
                for (int k=0; k < 2; k++) {
                    Vec3 weight_v = new Vec3(u-i, v-j, w-k);
                    accum += (i*uu + (1-i)*(1-uu))
                            * (j*vv + (1-j)*(1-vv))
                            * (k*ww + (1-k)*(1-ww))
                            * c[i][j][k].dot(weight_v);
                }

        return accum;
    }

    public double noise(Vec3 p){
        double u = p.x - Math.floor(p.x);
        double v = p.y - Math.floor(p.y);
        double w = p.z - Math.floor(p.z);

        int i = (int)Math.floor(p.x);
        int j = (int)Math.floor(p.y);
        int k = (int)Math.floor(p.z);

        Vec3[][][] c = new Vec3[2][2][2];

        for(int di = 0; di < 2; di++){
            for(int dj = 0; dj < 2; dj++){
                for(int dk = 0; dk < 2; dk++){
                    c[di][dj][dk] = ranVec[
                            permX[(i+di) & 255] ^
                                    permY[(j+dj) & 255] ^
                                    permZ[(k+dk) & 255]
                            ];
                }
            }
        }

        return perlinInterp(c, u, v, w);
    }

    public double turbulence(Vec3 p){
        double accum = 0;
        int depth = 7;
        double weight = 1.0;
        for(int i = 0; i < depth; i++){
            accum += weight * noise(p);
            weight *= 0.5;
            p.equalMultiply(2);
        }
        return Math.abs(accum);
    }
}
