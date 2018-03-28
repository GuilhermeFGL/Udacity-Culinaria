package com.guilhermefgl.icook.views.details;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ItemDetailsStepBinding;
import com.guilhermefgl.icook.models.Step;
import com.guilhermefgl.icook.viewmodels.StepViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final List<Step> mSptes;
    private final WeakReference<EventHandler> mEventHandler;

    StepAdapter(List<Step> steps, EventHandler eventHandler) {
        mSptes = steps;
        mEventHandler = new WeakReference<>(eventHandler);
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailsStepBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_details_step, parent, false);
        binding.setEventHandler(mEventHandler);
        return new StepViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.setModel(mSptes.get(position));
    }

    @Override
    public int getItemCount() {
        return mSptes.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        private ItemDetailsStepBinding mBinding;

        StepViewHolder(ItemDetailsStepBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            binding.setViewModel(new StepViewModel());
        }

        void setModel(Step model) {
            if (mBinding.getViewModel() != null) {
                mBinding.getViewModel().setModel(model);
                mBinding.executePendingBindings();
            }
        }
    }

    public interface EventHandler {
        void onItemClick(Step step);
    }
}
