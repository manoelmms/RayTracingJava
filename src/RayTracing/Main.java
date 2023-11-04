package RayTracing;

public class Main {
    public static void main(String[] args) {
        Scene chosen = Scene.finalScene();
        Renderer render = new Renderer(chosen.camera);
        render.imageRender(chosen.world);
        // render.windowRender(chosen.world);

    }
}