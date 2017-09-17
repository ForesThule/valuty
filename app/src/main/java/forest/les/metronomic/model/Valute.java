package forest.les.metronomic.model;

import com.google.gson.Gson;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by root on 04.06.17.
 */

@Root(name = "Valute")
public class Valute {
    @Element(name = "Value")
    public String value;

    @Attribute(name = "ID")
    public String id;

    @Element(name = "Name")
    public String name;

    @Element(name = "Nominal")
    public String nominal;

    @Element(name = "CharCode")
    public String charcode;

    @Element(name = "NumCode")
    public String numcode;


    //calculate this -> incoming valute
    //thisValuteVal
    public String calculate(String thisValuteVal, Valute targetValute) {

        Timber.i(thisValuteVal);

        Double thisValuteValDouble = Double.parseDouble(thisValuteVal);

        BigDecimal thisValutedDec = BigDecimal.valueOf(thisValuteValDouble);

        BigDecimal multiplicand = new BigDecimal(targetValute.value);

        BigDecimal multiply = thisValutedDec.multiply(multiplicand);
        Timber.i("calculate: %s", thisValutedDec);

        return thisValutedDec.toString();

//////////////////////////////////////////////////////////

//        String s = editText.getText().toString();
//
//        int nom=0;
//        try {
//            nom = Integer.parseInt(inputValute.nominal);
//        } catch (Exception e){
//
//        }
//        int thisValuteVal = Integer.parseInt(inputValute.value);
//
//
//        int inputInRubles = thisValuteVal/nom;
//
//        Timber.i("calculate: inputInRubles %d",inputInRubles);
//
//        int typedValue = 0;
//
//        try {typedValue = Integer.parseInt(s);}
//        catch (Exception e){}



    }

    //
    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
//        @Override
//        public String toString() {
//            return "Valute{" +
//                    "value='" + value + '\'' +
//                    ", id='" + id + '\'' +
//                    ", name='" + name + '\'' +
//                    ", nominal='" + nominal + '\'' +
//                    ", charcode='" + charcode + '\'' +
//                    ", numcode='" + numcode + '\'' +
//                    ", currency='" + currency + '\'' +
//                    '}';
//        }

}
