import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame myFrame = new JFrame("Color Grid");
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLocation(400, 150);
		myFrame.setSize(400, 400);

		MyPanel myPanel = new MyPanel();
		myFrame.add(myPanel);

		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		myFrame.addMouseListener(myMouseAdapter);

		myFrame.setVisible(true);
		myPanel.placeMines();
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				myPanel.mineCheck(i, j);
				if (myPanel.mineCheck(i, j) == 1) {
					System.out.println(i + "," + j);
				}
			}
		}
	}
}