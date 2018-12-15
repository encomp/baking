package com.toolinc.baking.ui.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.toolinc.baking.client.model.Step;
import com.toolinc.baking.databinding.ItemInstructionBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecipeListAdapter provides a binding from an {@link ImmutableList} of {@link Step} to the view
 * {@code R.layout.item_instruction} displayed within a RecyclerView.
 */
public class InstructionListAdapter
    extends RecyclerView.Adapter<InstructionListAdapter.InstructionViewHolder> {

  private static final ImmutableList<Step> EMPTY = ImmutableList.copyOf(Lists.newArrayList());
  private ImmutableList<Step> instructions = EMPTY;

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

  /** Describes a {@link Step} item about its place within the RecyclerView. */
  public final class InstructionViewHolder extends RecyclerView.ViewHolder {

    private ItemInstructionBinding itemInstructionBinding;

    public InstructionViewHolder(@NonNull ItemInstructionBinding itemInstructionBinding) {
      super(itemInstructionBinding.getRoot());
      this.itemInstructionBinding = itemInstructionBinding;
    }
  }
}
