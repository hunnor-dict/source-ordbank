package codes.adamzkover.spelling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Paradigm {

  private static final String SFX_LINE = "SFX %s %s %s .";

  private String ordbankId;

  private int hunspellId;

  private String rootPattern;

  private List<String> patterns;

  private List<String> pluses = null;

  private List<String> percents = null;

  public String getOrdbankId() {
    return ordbankId;
  }

  public void setOrdbankId(String ordbankId) {
    this.ordbankId = ordbankId;
  }

  public int getHunspellId() {
    return hunspellId;
  }

  public void setHunspellId(int hunspellId) {
    this.hunspellId = hunspellId;
  }

  public String getRootPattern() {
    return rootPattern;
  }

  public void setRootPattern(String rootPattern) {
    this.rootPattern = rootPattern;
  }

  public List<String> getPatterns() {
    return patterns;
  }

  public void setPatterns(List<String> patterns) {
    this.patterns = patterns;
  }

  public List<String> getPluses() {
    return pluses;
  }

  public void setPluses(List<String> pluses) {
    this.pluses = pluses;
  }

  public List<String> getPercents() {
    return percents;
  }

  public void setPercents(List<String> percents) {
    this.percents = percents;
  }

  /**
   * Convert the paradigm to Hunspell.
   * @return the Hunspell rules that represent the paradigm.
   */
  public String toHunspell() {

    List<String> lines = new ArrayList<>();

    if (pluses == null || pluses.isEmpty()) {
      if (percents == null || percents.isEmpty()) {
        // (-) +
        // (-) %
        addPatterns(lines);
      } else {
        // (-) +
        // (+) %
        resolvePercents(lines);
      }
    } else {
      if (percents == null || percents.isEmpty()) {
        // (+) +
        // (-) %
        resolvePluses(lines);
      } else {
        // (+) +
        // (+) %
        resolvePlusesPercents(lines);
      }
    }

    lines.sort(Comparator.naturalOrder());
    StringBuilder rules = new StringBuilder();

    rules.append("# ").append(this.getOrdbankId()).append("\n");
    rules.append(String.format("SFX %s N %s",
        this.getHunspellId(),
        lines.size())).append("\n");
    for (String line : lines) {
      rules.append(line).append("\n");
    }

    rules.append("\n");

    return rules.toString();

  }

  private void addPatterns(List<String> lines) {
    for (String pattern : this.getPatterns()) {
      String line = String.format(SFX_LINE,
          this.getHunspellId(),
          "".equals(this.getRootPattern()) ? "0" : this.getRootPattern(),
          "".equals(pattern) ? "0" : pattern);
      if (!lines.contains(line)) {
        lines.add(line);
      }
    }
  }

  private void resolvePercents(List<String> lines) {
    for (String percent : percents) {
      for (String pattern : this.getPatterns()) {
        String rootPatternPercent = "".equals(this.getRootPattern())
            ? "0" : this.getRootPattern().replace("+", percent);
        String patternPercent = "".equals(pattern) ? "0" : pattern.replace("+", percent);
        String line = String.format(SFX_LINE,
            this.getHunspellId(), rootPatternPercent, patternPercent);
        if (!lines.contains(line)) {
          lines.add(line);
        }
      }
    }
  }

  private void resolvePluses(List<String> lines) {
    for (String plus : pluses) {
      for (String pattern : this.getPatterns()) {
        String rootPatternPlus = "".equals(this.getRootPattern())
            ? "0" : this.getRootPattern().replace("+", plus);
        String patternPlus = "".equals(pattern) ? "0" : pattern.replace("+", plus);
        String line = String.format(SFX_LINE,
            this.getHunspellId(), rootPatternPlus, patternPlus);
        if (!lines.contains(line)) {
          lines.add(line);
        }
      }
    }
  }

  private void resolvePlusesPercents(List<String> lines) {
    for (String plus : pluses) {
      for (String percent : percents) {
        for (String pattern : this.getPatterns()) {
          replacePlusesPercents(plus, percent, pattern, lines);
        }
      }
    }
  }

  private void replacePlusesPercents(
      String plus, String percent, String pattern, List<String> lines) {
    String rootPatternPlusPercent = "".equals(this.getRootPattern())
        ? "0" : this.getRootPattern().replace("+", plus).replace("%", percent);
    String patternPlusPercent = "".equals(pattern)
        ? "0  " : pattern.replace("+", plus).replace("%", percent);
    String line = String.format(SFX_LINE,
        this.getHunspellId(), rootPatternPlusPercent, patternPlusPercent);
    if (!lines.contains(line)) {
      lines.add(line);
    }
  }

}
