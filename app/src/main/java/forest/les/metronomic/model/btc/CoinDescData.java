package forest.les.metronomic.model.btc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 07.12.17.
 */

public class CoinDescData {
    public Map<String,Float> bpi = new HashMap<>();

    @Override
    public String toString() {
        return "CoinDescData{" +
                "bpi=" + bpi +
                '}';
    }
}
