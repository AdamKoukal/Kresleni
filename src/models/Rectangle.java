package models;

import rasterizers.LineRasterizer;

public class Rectangle extends Shape
{
    private Point P1;
    private Point P2;
    private Point P3;
    private Point P4;

    private LineStyle lineStyle = LineStyle.Normal;

    private int LineWidth=1;

    public  Rectangle(Point P1, Point P2, Point P3, Point P4, int LineWidth,LineStyle lineStyle)
    {
        this.P1 = P1;
        this.P2 = P2;
        this.P3 = P3;
        this.P4 = P4;
        this.lineStyle = lineStyle;
        this.LineWidth = LineWidth;
    }

    @Override
    public void draw()
    {
        Line line1=new Line(P1,P2,this.color,LineWidth,lineStyle);
        Line line2=new Line(P3,P4,this.color,LineWidth,lineStyle);
        Line line3=new Line(P1,P3,this.color,LineWidth,lineStyle);
        Line line4=new Line(P2,P4,this.color,LineWidth,lineStyle);

        LineRasterizer lineRasterizer=new LineRasterizer(color,this.raster);
        lineRasterizer.rasterize(line1);
        lineRasterizer.rasterize(line2);
        lineRasterizer.rasterize(line3);
        lineRasterizer.rasterize(line4);

    }
}
