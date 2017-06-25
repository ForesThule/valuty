package forest.les.metronomic.model.realm;

import io.realm.RealmObject;

public class RealmString extends RealmObject {

    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}