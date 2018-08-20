package hx.kit.async;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by rose on 16-8-23.
 */

public class RxBus {

    private static RxBus _THIS = null;
    /**
     * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
     */

    private Subject<Object> mBus;

    private RxBus(){
//        mBus = new SerializedSubject<>(PublishSubject.create());
        mBus = PublishSubject.create();
    }

    public static synchronized RxBus get() {
        if (_THIS == null) _THIS = new RxBus();
        return _THIS;
    }

    public void post(Object o) {
        mBus.onNext(o);
    }
    @Deprecated
    public Observable<Object> toObserverable() {
        return mBus;
    }

    public <T> Observable<T> toObservable(final Class<T> eventType) {
//        return mBus.filter(eventType::isInstance).cast(eventType);
        return mBus.ofType(eventType);
    }

    public <T> void post(Class<T> clz, String tag){
        mBus.onNext(Obj.create(clz, tag));
    }
    public <T> Observable<T> toObservable(final Class<T> eventType, final String tag) {
        return mBus
                .observeOn(AndroidSchedulers.mainThread())
                .filter(o -> {
                    if (!(o instanceof Obj)) return false;
                    Obj ro = (Obj) o;
                    return eventType.isInstance(ro.getObj()) && tag != null && tag.equals(ro.getTag());
                }).map(o -> {
                    Obj ro = (Obj) o;
                    return (T) ro.getObj();
                });
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }


    public static class Obj {

        private String tag;
        private Object obj;

        public Obj(Object obj, String tag) {
            this.tag = tag;
            this.obj = obj;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public static Obj create(Object obj, String tag) {
            return new Obj(obj, tag);
        }
    }
}
