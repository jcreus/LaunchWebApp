package com.decmurphy.spx;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author declan
 */
public class ImageProcessor {

    public ImageProcessor() {
    }
/*
    public ImageTerminal createPNG() {
        ImageTerminal png = new ImageTerminal();
        File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
        File tempFile = new File(catalinaBase, "webapps/tmp/");

        File file = new File(tempFile + "plot.png");

        try {
            file.createNewFile();
            png.processOutput(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            System.err.print(ex);
        } catch (IOException ex) {
            System.err.print(ex);
        }

        JavaPlot p = new JavaPlot();
        p.setTerminal(png);

        p.getAxis(
                "x").setLabel("yield");
        p.getAxis(
                "y").setLabel("biomass");
        p.getAxis(
                "x").setBoundaries(0.0, 1.0);
        p.getAxis(
                "y").setBoundaries(0.0, 1.0);
        p.addPlot(
        ...);

    p.setTitle(
                "remaining EMs");
        p.plot();

        try {
            ImageIO.write(png.getImage(), "png", file);
        } catch (IOException ex) {
            System.err.print(ex);
        }

        return png;
    }
  */  
    public static void simple3D() {
        JavaPlot p = new JavaPlot();
        p.addPlot("sin(x)");
        p.plot();
    }
}
