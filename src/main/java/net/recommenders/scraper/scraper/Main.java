package net.recommenders.scraper.scraper;

import com.github.jankroken.commandline.CommandLineParser;
import com.github.jankroken.commandline.OptionStyle;

import java.lang.reflect.InvocationTargetException;

public class Main extends AbstractScraper{

    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("No arguments given");
            System.out.println("needs at least argument on the form: -Dexec.args=\"-y 2014\"");
            System.out.println("available arguments: -y -i");
            System.exit(0);
        }
        int id = 0;
        int year = 0;
        boolean resultlist = false;
        try {
            Arguments arguments = CommandLineParser.parse(Arguments.class, args, OptionStyle.SIMPLE);
            id = arguments.getId();
            year = arguments.getYear();
            resultlist = arguments.getResultlist();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if(resultlist){
            System.out.println("Scraping result list for year: " + year);
            ResultListScraper scraper = new ResultListScraper();
            scraper.collectRunners(year);
        }
        else {
            System.out.println("Scraping profiles for year: " + year);
            ProfileScraper profileScraper = new ProfileScraper();
            profileScraper.readRunnerFiles(year, id);
        }
    }

    public static void runAll(){
        for (int year : eventIDs.keySet())  {
            ResultListScraper scraper = new ResultListScraper();
            scraper.collectRunners(year);
            ProfileScraper profileScraper = new ProfileScraper();
            profileScraper.readRunnerFiles(year, 0);
        }
    }

}
