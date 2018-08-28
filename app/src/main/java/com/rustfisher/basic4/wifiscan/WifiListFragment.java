package com.rustfisher.basic4.wifiscan;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.rustfisher.basic4.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * WiFi list
 * Created by Rust on 2018/5/19.
 */
public class WifiListFragment extends Fragment {
    private static final String TAG = "WifiListFragment";
    private static final String LOG_PRE = "[frag wifi]";
    public static final String F_TAG = "f_tag_Wifi_ListFragment";
    private static final String UPGRADE_FIRMWARE_SP_NAME = "upgrade_firmware_sp";
    private static final String K_LAST_WIFI_PREFIX = "key_last_wifi_prefix";
    private static final int WIFI_LIST_SPAN_COUNT = 2;
    private Button mRefreshWiFiBtn;
    EditText mWiFiPrefixEt;
    private String mWiFiPrefix = "";
    private WifiManager mWifiManager;
    private WiFiReAdapter mWiFiReAdapter;
    private boolean mUserScanWiFi = false;
    private TextView mChosenInfoTv;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static WifiListFragment newFrag() {
        return new WifiListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifiManager = (WifiManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        getActivity().registerReceiver(mBroadcastReceiver, makeIf());
        Log.d(TAG, "onCreate: 注册广播");
    }

    @NonNull
    public IntentFilter makeIf() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        return intentFilter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_numbers_upgrade, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshWiFiBtn = view.findViewById(R.id.refresh_wifi_btn);
        mWiFiPrefixEt = view.findViewById(R.id.wifi_prefix_et);
        mChosenInfoTv = view.findViewById(R.id.chosen_info_tv);
        view.findViewById(R.id.top_back_tv).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.choose_all_btn).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.choose_none_btn).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.choose_revert_btn).setOnClickListener(mOnClickListener);

        mRefreshWiFiBtn.setOnClickListener(mOnClickListener);
        RecyclerView wifiRv = view.findViewById(R.id.wifi_rv);
        mWiFiReAdapter = new WiFiReAdapter();
        wifiRv.setAdapter(mWiFiReAdapter);
        wifiRv.setLayoutManager(new GridLayoutManager(getActivity(), WIFI_LIST_SPAN_COUNT));
        mWiFiReAdapter.setOnItemClickListener(mOnItemClickListener);
        mWiFiPrefixEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mWiFiPrefix = s.toString().trim();
                getActivity().getSharedPreferences(UPGRADE_FIRMWARE_SP_NAME, Context.MODE_PRIVATE)
                        .edit().putString(K_LAST_WIFI_PREFIX, mWiFiPrefix).apply();
            }
        });
        String prefix = getActivity().getSharedPreferences(UPGRADE_FIRMWARE_SP_NAME, Context.MODE_PRIVATE).getString(K_LAST_WIFI_PREFIX, "");
        mWiFiPrefixEt.setText(prefix);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
        Log.d(TAG, "onDestroy: 销毁广播");
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_back_tv:
                    getActivity().finish();
                    break;
                case R.id.refresh_wifi_btn:
                    startWiFiScan();
                    break;
                case R.id.choose_all_btn:
                    mWiFiReAdapter.chooseAll();
                    updateChosenNoteUI();
                    break;
                case R.id.choose_none_btn:
                    mWiFiReAdapter.chooseNone();
                    updateChosenNoteUI();
                    break;
                case R.id.choose_revert_btn:
                    mWiFiReAdapter.chooseRevert();
                    updateChosenNoteUI();
                    break;
            }
        }
    };

    public void startWiFiScan() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        mWifiManager.startScan();
        mRefreshWiFiBtn.setText("扫描中..");
        mUserScanWiFi = true;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                if (mUserScanWiFi) {
                    mUserScanWiFi = false;
                    List<ScanResult> scanResults = mWifiManager.getScanResults();
                    mWiFiReAdapter.clearList();
                    String lowerPrefix = mWiFiPrefix.toLowerCase().trim();
//                    Log.d(TAG, "lowerPrefix = " + lowerPrefix);
                    mRefreshWiFiBtn.setText("刷新");
                    for (ScanResult s : scanResults) {
//                        Log.d(TAG, "onReceive: " + s);
                        if (!TextUtils.isEmpty(s.SSID)) {
                            if (TextUtils.isEmpty(lowerPrefix) || s.SSID.toLowerCase().startsWith(lowerPrefix)) {
                                mWiFiReAdapter.addOrUpdateWiFiScanResult(s);
                            }
                        }
                    }
                    mWiFiReAdapter.notifyDataSetChanged();
                    updateChosenNoteUI();
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                final WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                final String logPrefix = "[WiFi状态] - ";
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    Log.d(TAG, logPrefix + "连接已断开");
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    Log.d(TAG, logPrefix + "已连接到网络:" + wifiInfo.getSSID()); // 这里SSID有引号
                } else if (info.getState().equals(NetworkInfo.State.CONNECTING)) {
                    Log.d(TAG, logPrefix + "正在连接:" + wifiInfo.getSSID());
                } else if (info.getState().equals(NetworkInfo.State.DISCONNECTING)) {
                    Log.d(TAG, logPrefix + "正在断开:" + wifiInfo.getSSID());
                }
            }
        }
    };

    public void updateChosenNoteUI() {
        mChosenInfoTv.setText(String.format(Locale.CHINA, "已选:%d项", mWiFiReAdapter.getChosenWiFi().size()));
    }

    private WiFiAdapterOnItemClickListener mOnItemClickListener = new WiFiAdapterOnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            mWiFiReAdapter.revertChosen(pos);
            updateChosenNoteUI();
        }
    };

    private class WiFiReAdapter extends RecyclerView.Adapter<WVH> {
        private WiFiAdapterOnItemClickListener onItemClickListener;
        private List<WiFiItem> wifiList;

        WiFiReAdapter() {
            wifiList = new ArrayList<>(20);
        }

        void setOnItemClickListener(WiFiAdapterOnItemClickListener listener) {
            this.onItemClickListener = listener;
        }

        void clearList() {
            wifiList.clear();
        }

        void addOrUpdateWiFiScanResult(ScanResult scanResult) {
            boolean has = false;
            for (WiFiItem item : wifiList) {
                if (item.scanResult.SSID.equals(scanResult.SSID)) {
                    has = true;
                    item.scanResult = scanResult;
                }
            }
            if (!has) {
                wifiList.add(new WiFiItem(scanResult));
            }
        }

        void chooseAll() {
            for (WiFiItem i : wifiList) {
                i.chosen = true;
            }
            notifyDataSetChanged();
        }

        void chooseNone() {
            for (WiFiItem i : wifiList) {
                i.chosen = false;
            }
            notifyDataSetChanged();
        }

        void chooseRevert() {
            for (WiFiItem i : wifiList) {
                i.chosen = !i.chosen;
            }
            notifyDataSetChanged();
        }

        void revertChosen(final int pos) {
            WiFiItem item = wifiList.get(pos);
            item.chosen = !item.chosen;
            notifyItemChanged(pos);
        }

        List<ScanResult> getChosenWiFi() {
            List<ScanResult> res = new ArrayList<>();
            for (WiFiItem item : wifiList) {
                if (item.chosen) {
                    res.add(item.scanResult);
                }
            }
            return res;
        }

        @Override
        public WVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.w_item_wifi, parent, false));
        }

        @Override
        public void onBindViewHolder(WVH holder, int position) {
            final int pos = position;
            WiFiItem item = wifiList.get(pos);
            holder.ssidTv.setText(item.scanResult.SSID);
            holder.chosenCb.setChecked(item.chosen);
            holder.itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(pos);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return wifiList.size();
        }
    }

    static class WiFiItem {
        boolean chosen = false;
        ScanResult scanResult;

        WiFiItem(ScanResult s) {
            scanResult = s;
        }
    }

    static class WVH extends RecyclerView.ViewHolder {
        View itemRoot;
        CheckBox chosenCb;
        TextView ssidTv;
        TextView noteTv;

        WVH(View itemView) {
            super(itemView);
            itemRoot = itemView;
            chosenCb = itemView.findViewById(R.id.chosen_cb);
            ssidTv = itemView.findViewById(R.id.ssid_tv);
            noteTv = itemView.findViewById(R.id.note_tv);
        }
    }

    public interface WiFiAdapterOnItemClickListener {
        void onItemClick(final int pos);
    }
}
