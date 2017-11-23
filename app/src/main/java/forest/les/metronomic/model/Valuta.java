package forest.les.metronomic.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name = "Valuta")
public class Valuta {
    @Attribute(name = "name")
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @ElementList(inline = true, required = false)
    public List<Item> items;

    @Attribute(required = false)
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