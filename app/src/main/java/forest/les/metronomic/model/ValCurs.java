package forest.les.metronomic.model;

import com.google.gson.Gson;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ValCurs")
public class ValCurs
{
    @Attribute(name = "Date", empty = "", required = false)
    public String date;

    @Attribute(required = false)
    private String ID;

    @Attribute(name = "name",required = false,empty = "")
    public String name;


    @ElementList(inline = true, required = false)
    public List<Valute> valute;


    @ElementList(inline = true, required = false)
    public List<Record> records;

//    @Override
//    public String toString() {
//        return "ValCurs{" +
//                "date='" + date + '\'' +
//                ", name='" + name + '\'' +
//                ", valute=" + valute +
//                ", records=" + records +
//                '}';
//    }

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
}