package RayTracing.Texture;

import RayTracing.Color;
import RayTracing.Point;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Image implements Texture{
    BufferedImage image;
    int width, height;

    public Image(String path){
        try {
            image = ImageIO.read(new File(path));
            width = image.getWidth();
            height = image.getHeight();
        }
        catch (Exception e){
            System.out.println("Error reading image");
        }
    }

    int[] getPixel(int x, int y){
        int[] pixel = new int[3];
        int color = image.getRGB(x, y);
        pixel[0] = (color >> 16) & 0xff;
        pixel[1] = (color >> 8) & 0xff;
        pixel[2] = color & 0xff;

        return pixel;
    }

    @Override
    public Color value(double u, double v, Point p) {
        int x = (int)(u * width);
        int y = (int)((1 - v) * height);

        int[] pixel = getPixel(x, y);

        return new Color(pixel[0]/255.0, pixel[1]/255.0, pixel[2]/255.0);
    }
}
