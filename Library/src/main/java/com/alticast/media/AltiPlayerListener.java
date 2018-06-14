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

public interface AltiPlayerListener {

    public void onComplete();

    public void onBufferingUpdate(int percent);

    public void onVideoSizeChanged(int width, int height);

    public void onError(int why, int extra);


    public void onPrepare(int type);

    public void onVideoRenderingStarted(int width, int height);
}
