package co.edu.udea.compumovil.gr04_20182.lab2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>{
    private ArrayList<DrinkPojo> drinkList;

    public static class DrinkViewHolder extends RecyclerView.ViewHolder{
        public TextView name,price;
        public ImageView drinkImage;

        public DrinkViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.drink_name);
            price = view.findViewById(R.id.drink_price);
            drinkImage = view.findViewById(R.id.drink_image);
        }
    }

    public DrinkAdapter(ArrayList<DrinkPojo> drinkList){
        this.drinkList = drinkList;
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkAdapter.DrinkViewHolder holder, int position) {
        holder.name.setText(drinkList.get(position).getName());
        holder.price.setText(drinkList.get(position).getPrice());
        holder.drinkImage.setImageURI(drinkList.get(position).getImageUri());
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    @NonNull
    @Override
    public DrinkAdapter.DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_drink_item, parent, false);
        return new DrinkViewHolder(view);
    }
}
