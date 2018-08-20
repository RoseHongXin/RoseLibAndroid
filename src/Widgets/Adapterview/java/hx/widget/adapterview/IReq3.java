package hx.widget.adapterview;

import io.reactivex.Observable;

/**
 * Created by Rose on 11/17/2016.
 *
 * get the request sequence, data loading has been handled within operator flatMap..
 *
 */

public interface IReq3<T> {

    Observable getReqTrigger(boolean refresh);
}
