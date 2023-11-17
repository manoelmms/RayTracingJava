package RayTracing;

public class Main {
    public static void main(String[] args) {
        Scene chosen = Scene.finalScene();
        Renderer render = new Renderer(chosen.camera);

        // imageRender(50spp, 1280x720) for final image = 206.791s
        // imageRenderParallel(50spp, 1280x720) for final image = 46.492s
        render.imageRenderParallel(chosen.world, 50, "final_test.png");

        //render.windowRender(chosen.world, 50);

    }
}