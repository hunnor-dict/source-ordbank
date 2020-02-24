package codes.adamzkover.spelling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class ParadigmTest {

  @Test
  public void testOrdbankId() {
    Paradigm paradigm = new Paradigm();
    assertNull(paradigm.getOrdbankId());
    paradigm.setOrdbankId("123A");
    assertEquals("123A", paradigm.getOrdbankId());
  }

  @Test
  public void testHunspellId() {
    Paradigm paradigm = new Paradigm();
    assertEquals(0, paradigm.getHunspellId());
    paradigm.setHunspellId(1);
    assertEquals(1, paradigm.getHunspellId());
  }

  @Test
  public void testRootPattern() {
    Paradigm paradigm = new Paradigm();
    assertNull(paradigm.getRootPattern());
    paradigm.setRootPattern("foo");
    assertEquals("foo", paradigm.getRootPattern());
  }

  @Test
  public void testPatterns() {
    Paradigm paradigm = new Paradigm();
    assertNull(paradigm.getPatterns());
    paradigm.setPatterns(Arrays.asList("foo"));
    assertNotNull(paradigm.getPatterns());
    assertEquals(1, paradigm.getPatterns().size());
    assertEquals("foo", paradigm.getPatterns().get(0));

  }

  @Test
  public void testPluses() {
    Paradigm paradigm = new Paradigm();
    assertNull(paradigm.getPluses());
    paradigm.setPluses(Arrays.asList("foo"));
    assertNotNull(paradigm.getPluses());
    assertEquals(1, paradigm.getPluses().size());
    assertEquals("foo", paradigm.getPluses().get(0));

  }

  @Test
  public void testPercents() {
    Paradigm paradigm = new Paradigm();
    assertNull(paradigm.getPercents());
    paradigm.setPercents(Arrays.asList("foo"));
    assertNotNull(paradigm.getPercents());
    assertEquals(1, paradigm.getPercents().size());
    assertEquals("foo", paradigm.getPercents().get(0));

  }

  @Test
  public void test019() {
    
    Paradigm paradigm = new Paradigm();

    paradigm.setOrdbankId("019");
    paradigm.setHunspellId(1);

    paradigm.setRootPattern("++e");
    paradigm.setPatterns(Arrays.asList(
        "++a", "++er", "++ast", "+de", "+d", "+t", "de", "+ande", "++"));

    paradigm.setPluses(Arrays.asList("d"));

    String expected = "# 019\n"
        + "SFX 1 N 8\n"
        + "SFX 1 dde dande .\n"
        + "SFX 1 dde dd .\n"
        + "SFX 1 dde dda .\n"
        + "SFX 1 dde ddast .\n"
        + "SFX 1 dde dde .\n"
        + "SFX 1 dde dder .\n"
        + "SFX 1 dde de .\n"
        + "SFX 1 dde dt .\n"
        + "\n";

    assertEquals(expected, paradigm.toHunspell());

  }

  @Test
  public void test700() {

    Paradigm paradigm = new Paradigm();

    paradigm.setOrdbankId("700");
    paradigm.setHunspellId(1);

    paradigm.setRootPattern("");
    paradigm.setPatterns(Arrays.asList("en", "ar", "ane"));

    String expected = "# 700\n"
        + "SFX 1 N 3\n"
        + "SFX 1 0 ane .\n"
        + "SFX 1 0 ar .\n"
        + "SFX 1 0 en .\n"
        + "\n";

    assertEquals(expected, paradigm.toHunspell());

  }

  @Test
  public void test800() {

    Paradigm paradigm = new Paradigm();

    paradigm.setOrdbankId("800");
    paradigm.setHunspellId(2);

    paradigm.setRootPattern("");
    paradigm.setPatterns(Arrays.asList("et", "a"));

    String expected = "# 800\n"
        + "SFX 2 N 2\n"
        + "SFX 2 0 a .\n"
        + "SFX 2 0 et .\n"
        + "\n";

    assertEquals(expected, paradigm.toHunspell());

  }

  @Test
  public void test905() {

    Paradigm paradigm = new Paradigm();

    paradigm.setOrdbankId("905");
    paradigm.setHunspellId(3);

    paradigm.setRootPattern("e");
    paradigm.setPatterns(Arrays.asList("a", "er", "ene"));

    String expected = "# 905\n"
        + "SFX 3 N 3\n"
        + "SFX 3 e a .\n"
        + "SFX 3 e ene .\n"
        + "SFX 3 e er .\n"
        + "\n";

    assertEquals(expected, paradigm.toHunspell());

  }

  @Test
  public void test001() {

    Paradigm paradigm = new Paradigm();

    paradigm.setOrdbankId("001");
    paradigm.setHunspellId(4);

    paradigm.setRootPattern("e");
    paradigm.setPatterns(Arrays.asList("a", "ande", "ar", "ast", ""));

    String expected = "# 001\n"
        + "SFX 4 N 5\n"
        + "SFX 4 e 0 .\n"
        + "SFX 4 e a .\n"
        + "SFX 4 e ande .\n"
        + "SFX 4 e ar .\n"
        + "SFX 4 e ast .\n"
        + "\n";

    assertEquals(expected, paradigm.toHunspell());

  }

  @Test
  public void test720() {

    Paradigm paradigm = new Paradigm();

    paradigm.setOrdbankId("720");
    paradigm.setHunspellId(1);

    paradigm.setRootPattern("e+");
    paradigm.setPatterns(Arrays.asList("e+en", "+ar", "+ane"));

    paradigm.setPluses(Arrays.asList("l", "r"));

    String expected = "# 720\n"
        + "SFX 1 N 6\n"
        + "SFX 1 el elen .\n"
        + "SFX 1 el lane .\n"
        + "SFX 1 el lar .\n"
        + "SFX 1 er eren .\n"
        + "SFX 1 er rane .\n"
        + "SFX 1 er rar .\n"
        + "\n";

    assertEquals(expected, paradigm.toHunspell());

  }

  // Pattern with percent only

  @Test
  public void test721() {

    Paradigm paradigm = new Paradigm();

    paradigm.setOrdbankId("721");
    paradigm.setHunspellId(3);

    paradigm.setRootPattern("++e%");
    paradigm.setPatterns(Arrays.asList("++e%en", "+%ar", "+%ane"));

    paradigm.setPluses(Arrays.asList("k", "m"));
    paradigm.setPercents(Arrays.asList("l", "r"));

    String expected = "# 721\n"
        + "SFX 3 N 12\n"
        + "SFX 3 kkel kkelen .\n"
        + "SFX 3 kkel klane .\n"
        + "SFX 3 kkel klar .\n"
        + "SFX 3 kker kkeren .\n"
        + "SFX 3 kker krane .\n"
        + "SFX 3 kker krar .\n"
        + "SFX 3 mmel mlane .\n"
        + "SFX 3 mmel mlar .\n"
        + "SFX 3 mmel mmelen .\n"
        + "SFX 3 mmer mmeren .\n"
        + "SFX 3 mmer mrane .\n"
        + "SFX 3 mmer mrar .\n"
        + "\n";

    assertEquals(expected, paradigm.toHunspell());

  }

}
