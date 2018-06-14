package com.alticast.viettelottcommons.IETP;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.alticast.ietp.IetpBluetoothClient;
import com.alticast.ietp.IetpClient;
import com.alticast.ietp.IetpClientInfo;
import com.alticast.ietp.IetpClientListener;
import com.alticast.ietp.IetpSocketClient;
import com.alticast.ietp.exception.IetpTimeoutException;
import com.alticast.ietp.result.IetpRegisterResult;
import com.alticast.ietp.result.IetpUnregisterResult;
import com.alticast.media.AltiPlayer;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.loader.UserDataLoader;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.response.STBInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Response;

public class IETP {
    public static final String IETP_DISPOSE = "dispose";
    public static final String IETP_SEND_KEY = "sendKey";
    public static final String IETP_SEND_TEXT = "sendText";
    public static final String IETP_SEND_DATA = "sendData";

    public static final String KEY_methodName = "methodName";
    public static final String KEY_resultMsg = "resultMsg";
    public static final String KEY_stbStatus = "stbStatus";
    public static final String KEY_userId = "userId";
    public static final String KEY_channelId = "channelId";
    public static final String KEY_programId = "programId";
    public static final String KEY_productId = "productId";

    public static final String METHOD_getStbStatus = "getStbStatus";
    public static final String METHOD_tuneChannel = "tuneChannel";
    public static final String METHOD_pushVodWatch = "pushVodWatch";
    public static final String METHOD_pushVodPurchase = "pushVodPurchase";
    public static final String METHOD_pushUserChange = "pushUserChange";
    public static final String METHOD_pullVodDetail = "pullVodDetail";
    public static final String METHOD_pullVodWatch = "pullVodWatch";
    public static final String METHOD_pullChannel = "pullChannel";

    public static final int ERROR_UNKNOWN = -201;
    public static final int ERROR_STB_IP = -205;
    public static final int ERROR_RESGISTER = -204;
    public static final int ERROR_CONNECT = -205;


    public static Handler mainHandler;
    public static String udid;

    private static IetpSocketClient client;
    private static Socket socket;
    public static String stbIP;
    //    private static String proxyIp = "192.168.1.201";
    private static String proxyIp = WindmillConfiguration.getProxyIp();
    private static IetpClientInfo clientInfo;
    private static byte[] key;

    private static IetpBluetoothClient bluetoothClient;
    private static BluetoothSocket bluetoothSocket;

    private static int TIMEOUT = 15000;
    private static long lastSended;

    private static final int REQUESTED_NONE = 0;
    private static final int SEND_KEY = 1;
    private static final int SEND_TEXT = 2;
    private static int requested;
    private static int codeToSend;
    private static String textToSend;

    private static String clientId;

    public static void refreshConnection() {
        stbIP = null;
        Log.e("set null", "refreshConnection()");
    }

    // ensure IETP connection
    public static int ready(String mStbIp) {
        if (mStbIp == null) {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ignored) {
                return ERROR_UNKNOWN;
            }
        }

        stbIP = mStbIp;

        boolean socketOnline = socketOnline();
        boolean wasOnline = socketOnline;
        if (!socketOnline) {
            int result = readySocket(mStbIp);
            if (result != 0) {
                return result;
            }
        }
        try {
            if (client == null) {
                client = new IetpSocketClient(socket, clientInfo);
                client.setResponseTimeout(TIMEOUT);
                client.setIetpClientListener(listener);
            } else if (!wasOnline) { // client is exists, but socket
                // is new one
                client.setSocket(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();

            return ERROR_CONNECT;
        }

        if (clientInfo == null || clientInfo.getClientId() == 0) {
            int result = register(false);
            if (result < 0) {
                return ERROR_RESGISTER;
            }
        }
        return client != null && clientInfo != null && clientInfo.getClientId() > 0 ? 0 : ERROR_UNKNOWN;
    }

    public static int ready() {
        if (stbIP == null) {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ignored) {
                return ERROR_UNKNOWN;
            }
        }
        boolean socketOnline = socketOnline();
        boolean wasOnline = socketOnline;
        if (!socketOnline) {
            int result = readySocket(stbIP);
            if (result != 0) {
                return result;
            }
        }
        try {
            if (client == null) {
                client = new IetpSocketClient(socket, clientInfo);
                client.setResponseTimeout(TIMEOUT);
                client.setIetpClientListener(listener);
            } else if (!wasOnline) { // client is exists, but socket
                // is new one
                client.setSocket(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();

            return ERROR_CONNECT;
        }

        if (clientInfo == null || clientInfo.getClientId() == 0) {
            int result = register(false);
            if (result < 0) {
                return ERROR_RESGISTER;
            }
        }
        return client != null && clientInfo != null && clientInfo.getClientId() > 0 ? 0 : ERROR_UNKNOWN;
    }

    private static boolean socketOnline() {
        if (socket == null) {
            return false;
        }
        if (socket.isClosed()) {
            socket = null;
            return false;
        }
        return true;
    }

    public static String socketDebug() {
        if (socket == null) {
            return "socket is null, ";
        }
        if (socket.isClosed()) {
            return "socket is closed, ";
        }
        return "socket ok, ";
    }

    public static String clientDebug() {
        if (client == null) {
            return "client is null";
        }
        if (clientInfo == null) {
            return "clientInfo is null";
        }
        if (clientInfo.getClientId() == 0) {
            return "clientId is 0";
        }
        return "client ok = " + clientInfo.getClientId();
    }

    private static int readySocket(String mStbIp) {
        int port = 8000;
//		stbIP = new StbIPAddress();
        int proxyPort = 443;

        if (mStbIp == null) {
            Response<STBInfo> response = UserDataLoader.getInstance().getSTBIp(HandheldAuthorization.getInstance().getSTBUdid());
            if (response == null) return ERROR_STB_IP;
            STBInfo stbInfo = (STBInfo) response.body();
            if (stbInfo != null && stbInfo.getUser() != null && stbInfo.getUser().getDevice() != null)
                stbIP = stbInfo.getUser().getDevice().getIp();
        } else {
            stbIP = mStbIp;
        }
        if (stbIP == null) {
            return ERROR_STB_IP;
        }

        return connect(port, proxyPort);
    }

    private static int connect(int port, int proxyPort) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            final SSLSocketFactory delegate = sslContext.getSocketFactory();
            SocketFactory factory = new SSLSocketFactory() {
                @Override
                public Socket createSocket(String host, int port)
                        throws IOException {
                    InetAddress address = InetAddress.getByName(host);
                    injectHostname(address, host);
                    return delegate.createSocket(address, port);
                }

                @Override
                public Socket createSocket(InetAddress host, int port)
                        throws IOException {
                    return delegate.createSocket(host, port);
                }

                @Override
                public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
                        throws IOException {
                    return delegate.createSocket(host, port, localHost, localPort);
                }

                @Override
                public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
                        throws IOException {
                    return delegate.createSocket(address, port, localAddress, localPort);
                }

                private void injectHostname(InetAddress address, String host) {
                    try {
                        Field field = InetAddress.class.getDeclaredField("hostName");
                        field.setAccessible(true);
                        field.set(address, host);
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
                    injectHostname(s.getInetAddress(), host);
                    return delegate.createSocket(s, host, port, autoClose);
                }

                @Override
                public String[] getDefaultCipherSuites() {
                    return delegate.getDefaultCipherSuites();
                }

                @Override
                public String[] getSupportedCipherSuites() {
                    return delegate.getSupportedCipherSuites();
                }
            };
            // socket = new Socket(InetAddress.getByName(proxyIp),proxyPort);
            socket = factory.createSocket(proxyIp, proxyPort);
            //     socket.setSoTimeout(TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_CONNECT;
        }


        boolean ok = false;
        try {
            String request = "CONNECT " + stbIP + ":" + port + " HTTP/1.0\r\n\r\n";
            Log.i("IETP", "Write to proxy = " + request);

            OutputStream os = socket.getOutputStream();
            Log.i("IETP", "getOutputStream() = " + os);
            os.write(request.getBytes());
            Log.i("IETP", "after write");
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("HTTP")) {
                    Log.i("IETP", "Response from proxy = " + line);
                    if (line.indexOf("200") != -1) { // Connection Established
                        ok = true;
                        break;
                    } else if (line.indexOf("50") != -1) {
                        // (502) Proxy Error, (503) Service Unavailable
                        ok = false;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR_CONNECT;
        }
        if (ok == false) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            socket = null;

            return ERROR_CONNECT;
        }

        return 0;
    }


    synchronized public static void dispose() {
        Log.i("IETP", "dispose(), socket = " + socket);
        if (client != null) {
            client.setIetpClientListener(null);
            client = null;
        }
        clientInfo = null;
        clientId = null;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                socket = null;
            }
        }
        stbIP = null;
        Log.e("set null", "dispose()");
    }


    synchronized public static int sendKey(int code) {
        int result = ready(stbIP);
        if (result == 0) {
            client.sendKeyEvent(code);
            lastSended = System.currentTimeMillis();
        }
        return result;
    }

    synchronized public static int sendText(String text) {
        requested = SEND_TEXT;
        textToSend = text;
        int result = ready(stbIP);
        if (result == 0) {
            client.sendText(text);
            lastSended = System.currentTimeMillis();
        }
        return result;
    }


    synchronized public static int sendData(String text) {
        Log.i("IETP", "sendData(), text = " + text);
        int result = ready(stbIP);
        if (result == 0) {
            client.sendData(text.getBytes(), "SmartRCU");
            lastSended = System.currentTimeMillis();
        }

        return result;

    }

    public static boolean unregister() {
        ready(stbIP);

        if (client == null) {
            Log.w("IETP", "unregister(), socket client is null");
            return false;
        }
        if (udid == null) {
            Log.w("IETP", "unregister(), udid is null");
            //UDID 세팅
//            udid = AltiPlayer.GetDeviceUniqueID();
            udid = WindmillConfiguration.deviceId;
//            udid ="bd7a91cc65713aafbe4a47ddd0a6439f";
        }
        byte[] hint = udid.getBytes();
        key = new byte[hint.length / 2];
        for (int i = 0; i < key.length; i++) {
            key[i] = hint[i * 2];
        }
        Log.i("IETP", "unregister(), hint = " + byteToHex(hint));
        Log.i("IETP", "unregister(), key = " + byteToHex(key));
        int id = -1;
        IetpUnregisterResult result = null;
        try {
            result = client.unregister(hint, key);
            id = result.getClientId();
        } catch (IetpTimeoutException e) {
            e.printStackTrace();
        }
        boolean ret = id == clientInfo.getClientId();
        Log.i("IETP", "unregister(), clientId = " + id + ", clientInfo.id = " + clientInfo.getClientId());
        return ret;
    }

    private static int register(boolean force) {
        if (client == null) {
            Log.w("IETP", "register(), socket client is null");
            return -99;
        }
        if (udid == null) {
            Log.w("IETP", "udid is null");
            //UDID 세팅
//            udid = AltiPlayer.GetDeviceUniqueID();
            udid = WindmillConfiguration.deviceId;
//            udid ="bd7a91cc65713aafbe4a47ddd0a6439f";
        }
        if (clientId != null && Integer.parseInt(clientId) < 0) {
            clientId = null;
        }
        if (force) {
            clientId = null;
        }

        byte[] hint = udid.getBytes();
        key = new byte[hint.length / 2];
        for (int i = 0; i < key.length; i++) {
            key[i] = hint[i * 2];
        }
        int id = -1;
        Log.i("IETP", "register(), key = " + byteToHex(key) + ", toStr = " + new String(key));
        if (clientId == null) {
            Log.i("IETP", "register(), hint = " + byteToHex(hint) + ", toStr = " + new String(hint));

            IetpRegisterResult result = null;
            try {
                result = client.register(hint, key);
                id = result.getClientId();
            } catch (IetpTimeoutException | NullPointerException e) {
                e.printStackTrace();

            }
            Log.i("IETP", "register(), clientId = " + id);
            if (id >= 0) {
                clientInfo = new IetpClientInfo(id, key);
                clientId = String.valueOf(id);
            } else {
                Log.e("IETP", "result = " + (result == null ? null : result.getErrorCode()));
//                id = result.getErrorCode() * -1; // negative means error case
            }
        } else {
            Log.i("IETP", "register(), stored clientId = " + clientId);
            clientInfo = new IetpClientInfo(Integer.parseInt(clientId), key);
        }

        if (client != null && clientInfo != null) {
            client.setClientInfo(clientInfo);
        }
        return id;
    }

    private static String byteToHex(byte[] data) {
        if (data == null) {
            return "NULL";
        }
        StringBuffer str = new StringBuffer("len(");
        str.append(data.length);
        str.append("), ");
        for (int i = 0; i < data.length; i++) {
            if ((0xFF & data[i]) <= 0xF)
                str.append("0");
            str.append(Integer.toHexString(0xFF & data[i]));
            if (i % 2 == 1)
                str.append(" ");
        }
        return str.toString();
    }

    private static X509Certificate cert;
    private static PublicKey publicKey;

    private static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            }
    };

    private static IetpClientListener listener = new IetpClientListener() {

        @Override
        public void onReceivedError(byte code) {

            if (ietpDataListener != null) {
                ietpDataListener.receivedOnError(code);
            }
            Log.e("IETP", "onReceiveError(), code = " + code);
            switch (code) {
                case IetpClient.ERROR_SOCKET:
                    long gap = System.currentTimeMillis() - lastSended;
                    if (gap < TIMEOUT) { // maybe socket error
                        Log.e("IETP", "onReceiveError(), socket error, requested = " + requested + ", key = " + codeToSend
                                + ", text = " + textToSend);
//                        if (ietpDataListener != null) {
//                            ietpDataListener.receivedOnError(-201);
//                        }
                        dispose();
                    } else {
                        Log.e("IETP", "onReceiveError(), expected error by time out.");
                        if (socket != null) {
                            try {
                                socket.close();
                                socket = null;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        client.setIetpClientListener(null);
                        client = null;
                        return;
                    }
                    break;
                case IetpClient.ERROR_CLIENT_ID:
                    int ret = register(true);
                    if (ret < 0) { // some error
                        if (ret == -2) { // not registered on windmill server
                            requested = REQUESTED_NONE; // so don't retry
                            clientId = null;
                        }
                    }
                    break;
            }
            switch (requested) {
                case SEND_KEY:
                    if (codeToSend > 0 && ready(stbIP) == 0) {
                        client.sendKeyEvent(codeToSend);
                        codeToSend = 0;
                    }
                    break;
                case SEND_TEXT:
                    if (textToSend != null && ready(stbIP) == 0) {
                        client.sendText(textToSend);
                        textToSend = null;
                    }
                    break;
            }
            requested = REQUESTED_NONE;

        }

        @Override
        public void onReceivedData(Object app, byte[] data) {
            Log.i("IETP", "onReceivedData(), app = " + app + ", data = " + new String(data));
            if (ietpDataListener != null) {
                ietpDataListener.receivedIetpMsg(new String(data));
            }
        }
    };

    public static class IETPTask extends AsyncTask<Object, Object, Integer> {

        protected Integer doInBackground(Object... params) {

            int ready = IETP.ready();
            if (ready == 0) {

                int result = 0;
                if (IETP_DISPOSE.equals(params[0])) {
                    IETP.dispose();
                } else if (IETP_SEND_KEY.equals(params[0])) {
                    result = IETP.sendKey(((Integer) params[1]).intValue());
                } else if (IETP_SEND_TEXT.equals(params[0])) {
                    result = IETP.sendText(params[1].toString());
                } else if (IETP_SEND_DATA.equals(params[0])) {
                    result = IETP.sendData(params[1].toString());
                }
                return result;
            } else {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            switch (result) {
                case 0:
                    break;
                default:
                    if (ietpDataListener != null) {
                        ietpDataListener.receivedOnError(result);
                    }
                    break;
            }
        }
    }

    private static IetpDataReceiver ietpDataListener;

    public static void getStbStaus(IetpDataReceiver listener) {
        ietpDataListener = listener;
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_methodName, METHOD_getStbStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IETPTask().execute(IETP_SEND_DATA, json.toString());
    }

    public static void tuneChannel(IetpDataReceiver listener, String channelId) {
        ietpDataListener = listener;
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_methodName, METHOD_tuneChannel);
            json.put(KEY_channelId, channelId);
            json.put(KEY_userId, HandheldAuthorization.getInstance().getCurrentId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IETPTask().execute(IETP_SEND_DATA, json.toString());
    }

    public static void pushVodWatch(IetpDataReceiver listener, String programId, String productId, int currentPosition) {
        ietpDataListener = listener;
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_methodName, METHOD_pushVodWatch);
            json.put(KEY_userId, HandheldAuthorization.getInstance().getCurrentId());
            json.put(KEY_programId, programId);
            if (productId != null) json.put("productId", productId);
            json.put("time_offset", String.valueOf(currentPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IETPTask().execute(IETP_SEND_DATA, json.toString());
    }

    public static void pushVodPurchase(IetpDataReceiver listener, String programId, String productId) {
        ietpDataListener = listener;
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_methodName, METHOD_pushVodPurchase);
            json.put(KEY_userId, HandheldAuthorization.getInstance().getCurrentId());
            json.put(KEY_programId, programId);
            json.put(KEY_productId, productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IETPTask().execute(IETP_SEND_DATA, json.toString());
    }

    public static void pushUserChange(IetpDataReceiver listener, String userId) {
        ietpDataListener = listener;
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_methodName, METHOD_pushUserChange);
            json.put(KEY_userId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IETPTask().execute(IETP_SEND_DATA, json.toString());
    }

    public static void pullVodWatch(IetpDataReceiver listener) {
        ietpDataListener = listener;
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_methodName, METHOD_pullVodWatch);
            json.put(KEY_userId, HandheldAuthorization.getInstance().getCurrentId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IETPTask().execute(IETP_SEND_DATA, json.toString());
    }

    public static void pullVodDetail(IetpDataReceiver listener) {
        ietpDataListener = listener;
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_methodName, METHOD_pullVodDetail);
            json.put(KEY_userId, HandheldAuthorization.getInstance().getCurrentId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IETPTask().execute(IETP_SEND_DATA, json.toString());
    }

    public static void pullChannel(IetpDataReceiver listener) {
        ietpDataListener = listener;
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_methodName, METHOD_pullChannel);
            json.put(KEY_userId, HandheldAuthorization.getInstance().getCurrentId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new IETPTask().execute(IETP_SEND_DATA, json.toString());
    }

    public interface IetpDataReceiver {
        public void receivedIetpMsg(String jsonData);

        public void receivedOnError(int result);
    }
}
