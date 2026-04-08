package models;

import rasterizers.LineRasterizer;

import java.awt.*;

public class Line extends Shape
{
    private Point p1;
    private Point p2;


    private LineStyle lineStyle = LineStyle.Normal;

    private int LineWidth=1;

    public Line(Point p1, Point p2, Color color,int lineWidth)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.color=color;
        this.LineWidth=lineWidth;
    }

    public Line(Point p1, Point p2,Color color,int lineWidth, LineStyle lineStyle)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.lineStyle = lineStyle;
        this.LineWidth=lineWidth;
        this.color=color;
    }

    public Point getP1()
    {
        return p1;
    }

    public Point getP2()
    {
        return p2;
    }

    public void setP1(Point p1)
    {
        this.p1 = p1;
    }

    public void setP2(Point p2)
    {
        this.p2 = p2;
    }

    public LineStyle getStyle()
    {
        return lineStyle;
    }

    public int getLineWidth()
    {
        return LineWidth;
    }
    public Color getColor()
    {
        return color;
    }

    @Override
    public void draw()
    {
        LineRasterizer lineRasterizer=new LineRasterizer(this.color,raster);
        lineRasterizer.rasterize(this);
    }
}
