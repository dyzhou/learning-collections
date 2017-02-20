/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-26
 * Project        : technician-thirdparty-server
 * File Name      : ImageResult.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.httpclient.meituan;

import com.alibaba.fastjson.JSONObject;

/**
 * 功能描述:  <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2016-07-26
 * @since technician-thirdparty-server 1.0
 */
public class ImageResult
{
    private boolean success = false;

    private String errMsg = "";

    private int errCode = 0;

    private String errType = "";

    private String fileKey = "";

    private String originalFileName = "";

    private int originalFileSize = 0;

    private String originalLink = "";

    private boolean isExist = false;

    public ImageResult()
    {
    }

    public boolean isSuccess()
    {
        return this.success;
    }

    public void setSuccess(boolean result)
    {
        this.success = result;
    }

    public String getErrMsg()
    {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getErrCode()
    {
        return this.errCode;
    }

    public void setErrCode(int errCode)
    {
        this.errCode = errCode;
    }

    public String getFileKey()
    {
        return this.fileKey;
    }

    public void setFileKey(String fileKey)
    {
        this.fileKey = fileKey;
    }

    public String getOriginalFileName()
    {
        return this.originalFileName;
    }

    public void setOriginalFileName(String originalFileName)
    {
        this.originalFileName = originalFileName;
    }

    public int getOriginalFileSize()
    {
        return this.originalFileSize;
    }

    public void setOriginalFileSize(int originalFileSize)
    {
        this.originalFileSize = originalFileSize;
    }

    public String getOriginalLink()
    {
        return this.originalLink;
    }

    public void setOriginalLink(String originalLink)
    {
        this.originalLink = originalLink;
    }

    public String getErrType()
    {
        return this.errType;
    }

    public void setErrType(String errType)
    {
        this.errType = errType;
    }

    public boolean isExist()
    {
        return this.isExist;
    }

    public void setExist(boolean isExist)
    {
        this.isExist = isExist;
    }

    public static ImageResult toImageResult(String httpResponse)
    {
        ImageResult result = new ImageResult();
        if (httpResponse == null)
        {
            result.setSuccess(false);
            return result;
        } else
        {
            JSONObject object = JSONObject.parseObject(httpResponse);
            JSONObject data;
            try
            {
                data = object.getJSONObject("error");
                if (data != null)
                {
                    result.setSuccess(false);
                    result.setErrMsg(data.getString("message"));
                    result.setErrCode(data.getInteger("code"));
                    result.setErrType(data.getString("type"));
                    return result;
                }
            }
            catch (Exception var7)
            {
                ;
            }
            try
            {
                data = object.getJSONObject("data");
                if (data != null)
                {
                    result.setSuccess(true);
                    result.setFileKey(data.getString("fileKey"));
                    result.setOriginalFileName(data.getString("originalFileName"));
                    result.setOriginalFileSize(data.getInteger("originalFileSize"));
                    result.setOriginalLink(data.getString("originalLink"));
                    try
                    {
                        result.setExist(data.getBoolean("isExist"));
                    }
                    catch (Exception var5)
                    {
                        ;
                    }
                    return result;
                }
            }
            catch (Exception var6)
            {
                ;
            }
            return result;
        }
    }
}
