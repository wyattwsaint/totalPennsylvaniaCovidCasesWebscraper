package webScraper.webScraper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class App {

	static Connection conn = null;
	static Statement stmt = null;
	static String sql;
	static String date;
	static String currentNumbers;
	static String lastRecord;
	static String dataShift;

	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {

		
		getLastRecord();
		createDatabaseAndTables();
		getCurrentDate();
		scrapeData();
		insertCurrentDataIntoTable();
		dataShift();
		insertDataShiftIntoTable();
		System.out.println(dataShift);
		
	}

	static void createDatabaseAndTables() throws ClassNotFoundException, SQLException {

		Class.forName("org.mariadb.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/", "root", "root");
		stmt = conn.createStatement();
		sql = "create database if not exists webScraper";
		stmt.executeUpdate(sql);
		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/webScraper", "root", "root");
		stmt = conn.createStatement();
		sql = "create table if not exists totalCases (case_change int)";
		stmt.executeUpdate(sql);
		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/webScraper", "root", "root");
		stmt = conn.createStatement();
		sql = "create or replace table currentCases (Current_Cases int)";
		stmt.executeUpdate(sql);

	}

	static void getCurrentDate() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		date = dtf.format(now);

	}

	static void scrapeData() throws IOException {

		String url = "https://www.health.pa.gov/topics/disease/coronavirus/Pages/Cases.aspx";
		Document doc = Jsoup.connect(url).get();
		Elements info = doc.getElementsByTag("strong");
		currentNumbers = (String.valueOf(info)).substring(87, 96).replaceAll(",", "");

	}

	static void insertDataShiftIntoTable() throws SQLException {
		
		int insert = Integer.valueOf(dataShift);

		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/webScraper", "root", "root");
		stmt = conn.createStatement();
		sql = "insert into totalcases (case_change, date) VALUES " + "('" + Integer.valueOf(dataShift) + "', '" + date + "')";
		stmt.executeUpdate(sql);
	}
	
	static void insertCurrentDataIntoTable() throws SQLException {
		
		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/webScraper", "root", "root");
		stmt = conn.createStatement();
		sql = "insert into currentCases (Current_Cases) VALUES " + "(" + Integer.valueOf(currentNumbers) + ")";
		stmt.executeUpdate(sql);

	}

	static void getLastRecord() throws ClassNotFoundException, SQLException {

		Class.forName("org.mariadb.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/webScraper", "root", "root");
		stmt = conn.createStatement();
		ResultSet rs;
		sql = "select * from currentCases";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int cases = rs.getInt("Current_Cases");
			lastRecord = String.valueOf(cases);
		}

	}
	
	static void dataShift() {
		
		int currentNumbersconvert = Integer.valueOf(currentNumbers);
		int lastRecordConvert = Integer.valueOf(lastRecord);
		int result = currentNumbersconvert - lastRecordConvert;
		dataShift = String.valueOf(result);
		
	}

}
