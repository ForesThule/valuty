package forest.les.metronomic.model.realm;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 25.06.17.
 */

public class RealmValCurs extends RealmObject {

    @PrimaryKey
    public String date;

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<RealmValute> getValutes() {
        return valutes;
    }

    public void setValutes(RealmList<RealmValute> valutes) {
        this.valutes = valutes;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<RealmValute> valutes;
}
