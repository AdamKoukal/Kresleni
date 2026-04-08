package rasterizers;

import models.Circle;
import models.Line;
import models.LineStyle;
import models.Point;
import rasters.Raster;

import java.awt.*;

public class EllipseRasterizer
{
    private Raster raster;
    private double Gap=5;


    public EllipseRasterizer(Raster raster)
    {
        this.raster=raster;
    }
    public void rasterize(Circle circle)
    {
        if(circle.getLineWidth()>1)
        {
            for(int i=0;i<circle.getLineWidth();i++)
            {
                Circle circle1=new Circle(circle.getRadius()-i,circle.getCenter(),circle.color,1,circle.getLineStyle() );
                Circle circle2=new Circle(circle.getRadius()+i,circle.getCenter(),circle.color,1,circle.getLineStyle() );
                rasterize(circle1);
                rasterize(circle2);
            }
        }
        if(circle.getLineStyle()== LineStyle.Normal)
        {
            Gap=0.01;
        }
        else if(circle.getLineStyle()== LineStyle.Dotted)
        {
            Gap=3;
        }
        else if(circle.getLineStyle()== LineStyle.Dashed)
        {
            Gap=0.01;
        }
        Point center=circle.getCenter();
        int radius=circle.getRadius();

        for(double i=0;i<360;i+=Gap)
        {

            int x= (int)(center.getX()+Math.cos(Math.toRadians(i))*radius);
            int y= (int)(center.getY()+Math.sin(Math.toRadians(i))*radius);

            if(x>0&&x<raster.getWidth()&&y>0&&y<raster.getHeight())
            {
                raster.setPixel(x,y,circle.color.getRGB());
            }

            if(circle.getLineStyle()==LineStyle.Dashed&&i%6<=3) // %3==0 nefunguje
            {
                i+=3;
            }

        }

    }
}
