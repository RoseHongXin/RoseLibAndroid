package rose.android.jlib.components;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Rose on 2017/4/22.
 */

public interface IRetrofitApi {
    void API_REQUEST(Observable observable);
    void API_DISPOSE();
}
