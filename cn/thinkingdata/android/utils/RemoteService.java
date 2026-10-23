package cn.thinkingdata.android.utils;

import javax.net.ssl.SSLSocketFactory;

/* JADX INFO: loaded from: classes.dex */
public interface RemoteService {

    public static class ServiceUnavailableException extends Exception {
        public ServiceUnavailableException(String str) {
            super(str);
        }
    }

    String performRequest(String str, String str2, boolean z, SSLSocketFactory sSLSocketFactory);
}
