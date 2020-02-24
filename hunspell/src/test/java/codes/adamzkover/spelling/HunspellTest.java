package codes.adamzkover.spelling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class HunspellTest {

  @Test
  public void testAffEmpty(@TempDir File tempDir) throws IOException {

    Paradigm paradigm = new Paradigm();
    paradigm.setOrdbankId("100");
    paradigm.setHunspellId(1);
    paradigm.setRootPattern("");
    paradigm.setPatterns(Arrays.asList("sfx"));

    File affFile = new File(tempDir, "test.aff");

    Hunspell hunspell = new Hunspell();
    hunspell.open(affFile);
    hunspell.writeParadigms(Arrays.asList(paradigm));
    hunspell.close();

    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(affFile)));

    List<String> lines = Arrays.asList(
        "SET UTF-8",
        "FLAG num",
        "",
        "# 100",
        "SFX 1 N 1",
        "SFX 1 0 sfx .",
        "",
        "SFX 2 N 1",
        "SFX 2 0 xyxyxyxy .",
        "");

    for (String expected : lines) {
      String actual = reader.readLine();
      assertEquals(expected, actual);
    }

    String line = reader.readLine();
    assertNull(line);

    reader.close();

  }

}
