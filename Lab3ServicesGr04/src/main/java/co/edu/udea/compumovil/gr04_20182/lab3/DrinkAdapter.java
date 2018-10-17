package co.edu.udea.compumovil.gr04_20182.lab3;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> implements View.OnClickListener{
    private ArrayList<DrinkPojo> drinkList;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public static class DrinkViewHolder extends RecyclerView.ViewHolder{
        public TextView name,price;
        public ImageView drinkImage, favorite;

        public DrinkViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.drink_name);
            price = view.findViewById(R.id.drink_price);
            drinkImage = view.findViewById(R.id.drink_image);
            favorite = view.findViewById(R.id.image_favorite);
        }
    }

    public DrinkAdapter(ArrayList<DrinkPojo> drinkList){
        this.drinkList = drinkList;
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkAdapter.DrinkViewHolder holder, int position) {
        holder.name.setText(drinkList.get(position).getName());
        holder.price.setText(drinkList.get(position).getPrice());
        Picasso.get().load(drinkList.get(position).getImageUri()).error(R.drawable.coke).into(holder.drinkImage);
        if(drinkList.get(position).isFavorite()){
            holder.favorite.setImageResource(R.drawable.ic_favorite_full);
        }else{
            holder.favorite.setImageResource(R.drawable.ic_favorite);
        }
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    @NonNull
    @Override
    public DrinkAdapter.DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_drink_item, parent, false);

        view.setOnClickListener(this);

        return new DrinkViewHolder(view);
    }
}
