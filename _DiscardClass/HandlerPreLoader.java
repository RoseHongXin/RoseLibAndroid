package hx.toolkit;

import android.os.HandlerThread;
import android.support.annotation.NonNull;

import java.util.LinkedList;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * 预加载
 * preLoader = RxPreLoader.preLoad(mObservable);
 * preLoader.get(observer1);
 * preLoader.get(observer2);
 * preLoader.reload();
 * preLoader.destroy()
 *
 * @author billy.qi
 */
public class HandlerPreLoader<T> {

    private BehaviorSubject<T> mBehaviorSubject;
    private Observable<T> mObservable;
    private Subscription mSubscription;
    private final LinkedList<Subscription> mObservers = new LinkedList<>();


    private HandlerPreLoader(Observable<T> observable) {
        //注意的是由于onCompleted也是数据流中的一个
        //如果直接observer.subscribeOn(Schedulers.io()).subscribe(mBehaviorSubject);
        //会导致subject只能缓存onCompleted
        //所以此处新建一个OnSubscribe，通过调用subject.onNext(t)的方式来缓存数据
        this.mObservable = observable;
        mBehaviorSubject = BehaviorSubject.create();
        mSubscription = Observable.create((Observable.OnSubscribe<T>) subscriber -> performLoad())
                .subscribeOn(Schedulers.io())
                .subscribe(mBehaviorSubject);
    }

    public static <R> HandlerPreLoader<R> preLoad(@NonNull Observable<R> observable) {
        return new HandlerPreLoader<R>(observable);
    }

    public void reload() {
        performLoad();
    }

    public Subscription get(Observer<T> observer) {
        Subscription subscription = mBehaviorSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        mObservers.add(subscription);
        return subscription;
    }


    private void performLoad() {
        mObservable.subscribeOn(Schedulers.io())
                .subscribe(t -> {
                    if (mBehaviorSubject != null) mBehaviorSubject.onNext(t);
                }, Throwable::printStackTrace);
    }

    public void destroy() {
        synchronized (mObservers) {
            while (!mObservers.isEmpty()) {
                unSubscribe(mObservers.removeFirst());
            }
        }
        unSubscribe(mSubscription);
        mSubscription = null;
        mBehaviorSubject = null;
    }

    private void unSubscribe(Subscription mSubscription) {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
