package com.vishwanathlokare.VendorHelper.PdfUtills;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class UpdateHelper {

    public static String KEY_UPDATE_ENABLED = "is_update";
    public static String KEY_UPDATE_VERSION = "version";
    public static String KEY_UPDATE_URL = "update_url";

    public interface OnUpdateCheckListener{
        void onUpdateCheckListener(String urlApp);
    }

    public static Builder with(Context context){
        return new Builder(context);
    }
    private Context context;

    public UpdateHelper(Context context, OnUpdateCheckListener onUpdateCheckListener) {
        this.context = context;
        this.onUpdateCheckListener = onUpdateCheckListener;
    }

    public void check(){
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        if(remoteConfig.getBoolean(KEY_UPDATE_ENABLED)){
            String currentVersion = remoteConfig.getString(KEY_UPDATE_VERSION);
            String appVersion = getAppVersion(context);
            String update_url = remoteConfig.getString(KEY_UPDATE_URL);
            if(!TextUtils.equals(currentVersion,appVersion) && onUpdateCheckListener != null){
                onUpdateCheckListener.onUpdateCheckListener(update_url);
            }
        }
    }

    private String getAppVersion(Context context) {
        String result= "";
        try{
            result = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            result = result.replaceAll("[a-zA-Z]|-","");
        }
        catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }

    private  OnUpdateCheckListener onUpdateCheckListener;


    public static class Builder{
        private Context context;
        private  OnUpdateCheckListener onUpdateCheckListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, OnUpdateCheckListener onUpdateCheckListener) {
            this.context = context;
            this.onUpdateCheckListener = onUpdateCheckListener;
        }
        public Builder OnUpdateCheck(OnUpdateCheckListener onUpdateCheckListener){
            this.onUpdateCheckListener = onUpdateCheckListener;
            return this;
        }
        public UpdateHelper build(){
            return new UpdateHelper(context,onUpdateCheckListener);
        }
        public UpdateHelper check(){
            UpdateHelper updateHelper = build();
            updateHelper.check();
            return updateHelper;
        }

    }

}
