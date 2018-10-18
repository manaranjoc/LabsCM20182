package co.edu.udea.compumovil.gr04_20182.lab3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> implements View.OnClickListener{
    private ArrayList<DishPojo> dishList;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder{
        public TextView name, type, price, time;
        public ImageView dishImage, favorite;

        public DishViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.dish_name);
            type = view.findViewById(R.id.dish_type);
            price = view.findViewById(R.id.dish_price);
            time = view.findViewById(R.id.dish_time);
            dishImage = view.findViewById(R.id.dish_image);
            favorite = view.findViewById(R.id.image_favorite);
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
        Picasso.get().load(dishList.get(position).getImageUri()).placeholder(R.drawable.pizza_peperonni).error(R.drawable.pizza_peperonni).into(holder.dishImage);

        if(dishList.get(position).isFavorite()){
            holder.favorite.setImageResource(R.drawable.ic_favorite_full);
        }else{
            holder.favorite.setImageResource(R.drawable.ic_favorite);
        }
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dish_item,parent,false);

        view.setOnClickListener(this);

        return new DishViewHolder(view);
    }
}
