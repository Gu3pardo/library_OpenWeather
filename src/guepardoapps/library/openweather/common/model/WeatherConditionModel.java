package guepardoapps.library.openweather.common.model;

import java.io.Serializable;

import guepardoapps.toolset.common.Logger;

public class WeatherConditionModel implements Serializable {

	private static final long serialVersionUID = 5283741583914709641L;

	private static final String TAG = WeatherConditionModel.class.getSimpleName();
	private Logger _logger;

	private int _count;
	private String _weekendTipp;
	private String _workdayTipp;
	private int _icon;

	public WeatherConditionModel(int count, String weekendTipp, String workdayTipp, int icon) {
		_logger = new Logger(TAG);
		_logger.Debug(WeatherConditionModel.class.getName() + " created...");

		_count = count;
		_weekendTipp = weekendTipp;
		_workdayTipp = workdayTipp;
		_icon = icon;

		_logger.Debug("_count: " + String.valueOf(_count));
		_logger.Debug("_weekendTipp: " + _weekendTipp);
		_logger.Debug("_workdayTipp: " + _workdayTipp);
		_logger.Debug("_icon: " + String.valueOf(_icon));
	}

	public int GetCount() {
		return _count;
	}

	public String GetWeekendTipp() {
		return _weekendTipp;
	}

	public String GetWorkdayTipp() {
		return _workdayTipp;
	}

	public int GetIcon() {
		return _icon;
	}

	public void ChangeTippsToTommorrow() {
		_weekendTipp = _weekendTipp.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's",
				"Tomorrow is");
		_workdayTipp = _workdayTipp.replace("Today", "Tomorrow").replace("today", "tomorrow").replace("It's",
				"Tomorrow is");
	}

	public void AddCount(int addValue) {
		_count += addValue;
	}

	@Override
	public String toString() {
		return "[WeatherConditionModel: Count: " + String.valueOf(_count) + ", WorkdayTipp: " + _workdayTipp
				+ ", WeekendTipp: " + _weekendTipp + "]";
	}
}