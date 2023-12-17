package RayTracing;

public class Main {
    public static void main(String[] args) {
        Scene chosen = Scene.finalScene();

        // Using a MacBook Air M2 for comparison:
        // imageRender(50spp, 1280x720) for final image = 206.791s
        // imageRenderParallel(50spp, 1280x720) for final image = 46.492s
        // imageRenderParallel(500spp, 1280x720) for final image = 546.934s
        // Only takes around 100MB of memory to render!

        System.out.println("\nSelect a render mode:");
        System.out.println("1. Single-threaded with 50 samples per pixel");
        System.out.println("2. Multi-threaded with 50 samples per pixel");
        System.out.println("3. Multi-threaded with 500 samples per pixel");
        System.out.println("4. Windowed with 15 samples per pixel");
        System.out.println("5. Windowed with 50 samples per pixel");


        int choice = 0;
        while (choice < 1 || choice > 5) {
            choice = Utility.readInt();
        }

        System.out.println("\nSelect a Scene:");
        System.out.println("1. Initial Scene");
        System.out.println("2. Final Scene");

        int sceneChoice = 0;
        while (sceneChoice < 1 || sceneChoice > 2) {
            sceneChoice = Utility.readInt();
        }

        switch (sceneChoice) {
            case 1:
                chosen = Scene.initScene();
                break;
            case 2:
                chosen = Scene.finalScene();
                break;
            default:
                break;
        }

        Renderer render = new Renderer(chosen.camera);

        switch (choice) {
            case 1:
                render.imageRender(chosen.world, 50, "Output(50spp).png");
                break;
            case 2:
                render.imageRenderParallel(chosen.world, 50, "Output(50spp).png");
                break;
            case 3:
                render.imageRenderParallel(chosen.world, 500, "Output(500spp).png");
                break;
            case 4:
                render.windowRender(chosen.world, 15);
                break;
            case 5:
                render.windowRender(chosen.world, 50);
                break;
            default:
                break;
        }
    }
}