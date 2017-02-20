/*
 * Create Author  : shang.gao
 * Create Date    : 2016-07-26
 * Project        : technician-thirdparty-server
 * File Name      : ImageUploadClientImpl.java
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.dianping.demo.httpclient.meituan;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * 功能描述: 参考wiki： http://wiki.sankuai.com/pages/viewpage.action?pageId=354735367 <p>
 *
 * @author : dengyun.zhou <p>
 * @version 1.0 2016-07-26
 * @since technician-thirdparty-server 1.0
 */
public class ImageUploadClientImpl implements ImageUploadClient
{

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImageUploadClientImpl.class);

    private String urlBasic = "http://pic-in.meituan.com/storage/";

    private String bucket = "";

    private String client_id = "";

    private String client_secret = "";

    public ImageUploadClientImpl(String bucket, String client_id, String client_secret)
    {
        this.bucket = bucket;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }

    @Override
    public ImageResult postFile(File file, String filename)
    {
        return this.postFile(file, filename, null, null, null, null);
    }

    @Override
    public ImageResult postFile(byte[] bytes, String srcFilename, String desFilename)
    {
        return this.postFile(bytes, srcFilename, desFilename, null, null, null, null);
    }

    @Override
    public ImageResult postFile(byte[] bytes, String srcFilename, String desFilename, String maxHeight, String maxWidth, String keepProportion, String needJudgeExist)
    {
        if (bytes == null)
        {
            ImageResult builder1 = new ImageResult();
            builder1.setSuccess(false);
            return builder1;
        } else
        {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().addPart("file", new ByteArrayBody(bytes, ContentType.DEFAULT_BINARY, srcFilename));
            String httpResponse = this.post(builder, desFilename, maxHeight, maxWidth, keepProportion, needJudgeExist);
            return ImageResult.toImageResult(httpResponse);
        }
    }

    @Override
    public ImageResult postFile(File file, String desFilename, String maxHeight, String maxWidth, String keepProportion, String needJudgeExist)
    {
        if (file == null)
        {
            ImageResult builder1 = new ImageResult();
            builder1.setSuccess(false);
            return builder1;
        } else
        {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().addPart("file", new FileBody(file));
            String httpResponse = this.post(builder, desFilename, maxHeight, maxWidth, keepProportion, needJudgeExist);
            return ImageResult.toImageResult(httpResponse);
        }
    }

    @Override
    public ImageResult postFile(File file, String srcFilename, String desFilename, String maxHeight, String maxWidth, String keepProportion, String needJudgeExist)
    {
        if (file == null)
        {
            ImageResult builder1 = new ImageResult();
            builder1.setSuccess(false);
            return builder1;
        } else
        {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().addPart("file", new FileBody(file, ContentType.DEFAULT_BINARY, srcFilename));
            String httpResponse = this.post(builder, desFilename, maxHeight, maxWidth, keepProportion, needJudgeExist);
            return ImageResult.toImageResult(httpResponse);
        }
    }

    private String post(MultipartEntityBuilder httpEntityBuilder, String filename, String maxHeight, String maxWidth, String keepProportion, String needJudgeExist)
    {
        if (filename != null)
        {
            httpEntityBuilder.addTextBody("filename", filename);
        }
        if (maxHeight != null)
        {
            httpEntityBuilder.addTextBody("maxHeight", maxHeight);
        }
        if (maxWidth != null)
        {
            httpEntityBuilder.addTextBody("maxWidth", maxWidth);
        }
        if (keepProportion != null)
        {
            httpEntityBuilder.addTextBody("keepProportion", keepProportion);
        }
        if (needJudgeExist != null)
        {
            httpEntityBuilder.addTextBody("needJudgeExist", needJudgeExist);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = this.urlBasic + this.bucket;
        HttpPost post = new HttpPost(url);
        try
        {
            Date date = new Date();
            URL u;
            try
            {
                u = new URL(url);
            }
            catch (MalformedURLException e)
            {
                logger.error(e.getMessage(), e);
                throw e;
            }
            if (this.client_id != null)
            {
                String response = u.getPath();
                String dateString = MeituanBAUtils.toGmtString(date);
                String authorization = MeituanBAUtils.toAuthorization(dateString, response, this.client_secret, this.client_id);
                post.setHeader("Date", dateString);
                post.setHeader("Authorization", authorization);
            }
            if (httpEntityBuilder != null)
            {
                post.setEntity(httpEntityBuilder.build());
            }
            CloseableHttpResponse response1 = httpclient.execute(post);
            if (response1.getStatusLine().getStatusCode() == 200)
            {
                String responseStr = EntityUtils.toString(response1.getEntity());
                return responseStr;
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return null;
        }
        finally
        {
            try
            {
                httpclient.close();
            }
            catch (Exception e)
            {
                ;
            }

        }
        return null;
    }
}
