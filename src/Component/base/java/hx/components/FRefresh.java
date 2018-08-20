package hx.components;

/**
 * Created by rose on 16-8-11.
 */

public abstract class FRefresh<D> extends FBaseRefresh implements IRefresh<D> {

    @Override
    public void refresh() {
        getApi().doOnComplete(() -> _p2rl_.refreshComplete())
                .subscribe(this::onBind);
    }

}
