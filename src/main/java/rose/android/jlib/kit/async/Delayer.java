package rose.android.jlib.kit.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by RoseHongXin on 2017/9/5 0005.
 */

public class Delayer {

    private static final int ONE_SECOND = 1000;

    private static final int MSG_TIME_UP = 0xff01;
    private static final int MSG_TICK = 0xff02;

    private Handler mHandler;
    private Handler mMainLoopHandler;
    private CopyOnWriteArrayList<DelayCallback> mCbs = new CopyOnWriteArrayList<>();


    private static Delayer mInstance;
    private Delayer(){
        HandlerThread handlerThread = new HandlerThread("Delayer");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_TIME_UP:
                        DelayCallback delayCallback = (DelayCallback) msg.obj;
                        if(!mCbs.contains(delayCallback)) return;
                        mMainLoopHandler.post(delayCallback::onTimeUp);
                        removeMessages(MSG_TIME_UP, delayCallback);
                        mCbs.remove(delayCallback);
                        break;
                    case MSG_TICK:
                        final int remain = (msg.arg1 - 1);
                        TickCallback tickCallback = (TickCallback)msg.obj;
                        if(!mCbs.contains(tickCallback)){
                            return;
                        }
                        if(remain < 0) {
                            mMainLoopHandler.post(tickCallback::onTimeUp);
                            removeMessages(MSG_TICK, tickCallback);
                            mCbs.remove(tickCallback);
                            return;
                        }
                        mMainLoopHandler.post(() -> tickCallback.onTick(remain));
                        Message message = new Message();
                        message.what = MSG_TICK;
                        message.arg1 = remain;
                        message.obj = msg.obj;
                        sendMessageDelayed(message, ONE_SECOND);
                        break;
                }
            }
        };
        mMainLoopHandler = new Handler(Looper.getMainLooper());
    }

    public static Delayer obtain(){
        if(mInstance == null) mInstance = new Delayer();
        return mInstance;
    }

    public void delay(DelayCallback cb){
        delay(cb, ONE_SECOND);
    }
    public void delay(DelayCallback cb, long delay){
        mCbs.add(cb);
        Message msg = new Message();
        msg.obj = cb;
        msg.what = MSG_TIME_UP;
        mHandler.sendMessageDelayed(msg, delay);
    }

    public void tick(int time, TickCallback cb){
        mCbs.add(cb);
        Message msg = new Message();
        msg.obj = cb;
        msg.what = MSG_TICK;
        msg.arg1 = (time + 1);
        mHandler.sendMessage(msg);
    }

    public void cancel(DelayCallback cb){
        mCbs.remove(cb);
    }
    public boolean ticking(DelayCallback cb){
        return mCbs.contains(cb) && mHandler.hasMessages(MSG_TICK, cb);
    }

    public interface DelayCallback {
        void onTimeUp();
    }
    public interface TickCallback extends DelayCallback{
        void onTick(int remain);
    }

}
