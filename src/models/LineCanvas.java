package models;

import java.util.ArrayList;

public class LineCanvas
{
    ArrayList<Line> lines;

    public LineCanvas(){
        lines=new ArrayList<>();
    }

    public void addLine(Line line)
    {
        lines.add(line);
    }

    public ArrayList<Line> getLines()
    {
        return lines;
    }

    public void clear()
    {
        lines.clear();
    }
}
