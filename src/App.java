import models.Line;
import models.LineCanvas;
import models.Point;

import rasterizers.DottedLineRasterizer;
import rasterizers.Rasterizer;
import rasterizers.TrivialResterizer;
import rasterizers.LineCanvasRasterizer;
import rasters.Raster;
import rasters.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Console;
import java.io.Serial;
import java.util.ArrayList;

public class App {

    private final JPanel panel;
    private final Raster raster;
    private Rasterizer rasterizer;
    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;
    private Point point;
    private LineCanvasRasterizer lineCanvasRasterizer;
    private LineCanvas lineCanvas;
    private Rasterizer dottedLineRasterizer;
    private boolean isAltDown =false;
    private boolean isShiftDown =false;
    private Point alignHelpPoint;

    private ArrayList<Line> lines=new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(800, 600).start());

    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    public void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    public void start() {
        clear(0xaaaaaa);
        panel.repaint();
    }

    public App(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());

        frame.setTitle("Delta : " + this.getClass().getName());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height);

        panel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        rasterizer=new TrivialResterizer(Color.CYAN, raster);
        dottedLineRasterizer=new DottedLineRasterizer(Color.CYAN, raster);

        lineCanvas=new LineCanvas();
        lineCanvasRasterizer=new LineCanvasRasterizer(rasterizer,dottedLineRasterizer);

        createAdapters();

        //JButton button = new JButton("Reset");
        //panel.add(button);

        panel.addMouseMotionListener(mouseAdapter);
        panel.addMouseListener(mouseAdapter);
        panel.addKeyListener(keyAdapter);
    }

    private void createAdapters()
    {
        keyAdapter = new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode()==KeyEvent.VK_Z && e.isControlDown())
                {

                    lineCanvas.getLines().removeLast();
                    raster.clear();
                    for (int i=0;i<lineCanvas.getLines().size();i++)
                    {
                        if(lineCanvas.getLines().get(i).getIsDotted()==true)
                        {
                            dottedLineRasterizer.rasterize(lineCanvas.getLines().get(i));
                        }
                        else{
                            rasterizer.rasterize(lineCanvas.getLines().get(i));
                        }

                    }
                    panel.repaint();
                }
                else if(e.isAltDown())
                {
                    isAltDown =true;
                }
                if(e.isShiftDown())
                {
                    isShiftDown=true;
                }

            }
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.isAltDown()==false)
                {
                    isAltDown =false;

                }
                if(e.isShiftDown()==false)
                {
                    isShiftDown =false;
                }


            }



        };

        mouseAdapter = new MouseAdapter()
        {

            @Override
            public void mouseDragged(MouseEvent e)
            {

                raster.clear();
                for (int i=0;i<lineCanvas.getLines().size();i++)
                {
                    if(lineCanvas.getLines().get(i).getIsDotted()==true)
                    {
                        dottedLineRasterizer.rasterize(lineCanvas.getLines().get(i));
                    }
                    else
                    {
                        rasterizer.rasterize(lineCanvas.getLines().get(i));
                    }


                }
                Point point2=new Point(e.getX(),e.getY());

                Line helpLine=new Line(point,point2);

                double k=calculateK(helpLine);

                double angle;
                double width;
                double height;

                if(Math.abs(helpLine.getP1().getX())>Math.abs(helpLine.getP2().getX()))
                {
                    width=helpLine.getP1().getX()-helpLine.getP2().getX();
                }
                else
                {
                    width=helpLine.getP2().getX()-helpLine.getP1().getX();
                }

                if(Math.abs(helpLine.getP1().getY())>Math.abs(helpLine.getP2().getY()))
                {
                    height=helpLine.getP1().getY()-helpLine.getP2().getY();
                }
                else
                {
                    height=helpLine.getP2().getY()-helpLine.getP1().getY();
                }
                angle=Math.toDegrees(Math.atan(height/width));

                if(k>=Double.NEGATIVE_INFINITY&&k<=0)
                {
                    angle=90-angle;
                }
                else if(k>0&& k<=Double.POSITIVE_INFINITY)
                {
                    angle+=90;
                }

                if(helpLine.getP1().getX()>helpLine.getP2().getX())
                {
                    angle+=180;
                }



                if(isShiftDown==true)
                {
                        double alignAngle=22.5; // Zajímavost 45/2 nefunguje asi špatná aproximace
                        //alignAngle=45;
                        angle=Math.toRadians(Math.round(angle/alignAngle)*alignAngle-90);
                        //System.out.println(Math.toRadians(Math.round(angle/alignAngle)*alignAngle-90));

                        double helplenght=Math.sqrt(Math.pow(width,2)+Math.pow(height,2));
                        double helpwidth=Math.cos(angle)*helplenght;
                        double helpheight=Math.tan(angle)*helpwidth;
                        point2=new Point( (int)(point.getX()+helpwidth),(int)(point.getY()+helpheight));
                        alignHelpPoint=point2;


                }




                //System.out.println("K: "+k);

                /*System.out.println("Nahore: "+(helpLine.getP2().getY() - helpLine.getP1().getY()));
                System.out.println("Dole: "+(helpLine.getP2().getX() - helpLine.getP1().getX()));*/

                //Point helppoint=new Point();

                Line line;
                //System.out.println(isShiftDown);

                if(isAltDown ==true)
                {
                    line =  new Line(point,point2,true);
                    dottedLineRasterizer.rasterize(line);
                }
                else
                {
                    line =  new Line(point,point2,false);
                    rasterizer.rasterize(line);
                }



                panel.repaint();


            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                Point point2=new Point(e.getX(),e.getY());
                if(isShiftDown==true&&alignHelpPoint!=null)
                {
                    point2=alignHelpPoint;
                }

                Line line;
                if(isAltDown ==true)
                {
                    line =  new Line(point,point2,true);
                }
                else
                {
                    line =  new Line(point,point2,false);
                }
                //System.out.println(line.getIsDotted());
                lineCanvas.addLine(line);

                raster.clear();
                for (int i=0;i<lineCanvas.getLines().size();i++)
                {
                    if(lineCanvas.getLines().get(i).getIsDotted()==true)
                    {
                        dottedLineRasterizer.rasterize(lineCanvas.getLines().get(i));
                    }
                    else
                    {
                        rasterizer.rasterize(lineCanvas.getLines().get(i));
                    }


                }
                panel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                point = new Point(e.getX(),e.getY());
            }
        };
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
