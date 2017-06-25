package forest.les.metronomic.model.realm;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by root on 25.06.17.
 */

public class RealmValCurs extends RealmObject {

    public RealmString date;

    public RealmString name;

    public RealmString getName() {
        return name;
    }

    public void setName(RealmString name) {
        this.name = name;
    }

    public RealmList<RealmString> getValutes() {
        return valutes;
    }

    public void setValutes(RealmList<RealmString> valutes) {
        this.valutes = valutes;
    }

    public RealmString getDate() {

        return date;
    }

    public void setDate(RealmString date) {
        this.date = date;
    }

    public RealmList<RealmString> valutes;
}
