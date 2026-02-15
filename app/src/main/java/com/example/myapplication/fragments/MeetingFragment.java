package com.example.myapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.RecyclerViewAdapterMeetings;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.models.api.Meeting;
import com.example.myapplication.models.api.MeetingsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapterMeetings adapter;

    private List<Meeting> meetingsList = new ArrayList<>();
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private static final int PAGE_SIZE = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meeting_fragment, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_meetings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupRecyclerView();
        loadMeetings(0);

        return view;
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapterMeetings();
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadMeetings(currentPage);
                    }
                }
            }
        });
    }

    private void loadMeetings(int page) {
        if (isLoading) return;

        isLoading = true;

        String credentials = "tatyanin:example_pass";
        String base64Credentials = android.util.Base64.encodeToString(
                credentials.getBytes(),
                android.util.Base64.NO_WRAP
        );
        String authHeader = "Basic " + base64Credentials;

        ApiClient.getApiService()
                .getMeetings(page, PAGE_SIZE, authHeader)
                .enqueue(new Callback<MeetingsResponse>() {
                    @Override
                    public void onResponse(Call<MeetingsResponse> call, Response<MeetingsResponse> response) {
                        if (getActivity() == null || !isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            MeetingsResponse data = response.body();

                            Log.i("err", "onResponse: " + data.getContent().get(0).getTitle());

                            if (page == 0) {
                                meetingsList.clear();
                                adapter.clear();
                            }

                            adapter.addMeetings(data.getContent());

                            currentPage = page + 1;
                            isLastPage = data.isLast();
                        } else {
                            Log.e("err", "Response error: " + response.code());
                            Toast.makeText(getContext(), "Ошибка сервера: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(Call<MeetingsResponse> call, Throwable t) {
                        if (getActivity() == null || !isAdded()) return;

                        isLoading = false;
                        Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("err", "Ошибка: " + t.getMessage());
                    }
                });
    }

}
