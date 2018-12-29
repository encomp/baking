package com.toolinc.baking;

import com.toolinc.baking.client.inject.BakingClientModule;
import com.toolinc.baking.inject.BakingModule;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/** Provides dependency injection for the entire application from the specific modules provided. */
@Component(modules = {AndroidInjectionModule.class, BakingClientModule.class, BakingModule.class})
public interface BakingApplicationComponent extends AndroidInjector<BakingApplication> {}
