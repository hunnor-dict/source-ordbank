package codes.adamzkover.spelling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class LemmaTest {

  @Test
  public void testGrunnform() {
    Lemma lemma = new Lemma();
    assertNull(lemma.getGrunnform());
    lemma.setGrunnform("foo");
    assertEquals("foo", lemma.getGrunnform());
  }

  @Test
  public void testParadigmList() {
    Lemma lemma = new Lemma();
    assertNull(lemma.getParadigmList());
    lemma.setParadigmList(Arrays.asList("123A"));
    assertNotNull(lemma.getParadigmList());
    assertEquals(1, lemma.getParadigmList().size());
    assertEquals("123A", lemma.getParadigmList().get(0));
  }

}
