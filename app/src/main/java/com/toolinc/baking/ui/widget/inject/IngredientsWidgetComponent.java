package com.toolinc.baking.ui.widget.inject;

import com.toolinc.baking.client.inject.BakingClientModule;
import com.toolinc.baking.ui.widget.IngredientsWidgetProvider;

import dagger.Component;

/**
 * Provides an injection point for {@link IngredientsWidgetProvider} of the module on {@link
 * BakingClientModule}.
 */
@Component(modules = {BakingClientModule.class})
public interface IngredientsWidgetComponent {

  /**
   * This method allows the injection of certain graph of objects into an instance of {@link
   * IngredientsWidgetProvider}.
   *
   * @param widgetProvider specifies the provider in which a specific graph of objects should be
   *     injected.
   */
  void inject(IngredientsWidgetProvider widgetProvider);
}
