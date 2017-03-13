package net.recommenders.scraper.scraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by saia on 2016-10-19.
 */
public class ProfileScraper extends AbstractScraper{

    private final static Logger logger = LogManager.getLogger(ProfileScraper.class);

    public void readRunnerFiles(int year, int id) {
        boolean parse = false;
        if (id == 0)
            parse = true;
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(year + "_results.tsv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int resultsCounter = 0;
        int profile = 0;
        StringBuffer results = new StringBuffer();

        String baseurl = "http://goteborgsvarvet.r.mikatiming.de/2016/?content=detail&";
        if(id == 0) {
            StringBuffer columns = new StringBuffer("place\tplaceInClass\tbibNo\tnationality\tname\tid\teventid\tclass\tclub\ttime\t5kTOD\t5ktime\t5kdiff\t5kminperk\t5kkmh\t5kplace\t10kTOD\t10ktime\t10kdiff\t10kminperk\t10kkmh\t10kplace\t15kTOD\t15ktime\t15kdiff\t15kminperk\t15kkmh\t15kplace\t20kTOD\t20ktime\t20kdiff\t20kminperk\t20kkmh\t20kplace\tfinishTOD\tfinishtime\tfinishdiff\tfinishminperk\tfinishkmh\tfinishplace\n");
            writeResults(year, columns);
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();

            String[] lineArray = line.split("\t");
            int number = Integer.parseInt(lineArray[2]);
            if(number == id) {
                parse = true;
                continue;
            }
            if (!parse)
                continue;
            String url = baseurl + "idp="+lineArray[5] + "&event=" + lineArray[6];
            String runningTimes = parseProfile(url, year);
            results.append(line + "\t" + runningTimes + "\n");
            profile++;
            resultsCounter++;
            if (resultsCounter == 100) {
                writeResults(year, results);
                results.setLength(0);
                resultsCounter = 0;
                System.out.print("\rScraped profile: " + profile);
                System.out.flush();
            }

        }
        if(results.length() != 0){
            writeResults(year, results);
            System.out.println("Scraped all profiles");
        }
    }

    private String parseProfile(String url, int year){
        Document doc = getDocument(url);
        String times = "";
        String km5Tag = ".f-time_01";
        times += parseTimeTable(doc, km5Tag);
        String km10Tag = ".f-time_02";
        times += parseTimeTable(doc, km10Tag);
        if (year < 2014) {
            String km15Tag = ".f-time_03";
            times += parseTimeTable(doc, km15Tag);
            String km20Tag = ".f-time_04";
            times += parseTimeTable(doc, km20Tag);
        } else {
            String km15Tag = ".f-time_04";
            times += parseTimeTable(doc, km15Tag);
            String km20Tag = ".f-time_05";
            times += parseTimeTable(doc, km20Tag);
        }
        String finishTag = ".f-time_finish_netto";

        times += parseTimeTable(doc, finishTag);
        return times;
    }

    private String parseTimeTable(Document doc, String kmTag) {
        Element kmRow = doc.select(kmTag).first();
        String times = "";
        if(kmRow.children().size() != 7 && doc.select(kmTag).size() > 1)
            kmRow = doc.select(kmTag).get(2);
        String timeOfDay = kmRow.getElementsByClass("time_day").text();
        String time = kmRow.getElementsByClass("time").text();
        String diff = kmRow.getElementsByClass("diff").text();
        String minPerKm = kmRow.getElementsByClass("min_km").text();
        String kmPerH = kmRow.getElementsByClass("kmh").text();
        String place = kmRow.getElementsByClass("place").text();

        times += timeOfDay + "\t";
        times += time + "\t";
        times += diff + "\t";
        times += minPerKm + "\t";
        times += kmPerH + "\t";
        times += place;
        if (!kmTag.contains("finish"))
            times += "\t";
        return times;
    }

    public boolean writeResults(int year, StringBuffer results){
        String fileName = year + "_complete.tsv";
        return writeData(results, fileName);
    }
}
