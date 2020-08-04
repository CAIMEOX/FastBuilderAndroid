package io.bushe.fastbuilder.Util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OKHttp3Util {
    public static void sendOkHttpRequest(final String address, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
