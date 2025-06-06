/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.oss.platform;

import com.aizuda.oss.AbstractFileStorage;
import com.aizuda.oss.MultipartUploadResponse;
import com.aizuda.oss.autoconfigure.OssProperty;
import com.aizuda.oss.model.OssResult;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * amazon aws s3 存储
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 一百多斤的雪松
 * @since 1.1.0
 */
public class AWSS3 extends AbstractFileStorage {

    private AmazonS3 s3Client;

    public AWSS3(OssProperty ossProperty) {
        this.ossProperty = ossProperty;
        BasicAWSCredentials credentials = new BasicAWSCredentials(ossProperty.getAccessKey(), ossProperty.getSecretKey());
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                ossProperty.getEndpoint(), Regions.DEFAULT_REGION.getName());
        s3Client = AmazonS3ClientBuilder.standard()
                // 凭证设置
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                // endpoint设置
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    public AmazonS3 getS3Client() {
        return s3Client;
    }

    @Override
    public OssResult upload(InputStream is, String filename, String objectName) throws Exception {
        String bucketName = this.getBucketName();
        String suffix = this.getFileSuffix(filename);
        objectName = this.getObjectName(suffix, objectName);

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(is.available());

        PutObjectRequest req = new PutObjectRequest(bucketName, objectName, is, meta);
        PutObjectResult putObjRet = s3Client.putObject(req);

        return null == putObjRet ? null : OssResult.builder().bucketName(bucketName)
                .objectName(objectName)
                .versionId(putObjRet.getVersionId())
                .filename(filename)
                .suffix(suffix)
                .build();
    }

    @Override
    public InputStream download(String objectName) throws Exception {
        String bucketName = this.getBucketName();
        S3Object ossObject = s3Client.getObject(bucketName, objectName);
        return null == ossObject ? null : ossObject.getObjectContent();
    }

    @Override
    public boolean delete(List<String> objectNameList) throws Exception {
        String bucketName = this.getBucketName();
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        List<DeleteObjectsRequest.KeyVersion> keyVersionList = new ArrayList<>();
        for (String objectName : objectNameList) {
            keyVersionList.add(new DeleteObjectsRequest.KeyVersion(objectName));
        }
        deleteObjectsRequest.withKeys(keyVersionList);
        DeleteObjectsResult deleteObjectsResult = s3Client.deleteObjects(deleteObjectsRequest);
        return null != deleteObjectsResult;
    }

    @Override
    public boolean delete(String objectName) throws Exception {
        String bucketName = this.getBucketName();
        s3Client.deleteObject(bucketName, objectName);
        return Boolean.TRUE;
    }

    @Override
    public String getUrl(String objectName, int duration, TimeUnit unit) throws Exception {
        String bucketName = this.getBucketName();
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName);
        request.setExpiration(getExpiration(unit.toSeconds(duration)));
        URL url = s3Client.generatePresignedUrl(request);
        return url.toString();
    }

    @Override
    public MultipartUploadResponse getUploadSignedUrl(String filename) {
        String suffix = this.getFileSuffix(filename);
        String objectName = this.getObjectName(suffix, null);
        Date expiration = this.getExpiration(TimeUnit.HOURS.toSeconds(12));
        String bucketName = this.getBucketName();
        URL url = s3Client.generatePresignedUrl(bucketName, objectName, expiration, HttpMethod.PUT);
        return null == url ? null : MultipartUploadResponse.builder().bucketName(bucketName).objectName(objectName)
                .uploadUrl(url.toString()).build();
    }

    protected Date getExpiration(long seconds) {
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(seconds);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
