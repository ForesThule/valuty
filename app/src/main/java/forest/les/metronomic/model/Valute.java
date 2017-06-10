package forest.les.metronomic.model;

import com.google.gson.Gson;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by root on 04.06.17.
 */

@Root(name = "Valute")
public class Valute
    {
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

        public Currency currency;


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
