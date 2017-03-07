package com.codemaker.reactivesample;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.codemaker.reactivesample.kotlin.KwiCell;
import com.codemaker.reactivesample.kotlin.KwiCells;
import com.codemaker.reactivesample.kotlin.UpdateListener;

import org.jetbrains.annotations.NotNull;

/**
 * Created by kwi on 2017-03-05.
 */

public class ReactiveSampleAdapter extends RecyclerView.Adapter<ReactiveSampleAdapter.ViewHolder>
        implements UpdateListener {

    public ReactiveSampleAdapter() {
        for (int i = 0; i < KwiCells.INSTANCE.getSize(); i++) {
            KwiCells.INSTANCE.getCellById(i).setOnUpdateListener(this);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reactive_sample_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String string = getItem(position).cal();
        string = (TextUtils.isEmpty(string) || KwiCells.ERROR_MSG.equals(string))
                ? "" : ExpressionHelper.convert(string, KwiCells.ERROR_MSG);
        holder.labelText.setText(String.valueOf(getItem(position).getLabel()));
        holder.editText.setText(string);
    }

    @Override
    public int getItemCount() {
        return KwiCells.INSTANCE.getSize();
    }

    public KwiCell getItem(int postion) {
        return KwiCells.INSTANCE.getCellById(postion);
    }

    @Override
    public void onUpdated(@NotNull KwiCell kwiCell) {
        notifyItemChanged(kwiCell.getId());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnFocusChangeListener {

        public TextView labelText;
        public EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            labelText = (TextView) itemView.findViewById(R.id.text_view_label);
            editText = (EditText) itemView.findViewById(R.id.edit_text_cell);
            editText.setOnFocusChangeListener(this);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if(v instanceof TextView) {
                final TextView textView = (TextView) v;

                if (!hasFocus) {
                    final KwiCell kwiCell = getItem(getAdapterPosition());
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            kwiCell.setCellText(textView.getText().toString().toUpperCase());
                        }
                    });
                } else {
                    textView.setText(getItem(getAdapterPosition()).getExpression());
                    textView.requestFocus();
                }
            }
        }
    }
}
