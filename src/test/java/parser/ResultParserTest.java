package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 *
 */
public class ResultParserTest {
  private final ObjectWriter _writer = new ObjectMapper().writerWithDefaultPrettyPrinter();


  @Test
  public void testParse() throws Exception {
    File testFile = Paths.get(getClass().getResource("results/AL-86813.html").toURI()).toFile();
    Document document = Jsoup.parse(testFile, "UTF-8");
    JsonNode parsedDocument = ResultParser.parse(document);

    System.out.println(_writer.writeValueAsString(parsedDocument));
  }
}