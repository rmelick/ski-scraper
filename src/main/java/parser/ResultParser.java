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

  public static ObjectNode parse(Document document) {
    Elements tableSections = document.select("div.bloc-tab");
    Element raceInfoSection = tableSections.get(0);
    Element resultsSection = tableSections.get(1);


    ObjectNode resultsJson = FACTORY.objectNode();
    resultsJson.set("raceInfo", parseRaceInfoSection(raceInfoSection));
    resultsJson.set("results", parseResultSection(resultsSection));

    return resultsJson;
  }

  private static JsonNode parseRaceInfoSection(Element raceInfoSection) {
    ObjectNode raceInfo = FACTORY.objectNode();
    for (Element dataTable : raceInfoSection.select("table.table-datas")) {
      Element tableName = dataTable.child(0);
      ObjectNode tableValues = FACTORY.objectNode();

      Elements data = dataTable.child(1).select("tr");
      for (Element dataRow : data) {
        Elements children = dataRow.children();
        if (children.size() == 3) {
          ObjectNode personNode = FACTORY.objectNode();
          personNode.put("name", text(children.get(1)));
          personNode.put("country", text(children.get(2)));
          tableValues.set(text(children.get(0)), personNode);
        } else if (children.size() == 2) {
          tableValues.put(text(children.get(0)), text(children.get(1)));
        }
      }

      raceInfo.set(text(tableName), tableValues);
    }
    return raceInfo;
  }

  private static JsonNode parseResultSection(Element resultsSection) {
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
