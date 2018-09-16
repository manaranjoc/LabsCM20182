package co.edu.udea.compumovil.gr04_20182.lab2;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    private ArrayList<DishPojo> dishList;

    public static class DishViewHolder extends RecyclerView.ViewHolder{
        public TextView name, type, price, time;
        public ImageView dishImage;

        public DishViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.dish_name);
            type = view.findViewById(R.id.dish_type);
            price = view.findViewById(R.id.dish_price);
            time = view.findViewById(R.id.dish_time);
            dishImage = view.findViewById(R.id.dish_image);
        }
    }

    public DishAdapter(ArrayList<DishPojo> dishList){
        this.dishList = dishList;
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        holder.name.setText(dishList.get(position).getName());
        holder.type.setText(dishList.get(position).getType());
        holder.price.setText(dishList.get(position).getPrice());
        holder.time.setText(dishList.get(position).getTime());
        holder.dishImage.setImageURI(dishList.get(position).getImageUri());
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dish_item,null,false);
        return new DishViewHolder(view);
    }
}
