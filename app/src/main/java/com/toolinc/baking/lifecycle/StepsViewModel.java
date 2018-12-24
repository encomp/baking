package com.toolinc.baking.lifecycle;

import com.google.common.collect.ImmutableList;
import com.toolinc.baking.client.model.Step;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel is a class that is responsible for preparing and managing the data to display the video
 * and instructions.
 */
public final class StepsViewModel extends ViewModel {

  private final MutableLiveData<ImmutableList<Step>> stepsLiveData = new MutableLiveData<>();
  private final MutableLiveData<Step> stepLiveData = new MutableLiveData<>();
  private final MutableLiveData<Integer> stepPositionLiveData = new MutableLiveData<>();

  public void setSteps(ImmutableList<Step> steps, int position) {
    stepsLiveData.setValue(steps);
    stepPositionLiveData.setValue(position);
    stepLiveData.setValue(steps.get(position));
  }

  public void nextStep() {
    if (stepPositionLiveData.getValue() + 1 < stepsLiveData.getValue().size()) {
      stepPositionLiveData.setValue(stepPositionLiveData.getValue() + 1);
      stepLiveData.setValue(stepsLiveData.getValue().get(stepPositionLiveData.getValue()));
    }
  }

  public void priorStep() {
    if (stepPositionLiveData.getValue() - 1 >= 0) {
      stepPositionLiveData.setValue(stepPositionLiveData.getValue() - 1);
      stepLiveData.setValue(stepsLiveData.getValue().get(stepPositionLiveData.getValue()));
    }
  }

  public Step getCurrentStep() {
    return stepLiveData.getValue();
  }

  public int getCurrentStepIndex() {
    return stepPositionLiveData.getValue();
  }
}
