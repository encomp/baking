package com.toolinc.baking;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.toolinc.baking.offline.BakingDownloadManager;

import java.io.File;

/**
 * Placeholder application to facilitate overriding Application methods for debugging and testing.
 */
public class BakingApplication extends Application {

  private static final String DOWNLOAD_ACTION_FILE = "actions";
  private static final String DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions";
  private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";
  private static final int MAX_SIMULTANEOUS_DOWNLOADS = 2;

  protected String userAgent;

  private File downloadDirectory;
  private Cache downloadCache;
  private DownloadManager downloadManager;
  private BakingDownloadManager downloadTracker;

  @Override
  public void onCreate() {
    super.onCreate();
    userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
  }

  public static boolean isInternetAvailable(Context context) {
    ConnectivityManager mConMgr =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    return mConMgr != null
        && mConMgr.getActiveNetworkInfo() != null
        && mConMgr.getActiveNetworkInfo().isAvailable()
        && mConMgr.getActiveNetworkInfo().isConnected();
  }

  /** Returns a {@link DataSource.Factory}. */
  public DataSource.Factory buildDataSourceFactory() {
    DefaultDataSourceFactory upstreamFactory =
        new DefaultDataSourceFactory(this, buildHttpDataSourceFactory());
    return buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache());
  }

  /** Returns a {@link HttpDataSource.Factory}. */
  private DataSource.Factory buildHttpDataSourceFactory() {
    return new DefaultHttpDataSourceFactory(userAgent);
  }

  public DownloadManager getDownloadManager() {
    initDownloadManager();
    return downloadManager;
  }

  public BakingDownloadManager getBakingDownloadManager() {
    initDownloadManager();
    return downloadTracker;
  }

  private synchronized void initDownloadManager() {
    if (downloadManager == null) {
      DownloaderConstructorHelper downloaderConstructorHelper =
          new DownloaderConstructorHelper(getDownloadCache(), buildHttpDataSourceFactory());
      downloadManager =
          new DownloadManager(
              downloaderConstructorHelper,
              MAX_SIMULTANEOUS_DOWNLOADS,
              DownloadManager.DEFAULT_MIN_RETRY_COUNT,
              new File(getDownloadDirectory(), DOWNLOAD_ACTION_FILE));
      downloadTracker =
          new BakingDownloadManager(
              /* context= */ this,
              buildDataSourceFactory(),
              new File(getDownloadDirectory(), DOWNLOAD_TRACKER_ACTION_FILE));
      downloadManager.addListener(downloadTracker);
    }
  }

  private synchronized Cache getDownloadCache() {
    if (downloadCache == null) {
      File downloadContentDirectory = new File(getDownloadDirectory(), DOWNLOAD_CONTENT_DIRECTORY);
      downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor());
    }
    return downloadCache;
  }

  private File getDownloadDirectory() {
    if (downloadDirectory == null) {
      downloadDirectory = getExternalFilesDir(null);
      if (downloadDirectory == null) {
        downloadDirectory = getFilesDir();
      }
    }
    return downloadDirectory;
  }

  private static CacheDataSourceFactory buildReadOnlyCacheDataSource(
      DefaultDataSourceFactory upstreamFactory, Cache cache) {
    return new CacheDataSourceFactory(
        cache,
        upstreamFactory,
        new FileDataSourceFactory(),
        /* cacheWriteDataSinkFactory= */ null,
        CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
        /* eventListener= */ null);
  }
}
