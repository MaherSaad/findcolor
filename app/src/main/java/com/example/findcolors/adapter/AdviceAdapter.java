package com.example.findcolors.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.findcolors.R;
import com.example.findcolors.util.AdviceModel;

import java.util.ArrayList;
import java.util.List;


public class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.Advice> {

    List<AdviceModel> data;
    public AdviceAdapter() {
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public Advice onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.advice_item,viewGroup,false);
        return new Advice(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Advice advice, int i) {
        AdviceModel model = data.get(i);
        advice.question.setText(model.getQuestion());
        advice.answer.setText(model.getAnswer());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Advice extends RecyclerView.ViewHolder{
        TextView question,answer;
        public Advice(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
        }
    }


    public void loadItems(List<AdviceModel> list){
        this.data = list;
        notifyDataSetChanged();
    }
}
