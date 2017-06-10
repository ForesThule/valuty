package forest.les.metronomic.model;

import com.google.gson.Gson;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Currency;

/**
 * Created by root on 04.06.17.
 */

@Root(name = "Item")
public class Item {
    @Attribute(name = "ID")
    public String id;

    @Element(name = "Name")
    public String name;

    @Element(name = "EngName")
    public String engName;

    @Element(name = "Nominal")
    public String nominal;

    @Element(name = "ParentCode")
    public String parentCode;

    @Element(data = true, name = "ISO_Num_Code", required = false)
    public String ISO_Num_Code;

    @Element(name = "ISO_Char_Code", required = false)
    public String isoCharcode;


    public Currency currency;

//        @Override
//        public String toString() {
//            return "Item{" +
//                    "id='" + id + '\'' +
//                    ", name='" + name + '\'' +
//                    ", engName='" + engName + '\'' +
//                    ", nominal='" + nominal + '\'' +
//                    ", parentCode='" + parentCode + '\'' +
//                    ", isoNumCode='" + ISO_Num_Code + '\'' +
//                    ", isoCharcode='" + isoCharcode + '\'' +
//                    ", currency=" + currency +
//                    '}';
//        }

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
}
