package forest.les.metronomic.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Valuta")
public class Valuta
{
    @Attribute(name = "name")
    public String name;

    @ElementList(inline = true, required = false)
    public List<Item> items;

    @Attribute(required = false)
    private String ID;

//    @Override
//    public String toString() {
//
////        return "Valuta{" +
////                "name='" + name + '\'' +
////                ", items=" + items +
////                '}';
//
////        return new com.google.gson.Gson().toJson(this);
////    }
}