package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.models.api.Meeting;
import com.example.myapplication.models.api.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class RecyclerViewHolderMeetings extends RecyclerView.ViewHolder {

    TextView title, description, owner, data, time;
    public RecyclerViewHolderMeetings(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        owner = itemView.findViewById(R.id.owner);
        data = itemView.findViewById(R.id.data);
        time = itemView.findViewById(R.id.time);
    }
}
public class RecyclerViewAdapterMeetings extends RecyclerView.Adapter<RecyclerViewHolderMeetings>{

    private List<Meeting> meetings = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerViewHolderMeetings onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_meetings_item, parent, false);
        return new RecyclerViewHolderMeetings(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderMeetings holder, int position) {
        Meeting meeting = meetings.get(position);

        holder.title.setText(meeting.getTitle());
        holder.description.setText(meeting.getDescription());


        String credentials = "tatyanin:example_pass";
        String base64Credentials = android.util.Base64.encodeToString(
                credentials.getBytes(),
                android.util.Base64.NO_WRAP
        );
        String authHeader = "Basic " + base64Credentials;

        ApiClient.getApiService()
                .getUserById(meeting.getOrganizerId(), authHeader)
                .enqueue(new Callback<UserResponse>() {
                             @Override
                             public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                 String owner = "default";
                                 owner = response.body().getName();
                                 holder.owner.setText(owner);
                             }

                             @Override
                             public void onFailure(Call<UserResponse> call, Throwable t) {
                             }
                         });

        holder.data.setText(meeting.getDate());
        holder.time.setText(meeting.getStartTime());
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public void addMeetings(List<Meeting> newMeetings) {
        int startPos = meetings.size();
        meetings.addAll(newMeetings);
        notifyItemRangeInserted(startPos, newMeetings.size());
    }

    public void clear() {
        meetings.clear();
        notifyDataSetChanged();
    }
}
