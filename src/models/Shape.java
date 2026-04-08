package models;

import rasters.Raster;
import rasters.RasterBufferedImage;

import java.awt.*;

public abstract class Shape
{
    public Color color=Color.WHITE;
    public abstract void draw();
    protected Raster raster;


    public void setRaster(Raster raster) {
        this.raster = raster;
    }
}
