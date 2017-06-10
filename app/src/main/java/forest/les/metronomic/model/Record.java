package forest.les.metronomic.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Record")
public class Record
{
    @Attribute(name = "Date",required = false)
    public String date;

    @Attribute(name = "Id" ,required = false)
    private String ID;

    @Element(name = "Value",required = false)
    public String value;

    @Element(name = "Nominal", required = false)
    public String nominal;

    @Override
    public String toString() {
        return "Record{" +
                "date='" + date + '\'' +
                ", ID='" + ID + '\'' +
                ", value='" + value + '\'' +
                ", nominal='" + nominal + '\'' +
                '}';
    }
}