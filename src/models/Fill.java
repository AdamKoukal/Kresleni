package models;

import java.awt.*;
import java.util.ArrayList;

public class Fill extends Shape {
    private ArrayList<Point> points;
    private Color color;

    public Fill(ArrayList<Point> points, Color color)
    {
        this.points=points;
        this.color=color;
    }
    public ArrayList<Point> getPoints()
    {
        return points;
    }

    @Override
    public void draw()
    {
        for(Point p:points)
        {
            raster.setPixel(p.getX(),p.getY(), color.getRGB());
        }
    }
}
