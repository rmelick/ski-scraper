package types;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 */
public class EventDetails {
  private final JsonNode _additionalInfo;
  private final String _resultsUrl;

  public EventDetails(JsonNode additionalInfo, String resultsUrl) {
    _additionalInfo = additionalInfo;
    _resultsUrl = resultsUrl;
  }

  public JsonNode getAdditionalInfo() {
    return _additionalInfo;
  }

  public String getResultsUrl() {
    return _resultsUrl;
  }

  @Override public String toString() {
    return "EventDetails{" +
        "additionalInfo=" + _additionalInfo +
        ", resultsUrl='" + _resultsUrl + '\'' +
        '}';
  }
}
