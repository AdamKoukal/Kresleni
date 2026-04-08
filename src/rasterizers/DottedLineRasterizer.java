/*package rasterizers;

import models.Line;
import models.Point;
import rasters.Raster;

import java.awt.*;

public class DottedLineRasterizer implements Rasterizer
{
    private Color defaultColor=Color.CYAN;
    private Raster raster;

    private int Gap=5;
    public DottedLineRasterizer(Color defaultColor, Raster raster)
    {
        this.defaultColor=defaultColor;
        this.raster=raster;
    }
    @Override
    public void setColor(Color color) {
        this.defaultColor=color;
    }

    @Override
    public void setRaster(Raster raster) {
        this.raster=raster;
    }

    @Override
    public void rasterize(Line line)
    {
        double k =calculateK(line);
        double q = calculateQ(line.getP1(),k);

        if((line.getP2().getX() - line.getP1().getX())==0)
        {
            if(line.getP1().getY()>line.getP2().getY())
            {
                Point helpPoint=new Point(line.getP1().getX(),line.getP1().getY());
                line.setP1(line.getP2());
                line.setP2(helpPoint);

            }
            int x=line.getP1().getX();
            for(int y=line.getP1().getY();y<=line.getP2().getY();y+=Gap+1)
            {

                raster.setPixel(x,y,defaultColor.getRGB());

            }
        }
        else if(Math.abs(k)>=1)
        {

            if(line.getP1().getY()>line.getP2().getY())
            {
                Point helpPoint=new Point(line.getP1().getX(),line.getP1().getY());
                line.setP1(line.getP2());
                line.setP2(helpPoint);

            }
            for(int y=line.getP1().getY();y<=line.getP2().getY();y+=Gap+1)
            {
                int x=(int) Math.round((y-q)/k);

                raster.setPixel(x,y,defaultColor.getRGB());

            }
        }
        else if(Math.abs(k)<1)
        {
            if(line.getP1().getX()>line.getP2().getX())
            {
                Point helpPoint=new Point(line.getP1().getX(),line.getP1().getY()); // todle fungovalo chyba v druhe souradnici pointu Point helpPoint=new Point(line.getP1().getX(),line.getP2().getY());
                line.setP1(line.getP2());
                line.setP2(helpPoint);

            }

            for(int x=line.getP1().getX();x<=line.getP2().getX();x+=Gap+1)
            {
                int y=(int) Math.round(k*x+q);

                raster.setPixel(x,y,defaultColor.getRGB());

            }
        }

    }

    private double calculateK(Line line)
    {
        return (double) (line.getP2().getY() - line.getP1().getY()) / (line.getP2().getX() - line.getP1().getX());
    }
    private double calculateQ(Point p, double k)
    {
        return p.getY()-(k*p.getX());
    }
}*/
