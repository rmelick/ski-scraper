package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Parses the schedule and return links to all of the individual races
 */
public class EventDetailsParser {

  /**
   *
   * @return The list of urls for the results pages
   */
  public static List<String> parse(Document document) {
    Elements calendar = document.select("table.calendar");
    Elements allLinks = calendar.select("a");
    Set<String> resultUrls = new HashSet<>();
    for (Element link : allLinks) {
      String url = link.attr("href");
      if (url.contains("raceid")) {
        resultUrls.add(url);
      }
    }

    return new ArrayList<>(resultUrls);
  }
}
