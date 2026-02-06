package rasterizers;

import models.Line;
import models.LineCanvas;

public class LineCanvasRasterizer
{
    private Rasterizer lineRasterizer;
    private Rasterizer dottedLineRasterizer;

    public LineCanvasRasterizer(Rasterizer lineRasterizer, Rasterizer dottedLineRasterizer)
    {
        this.lineRasterizer=lineRasterizer;
        this.dottedLineRasterizer=dottedLineRasterizer;
    }

    public void rasterizeLineCanvas(LineCanvas lineCanvas)
    {
        for(Line line: lineCanvas.getLines())
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
}
