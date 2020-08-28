package it.saldimic.birds;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
 
public class BirdsPanel extends JPanel {
	Fleet fleet;
    final int widthP, heightP;
    static Timer timer;
    static JTextField nBirdText;
    static JTextField dimBirdText;
    int nBird = 180;
    int sizeBird = 1;
    int velBird = 15;
    
    public BirdsPanel() {
    	nBird = 180;
    	
    	widthP = 1350;
    	heightP = 680;
 
        setPreferredSize(new Dimension(widthP, heightP));
        setBackground(Color.black);
 
        spawnFlock(nBird, sizeBird);
        
        try {
        	velBird = Integer.parseInt(dimBirdText.getText());
    	}catch(Exception ex) {}
        
        timer = new Timer(velBird, (ActionEvent e) -> {
            if (fleet.hasLeftTheBuilding(widthP)) {
            	try {
            		nBird = Integer.parseInt(nBirdText.getText());
            	}catch(Exception ex) {}                
            	try {
            		sizeBird = Integer.parseInt(dimBirdText.getText());
            	}catch(Exception ex) {}                
            	spawnFlock(nBird, sizeBird);
            }
            repaint();
        });
        
    }
 
    private void spawnFlock(int nBird, int sizeBird) {
    	fleet = Fleet.spawn(-300, heightP * 0.5, nBird, sizeBird);
    }
 
    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
 
        fleet.run(g, widthP, heightP);
    }
 
    public static void main(String[] args) {
    	JPanel settingP = new JPanel();
//    	settingP.setSize(1470, 30);
    	JButton btnStart = new JButton("Start");
    	btnStart.setSize(20, 10);
    	btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.restart();
			}    		
    	});
    	
    	JButton btnStop = new JButton("Stop");
    	btnStop.setSize(20, 10);
    	btnStop.addActionListener(new ActionListener() {    		
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			timer.stop();
    		}    		
    	});
    	
    	JLabel nBird = new JLabel("N. Bird:");
    	JLabel dimBird = new JLabel("Vel. Bird:");
    	nBirdText = new JTextField(4);
    	nBirdText.setText("180");
    	dimBirdText = new JTextField(3);
    	dimBirdText.setText("15");
    	settingP.add(nBird, null);
    	settingP.add(nBirdText, null);
    	settingP.add(dimBird, null);
    	settingP.add(dimBirdText, null);
    	settingP.add(btnStart, null);
    	settingP.add(btnStop, null);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(1400, 680);
            frame.setTitle("Birds - Volo di uno stormo di uccelli");
            frame.setBackground(Color.white);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new BirdsPanel(), BorderLayout.SOUTH);
            frame.add(settingP, BorderLayout.NORTH);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
 
