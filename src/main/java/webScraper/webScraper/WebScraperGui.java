package webScraper.webScraper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class WebScraperGui extends JFrame implements ActionListener {
	
	static JFrame myFrame;
	static JPanel myPanel;
	static JTable myTable;

	WebScraperGui() {
		
		myFrame = new JFrame();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		myFrame.setTitle("COVID Web Scraper");
		myFrame.setPreferredSize(new Dimension(1100, 690));
		myFrame.setResizable(false);
		
		myPanel = new JPanel();
		myPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		myPanel.setPreferredSize(new Dimension(1000, 650));
		myPanel.setBackground(Color.GRAY);
		
		createJTable();
			
		
		myPanel.add(myTable);
		myFrame.add(myPanel);
		
		myFrame.pack();
		myFrame.setLocationRelativeTo(null);
		myFrame.setVisible(true);
		
		
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	
	static void createJTable() {
		
		String [][] data = {

				{"New cases since last runtime:", App.dataShift},
				{"People currently sick:", App.peopleSickInPA},
				{"Percent of population currently sick:", App.percentageSick}
		};
		String [] header = {"Description", "Data"};
		DefaultTableModel model = new DefaultTableModel(data, header);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		myTable = new JTable(model);
		myTable.setDefaultRenderer(Object.class, centerRenderer);
		myTable.setPreferredSize(new Dimension(1000, 600));
		myTable.setFont(new Font("Arial", Font.PLAIN, 40));
		myTable.setRowHeight(myTable.getRowHeight() + 50);
		
		
		
	}

}
