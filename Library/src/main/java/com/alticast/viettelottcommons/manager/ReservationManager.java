package com.alticast.viettelottcommons.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;

import com.alticast.android.util.Log;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.ReservationLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.MultiLingualText;
import com.alticast.viettelottcommons.resource.Reservation;
import com.alticast.viettelottcommons.resource.response.ReservationRes;
import com.alticast.viettelottcommons.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import retrofit2.Call;

public class ReservationManager {
    public static final String ACTION = ReservationManager.class.getName() + ".ACTION";
    private static final String TAG = ReservationManager.class.getSimpleName();
    private static ReservationManager sSelf = new ReservationManager();

    private Map<String, Reservation> mMap = new HashMap<>();
    private AlarmHandler mAlarmHandler = new AlarmHandler();

    private boolean mIsLoaded;
    private LocalBroadcastManager mBroadcastManager;

    public static ReservationManager get() {
        return sSelf;
    }

    private ReservationManager() {
    }

    public void initializeAlarm(Context context) {
        mBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public synchronized void retrieve() {
        if (!mIsLoaded) {
            refresh();
            mIsLoaded = true;
            setAlarm();
        }
    }

    private void refresh() {

        ReservationLoader.getInstance().show(new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                try {
                    String data = ((ReservationRes) obj).getData();
                    byte[] decode = Base64.decode(data, Base64.DEFAULT);

                    String json = unzipStringFromGZIPBytes(decode);
                    JSONObject jRoot = new JSONObject(json);
                    JSONArray jList = jRoot.getJSONArray("data");
                    int length = jList.length();
                    long currentTimeMillis = TimeManager.getInstance().getServerCurrentTimeMillis();
                    synchronized (this) {
                        mMap.clear();
                        for (int i = 0; i < length; i++) {
                            JSONObject jItem = jList.getJSONObject(i);
                            String channelId = jItem.getString("channelId");
                            String eventId = jItem.getString("eventId");
                            int serviceId = jItem.getInt("serviceId");
                            int rating = jItem.getInt("rating");
                            ArrayList<MultiLingualText> title = Reservation.getTitles(jItem);
                            String resolution = jItem.has("resolution") ? jItem.getString("resolution") : "SD";
                            long startTime = jItem.getLong("startTime");
                            long endTime = jItem.getLong("endTime");

                            if (currentTimeMillis < startTime) {
                                mMap.put(eventId, new Reservation(channelId, eventId, serviceId, rating, title, resolution, startTime, endTime));
                            }
                        }
                        setAlarm();
                        getReservationsWithoutAlarm(null);
                    }
                } catch (Exception e) {
                    Logger.print(TAG, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }

            @Override
            public void onError(ApiError error) {

            }
        });

    }

    private void setAlarm() {
        setAlarm(getReservations());
    }

    private void setAlarm(Reservation[] reservations) {
        if (reservations.length > 0) {
            mAlarmHandler.removeMessages(0);
            Message msg = Message.obtain();
            msg.obj = reservations[0];
            msg.what = 0;
            mAlarmHandler.sendMessageDelayed(msg, reservations[0].getStartTime() - TimeManager.getInstance().getServerCurrentTimeMillis());
//            mAlarmHandler.sendMessageDelayed(msg, 1000);
        }

    }


    private Reservation[] getReservations() {
        retrieve();
        if (mMap == null) return null;

        TreeSet<Reservation> list = new TreeSet<>(new Comparator<Reservation>() {
            @Override
            public int compare(Reservation lhs, Reservation rhs) {
                return (int) (lhs.getStartTime() - rhs.getStartTime());
            }
        });

        synchronized (this) {
            long currentTimeMillis = TimeManager.getInstance().getServerCurrentTimeMillis();
            for (Reservation reservation : mMap.values()) {
                if (currentTimeMillis < reservation.getStartTime()) {
                    list.add(reservation);
                }
            }
        }
        Reservation[] reservations = list.toArray(new Reservation[list.size()]);
        setAlarm(reservations);
        return reservations;
    }

    public void getReservationsWithoutAlarm(final WindmillCallback callback) {
        if (!mIsLoaded) {
            ReservationLoader.getInstance().show(new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    try {
                        String data = ((ReservationRes) obj).getData();
                        byte[] decode = Base64.decode(data, Base64.DEFAULT);

                        String json = unzipStringFromGZIPBytes(decode);
                        JSONObject jRoot = new JSONObject(json);
                        JSONArray jList = jRoot.getJSONArray("data");
                        int length = jList.length();
                        long currentTimeMillis = TimeManager.getInstance().getServerCurrentTimeMillis();
                        synchronized (this) {
                            mMap.clear();
                            for (int i = 0; i < length; i++) {
                                JSONObject jItem = jList.getJSONObject(i);
                                String channelId = jItem.getString("channelId");
                                String eventId = jItem.getString("eventId");
                                int serviceId = jItem.getInt("serviceId");
                                int rating = jItem.getInt("rating");
                                ArrayList<MultiLingualText> title = Reservation.getTitles(jItem);
                                String resolution = jItem.has("resolution") ? jItem.getString("resolution") : "SD";
                                long startTime = jItem.getLong("startTime");
                                long endTime = jItem.getLong("endTime");

                                if (currentTimeMillis < startTime) {
                                    mMap.put(eventId, new Reservation(channelId, eventId, serviceId, rating, title, resolution, startTime, endTime));
                                }
                            }
                            callback.onSuccess(loadReservationList());
                        }
                    } catch (Exception e) {
                        Logger.print(TAG, e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }

                @Override
                public void onError(ApiError error) {

                }
            });
        }

        callback.onSuccess(loadReservationList());
//        return loadReservationList();
    }

    private Reservation[] loadReservationList() {
        if (mMap == null) {
            return null;
        }

        TreeSet<Reservation> list = new TreeSet<>(new Comparator<Reservation>() {
            @Override
            public int compare(Reservation lhs, Reservation rhs) {
                return (int) (lhs.getStartTime() - rhs.getStartTime());
            }
        });

        synchronized (this) {
            long currentTimeMillis = TimeManager.getInstance().getServerCurrentTimeMillis();
            for (Reservation reservation : mMap.values()) {
                if (currentTimeMillis < reservation.getStartTime()) {
                    list.add(reservation);
                }
            }
        }
        Reservation[] reservations = list.toArray(new Reservation[list.size()]);
        for (int i = 0; i < reservations.length; i++) {

        }
        return reservations;
    }

    public boolean contains(String id) {
        synchronized (this) {
            return mMap.containsKey(id);
        }
    }

    public Reservation duplicatedItem(long startTime) {
        retrieve();
        synchronized (this) {
            Collection<Reservation> values = mMap.values();
            for (Reservation r : values) {
                if (r.getStartTime() == startTime) {
                    return r;
                } else if (r.getStartTime() > startTime) {
                    continue;
                }
            }
        }
        return null;
    }


    public boolean create(final Reservation newItem, final Reservation oldItem, final WindmillCallback callback) {
        refresh();
        LinkedHashMap<String, Reservation> map;
        synchronized (this) {
            map = new LinkedHashMap<>(mMap);
        }
        if (newItem != null) map.put(newItem.getEventId(), newItem);
        if (oldItem != null) map.remove(oldItem.getEventId());

        ReservationLoader.getInstance().create(toData(map.values()), new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                synchronized (this) {
                    if (newItem != null) mMap.put(newItem.getEventId(), newItem);
                    if (oldItem != null) mMap.remove(oldItem.getEventId());
                }
                setAlarm();
                callback.onSuccess(obj);
            }


            @Override
            public void onFailure(Call call, Throwable t) {

            }

            @Override
            public void onError(ApiError error) {

            }
        });
        return false;
    }

    public boolean createList(final Reservation newItem, final ArrayList<Reservation> oldItem, final WindmillCallback callback) {
//        refresh();
        final LinkedHashMap<String, Reservation> map;
        synchronized (this) {
            map = new LinkedHashMap<>(mMap);
        }
        if (newItem != null) map.put(newItem.getEventId(), newItem);
        for(Reservation reservation : oldItem) {
            if (oldItem != null) map.remove(reservation.getEventId());
        }

        ReservationLoader.getInstance().create(toData(map.values()), new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                synchronized (this) {
                    if (newItem != null) mMap.put(newItem.getEventId(), newItem);
                    for(Reservation reservation : oldItem) {
                        if (oldItem != null) mMap.remove(reservation.getEventId());
                    }
                }
                setAlarm();
                callback.onSuccess(obj);
            }


            @Override
            public void onFailure(Call call, Throwable t) {

            }

            @Override
            public void onError(ApiError error) {

            }
        });
        return false;
    }

    private static String toData(Collection<Reservation> list) {
        JSONObject jRoot = new JSONObject();
        JSONArray jList = new JSONArray();
        try {
            for (Reservation reservation : list) {
                JSONObject jItem = new JSONObject();
                jItem.put("channelId", reservation.getChannelId());
                jItem.put("eventId", reservation.getEventId());
                jItem.put("serviceId", reservation.getServiceId());
                jItem.put("rating", reservation.getRating());
                jItem.put("title", reservation.getTitle());
                jItem.put("resolution", reservation.getResolution());
                jItem.put("startTime", reservation.getStartTime());
                jItem.put("endTime", reservation.getEndTime());
                jList.put(jItem);
            }
            jRoot.put("data", jList);
        } catch (JSONException ignored) {
        }

        byte[] zipped = zipStringToBytes(jRoot.toString());
        return Base64.encodeToString(zipped, Base64.NO_WRAP);
    }

    private static byte[] zipStringToBytes(String input) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(gzipOutputStream);
            bufferedOutputStream.write(input.getBytes("UTF-8"));
            bufferedOutputStream.close();
            byteArrayOutputStream.close();
        } catch (UnsupportedEncodingException ignored) {
        } catch (IOException ignored) {
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static String unzipStringFromGZIPBytes(byte[] bytes) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String result = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(gzipInputStream);
            byte[] buffer = new byte[100];
            int length;
            while ((length = bufferedInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            bufferedInputStream.close();
            gzipInputStream.close();
            byteArrayInputStream.close();
            byteArrayOutputStream.close();
            result = byteArrayOutputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        } catch (IOException ignored) {
        }
        return result;
    }

    private final class AlarmHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(ACTION);
            intent.putExtra(Reservation.class.getName(), (Reservation) msg.obj);
            mBroadcastManager.sendBroadcast(intent);
//            setAlarm();
        }
    }

    public void clear() {
        mAlarmHandler.removeMessages(0);
        mIsLoaded = false;
        if (mMap != null) {
            mMap.clear();
        }
    }


}
