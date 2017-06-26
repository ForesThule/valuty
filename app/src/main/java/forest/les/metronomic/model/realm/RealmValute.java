package forest.les.metronomic.model.realm;

import io.realm.RealmObject;

/**
 * Created by root on 25.06.17.
 */

public class RealmValute extends RealmObject {

    private String name;
    private String value;
    private String nominal;
    private String parentCode;
    private String date;

    public RealmValute fromValuta(){
        RealmValute realmValute = new RealmValute();

        return realmValute;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
