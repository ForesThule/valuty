package forest.les.metronomic.model.realm;

import com.google.gson.Gson;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import forest.les.metronomic.model.Record;
import io.realm.RealmList;
import io.realm.RealmObject;

@Root(name = "ValCurs")
public class RealmValCursPeriod extends RealmObject{


    @Attribute(name = "DateRange1", empty = "", required = false)
    public String date1;

    @Attribute(name = "DateRange2", empty = "", required = false)
    public String date2;

    @Attribute(required = false)
    private String ID;

    @Attribute(name = "name", required = false, empty = "")
    public String name;


    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<RealmRecord> getRecords() {
        return records;
    }

    public void setRecords(RealmList<RealmRecord> records) {
        this.records = records;
    }

    @ElementList(inline = true, required = false)
    public RealmList<RealmRecord> records;

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