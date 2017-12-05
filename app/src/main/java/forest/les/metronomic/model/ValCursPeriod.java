package forest.les.metronomic.model;

import com.google.gson.Gson;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ValCurs")
public class ValCursPeriod {
    @Attribute(name = "DateRange1", empty = "", required = false)
    public String date1;

    @Attribute(name = "DateRange2", empty = "", required = false)
    public String date2;

    @Attribute(required = false)
    public String ID = "";

    @Attribute(name = "name", required = false, empty = "")
    public String name;

    @ElementList(inline = true, required = false)
    public List<Record> records;

    //    @Override
//    public String toString() {
//        return "ValCursPeriod{" +
//                "date1='" + date1 + '\'' +
//                ", date2='" + date2 + '\'' +
//                ", ID='" + ID + '\'' +
//                ", name='" + name + '\'' +
//                ", records=" + records +
//                '}';
//    }
//
    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }

}