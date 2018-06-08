package com.rustfisher.appndkground.frag;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rustfisher.appndkground.R;
import com.rustfisher.appndkground.jni.ContextUtils;

/**
 * Show some functions
 * Created by Rust on 2018/6/8.
 */
public class FunctionListFragment extends Fragment {
    private static final String TAG = "rustAppFuncList";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_func_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View root) {
        Context context = getContext();
        TextView pkgTv = root.findViewById(R.id.pkg_name_tv);
        TextView appSignTv = root.findViewById(R.id.app_sign_tv);
        TextView appPkgNameInListTv = root.findViewById(R.id.pkg_name_in_list);
        pkgTv.setText(ContextUtils.nGetPkgName(context));

        String javaSignature = "Java";
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            javaSignature = packageInfo.signatures[0].toCharsString();
            Log.d(TAG, String.format("packageInfo.signatures.length == %d", packageInfo.signatures.length));
            for (Signature s : packageInfo.signatures) {
                Log.d(TAG, "signatures: " + s.toCharsString());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appSignTv.setText(String.format("native and Java get the same signature: %b",
                javaSignature.equals(ContextUtils.nGetApkSignedInfo(context))));

        appPkgNameInListTv.setText(String.format("Package name in the list: %b",
                ContextUtils.nPkgNameInList(context)));
    }
}
