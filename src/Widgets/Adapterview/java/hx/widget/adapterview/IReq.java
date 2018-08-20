package hx.widget.adapterview;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Rose on 10/13/2016.
 *
 * get one page data, without paging.
 *
 */

public interface IReq<T> {

    Observable<List<T>> get();
}
