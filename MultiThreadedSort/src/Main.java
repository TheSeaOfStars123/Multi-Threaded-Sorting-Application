
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.bohush.sorting.*;

//Visualization and Comparison of Sorting Algorithms
public class Main extends JApplet {

	private static final long serialVersionUID = 1L;
	private SortPanel[] sortPanels = new SortPanel[4];
	private static int size = 100;//number of colors to be sorted
	private int sleepTime = 2;
	private String animationName = "";

	public Main() {
		setLayout(new GridLayout(1, 1));
		SortPanelsHolder sortPanelHolder = new SortPanelsHolder();
		sortPanelHolder.setLayout(new  GridLayout(0, 1));
		sortPanelHolder.setBackground(Color.BLACK);
		sortPanelHolder.setForeground(Color.BLACK);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width / 2;
		int height = screenSize.height / 5;

		sortPanels[0] = new InsertionSortPanel(" Insertion Sort ", sleepTime, width, height);
		sortPanels[1] = new MergeSortPanel(" Merge Sort ", sleepTime, width, height);
		sortPanels[2] = new QuickSortPanel(" Quick Sort ", sleepTime, width, height);
		sortPanels[3] = new BubbleSortPanel(" Bubble Sort ", sleepTime, width, height);
		
		
		for (int i = 0; i < sortPanels.length; i++) {
			sortPanels[i].setVisible(false);
			sortPanelHolder.add(sortPanels[i]);				
		}
		add(sortPanelHolder);
	}
	
	class SortPanelsHolder extends JPanel {
		private static final long serialVersionUID = 1L;
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Font animationNameFont = new Font(Font.MONOSPACED, Font.BOLD, 80);
			FontMetrics animationNameFontFontMetrix = getFontMetrics(animationNameFont);
			g.setFont(animationNameFont);
			int x = (getWidth() - animationNameFontFontMetrix.stringWidth(animationName)) / 2;
			int y = (getHeight() - animationNameFontFontMetrix.getLeading()) / 2;

			float startH = (float)Math.random() / 2;
			float finishH = 0.5f + (float)Math.random() / 2;
			float S = 1; // Saturation
			float B = 1; // Brightness
			Color startColor = Color.getHSBColor(startH, S, B);
			Color finishColor = Color.getHSBColor(finishH, S, B);
			GradientPaint gradientPaint = new GradientPaint(x, 0, startColor,
                                x + animationNameFontFontMetrix.stringWidth(animationName), 0,finishColor);
			((Graphics2D)g).setPaint(gradientPaint);
			g.drawString(animationName, x, y);
		}
	}
        

	public void beginAnimation(String animationName, int[] list) {
		try {
			
			this.animationName = animationName;
			repaint();
			Thread.sleep(3000);
			this.animationName = "";
			repaint();
			for (int i = 0; i < sortPanels.length; i++) {
				sortPanels[i].setList(list);
				sortPanels[i].setVisible(true);
			}
			Thread.sleep(1000);
			ExecutorService executor = Executors.newFixedThreadPool(sortPanels.length);
			for (int i = 0; i < sortPanels.length; i++) {
				executor.execute(sortPanels[i]);
			}
			executor.shutdown();
			while(!executor.isTerminated()) {
				Thread.sleep(100);
			}
			Thread.sleep(1000);
			for (int i = 0; i < sortPanels.length; i++) {
				sortPanels[i].setVisible(false);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
            
           JFrame frame = new JFrame("Sorting Algorithm Animations");
		Main main = new Main();
		frame.add(main);
//		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		int[] list = new int[size];
		//initializing the list
		for (int i = 0; i < list.length; i++) {
			list[i] = i + 1;
		}
                //randomizing the list
		for (int i = 0; i < list.length; i++) {
			int index = (int) (Math.random() * list.length);
			int temp = list[i];
			list[i] = list[index];
			list[index] = temp;
		}
                
                
                main.beginAnimation("Random", list);
		
		
		for (int i = 0; i < list.length; i++) {
			list[i] = (1 + i / (size / 4) ) * (size / 4);
		}
		for (int i = 0; i < list.length; i++) {
			int index = (int) (Math.random() * list.length);
			int temp = list[i];
			list[i] = list[index];
			list[index] = temp;
		}
		main.beginAnimation("Few Unique", list);

		
		for (int i = 0; i < list.length; i++) {
			list[i] = size - i;
		}
		main.beginAnimation("Reversed", list);
		
		
		for (int i = 0; i < list.length / 2; i++) {
			list[i] = i + 1;
		}
		for (int i = list.length / 2; i < list.length; i++) {
			list[i] = i + 2;
		}
		list[list.length - 1] = list.length / 2 + 1;
		main.beginAnimation("Almost Sorted", list);
	}
}