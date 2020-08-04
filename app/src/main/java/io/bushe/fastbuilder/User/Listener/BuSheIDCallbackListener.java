package io.bushe.fastbuilder.User.Listener;

import io.bushe.fastbuilder.User.BuSheID;

public interface BuSheIDCallbackListener {
    void onFinish(BuSheID buSheID);

    void onError(Exception e);

    void onFBProFinish(String UserDataJSON);
}
