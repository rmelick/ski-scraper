import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import parser.EventDetailsParser;
import parser.ResultParser;
import parser.ScheduleParser;
import types.EventDetails;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
  private static final Pattern EVENT_ID_PATTERN = Pattern.compile("raceid=(?<raceId>.*)");

  private final ObjectWriter _writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

  public static void main(String[] args) {
    try {
      new App().run();
    } catch (Exception e) {
      System.out.println("Error processing" + e);
      e.printStackTrace();
    }
  }

  public void run() throws Exception {
    File outputDirectory = new File("results");
    outputDirectory.mkdir();
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    for (int season = 1967; season <= 2017; season++) {
      final int seasonToExecute = season;
      executorService.submit(() -> {
        try {
          downloadSeason(outputDirectory, seasonToExecute);
        } catch (Throwable t) {
          System.err.println("Error while processing " + seasonToExecute);
          t.printStackTrace();
        }
      });
    }

    executorService.shutdown();
  }

  private void downloadSeason(File outputDirectory, int season) throws IOException, InterruptedException {
    File seasonDirectory = new File(outputDirectory, String.valueOf(season));
    boolean newSeason = seasonDirectory.mkdir();
//    if (!newSeason) {
//      System.out.println("Already downloaded season " + season);
//      return;
//    }

    String seasonSearchUrl = String.format("http://data.fis-ski.com/global-links/all-fis-results.html?"
            + "seasoncode_search=%s&sector_search=AL&category_search=WC&date_from=01&search=Search&limit=100",
        season);

    System.out.println("Downloading season from " + seasonSearchUrl);
    Document schedule = Jsoup.connect(seasonSearchUrl).get();
    List<String> detailPages = ScheduleParser.parse(schedule);
    for (String detailPageUrl : detailPages) {
      System.out.println("Downloading detail page from " + detailPageUrl);
      Document detailPage = Jsoup.connect(detailPageUrl).get();
      List<EventDetails> resultPages = EventDetailsParser.parse(detailPage);

      for (EventDetails eventResult: resultPages) {
        if (eventResult.getResultsUrl() == null) {
          System.out.println("Skipping downloading " + eventResult);
          continue;
        }
        File outputFile = new File(seasonDirectory, String.format("race-%s.json", extractRaceId(eventResult.getResultsUrl())));
        if (outputFile.exists()) {
          System.out.println("Skipping downloading " + outputFile);
          continue;
        }

        System.out.println("Downloading result page from " + eventResult.getResultsUrl());
        Document resultPage = Jsoup.connect(eventResult.getResultsUrl()).get();
        ObjectNode parsedResult = ResultParser.parse(resultPage);
        parsedResult.set("eventInfo", eventResult.getAdditionalInfo());
        _writer.writeValue(outputFile, parsedResult);

        System.out.println("Sleeping 100ms");
        Thread.sleep(100);
      }
    }
  }

  private String extractRaceId(String url) {
    Matcher matcher = EVENT_ID_PATTERN.matcher(url);
    if (matcher.find()) {
      return matcher.group("raceId");
    }
    return null;
  }

}
