package RayTracing;

import RayTracing.Hittable.*;
import RayTracing.Material.*;
import RayTracing.Texture.*;

import static RayTracing.Hittable.Quad.box;
import static RayTracing.Utility.randomDouble;

public class Scene {
    Camera camera;
    Hittable world;
    int imageWidth;
    int imageHeight;

    Color backgroundColor;

    Scene(Camera camera, Hittable world){
        this.camera = camera;
        this.world = world;
        this.backgroundColor = null;
    }

    Scene(Camera camera, Hittable world, int width, int height){
        this.camera = camera;
        this.world = world;
        this.imageWidth = width;
        this.imageHeight = height;
        this.backgroundColor = null;
    }

    Scene(Camera camera, Hittable world, Color bgColor){
        this.camera = camera;
        this.world = world;
        this.backgroundColor = bgColor;
    }

    public static Scene initScene(){
        Point lookFrom = new Point(0, 0, 0);
        Point lookAt = new Point(0, 0, -1);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double dist_to_focus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 90,
                0.01, dist_to_focus, 16/9.0);

        HitList world = new HitList();

        Material ground_material = new Lambertian(new Color(0.8, 0.8, 0));
        Material center_material = new Lambertian(new Color(/*0.9,0.9,1.0*/0.8, 0.3, 0.3));
        Material right_material = new Metal(new Color(0.8, 0.8, 0.8), 0.2);
        Material left_material = new Dielectric(new Color(1.0, 1.0, 1.0), 1.6);

        Sphere ground = new Sphere(new Point(0, -100.5, -1), 100, ground_material);
        Sphere SphereC = new Sphere(new Point(0.0, 0.0, -1.0), 0.5, center_material);
        Sphere SphereR = new Sphere(new Point(1.0, 0.0, -1.0), 0.5, right_material);
        Sphere SphereL = new Sphere(new Point(-1.0, 0.0, -1.0), 0.5, left_material);

        world.add(ground);
        world.add(SphereC);
        world.add(SphereR);
        world.add(SphereL);

        return new Scene(camera, new HitList(new BVH(world.objects)));
    }

    public static Scene random_sphere_scene(){
        Point lookFrom =new Point(13, 2, 3);
        Point lookAt = new Point(0, 0, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 20,
                0.05, distToFocus, 16/9.0);


        HitList world = new HitList();

        Texture checker = new Checker(0.32, new Color(0.2,0.3,0.1), new Color(0.9,0.9,0.9));
        Material ground_material = new Lambertian(checker);
        Sphere ground = new Sphere(new Point(0, -1000, 0), 1000, ground_material);
        world.add(ground);
        for (int a = -11; a < 11; ++a){
            for (int b = -11; b < 11; ++b){
                double choose_mat = randomDouble();
                Point center = new Point(a + 0.9 * randomDouble(), 0.2, b + 0.9 * randomDouble());

                if (center.minus(new Point(4, 0.2, 0)).length() > 0.9) {
                    Material sphereMaterial;

                    if (choose_mat < 0.8) {
                        // diffuse
                        Color albedo = new Color(Vec3.random());
                        sphereMaterial = new Lambertian(albedo);
                        world.add(new Sphere(center,0.2, sphereMaterial));
                    } else if (choose_mat < 0.95) {
                        // metal
                        Color albedo = new Color(Vec3.random(0.5, 1));
                        double fuzz = randomDouble(0, 0.5);
                        sphereMaterial = new Metal(albedo, fuzz);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else {
                        // glass
                        sphereMaterial = new Dielectric(new Color(1),1.5);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    }
                }
            }
        }
        Material material1 = new Dielectric(new Color(1), 1.5);
        world.add(new Sphere(new Point(0, 1, 0), 1.0, material1));
        //world.add(new Medium(new Sphere(new Point(0, 1, 0), 0.8, material1), 2, new Color(0.98,0.3,0.3)));

        Material material2 = new Lambertian(new Color(Vec3.random()));
        world.add(new Sphere(new Point(-4, 1, 0), 1.0, material2));

        Material material3 = new Metal(new Color(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Point(4, 1, 0), 1.0, material3));


        return new Scene(camera, new HitList(new BVH(world.objects)));
    }

    public static Scene two_spheres_scene(){
        Point lookFrom = new Point(13, 2, 3);
        Point lookAt = new Point(0, 0, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 20,
                0.05, distToFocus, 16/9.0);

        HitList world = new HitList();

        Texture checker = new Checker(0.8, new Color(0.2,0.3,0.1), new Color(0.9,0.9,0.9));
        world.add(new Sphere(new Point(0, -10, 0), 10, new Lambertian(checker)));
        world.add(new Sphere(new Point(0, 10, 0), 10, new Lambertian(checker)));

        return new Scene(camera, new HitList(new BVH(world.objects)));
    }

    public static Scene earth(){
        Point lookFrom = new Point(13, 2, 3);
        Point lookAt = new Point(0, 0, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 20,
                0.05, distToFocus, 16/9.0);

        HitList world = new HitList();

        Texture earthTexture = new Image("./textures/earthmap.jpg");
        Material earthMaterial = new Lambertian(earthTexture);
        world.add(new Sphere(new Point(0, 0, 0), 2, earthMaterial));

        return new Scene(camera, new HitList(new BVH(world.objects)));
    }

    public static Scene perlinSpheres(){
        Point lookFrom = new Point(13, 2, 3);
        Point lookAt = new Point(0, 0, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 20,
                0.05, distToFocus, 16/9.0);

        HitList world = new HitList();
        Texture pertext = new Noise(3);
        world.add(new Sphere(new Point(0,-1000,0), 1000, new Lambertian(pertext)));
        world.add(new Sphere(new Point(0, 2, 0), 2, new Lambertian(pertext)));

        return new Scene(camera, new HitList(new BVH(world.objects)));
    }

    public static Scene quads(){
        Point lookFrom = new Point(0, 0, 9);
        Point lookAt = new Point(0, 0, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 90,
                0.05, distToFocus, 1);

        HitList world = new HitList();
        Material leftRed = new Lambertian(new Color(1,.2,.2));
        Material backGreen = new Lambertian(new Color(.2,1,.2));
        Material rightBlue = new Lambertian(new Color(.2,.2,1));
        Material upperOrange = new Lambertian(new Color(1,.5,0));
        Material lowerTeal = new Lambertian(new Color(.2,.8,.8));

        world.add(new Quad(new Point(-3,-2,5), new Vec3(0,0,-4), new Vec3(0,4,0), leftRed));
        world.add(new Quad(new Point(-2,-2,0), new Vec3(4,0,0), new Vec3(0,4,0), backGreen));
        world.add(new Quad(new Point(3,-2,1), new Vec3(0,0,4), new Vec3(0,4,0), rightBlue));
        world.add(new Quad(new Point(-2,3,1), new Vec3(4,0,0), new Vec3(0,0,4), upperOrange));
        world.add(new Quad(new Point(-2,-3,5), new Vec3(4,0,0), new Vec3(0,0,-4), lowerTeal));

        return new Scene(camera, new HitList(new BVH(world.objects)));
    }

    public static Scene simpleLight(){
        Point lookFrom =new Point(26, 3, 6);
        Point lookAt = new Point(0, 2, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 20,
                0.05, distToFocus, 16/9.0);

        HitList world = new HitList();
        Texture pertext = new Noise(4);
        world.add(new Sphere(new Point(0,-1000,0), 1000, new Lambertian(pertext)));
        world.add(new Sphere(new Point(0,2,0), 2, new Lambertian(pertext)));

        Material difflight = new DiffuseLight(new Color(4,4,4));
        world.add(new Sphere(new Point(0,7,0), 2, difflight));
        // world.add(new Quad(new Point(3,1,-3), new Vec3(2,0,0), new Vec3(0,2,0), difflight));

        return new Scene(camera, new HitList(new BVH(world.objects)), new Color(0));
    }

    public static Scene cornellBox(){
        Point lookFrom = new Point(278, 278, -800);
        Point lookAt = new Point(278, 278, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 40,
                0.05, distToFocus, 1);

        HitList world = new HitList();
        Material red   = new Lambertian(new Color(.65, .05, .05));
        Material white = new Lambertian(new Color(.73, .73, .73));
        Material green = new Lambertian(new Color(.12, .45, .15));
        Material light = new DiffuseLight(new Color(15, 15, 15));

        world.add(new Quad(new Point(555,0,0), new Vec3(0,555,0), new Vec3(0,0,555), green));
        world.add(new Quad(new Point(0,0,0), new Vec3(0,555,0), new Vec3(0,0,555), red));
        world.add(new Quad(new Point(343, 554, 332), new Vec3(-130,0,0), new Vec3(0,0,-105), light));
        world.add(new Quad(new Point(0,0,0), new Vec3(555,0,0), new Vec3(0,0,555), white));
        world.add(new Quad(new Point(555,555,555), new Vec3(-555,0,0), new Vec3(0,0,-555), white));
        world.add(new Quad(new Point(0,0,555), new Vec3(555,0,0), new Vec3(0,555,0), white));

        Hittable box1 = box(new Point(0,0,0), new Point(165,330,165), white);
        box1 = new Rotate(box1, 20, 1);
        box1 = new Translate(box1, new Point(265,0,295));
        world.add(box1);

        Hittable box2 = box(new Point(0,0,0), new Point(165,165,165), white);
        box2 = new Rotate(box2, -18, 1);
        box2 = new Translate(box2, new Point(130,0,65));
        world.add(box2);

        return new Scene(camera, new HitList(new BVH(world.objects)), new Color(0));
    }

    public static Scene cornellSmoke() {
        Point lookFrom = new Point(278, 278, -800);
        Point lookAt = new Point(278, 278, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 40, 0.05, distToFocus, 1);

        HitList world = new HitList();
        Material red = new Lambertian(new Color(.65, .05, .05));
        Material white = new Lambertian(new Color(.73, .73, .73));
        Material green = new Lambertian(new Color(.12, .45, .15));
        Material light = new DiffuseLight(new Color(7, 7, 7));


        world.add(new Quad(new Point(555, 0, 0), new Vec3(0, 555, 0), new Vec3(0, 0, 555), green));
        world.add(new Quad(new Point(0, 0, 0), new Vec3(0, 555, 0), new Vec3(0, 0, 555), red));
        world.add(new Quad(new Point(113, 554, 127), new Vec3(330, 0, 0), new Vec3(0, 0, 305), light));
        world.add(new Quad(new Point(0, 0, 0), new Vec3(555, 0, 0), new Vec3(0, 0, 555), white));
        world.add(new Quad(new Point(0, 555, 0), new Vec3(555, 0, 0), new Vec3(0, 0, 555), white));
        world.add(new Quad(new Point(0, 0, 555), new Vec3(555, 0, 0), new Vec3(0, 555, 0), white));

        Hittable box1 = box(new Point(0, 0, 0), new Point(165, 330, 165), white); //new Metal(new Color(.73, .73, .73), 0.01));
        box1 = new Rotate(box1, 15, 1);
        box1 = new Translate(box1, new Point(265, 0, 295));
        //world.add(box1);

        Hittable box2 = box(new Point(0, 0, 0), new Point(165, 165, 165), white); //new Metal(new Color(.73, .73, .73), 0.01)
        box2 = new Rotate(box2, -18, 1);
        box2 = new Translate(box2, new Point(130, 0, 65));
        //world.add(box2);

        world.add(new Medium(box1, 0.01, new Color(0, 0, 0)));
        world.add(new Medium(box2, 0.01, new Color(1, 1, 1)));

        return new Scene(camera, new HitList(new BVH(world.objects)) , new Color(0));
    }

    public static Scene finalScene() {
        Point lookFrom = new Point(478, 278, -600);
        Point lookAt = new Point(278, 278, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 40, 0.05, distToFocus, 1);


        HitList world = new HitList();
        HitList boxes1 = new HitList();
        Material ground = new Lambertian(new Color(0.48, 0.83, 0.53));

        int boxesPerSide = 20;
        for (int i = 0; i < boxesPerSide; i++) {
            for (int j = 0; j < boxesPerSide; j++) {
                double w = 100.0;
                double x0 = -1000.0 + i * w;
                double z0 = -1000.0 + j * w;
                double y0 = 0.0;
                double x1 = x0 + w;
                double y1 = randomDouble(1, 101);
                double z1 = z0 + w;

                boxes1.add(box(new Point(x0, y0, z0), new Point(x1, y1, z1), ground));
            }
        }


        world.add(new BVH(boxes1.objects));

        Material light = new DiffuseLight(new Color(7, 7, 7));
        world.add(new Quad(new Point(123, 554, 127), new Vec3(300, 0, 0), new Vec3(0, 0, 265), light));

        Point center1 = new Point(400, 400, 200);
        Point center2 = center1.add(new Vec3(30, 0, 0));
        Material movingSphereMaterial = new Lambertian(new Color(0.7, 0.3, 0.1));
        world.add(new MovingSphere(center1, center2, 50, movingSphereMaterial));

        world.add(new Sphere(new Point(260, 150, 45), 50, new Dielectric(new Color(1), 1.5)));
        world.add(new Sphere(new Point(0, 150, 145), 50, new Metal(new Color(0.8, 0.8, 0.9), 1)));

        Sphere boundary = new Sphere(new Point(360, 150, 145), 70, new Dielectric(new Color(1), 1.5));
        world.add(boundary);
        world.add(new Medium(boundary, 0.2, new Color(0.2, 0.7, 0.9)));
        boundary = new Sphere(new Point(0, 0, 0), 5000, new Dielectric(new Color(1), 1.5));
        world.add(new Medium(boundary, .0001, new Color(1, 1, 1)));

        Material emat = new Lambertian(new Image("./textures/earthmap.jpg"));
        world.add(new Sphere(new Point(400, 200, 400), 100, emat));

        Texture pertext = new Noise(0.1);
        world.add(new Sphere(new Point(220, 280, 300), 80, new Lambertian(pertext)));

        HitList boxes2 = new HitList();
        Material white = new Lambertian(Color.random());
        int ns = 1000;
        for (int j = 0; j < ns; j++) {
            boxes2.add(new Sphere(Point.random(0, 165), 10, white));
        }

        world.add(new Translate(
                new Rotate(new BVH(boxes2.objects), 15, 2),
                new Point(-100, 270, 395)
        ));

        return new Scene(camera, new HitList(new BVH(world.objects)) , new Color(0));
    }

    public static Scene reimu3D() {
        Point lookFrom = new Point(1, 0.8, -8);
        Point lookAt = new Point(0, 0.8, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 30,
                0.02, distToFocus, 16/9.0);

        HitList world = new HitList();

        Texture reimuTexture = new Image("./textures/reimu_low_Material_u1_v1_Base_Color.png");
        Model3D reimu = Model3D.loadModel("./textures/reimu_low.obj", new Lambertian(reimuTexture));
        reimu.scale(0.1);
        reimu.rotate(270, 0);
        reimu.rotate(30, 1);
        reimu.translate(0, -0.65, 0);

        Sphere light = new Sphere(new Point(0, 1000, -500), 100.0, new DiffuseLight(new Color(30)));
        Sphere ground = new Sphere(new Point(0, -1000.5, -1), 1000, new Lambertian(new Color(0.8,0.8,0.8)));

        world.add(new BVH(reimu.triangles));
        world.add(light);
        world.add(ground);

        return new Scene(camera, new HitList(new BVH(world.objects)));
    }

    public static Scene dragon(){
        Point lookFrom = new Point(0, 0, 3);
        Point lookAt = new Point(0, 0, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 30,
                0.02, distToFocus, 16/9.0);

        HitList world = new HitList();

        Texture dragonTexture = new SolidColor(new Color(0.4, 0.8, 0.6));
        Model3D dragon = Model3D.loadModel("./textures/dragon_low.obj", new Metal(dragonTexture, 0.1));
        dragon.scale(5);
        dragon.translate(0, -0.5,0);

        Texture checker = new Checker(0.4, new Color(0.2,0.3,0.1), new Color(0.9,0.9,0.9));
        Sphere ground = new Sphere(new Point(0, -100.5, -1), 100, new Lambertian(checker));

        world.add(new BVH(dragon.triangles));
        world.add(ground);

        return new Scene(camera, new HitList(new BVH(world.objects)));
    }

    public static Scene ultimateScene(){
        Point lookFrom = new Point(278, 278, -800);
        Point lookAt = new Point(278, 278, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        Vec3 lookLength = lookFrom.minus(lookAt);
        double distToFocus = lookLength.length();

        Camera camera = new Camera(lookFrom, lookAt, vup, 40,
                0.05, distToFocus, 1);

        HitList world = new HitList();

        Material red   = new Lambertian(new Color(.65, .05, .05));
        Material white = new Lambertian(new Color(.73, .73, .73));
        Material green = new Lambertian(new Color(.12, .45, .15));
        Material light = new DiffuseLight(new Color(15, 15, 15));

        world.add(new Quad(new Point(555,0,0), new Vec3(0,555,0), new Vec3(0,0,555), red));
        world.add(new Quad(new Point(0,0,0), new Vec3(0,555,0), new Vec3(0,0,555), green));
        world.add(new Quad(new Point(0,0,0), new Vec3(555,0,0), new Vec3(0,0,555), white));
        world.add(new Quad(new Point(555,555,555), new Vec3(-555,0,0), new Vec3(0,0,-555), white));
        world.add(new Quad(new Point(0,0,555), new Vec3(555,0,0), new Vec3(0,555,0), white));

        world.add(new Quad(new Point(343, 554, 332), new Vec3(-130,0,0), new Vec3(0,0,-105), light));


        Hittable box1 = box(new Point(0,0,0), new Point(165,330,165), new Metal(new Color(0.8, 0.85, 0.88),0.01));
        box1 = new Rotate(box1, 20, 1);
        box1 = new Translate(box1, new Point(265,0,295));
        world.add(box1);

        Hittable box2 = box(new Point(0,0,0), new Point(165,165,165), white);
        box2 = new Rotate(box2, -18, 1);
        box2 = new Translate(box2, new Point(130,0,65));
        world.add(box2);

        Material glass = new Dielectric(new Color(1), 1.5);
        Hittable sphere = new Sphere(new Point(190,90+165,190), 90, glass);
        world.add(sphere);

        Texture reimuTexture = new Image("./textures/reimu_low_Material_u1_v1_Base_Color.png");
        Model3D reimu = Model3D.loadModel("./textures/reimu_low.obj", new Lambertian(reimuTexture));
        reimu.scale(5);
        reimu.rotate(270, 0);
        reimu.rotate(30, 1);
        reimu.translate(340,330,340);
        world.add(new BVH(reimu.triangles));

        Texture dragonTexture = new SolidColor(new Color(0.4, 0.8, 0.6));
        Model3D dragon = Model3D.loadModel("./textures/dragon_low.obj", new Metal(dragonTexture, 0.05));
        dragon.scale(700);
        dragon.translate(380, 0,100);
        world.add(new BVH(dragon.triangles));

        return new Scene(camera, new HitList(new BVH(world.objects)), new Color(0));
    }
}
