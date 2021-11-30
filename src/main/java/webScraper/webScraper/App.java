package webScraper.webScraper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class App {

	public static void main(String[] args) {
		
		try {
			createDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static void createDatabase() throws ClassNotFoundException, SQLException {
		
		
		Connection conn = null;
        Statement stmt = null;
            //STEP 2: Register JDBC driver
        	Class.forName("org.mariadb.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/", "root", "root");
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = "create database if not exists webScraper";
            stmt.executeUpdate(sql);
		
	}
	
	static void webScrape() {
		
		try {
			String url = "https://www.health.pa.gov/topics/disease/coronavirus/Pages/Cases.aspx";
			Document doc = Jsoup.connect(url).get();
			Elements info = doc.getElementsByTag("strong");
			
			
			
			FileWriter fw = new FileWriter("data");
			fw.write(info.toString());
			fw.close();
			
			String caseCount = Files.readAllLines(Paths.get("data")).get(3);
			
			String caseCount1 = caseCount.replaceFirst("<strong>", "");
			String caseCount2 = caseCount1.substring(0, caseCount1.length()-9);
			String caseCount3 = caseCount2.replaceAll(",", "");
			
			int actualCaseCount = Integer.parseInt(caseCount3.toString());

			
			String oldCaseCount = Files.readAllLines(Paths.get("caseCountFile")).get(0);
			int newOldCaseCount = Integer.valueOf(oldCaseCount.toString());
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("caseCountFile"));
			bw.write(caseCount3);
			bw.flush();
			
			int caseTrend = actualCaseCount - newOldCaseCount;
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			String dateTime = dtf.format(now);
			
			BufferedWriter caseTrends = new BufferedWriter(new FileWriter("caseTrendFile", true));
			caseTrends.newLine();
			caseTrends.write(new Integer(caseTrend).toString());
			caseTrends.newLine();
			caseTrends.write(dateTime);
			caseTrends.newLine();
			caseTrends.write("------");
			caseTrends.flush();
			caseTrends.close();
			
			System.out.println(caseTrend);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
