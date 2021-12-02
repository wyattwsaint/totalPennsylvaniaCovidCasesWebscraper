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
	static String currentPACovidNumbers;
	static String populationPA;
	static String lastRecord;
	static String dataShift;
	static String percentRecovered;
	static String peopleSickInPA;
	static String percentageSick;

	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {

		getLastRecord();
		createDatabaseAndTables();
		getCurrentDate();
		scrapeData();
		insertCurrentDataIntoTable();
		dataShift();
		insertDataShiftIntoTable();
		System.out.println("New cases since last runtime: " + dataShift);
		numberOfPeopleSickInPA();
		
		WebScraperGui run = new WebScraperGui();

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

		String url;
		Document doc;
		Elements info;

		url = "https://www.health.pa.gov/topics/disease/coronavirus/Pages/Cases.aspx";
		doc = Jsoup.connect(url).get();
		info = doc.getElementsByTag("strong");
		currentPACovidNumbers = (String.valueOf(info)).substring(87, 96).replaceAll(",", "");
		url = "https://www.census.gov/quickfacts/fact/table/PA#";
		doc = Jsoup.connect(url).get();
		info = doc.getElementsByAttribute("data-value");
		populationPA = (String.valueOf(info).substring(51, 59));
		url = "https://www.health.pa.gov/topics/disease/coronavirus/Pages/Cases.aspx";
		doc = Jsoup.connect(url).get();
		info = doc.getElementsByTag("strong");
		percentRecovered = (String.valueOf(info).substring(220, 222));
		

	}

	static void insertDataShiftIntoTable() throws SQLException {

		int insert = Integer.valueOf(dataShift);

		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/webScraper", "root", "root");
		stmt = conn.createStatement();
		sql = "insert into totalcases (case_change, date) VALUES " + "('" + Integer.valueOf(dataShift) + "', '" + date
				+ "')";
		stmt.executeUpdate(sql);
	}

	static void insertCurrentDataIntoTable() throws SQLException {

		conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/webScraper", "root", "root");
		stmt = conn.createStatement();
		sql = "insert into currentCases (Current_Cases) VALUES " + "(" + Integer.valueOf(currentPACovidNumbers) + ")";
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

		int currentPACovidNumbersconvert = Integer.valueOf(currentPACovidNumbers);
		int lastRecordConvert = Integer.valueOf(lastRecord);
		int result = currentPACovidNumbersconvert - lastRecordConvert;
		dataShift = String.valueOf(result);

	}
	
	static void numberOfPeopleSickInPA() {
		
		double percent =  Integer.valueOf(percentRecovered) * .01;
		double percentNotRecovered = 1 - percent;
		double numberOfPeopleNotRecovered = percentNotRecovered * Integer.valueOf(currentPACovidNumbers);
		int numberOfPeopleNotRecovered1 = (int) numberOfPeopleNotRecovered;
		peopleSickInPA = String.valueOf(numberOfPeopleNotRecovered1);
		System.out.println("People currently sick: " + peopleSickInPA);
		
		double percentageSick0 = Integer.valueOf(peopleSickInPA);
		double percentageSick01 = Integer.valueOf(populationPA);
		double percentageSick001 = percentageSick0 / percentageSick01;
		double percentageSick1 = percentageSick001 * 100;
		percentageSick = String.valueOf((int) percentageSick1);
		System.out.println("Percentage of population currently sick: " + percentageSick + "%");
		
		
	}

}
