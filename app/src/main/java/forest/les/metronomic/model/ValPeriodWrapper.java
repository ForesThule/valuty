package forest.les.metronomic.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10.06.17.
 */

public class ValPeriodWrapper {
    private Map<Item, ValCursPeriod> period;

    public Map<Item, ValCursPeriod> getPeriod() {
        return period;
    }

    public ValPeriodWrapper(Map<Item, ValCursPeriod> period) {

        this.period = period;
    }
}
