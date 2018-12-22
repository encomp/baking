package com.toolinc.baking;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Placeholder application to facilitate overriding Application methods for debugging and testing.
 */
public final class BakingApplication extends Application {

  public static boolean isInternetAvailable(Context context) {
    ConnectivityManager mConMgr =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    return mConMgr != null
        && mConMgr.getActiveNetworkInfo() != null
        && mConMgr.getActiveNetworkInfo().isAvailable()
        && mConMgr.getActiveNetworkInfo().isConnected();
  }
}
