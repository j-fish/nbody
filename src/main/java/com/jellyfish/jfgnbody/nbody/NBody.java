package com.jellyfish.jfgnbody.nbody;

import com.jellyfish.jfgnbody.gui.GUIDTO;
import com.jellyfish.jfgnbody.interfaces.NBodyForceComputable;
import com.jellyfish.jfgnbody.interfaces.Writable;
import com.jellyfish.jfgnbody.nbody.entities.Body;
import com.jellyfish.jfgnbody.nbody.barneshut.Quadrant;
import com.jellyfish.jfgnbody.nbody.constants.NBodyConst;
import com.jellyfish.jfgnbody.nbody.entities.SupermassiveBody;
import com.jellyfish.jfgnbody.nbody.force.BHTreeForceUpdater;
import com.jellyfish.jfgnbody.nbody.space.SpatialArea;
import com.jellyfish.jfgnbody.utils.BodyGenerationUtils;
import com.jellyfish.jfgnbody.utils.StopWatch;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;

/**
 *
 * @author thw
 */
public class NBody extends javax.swing.JPanel implements ComponentListener {

    //<editor-fold defaultstate="collapsed" desc="vars">
    /**
     * Count of Body classes to instanciate.
     */
    public int N;

    /**
     * Collection of Body instances.
     */
    public final HashMap<Integer, Body> bodyMap = new HashMap<>();

    /**
     * Stop watch util.
     */
    private StopWatch stopWatch;
    
    /**
     * Spatial partitioning area.
     */
    public SpatialArea spatialArea = null;
    
    /**
     * The draw counter constant - reduce to perform drawing at lower interval - 
     * If = 64, then drawinf or painting will be performed every 64 iterations.
     */
    private static final int DRAW_COUNTER = 64;
    
    /**
     * Drow count - do not perform draw on every iteration.
     */
    private int drawCount = 0;
    
    /**
     * Global space quandrant.
     */
    private final Quadrant q = new Quadrant(0, 0, 8 * NBodyConst.NBODY_MASS_CONST); // Previously new Quadrant(0, 0, 2 * 1e18)
    
    /**
     * Interface for updating forces.
     */
    private NBodyForceComputable fu = new BHTreeForceUpdater();
    
    /**
     * Data output writer.
     */
    private Writable writer = null;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * @param n number of bodies.
     * @param iterationSpeed iteration speed for StopWatch.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public NBody(final int n, final double iterationSpeed) {
        this.N = n;
        BodyGenerationUtils.startBodies(N, this, 1);
        this.stopWatch = new StopWatch(iterationSpeed);
        this.addComponentListener(this);
        this.setBackground(new Color(8,19,35));
    }
    
    /**
     * @param n
     * @param iterationSpeed
     * @param writer 
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public NBody(final int n, final double iterationSpeed, final Writable writer) {
        this(n, iterationSpeed);
        this.writer = writer;
        this.writer.setParent(this);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public void paint(Graphics g) {

        if (performPaint() && this.stopWatch.hasReachedMaxElapsedMS()) {
            
            NBodyData.bodyCount = 0;
            g.clearRect(0, 0, this.getWidth(), this.getHeight());
            // Originally the origin is in the top right. Put it in its normal place :
            g.translate(this.getWidth() / 2, this.getHeight() / 2);

            if (!(this.bodyMap.size() > 0)) return;

            for (Body b : this.bodyMap.values()) {   
                
                NBodyData.bodyCount++;
                g.setColor(b.graphics.color);
                if (b instanceof SupermassiveBody) {
                    g.drawOval(b.graphics.graphicX, b.graphics.graphicY, b.graphics.graphicSize,
                        b.graphics.graphicSize);
                } else {
                    g.fillOval(b.graphics.graphicX, b.graphics.graphicY, b.graphics.graphicSize,
                        b.graphics.graphicSize);
                }
            }

            if (!GUIDTO.pause) {
                NBodyData.iterationCount++;
                cleanBodyMap();
                fu.addForces(getWidth(), getHeight(), q, bodyMap);
                if (this.stopWatch != null) {
                    this.stopWatch.start();
                }

                if (this.writer != null && GUIDTO.displayOutput) this.writer.appendData(NBodyData.getFormattedData());
            }
        }
        
        super.repaint();
    }

    /**
     * Remove all bodies that have collided with more massiv bodies.
     */
    private void cleanBodyMap() {

        final int[] keys = new int[this.bodyMap.size()];
        int i = 0;
        for (Body b : this.bodyMap.values()) {
            if (b.swallowed) {
                keys[i] = b.graphics.key;
                ++i;
            }
        }

        for (int j = 0; j < i; j++) {
            this.bodyMap.remove(keys[j]);
        }
    }

    /**
     * Restart a new simulation.
     * @param n
     * @param iSpeed 
     * @param mN 
     */
    public void restart(int n, int iSpeed, final Integer mN) {
        this.N = n;
        this.bodyMap.clear();
        if (mN == 0) {
            BodyGenerationUtils.startBodies(N, this);
        } else {
            BodyGenerationUtils.startBodies(N, this, mN);
        }
        this.stopWatch = new StopWatch(iSpeed);
        this.spatialArea.updateSize(this.getWidth(), this.getHeight());
        NBodyData.bodyCount = 0;
        NBodyData.iterationCount = 0;
        NBodyData.superMassiveBodyMass = 0.0;
    }
    
    private boolean performPaint() {
        
        if (NBody.DRAW_COUNTER > this.drawCount) {
            this.drawCount++;
            return false;
        } else {
            this.drawCount = 0;
            return true;
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="GUI called methods">
    /**
     * Switch or swap interface.
     * @param fu 
     */
    public void swapForceUpdater(final NBodyForceComputable fu) {
        this.fu = fu;
    }

    public void setWriter(final Writable w) {
        this.writer = w;
    }
    
    public Writable getWriter() {
        return this.writer;
    }
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="javax.swing.JPanel overrides">
    @Override
    public void componentResized(ComponentEvent evt) {
        if (this.spatialArea != null) this.spatialArea.updateSize(this.getWidth(), this.getHeight());
    }
    
    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }
    //</editor-fold>

}