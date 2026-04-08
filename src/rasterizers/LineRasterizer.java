package rasterizers;

import models.Line;

import java.awt.*;

import models.LineStyle;
import models.Point;
import rasters.Raster;

public class LineRasterizer
{
    private Color color=Color.CYAN;
    private Raster raster;

    private int gap=5;

    public LineRasterizer(Color defaultColor, Raster raster)
    {
        this.color=defaultColor;
        this.raster=raster;
    }


    public void setColor(Color color) {

    }


    public void setRaster(Raster raster)
    {
        this.raster = raster;
    }



    public void rasterize(Line line)
    {


        if(line.getStyle()== LineStyle.Normal)
        {
            gap=0;
        }
        else if(line.getStyle()== LineStyle.Dotted)
        {
            gap=5;
        }
        else if(line.getStyle()== LineStyle.Dashed)
        {
            gap=0;
        }
        color=line.color;

        double k =calculateK(line);
        double q = calculateQ(line.getP1(),k);
        //System.out.println("k: "+k);
        if((line.getP2().getX() - line.getP1().getX())==0)
        {

            //System.out.println("1");
            if(line.getP1().getY()>line.getP2().getY())
            {
                Point helpPoint=new Point(line.getP1().getX(),line.getP1().getY());
                line.setP1(line.getP2());
                line.setP2(helpPoint);

            }
            int x=line.getP1().getX();

            if(line.getLineWidth()>1)
            {
                for(int i=1;i<line.getLineWidth();i++)
                {
                    Line helpLine=new Line(new Point(line.getP1().getX()+i,line.getP1().getY()),new Point(line.getP2().getX()+i,line.getP2().getY()),line.getColor(),1,line.getStyle());
                    Line helpLine2=new Line(new Point(line.getP1().getX()-i,line.getP1().getY()),new Point(line.getP2().getX()-i,line.getP2().getY()),line.getColor(),1,line.getStyle());
                    rasterize(helpLine);
                    rasterize(helpLine2);
                }
            }

            for(int y=line.getP1().getY();y<=line.getP2().getY();y+=gap+1)
            {
                if(line.getStyle()==LineStyle.Dashed&&y+5<=raster.getHeight()&&y%5==0)
                {
                    y+=5;
                }

                if(x>0&&x<raster.getWidth()&&y>0&&y<raster.getHeight()   )
                {
                    raster.setPixel(x,y,color.getRGB());
                }


            }
        }
        else if(Math.abs(k)>=1)
        {

            //System.out.println("2");
            if(line.getP1().getY()>line.getP2().getY())
            {
                Point helpPoint=new Point(line.getP1().getX(),line.getP1().getY());
                line.setP1(line.getP2());
                line.setP2(helpPoint);

            }
            if(line.getLineWidth()>1)
            {
                for(int i=1;i<line.getLineWidth();i++)
                {
                    Line helpLine=new Line(new Point(line.getP1().getX()+i,line.getP1().getY()),new Point(line.getP2().getX()+i,line.getP2().getY()),line.getColor(),1,line.getStyle());
                    Line helpLine2=new Line(new Point(line.getP1().getX()-i,line.getP1().getY()),new Point(line.getP2().getX()-i,line.getP2().getY()),line.getColor(),1,line.getStyle());
                    rasterize(helpLine);
                    rasterize(helpLine2);
                }
            }
            for(int y=line.getP1().getY();y<=line.getP2().getY();y+=gap+1)
            {
                if(line.getStyle()==LineStyle.Dashed&&y+5<=raster.getHeight()&&y%5==0)
                {
                    y+=5;
                }
                int x=(int) Math.round((y-q)/k);

                if(x>0&&x<raster.getWidth()&&y>0&&y<raster.getHeight()   )
                {
                    raster.setPixel(x,y,color.getRGB());
                }


            }
        }
        else if(Math.abs(k)<1)
        {

            //System.out.println("3");
            if(line.getP1().getX()>line.getP2().getX())
            {
                Point helpPoint=new Point(line.getP1().getX(),line.getP1().getY()); // todle fungovalo chyba v druhe souradnici pointu Point helpPoint=new Point(line.getP1().getX(),line.getP2().getY());
                line.setP1(line.getP2());
                line.setP2(helpPoint);

            }
            if(line.getLineWidth()>1)
            {
                for(int i=1;i<line.getLineWidth();i++)
                {
                    Line helpLine=new Line(new Point(line.getP1().getX(),line.getP1().getY()+i),new Point(line.getP2().getX(),line.getP2().getY()+i),line.getColor(),1,line.getStyle());
                    Line helpLine2=new Line(new Point(line.getP1().getX(),line.getP1().getY()-i),new Point(line.getP2().getX(),line.getP2().getY()-i),line.getColor(),1, line.getStyle());
                    rasterize(helpLine);
                    rasterize(helpLine2);
                }
            }
            for(int x=line.getP1().getX();x<=line.getP2().getX();x+=gap+1)
            {
                //System.out.println(105);

                if(line.getStyle()==LineStyle.Dashed&&x%5==0)
                {
                    x+=5;
                }




                int y=(int) Math.round(k*x+q);

                if(x>0&&x<raster.getWidth()&&y>0&&y<raster.getHeight()   )
                {
                    raster.setPixel(x,y,color.getRGB());
                }


            }
        }

    }

    private double calculateK(Line line)
    {
        return (double) (line.getP2().getY() - line.getP1().getY()) / (line.getP2().getX() - line.getP1().getX());
    }
    private double calculateQ(Point p, double k)
    {
        return p.getY()-(k*p.getX());
    }

}
