package models;

import java.util.ArrayList;

public class Shapes
{
    private ArrayList<Shape> shapes;

    public Shapes(){
        shapes=new ArrayList<>();
    }

    public void addShape(Shape Shape)
    {
        shapes.add(Shape);
    }

    public void clear()
    {
        shapes.clear();
    }

    public void drawAll()
    {
        for(Shape shape : shapes)
        {
            shape.draw();
        }
    }
    public void deleteAllShapes(){
        shapes.clear();
    }

    public ArrayList<Shape> getShapes()
    {
        return shapes;
    }
}
