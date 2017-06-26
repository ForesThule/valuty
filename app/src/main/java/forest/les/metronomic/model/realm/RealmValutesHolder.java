package forest.les.metronomic.model.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by root on 25.06.17.
 */

public class RealmValutesHolder extends RealmObject {

    private RealmList<RealmValute> valutes;

    public RealmList<RealmValute> getValutes() {
        return valutes;
    }

    public void setValutes(RealmList<RealmValute> valutes) {
        this.valutes = valutes;
    }
}
