import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Display extends JPanel {
    private final double radius = 10;
    private static final long serialVersionUID = 1L;
    private ArrayList<Point2D.Double> points;
    private Point2D.Double punct, v0, v1, v2;
    private JButton btnHull, btnTriangulare, btnRestart, btnVerify;
    private boolean drawLine, drawPoint, btnPressed, pointDeclared, triangulate, startAlgorithm;
    private boolean clearScreen, inside, finishedAlgorithm, onEdge;
    private int counter;
    private double[] x, y;
    private final int fontSize = 18;

    public Display() {
        counter = 0;
        drawLine = false;
        drawPoint = false;
        btnPressed = false;
        inside = false;
        onEdge = false;
        triangulate = false;
        clearScreen = false;
        pointDeclared = false;
        startAlgorithm = false;
        finishedAlgorithm = false;

        x = new double[3];
        y = new double[3];
        points = new ArrayList<Point2D.Double>();

        v0 = new Point2D.Double();
        v1 = new Point2D.Double();
        v2 = new Point2D.Double();
        setBackground(Color.WHITE);
        repaint();


        //Mouse Listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (btnPressed == false) {
                    points.add(new Point2D.Double(e.getX(), e.getY()));
                    System.out.println("n= " + points.size());
                    repaint();
                } else {
                    System.out.println("punct declarat");
                    punct = new Point2D.Double(e.getX(), e.getY());
                    pointDeclared = true;
                    drawPoint = true;
                    repaint();
                }
            }
        });

        //Draw Convex Hull Button
        btnHull = new JButton("Create convex hull");
        btnHull.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("in btn");
                drawLine = true;
                if (points.size() > 0) {
                    btnPressed = true;
                }
                System.out.println("btnPressed: " + btnPressed);
                repaint();
            }
        });

        add(btnHull);

        //Triangulare
        btnTriangulare = new JButton("Triangulate polygon");
        btnTriangulare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triangulate = true;
                repaint();
            }
        });
        add(btnTriangulare);

        //Restart
        btnRestart = new JButton("Restart");
        btnRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearScreen = true;
                startAlgorithm = false;
                finishedAlgorithm = false;
                inside = false;
                pointDeclared = false;
                counter = 0;
                repaint();
            }
        });
        add(btnRestart);

        //Verifica fiecare triunghi
        btnVerify = new JButton("Next");
        btnVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                startAlgorithm = true;
                repaint();
            }
        });
        add(btnVerify);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        for (Point2D.Double point : points) {
            g2.setColor(Color.black);

            g2.fill(new Ellipse2D.Double(point.x - radius / 2, point.y - radius / 2, radius, radius));
        }

        if (points.size() >= 1) {
            if (drawLine == true && btnPressed == true) {
                for (int i = 0; i < points.size() - 1; i++) {
                    Point2D.Double p1, p2;
                    p1 = points.get(i);
                    p2 = points.get(i + 1);

                    g2.setColor(Color.black);
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(new Line2D.Double(p1, p2));
                }

                g2.draw(new Line2D.Double(points.get(points.size() - 1), points.get(0)));
            }

            if (btnPressed == true && drawPoint == true) {
                g2.fill(new Ellipse2D.Double(punct.x - radius / 2, punct.y - radius / 2, radius, radius));
            }
        }

        if (triangulate == true && btnPressed == true) {
            Point2D.Double pivot = new Point2D.Double();
            pivot = points.get(0);
            for (int i = 2; i < points.size() - 1; i++) {
                Point2D.Double p1, p2;
                p1 = points.get(i);

                g2.setColor(Color.black);
                g2.setStroke(new BasicStroke(2));
                g2.draw(new Line2D.Double(pivot, p1));
            }
        }

        if (clearScreen == true) {
            g2.clearRect(0, 0, getWidth(), getHeight());
            drawLine = false;
            drawPoint = false;
            btnPressed = false;
            triangulate = false;
            clearScreen = false;
            points.clear();
        }

        g2.setColor(Color.cyan);
        g2.fill(new Rectangle2D.Double(0, 0, 1300, 100));


        if (startAlgorithm == true && !finishedAlgorithm) {
            if (!pointDeclared) {
                g2.setColor(Color.red);
                g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
                g2.drawString("No point declared!", 100, 60);
            } else {
                System.out.println("Start algorithm");
                if (counter <= points.size() - 2) {
                    System.out.println("points.size(): " + points.size());
                    System.out.println("counter: " + counter);

                    //Graphics
                    x[0] = points.get(0).getX();
                    y[0] = points.get(0).getY();

                    x[1] = points.get(counter).getX();
                    y[1] = points.get(counter).getY();

                    x[2] = points.get(counter + 1).getX();
                    y[2] = points.get(counter + 1).getY();

                    GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x.length);

                    polygon.moveTo(x[0], y[0]);

                    g2.setColor(Color.black);
                    g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
                    g2.drawString("Working on triangle A0, A" + counter + ", A" + (counter + 1) + "... ", 100, 60);
                    for (int i = 1; i < x.length; i++) {
                        polygon.lineTo(x[i], y[i]);
                    }

                    g2.setColor(Color.red);
                    polygon.closePath();
                    //g2.draw(polygon);
                    g2.fill(polygon);
                    g2.setColor(Color.black);
                    g2.fill(new Ellipse2D.Double(punct.x - radius / 2, punct.y - radius / 2, radius, radius));

                    //Algorithm
                    if (insideTriangle(points.get(0), points.get(counter), points.get(counter + 1)) == true) {
                        inside = true;
                        finishedAlgorithm = true;
                    }
                }

                if (counter > points.size() - 2) {
                    finishedAlgorithm = true;
                    inside = false;
                }


                System.out.println("finishedAlgorithm: " + finishedAlgorithm);
                if (finishedAlgorithm == true) {
                    g2.setColor(Color.red);
                    if (inside == true) {
                        if (!onEdge) {
                            g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize + 10));
                            g2.drawString("The point is inside the polygon", 100, 90);
                        } else if (counter == 1 || counter == points.size() - 1) {
                            g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize + 10));
                            g2.drawString("The point is on an edge of the polygon", 100, 90);
                        }
                    } else {
                        g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize + 10));
                        g2.drawString("The point is outside the polygon", 100, 90);
                    }
                }
            }
        }
    }

    public double dotProduct(Point2D.Double x, Point2D.Double y) {
        return ((x.getX() * y.getX()) + (x.getY() * y.getY()));
    }

    public boolean insideTriangle(Point2D.Double A, Point2D.Double B, Point2D.Double C) {
        double u, v, dot00, dot01, dot02, dot11, dot12, impartitor;

        //v0 = C-A, v1 = B-A, v2 = punct - A
        v0.setLocation(C.getX() - A.getX(), C.getY() - A.getY());
        v1.setLocation(B.getX() - A.getX(), B.getY() - A.getY());
        v2.setLocation(punct.getX() - A.getX(), punct.getY() - A.getY());

        //Facem calculele necesare pentru a afla u, v( punct = A + u * v0 + v * v1)
        dot00 = dotProduct(v0, v0);
        dot01 = dotProduct(v0, v1);
        dot02 = dotProduct(v0, v2);
        dot11 = dotProduct(v1, v1);
        dot12 = dotProduct(v1, v2);
        System.out.println(v2.getX() + " " + v2.getY());
        impartitor = 1 / (dot00 * dot11 - dot01 * dot01);
        u = (dot11 * dot02 - dot01 * dot12) * impartitor;
        v = (dot00 * dot12 - dot01 * dot02) * impartitor;

        System.out.println("u :" + u + " v: " + v);
        if (u >= 0 && v >= 0) {
            if (u + v < 1) {
                return true;
            } else if (u + v == 1) {
                onEdge = true;
                return true;
            } else {
                return false;
            }
        }

        return false;
    }


}


