package cn.thinkingdata.android.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

/* JADX INFO: loaded from: classes.dex */
public class HttpService implements RemoteService {
    private static final String TAG = "ThinkingAnalytics.HttpService";

    private String encodeData(String str) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(str.getBytes().length);
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gZIPOutputStream.write(str.getBytes());
        gZIPOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return new String(Base64Coder.encode(byteArray));
    }

    /* JADX WARN: Code duplicated, block: B:109:0x0162  */
    /* JADX WARN: Code duplicated, block: B:119:0x0153 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:121:0x014e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:126:0x015d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:130:0x0158 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:13:0x0026 A[Catch: all -> 0x001f, IOException -> 0x0021, TRY_LEAVE, TryCatch #14 {IOException -> 0x0021, blocks: (B:5:0x0014, B:7:0x0018, B:13:0x0026, B:15:0x002c, B:19:0x0035, B:21:0x0040, B:28:0x0053, B:26:0x004c, B:82:0x011d, B:83:0x0126, B:84:0x0127, B:86:0x012b, B:87:0x012e), top: B:125:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:147:0x00b3 A[EDGE_INSN: B:147:0x00b3->B:46:0x00b3 BREAK  A[LOOP:0: B:39:0x00a0->B:41:0x00a6], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:148:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:18:0x0033  */
    /* JADX WARN: Code duplicated, block: B:25:0x004a  */
    /* JADX WARN: Code duplicated, block: B:36:0x008d A[Catch: all -> 0x00da, IOException -> 0x00de, TRY_LEAVE, TryCatch #19 {IOException -> 0x00de, all -> 0x00da, blocks: (B:32:0x0074, B:34:0x007a, B:36:0x008d, B:63:0x00e3, B:64:0x00f4), top: B:144:0x0074 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x00a6 A[Catch: all -> 0x00aa, IOException -> 0x00b1, LOOP:0: B:39:0x00a0->B:41:0x00a6, LOOP_END, TryCatch #22 {IOException -> 0x00b1, all -> 0x00aa, blocks: (B:38:0x009b, B:39:0x00a0, B:41:0x00a6, B:46:0x00b3), top: B:138:0x009b }] */
    /* JADX WARN: Code duplicated, block: B:63:0x00e3 A[Catch: all -> 0x00da, IOException -> 0x00de, TRY_ENTER, TryCatch #19 {IOException -> 0x00de, all -> 0x00da, blocks: (B:32:0x0074, B:34:0x007a, B:36:0x008d, B:63:0x00e3, B:64:0x00f4), top: B:144:0x0074 }] */
    /* JADX WARN: Code duplicated, block: B:84:0x0127 A[Catch: all -> 0x001f, IOException -> 0x0021, TRY_LEAVE, TryCatch #14 {IOException -> 0x0021, blocks: (B:5:0x0014, B:7:0x0018, B:13:0x0026, B:15:0x002c, B:19:0x0035, B:21:0x0040, B:28:0x0053, B:26:0x004c, B:82:0x011d, B:83:0x0126, B:84:0x0127, B:86:0x012b, B:87:0x012e), top: B:125:0x0014 }] */
    /* JADX WARN: Instruction removed from duplicated block: B:63:0x00e3, please report this as an issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v9 */
    @Override // cn.thinkingdata.android.utils.RemoteService
    public String performRequest(String str, String str2, boolean z, SSLSocketFactory sSLSocketFactory) throws Throwable {
        Throwable th;
        InputStream inputStream;
        OutputStream outputStream;
        HttpURLConnection httpURLConnection;
        ?? r0;
        IOException e;
        InputStream inputStream2;
        InputStream inputStream3;
        Object obj;
        Object obj2;
        InputStream inputStream4;
        Object obj3;
        Object obj4;
        BufferedOutputStream bufferedOutputStream;
        BufferedOutputStream bufferedOutputStream2;
        int responseCode;
        BufferedReader bufferedReader;
        StringBuffer stringBuffer;
        String line;
        ?? r3 = 0;
        outputStream = null;
        outputStream = null;
        r3 = 0;
        r3 = 0;
        OutputStream outputStream2 = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            if (sSLSocketFactory == null) {
                if (str2 != null) {
                    throw new InvalidParameterException("Content is null");
                }
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
                if (z) {
                    httpURLConnection.setRequestProperty(HTTP.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestProperty("charset", "utf-8");
                } else {
                    httpURLConnection.setRequestProperty(HTTP.CONTENT_TYPE, "text/plain");
                    str2 = encodeData(str2);
                }
                httpURLConnection.setFixedLengthStreamingMode(str2.getBytes("UTF-8").length);
                outputStream = httpURLConnection.getOutputStream();
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                bufferedOutputStream.write(str2.getBytes("UTF-8"));
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                outputStream.close();
                responseCode = httpURLConnection.getResponseCode();
                TDLog.d(TAG, "ret_code:" + responseCode);
                if (responseCode == 200) {
                    throw new RemoteService.ServiceUnavailableException("Service unavailable with response code: " + responseCode);
                }
                inputStream2 = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream2));
                stringBuffer = new StringBuffer();
                while (true) {
                    line = bufferedReader.readLine();
                    if (line != null) {
                        break;
                        break;
                    }
                    stringBuffer.append(line);
                }
                inputStream2.close();
                bufferedReader.close();
                String string = stringBuffer.toString();
                inputStream2.close();
                bufferedReader.close();
                httpURLConnection.disconnect();
                return string;
            }
            try {
                try {
                    if (httpURLConnection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sSLSocketFactory);
                    }
                    if (str2 != null) {
                        throw new InvalidParameterException("Content is null");
                    }
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
                    if (z) {
                        httpURLConnection.setRequestProperty(HTTP.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);
                        httpURLConnection.setUseCaches(false);
                        httpURLConnection.setRequestProperty("charset", "utf-8");
                    } else {
                        httpURLConnection.setRequestProperty(HTTP.CONTENT_TYPE, "text/plain");
                        try {
                            str2 = encodeData(str2);
                        } catch (IOException e2) {
                            throw new InvalidParameterException(e2.getMessage());
                        }
                    }
                    httpURLConnection.setFixedLengthStreamingMode(str2.getBytes("UTF-8").length);
                    outputStream = httpURLConnection.getOutputStream();
                    try {
                        bufferedOutputStream = new BufferedOutputStream(outputStream);
                        try {
                            bufferedOutputStream.write(str2.getBytes("UTF-8"));
                            bufferedOutputStream.flush();
                            bufferedOutputStream.close();
                            try {
                                outputStream.close();
                                try {
                                    responseCode = httpURLConnection.getResponseCode();
                                    TDLog.d(TAG, "ret_code:" + responseCode);
                                    if (responseCode == 200) {
                                        throw new RemoteService.ServiceUnavailableException("Service unavailable with response code: " + responseCode);
                                    }
                                    inputStream2 = httpURLConnection.getInputStream();
                                    try {
                                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream2));
                                        try {
                                            stringBuffer = new StringBuffer();
                                            while (true) {
                                                line = bufferedReader.readLine();
                                                if (line != null) {
                                                    break;
                                                }
                                                stringBuffer.append(line);
                                            }
                                            inputStream2.close();
                                            bufferedReader.close();
                                            String string2 = stringBuffer.toString();
                                            try {
                                                inputStream2.close();
                                            } catch (IOException unused) {
                                            }
                                            try {
                                                bufferedReader.close();
                                            } catch (IOException unused2) {
                                            }
                                            httpURLConnection.disconnect();
                                            return string2;
                                        } catch (IOException e3) {
                                            e = e3;
                                            obj = bufferedReader;
                                            obj4 = null;
                                            try {
                                                throw e;
                                            } catch (Throwable th2) {
                                                Object obj5 = obj4;
                                                inputStream4 = inputStream2;
                                                th = th2;
                                                obj2 = obj5;
                                                obj3 = obj;
                                                Object obj6 = obj2;
                                                httpURLConnection = httpURLConnection;
                                                inputStream = inputStream4;
                                                outputStream = outputStream2;
                                                r3 = obj6;
                                                r0 = obj3;
                                                if (r3 != 0) {
                                                    try {
                                                        r3.close();
                                                    } catch (IOException unused3) {
                                                    }
                                                }
                                                if (outputStream != null) {
                                                    try {
                                                        outputStream.close();
                                                    } catch (IOException unused4) {
                                                    }
                                                }
                                                if (inputStream != null) {
                                                    try {
                                                        inputStream.close();
                                                    } catch (IOException unused5) {
                                                    }
                                                }
                                                if (r0 != 0) {
                                                    try {
                                                        r0.close();
                                                    } catch (IOException unused6) {
                                                    }
                                                }
                                                if (httpURLConnection != null) {
                                                    throw th;
                                                }
                                                httpURLConnection.disconnect();
                                                throw th;
                                            }
                                        } catch (Throwable th3) {
                                            obj3 = bufferedReader;
                                            inputStream4 = inputStream2;
                                            th = th3;
                                            obj2 = null;
                                            Object obj7 = obj2;
                                            httpURLConnection = httpURLConnection;
                                            inputStream = inputStream4;
                                            outputStream = outputStream2;
                                            r3 = obj7;
                                            r0 = obj3;
                                            if (r3 != 0) {
                                                r3.close();
                                            }
                                            if (outputStream != null) {
                                                outputStream.close();
                                            }
                                            if (inputStream != null) {
                                                inputStream.close();
                                            }
                                            if (r0 != 0) {
                                                r0.close();
                                            }
                                            if (httpURLConnection != null) {
                                                throw th;
                                            }
                                            httpURLConnection.disconnect();
                                            throw th;
                                        }
                                    } catch (IOException e4) {
                                        e = e4;
                                        inputStream3 = null;
                                        obj = inputStream3;
                                        obj4 = inputStream3;
                                        throw e;
                                    } catch (Throwable th4) {
                                        inputStream = inputStream2;
                                        th = th4;
                                        outputStream = null;
                                        r0 = outputStream;
                                        if (r3 != 0) {
                                            r3.close();
                                        }
                                        if (outputStream != null) {
                                            outputStream.close();
                                        }
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        if (r0 != 0) {
                                            r0.close();
                                        }
                                        if (httpURLConnection != null) {
                                            throw th;
                                        }
                                        httpURLConnection.disconnect();
                                        throw th;
                                    }
                                } catch (IOException e5) {
                                    e = e5;
                                    bufferedOutputStream2 = null;
                                    outputStream = null;
                                    obj = null;
                                    outputStream2 = outputStream;
                                    obj4 = bufferedOutputStream2;
                                    inputStream2 = null;
                                    throw e;
                                } catch (Throwable th5) {
                                    th = th5;
                                    outputStream = null;
                                    bufferedOutputStream = null;
                                    r0 = 0;
                                    r3 = bufferedOutputStream;
                                    httpURLConnection = httpURLConnection;
                                    inputStream = null;
                                    if (r3 != 0) {
                                        r3.close();
                                    }
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (r0 != 0) {
                                        r0.close();
                                    }
                                    if (httpURLConnection != null) {
                                        throw th;
                                    }
                                    httpURLConnection.disconnect();
                                    throw th;
                                }
                            } catch (IOException e6) {
                                e = e6;
                                bufferedOutputStream2 = null;
                            } catch (Throwable th6) {
                                th = th6;
                                bufferedOutputStream = null;
                            }
                        } catch (IOException e7) {
                            e = e7;
                            bufferedOutputStream2 = bufferedOutputStream;
                        } catch (Throwable th7) {
                            th = th7;
                        }
                    } catch (IOException e8) {
                        e = e8;
                        inputStream2 = null;
                        obj = null;
                        outputStream2 = outputStream;
                        obj4 = null;
                    } catch (Throwable th8) {
                        th = th8;
                        httpURLConnection = httpURLConnection;
                        inputStream = null;
                        r0 = 0;
                    }
                } catch (Throwable th9) {
                    th = th9;
                    inputStream = null;
                    outputStream = null;
                    r0 = outputStream;
                    if (r3 != 0) {
                        r3.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (r0 != 0) {
                        r0.close();
                    }
                    if (httpURLConnection != null) {
                        throw th;
                    }
                    httpURLConnection.disconnect();
                    throw th;
                }
            } catch (IOException e9) {
                e = e9;
                inputStream2 = null;
                inputStream3 = inputStream2;
                obj = inputStream3;
                obj4 = inputStream3;
                throw e;
            }
        } catch (IOException e10) {
            e = e10;
            httpURLConnection = null;
            inputStream2 = null;
        } catch (Throwable th10) {
            th = th10;
            inputStream = null;
            outputStream = null;
            httpURLConnection = null;
            r0 = 0;
        }
        if (r3 != 0) {
            r3.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (r0 != 0) {
            r0.close();
        }
        if (httpURLConnection != null) {
            throw th;
        }
        httpURLConnection.disconnect();
        throw th;
    }
}
