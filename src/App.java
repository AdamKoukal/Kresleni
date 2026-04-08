import models.*;

import models.Point;
import models.Polygon;
import models.Rectangle;
import rasterizers.EllipseRasterizer;
import rasterizers.LineRasterizer;
import rasters.Raster;
import rasters.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class App {

    private final JPanel panel;
    private final Raster raster;

    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;
    private Point point;

    private ArrayList<Point> polygonPoints=new ArrayList<>();

    private Color selectedColor=Color.WHITE;
    private LineStyle selectedLineStyle = LineStyle.Normal;
    private int selectedLineWidth=1;
    private String selectedTool="Line";

    private Shapes shapes;
    private LineRasterizer lineRasterizer;
    private EllipseRasterizer ellipseRasterizer;

    private boolean isAltDown =false;
    private boolean isShiftDown =false;
    private boolean isKeyFDown =false;

    private Point alignHelpPoint;

    //private ArrayList<Line> lines=new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(1280, 720).start());

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

        //můj kód

        shapes=new Shapes();
        lineRasterizer=new LineRasterizer(Color.WHITE,raster);

        ellipseRasterizer=new EllipseRasterizer(raster);



        createAdapters();
        JButton deleteButton = new JButton("Delete All");
        deleteButton.addActionListener(e-> {
            shapes.deleteAllShapes();
            raster.clear();
            panel.repaint();
            panel.requestFocusInWindow();
        });
        JPanel selectedColorIcon=new JPanel();
        selectedColorIcon.setPreferredSize(new Dimension(20,20));

        //JButton button = new JButton("Reset");
        //panel.add(button);

        JSpinner lineWidthField=new JSpinner();

        lineWidthField.setPreferredSize(new Dimension(30,20));
        lineWidthField.setValue(selectedLineWidth);
        lineWidthField.addChangeListener(e->{
            selectedLineWidth=Integer.parseInt(lineWidthField.getValue().toString());
            panel.requestFocusInWindow();
        });

        String[]lineStyles={"Normal", "Dotted", "Dashed"};
        JComboBox<String> linesStylesDropDown=new JComboBox<>(lineStyles);

        String[]tools={"Line", "Fill","Rectangle","Circle", "Polygon"};
        JComboBox<String> toolsDropDown=new JComboBox<>(tools);
        toolsDropDown.addActionListener(e->{
            selectedTool=toolsDropDown.getSelectedItem().toString();
            panel.requestFocusInWindow();
        });

        JColorChooser colorChooser=new JColorChooser();
        JButton colorButton=new JButton("Select Color");
        colorButton.addActionListener(e-> {

            selectedColor=JColorChooser.showDialog(colorChooser,"Select Your Color",Color.WHITE );
            selectedColorIcon.setBackground(selectedColor);
            panel.requestFocusInWindow();

        });
        linesStylesDropDown.addActionListener(e->{
            selectedLineStyle =LineStyle.valueOf(linesStylesDropDown.getSelectedItem().toString());
            System.out.println(selectedLineStyle);
            panel.requestFocusInWindow();
        });

        panel.add(lineWidthField);
        panel.add(linesStylesDropDown);
        panel.add(colorButton);
        panel.add(selectedColorIcon);
        panel.add(toolsDropDown);
        panel.add(deleteButton);



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
                    if(shapes.getShapes().size()>0)
                    {
                        shapes.getShapes().removeLast();
                        raster.clear();

                        shapes.drawAll();


                        panel.repaint();
                    }

                }

                if(e.isShiftDown())
                {
                    isShiftDown=true;
                }


            }
            @Override
            public void keyReleased(KeyEvent e)
            {


                if(e.isShiftDown()==false)
                {
                    isShiftDown =false;
                }
                if(selectedTool=="Polygon"&&e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    if(polygonPoints.size()>=3)
                    {
                        Polygon polygon=new Polygon(new ArrayList<>(polygonPoints),selectedColor,selectedLineWidth,selectedLineStyle);
                        polygon.setRaster(raster);
                        shapes.addShape(polygon);
                        polygon.draw();
                        panel.repaint();
                        polygonPoints.clear();
                    }
                }




            }



        };

        mouseAdapter = new MouseAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                Point point2=new Point(e.getX(),e.getY());
                double angle=0;
                double width=0;
                double height=0;
                if(point!=null)
                {
                    Line helpLine=new Line(point,point2, selectedColor, selectedLineWidth,selectedLineStyle);

                    double k=calculateK(helpLine);




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
                }

                if(selectedTool=="Polygon")
                {
                    if(isShiftDown==true&&point!=null)
                    {
                        double alignAngle=22.5; // Zajímavost 45/2 nefunguje asi špatná aproximace
                        //alignAngle=45;
                        double helpangle=Math.round(angle/alignAngle)*alignAngle;
                        System.out.println(helpangle);

                    /*if(helpangle==360)
                       {
                           helpangle=0;
                       }*/

                        angle=Math.toRadians(helpangle-90);

                        System.out.println(angle);
                        //System.out.println(Math.toRadians(Math.round(angle/alignAngle)*alignAngle-90));
                        double helplenght=Math.sqrt(Math.pow(width,2)+Math.pow(height,2));
                        double helpwidth=Math.cos(angle)*helplenght;
                        double helpheight=Math.tan(angle)*helpwidth;



                        //System.out.println(round(Math.cos(Math.toRadians(315))*helplenght,12));
                        //System.out.println(round(Math.cos(Math.toRadians(45))*helplenght,12));


                        point2=new Point( (int)round(point.getX()+helpwidth,0),(int)round((point.getY()+helpheight),0));
                        alignHelpPoint=point2;
                    }


                    ArrayList<Point> helpPolygonPoints=new ArrayList<>(polygonPoints);
                    helpPolygonPoints.add(point2);
                    raster.clear();
                    shapes.drawAll();
                    if(helpPolygonPoints.size()>=3)
                    {
                        Polygon polygon=new Polygon(helpPolygonPoints,selectedColor,selectedLineWidth,selectedLineStyle);
                        polygon.setRaster(raster);
                        polygon.draw();
                    }

                    panel.repaint();
                }
            }
            @Override
            public void mouseDragged(MouseEvent e)
            {

                raster.clear();

                shapes.drawAll();

                Point point2=new Point(e.getX(),e.getY());

                Line helpLine=new Line(point,point2, selectedColor, selectedLineWidth,selectedLineStyle);

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
                    if(selectedTool=="Line")
                    {
                        double alignAngle=22.5; // Zajímavost 45/2 nefunguje asi špatná aproximace
                        //alignAngle=45;
                        double helpangle=Math.round(angle/alignAngle)*alignAngle;
                        System.out.println(helpangle);

                    /*if(helpangle==360)
                       {
                           helpangle=0;
                       }*/

                        angle=Math.toRadians(helpangle-90);

                        System.out.println(angle);
                        //System.out.println(Math.toRadians(Math.round(angle/alignAngle)*alignAngle-90));
                        double helplenght=Math.sqrt(Math.pow(width,2)+Math.pow(height,2));
                        double helpwidth=Math.cos(angle)*helplenght;
                        double helpheight=Math.tan(angle)*helpwidth;

                        System.out.println(Math.cos(Math.toRadians(315))*helplenght);
                        System.out.println(Math.cos(Math.toRadians(45))*helplenght);

                        //System.out.println(round(Math.cos(Math.toRadians(315))*helplenght,12));
                        //System.out.println(round(Math.cos(Math.toRadians(45))*helplenght,12));


                        point2=new Point( (int)round(point.getX()+helpwidth,0),(int)round((point.getY()+helpheight),0));
                        alignHelpPoint=point2;
                    }
                    else if(selectedTool=="Rectangle")
                    {
                        System.out.println(angle);

                        int rectangleWidth;
                        rectangleWidth=point.getX()-point2.getX();

                        int rectangleHeight;
                        rectangleHeight=point.getY()-point2.getY();
                        System.out.println("Height: "+rectangleHeight);
                        System.out.println("Width: "+rectangleWidth);
                        if((angle>180&&angle<270)||(angle>0&&angle<90))
                        {
                            rectangleWidth*=-1;
                            rectangleHeight*=-1;
                        }

                        if(Math.abs(rectangleHeight)>Math.abs(rectangleWidth))
                        {
                            point2=new Point(point2.getX(), point.getY()-rectangleWidth);
                            alignHelpPoint=point2;

                        }
                        else
                        {
                            point2=new Point(point.getX()-rectangleHeight, point2.getY());
                            alignHelpPoint=point2;
                        }
                    }


                }

                //System.out.println("K: "+k);

                /*System.out.println("Nahore: "+(helpLine.getP2().getY() - helpLine.getP1().getY()));
                System.out.println("Dole: "+(helpLine.getP2().getX() - helpLine.getP1().getX()));*/

                //Point helppoint=new Point();

                if(selectedTool=="Line")
                {
                    Line line;

                    line = new Line(point,point2,selectedColor,selectedLineWidth,selectedLineStyle);
                    lineRasterizer.rasterize(line);
                }
                else if(selectedTool=="Rectangle")
                {
                    Rectangle rectangle;

                    rectangle=new Rectangle(point,new Point(point2.getX(),point.getY()),new Point(point.getX(),point2.getY()),point2,selectedLineWidth,selectedLineStyle);
                    rectangle.setRaster(raster);
                    rectangle.draw();
                }
                else if(selectedTool=="Circle")
                {
                    int helpRadius;
                    int x =point.getX();
                    int y =point.getY();

                    int x2 =point2.getX();
                    int y2 =point2.getY();

                    int helpWidth=point2.getX()-point.getX();
                    int helpWeight=point2.getY()-point.getY();

                    helpRadius=(int)Math.sqrt(Math.pow(helpWidth,2)+Math.pow(helpWeight,2));
                    Circle circle=new Circle(helpRadius,point,selectedColor,selectedLineWidth,selectedLineStyle);
                    circle.setRaster(raster);
                    circle.draw();

                }



                //System.out.println("2: "+line.getStyle());


                panel.repaint();

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {

                Point point2=new Point(e.getX(),e.getY());


                if(selectedTool=="Line")
                {
                    Line line;
                    if(isShiftDown==true&&alignHelpPoint!=null)
                    {
                        point2=alignHelpPoint;
                    }


                    line =  new Line(point,point2,selectedColor,selectedLineWidth,selectedLineStyle);

                    //System.out.println(line.getIsDotted());

                    if(!( point2.getX()==point.getX() && point2.getY()==point.getY() ))
                    {
                        line.setRaster(raster);
                        shapes.addShape(line);
                    }
                }
                else if(selectedTool=="Rectangle")
                {
                    if(isShiftDown==true&&alignHelpPoint!=null)
                    {
                        point2=alignHelpPoint;
                    }

                    Rectangle rectangle;

                    rectangle=new Rectangle(point,new Point(point2.getX(),point.getY()),new Point(point.getX(),point2.getY()),point2,selectedLineWidth,selectedLineStyle);
                    rectangle.setRaster(raster);

                    shapes.addShape(rectangle);
                }
                else if(selectedTool=="Circle")
                {
                    int helpRadius;
                    int x =point.getX();
                    int y =point.getY();

                    int x2 =point2.getX();
                    int y2 =point2.getY();

                    int width=point2.getX()-point.getX();
                    int height=point2.getY()-point.getY();

                    helpRadius=(int)Math.sqrt(Math.pow(width,2)+Math.pow(height,2));
                    Circle circle=new Circle(helpRadius,point,selectedColor,selectedLineWidth,selectedLineStyle);
                    circle.setRaster(raster);
                    shapes.addShape(circle);
                }





                //System.out.println("k::::::"+calculateK(line));

                raster.clear();
                shapes.drawAll();
                if(selectedTool=="Polygon")
                {
                    if(isShiftDown==true&&alignHelpPoint!=null)
                    {
                        point=alignHelpPoint;
                    }
                    polygonPoints.add(point);

                    if(polygonPoints.size()>=3)
                    {
                        Polygon polygon=new Polygon(polygonPoints,selectedColor,selectedLineWidth,selectedLineStyle);
                        polygon.setRaster(raster);
                        polygon.draw();
                    }


                }

                panel.repaint();

            }

            @Override
            public void mousePressed(MouseEvent e)
            {


                //System.out.println(isKeyFDown);
                /*if(isShiftDown==false)
                {*/
                point = new Point(e.getX(),e.getY());
                //}
                if(selectedTool=="Fill")
                {
                    ArrayList<Point> points=new ArrayList<>();
                    fill(new Point(e.getX(),e.getY()),points);

                    Fill fill=new Fill(points,selectedColor);
                    fill.setRaster(raster);
                    shapes.addShape(fill);

                }




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

    public static double round(double value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Počet desetinných míst musí být >= 0");
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void fill(Point point, ArrayList<Point> points )
    {

        if(raster.getPixel(point.getX(), point.getY())==Color.BLACK.getRGB())
        {
            raster.setPixel(point.getX(), point.getY(), Color.CYAN.getRGB());

            points.add(point);

            if(point.getX()-1>0)
            {
                fill(new Point(point.getX()-1, point.getY()),points);
            }
            if(point.getX()+1<raster.getWidth())
            {
                fill(new Point(point.getX()+1, point.getY()),points);
            }
            if(point.getY()-1>0)
            {
                fill(new Point(point.getX(), point.getY()-1),points);
            }
            if(point.getY()+1<raster.getHeight())
            {
                fill(new Point(point.getX(), point.getY()+1),points);
            }
        }

    }


}

/*
private void fill(Point point, ArrayList<Point> points )
    {
        if(raster.getPixel(point.getX(), point.getY())==Color.BLACK.getRGB())
        {
            raster.setPixel(point.getX(), point.getY(), Color.CYAN.getRGB());

            points.add(point);

            if(point.getX()-1>0)
            {
                fill(new Point(point.getX()-1, point.getY()),points);
            }
            if(point.getX()+1<raster.getWidth())
            {
                fill(new Point(point.getX()+1, point.getY()),points);
            }
            if(point.getY()-1>0)
            {
                fill(new Point(point.getX(), point.getY()-1),points);
            }
            if(point.getY()+1<raster.getHeight())
            {
                fill(new Point(point.getX(), point.getY()+1),points);
            }
        }
        panel.repaint();

    }
*/