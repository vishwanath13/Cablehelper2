package com.vishwanathlokare.VendorHelper;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.vishwanathlokare.VendorHelper.PdfUtills.UpdateHelper;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        Map<String, Object> defaultValues = new HashMap<>();
        defaultValues.put(UpdateHelper.KEY_UPDATE_ENABLED,false);
        defaultValues.put(UpdateHelper.KEY_UPDATE_URL,"MyappUrl");
        defaultValues.put(UpdateHelper.KEY_UPDATE_VERSION,"1.0");
        remoteConfig.setDefaultsAsync(defaultValues);
        remoteConfig.fetch(5).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    remoteConfig.fetchAndActivate();
                }
            }
        });

    }
}



