package com.example.trafficroadworks.alladapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trafficroadworks.R;
import com.example.trafficroadworks.allinterfaces.RoadWorkListenerAdapterInterface;
import com.example.trafficroadworks.animation_in_view.view_animation;
import com.example.trafficroadworks.universalmodel.DataModel;

import java.util.List;


public class ItemViewRoadWorksAdapter extends RecyclerView.Adapter<ItemViewRoadWorksAdapter.ViewHolder> {
    //declaring activity context
   Context root;

   //declaring list items data variable for showing in recycler view
   List<DataModel>roadWorksClarifyItems;

   //declaring incident interfaces
   RoadWorkListenerAdapterInterface RoadWorksListenerAdapterInterface;

    //declaring animation element for sowing in recycler view
    boolean on_connect = true;

    //for data get from fragment to show the view

    public ItemViewRoadWorksAdapter(Context root, List<DataModel> roadWorksClarifyItems, RoadWorkListenerAdapterInterface roadWorksListenerAdapterInterface) {
        this.root = root;
        this.roadWorksClarifyItems = roadWorksClarifyItems;
        RoadWorksListenerAdapterInterface = roadWorksListenerAdapterInterface;
    }

    //for declaring custom view layout for each item

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event_view, parent, false);
        return new ViewHolder(view);
    }

    //for binding each view in each recycler item

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //setting the name of the event in item
        holder.roadworks_event_name.setText(roadWorksClarifyItems.get(position).getRoad_title_name());

        //setting the time of the event in the item
        holder.roadworks_event_day.setText(roadWorksClarifyItems.get(position).getRoad_publication_date());

        //setting the image of the event in the item
        holder.imageView.setImageResource(R.drawable.roadworks);

        //setting the animation to the recycler view
        view_animation.FromRightToLeft(holder.itemView, position, on_connect);
    }

    //for attach the animation to the item view

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_connect = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        super.onAttachedToRecyclerView(recyclerView);
    }


    //view size of items to the recycler view

    @Override
    public int getItemCount() {
        return roadWorksClarifyItems.size();
    }

    //for initializing custom item and show it to view

    public class ViewHolder extends RecyclerView.ViewHolder{

        //declaring all the variable which attach in the recycler view
        private TextView roadworks_event_name;
        private TextView roadworks_event_day;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //initialization of the item
            roadworks_event_name = itemView.findViewById(R.id.event_name);
            roadworks_event_day = itemView.findViewById(R.id.event_date);
            imageView=itemView.findViewById(R.id.traffic_Icon);

            //for item click listener to go through the second activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RoadWorksListenerAdapterInterface.onClickListenerForRoadWorks(roadWorksClarifyItems.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}
