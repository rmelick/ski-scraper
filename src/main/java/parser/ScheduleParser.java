package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the schedule and return links to all of the individual races
 */
public class ScheduleParser {
  private static final Pattern EVENT_ID_PATTERN = Pattern.compile("raceid=(?<raceId>.*)&");

  /**
   * @return the list of urls for the EventDetails pages
   */
  public static List<String> parse(Document document) {
    Elements calendar = document.select("table.calendar");
    Elements allLinks = calendar.select("a");
    Set<String> resultUrls = new HashSet<>();
    for (Element link : allLinks) {
      String url = link.attr("href");
      if (url.contains("event_id")) {
        resultUrls.add(url);
      }
    }

    return new ArrayList<>(resultUrls);
//
//    Matcher matcher = EVENT_ID_PATTERN.matcher(calendar.html());
//    Set<String> eventIds = new HashSet<>();
//    while (matcher.find()) {
//      eventIds.add(matcher.group("eventId"));
//    }
//
//    List<String> eventUrls = new ArrayList<>(eventIds.size());
//    for (String eventId : eventIds) {
//      eventUrls.add(String.format("http://data.fis-ski.com/dynamic/results.html?sector=AL&raceid=.*", eventId));
//    }
//
//    return eventUrls;
  }
}
