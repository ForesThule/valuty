package forest.les.metronomic.model.realm;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import io.realm.RealmObject;

@Root(name = "Record")
public class RealmRecord extends RealmObject
{
    @Attribute(name = "Date",required = false)
    public String date;

    @Attribute(name = "Id" ,required = false)
    private String ID;

    @Element(name = "Value",required = false)
    public String value;

    @Element(name = "Nominal", required = false)
    public String nominal;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

//    @Override
//    public String toString() {
//        return "Record{" +
//                "date='" + date + '\'' +
//                ", ID='" + ID + '\'' +
//                ", value='" + value + '\'' +
//                ", nominal='" + nominal + '\'' +
//                '}';
//    }

//    @Override
//    public String toString() {
//        return new Gson().toJson(this).toString();
//    }
}