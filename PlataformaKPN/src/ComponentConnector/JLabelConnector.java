package ComponentConnector;

import static ComponentConnector.ConnectLine.LINE_ARROW_DEST;
import javax.swing.*;
import java.awt.*;


/**
 * The class represents pair of components with a connecting line.
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * @author Stanislav Lapitsky
 * @version 1.0
 */
public class JLabelConnector extends JPanel {    
    public static final int CONNECT_LINE_TYPE_RECTANGULAR = 1;
    protected JComponent source;
    protected JComponent dest;
    protected ConnectLine line;
    protected int lineArrow = ConnectLine.LINE_ARROW_DEST;
    protected int lineType = CONNECT_LINE_TYPE_RECTANGULAR;
    protected Color lineColor;

   
   

    /**
     * Constructs a connector with specified arrow, line type and color.
     * @param source JComponent
     * @param dest JComponent
     * @param lineColor Color
     */
    public JLabelConnector(JComponent source, JComponent dest, Color lineColor) {
        this.source = source;
        this.dest = dest;
        this.lineArrow = LINE_ARROW_DEST;
        this.lineType = CONNECT_LINE_TYPE_RECTANGULAR;
        this.lineColor = lineColor;
    }

    /**
     * Overrides parent's paint(). It resets clip to draw connecting line
     * between components and set the clip back.
     * @param g Graphics
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        calculateLine();
        if (line != null) {
            Shape oldClip = g2d.getClip();
            g2d.setClip(getLineBounds());
            g2d.setColor(lineColor);
            line.paint(g2d);
            g2d.setClip(oldClip);
        }
    }

    protected void calculateLine() {
        Rectangle rSource = source.getBounds();
        Rectangle rDest = dest.getBounds();
        if (rSource.intersects(rDest)) {
            line = null;
            return;
        }

        boolean xIntersect = (rSource.x <= rDest.x && rSource.x + rSource.width >= rDest.x)
            || (rDest.x <= rSource.x && rDest.x + rDest.width >= rSource.x);
        boolean yIntersect = rSource.y <= rDest.y && rSource.y + rSource.height >= rDest.y
            || (rDest.y <= rSource.y && rDest.y + rDest.height >= rSource.y);

        if (xIntersect) {
            int y1;
            int y2;
            int x1 = rSource.x + rSource.width / 2;
            int x2 = rDest.x + rDest.width / 2;
            if (rSource.y + rSource.height <= rDest.y) {
                //source higher
                y1 = rSource.y + rSource.height;
                y2 = rDest.y;
            }
            else {
                y1 = rSource.y;
                y2 = rDest.y + rDest.height;
            }
            line = new ConnectLine(new Point(x1, y1), new Point(x2, y2), ConnectLine.LINE_TYPE_RECT_2BREAK, ConnectLine.LINE_START_VERTICAL, lineArrow);
            
        }
        else if (yIntersect) {
            int y1 = rSource.y + rSource.height / 2;            
            int y2 = rDest.y + rDest.height / 2;            
            int x1;
            int x2;
            if (rSource.x + rSource.width <= rDest.x) {
                x1 = rSource.x + rSource.width;
                x2 = rDest.x;
            }
            else {
                x1 = rSource.x;
                x2 = rDest.x + rDest.width;
            }
            line = new ConnectLine(new Point(x1, y1), new Point(x2, y2), ConnectLine.LINE_TYPE_RECT_2BREAK, ConnectLine.LINE_START_HORIZONTAL, lineArrow);
            
        }
        else {
            int y1;
            int y2;
            int x1;
            int x2;
            if (rSource.y + rSource.height <= rDest.y) {
                //source higher
                y1 = rSource.y + rSource.height / 2;
                y2 = rDest.y;
                if (rSource.x + rSource.width <= rDest.x) {
                    x1 = rSource.x + rSource.width;
                }
                else {
                    x1 = rSource.x;
                }
                x2 = rDest.x + rDest.width / 2;
            }
            else {
                y1 = rSource.y + rSource.height / 2;
                y2 = rDest.y + rDest.height;
                if (rSource.x + rSource.width <= rDest.x) {
                    x1 = rSource.x + rSource.width;
                }
                else {
                    x1 = rSource.x;
                }
                x2 = rDest.x + rDest.width / 2;
            }
            line = new ConnectLine(new Point(x1, y1), new Point(x2, y2), ConnectLine.LINE_TYPE_RECT_1BREAK, ConnectLine.LINE_START_HORIZONTAL, lineArrow);
           
        }
    }

    protected Rectangle getLineBounds() {
        int add = 10;
        int maxX = Math.max(line.getPointSource().x, line.getPointDestination().x);
        int minX = Math.min(line.getPointSource().x, line.getPointDestination().x);
        int maxY = Math.max(line.getPointSource().y, line.getPointDestination().y);
        int minY = Math.min(line.getPointSource().y, line.getPointDestination().y);

        Rectangle res = new Rectangle(minX - add, minY - add, maxX - minX + 2 * add, maxY - minY + 2 * add);
        return res;
    }
    
}
