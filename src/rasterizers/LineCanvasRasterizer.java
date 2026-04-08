/*package rasterizers;

import models.Line;
import models.Canvas;

public class LineCanvasRasterizer
{
    private Rasterizer lineRasterizer;
    private Rasterizer dottedLineRasterizer;

    public LineCanvasRasterizer(Rasterizer lineRasterizer, Rasterizer dottedLineRasterizer)
    {
        this.lineRasterizer=lineRasterizer;
        this.dottedLineRasterizer=dottedLineRasterizer;
    }

    public void rasterizeLineCanvas(Canvas canvas)
    {
        for(Line line: canvas.getLines())
        {
            if(line.getIsDotted()==false)
            {
                lineRasterizer.rasterize(line);
            }
            else
            {
                dottedLineRasterizer.rasterize(line);
            }

        }
    }
}*/
