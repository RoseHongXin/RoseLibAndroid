package hx.widget.adapterview;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Rose on 10/13/2016.
 *
 * paging.
 *
 */

public interface IReq2<T> {

    Observable<List<T>> get(int page);

}
