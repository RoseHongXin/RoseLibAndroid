package hx.widget.adapterview;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Rose on 11/17/2016.
 *
 * with basic paging request. and extra refresh callback and complete callback when paging api request finished.
 *
 */
public interface IReq4<T> {
    Observable<List<T>> getReqApi(int page);
    void onRefresh();
    void onComplete();
}
