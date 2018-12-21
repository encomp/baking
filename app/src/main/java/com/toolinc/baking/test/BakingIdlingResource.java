package com.toolinc.baking.test;

import com.google.common.base.Optional;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.test.espresso.IdlingResource;

/** Specifies an asynchronous operation whose results affect subsequent operations in a UI test. */
public final class BakingIdlingResource implements IdlingResource {

  private final AtomicBoolean mIsIdleNow = new AtomicBoolean(true);
  private volatile Optional<ResourceCallback> mCallback;

  @Override
  public String getName() {
    return this.getClass().getName();
  }

  @Override
  public boolean isIdleNow() {
    return mIsIdleNow.get();
  }

  @Override
  public void registerIdleTransitionCallback(ResourceCallback callback) {
    mCallback = Optional.fromNullable(callback);
  }

  public void idle() {
    setIdleState(false);
  }

  public void completed() {
    setIdleState(true);
  }

  /**
   * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
   *
   * @param isIdleNow false if there are pending operations, true if idle.
   */
  private void setIdleState(boolean isIdleNow) {
    mIsIdleNow.set(isIdleNow);
    if (isIdleNow && mCallback.isPresent()) {
      mCallback.get().onTransitionToIdle();
    }
  }
}
