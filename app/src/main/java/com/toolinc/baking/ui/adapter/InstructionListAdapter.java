package com.toolinc.baking.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.toolinc.baking.client.model.Step;
import com.toolinc.baking.databinding.ItemInstructionBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * RecipeListAdapter provides a binding from an {@link ImmutableList} of {@link Step} to the view
 * {@code R.layout.item_instruction} displayed within a RecyclerView.
 */
public final class InstructionListAdapter
    extends RecyclerView.Adapter<InstructionListAdapter.InstructionViewHolder> {

  private static final ImmutableList<Step> EMPTY = ImmutableList.copyOf(Lists.newArrayList());
  private final OnStepSelected onStepSelected;
  private ImmutableList<Step> instructions = EMPTY;

  public InstructionListAdapter(OnStepSelected onStepSelected) {
    this.onStepSelected = checkNotNull(onStepSelected, "OnStepSelected is missing.");
  }

  @NonNull
  @Override
  public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    ItemInstructionBinding itemInstructionBinding =
        ItemInstructionBinding.inflate(inflater, viewGroup, false);
    return new InstructionViewHolder(itemInstructionBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull InstructionViewHolder instructionViewHolder, int position) {
    instructionViewHolder.itemInstructionBinding.setStep(instructions.get(position));
  }

  public void setInstructions(@Nullable ImmutableList<Step> instructions) {
    if (Optional.fromNullable(instructions).isPresent()) {
      this.instructions = instructions;
    } else {
      this.instructions = EMPTY;
    }
  }

  @Override
  public int getItemCount() {
    return instructions.size();
  }

  /** Specifies the behavior upon selection of a {@link Step}. */
  public interface OnStepSelected {

    /** Specifies the step position that has been selected by the user. */
    void onSelected(int position);
  }

  /** Describes a {@link Step} item about its place within the RecyclerView. */
  public final class InstructionViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {

    private ItemInstructionBinding itemInstructionBinding;

    public InstructionViewHolder(@NonNull ItemInstructionBinding itemInstructionBinding) {
      super(itemInstructionBinding.getRoot());
      this.itemInstructionBinding = itemInstructionBinding;
      this.itemInstructionBinding.mcvInstruction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      onStepSelected.onSelected(getAdapterPosition());
    }
  }
}
