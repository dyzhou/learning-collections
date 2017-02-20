/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-26
 * Project        : technician-thirdparty-server
 * File Name      : ImageUploadClient.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.httpclient.meituan;

import java.io.File;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-26
 * @since technician-thirdparty-server 1.0
 */
public interface ImageUploadClient
{
    ImageResult postFile(File file, String filename);

    ImageResult postFile(byte[] bytes, String srcFilename, String desFilename);

    ImageResult postFile(File file, String desFilename, String maxHeight, String maxWidth, String keepProportion, String needJudgeExist);

    ImageResult postFile(File file, String srcFilename, String desFilename, String maxHeight, String maxWidth, String keepProportion, String needJudgeExist);

    ImageResult postFile(byte[] bytes, String srcFilename, String desFilename, String maxHeight, String maxWidth, String keepProportion, String needJudgeExist);
}
