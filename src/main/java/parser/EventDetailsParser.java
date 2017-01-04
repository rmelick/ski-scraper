package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import types.EventDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Parses the schedule and return links to all of the individual races
 */
public class EventDetailsParser {
  private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

  /**
   *
   * @return The EventDetails of urls for the results pages
   */
  public static List<EventDetails> parse(Document document) {
    return parseResultSection(document);
  }

  private static List<EventDetails> parseResultSection(Document document) {
    Element eventsTable = document.select("table.table-datas").get(0);
    Element headerRow = eventsTable.child(0);

    List<String> headers = getDefaultHeaders();
    Elements headerElements = headerRow.getElementsByTag("th");
    for (int headerIndex = 0; headerIndex < headerElements.size(); headerIndex++) {
      Element header = headerElements.get(headerIndex);
      headers.set(headerIndex, text(header));
    }

    List<EventDetails> parsedEvents = new ArrayList<>();
    Elements events = eventsTable.child(1).children();
    for (Element singleEvent : events) {
      Elements singleEventInfos = singleEvent.children();
      ObjectNode singleEventJson = FACTORY.objectNode();
      for (int column = 0; column < singleEventInfos.size(); column++) {
        String header = headers.get(column);
        if (!header.isEmpty()) {
          String value = text(singleEventInfos.get(column));
          singleEventJson.put(header, value);
        }
      }

      if (singleEventJson.size() > 0) {
        String url = findResultsLink(singleEventInfos);
        EventDetails details = new EventDetails(singleEventJson, url);
        parsedEvents.add(details);
      }
    }

    return parsedEvents;
  }

  /**
   * remove nbsps, etc.
   */
  private static String text(Element element) {
    return element.text().replace((char) 160, ' ').trim();
  }

  private static List<String> getDefaultHeaders() {
    return Arrays.asList(
        "",
        "Date",
        "Place",
        "Nation",
        "Codex",
        "Discipline",
        "Gender",
        "Category",
        "Comments",
        "Download",
        "");
  }

  private static String findResultsLink(Elements element) {
    Elements allLinks = element.select("a");
    for (Element link : allLinks) {
      String url = link.attr("href");
      if (url.contains("raceid") && url.contains("results.html")) {
        return url;
      }
    }
    return null;
  }
}
