package forest.les.metronomic.data;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by root on 24.10.17.
 */

public class Repository implements RepoIntarface {
    @Override
    public Observable<String> getdata() {
        return Observable.fromCallable(() -> "1111111111");
    }
}
