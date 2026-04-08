package models;

import rasterizers.LineRasterizer;

import java.awt.*;
import java.util.ArrayList;

public class Polygon extends Shape
{
    private ArrayList<Point> points;

    private LineStyle lineStyle = LineStyle.Normal;

    private int LineWidth=1;

    public Polygon(ArrayList<Point> points, Color color, int lineWidth, LineStyle lineStyle)
    {
        this.points = points;
        this.color = color;
        this.lineStyle = lineStyle;
        this.LineWidth = lineWidth;
    }
    @Override
    public void draw()
    {
        if(points.size()>=3)
        {
            Line line;
            LineRasterizer lineRasterizer=new LineRasterizer(color,this.raster);
            for(int i=0;i<points.size()-1;i++)
            {
                line=new Line(points.get(i),points.get(i+1),this.color,LineWidth,lineStyle);

                lineRasterizer.rasterize(line);
            }
            line=new Line(points.getLast(),points.getFirst(),this.color,LineWidth,lineStyle);
            lineRasterizer.rasterize(line);
        }

    }
}
