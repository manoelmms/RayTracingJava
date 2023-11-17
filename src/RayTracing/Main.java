package RayTracing;

public class Main {
    public static void main(String[] args) {
        Scene chosen = Scene.finalScene();
        Renderer render = new Renderer(chosen.camera);
        render.imageRenderParallel(chosen.world, 500, "final_500.ppm");
        //render.windowRender(chosen.world, 50);

    }
}