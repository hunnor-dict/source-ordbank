package codes.adamzkover.spelling;

import java.util.List;

public class Lemma {

  private String grunnform;

  private List<String> paradigmList;

  public String getGrunnform() {
    return grunnform;
  }

  public void setGrunnform(String grunnform) {
    this.grunnform = grunnform;
  }

  public List<String> getParadigmList() {
    return paradigmList;
  }

  public void setParadigmList(List<String> paradigmList) {
    this.paradigmList = paradigmList;
  }

}
