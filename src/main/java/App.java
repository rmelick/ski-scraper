import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import parser.EventDetailsParser;
import parser.ResultParser;
import parser.ScheduleParser;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
  private static final Pattern EVENT_ID_PATTERN = Pattern.compile("raceid=(?<raceId>.*)");

  private final ObjectWriter _writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

  public static void main(String[] args) {
    try {
      new App().run();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void run() throws Exception {
    String seasonSearchUrl = "http://data.fis-ski.com/global-links/all-fis-results.html?place_search=&seasoncode_search=2017&sector_search=AL&date_search=&gender_search=&category_search=WC&codex_search=&nation_search=&disciplinecode_search=&date_from=01&search=Search&limit=100";
    File outputDirectory = new File("downloads");
    outputDirectory.mkdir();

    System.out.println("Downloading season from " + seasonSearchUrl);
    Document schedule = Jsoup.connect(seasonSearchUrl).get();
    List<String> detailPages = ScheduleParser.parse(schedule);
    for (String detailPageUrl : detailPages) {
      System.out.println("Downloading detail page from " + detailPageUrl);
      Document detailPage = Jsoup.connect(detailPageUrl).get();
      List<String> resultPages = EventDetailsParser.parse(detailPage);

      for (String resultUrl : resultPages) {
        File outputFile = new File(outputDirectory, String.format("race-%s.json", extractRaceId(resultUrl)));
        if (outputFile.exists()) {
          System.out.println("Skipping downloading " + outputFile);
          continue;
        }

        System.out.println("Downloading result page from " + resultUrl);
        Document resultPage = Jsoup.connect(resultUrl).get();
        JsonNode parsedResult = ResultParser.parse(resultPage);
        System.out.println(_writer.writeValueAsString(parsedResult));

        _writer.writeValue(outputFile, parsedResult);

        System.out.println("Sleeping 500ms");
        Thread.sleep(500);
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
