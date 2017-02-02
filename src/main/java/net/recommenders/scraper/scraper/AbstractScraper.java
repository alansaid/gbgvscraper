package net.recommenders.scraper.scraper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * Created by saia on 2016-10-19.
 */
public class AbstractScraper {

    public static final List<String> USER_AGENTS = new ArrayList<String>() {
        {
            add("Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14");
            add("Mozilla/5.0 (Windows NT 6.0; rv:2.0) Gecko/20100101 Firefox/4.0 Opera 12.14");
            add("Opera/12.80 (Windows NT 5.1; U; en) Presto/2.10.289 Version/12.02");
            add("Opera/9.80 (Windows NT 5.1; U; zh-sg) Presto/2.9.181 Version/12.00");
            add("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; de) Presto/2.9.168 Version/11.52");
            add("Opera/9.80 (X11; Linux x86_64; U; Ubuntu/10.10 (maverick); pl) Presto/2.7.62 Version/11.01");
            add("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");
            add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1664.3 Safari/537.36");
            add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1623.0 Safari/537.36");
            add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.17 Safari/537.36");
            add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1464.0 Safari/537.36");
            add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1468.0 Safari/537.36");
            add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0");
            add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:25.0) Gecko/20100101 Firefox/25.0");
            add("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:24.0) Gecko/20100101 Firefox/24.0");
            add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:24.0) Gecko/20100101 Firefox/24.0");
            add("Mozilla/5.0 (Windows NT 6.2; rv:22.0) Gecko/20130405 Firefox/23.0");
            add("Mozilla/5.0 (Windows NT 6.1; rv:6.0) Gecko/20100101 Firefox/19.0");
            add("Mozilla/5.0 (Windows NT 6.1; rv:14.0) Gecko/20100101 Firefox/18.0.1");
            add("Mozilla/5.0 (X11; Linux x86_64; rv:17.0) Gecko/20121202 Firefox/17.0 Iceweasel/17.0.1");
            add("Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0");
            add("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
            add("Mozilla/5.0 (Windows; U; MSIE 9.0; WIndows NT 9.0; en-US))");
            add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; Zune 4.0; InfoPath.3; MS-RTC LM 8; .NET4.0C; .NET4.0E)");
            add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; chromeframe/12.0.742.112)");
            add("Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25");
            add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/537.13+ (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2");
        }
    };

    private final static Logger logger = LogManager.getLogger(AbstractScraper.class);

    public static final Map<Integer, Integer> eventPages = new HashMap<Integer, Integer>(){{
        put(1980, 1);
        put(1981, 1);
        put(1982, 2);
        put(1983, 3);
        put(1984, 4);
        put(1985, 6);
        put(1986, 7);
        put(1987, 10);
        put(1988, 12);
        put(1989, 14);
        put(1990, 16);
        put(1991, 18);
        put(1992, 22);
        put(1993, 25);
        put(1994, 28);
        put(1995, 31);
        put(1996, 35);
        put(1997, 43);
        put(1998, 48);
        put(1999, 54);
        put(2000, 65);
        put(2001, 78);
        put(2002, 80);
        put(2003, 84);
        put(2004, 92);
        put(2005, 94);
        put(2006, 107);
        put(2007, 124);
        put(2008, 137);
        put(2009, 163);
        put(2010, 154);
        put(2011, 172);
        put(2012, 177);
        put(2013, 181);
        put(2014, 190);
        put(2015, 186);
        put(2016, 181);
    }}  ;

    public static final Map<Integer, String> eventIDs = new HashMap<Integer, String>(){{
        put(1980, "GV_0000171A136FC50000000015");
        put(1981, "GV_9999991A136FC50000000016");
        put(1982, "GV_9999991A136FC50000000017");
        put(1983, "GV_9999991A136FC50000000029");
        put(1984, "GV_9999991A136FC5000000002A");
        put(1985, "GV_9999991A136FC5000000002B");
        put(1986, "GV_9999991A136FC5000000002C");
        put(1987, "GV_9999991A136FC5000000002D");
        put(1988, "GV_9999991A136FC5000000003D");
        put(1989, "GV_9999991A136FC5000000003E");
        put(1990, "GV_9999991A136FC50000000051");
        put(1991, "GV_9999991A136FC50000000052");
        put(1992, "GV_9999991A136FC50000000053");
        put(1993, "GV_9999991A136FC50000000054");
        put(1994, "GV_9999991A136FC50000000055");
        put(1995, "GV_9999991A136FC50000000056");
        put(1996, "GV_9999991A136FC50000000057");
        put(1997, "GV_9999991A136FC50000000058");
        put(1998, "GV_9999991A136FC50000000059");
        put(1999, "GV_9999991A136FC5000000005A");
        put(2000, "GV_9999991A136FC5000000005B");
        put(2001, "GV_9999991A136FC50000000065");
        put(2002, "GV_9999991A136FC50000000066");
        put(2003, "GV_9999991A136FC50000000067");
        put(2004, "GV_9999991A136FC50000000068");
        put(2005, "GV_9999991A136FC50000000069");
        put(2006, "GV_9999991A136FC5000000006A");
        put(2007, "GV_9999991A136FC5000000006B");
        put(2008, "GV_9999991A136FC5000000006C");
        put(2009, "GV_9999991A136FC5000000006D");
        put(2010, "GV_9999991A136FC5000000006E");
        put(2011, "GV_9999991A136FC5000000006F");
        put(2012, "GV_9999991A136FC50000000070");
        put(2013, "GV_9999991A136FC50000000071");
        put(2014, "GV_9999991A136FC500000000A1");
        put(2015, "GV_0000171A136FC50000000001");
        put(2016, "GV_9999991A136FC7000000034A");
    }};

    public static final int placeTotalIdx = 0;
    public static final int placeAgeIdx = 1;
    public static final int bibNoIdx = 2;
    public static final int nationalityIdx = 3;
    public static final int nameIdx = 4;
    public static final int ageGroupIdx = 5;
    public static final int clubIdx = 6;
    public static final int timeIdx = 7;

    public static final String[] labels = {"place", "placeInClass", "startingNo", "nationality", "name", "url", "class", "club", "totalTime",
            "timeOfDay5km" , "time5km" , "diff5km" , "minutesPerKm5km" , "kmPerH5km" , "place5km",
            "timeOfDay10km", "time10km", "diff10km", "minutesPerKm10km", "kmPerH10km", "place10km",
            "timeOfDay15km", "time15km", "diff15km", "minutesPerKm15km", "kmPerH15km", "place15km",
            "timeOfDay20km", "time20km", "diff20km", "minutesPerKm20km", "kmPerH20km", "place20km",
            "timeOfDay21km", "time21km", "diff21km", "minutesPerKm21km", "kmPerH21km", "place21km"};

    /**
     * times += timeOfDay + "\t";
     times += time + "\t";
     times += diff + "\t";
     times += minPerKm + "\t";
     times += kmPerH + "\t";
     times += place + "\t";
     * @param baseURL
     * @return
     */
    public Document getDocument(String baseURL) {
        Document doc = null;
        try {
            doc = Jsoup.connect(baseURL).userAgent(USER_AGENTS.get((int) Math.random() * USER_AGENTS.size())).timeout(10000000).get();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return doc;
    }


    public boolean writeData(StringBuffer input, String filename) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
            out.write(input.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}