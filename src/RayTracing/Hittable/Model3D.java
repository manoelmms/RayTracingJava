package RayTracing.Hittable;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.Material.Material;
import RayTracing.Point;
import RayTracing.Ray;
import RayTracing.Vec2;
import RayTracing.Vec3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Model3D implements Hittable {
    public List<Hittable> triangles;
    public AABB box;

    public Model3D() {
        box = new AABB();
        triangles = new ArrayList<>();
    }

    public void add(Triangle object) {
        triangles.add(object);
        box = new AABB(box, object.boundingBox());
    }

    public void translate(double x, double y, double z){
        box = new AABB();
        Vec3 move_vec = new Vec3(x,y,z);
        for (Hittable triangle : triangles) {
            if (triangle instanceof Triangle tri) {
                ((Triangle) triangle).vertices[0] = tri.vertices[0].add(move_vec);
                ((Triangle) triangle).vertices[1] = tri.vertices[1].add(move_vec);
                ((Triangle) triangle).vertices[2] = tri.vertices[2].add(move_vec);
                box = new AABB(box, triangle.boundingBox());
            }
        }
    }

    public void scale(double s){
        box = new AABB();
        for (int i = 0; i < triangles.size(); ++i) {
            if (triangles.get(i) instanceof Triangle tri) {
                Vec3[] new_vertexes = new Vec3[]{
                        tri.vertices[0].multiply(s),
                        tri.vertices[1].multiply(s),
                        tri.vertices[2].multiply(s)
                };
                triangles.set(i, new Triangle(new_vertexes, tri.uvs, tri.mat));
                box = new AABB(box, triangles.get(i).boundingBox());
            }
        }
    }

    public void rotate(double angle, int axis) {
        box = new AABB();
        for (int i = 0; i < triangles.size(); ++i) {
            if (triangles.get(i) instanceof Triangle tri) {
                Vec3[] new_vertexes = new Vec3[]{
                        tri.vertices[0].rotate(angle, axis),
                        tri.vertices[1].rotate(angle, axis),
                        tri.vertices[2].rotate(angle, axis)
                };
                triangles.set(i, new Triangle(new_vertexes, tri.uvs, tri.mat));
                box = new AABB(box, triangles.get(i).boundingBox());
            }
        }
    }


    @Override
    public boolean hit(Ray r, Interval t, HitRecord rec) {
        boolean hitAnything = false;
        for (Hittable object : triangles) {
            if (object.hit(r, t, rec)) {
                t = new Interval(t.min(), rec.t);
                hitAnything = true;
            }
        }
        return hitAnything;
    }

    @Override
    public AABB boundingBox() {
        return box;
    }

    public static Model3D loadModel(String path, Material mat) {
        Model3D model = new Model3D();

        try (InputStream fileData = new FileInputStream(path);
             InputStreamReader InputStreamReader = new InputStreamReader(fileData);
             BufferedReader buffer = new BufferedReader(InputStreamReader))
        {
            String line;
            String[] text;
            int[] index = new int[3];

            Point[] vertex;
            Vec2[] uv_data;
            Triangle triangle;

            ArrayList<Double> vertexData = new ArrayList<>();
            ArrayList<Double> uData = new ArrayList<>();
            ArrayList<Double> vData = new ArrayList<>();
            while((line=buffer.readLine())!=null)
            {
                text = line.split("\\s+");
                switch (text[0].trim()) {
                    case "v" -> {
                        // Vertex
                        vertexData.add(Double.parseDouble(text[1]));
                        //System.out.println(text[1]);
                        vertexData.add(Double.parseDouble(text[2]));
                        vertexData.add(Double.parseDouble(text[3]));
                    }
                    case "vt" -> {
                        // UV
                        uData.add(Double.parseDouble(text[1]));
                        vData.add(Double.parseDouble(text[2]));
                    }
                    case "f" -> {
                        // Face
                        String[] info1, info2, info3;
                        if (text[1].contains("/")) {
                            info1 = text[1].split("/");
                            info2 = text[2].split("/");
                            info3 = text[3].split("/");

                            vertex = new Point[3];
                            index[0] = Integer.parseInt(info1[0]) - 1;
                            vertex[0] = new Point(vertexData.get(index[0] * 3), vertexData.get(index[0] * 3 + 1), vertexData.get(index[0] * 3 + 2));
                            index[1] = Integer.parseInt(info2[0]) - 1;
                            vertex[1] = new Point(vertexData.get(index[1] * 3), vertexData.get(index[1] * 3 + 1), vertexData.get(index[1] * 3 + 2));
                            index[2] = Integer.parseInt(info3[0]) - 1;
                            vertex[2] = new Point(vertexData.get(index[2] * 3), vertexData.get(index[2] * 3 + 1), vertexData.get(index[2] * 3 + 2));

                            uv_data = new Vec2[3];
                            index[0] = Integer.parseInt(info1[1]) - 1;
                            uv_data[0] = new Vec2(uData.get(index[0]), vData.get(index[0]));
                            index[1] = Integer.parseInt(info2[1]) - 1;
                            uv_data[1] = new Vec2(uData.get(index[1]), vData.get(index[1]));
                            index[2] = Integer.parseInt(info3[1]) - 1;
                            uv_data[2] = new Vec2(uData.get(index[2]), vData.get(index[2]));

                            triangle = new Triangle(vertex, uv_data, mat);
                            model.add(triangle);
                        }else {
                            vertex = new Point[3];
                            index[0] = Integer.parseInt(text[1]) - 1;
                            vertex[0] = new Point(vertexData.get(index[0] * 3), vertexData.get(index[0] * 3 + 1), vertexData.get(index[0] * 3 + 2));
                            index[1] = Integer.parseInt(text[2]) - 1;
                            vertex[1] = new Point(vertexData.get(index[1] * 3), vertexData.get(index[1] * 3 + 1), vertexData.get(index[1] * 3 + 2));
                            index[2] = Integer.parseInt(text[3]) - 1;
                            vertex[2] = new Point(vertexData.get(index[2] * 3), vertexData.get(index[2] * 3 + 1), vertexData.get(index[2] * 3 + 2));

                            triangle = new Triangle(vertex, mat);
                            model.add(triangle);
                        }
                    }
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return model;
    }
}


