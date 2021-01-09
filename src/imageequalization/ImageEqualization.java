/*
    Nama    : Almas Fauzia Wibawa
    NIM     : 17/409427/PA/17734
 */
package imageequalization;

import java.awt.Color;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageEqualization {

    public static void main(String[] args) {
        int n = 3;
        BufferedImage[] img = new BufferedImage[n];
        File file1, file2, file3;
        
        try {
            file1 = new File("D:\\Kuliah\\Semester 5\\Tugas\\"
                    + "Pengolahan Citra Digital (2)\\ImageEqualization\\"
                    + "img0.jpeg");
            img[0] = ImageIO.read(file1);
            
            file2 = new File("D:\\Kuliah\\Semester 5\\Tugas\\"
                    + "Pengolahan Citra Digital (2)\\ImageEqualization\\"
                    + "img1.jpeg");
            img[1] = ImageIO.read(file2);
            file2 = new File("D:\\Kuliah\\Semester 5\\Tugas\\"
                    + "Pengolahan Citra Digital (2)\\ImageEqualization\\"
                    + "img2.jpeg");
            img[2] = ImageIO.read(file2);
        } catch(IOException e) {
            System.out.println(e);
        }
        
        for (int i = 0; i < n; i++) {
            histogramEqualization(img[i], i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    swapHistogram(img[i], img[j], i, j);
                }
            }
        }
    }
    
    public static void histogramEqualization(BufferedImage img, int n) {
        File file;
        int red, green, blue, alpha, pixel;
        ArrayList<int[]> histLookup = histogramLookup(img);
        
        int width = img.getWidth();
        int height = img.getHeight();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixel = img.getRGB(x, y);
                alpha = (pixel >> 24)&0xff;
                red = (pixel >> 16)&0xff;
                green = (pixel >> 8)&0xff;
                blue = pixel&0xff;
                
                red = histLookup.get(0)[red];
                green = histLookup.get(1)[green];
                blue = histLookup.get(2)[blue];
                
                pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                img.setRGB(x, y, pixel);
            }
        }
        
        try {
            file = new File("D:\\Kuliah\\Semester 5\\Tugas\\"
                    + "Pengolahan Citra Digital (2)\\ImageEqualization\\"
                    + "output" + n + ".jpg");
            ImageIO.write(img, "jpg", file);
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public static void swapHistogram(
            BufferedImage img1, BufferedImage img2, int n, int m) {
        //img1 sbg image yang akan diubah histogramnya
        //img2 sbg image yang akan digunakan histogramnya
        File file;
        int red, green, blue, alpha, pixel;
        ArrayList<int[]> histLookup = histogramLookup(img2);
        
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                pixel = img1.getRGB(x, y);
                alpha = (pixel >> 24)&0xff;
                red = (pixel >> 16)&0xff;
                green = (pixel >> 8)&0xff;
                blue = pixel&0xff;
                
                red = histLookup.get(0)[red];
                green = histLookup.get(1)[green];
                blue = histLookup.get(2)[blue];
                
                pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                img1.setRGB(x, y, pixel);
            }
        }
        
        try {
            file = new File("D:\\Kuliah\\Semester 5\\Tugas\\"
                    + "Pengolahan Citra Digital (2)\\ImageEqualization\\"
                    + "outputSwap" + n + m + ".jpg");
            ImageIO.write(img1, "jpg", file);
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public static ArrayList<int[]> histogramLookup(BufferedImage img) {
        ArrayList<int[]> arrayResult = new ArrayList<>();
        ArrayList<int[]> histogramOri = histogramOfOriImg(img);
        
        int[] redHistogram = new int[256];
        int[] greenHistogram = new int[256];
        int[] blueHistogram = new int[256];
        
        for (int i = 0; i < 256; i++) {
            redHistogram[i] = 0;
            greenHistogram[i] = 0;
            blueHistogram[i] = 0;
        }
        
        long sumRed = 0, sumGreen = 0, sumBlue = 0;
        
        float scaleFactor = (float)(255.0 / (img.getWidth() * img.getHeight()));
        for (int i = 0; i < 256; i++) {
            sumRed += histogramOri.get(0)[i];
            int valRed = (int) (sumRed * scaleFactor);
            if (valRed > 255) {
                redHistogram[i] = 255;
            } else {
                redHistogram[i] = valRed;
            }
            
            sumGreen += histogramOri.get(1)[i];
            int valGreen = (int) (sumGreen * scaleFactor);
            if (valGreen > 255) {
                greenHistogram[i] = 255;
            } else {
                greenHistogram[i] = valGreen;
            }
            
            sumBlue += histogramOri.get(2)[i];
            int valBlue = (int) (sumBlue * scaleFactor);
            if (valBlue > 255) {
                blueHistogram[i] = 255;
            } else {
                blueHistogram[i] = valBlue;
            }
        }
        
        arrayResult.add(redHistogram);
        arrayResult.add(greenHistogram);
        arrayResult.add(blueHistogram);
        
        return arrayResult;
    }
    
    public static ArrayList<int[]> histogramOfOriImg(BufferedImage img) {
        ArrayList<int[]> arrayResult = new ArrayList<>();
        
        int[] redFreq = new int[256];
        int[] greenFreq = new int[256];
        int[] blueFreq = new int[256];
        for (int i = 0; i < 256; i++) {
            redFreq[i] = 0;
            greenFreq[i] = 0;
            blueFreq[i] = 0;
        }
        
        int pixel, alpha, red, green, blue;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                pixel = img.getRGB(x, y);
                alpha = (pixel >> 24)&0xff;
                red = (pixel >> 16)&0xff;
                green = (pixel >> 8)&0xff;
                blue = pixel&0xff;
                
                redFreq[red]++;
                greenFreq[green]++;
                blueFreq[blue]++;
            }
        }
        
        arrayResult.add(redFreq);
        arrayResult.add(greenFreq);
        arrayResult.add(blueFreq);
        
        return arrayResult;
    }
}
