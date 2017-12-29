package guepardoapps.library.openweather.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import guepardoapps.library.openweather.common.utils.Logger;

public class ReceiverController implements Serializable {
    private static final long serialVersionUID = 2288241732336744506L;

    private static String TAG = ReceiverController.class.getSimpleName();

    private Context _context;
    private List<BroadcastReceiver> _registeredReceiver;

    public ReceiverController(@NonNull Context context) {
        _context = context;
        _registeredReceiver = new ArrayList<>();
    }

    public void RegisterReceiver(
            @NonNull BroadcastReceiver receiver,
            @NonNull String[] actions) {
        IntentFilter downloadStateFilter = new IntentFilter();
        for (String action : actions) {
            downloadStateFilter.addAction(action);
        }

        unregisterReceiver(receiver);

        _context.registerReceiver(receiver, downloadStateFilter);
        _registeredReceiver.add(receiver);
    }

    private void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        for (int index = 0; index < _registeredReceiver.size(); index++) {
            if (_registeredReceiver.get(index) == receiver) {
                try {
                    _context.unregisterReceiver(receiver);
                    _registeredReceiver.remove(index);
                } catch (Exception exception) {
                    Logger.getInstance().Error(TAG, exception.getMessage());
                }
                break;
            }
        }
    }

    public void Dispose() {
        for (int index = 0; index < _registeredReceiver.size(); index++) {
            try {
                _context.unregisterReceiver(_registeredReceiver.get(index));
                _registeredReceiver.remove(index);
            } catch (Exception exception) {
                Logger.getInstance().Error(TAG, exception.getMessage());
            }
        }
    }
}
