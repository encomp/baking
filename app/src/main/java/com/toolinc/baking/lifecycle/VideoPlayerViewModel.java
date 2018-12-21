package com.toolinc.baking.lifecycle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/** Defines a new {@link ViewModel} for the {@link VideoPlayerViewModel}. */
public final class VideoPlayerViewModel extends ViewModel {

  private final MutableLiveData<Long> mldPosition = new MutableLiveData<>();
  private final MutableLiveData<String> mldVideoUrl = new MutableLiveData<>();

  public void setPosition(long position) {
    mldPosition.setValue(position);
  }

  public long getPosition() {
    return mldPosition.getValue();
  }

  public void setVideoUrl(String url) {
    mldVideoUrl.setValue(url);
  }

  public String getVideoUrl() {
    return mldVideoUrl.getValue();
  }
}
