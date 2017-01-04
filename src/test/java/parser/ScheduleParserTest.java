package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 */
public class ScheduleParserTest {
  private final ObjectWriter _writer = new ObjectMapper().writerWithDefaultPrettyPrinter();


  @Test
  public void testParse() throws Exception {
    File testFile = Paths.get(getClass().getResource("schedule/2017-Results.htm").toURI()).toFile();
    Document document = Jsoup.parse(testFile, "UTF-8");
    List<String> parsedDocument = ScheduleParser.parse(document);

    System.out.println(_writer.writeValueAsString(parsedDocument));
  }
}