package types;

/**
 *
 */
public class RacerResult {
  private final Integer _rank;
  private final Integer _bib;
  private final Integer _fisCode;
  private final String _name;
  private final Integer _birthYear;
  private final String _nation;
  private final Double _run1TimeMs;
  private final Double _run2TimeMs;
  private final Double _totalTimeMs;
  private final Double _diffFromWinnerMs;
  private final Double _fisPoints;
  private final Double _wcPoints;

  private RacerResult(Integer rank, Integer bib, Integer fisCode, String name, Integer birthYear, String nation,
      Double run1TimeMs, Double run2TimeMs, Double totalTimeMs, Double diffFromWinnerMs, Double fisPoints,
      Double wcPoints)
  {
    _rank = rank;
    _bib = bib;
    _fisCode = fisCode;
    _name = name;
    _birthYear = birthYear;
    _nation = nation;
    _run1TimeMs = run1TimeMs;
    _run2TimeMs = run2TimeMs;
    _totalTimeMs = totalTimeMs;
    _diffFromWinnerMs = diffFromWinnerMs;
    _fisPoints = fisPoints;
    _wcPoints = wcPoints;
  }
  
  public static class Builder {
    private Integer _rank;
    private Integer _bib;
    private Integer _fisCode;
    private String _name;
    private Integer _birthYear;
    private String _nation;
    private Double _run1TimeMs;
    private Double _run2TimeMs;
    private Double _totalTimeMs;
    private Double _diffFromWinnerMs;
    private Double _fisPoints;
    private Double _wcPoints;

    public void setRank(Integer rank) {
      _rank = rank;
    }

    public void setBib(Integer bib) {
      _bib = bib;
    }

    public void setFisCode(Integer fisCode) {
      _fisCode = fisCode;
    }

    public void setName(String name) {
      _name = name;
    }

    public void setBirthYear(Integer birthYear) {
      _birthYear = birthYear;
    }

    public void setNation(String nation) {
      _nation = nation;
    }

    public void setRun1TimeMs(Double run1TimeMs) {
      _run1TimeMs = run1TimeMs;
    }

    public void setRun2TimeMs(Double run2TimeMs) {
      _run2TimeMs = run2TimeMs;
    }

    public void setTotalTimeMs(Double totalTimeMs) {
      _totalTimeMs = totalTimeMs;
    }

    public void setDiffFromWinnerMs(Double diffFromWinnerMs) {
      _diffFromWinnerMs = diffFromWinnerMs;
    }

    public void setFisPoints(Double fisPoints) {
      _fisPoints = fisPoints;
    }

    public void setWcPoints(Double wcPoints) {
      _wcPoints = wcPoints;
    }

    public RacerResult build() {
      return new RacerResult(_rank, _bib, _fisCode, _name, _birthYear, _nation, _run1TimeMs, _run2TimeMs, _totalTimeMs,
          _diffFromWinnerMs, _fisPoints, _wcPoints);
    }
  }
}
