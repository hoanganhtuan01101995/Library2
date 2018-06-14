/*
 *  Copyright (c) 2012 Alticast Corp.
 *  All rights reserved. http://www.alticast.com/
 *
 *  This software is the confidential and proprietary information of
 *  Alticast Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Alticast.
 */
package com.alticast.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.UUID;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Surface;

import com.alticast.android.util.BuildConfig;
import com.alticast.android.util.LibraryPathLoader;
import com.alticast.android.util.Log;
import com.alticast.android.util.NativeUtils;
import com.alticast.viettelottcommons.WindmillConfiguration;

public class AltiPlayer {

    private static final Log LOG = Log.createLog("AltiPlayer");

    //
    // Error Codes
    //
//    public static final boolean PLAYER_LOG_ON = true;
    public static final boolean PLAYER_LOG_ON = WindmillConfiguration.PLAYER_LOG_ON;
    // Error Codes Of Set Data Source.
    public static final int OPEN_SUCCEEDED = 0x00;
    public static final int OPEN_FILE_FAILED = -0x01;
    public static final int OPEN_STREAM_FAILED = -0x02;
    public static final int OPEN_MEMALLOC_FAILED = -0x03;
    public static final int OPEN_TIMEOUT_FAILED = -0x04;

    //  Error Codes Of Read Data.
    public static final int READ_BUFFER_FAILED = -0x10;
    public static final int READ_FILE_IO_FAILED = -0x11;
    public static final int READ_EOS_FAILED = -0x12;

    // Error Codes Of Open Data Source.
    public static final int OPEN_SEEK_FAILED = -0x20;
    public static final int OPEN_CONFIG_FAILED = -0x30;
    public static final int OPEN_NETWORK_FAILED = -0x40;

    // Error Codes of DRM2.
    public static final int OPEN_DRM2_BOOT_FAILED = -0x50;
    public static final int OPEN_DRM2_INIT_FAILED = -0x51;
    public static final int OPEN_DRM2_SELECT_FAILED = -0x52;
    public static final int OPEN_DRM2_DECRYPT_FAILED = -0x53;
    // Error Codes Of DRM3.
    public static final int OPEN_DRM3_SET_FAILED = -0x60;
    public static final int OPEN_DRM3_OPEN_FAILED = -0x61;
    public static final int OPEN_DRM3_DECRYPT_FAILED = -0x62;

    // Error Codes Of ETC.
    public static final int OPEN_POWER_OFF_FAILED = -0xA0;
    public static final int OPEN_ACTIVE_FAILED = -0xA1;

    // Error Codes Of UNKNOWN.
    public static final int OPEN_UNKNOWN_FAILED = -0xFF;

    // Error Codes Of Set Video Surface.
    public static final int SET_DISPLAY_SUCCEESED = 0x00;
    public static final int SET_DISPLAY_FAILED = -0x01;

    // Error Codes Of Source.
    public static final int ERROR_SOURCE_FTP = 0x01;
    public static final int ERROR_SOURCE_FILE = 0x02;
    public static final int ERROR_SOURCE_HLS = 0x03;

    // Error Codes Of FTP.
    public static final int ERROR_CONNECTION_REFUSED = 0x01;
    public static final int ERROR_CONNECTION_FAIL = 0x02;


    //
    // Types, States, Flags and so on.
    //

    // States Of Player
    public static final int PLAYER_STATE_IDLE = 0;
    public static final int PLAYER_STATE_PREPARED = 1;
    public static final int PLAYER_STATE_PLAYING = 2;
    public static final int PLAYER_STATE_PAUSED = 3;
    public static final int PLAYER_STATE_STOPPED = 4;

    // Type Of Service
    public static final int TYPE_VOD = 1;
    public static final int TYPE_CHANNEL = 2;

    public static final int USER_VIDEO_QUALITY_AUTO = 0;
    public static final int USER_VIDEO_QUALITY_LOW = 1;
    public static final int USER_VIDEO_QUALITY_NORMAL = 2;
    public static final int USER_VIDEO_QUALITY_HIGH = 3;
    public static final int USER_VIDEO_QUALITY_SOUND = 4;

    // Type Of CPU Architecture.
    public static final int CPU_ARCH_ARM = 0;
    public static final int CPU_ARCH_X86 = 1;
    public static final int CPU_ARCH_MIPS = 2;

    public static final int AUDIO_SAMPLE_RATE_44100 = 44100;
    public static final int AUDIO_SAMPLE_RATE_48000 = 48000;

    private static int playerState = PLAYER_STATE_IDLE;
    private static AudioTrack audioTrack = null;
    private static Context mContext;
    private static String mConfigFileDir;

    private static int mCpuArchitecture = CPU_ARCH_ARM;
    private static boolean mVideoHwCodecUse = true;

    public AltiPlayer() {
    }

    /**
     * Initialize AltiPlayer.
     *
     * @param libraryDir    Path of libraries for AltiPlayer.
     *                      ex) /data/data/com.viettel.ott/lib/
     * @param configFileDir Root path of configuration and something else.
     *                      Require read/write permission.
     *                      ex) /data/data/com.viettel.ott/files/player
     * @return True if success, otherwise return false.
     * @parma context Interface to global information about an application environment.
     */


    public static boolean init(String libraryDir, String configFileDir, Context context) {
        final int sdkVersion = Build.VERSION.SDK_INT;

        // LOG.printMsg("SUPPORTED_ABIS        : " + Arrays.toString(android.os.Build.SUPPORTED_ABIS)  + ".");
        // LOG.printMsg("SUPPORTED_32_BIT_ABIS : " + Arrays.toString(android.os.Build.SUPPORTED_32_BIT_ABIS)  + ".");
        // LOG.printMsg("SUPPORTED_64_BIT_ABIS : " + Arrays.toString(android.os.Build.SUPPORTED_64_BIT_ABIS)  + ".");

        LOG.printMsg("OS.ARCH : " + System.getProperty("os.arch"));
        // LOG.printMsg("ro.product.cpu.abi : " + System.getProperty("ro.product.cpu.abi"));

        String cpuArchitecture = NativeUtils.getCPUArchitecture();
        if (Log.MSG) {
            LOG.printMsg("SYSTEM CPU ARCHITECTURE: " + cpuArchitecture + ".");
        }

        if (cpuArchitecture.equals("arm")) {
            mCpuArchitecture = CPU_ARCH_ARM;
            if (Log.MSG) {
                LOG.printMsg("System Working Mode: CPU_ARCH_ARM.");
            }
        } else if (cpuArchitecture.equals("x86")) {
            mCpuArchitecture = CPU_ARCH_X86;
            if (Log.MSG) {
                LOG.printMsg("System Working Mode: CPU_ARCH_X86.");
            }
        } else {
            if (Log.ERR) {
                LOG.printMsg("Not Supported CPU Architecture.");
            }
            return false;
        }

        if (Log.MSG) {
            LOG.printMsg("init() start.");
        }

        if (Log.MSG) {
            LOG.printMsg("sdkVersion = " + sdkVersion);
            LOG.printMsg("libraryDir = " + libraryDir);
            LOG.printMsg("configFileDir = " + configFileDir);
        }

        if (sdkVersion < 16) {
            if (Log.MSG) {
                LOG.printMsg("unsuppoerted android sdkVersion = " + sdkVersion);
            }

            return false;
        }

        mConfigFileDir = configFileDir;
        mContext = context;

        // Get and set device ID.
        String uuidDevice = GetDeviceUniqueID();
        if (Log.MSG) {
            LOG.printMsg("uuidDevice = " + uuidDevice);
        }

        File ffmpegLibFile = null;
        long ffmpegFileVersion = -1;
        long buildVersion = -1;

        String libNameFFmpeg = null;

        String libNamePlayer = null;
        String libPathFFmpeg = null;

        // CPU Architecture: Arm 
        if (mCpuArchitecture == CPU_ARCH_ARM) {
            // Support NEON
            if (NativeUtils.isNeonSupported()) {
                libNameFFmpeg = "ffmpeg";

                // Loading player library according to sdk version respectively.
                if (sdkVersion >= 16 && sdkVersion < 18) {
                    if (PLAYER_LOG_ON) {
                        libNamePlayer = "altiplayer_arm_j1_n_dl";
                    } else {
                        libNamePlayer = "altiplayer_arm_j1_n_or";
                    }

                } else if (sdkVersion >= 18 && sdkVersion < 21) {
                    if (PLAYER_LOG_ON) {
                        libNamePlayer = "altiplayer_arm_j2_n_dl";
                    } else {
                        libNamePlayer = "altiplayer_arm_j2_n_or";
                    }
                } else if (sdkVersion >= 21) {
                    if (PLAYER_LOG_ON) {
                        libNamePlayer = "altiplayer_arm_lp_n_dl";
                    } else {
                        libNamePlayer = "altiplayer_arm_lp_n_or";
                    }

                }

                buildVersion = BuildConfig.FFMPEG_ARM_N_CODEC_VERSION;
            } else {
                libNameFFmpeg = "ffmpegv";

                // Loading player library according to sdk version respectively.
                if (sdkVersion >= 16 && sdkVersion < 18) {
                    if (PLAYER_LOG_ON) {
                        libNamePlayer = "altiplayer_arm_j1_v_dl";
                    } else {
                        libNamePlayer = "altiplayer_arm_j1_v_or";
                    }

                } else if (sdkVersion >= 18 && sdkVersion < 21) {
                    if (PLAYER_LOG_ON) {
                        libNamePlayer = "altiplayer_arm_j2_v_dl";
                    } else {
                        libNamePlayer = "altiplayer_arm_j2_v_or";
                    }

                } else if (sdkVersion >= 21) {
                    if (PLAYER_LOG_ON) {
                        libNamePlayer = "altiplayer_arm_lp_v_dl";
                    } else {
                        libNamePlayer = "altiplayer_arm_lp_v_or";
                    }

                }
                buildVersion = BuildConfig.FFMPEG_ARM_V_CODEC_VERSION;
            }
        }
        // CPU Architecture: x86 
        else if (mCpuArchitecture == CPU_ARCH_X86) {
            libNameFFmpeg = "ffmpeg";

            // Loading player library according to sdk version respectively.
            if (sdkVersion >= 16 && sdkVersion < 18) {
                if (PLAYER_LOG_ON) {
                    libNamePlayer = "altiplayer_x86_j1_x_dl";
                } else {
                    libNamePlayer = "altiplayer_x86_j1_x_or";
                }

            } else if (sdkVersion >= 18 && sdkVersion < 21) {
                if (PLAYER_LOG_ON) {
                    libNamePlayer = "altiplayer_x86_j2_x_dl";
                } else {
                    libNamePlayer = "altiplayer_x86_j2_x_or";
                }
            } else if (sdkVersion >= 21) {
                if (PLAYER_LOG_ON) {
                    libNamePlayer = "altiplayer_x86_lp_x_dl";
                } else {
                    libNamePlayer = "altiplayer_x86_lp_x_or";
                }
            }

            // TODO:
            buildVersion = BuildConfig.FFMPEG_X86_X_CODEC_VERSION;
        }
        // CPU Architecture: Not supported(MIPS(mix), ...)  
        else {
            if (Log.ERR) {
                LOG.printMsg("Not Supported CPU Architecture: " + mCpuArchitecture + ".");
            }
            return false;
        }

        if (Log.MSG) {
            LOG.printMsg("Lib Of Player = " + libNamePlayer);
        }
        try {

            LibraryPathLoader loader = new LibraryPathLoader(libraryDir);

            if (mCpuArchitecture == CPU_ARCH_ARM) {
                System.load(loader.findLibrary("altiyuvrgb"));
                if (Log.MSG) {
                    LOG.printMsg("altiyuvrgb done.");
                }
            }

            System.load(loader.findLibrary("altirdns"));
            if (Log.MSG) {
                LOG.printMsg("altirdns done.");
            }

            System.load(loader.findLibrary("ViewRightWebClient"));
            if (Log.MSG) {
                LOG.printMsg("ViewRightWebClient done.");
            }

            System.load(loader.findLibrary(libNameFFmpeg));

            if (Log.MSG) {
                LOG.printMsg("FFmpeg Libs done.");
            }

            System.load(loader.findLibrary(libNamePlayer));
            if (Log.MSG) {
                LOG.printMsg("altiplayer done.");
            }

            libPathFFmpeg = loader.findLibrary(libNameFFmpeg);
            LOG.printMsg("altiplayer all done.");
        } catch (Exception e) {
            if (Log.ERR) {
                LOG.printEx(e);
                LOG.printMsg("fail to load libraries");
            }
        }

        ffmpegLibFile = new File(libPathFFmpeg);

        if (ffmpegLibFile != null && ffmpegLibFile.exists()) {
            ffmpegFileVersion = ffmpegLibFile.length();
        }

        startPlayerListener();

        boolean retValue = setVideoHwDocoderUse(mVideoHwCodecUse);
        if (Log.MSG) {
            LOG.printMsg("setVideoHwDocoderUse(" + mVideoHwCodecUse + ") return = " + retValue + ".");
        }

        setDeviceUniqueIdentifier0(uuidDevice);

        if (ffmpegFileVersion != buildVersion) {
            return false;
        } else {
            // If success, set confDir.
            setConfigFileDir0(configFileDir);
            return true;
        }
    }

    private static AltiPlayerListener listener;

    /**
     * Set AltiPlayerListener.
     *
     * @param l AltiPlayerListener to be set.
     */
    public static void setAltiPlayerListener(AltiPlayerListener l) {
        if (Log.MSG) {
            LOG.printMsg("setAltiPlayerListener() start.");
        }

        listener = l;
    }

    public static AltiPlayerListener getAltiPlayerListener() {
        return listener;
    }

    /**
     * Remove AltiPlayerListener.
     */
    public static void removeAltiPlayerListener() {
        if (Log.MSG) {
            LOG.printMsg("removeAltiPlayerListener() start.");
        }
        listener = null;
    }

    /**
     * Set user agent. Not used in Viettel.
     *
     * @param useragent user agent string in HTTP.
     */
    public static void setUserAgent(String useragent) {
        if (Log.MSG) {
            LOG.printMsg("setUserAgent() - useragent = " + useragent);
        }

        setHttpUserAgent0(useragent);
    }

    /**
     * Set data source.
     *
     * @param path Path of source data and parameters.
     *             ex) hlsvod://http://121.156.46.14:80/M85EA008SGL150000100_DRM_M.m3u8?
     *             param=aa4759ff6fd8246cdea83059bf90e87b727c92f872ddc78107544f10c94
     *             27078bf369bf5e7bd50fc27b2d11671153ae2e54d10bcf47f3dd49a7bf046d9e5
     *             fed91256cc95602596f1807040faf348e851b17e6570a518a89037534fb887481
     *             ee3a696fb9bc11b1e59d02459f0e9da78f2097059a9825cc884d88627847d4a27
     *             12786e37b8b54a8c70053fe7e8c585318b596db6ac0088ab9afe56a8c8ade2dda
     *             3c65ec1abd79dd541088ae95e9cf3d946?
     *             content_id=20113830&start_file_offset=0&duration=8826000&settlement_type=1&ad=0&user_pin=0&payment_type=0&hls_max_bw=0&srcdata_timeout=15
     * @return error code.
     */
    public static int setDataSource(String path) {
        int retValue;

        if (Log.MSG) {
            LOG.printMsg("setDataSource() - path = " + path);
        }

        retValue = setDataSource0(path);

        if (retValue == OPEN_SUCCEEDED) {
            playerState = PLAYER_STATE_PREPARED;
        }

        if (Log.MSG) {
            LOG.printMsg("setDataSource() - playerState = " + playerState);
        }

        return retValue;
    }

    /**
     * Set video surface.
     *
     * @param surface Surface for video screen.
     * @return error code.
     */
    public static int setVideoSurface(Surface surface) {
        int returnCode;

        if (Log.MSG) {
            LOG.printMsg("setVideoSurface() start.");
        }

        returnCode = setDisplay0(surface);

        if (Log.MSG) {
            LOG.printMsg("setVideoSurface() return " + returnCode + ".");
        }

        return returnCode;
    }

    /**
     * Start play.
     *
     * @param type TYPE_VOD or TYPE_CHANNEL.
     */
    public static void start(int type) {
        if (Log.MSG) {
            LOG.printMsg("start() - type = " + type);
        }

        if (audioTrack != null) {
            audioTrack.release();
        }


        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // TEST: by suhoshin. should be removed later.
        // String asInfo = getCurrentAsInfo();
        // if (Log.MSG) {
        //     LOG.printMsg("CURRENT AS GET INFO: = " + asInfo);
        // }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////


        int sampleRate = getAudioSampleRate0();

        switch (sampleRate) {
            case AUDIO_SAMPLE_RATE_44100:
            case AUDIO_SAMPLE_RATE_48000:
                break;
            default:
                if (Log.MSG) {
                    LOG.printMsg("start() - wrong sample rate = " + sampleRate);
                }
                switch (type) {
                    case TYPE_VOD:
                        sampleRate = AUDIO_SAMPLE_RATE_48000;
                        break;
                    case TYPE_CHANNEL:
                        sampleRate = AUDIO_SAMPLE_RATE_44100;
                        break;
                }
                break;
        }

        if (Log.MSG) {
            LOG.printMsg("start() - sample rate = " + sampleRate);
        }

        int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        if (Log.MSG) {
            LOG.printMsg("start() - minBufferSize = " + minBufferSize);
        }

        final int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 18) {
            minBufferSize *= 2;
        }

        if (Log.MSG) {
            LOG.printMsg("start() - adjusted minBufferSize = " + minBufferSize);
        }
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);


        setAudioTrack0(audioTrack);
        start0();

        playerState = PLAYER_STATE_PLAYING;
    }

    /**
     * Stop play.
     * Not used in Viettel. Use release() instead of this stop().
     */
    public static void stop() {
        if (Log.MSG) {
            LOG.printMsg("stop()");
        }
        stop0();

        playerState = PLAYER_STATE_STOPPED;
    }

    /**
     * Stop and release the current play.
     */
    public static void release() {
        if (Log.MSG) {
            LOG.printMsg("release()");
        }
        release0();

        // Release audio resources on exit.
        if (audioTrack != null) {
            audioTrack.release();
            audioTrack = null;
        }
        removeAltiPlayerListener();

        playerState = PLAYER_STATE_IDLE;
    }

    /**
     * Resume the current play.
     */
    public static void resume() {
        if (Log.MSG) {
            LOG.printMsg("resume()");
        }
        if (audioTrack != null) {
            audioTrack.play();
        }
        resume0();
        playerState = PLAYER_STATE_PLAYING;
    }


    /**
     * Pause the current play.
     */
    public static void pause() {
        if (Log.MSG) {
            LOG.printMsg("pause()");
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // TEST: should be removed later.
        // String asMode = "{ \"protocol_type\" : \"alticast.android.media.player\", \"version\": \"1.0.0\", \"return_code\": 0, \"as_set_mode\" : 1 }";
        //        
        // int asRet = setCurrentAsMode(asMode);
        // if (Log.MSG) {
        //     LOG.printMsg("CURRENT AS SET MODE: = " + asRet);
        // }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////        


        pause0();
        if (audioTrack != null) {
            audioTrack.pause();
        }


        playerState = PLAYER_STATE_PAUSED;

        if (Log.MSG) {
            LOG.printMsg("pause()   done :" + playerState);
        }
    }

    /**
     * Seek to position.
     * In HLS service, Position is time offset.
     *
     * @param position         Position to seek. Unit is byte or millisecond.
     * @param seekByFileOffset If position is byte offset, set true.
     *                         If position is time offset, set false.
     */
    public static void seekTo(int position, boolean seekByFileOffset) {
        if (Log.MSG) {
            LOG.printMsg("seekTo() - position = " + position + " , seekByFileOffset = " + seekByFileOffset);
        }
        seekTo0(position, seekByFileOffset);
    }

    /**
     * Get position of the current playing time.
     *
     * @return current position by millisecond.
     */
    public static int getCurrentPosition() {
//        if (Log.DBG) {
//            LOG.printDbg("getCurrentPosition()");
//        }
        return getCurrentPosition0();
    }

    /**
     * Get file offset of the current playing time.
     * Not used in Viettel.
     *
     * @return current file offset by byte.
     */
    public static long getCurrentFileOffset() {
        if (Log.DBG) {
            LOG.printDbg("getCurrentFileOffset()");
        }
        return getCurrentFileOffset0();
    }

    /**
     * Get the height of the current video.
     *
     * @return height of video.
     */
    public static int getVideoHeight() {
        if (Log.MSG) {
            LOG.printMsg("getVideoHeight()");
        }
        return getVideoHeight0();
    }

    /**
     * Get the width of the current video.
     *
     * @return width of video.
     */
    public static int getVideoWidth() {
        if (Log.MSG) {
            LOG.printMsg("getVideoWidth()");
        }
        return getVideoWidth0();
    }

    /**
     * Get the state of player.
     *
     * @return 1 : playing, 0 : paused or stopped.
     */
    public static boolean isPlaying() {
        return isPlaying0();
    }

    /**
     * Get the duration of the current content.
     *
     * @return duration by millisecond.
     */
    public static int getDuration() {
//        if (Log.MSG) {
//            LOG.printMsg("getDuration()");
//        }
        return getDuration0();
    }

    /**
     * Get the size of the current content.
     *
     * @return size by byte.
     */
    public static long getFileSize() {
        if (Log.MSG) {
            LOG.printMsg("getFileSize");
        }
        return getFileSize0();
    }

    /**
     * Get the as informations of the current content.
     *
     * @return as information json formatted(success:return_code > 0, fail: return_code <= 0).
     */
    public static String getCurrentAsInfo() {
        if (Log.MSG) {
            LOG.printMsg("getCurrentAsInfo");
        }
        return getCurrentAsInfo0();
    }

    /**
     * Set the as mode.
     *
     * @return success: >= 0, fail: < 0.
     */
    public static int setCurrentAsMode(String asMode) {
        if (Log.MSG) {
            LOG.printMsg("setCurrentAsMode");
        }
        int result = setCurrentAsMode0(asMode);

        return result;
    }

    /**
     * Set FTP server IP address.
     * Not used in Viettel.
     */
    public static void setServerIp(String[] serverIp) {

        StringBuilder sb = new StringBuilder();
        for (String ip : serverIp) {
            sb.append(ip).append("|");
        }

        if (Log.MSG) {
            LOG.printMsg("setServerIp() - " + sb.toString());
        }

        setServerIp0(sb.toString());
    }

    /**
     * Called when the end of a media source is reached during playback.
     */
    private static void onCompletion() {
        if (Log.MSG) {
            LOG.printMsg("onCompletion()");
        }
        if (listener != null) {
            listener.onComplete();
        }
    }

    /**
     * Called to update status in buffering a media stream.
     *
     * @param percent the percentage (0-100) of the buffer that has been filled.
     */
    private static void onBufferingUpdate(int percent) {
        if (Log.MSG) {
            LOG.printMsg("onBufferingUpdate()");
        }
        if (listener != null) {
            listener.onBufferingUpdate(percent);
        }
    }

    /**
     * Called on video resolution changed.
     *
     * @param width  New width of video.
     * @param height New height of video.
     */
    private static void onVideoSizeChanged(int width, int height) {
        if (Log.MSG) {
            LOG.printMsg("onVideoSizeChanged() - width = " + width + " , height = " + height);
        }
        if (listener != null) {
            listener.onVideoSizeChanged(width, height);
        }
    }

    /**
     * Called when video rendering start.
     *
     * @param width  New width of video.
     * @param height New height of video.
     */
    private static void onVideoRenderingStarted(int width, int height) {
        if (Log.MSG) {
            LOG.printMsg("onVideoRenderingStarted() - width = " + width + " , height = " + height);
        }

        if (listener != null) {
            listener.onVideoRenderingStarted(width, height);
        }
    }

    /**
     * Called when error occurs in player.
     *
     * @param whyError   Error code.
     * @param extraError Extra error code.
     * @return error code.
     */
    private static int getNativeError(int whyError, int extraError) {
        if (Log.MSG) {
            LOG.printMsg("getNativeError " + whyError + ", " + extraError);
        }
        if (listener != null) {
            listener.onError(whyError, extraError);
        }
        return whyError;
    }

    /**
     * Start total listener of player in native.
     */
    private static void startPlayerListener() {

        new Thread() {
            public void run() {
                if (Log.MSG) {
                    LOG.printMsg("startPlayerListener() start.");
                }
                startTotalListener0();
            }
        }.start();
    }

    public static boolean supportHwDocoder() {
        return supportHwDecoder0();
    }

    /**
     * Set the flag of hw video decoder use.
     * This call works in idle state of player.
     *
     * @param useHwDecoder true(use hw decoder), false(use sw decoder)
     * @return error code true(success), false(fail).
     */
    public static boolean setVideoHwDocoderUse(boolean useHwDecoder) {
        if (Log.MSG) {
            LOG.printMsg("setVideoHwDocoderUse. useHwDecoder - " + useHwDecoder);
        }

        if (playerState != PLAYER_STATE_IDLE) {
            if (Log.MSG) {
                LOG.printMsg("setVideoHwDocoderUse. Invalid state of player - " + playerState);
            }
            return false;
        }

        if (mVideoHwCodecUse == useHwDecoder) {
            if (Log.MSG) {
                LOG.printMsg("setVideoHwDocoderUse. mVideoHwCodecUse == useHwDecoder, so nothing to do.");
            }
            return true;
        }

        useHwDecoder0(useHwDecoder);

        mVideoHwCodecUse = supportHwDecoder0();

        if (Log.MSG) {
            LOG.printMsg("setVideoHwDocoderUse. mVideoHwCodecUse - " + mVideoHwCodecUse);
        }
        return mVideoHwCodecUse;
    }

    /**
     * Get the flag of hw video decoder use.
     *
     * @return the flag of hw video decoder use.
     */
    public static boolean getVideoHwDocoderUse() {
        if (Log.MSG) {
            LOG.printMsg("getVideoHwDocoderUse. mVideoHwCodecUse - " + mVideoHwCodecUse);
        }
        return mVideoHwCodecUse;
    }

    public static int setMasVSPMode(int mode) {
        if (Log.MSG) {
            LOG.printMsg("setMasVSPMode called. mode - " + mode);
        }
        return mode;
    }

    public static int setMasPreset(int preset) {
        if (Log.MSG) {
            LOG.printMsg("setMasPreset called. preset - " + preset);
        }
        return preset;
    }

    public static int setMasDistance(int distance) {
        if (Log.MSG) {
            LOG.printMsg("setMasDistance called. distance - " + distance);
        }
        return distance;
    }

    public static int setMasOnOff(int on_off) {
        if (Log.MSG) {
            LOG.printMsg("setMasOnOff called. on_off - " + on_off);
        }
        return on_off;
    }

    public static int setAudioPassthrough(int flag) {

        if (Log.MSG) {
            LOG.printMsg("setAudioPassthrough called. flag - " + flag);
        }
        return flag;
    }

    public static int evtAvDevices(int event) {
        if (Log.MSG) {
            LOG.printMsg("evtAvDevices called. event - " + event);
        }
        return event;
    }

    private static String removeChar(String str, char ch) {
        String cleanStr = "";

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ch)
                cleanStr += str.charAt(i);
        }

        return cleanStr;
    }

//    public static String GetDeviceUniqueID(Context mContext) {
//
//        final String deviceInfoFile = "device_info.dat";
//
//        File infoFile = new File(mConfigFileDir, deviceInfoFile);
//        if (Log.MSG) {
//            LOG.printMsg("GetDeviceUniqueID() = " + infoFile);
//        }
//
//        try {
//            if (!infoFile.exists()) {
//                String deviceId = null;
//
//                // Generate device unique id.
//                // For production
//                if (true) { // [TODO] should be true in production.
//                    final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//                    if (tm == null) {
//                        if (Log.MSG) {
//                            LOG.printMsg("Oops, tm is null.");
//                        }
//                    }
//
//                    final String tmDevice, tmSerial, androidId;
//                    tmDevice = tm.getDeviceId();
//                    tmSerial = tm.getSimSerialNumber();
//                    androidId = android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//                    if ((tmDevice == null || tmDevice.length() == 0) &&
//                            (tmSerial == null || tmSerial.length() == 0) &&
//                            (androidId == null || androidId.length() == 0)) {
//                        deviceId = UUID.randomUUID().toString();
//                        if (Log.MSG) {
//                            LOG.printMsg("All is null. so device id made by sth. = " + deviceId);
//                        }
//                    } else {
//                        UUID deviceUuid;
//                        String deviceIdSet = null;
//                        if (androidId != null) {
//                            deviceIdSet = androidId;
//                        }
//
//                        if (tmDevice != null) {
//                            deviceIdSet += tmDevice;
//                        }
//
//                        if (tmSerial != null) {
//                            deviceIdSet += tmSerial;
//                        }
//
//                        deviceUuid = UUID.nameUUIDFromBytes(deviceIdSet.getBytes());
//                        deviceId = deviceUuid.toString();
//                    }
//
//                    // Remove '-' character in deviceId.
//                    deviceId = removeChar(deviceId, '-');
//                }
//                // For Test bed
//                else {
//                    WifiManager manager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//                    WifiInfo info = manager.getConnectionInfo();
//                    deviceId = info.getMacAddress();
//
//                    // Remove '-' charaters in deviceId.
//                    deviceId = removeChar(deviceId, ':');
//                    deviceId = "77f16ca4b33b3d10a72aee780ff54463";
//                }
//
//                if (Log.MSG) {
//                    LOG.printMsg("After removing delimiter in device id : " + deviceId);
//                }
//
//                // Write id to info file.
//                FileOutputStream outInfoFile = new FileOutputStream(infoFile);
//                outInfoFile.write(deviceId.getBytes());
//                outInfoFile.close();
//                // [TODO] should be removed later
//                if (Log.MSG) {
//                    LOG.printMsg("First generation : device id = " + deviceId);
//                }
//            }
//
//            // Read device unique id from infoFile.
//            RandomAccessFile readInfoFile = new RandomAccessFile(infoFile, "r");
//            byte[] deviceIdBytes = new byte[(int) readInfoFile.length()];
//            readInfoFile.readFully(deviceIdBytes);
//            readInfoFile.close();
//
//            String deviceId = new String(deviceIdBytes);
//
//            return deviceId;
//
//        } catch (Exception e) {
//            return "";
////            throw new RuntimeException(e);
//        }
//    }
    public static String GetDeviceUniqueID() {

        if(WindmillConfiguration.deviceId != null) {
            return WindmillConfiguration.deviceId;
        }

        final String deviceInfoFile = "device_info.dat";

        File infoFile = new File(mConfigFileDir, deviceInfoFile);
        if (Log.MSG) {
            LOG.printMsg("GetDeviceUniqueID() = " + infoFile);
        }

        try {
            if (!infoFile.exists()) {
                String deviceId = null;

                // Generate device unique id.
                // For production
                if (true) { // [TODO] should be true in production.
                    final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                    if (tm == null) {
                        if (Log.MSG) {
                            LOG.printMsg("Oops, tm is null.");
                        }
                    }

                    final String tmDevice, tmSerial, androidId;
                    tmDevice = tm.getDeviceId();
                    tmSerial = tm.getSimSerialNumber();
                    androidId = android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    if ((tmDevice == null || tmDevice.length() == 0) &&
                            (tmSerial == null || tmSerial.length() == 0) &&
                            (androidId == null || androidId.length() == 0)) {
                        deviceId = UUID.randomUUID().toString();
                        if (Log.MSG) {
                            LOG.printMsg("All is null. so device id made by sth. = " + deviceId);
                        }
                    } else {
                        UUID deviceUuid;
                        String deviceIdSet = null;
                        if (androidId != null) {
                            deviceIdSet = androidId;
                        }

                        if (tmDevice != null) {
                            deviceIdSet += tmDevice;
                        }

                        if (tmSerial != null) {
                            deviceIdSet += tmSerial;
                        }

                        deviceUuid = UUID.nameUUIDFromBytes(deviceIdSet.getBytes());
                        deviceId = deviceUuid.toString();
                    }

                    // Remove '-' character in deviceId.
                    deviceId = removeChar(deviceId, '-');
                }
                // For Test bed
                else {
                    WifiManager manager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = manager.getConnectionInfo();
                    deviceId = info.getMacAddress();

                    // Remove '-' charaters in deviceId.
                    deviceId = removeChar(deviceId, ':');
                    deviceId = "77f16ca4b33b3d10a72aee780ff54463";
                }

                if (Log.MSG) {
                    LOG.printMsg("After removing delimiter in device id : " + deviceId);
                }

                // Write id to info file.
                FileOutputStream outInfoFile = new FileOutputStream(infoFile);
                outInfoFile.write(deviceId.getBytes());
                outInfoFile.close();
                // [TODO] should be removed later
                if (Log.MSG) {
                    LOG.printMsg("First generation : device id = " + deviceId);
                }
            }

            // Read device unique id from infoFile.
            RandomAccessFile readInfoFile = new RandomAccessFile(infoFile, "r");
            byte[] deviceIdBytes = new byte[(int) readInfoFile.length()];
            readInfoFile.readFully(deviceIdBytes);
            readInfoFile.close();

            String deviceId = new String(deviceIdBytes);

            if(deviceId == null || deviceId.length() == 0) {
                return UUID.randomUUID().toString();
            }

            return deviceId;

        } catch (Exception e) {
//            return "";
            return UUID.randomUUID().toString();
//            throw new RuntimeException(e);
        }
    }

    private static native int startTotalListener0();

    private static native int setDataSource0(String path);

    private static native int setDisplay0(Surface surface);

    private static native void setAudioTrack0(AudioTrack audioTrack);

    private static native void start0();

    private static native void stop0();

    private static native void pause0();

    private static native void resume0();

    private static native void release0();

    private static native int getCurrentPosition0();

    private static native long getCurrentFileOffset0();

    private static native int getVideoHeight0();

    private static native int getVideoWidth0();

    private static native boolean isPlaying0();

    private static native int getDuration0();

    private static native void seekTo0(int position, boolean seekByFileOffset);

    private static native long getFileSize0();

    private static native int getAudioSampleRate0();

    private static native String getCurrentAsInfo0();

    private static native int setCurrentAsMode0(String asMode);

    private static native void setConfigFileDir0(String configFileDir);

    private static native void setServerIp0(String ip);

    private static native boolean supportHwDecoder0();

    private static native boolean useHwDecoder0(boolean use);

    private static native void setHttpUserAgent0(String httpUserAgent);

    private static native void setDeviceUniqueIdentifier0(String deviceId);
    private static AltiPlayerListener videoPlayBackListener;
    public static void setAltiPlaybackListener(AltiPlayerListener l) {
        if (Log.MSG) {
            LOG.printMsg("setAltiPlayerListener() start.");
        }

        videoPlayBackListener = l;
    }
    public static void recvFingerprint(String data) {

    }

}