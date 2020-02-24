package codes.adamzkover.spelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hunspell {

  private static final Logger logger = LoggerFactory.getLogger(Hunspell.class);

  private PrintWriter writer;

  /**
   * Open the writer.
   * @param file the file to write to
   */
  public void open(File file) {
    try {
      writer = new PrintWriter(file.getAbsolutePath(), StandardCharsets.UTF_8.name());
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * Close the writer.
   */
  public void close() {
    if (writer != null) {
      writer.close();
    }
  }

  /**
   * Write all paradigms to the writer.
   * @param paradigmList paradigms
   */
  public void writeParadigms(List<Paradigm> paradigmList) {
    writer.println("SET UTF-8");
    writer.println("FLAG num");
    writer.println("");

    for (Paradigm paradigm : paradigmList) {
      if (paradigm.getPatterns() != null && !paradigm.getPatterns().isEmpty()) {
        writer.write(paradigm.toHunspell());
      }
    }

    int id = paradigmList.size() + 1;
    writer.println("SFX " + id + " N 1");
    writer.println("SFX " + id + " 0 xyxyxyxy .");
    writer.println("");

  }

  /**
   * Write all lemmas to the writer.
   * @param lemmaList lemmas to write
   * @param paradigmList paradigms
   */
  public void writeLemmas(List<Lemma> lemmaList, List<Paradigm> paradigmList) {
    
    Map<String, Paradigm> paradigmByOrdbankId = paradigmList.stream()
        .collect(Collectors.toMap(Paradigm::getOrdbankId, Function.identity()));
    
    List<String> lines = new ArrayList<>();

    int dummyId = paradigmList.size() + 1;
    for (Lemma lemma : lemmaList) {
      writeLemma(lemma, lines, paradigmByOrdbankId, dummyId);
    }

    writer.println(lines.size());
    for (String line : lines) {
      writer.println(line);
    }

  }

  private void writeLemma(Lemma lemma, List<String> lines,
      Map<String, Paradigm> paradigmByOrdbankId, int dummyId) {
    List<String> extraForms = new ArrayList<>();
    String line = lemma.getGrunnform();
    if (line.contains("/")) {
      line = "\"" + line + "\""; 
    }
    List<String> ordbankParadigmList = lemma.getParadigmList();
    if (ordbankParadigmList != null && !ordbankParadigmList.isEmpty()) {
      line = line + "/";
      StringBuilder idList = new StringBuilder();
      for (String ordbankParadigmId : ordbankParadigmList) {
        Paradigm paradigm = paradigmByOrdbankId.get(ordbankParadigmId);
        int hunspellId = paradigm.getHunspellId();
        if (idList.length() > 0) {
          idList.append(",");
        }
        idList.append(hunspellId);
        if (paradigm.getRootPattern().equals(lemma.getGrunnform())) {
          List<String> forms = paradigm.getPatterns().stream()
              .distinct().collect(Collectors.toList());
          extraForms.addAll(forms);
        }
      }
      line = line + idList;
    } else {
      if (line.contains("/")) {
        line = line + "/" + dummyId;
      }
    }
    lines.add(line);
    lines.addAll(extraForms);
  }

}
