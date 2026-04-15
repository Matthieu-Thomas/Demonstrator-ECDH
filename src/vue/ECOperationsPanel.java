package vue;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Panneau pour visualiser les opérations sur courbe elliptique
 */
public class ECOperationsPanel extends JPanel {
    private final int defaultWidth = 500;
    private final int defaultHeight = 600;

    private final double a = -1;
    private final double b = 1;
    
    // Points pour le doublement
    private final Point2D.Double pointA = new Point2D.Double(0, 1);
    private Point2D.Double point2A;
    private Point2D.Double pointMinus2A;
    
    // Points pour l'addition
    private final Point2D.Double pointB = new Point2D.Double(0.5, 0.7906);
    private Point2D.Double pointAPlusB;
    private Point2D.Double pointMinusAPlusB;
    
    private int currentStep = 0;
    private OperationType operationType = OperationType.DOUBLING;
    
    // Panel de contrôle
    private JPanel controlPanel;
    
    public enum OperationType {
        DOUBLING, ADDITION
    }
    
    public ECOperationsPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(defaultWidth, defaultHeight));
        setBackground(Color.WHITE);
        
        // Panel de dessin
        JPanel drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                
                drawAxes(g2);
                drawCurveZoomed(g2);
                
                if (operationType == OperationType.DOUBLING) {
                    drawDoublingSteps(g2);
                } else {
                    drawAdditionSteps(g2);
                }
                
                drawOperationTitle(g2);
            }
        };
        drawPanel.setPreferredSize(new Dimension(defaultWidth, defaultHeight - 100));
        drawPanel.setBackground(Color.WHITE);
        
        // Panel de contrôle
        createControlPanel();
        
        add(drawPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        calculatePoints();
    }
    
    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Boutons de choix d'opération
        JButton btnDoubling = new JButton("Doublement (2A)");
        JButton btnAddition = new JButton("Addition (A+B)");
        
        btnDoubling.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAddition.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        btnDoubling.addActionListener(e -> {
            operationType = OperationType.DOUBLING;
            currentStep = 0;
            repaint();
        });
        
        btnAddition.addActionListener(e -> {
            operationType = OperationType.ADDITION;
            currentStep = 0;
            repaint();
        });
        
        // Boutons de contrôle d'animation
        JButton btnReset = new JButton("⟲ Réinitialiser");
        
        btnReset.addActionListener(e -> reset());
        
        controlPanel.add(btnDoubling);
        controlPanel.add(btnAddition);
        controlPanel.add(new JSeparator(JSeparator.VERTICAL));
        controlPanel.add(btnReset);
    }
    
    private void calculatePoints() {
        // Calcul pour doublement
        double x1 = pointA.x;
        double y1 = pointA.y;
        double lambda = (3 * x1 * x1 + a) / (2 * y1);
        double x3 = lambda * lambda - 2 * x1;
        double y3 = lambda * (x1 - x3) - y1;
        
        pointMinus2A = new Point2D.Double(x3, -y3);
        point2A = new Point2D.Double(x3, y3);
        
        // Calcul pour addition A+B
        double xA = pointA.x;
        double yA = pointA.y;
        double xB = pointB.x;
        double yB = pointB.y;
        
        double lambdaAdd = (yB - yA) / (xB - xA);
        double xSum = lambdaAdd * lambdaAdd - xA - xB;
        double ySum = lambdaAdd * (xA - xSum) - yA;
        
        pointMinusAPlusB = new Point2D.Double(xSum, -ySum);
        pointAPlusB = new Point2D.Double(xSum, ySum);
    }

    private void drawAxes(Graphics2D g2) {
        int panelWidth = getWidth();
        int panelHeight = getHeight() - controlPanel.getHeight(); // hauteur utile sans le panneau de contrôle

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));

        // Axe X (horizontal au milieu)
        g2.drawLine(0, panelHeight / 2, panelWidth, panelHeight / 2);
        g2.drawString("x", panelWidth - 20, panelHeight / 2 - 10);

        // Axe Y (vertical au milieu)
        g2.drawLine(panelWidth / 2, 0, panelWidth / 2, panelHeight);
        g2.drawString("y", panelWidth / 2 + 10, 20);
    }


    /**
     * Dessine la courbe elliptique en ROUGE
     */
    private void drawCurveZoomed(Graphics2D g2) {
        g2.setColor(new Color(255, 50, 50));
        g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        List<Point2D.Double> upperBranch = new ArrayList<>();
        List<Point2D.Double> lowerBranch = new ArrayList<>();
        
        for (double x = -2; x <= 2; x += 0.03) {
            double rhs = x * x * x + a * x + b;
            if (rhs >= 0) {
                double y = Math.sqrt(rhs);
                upperBranch.add(new Point2D.Double(x, y));
                if (y != 0) {
                    lowerBranch.add(new Point2D.Double(x, -y));
                }
            }
        }
        
        upperBranch.sort((p1, p2) -> Double.compare(p1.x, p2.x));
        lowerBranch.sort((p1, p2) -> Double.compare(p1.x, p2.x));
        
        drawBranch(g2, upperBranch);
        drawBranch(g2, lowerBranch);
    }
    
    private void drawBranch(Graphics2D g2, List<Point2D.Double> branch) {
        Point2D.Double prevPoint = null;
        for (Point2D.Double point : branch) {
            if (prevPoint != null && Math.abs(point.x - prevPoint.x) < 0.1) {
                int x1 = toZoomedX(prevPoint.x);
                int y1 = toZoomedY(prevPoint.y);
                int x2 = toZoomedX(point.x);
                int y2 = toZoomedY(point.y);
                
                g2.drawLine(x1, y1, x2, y2);
            }
            prevPoint = point;
        }
    }
    
    /**
     * Dessine les étapes du DOUBLEMENT 2A
     */
    private void drawDoublingSteps(Graphics2D g2) {
        if (currentStep >= 0) {
            drawPoint(g2, pointA, "A", new Color(50, 200, 50), 10);
        }
        
        if (currentStep >= 1) {
            drawTangent(g2);
        }
        
        if (currentStep >= 2) {
            drawPoint(g2, pointMinus2A, "-2A", new Color(150, 50, 200), 8);
            
            g2.setColor(new Color(0, 180, 0, 150));
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                        BasicStroke.JOIN_BEVEL, 0, 
                                        new float[]{8, 4}, 0));
            int x = toZoomedX(pointMinus2A.x);
            g2.drawLine(x, 0, x, 500);
        }
        
        if (currentStep >= 3) {
            drawPoint(g2, point2A, "2A", new Color(50, 150, 255), 10);
        }
    }
    
    /**
     * Dessine les étapes de l'ADDITION A+B
     */
    private void drawAdditionSteps(Graphics2D g2) {
        if (currentStep >= 0) {
            drawPoint(g2, pointA, "A", new Color(50, 200, 50), 10);
            drawPoint(g2, pointB, "B", new Color(50, 200, 50), 10);
        }
        
        if (currentStep >= 1) {
            drawLineThroughPoints(g2, pointA, pointB);
        }
        
        if (currentStep >= 2) {
            drawPoint(g2, pointMinusAPlusB, "-(A+B)", new Color(150, 50, 200), 8);
            
            g2.setColor(new Color(0, 180, 0, 150));
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                        BasicStroke.JOIN_BEVEL, 0, 
                                        new float[]{8, 4}, 0));
            int x = toZoomedX(pointMinusAPlusB.x);
            g2.drawLine(x, 0, x, 500);
        }
        
        if (currentStep >= 3) {
            drawPoint(g2, pointAPlusB, "A+B", new Color(50, 150, 255), 10);
        }
    }
    
    /**
     * Dessine la tangente (pour doublement)
     */
    private void drawTangent(Graphics2D g2) {
        double x1 = pointA.x;
        double y1 = pointA.y;
        double lambda = (3 * x1 * x1 + a) / (2 * y1);
        
        double xStart = -2;
        double xEnd = 2;
        double yStart = lambda * (xStart - x1) + y1;
        double yEnd = lambda * (xEnd - x1) + y1;
        
        g2.setColor(new Color(255, 100, 0, 230));
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, 
                                    BasicStroke.JOIN_BEVEL, 0, 
                                    new float[]{10, 5}, 0));
        
        int screenX1 = toZoomedX(xStart);
        int screenY1 = toZoomedY(yStart);
        int screenX2 = toZoomedX(xEnd);
        int screenY2 = toZoomedY(yEnd);
        
        g2.drawLine(screenX1, screenY1, screenX2, screenY2);
        
        g2.setColor(new Color(255, 100, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("Tangente", toZoomedX(1.2), toZoomedY(lambda * (1.2 - x1) + y1) - 8);
    }
    
    /**
     * Dessine la ligne passant par deux points (pour addition)
     */
    private void drawLineThroughPoints(Graphics2D g2, Point2D.Double p1, Point2D.Double p2) {
        double lambda = (p2.y - p1.y) / (p2.x - p1.x);
        
        double xStart = -2;
        double xEnd = 2;
        double yStart = lambda * (xStart - p1.x) + p1.y;
        double yEnd = lambda * (xEnd - p1.x) + p1.y;
        
        g2.setColor(new Color(255, 100, 0, 230));
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, 
                                    BasicStroke.JOIN_BEVEL, 0, 
                                    new float[]{10, 5}, 0));
        
        int screenX1 = toZoomedX(xStart);
        int screenY1 = toZoomedY(yStart);
        int screenX2 = toZoomedX(xEnd);
        int screenY2 = toZoomedY(yEnd);
        
        g2.drawLine(screenX1, screenY1, screenX2, screenY2);
        
        g2.setColor(new Color(255, 100, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("Droite A-B", toZoomedX(1.2), toZoomedY(lambda * (1.2 - p1.x) + p1.y) - 8);
    }
    
    private void drawPoint(Graphics2D g2, Point2D.Double point, String label, 
                          Color color, int size) {
        int px = toZoomedX(point.x);
        int py = toZoomedY(point.y);
        
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
        g2.fillOval(px - size - 4, py - size - 4, (size + 4) * 2, (size + 4) * 2);
        
        g2.setColor(color);
        g2.fillOval(px - size, py - size, size * 2, size * 2);
        
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(px - size, py - size, size * 2, size * 2);
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString(label, px + size + 8, py - size - 5);
        
    }
    
    private void drawOperationTitle(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 245));
        g2.fillRoundRect(10, 10, 280, 45, 10, 10);
        
        g2.setColor(new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(10, 10, 280, 45, 10, 10);
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        
        if (operationType == OperationType.DOUBLING) {
            g2.drawString("Doublement de Point : 2A", 20, 32);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("Opération: A + A = 2A", 20, 52);
        } else {
            g2.drawString("Addition de Points : A + B", 20, 32);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("Opération: A + B", 20, 52);
        }
    }

    private int toZoomedX(double x) {
        int panelWidth = getWidth();
        double scale = panelWidth / 4.0; // [-2, 2] → 4 unités
        return (int) ((x + 2) * scale);
    }

    private int toZoomedY(double y) {
        int panelHeight = getHeight() - controlPanel.getHeight(); // hauteur utile
        double scale = panelHeight / 4.0; // [-2, 2] → 4 unités
        return (int) ((2 - y) * scale);
    }

    public void setStep(int step) {
        this.currentStep = step;
        repaint();
    }
    
    public void nextStep() {
        if (currentStep < 3) {
            currentStep++;
            repaint();
        }
    }
    
    public void reset() {
        currentStep = 0;
        repaint();
    }
    
    public void setOperationType(OperationType type) {
        this.operationType = type;
        currentStep = 0;
        repaint();
    }
}