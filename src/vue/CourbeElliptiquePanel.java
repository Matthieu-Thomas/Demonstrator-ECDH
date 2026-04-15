package vue;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CourbeElliptiquePanel extends JPanel {
    private final int defaultWidth = 500;
    private final int defaultHeight = 500;
    private final int scale = 50;
    private final double a = -1; // Coefficients de la courbe y² = x³ + ax + b
    private final double b = 1;
    
    private final List<Point2D.Double> points = new ArrayList<>();
    private Point2D.Double pointG, pointA, pointB, pointShared;
    
    private boolean showOperation = false;
    private Point2D.Double operationLine1, operationLine2;
    
    public CourbeElliptiquePanel() {
        setPreferredSize(new Dimension(defaultWidth, defaultHeight));
        setBackground(Color.WHITE);
        calculerPoints();
    }
    
    private void calculerPoints() {
        points.clear();
        // Calcul des points sur la courbe dans l'espace réel
        for (double x = -5; x <= 5; x += 0.1) {
            double rhs = x * x * x + a * x + b;
            if (rhs >= 0) {
                double y = Math.sqrt(rhs);
                points.add(new Point2D.Double(x, y));
                if (y != 0) {
                    points.add(new Point2D.Double(x, -y));
                }
            }
        }
        
        // Points d'exemple pour la démonstration
        pointG = new Point2D.Double(0, 1);      // Point générateur
        pointA = new Point2D.Double(1, 0);      // Point Alice
        pointB = new Point2D.Double(-1, 0);     // Point Bob
        pointShared = new Point2D.Double(0, -1); // Secret partagé
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dessiner la grille
        drawGrid(g2);
        
        // Dessiner les axes
        drawAxes(g2);
        
        // Dessiner la courbe
        drawCurve(g2);
        
        // Dessiner les points spéciaux
        drawSpecialPoints(g2);
        
        // Dessiner les opérations si activées
        if (showOperation) {
            drawOperations(g2);
        }
        
        // Légende
        drawLegend(g2);
        
        // Titre et équation
        drawTitle(g2);
    }
    
    private void drawGrid(Graphics2D g2) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        g2.setColor(new Color(240, 240, 240));
        g2.setStroke(new BasicStroke(1));
        
        // Lignes verticales
        for (int i = -10; i <= 10; i++) {
            int x = toScreenX(i);
            g2.drawLine(x, 0, x, panelHeight);
        }
        
        // Lignes horizontales
        for (int i = -10; i <= 10; i++) {
            int y = toScreenY(i);
            g2.drawLine(0, y, panelWidth, y);
        }
    }
    
    private void drawAxes(Graphics2D g2) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        
        // Axe X
        g2.drawLine(0, panelHeight / 2, panelWidth, panelHeight / 2);
        g2.drawString("x", panelWidth - 20, panelHeight / 2 - 10);
        
        // Axe Y
        g2.drawLine(panelWidth / 2, 0, panelWidth / 2, panelHeight);
        g2.drawString("y", panelWidth / 2 + 10, 20);
        
        // Graduations
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (int i = -5; i <= 5; i++) {
            if (i != 0) {
                // Graduations X
                int x = toScreenX(i);
                g2.drawLine(x, panelHeight / 2 - 5, x, panelHeight / 2 + 5);
                g2.drawString(String.valueOf(i), x - 5, panelHeight / 2 + 20);
                
                // Graduations Y
                int y = toScreenY(i);
                g2.drawLine(panelWidth / 2 - 5, y, panelWidth / 2 + 5, y);
                g2.drawString(String.valueOf(i), panelWidth / 2 + 10, y + 5);
            }
        }
    }
    
    private void drawCurve(Graphics2D g2) {
        g2.setColor(new Color(100, 100, 200));
        g2.setStroke(new BasicStroke(2));
        
        Point2D.Double prevPoint = null;
        for (Point2D.Double point : points) {
            if (prevPoint != null && 
                Math.abs(point.x - prevPoint.x) < 0.2 && 
                Math.abs(point.y - prevPoint.y) < 2) {
                
                int x1 = toScreenX(prevPoint.x);
                int y1 = toScreenY(prevPoint.y);
                int x2 = toScreenX(point.x);
                int y2 = toScreenY(point.y);
                
                g2.drawLine(x1, y1, x2, y2);
            }
            prevPoint = point;
        }
        
        // Dessiner tous les points calculés (petits)
        g2.setColor(new Color(150, 150, 200, 100));
        for (Point2D.Double point : points) {
            int px = toScreenX(point.x);
            int py = toScreenY(point.y);
            g2.fillOval(px - 1, py - 1, 2, 2);
        }
    }
    
    private void drawSpecialPoints(Graphics2D g2) {
        // Point générateur G
        drawLabeledPoint(g2, pointG, "G", new Color(255, 140, 0), 
            "Point générateur");
        
        // Point Alice
        drawLabeledPoint(g2, pointA, "PA", new Color(255, 20, 147), 
            "Clé publique Alice");
        
        // Point Bob
        drawLabeledPoint(g2, pointB, "PB", new Color(30, 144, 255), 
            "Clé publique Bob");
        
        // Point secret partagé
        drawLabeledPoint(g2, pointShared, "S", new Color(50, 205, 50), 
            "Secret partagé");
    }
    
    private void drawLabeledPoint(Graphics2D g2, Point2D.Double point, 
                                  String label, Color color, String description) {
        int px = toScreenX(point.x);
        int py = toScreenY(point.y);
        
        // Cercle extérieur
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
        g2.fillOval(px - 10, py - 10, 20, 20);
        
        // Point
        g2.setColor(color);
        g2.fillOval(px - 6, py - 6, 12, 12);
        
        // Label
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString(label, px + 10, py - 10);
        
    }
    
    private void drawOperations(Graphics2D g2) {
        // Dessiner une ligne illustrant l'addition de points
        if (operationLine1 != null && operationLine2 != null) {
            g2.setColor(new Color(255, 0, 0, 150));
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                        BasicStroke.JOIN_BEVEL, 0, 
                                        new float[]{9}, 0));
            
            int x1 = toScreenX(operationLine1.x);
            int y1 = toScreenY(operationLine1.y);
            int x2 = toScreenX(operationLine2.x);
            int y2 = toScreenY(operationLine2.y);
            
            g2.drawLine(x1, y1, x2, y2);
        }
    }
    
    private void drawLegend(Graphics2D g2) {
        int legendX = 10;
        int legendY = getHeight() - 130;

        // Fond de la légende
        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(legendX, legendY, 180, 120, 10, 10);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(legendX, legendY, 180, 120, 10, 10);
        
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2.drawString("Légende", legendX + 10, legendY + 20);
        
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        int lineHeight = 18;
        int currentY = legendY + 40;
        
        // Points
        drawLegendItem(g2, legendX + 10, currentY, 
                      new Color(255, 140, 0), "G : Générateur");
        currentY += lineHeight;
        
        drawLegendItem(g2, legendX + 10, currentY, 
                      new Color(255, 20, 147), "PA : Alice = nA × G");
        currentY += lineHeight;
        
        drawLegendItem(g2, legendX + 10, currentY, 
                      new Color(30, 144, 255), "PB : Bob = nB × G");
        currentY += lineHeight;
        
        drawLegendItem(g2, legendX + 10, currentY, 
                      new Color(50, 205, 50), "S : Secret partagé");
    }
    
    private void drawLegendItem(Graphics2D g2, int x, int y, Color color, String text) {
        g2.setColor(color);
        g2.fillOval(x, y - 6, 8, 8);
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 15, y);
    }
    
    private void drawTitle(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(10, 10, 260, 50, 10, 10);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(10, 10, 260, 50, 10, 10);
        
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Courbe Elliptique", 20, 30);
        
        g2.setFont(new Font("Serif", Font.ITALIC, 12));
        g2.drawString("y² = x³ + ax + b", 20, 50);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.drawString(String.format("avec a = %.0f, b = %.0f", a, b), 150, 50);
    }

    private int toScreenX(double x) {
        int panelWidth = getWidth();
        return (int) (panelWidth / 2 + x * scale);
    }

    private int toScreenY(double y) {
        int panelHeight = getHeight();
        return (int) (panelHeight / 2 - y * scale);
    }


    // Méthodes pour contrôler l'animation
    public void showPointAddition(boolean show) {
        this.showOperation = show;
        if (show) {
            operationLine1 = pointG;
            operationLine2 = pointA;
        }
        repaint();
    }
    
    public void setPointPositions(Point2D.Double g, Point2D.Double a, 
                                 Point2D.Double b, Point2D.Double shared) {
        if (g != null) this.pointG = g;
        if (a != null) this.pointA = a;
        if (b != null) this.pointB = b;
        if (shared != null) this.pointShared = shared;
        repaint();
    }
}