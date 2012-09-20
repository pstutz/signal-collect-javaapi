package com.signalcollect.javaapi.examples.gameoflife;

import java.awt.*;
import javax.swing.*;

/**
 * Utility do display data with a limited amount of values in a grid.
 * 
 * @author Daniel Strebel
 *
 */
public class PixelMap extends JComponent {

	private static final long serialVersionUID = 1L;
	private int width = 500;
	private int height = 500;
	private byte[] imageValues;
	JFrame window;
	private boolean isPainted = false;
	
	public PixelMap(String title) {
		window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(new Color(140, 152, 181, 10));
		window.setBounds(30, 30, width, height);
		window.getContentPane().add(this);
	}

	/**
	 * Sets the data for display and updates the image.
	 * 
	 * @param data the data to be displayed.
	 */
	public void setData(byte[] data) {
		imageValues = data;
		this.repaint();
		if(!window.isVisible()) {
			window.setVisible(true);			
		}
	}

	/**
	 * Displays an image if the data is available.
	 * 
	 * @note the data will be displayed as a squared image.
	 */
	public void paint(Graphics g) {
		g.clearRect(0, 0, window.getWidth(), window.getHeight());
		if (imageValues != null) {
			// determine the scaling
			int widthOfPixel = width
					/ (int) Math.floor(Math.sqrt(imageValues.length));
			int heightOfPixel = widthOfPixel;
			
			
			
			//Print the pixels
			for (int i = 0; i < imageValues.length; i++) {
				if (imageValues[i] == 0) {
					g.setColor(new Color(140, 152, 181, 60));
				} else {
					g.setColor(new Color(63, 76, 107, 160));
				}
				g.fillRect((i % (width / widthOfPixel)) * widthOfPixel,
						(i / (height / heightOfPixel)) * heightOfPixel,
						widthOfPixel, heightOfPixel);
			}
			
			//draw grid
			g.setColor(new Color(20, 35, 70, 128));
			for (int i= 0; i<= width; i+=widthOfPixel) {
				g.drawLine(i, 0, i, height);
			}
			for (int i= 0; i<= height; i+=heightOfPixel) {
				g.drawLine(0, i, width, i);
			}
			
			isPainted = true;
		}
	}
	
	public boolean isNotUpdated() {
		if(!isPainted) {
			return true;
		}
		else {
			isPainted = false;
			return false;
		}
		
	}
}
