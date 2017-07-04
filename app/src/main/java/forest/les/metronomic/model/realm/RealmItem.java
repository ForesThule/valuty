package forest.les.metronomic.model.realm;

import com.google.gson.Gson;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import io.realm.RealmObject;

/**
 * Created by root on 04.06.17.
 */

public class RealmItem extends RealmObject{
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getISO_Num_Code() {
        return ISO_Num_Code;
    }

    public void setISO_Num_Code(String ISO_Num_Code) {
        this.ISO_Num_Code = ISO_Num_Code;
    }

    public String getIsoCharcode() {
        return isoCharcode;
    }

    public void setIsoCharcode(String isoCharcode) {
        this.isoCharcode = isoCharcode;
    }

    public String id;

    public String name;

    public String engName;

    public String nominal;

    public String parentCode;

    public String ISO_Num_Code;

    public String isoCharcode;

//        @Override
//        public String toString() {//    @Override
//    public String toString() {
//        return new Gson().toJson(this).toString();
//    }
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
