package forest.les.metronomic.events;

import com.mikepenz.materialize.holder.StringHolder;

import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.realm.RealmValCursPeriod;

/**
 * Created by root on 05.06.17.
 */

public class EventDynamicCurse {

    public final RealmValCursPeriod valCurs;

    public EventDynamicCurse(RealmValCursPeriod valCurs) {
        this.valCurs = valCurs;
    }

}
