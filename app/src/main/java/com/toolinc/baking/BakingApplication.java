package com.toolinc.baking;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.toolinc.baking.ui.widget.inject.DaggerIngredientsWidgetComponent;
import com.toolinc.baking.ui.widget.inject.IngredientsWidgetComponent;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Base class for maintaining global application state and provide injection to different
 * components.
 */
public final class BakingApplication extends Application
    implements HasActivityInjector, HasSupportFragmentInjector {

  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  private IngredientsWidgetComponent ingredientsWidgetComponent;

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    DaggerBakingApplicationComponent.create().inject(this);
    ingredientsWidgetComponent = DaggerIngredientsWidgetComponent.builder().build();
  }

  public IngredientsWidgetComponent getIngredientsWidgetComponent() {
    return ingredientsWidgetComponent;
  }

  public static boolean isInternetAvailable(Context context) {
    ConnectivityManager mConMgr =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    return mConMgr != null
        && mConMgr.getActiveNetworkInfo() != null
        && mConMgr.getActiveNetworkInfo().isConnected();
  }
}
