package ComponentConnector;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * The class represents base line model and rendering according to multiple params.
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * @author Stanislav Lapitsky
 * @version 1.0
 */
public class ConnectLine {    
    public static final int LINE_TYPE_RECT_1BREAK = 1;
    public static final int LINE_TYPE_RECT_2BREAK = 2;

    public static final int LINE_START_HORIZONTAL = 0;
    public static final int LINE_START_VERTICAL = 1;

  
    public static final int LINE_ARROW_DEST = 2;
    

    public static int LINE_ARROW_WIDTH = 15;

    /**
     * Source line point
     */
    Point pointSource;
    /**
     * Destination line point
     */
    Point pointDestination;

    /**
     * Line type
     */
    int lineType = LINE_TYPE_RECT_1BREAK;
    
    /**
     * for the LINE_TYPE_RECT_2BREAK type the param defines how line should be rendered
     */
    int lineStart = LINE_START_HORIZONTAL;
    /**     
     * LINE_ARROW_DEST - arrow beside dest point     
     */
    
    int lineArrow = LINE_ARROW_DEST;
    
    /**
     * Constructs line with specified params
     * @param p1 Point start
     * @param p2 Point end
     * @param lineType int type of line (LINE_TYPE_SIMPLE, LINE_TYPE_RECT_1BREAK, LINE_TYPE_RECT_2BREAK)
     * @param lineStart int for the LINE_TYPE_RECT_2BREAK type the param defines how line should be rendered
     * @param lineArrow int defines line arrow type
     */
    public ConnectLine(Point p1, Point p2, int lineType, int lineStart, int lineArrow) {
        this.pointSource = p1;
        this.pointDestination = p2;
        this.lineType = lineType;
        this.lineStart = lineStart;
        this.lineArrow = lineArrow;
    }

    /**
     * Paints the line with specified params
     * @param g2d Graphics2D
     */
    public void paint(Graphics2D g2d) {
        switch (lineType) {          
            case LINE_TYPE_RECT_1BREAK:
                paint1Break(g2d);
                break;
            case LINE_TYPE_RECT_2BREAK:
                paint2Breaks(g2d);
                break;
        }
    }  

    protected void paintArrow(Graphics2D g2d, Point p1, Point p2) {
        paintArrow(g2d, p1, p2, getRestrictedArrowWidth(p1, p2));
    }

    protected void paintArrow(Graphics2D g2d, Point p1, Point p2, int width) {
        Point2D.Float pp1 = new Point2D.Float(p1.x, p1.y);
        Point2D.Float pp2 = new Point2D.Float(p2.x, p2.y);
        Point2D.Float left = getLeftArrowPoint(pp1, pp2, width);
        Point2D.Float right = getRightArrowPoint(pp1, pp2, width);

        g2d.drawLine(p2.x, p2.y, Math.round(left.x), Math.round(left.y));
        g2d.drawLine(p2.x, p2.y, Math.round(right.x), Math.round(right.y));
    }

    protected void paint1Break(Graphics2D g2d) {
        if (lineStart == LINE_START_HORIZONTAL) {
            g2d.drawLine(pointSource.x, pointSource.y, pointDestination.x, pointSource.y);
            g2d.drawLine(pointDestination.x, pointSource.y, pointDestination.x, pointDestination.y);
            switch (lineArrow) {
                case LINE_ARROW_DEST:
                    paintArrow(g2d, new Point(pointDestination.x, pointSource.y), pointDestination);
                    break;                
            }
        }
        else if (lineStart == LINE_START_VERTICAL) {
            g2d.drawLine(pointSource.x, pointSource.y, pointSource.x, pointDestination.y);
            g2d.drawLine(pointSource.x, pointDestination.y, pointDestination.x, pointDestination.y);
            switch (lineArrow) {
                case LINE_ARROW_DEST:
                    paintArrow(g2d, new Point(pointSource.x, pointDestination.y), pointDestination);
                    break;                
            }
        }
    }

    protected void paint2Breaks(Graphics2D g2d) {
        if (lineStart == LINE_START_HORIZONTAL) {
            g2d.drawLine(pointSource.x, pointSource.y, pointSource.x + (pointDestination.x - pointSource.x) / 2, pointSource.y);
            g2d.drawLine(pointSource.x + (pointDestination.x - pointSource.x) / 2, pointSource.y, pointSource.x + (pointDestination.x - pointSource.x) / 2, pointDestination.y);
            g2d.drawLine(pointSource.x + (pointDestination.x - pointSource.x) / 2, pointDestination.y, pointDestination.x, pointDestination.y);
            switch (lineArrow) {
                case LINE_ARROW_DEST:
                    paintArrow(g2d, new Point(pointSource.x + (pointDestination.x - pointSource.x) / 2, pointDestination.y), pointDestination);
                    break;                
            }
        }
        else if (lineStart == LINE_START_VERTICAL) {
            g2d.drawLine(pointSource.x, pointSource.y, pointSource.x, pointSource.y + (pointDestination.y - pointSource.y) / 2);
            g2d.drawLine(pointSource.x, pointSource.y + (pointDestination.y - pointSource.y) / 2, pointDestination.x, pointSource.y + (pointDestination.y - pointSource.y) / 2);
            g2d.drawLine(pointDestination.x, pointSource.y + (pointDestination.y - pointSource.y) / 2, pointDestination.x, pointDestination.y);

            switch (lineArrow) {
                case LINE_ARROW_DEST:
                    paintArrow(g2d, new Point(pointDestination.x, pointSource.y + (pointDestination.y - pointSource.y) / 2), pointDestination);
                    break;               
            }
        }
    }

    public int getLineType() {
        return lineType;
    }

    public void setLineType(int type) {
        lineType = type;
    }

    public int getLineStart() {
        return lineStart;
    }

    public void setLineStart(int start) {
        lineStart = start;
    }

    public int getLineArrow() {
        return lineArrow;
    }

    public void setLineArrow(int arrow) {
        lineType = lineArrow;
    }

    public Point getPointSource() {
        return pointSource;
    }

    public void setPointSource(Point p) {
        pointSource = p;
    }

    public Point getPointDestination() {
        return pointDestination;
    }

    public void setPointDestination(Point p) {
        pointDestination = p;
    }

    protected static Point2D.Float getMidArrowPoint(Point2D.Float p1, Point2D.Float p2, float w) {
        Point2D.Float res = new Point2D.Float();
        float d = Math.round(Math.sqrt( (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));

        if (p1.x < p2.x) {
            res.x = p2.x - w * Math.abs(p1.x - p2.x) / d;
        }
        else {
            res.x = p2.x + w * Math.abs(p1.x - p2.x) / d;
        }

        if (p1.y < p2.y) {
            res.y = p2.y - w * Math.abs(p1.y - p2.y) / d;
        }
        else {
            res.y = p2.y + w * Math.abs(p1.y - p2.y) / d;
        }

        return res;
    }

    protected static Point2D.Float getLeftArrowPoint(Point2D.Float p1, Point2D.Float p2) {
        return getLeftArrowPoint(p1, p2, LINE_ARROW_WIDTH);
    }

    protected static Point2D.Float getLeftArrowPoint(Point2D.Float p1, Point2D.Float p2, float w) {
        Point2D.Float res = new Point2D.Float();
        double alpha = Math.PI / 2;
        if (p2.x != p1.x) {
            alpha = Math.atan( (p2.y - p1.y) / (p2.x - p1.x));
        }
        alpha += Math.PI / 10;
        float xShift = Math.abs(Math.round(Math.cos(alpha) * w));
        float yShift = Math.abs(Math.round(Math.sin(alpha) * w));
        if (p1.x <= p2.x) {
            res.x = p2.x - xShift;
        }
        else {
            res.x = p2.x + xShift;
        }
        if (p1.y < p2.y) {
            res.y = p2.y - yShift;
        }
        else {
            res.y = p2.y + yShift;
        }
        return res;
    }

    protected static Point2D.Float getRightArrowPoint(Point2D.Float p1, Point2D.Float p2) {
        return getRightArrowPoint(p1, p2, LINE_ARROW_WIDTH);
    }

    protected static Point2D.Float getRightArrowPoint(Point2D.Float p1, Point2D.Float p2, float w) {
        Point2D.Float res = new Point2D.Float();
        double alpha = Math.PI / 2;
        if (p2.x != p1.x) {
            alpha = Math.atan( (p2.y - p1.y) / (p2.x - p1.x));
        }
        alpha -= Math.PI / 10;
        float xShift = Math.abs(Math.round(Math.cos(alpha) * w));
        float yShift = Math.abs(Math.round(Math.sin(alpha) * w));
        if (p1.x < p2.x) {
            res.x = p2.x - xShift;
        }
        else {
            res.x = p2.x + xShift;
        }
        if (p1.y <= p2.y) {
            res.y = p2.y - yShift;
        }
        else {
            res.y = p2.y + yShift;
        }
        return res;
    }

    protected int getRestrictedArrowWidth(Point p1, Point p2) {
        return Math.min(LINE_ARROW_WIDTH, (int) Math.sqrt( (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
    }
}
