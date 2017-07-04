package forest.les.metronomic.model.realm;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import forest.les.metronomic.model.Item;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmValuta extends RealmObject {

    @PrimaryKey
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<RealmItem> getItems() {
        return items;
    }

    public void setItems(RealmList<RealmItem> items) {
        this.items = items;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public RealmList<RealmItem> items;

    public String ID;

    @Override
    public String toString() {

//        return "Valuta{" +
//                "name='" + name + '\'' +
//                ", items=" + items +
//                '}';

        return new com.google.gson.Gson().toJson(this);
    }
}