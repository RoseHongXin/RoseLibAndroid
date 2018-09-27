package hx.components;

import io.reactivex.Observable;

/**
 * Created by rose on 16-8-12.
 */


public interface IRefresh<D> {

    Observable<D> getApi();
    void onBind(D data);
}
