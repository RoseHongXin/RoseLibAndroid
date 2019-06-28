package hx.components;

import io.reactivex.Observable;

/**
 * Created by Rose on 2017/4/22.
 */

public interface IRetrofitApi {
    void API_REQUEST(Observable observable);
    void API_DISPOSE();
}
