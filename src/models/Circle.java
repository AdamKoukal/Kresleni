package models;

import rasterizers.EllipseRasterizer;
import rasterizers.LineRasterizer;

import java.awt.*;

public class Circle extends Shape
{
    private int radius;
    private Point center;
    private LineStyle lineStyle = LineStyle.Normal;

    private int LineWidth=1;

    public Circle(int radius, Point center, Color color, int LineWidth, LineStyle lineStyle)
    {
        this.radius = radius;
        this.center = center;
        this.color=color;
        this.lineStyle=lineStyle;
        this.LineWidth=LineWidth;
    }

    public int getRadius()
    {
        return radius;
    }

    public Point getCenter()
    {
        return center;
    }

    public LineStyle getLineStyle()
    {
        return lineStyle;
    }

    public int  getLineWidth()
    {
        return LineWidth;
    }

    @Override
    public void draw()
    {
        EllipseRasterizer ellipseRasterizer=new EllipseRasterizer(raster);
        ellipseRasterizer.rasterize(this);
    }
}
