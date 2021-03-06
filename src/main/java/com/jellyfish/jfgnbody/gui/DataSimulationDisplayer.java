package com.jellyfish.jfgnbody.gui;

import com.jellyfish.jfgnbody.interfaces.NBodyDrawable;
import com.jellyfish.jfgnbody.interfaces.Writable;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author thw
 */
public class DataSimulationDisplayer extends javax.swing.JFrame implements Writable {

    /**
     * Parent panel.
     */
    private NBodyDrawable nb = null;
    
    /**
     * Creates new form DataSimulationDisplayer
     */
    public DataSimulationDisplayer() {
        initComponents();
        this.setBackground(new Color(250,250,250));
        this.getContentPane().setBackground(new Color(250,250,250));
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        this.setIconImage(icon);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("NBody simulation data output");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        scrollPane.setBorder(null);
        scrollPane.setDoubleBuffered(true);

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setFont(new java.awt.Font("Lucida Console", 0, 14)); // NOI18N
        textArea.setRows(5);
        textArea.setBorder(null);
        scrollPane.setViewportView(textArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        GUIDTO.displayOutput = false;
        this.setVisible(false);
        this.dispose();
        if (this.nb != null) this.nb.setWriter(null);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables

    @Override
    public void writeln(final String l) { 
        this.textArea.append(l + "\n");
    }

    @Override
    public void write(final String l) { }

    @Override
    public void appendData(final String data) { 
        this.textArea.append(data + "\n");
        this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
    }

    @Override
    public void setParent(final NBodyDrawable nb) {
        this.nb = nb;
    }
    
}
