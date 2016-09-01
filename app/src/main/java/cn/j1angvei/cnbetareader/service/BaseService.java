package cn.j1angvei.cnbetareader.service;

import android.app.IntentService;

import cn.j1angvei.cnbetareader.CBApplication;
import cn.j1angvei.cnbetareader.di.component.ApplicationComponent;
import cn.j1angvei.cnbetareader.di.component.ServiceComponent;

/**
 * Created by Wayne on 2016/9/1.
 */

public abstract class BaseService extends IntentService {
    protected ServiceComponent mServiceComponent;

    public BaseService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doInjection();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((CBApplication) getApplication()).getApplicationComponent();
    }

    public ServiceComponent getServiceComponent() {
        return mServiceComponent;
    }

    protected abstract void doInjection();
}