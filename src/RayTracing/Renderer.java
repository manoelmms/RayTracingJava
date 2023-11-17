package RayTracing;

import RayTracing.HitInfo.HitRecord;
import RayTracing.HitInfo.Interval;
import RayTracing.HitInfo.Radiance;
import RayTracing.HitTable.HitTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.IntStream;

import static RayTracing.Utility.*;

public class Renderer {

    int image_width;
    int image_height;
    int depth = 50;

    Camera camera;

    Renderer(Camera camera){
        this.camera = camera;
        this.image_width = 1280;
        this.image_height = 720;
    }

    Renderer(Camera camera, int width, int height){
        this.camera = camera;
        this.image_width = width;
        this.image_height = height;
    }

    String writeRGB(Vec3 c, int sampler_per_pixel) {
        double r = c.x;
        double g = c.y;
        double b = c.z;

        double scale = 1.0/sampler_per_pixel;
        r = Math.sqrt(scale * r);
        g = Math.sqrt(scale * g);
        b = Math.sqrt(scale * b);

        int R = (int) (clamp(r,0.0,0.999) * 256);
        int G = (int) (clamp(g,0.0,0.999) * 256);
        int B = (int) (clamp(b,0.0,0.999) * 256);

        return R+" "+G+" "+B;
    }

    // For use in windowed mode
    java.awt.Color writeAwtColor(Vec3 c, long sampler_per_pixel) {
        double r = c.x;
        double g = c.y;
        double b = c.z;

        double scale = 1.0/sampler_per_pixel;
        r = Math.sqrt(scale * r);
        g = Math.sqrt(scale * g);
        b = Math.sqrt(scale * b);

        short R = (short) (clamp(r,0.0,0.999) * 256);
        short G = (short) (clamp(g,0.0,0.999) * 256);
        short B = (short) (clamp(b,0.0,0.999) * 256);

        return new java.awt.Color(R, G, B);
    }


    Color skyColor(Ray r) {
        Vec3 unit_direction = Vec3.unitVector(r.dir());

        // Linear interpolation between white and blue trick
        double t = 0.5*(unit_direction.y+1.0);

        Color colorW = new Color(1.0,1.0,1.0);
        Color colorB = new Color(0.5,0.7,1.0);
        return colorW.multiply(1.0-t).add(colorB.multiply(t));
    }

    Color rayColor(Ray r, HitTable world, int depth){
        if(depth==0)
            return new Color(0);

        HitRecord rec = new HitRecord();
        Radiance radiance = new Radiance();
        Interval t = new Interval(0.0001, infinity);

        if(world.hit(r, t, rec)) {
            if(rec.mat.scatter(r, rec, radiance)) {
                return radiance.attenuation.multiply(rayColor(radiance.scattered, world, depth-1));
            }
            return new Color(0);
        }

        return skyColor(r);
    }

    // Old version of imageRender, kept for benchmarking purposes and for futures references. Do not use.
    void imageRender(HitTable scene, int spp, String filename) {
        try(FileOutputStream file = new FileOutputStream(filename);
            PrintStream filePrint = new PrintStream(file) ) {

            filePrint.println("P3");
            filePrint.println(image_width+" "+image_height);
            filePrint.println(255);

            System.out.println(image_width + "x" + image_height + ", " + spp + " samples per pixel");
            long start_time = System.currentTimeMillis(); //Time for Benchmark

            for (long y=0; y < image_height; ++y) {
                System.out.print("\r" + "Scanlines remaining: "+(image_height-y));
                for (long x=0; x < image_width; ++x) {
                    Color pixelColor = new Color(0);
                    for (int s=0; s < spp; ++s) {
                        double u = (x + randomDouble()) / (image_width-1);
                        double v = (y + randomDouble()) / (image_height-1);
                        pixelColor.equalAdd(rayColor(camera.get_ray(u, v), scene, depth));
                    }
                    filePrint.println(writeRGB(pixelColor, spp));
                }

            }
            System.out.println("\nJob finished in: " + (double)(System.currentTimeMillis()-start_time)/1000+"s");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void imageRenderParallel(HitTable scene, int spp, String filename) {

        BufferedImage image = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_RGB);

        System.out.println(image_width + "x" + image_height + ", " + spp + " samples per pixel");
        long start_time = System.currentTimeMillis(); //Time for Benchmark

        for (long y=0; y < image_height; ++y) {
            System.out.print("\r" + "Scanlines remaining: "+(image_height-y));
            long finalY = y;
            IntStream.range(0, image_width).parallel().forEach(x -> {
                Color pixelColor = new Color(0);
                for (int s = 0; s < spp; ++s) {
                    double u = (x + randomDouble()) / (image_width - 1);
                    double v = (finalY + randomDouble()) / (image_height - 1);
                    pixelColor.equalAdd(rayColor(camera.get_ray(u, v), scene, depth));
                }
                // pixelColor = pixelColor.multiply(1.0 / spp);

                RayResult result = new RayResult();
                int index = (int) (finalY * image_width + x);
                result.color = pixelColor;
                result.index = index;
                synchronized (image) {
                    image.setRGB(index % image_width, index / image_width, writeAwtColor(result.color, spp).getRGB());
                }
            });
        }
        try {
            ImageIO.write(image, "png", new java.io.File(filename));
            System.out.println("\nImage saved to " + filename);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nJob finished in: " + (double)(System.currentTimeMillis()-start_time)/1000+"s");

    }

    void windowRender(HitTable scene, int spp) {
        Color[] pixelColors = new Color[image_height*image_width];
        for (int i = 0; i<image_height*image_width; ++i)
            pixelColors[i] = new Color(0);

        long init_time = System.currentTimeMillis(); // Time for Benchmark

        JFrame window = new JFrame("RayTracing") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                System.out.println(image_width + "x" + image_height + ", " + spp + " samples per pixel");

                // For each sample per pixel, render the whole image
                for (long s=0; s < spp; ++s) {
                    long start_time = System.currentTimeMillis(); // Time for Benchmark
                    for (int y = 0; y < image_height; ++y) {
                        for (int x = 0; x < image_width; ++x) {

                            double u = (x + randomDouble()) / (image_width - 1);
                            double v = (y + randomDouble()) / (image_height - 1);

                            // Draw the pixel to the screen
                            pixelColors[y * image_width + x].equalAdd(rayColor(camera.get_ray(u, v), scene, depth));
                            g.setColor(writeAwtColor(pixelColors[y * image_width + x], s + 1));
                            g.drawRect(x, y, 1, 1);
                        }
                    }
                    System.out.print("\r" + "Sample: " + (s+1) + " " + "in " + (double)(System.currentTimeMillis()-start_time)/1000+"s");
                }
            }
        };


        window.setSize(image_width, image_height);
        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Save the image to a file
        try {
            BufferedImage image = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            window.paint(graphics);
            ImageIO.write(image, "png", new java.io.File("Output.png"));
            System.out.println("\nImage saved to Output.png");
            System.out.println("Rendering finished in " + (double)(System.currentTimeMillis()-init_time)/1000+"s");

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
