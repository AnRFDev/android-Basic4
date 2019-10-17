package com.rustfisher.appdowloadsample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rustfisher.appdowloadsample.R;
import com.rustfisher.appdowloadsample.download.ControlCallBack;
import com.rustfisher.appdowloadsample.download.DownloadCenter;
import com.rustfisher.appdowloadsample.download.DownloadCenterListener;
import com.rustfisher.appdowloadsample.download.DownloadTaskState;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载中 界面
 * Created on 2019-10-17
 */
public class DownloadingFrag extends Fragment {

    private TaskAdapter taskAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_downloading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.download_task_re);
        taskAdapter = new TaskAdapter();
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        DownloadCenter.getInstance().addListener(mDownloadCenterListener);
    }

    @Override
    public void onDestroy() {
        DownloadCenter.getInstance().removeListener(mDownloadCenterListener);
        super.onDestroy();
    }

    private DownloadCenterListener mDownloadCenterListener = new DownloadCenterListener() {
        @Override
        public void onStart(final ControlCallBack callBack) {
            super.onStart(callBack);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    taskAdapter.addTask(callBack);
                }
            });
        }

        @Override
        public void onSuccess(DownloadTaskState state, final String url) {
            super.onSuccess(state, url);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    taskAdapter.updateSuccess(url);
                }
            });
        }

        @Override
        public void onError(DownloadTaskState state, final String url, Throwable e) {
            super.onError(state, url, e);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    taskAdapter.updateError(url);
                }
            });
        }

        @Override
        public void onProgress(final String url, final long bytesRead, final long contentLength, final boolean done) {
            super.onProgress(url, bytesRead, contentLength, done);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    taskAdapter.updateProgress(url, bytesRead, contentLength, done);
                }
            });
        }

        @Override
        public void onDeleted(final String url) {
            super.onDeleted(url);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    taskAdapter.updateDeleted(url);
                }
            });
        }
    };

    static class VH extends RecyclerView.ViewHolder {
        public View root;
        public TextView urlTv;
        public TextView filenameTv;
        ProgressBar pb;

        public VH(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            filenameTv = itemView.findViewById(R.id.filename_tv);
            urlTv = itemView.findViewById(R.id.url_tv);
            pb = itemView.findViewById(R.id.pb);
        }
    }

    class TaskAdapter extends RecyclerView.Adapter<VH> {
        private List<ControlCallBack> callBackList;

        public TaskAdapter() {
            callBackList = new ArrayList<>();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_task, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            ControlCallBack callBack = callBackList.get(position);
            holder.urlTv.setText(callBack.getUrl());
            holder.filenameTv.setText(callBack.getTargetFile().getName());
            holder.pb.setVisibility(callBack.isDownloading() ? View.VISIBLE : View.INVISIBLE);
            if (callBack.getProgressTotal() > 0 && callBack.getProgressCurrent() >= 0) {
                double progress = callBack.getProgressCurrent() * 1.0 / callBack.getProgressTotal();
                holder.pb.setMax(1000);
                holder.pb.setProgress((int) (1000 * progress));
            } else {
                holder.pb.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return callBackList.size();
        }

        public void addTask(ControlCallBack callBack) {
            callBackList.add(callBack);
            notifyDataSetChanged();
        }

        public void updateSuccess(String url) {
            ControlCallBack callBack = getTaskByUrl(url);
            notifyDataSetChanged();
        }

        public void updateError(String url) {
            ControlCallBack callBack = getTaskByUrl(url);

            notifyDataSetChanged();
        }

        public void updateProgress(String url, long bytesRead, long contentLength, boolean done) {
            ControlCallBack callBack = getTaskByUrl(url);
            if (callBack != null) {
                callBack.setProgressCurrent(bytesRead);
                callBack.setProgressTotal(contentLength);
            }
            notifyDataSetChanged();
        }

        public void updateDeleted(String url) {
            ControlCallBack callBack = getTaskByUrl(url);
            callBackList.remove(callBack);
            notifyDataSetChanged();
        }

        private ControlCallBack getTaskByUrl(String url) {
            for (ControlCallBack c : callBackList) {
                if (c.getUrl().equals(url)) {
                    return c;
                }
            }
            return null;
        }
    }

}
