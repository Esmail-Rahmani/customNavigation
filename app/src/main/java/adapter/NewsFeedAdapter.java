package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customnavigation.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.NewsFeed;
import model.Teacher;
import utils.Constant;
import utils.ExtraMethods;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.MViewHolder> {
    Context context;
    List<NewsFeed> newFeedList;

    public NewsFeedAdapter(Context context, List<NewsFeed> news) {
        this.context = context;
        this.newFeedList = news;
    }


    @NonNull
    @Override
    public NewsFeedAdapter.MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_row, parent, false);
        return new NewsFeedAdapter.MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedAdapter.MViewHolder holder, int position) {

        String T =newFeedList.get(position).getTime();
        Log.d("test123", "onBindViewHolder: "+T);
        Date temp = null;
        try {
            temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
                    .parse(T+".000000");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("test123", "onBindViewHolder: "+e);
        }
        long mills = temp.getTime();
        Log.d("test123", "onBindViewHolder: "+mills);

        String t = ExtraMethods.getTimeAgo(mills);
        Log.d("test123", "onBindViewHolder: "+t);

        holder.title.setText(newFeedList.get(position).getTitle());
        holder.time.setText(t);
        holder.description.setText(newFeedList.get(position).getDescription());

        Log.d("test123", "onBindViewHolder: "+newFeedList.get(position).getMainImage());
        Picasso.get()
                .load(Constant.IMAGE_URL + newFeedList.get(position).getProfImage())
                .placeholder(R.drawable.profile_error)
                .into(holder.profImage);
        if (!newFeedList.get(position).getMainImage().equals("null")){
            Picasso.get()
                    .load(Constant.IMAGE_URL + newFeedList.get(position).getMainImage())
                    .placeholder(R.drawable.profile_error)
                    .into(holder.mainImage);
        }else{
            holder.mainImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return newFeedList.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        TextView title, time, description;
        ImageView profImage, mainImage;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.profile_title_feed_news);
            time = itemView.findViewById(R.id.profile_time_feed_news);
            description = itemView.findViewById(R.id.profile_description_feed_news);
            mainImage = itemView.findViewById(R.id.profile_main_image_feed_news);
            profImage = itemView.findViewById(R.id.profile_image_feed_news);
        }
    }
}
