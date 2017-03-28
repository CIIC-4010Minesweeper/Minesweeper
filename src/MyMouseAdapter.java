import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
	public static int f = 1;
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			Component c1 = e.getComponent();
			while (!(c1 instanceof JFrame)) {
				c = c1.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame1 = (JFrame)c1;
			MyPanel myPanel1 = (MyPanel) myFrame1.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets1 = myFrame1.getInsets();
			int x2 = myInsets1.left;
			int y2 = myInsets1.top;
			e.translatePoint(-x2, -y2);
			int x3 = e.getX();
			int y3 = e.getY();
			myPanel1.x = x3;
			myPanel1.y = y3;
			myPanel1.mouseDownGridX = myPanel1.getGridX(x3, y3);
			myPanel1.mouseDownGridY = myPanel1.getGridY(x3, y3);
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Do nothing
				}else if (gridX == 0 && gridY == 9) {
						myPanel.reset();
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed
						if (!(myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
							if (!(myPanel.colorArray[gridX][gridY] == Color.RED)) {
	 							if (myPanel.bombCheck(gridX, gridY) == 0) {
	 								myPanel.checkAround(gridX, gridY);
	 							}
	 							else{
	 								myPanel.check(gridX, gridY);
	 								System.out.println("BOOM! You've lost... Try again!");
	 								myPanel.repaint();
	 							}
				    		}
						}
					}
				}
			}
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			Component c1 = e.getComponent();
			while (!(c1 instanceof JFrame)) {
				c = c1.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame1 = (JFrame)c1;
			MyPanel myPanel1 = (MyPanel) myFrame1.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets1 = myFrame1.getInsets();
			int x2 = myInsets1.left;
			int y2 = myInsets1.top;
			e.translatePoint(-x2, -y2);
			int x3 = e.getX();
			int y3 = e.getY();
			myPanel1.x = x3;
			myPanel1.y = y3;
			int gridX1 = myPanel1.getGridX(x3, y3);
			int gridY1 = myPanel1.getGridY(x3, y3);
			int flags = 10;
			if ((myPanel1.mouseDownGridX == -1) || (myPanel1.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX1 == -1) || (gridY1 == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel1.mouseDownGridX != gridX1) || (myPanel1.mouseDownGridY != gridY1)) {
					}else{
						if (!(myPanel1.colorArray[gridX1][gridY1] == Color.LIGHT_GRAY)) {
							if (myPanel1.checkflag(gridX1, gridY1) == 0) {
								if (!(f > flags)) {
									if (myPanel1.bombCheck(gridX1, gridY1) == 1) {
										MyPanel.flagged ++;
									}
									myPanel1.colorArray[gridX1][gridY1] = Color.RED ;
									myPanel1.repaint();
									f ++;
								}
							}
							else {
								myPanel1.colorArray[gridX1][gridY1] = Color.WHITE ;
								myPanel1.repaint();
								f --;
							}
						}
					}
				}
			}
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
}