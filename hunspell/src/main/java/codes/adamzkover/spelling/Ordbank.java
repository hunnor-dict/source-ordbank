package codes.adamzkover.spelling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ordbank {

  private static final Logger logger = LoggerFactory.getLogger(Ordbank.class);

  private Connection connection;

  /**
   * Open the database connection.
   * @param properties database connection properties
   */
  public void open(Properties properties) {
    try {
      connection = DriverManager.getConnection(properties.getProperty("url"), properties);
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * Read paradigms from teh database.
   * @param norm the database table prefix
   * @return
   */
  public List<Paradigm> readParadigms(String norm) {

    List<Paradigm> paradigmList = new ArrayList<>();

    String sql = String.format("SELECT DISTINCT PARADIGME_ID"
        + " FROM %1$s_LEMMA_PARADIGME"
        + " ORDER BY PARADIGME_ID", norm);

    try (
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet results = statement.executeQuery()) {
      while (results.next()) {

        Paradigm paradigm = new Paradigm();
        paradigm.setOrdbankId(results.getString(1));
        paradigm.setHunspellId(paradigmList.size() + 1);

        loadParadigm(norm, paradigm);
        if (paradigm.getRootPattern() != null) {
          loadPlaceholders(norm, paradigm);
        }

        paradigmList.add(paradigm);

      }
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    }
    
    return paradigmList;

  }

  private void loadParadigm(String norm, Paradigm paradigm) {

    String sql = String.format("SELECT BOY_UTTRYKK"
        + " FROM %1$s_PARADIGME_BOYING"
        + " WHERE PARADIGME_ID = ? ORDER BY BOY_NUMMER", norm);

    try (
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, paradigm.getOrdbankId());
      loadParadigm2(paradigm, statement);
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    }

  }

  private void loadParadigm2(Paradigm paradigm, PreparedStatement statement) {

    try (ResultSet results = statement.executeQuery()) {

      while (results.next()) {
        String pattern = results.getString(1);
        if (paradigm.getRootPattern() == null) {
          paradigm.setRootPattern(pattern);
        } else {
          if (paradigm.getPatterns() == null) {
            paradigm.setPatterns(new ArrayList<>());
          }
          if (!paradigm.getRootPattern().equals(pattern)
              && !"-".equals(pattern)
              && !paradigm.getPatterns().contains(pattern)) {
            paradigm.getPatterns().add(pattern);
          }
        }
      }

    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    }

  }

  private void loadPlaceholders(String norm, Paradigm paradigm) {

    int plusPos = paradigm.getRootPattern().indexOf('+');
    int percentPos = paradigm.getRootPattern().indexOf('%');

    if (plusPos == -1 && percentPos == -1) {
      return;
    }

    if (plusPos != -1) {
      paradigm.setPluses(new ArrayList<>());
    }
    if (percentPos != -1) {
      paradigm.setPercents(new ArrayList<>());
    }

    List<String> lemmaList = loadGrunnforms(norm, paradigm.getOrdbankId());

    for (String lemma : lemmaList) {
      if (plusPos != -1) {
        int index = lemma.length() - paradigm.getRootPattern().length() + plusPos;
        String plus = lemma.substring(index, index + 1);
        if (!paradigm.getPluses().contains(plus)) {
          paradigm.getPluses().add(plus);
        }
      }
      if (percentPos != -1) {
        int index = lemma.length() - paradigm.getRootPattern().length() + percentPos;
        String percent = lemma.substring(index, index + 1);
        if (!paradigm.getPercents().contains(percent)) {
          paradigm.getPercents().add(percent);
        }
      }
    }

    sortPlaceholders(plusPos, percentPos, paradigm);

  }

  private void sortPlaceholders(int plusPos, int percentPos, Paradigm paradigm) {
    if (plusPos != -1) {
      paradigm.getPluses().sort(Comparator.naturalOrder());
    }
    if (percentPos != -1) {
      paradigm.getPercents().sort(Comparator.naturalOrder());
    }
  }

  private List<String> loadGrunnforms(String norm, String ordbankId) {
    List<String> lemmaList = new ArrayList<>();
    String sql = String.format("SELECT GRUNNFORM FROM %1$s_LEMMA, %1$s_LEMMA_PARADIGME"
        + " WHERE %1$s_LEMMA.LEMMA_ID = %1$s_LEMMA_PARADIGME.LEMMA_ID"
        + " AND PARADIGME_ID = ?", norm);
    try (
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, ordbankId);
      loadGrunnforms2(lemmaList, statement);
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    }
    return lemmaList;
  }

  private void loadGrunnforms2(List<String> lemmaList, PreparedStatement statement) {

    try (ResultSet results = statement.executeQuery()) {

      while (results.next()) {
        String lemma = results.getString(1);
        lemmaList.add(lemma);
      }

    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    }

  }

  /**
   * Read lemmas from the database.
   * @param norm the database table prefix
   * @return all lemmas
   */
  public List<Lemma> readLemmas(String norm) {

    List<Lemma> lemmaList = new ArrayList<>();

    String sql = String.format("SELECT GRUNNFORM, group_concat(%1$s_LEMMA_PARADIGME.PARADIGME_ID)"
        + " FROM %1$s_LEMMA LEFT JOIN %1$s_LEMMA_PARADIGME"
        + " ON %1$s_LEMMA.LEMMA_ID = %1$s_LEMMA_PARADIGME.LEMMA_ID"
        + " GROUP BY %1$s_LEMMA.LEMMA_ID ORDER BY GRUNNFORM;", norm);

    try (
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet results = statement.executeQuery()) {

      while (results.next()) {
        Lemma lemma = new Lemma();
        String grunnform = results.getString(1);
        lemma.setGrunnform(grunnform);
        String paradigms = results.getString(2);
        if (paradigms != null) {
          lemma.setParadigmList(Arrays.asList(paradigms.split(",")));
        }
        lemmaList.add(lemma);
      }

    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    }

    return lemmaList;

  }

}
