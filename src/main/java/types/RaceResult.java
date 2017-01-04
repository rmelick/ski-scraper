package types;

import java.util.List;

/**
 * A result for a race
 *
 * E.x. http://data.fis-ski.com/dynamic/results.html?sector=AL&raceid=86813
 */
public class RaceResult {
	private final String _name;
	private final Long _id;
	private final List<RacerResult> _racerResults;

	private RaceResult(String name, long id, List<RacerResult> racerResults) {
		_name = name;
		_id = id;
		_racerResults = racerResults;
	}

	public static class Builder {
		private String _name;
		private Long _id;
		private List<RacerResult> _racerResults;

		public void setName(String name) {
			_name = name;
		}

		public void setId(Long id) {
			_id = id;
		}

		public void setRacerResults(List<RacerResult> racerResults) {
			_racerResults = racerResults;
		}

		public RaceResult build() {
			return new RaceResult(_name, _id, _racerResults);
		}
	}
}
