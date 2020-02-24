package codes.adamzkover.spelling;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

  private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

  /**
   * Create Hunspell dictionary from a database.
   * @param args command line arguments are not used
   */
  public static void main(String[] args) {

    Properties ordbankProperties = loadProperties("/jdbc.properties");
    Ordbank ordbank = new Ordbank();

    ordbank.open(ordbankProperties);
    List<Paradigm> paradigmList = ordbank.readParadigms("NN");
    List<Lemma> lemmaList = ordbank.readLemmas("NN");

    Properties hunspellProperties = loadProperties("/files.properties");
    Hunspell hunspell = new Hunspell();

    hunspell.open(new File(hunspellProperties.getProperty("aff")));
    hunspell.writeParadigms(paradigmList);
    hunspell.close();

    hunspell.open(new File(hunspellProperties.getProperty("dic")));
    hunspell.writeLemmas(lemmaList, paradigmList);
    hunspell.close();

  }

  private static Properties loadProperties(String propertyFile) {
    Properties properties = new Properties();
    try (InputStream stream = Launcher.class.getResourceAsStream(propertyFile)) {
      properties.load(stream);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return properties;
  }

}
