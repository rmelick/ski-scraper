package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parses a single result html file into a json result
 */
public class ResultParser {
  private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

  public static JsonNode parse(Document document) {
    Elements tableSections = document.select("div.bloc-tab");
    Element raceInfoSection = tableSections.get(0);
    Element resultsSection = tableSections.get(1);


    ObjectNode resultsJson = FACTORY.objectNode();
    resultsJson.set("result", parseResultSection(resultsSection));

    return resultsJson;
  }

  /**
   * Not yet ready
   */
  private static void parseRaceInfoSection(Element raceInfoSection) {
    ObjectNode raceInfo = FACTORY.objectNode();
    for (Element dataTable : raceInfoSection.select("table.table-datas")) {
      Element header = dataTable.getElementsByTag("thead").get(0);
      String headerName = text(header.getElementsByTag("th").get(0));
      Element body = dataTable.getElementsByTag("tbody").get(0);
    }
  }

  private static JsonNode parseResultSection(Element resultsSection) {
    //List<RacerResult> results = new ArrayList<RacerResult>();
    Element resultTable = resultsSection.select("table.table-datas").get(0);
    Element headerRow = resultTable.child(0);

    List<String> headers = getDefaultHeaders();
    Elements headerElements = headerRow.getElementsByTag("th");
    for (int headerIndex = 0; headerIndex < headerElements.size(); headerIndex++) {
      Element header = headerElements.get(headerIndex);
      headers.set(headerIndex, text(header));
    }

    ArrayNode resultsJson = FACTORY.arrayNode();
    Element results = resultTable.child(1);
    for (Element singleResult : results.children()) {
      Elements singleResultInfos = singleResult.children();
      ObjectNode singleResultJson = FACTORY.objectNode();
      for (int column = 0; column < singleResultInfos.size(); column++) {
        String header = headers.get(column);
        String value = text(singleResultInfos.get(column));
        singleResultJson.put(header, value);
      }
      resultsJson.add(singleResultJson);
    }

    return resultsJson;
  }

  /**
   * remove nbsps, etc.
   */
  private static String text(Element element) {
    return element.text().replace((char) 160, ' ').trim();
  }

  private static List<String> getDefaultHeaders() {
    return Arrays.asList(
        "Rank",
        "Bib",
        "FIS Code",
        "Name",
        "Year",
        "Nation",
        "Run 1",
        "Run 2",
        "Total Time",
        "Diff.",
        "FIS Points",
        "WC Points");
  }
}
