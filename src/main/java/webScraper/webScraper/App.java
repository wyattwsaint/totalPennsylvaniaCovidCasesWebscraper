package webScraper.webScraper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class App {

	public static void main(String[] args) {
		
		try {
			String url = "https://www.health.pa.gov/topics/disease/coronavirus/Pages/Cases.aspx";
			Document doc = Jsoup.connect(url).get();
			Elements info = doc.getElementsByTag("strong");
			
			FileWriter fw = new FileWriter("data");
			fw.write(info.toString());
			fw.close();
			
			String caseCount = Files.readAllLines(Paths.get("data")).get(2);
			
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
