package codes.adamzkover.spelling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.Test;

public class OrdbankTest {

  @Test
  public void testOrdbankReader() throws SQLException {

    Properties properties = new Properties();
    properties.put("url", "jdbc:sqlite:src/test/resources/ordbank.sqlite3");
    properties.put("username", "ordbank");
    properties.put("password", "ordbank");
    
    Ordbank ordbank = new Ordbank();
    ordbank.open(properties);

    List<Paradigm> paradigmList = ordbank.readParadigms("NN");
    assertEquals(7, paradigmList.size());

    Paradigm paradigm = paradigmList.get(0);
    assertEquals("001", paradigm.getOrdbankId());
    assertEquals(1, paradigm.getHunspellId());
    assertEquals("e", paradigm.getRootPattern());
    List<String> patterns = Arrays.asList("a", "ar", "ast", "ande", "");
    assertEquals(patterns, paradigm.getPatterns());
    assertNull(paradigm.getPluses());
    assertNull(paradigm.getPercents());

    paradigm = paradigmList.get(1);
    assertEquals("019", paradigm.getOrdbankId());
    assertEquals(2, paradigm.getHunspellId());
    assertEquals("++e", paradigm.getRootPattern());
    patterns = Arrays.asList("++a", "++er", "++ast", "+de", "+t", "+d", "++ande", "++");
    assertEquals(patterns, paradigm.getPatterns());
    assertNotNull(paradigm.getPluses());
    assertEquals(1, paradigm.getPluses().size());
    assertEquals("d", paradigm.getPluses().get(0));
    assertNull(paradigm.getPercents());

    paradigm = paradigmList.get(2);
    assertEquals("700", paradigm.getOrdbankId());
    assertEquals(3, paradigm.getHunspellId());
    assertEquals("", paradigm.getRootPattern());
    patterns = Arrays.asList("en", "ar", "ane");
    assertEquals(patterns, paradigm.getPatterns());
    assertNull(paradigm.getPluses());
    assertNull(paradigm.getPercents());

    paradigm = paradigmList.get(3);
    assertEquals("720", paradigm.getOrdbankId());
    assertEquals(4, paradigm.getHunspellId());
    assertEquals("e+", paradigm.getRootPattern());
    patterns = Arrays.asList("e+en", "+ar", "+ane");
    assertEquals(patterns, paradigm.getPatterns());
    assertNotNull(paradigm.getPluses());
    assertEquals(2, paradigm.getPluses().size());
    assertEquals("l", paradigm.getPluses().get(0));
    assertEquals("r", paradigm.getPluses().get(1));
    assertNull(paradigm.getPercents());

    paradigm = paradigmList.get(4);
    assertEquals("721", paradigm.getOrdbankId());
    assertEquals(5, paradigm.getHunspellId());
    assertEquals("++e%", paradigm.getRootPattern());
    patterns = Arrays.asList("++e%en", "+%ar", "+%ane");
    assertEquals(patterns, paradigm.getPatterns());
    assertNotNull(paradigm.getPluses());
    assertEquals(2, paradigm.getPluses().size());
    assertEquals("m", paradigm.getPluses().get(0));
    assertEquals("t", paradigm.getPluses().get(1));
    assertNotNull(paradigm.getPercents());
    assertEquals(2, paradigm.getPercents().size());
    assertEquals("l", paradigm.getPercents().get(0));
    assertEquals("r", paradigm.getPercents().get(1));

    paradigm = paradigmList.get(5);
    assertEquals("800", paradigm.getOrdbankId());
    assertEquals(6, paradigm.getHunspellId());
    assertEquals("", paradigm.getRootPattern());
    patterns = Arrays.asList("et", "a");
    assertEquals(patterns, paradigm.getPatterns());
    assertNull(paradigm.getPluses());
    assertNull(paradigm.getPercents());

    paradigm = paradigmList.get(6);
    assertEquals("905", paradigm.getOrdbankId());
    assertEquals(7, paradigm.getHunspellId());
    assertEquals("e", paradigm.getRootPattern());
    patterns = Arrays.asList("a", "er", "ene");
    assertEquals(patterns, paradigm.getPatterns());
    assertNull(paradigm.getPluses());
    assertNull(paradigm.getPercents());

  }

  @Test
  public void testLemma() throws SQLException {

    Properties properties = new Properties();
    properties.put("url", "jdbc:sqlite:src/test/resources/ordbank.sqlite3");
    properties.put("username", "ordbank");
    properties.put("password", "ordbank");
    
    Ordbank ordbank = new Ordbank();
    ordbank.open(properties);
    
    List<Lemma> lemmaList = ordbank.readLemmas("NN");
    assertEquals(11, lemmaList.size());

  }

}
