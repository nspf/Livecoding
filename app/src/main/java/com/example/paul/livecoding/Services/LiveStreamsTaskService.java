package com.example.paul.livecoding.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LiveStreamsTaskService extends GcmTaskService {
    public LiveStreamsTaskService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
