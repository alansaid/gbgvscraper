package net.recommenders.scraper.scraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;


public class ResultListScraper extends AbstractScraper{

    private final static Logger logger = LogManager.getLogger(ResultListScraper.class);


    public boolean collectRunners(int year) {
        int resultsCounter = 0;
        int pages = eventPages.get(year);

        System.out.println("Starting result scraping");
        StringBuffer results = new StringBuffer();
        for (int page = 1; page <= pages; page++) {
            String baseURL = buildURL(year, page);
            Document doc = getDocument(baseURL);

            Element table = doc.select(".list-table").first();
            Elements body = table.select("tbody");
            resultsCounter = parseRow(resultsCounter, results, body);

            if (resultsCounter > 100){
                writeResults(year, results);
                results.setLength(0);
                resultsCounter = 0;
                System.out.print("\rScraped page: " + page);
                System.out.flush();
            }
            if(page == pages && results.length() != 0){
                writeResults(year, results);
                System.out.println("Scraped all pages");
            }
        }
        return true;
    }

    private int parseRow(int resultsCounter, StringBuffer results, Elements body){
        Iterator<Element> rows = body.select("tr").iterator();
        while (rows.hasNext()) {
            Element row = rows.next();
            if(!row.child(placeTotalIdx).text().matches("\\d+")) {
                logger.info("skipping :" + row.child(placeTotalIdx).text());
            }
            int placeTotal = -1;
            try{
                placeTotal = Integer.parseInt(row.child(placeTotalIdx).text());
            } catch (NumberFormatException nfe){
                logger.info("Could not parse placeTotal: " + row.child(placeTotalIdx).text());
            }
            int placeAge = -1;
            try{
                placeAge = Integer.parseInt((row.child(placeAgeIdx).text().length() > 0 ? row.child(placeAgeIdx).text() : "0"));
            } catch (NumberFormatException nfe){
                logger.info("Could not parse placeAge: " + row.child(placeAgeIdx).text());
            }
            int bibNo = -1;
            try {
                bibNo = Integer.parseInt(row.child(bibNoIdx).text());
            } catch (NumberFormatException nfe){
                logger.info("Could not parse bib no");
            }
            String nationality = "";
            if (!row.child(nationalityIdx).select("img").isEmpty())
                nationality = row.child(nationalityIdx).select("img").first().attr("alt");
            else
                nationality = row.child(nationalityIdx).text();
            String name = row.child(nameIdx).text().substring(2);
            //http://goteborgsvarvet.r.mikatiming.de/2016/?content=detail&idp=9999991A136FC5000001E554&event=GV_9999991A136FC50000000071
//          http://goteborgsvarvet.r.mikatiming.de/2016/?content=detail&fpid=search&pid=search&idp=9999991A136FC500000947C6&lang=SE&event=GV_9999991A136FC50000000070&event_main_group=2012&num_results=250&search%5Bage_class%5D=%25&search%5Bsex%5D=%25&search%5Bnation%5D=%25&search_sort=place_all&search_event=GV_9999991A136FC50000000070
            String urlPart = row.child(nameIdx).select("a").first().attr("href");
            String id = urlPart.substring(urlPart.indexOf("idp=")+4, urlPart.indexOf("&lang"));
            String event = urlPart.substring(urlPart.indexOf("event=")+6, urlPart.indexOf("&event_main"));
            String ageGroup = row.child(ageGroupIdx).text();
            String club = row.child(clubIdx).text();
            String time = row.child(timeIdx).text();
            resultsCounter++;
            String resultLine = placeTotal + "\t" + placeAge + "\t" + bibNo + "\t" + nationality + "\t" + name + "\t" + id + "\t" + event + "\t" + ageGroup + "\t" + club + "\t" + time + "\n";
            results.append(resultLine);
        }
        return resultsCounter;
    }


    public boolean writeResults(int year, StringBuffer results){
        String fileName = year + "_results.tsv";
        return writeData(results, fileName);
    }

    private String buildURL(int year, int page){
        String urlPart1 = "http://goteborgsvarvet.r.mikatiming.de/2016/?page=" + page;
        String urlPart2 =  "&event=" + eventIDs.get(year);
        String urlPart3 = "&event_main_group="+year+"&num_results=250&pid=search&search%5Bage_class%5D=%25&search%5Bsex%5D=%25&search%5Bnation%5D=%25&search_sort=place_all";
        String url = urlPart1 + urlPart2 + urlPart3;
        return url;
    }
}
