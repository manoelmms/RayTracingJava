package RayTracing;

public class Main {
    public static void main(String[] args) {
        Scene chosen = Scene.initScene();

        // Using a MacBook Air M2 for comparison:
        // imageRender(50spp, 1280x720) for final image = 206.791s
        // imageRenderParallel(50spp, 1280x720) for final image = 46.492s
        // imageRenderParallel(500spp, 1280x720) for final image = 546.934s - With AABB is 228s!
        // AABB improves performance by approximately 2x
        // Only takes around 100MB of memory to render! - With AABB is 600MB!

        System.out.println("\nSelect a render mode:");
        System.out.println("1. Single-threaded with 50 samples per pixel (deprecated)");
        System.out.println("2. Multi-threaded with 50 samples per pixel");
        System.out.println("3. Multi-threaded with 500 samples per pixel");
        System.out.println("4. Multi-threaded with 1500 samples per pixel - Warning: Takes a long time!");
        System.out.println("5. Windowed with 15 samples per pixel");
        System.out.println("6. Windowed with 50 samples per pixel");


        int choice = 0;
        while (choice < 1 || choice > 6) {
            choice = Utility.readInt();
        }

        System.out.println("\nSelect a Scene:");
        System.out.println("1. Initial Scene");
        System.out.println("2. Two Spheres Scene");
        System.out.println("3. Random Spheres Scene");
        System.out.println("4. Earth Scene");
        System.out.println("5. Perlin Spheres Scene");
        System.out.println("6. Quads Scene");
        System.out.println("7. Simple Light Scene");
        System.out.println("8. Cornell Box Scene");
        System.out.println("9. Cornell Smoke Scene");
        System.out.println("10. Final Scene");
        System.out.println("11. Reimu Scene");
        System.out.println("12. Dragon Scene");
        System.out.println("13. Ultimate Scene");

        int sceneChoice = 0;
        while (sceneChoice < 1 || sceneChoice > 13) {
            sceneChoice = Utility.readInt();
        }

        switch (sceneChoice) {
            case 1:
                chosen = Scene.initScene();
                break;
            case 2:
                chosen = Scene.two_spheres_scene();
                break;
            case 3:
                chosen = Scene.random_sphere_scene();
                break;
            case 4:
                chosen = Scene.earth();
                break;
            case 5:
                chosen = Scene.perlinSpheres();
                break;
            case 6:
                chosen = Scene.quads();
                break;
            case 7:
                chosen = Scene.simpleLight();
                break;
            case 8:
                chosen = Scene.cornellBox();
                break;
            case 9:
                chosen = Scene.cornellSmoke();
                break;
            case 10:
                chosen = Scene.finalScene();
                break;
            case 11:
                chosen = Scene.reimu3D();
                break;
            case 12:
                chosen = Scene.dragon();
                break;
            case 13:
                chosen = Scene.ultimateScene();
                break;
            default:
                break;
        }

        Renderer render;


        if (sceneChoice == 6 || sceneChoice  == 8 || sceneChoice == 9 || sceneChoice == 10 || sceneChoice == 13) {
            render = new Renderer(chosen.camera, 1080, 1080, chosen.backgroundColor);
        } else {
            render = new Renderer(chosen.camera, chosen.backgroundColor);
        }

        switch (choice) {
            case 1:
                render.imageRender(chosen.world, 50, "Output(50spp).png");
                break;
            case 2:
                render.imageRenderParallel(chosen.world, 50, "Output(50spp).png");
                break;
            case 3:
                render.imageRenderParallel(chosen.world,  500, "Output(500spp).png");
                break;
            case 4:
                render.imageRenderParallel(chosen.world, 1500, "Output(1500spp).png");
            case 5:
                render.windowRender(chosen.world, 15);
                break;
            case 6:
                render.windowRender(chosen.world, 50);
                break;
            default:
                break;
        }
    }
}
