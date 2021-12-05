package webScraper.webScraper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

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
		myFrame.getContentPane().setBackground(Color.lightGray);

		myPanel = new JPanel();
		myPanel.setLayout(new GridBagLayout());
		myPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		myPanel.setPreferredSize(new Dimension(1000, 600));
		myPanel.setBackground(Color.lightGray);

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

		String[][] data = {

				{ "New PA cases since last runtime:", App.dataShift }, { "People currently sick in PA:", App.peopleSickInPA },
				{ "Percent of PA population currently sick:", App.percentageSick+"%" } };
		String[] header = { "Description", "Data" };
		DefaultTableModel model = new DefaultTableModel(data, header);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		myTable = new JTable(model);
		myTable.setDefaultRenderer(Object.class, centerRenderer);
		myTable.setPreferredSize(new Dimension(1000, 197));
		myTable.setFont(new Font("Arial", Font.PLAIN, 40));
		myTable.setRowHeight(myTable.getRowHeight() + 50);
		TableColumnModel columnModel = myTable.getColumnModel();
		for (int column = 0; column < myTable.getColumnCount(); column++) {
			int width = 15; // Min width
			for (int row = 0; row < myTable.getRowCount(); row++) {
				TableCellRenderer renderer = myTable.getCellRenderer(row, column);
				Component comp = myTable.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			columnModel.getColumn(column).setPreferredWidth(width);

		}
	}
}
