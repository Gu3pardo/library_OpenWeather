package guepardoapps.library.openweather.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import guepardoapps.library.openweather.common.utils.Logger;

public class NetworkController {
    private static final String TAG = NetworkController.class.getSimpleName();

    private Context _context;

    public NetworkController(@NonNull Context context) {
        _context = context;
    }

    public boolean IsNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Logger.getInstance().Error(TAG, "connectivityManager is null!");
            return false;
        }

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}