package gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PathPanel extends JPanel{
	private JTextArea path;
	public PathPanel() {
		
		// Set layout
		setLayout(new BorderLayout());
		
		// Set border
		TitledBorder titledBorder = BorderFactory.createTitledBorder("Chosen Path");
		titledBorder.setTitleColor(Color.WHITE);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,10), titledBorder));
		
		// Create components
		path = new JTextArea();
		path.setFont(new Font("Consolas",Font.PLAIN, 12));
		JScrollPane pathScroll = new JScrollPane(path);
		
		// Set background and style
		setBackground(Color.DARK_GRAY);
		path.setMargin(new Insets(5, 5, 5, 5));
		
		// Disable typing
		path.setEditable(false);
		
		// Set minimum size
		setPreferredSize(new Dimension(280,-1));
		
		// Add component
		add(pathScroll, BorderLayout.CENTER);
	}
	
	/**
	 * Set path text
	 * @param str
	 */
	public void setPahtText(String str){
		path.setText(str);
	}
}
