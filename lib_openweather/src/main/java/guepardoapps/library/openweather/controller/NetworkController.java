package guepardoapps.library.openweather.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.util.Locale;

import guepardoapps.library.openweather.common.utils.Logger;

public class NetworkController {

    private static final String TAG = NetworkController.class.getSimpleName();
    private Logger _logger;

    private Context _context;

    public NetworkController(@NonNull Context context) {
        _logger = new Logger(TAG);
        _logger.Debug("Created new " + TAG + "...");

        _context = context;
    }

    public boolean IsNetworkAvailable() {
        _logger.Debug("IsNetworkAvailable...");
        ConnectivityManager connectivityManager = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isNetworkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        _logger.Debug(String.format(Locale.getDefault(), "Network is available: %s", isNetworkAvailable));
        return isNetworkAvailable;
    }
}