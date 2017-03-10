/**
 * 
 */
package com.youanmi.fastdfs.utils;

import com.youanmi.scrm.commons.util.file.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Properties;


/**
 * 文件服务器操作工具类
 * 
 * @author chenwenlong
 */
public class FastDFSUtil {

    private static final String PIC_PROT_HTTP = "http://";

    private static final Logger LOG = LoggerFactory.getLogger(FastDFSUtil.class);

    private static String FILE_URL_HOST = null;// 文件地址

    private FastDFSUtil() {
    }

    /**
     *
     * 获取classpath
     *
     * @return
     */
    public static String getClassPath() {
        URL url = FastDFSUtil.class.getClassLoader().getResource("");
        return url.getPath();
    }

    /**
     *
     * 初始化
     *
     * @param fileName
     */
    public static void build(String fileName) {

        // 获取图片地址
        Properties pros = PropertiesUtils.getProperties(fileName);
        FILE_URL_HOST = pros.getProperty("file_url_host");
        if (StringUtils.isBlank(FILE_URL_HOST)) {
            throw new RuntimeException("fastdfs配置文件file_url_host没有找到");
        }

        LOG.info("FILE_URL_HOST has changed." + FILE_URL_HOST);

        String classPath = getClassPath();
        try {
            ClientGlobal.init(classPath + fileName);
            // ClientGlobal.init("D:/fdfs_client.conf");
        }
        catch (Exception e) {
            LOG.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    // public static void main(String[] args) {
    // build(null);
    // long start = System.currentTimeMillis();
    // FastDFSUtil.uploadFile("d:/100.jpg", null);
    //
    // long end = System.currentTimeMillis();
    // System.out.println(end - start);
    // // 9336
    // }

    /**
     * 用于创建缩略图
     *
     * @param fileId 服务器上的文件ID
     * @param prifix 在fileid上后缀修饰 如100X100
     * @param imageFile 本地路径
     * @param metaList 效果 group1/M00/00/A9/wKgBq1S047KAVw-kAFkOyLNc6SQ7158801
     *            group1/M00/00/A9/wKgBq1S047KAVw-kAFkOyLNc6SQ7158801_100X100
     * @return
     */
    public static String updateFile(String fileId, String prifix, String imageFile, NameValuePair[] metaList) {

        imageFile = getFileIdFromFile(imageFile);
        String fileid = "";
        TrackerServer ts = null;
        StorageClient1 sc1 = null;
        StorageServer ss = null;
        try {
            TrackerClient tc = new TrackerClient();
            ts = tc.getConnection();

            if (null == ts) {
                throw new RuntimeException("连接fastdsf失败!");
            }
            ss = tc.getStoreStorage(ts);
            sc1 = new StorageClient1(ts, ss);

            // 不需要文件的后缀显示
            fileid = sc1.upload_file1(fileId, prifix, imageFile, "", metaList);

        }
        catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("上传文件失败" + ExceptionUtils.getStackTrace(e));
            }
            throw new RuntimeException(e);
        }
        finally {
            closeStorage(ss);
            closeTracker(ts);

        }
        String fullFilePath = PIC_PROT_HTTP + getImageIPAddress() + "/" + fileid;
        return fullFilePath;

    }

    /**
     *
     * 获取fastdfs host地址
     *
     * @return
     */
    public static String getImageIPAddress() {
        if (StringUtils.isNotBlank(FILE_URL_HOST)) {
            return FILE_URL_HOST;
        }
        InetSocketAddress[] trackerServers = ClientGlobal.getG_tracker_group().tracker_servers;
        if (null == trackerServers) {
            throw new RuntimeException("get fastdfs url fail!");
        }
        return trackerServers[0].getAddress().getHostName();
    }

    /**
     *
     * 检查文件是否包含协议头如果有则解析出文件ID http://192.168.1.171/
     *
     * @param fileUrl
     * @return
     */
    public static String getFileIdFromFile(String fileUrl) {
        if (-1 != fileUrl.indexOf(PIC_PROT_HTTP)) {
            int len = getImageIPAddress().length() + PIC_PROT_HTTP.length();
            return fileUrl.substring(len + 1, fileUrl.length());
        }

        return fileUrl;
    }

    /**
     * 文件上传
     *
     * @param imageFile
     * @param metaList
     * @return
     */
    public static String uploadFile(String imageFile, String fileType, NameValuePair[] metaList) {
        imageFile = getFileIdFromFile(imageFile);
        // String classPath = getClassPath();
        String fileid = "";
        TrackerServer ts = null;
        StorageServer ss = null;
        try {
            // ClientGlobal.init(classPath + FAST_CONF_FILE);
            TrackerClient tc = new TrackerClient();
            ts = tc.getConnection();
            if (null == ts) {
                throw new RuntimeException("连接fastdsf失败!");
            }
            ss = tc.getStoreStorage(ts);
            StorageClient1 sc1 = new StorageClient1(ts, ss);

            fileid = sc1.upload_file1(imageFile, fileType, metaList);

        }
        catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("上传文件失败" + ExceptionUtils.getStackTrace(e));
            }
            throw new RuntimeException(e);
        }
        finally {
            closeStorage(ss);
            closeTracker(ts);

        }
        String fullFilePath = PIC_PROT_HTTP + getImageIPAddress() + "/" + fileid;
        return fullFilePath;
    }

    /**
     * 文件上传
     *
     * @param imageFile
     * @param metaList
     * @return
     */
    public static String uploadFile(String imageFile, NameValuePair[] metaList) {
        imageFile = getFileIdFromFile(imageFile);
        // String classPath = getClassPath();
        String fileid = "";
        TrackerServer ts = null;
        StorageServer ss = null;
        try {
            // ClientGlobal.init(classPath + FAST_CONF_FILE);
            TrackerClient tc = new TrackerClient();
            ts = tc.getConnection();
            if (null == ts) {
                throw new RuntimeException("连接fastdsf失败!");
            }
            ss = tc.getStoreStorage(ts);
            StorageClient1 sc1 = new StorageClient1(ts, ss);

            // 获取文件后缀
            int prifix = imageFile.lastIndexOf(".");

            String prifixType = imageFile.substring(prifix + 1, imageFile.length());

            fileid = sc1.upload_file1(imageFile, prifixType, metaList);

        }
        catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("上传文件失败" + ExceptionUtils.getStackTrace(e));
            }
            throw new RuntimeException(e);
        }
        finally {
            closeStorage(ss);
            closeTracker(ts);

        }
        String fullFilePath = PIC_PROT_HTTP + getImageIPAddress() + "/" + fileid;
        return fullFilePath;
    }

    /**
     * 文件上传,上传文件后校验文件的大小与服务器的文件大小是否相同,不相同则重新上传
     *
     * @author tanguojun 2016年3月17日
     * @param imageFile
     * @param metaList
     * @param maxErrorRetryNum 上次失败后最大的重试次数
     * @return
     */
    public static String uploadFile(String imageFile, NameValuePair[] metaList, int maxErrorRetryNum) {
        // 失败次数获取
        maxErrorRetryNum = maxErrorRetryNum < 1 ? 1 : (maxErrorRetryNum > 10 ? 10 : maxErrorRetryNum);
        imageFile = getFileIdFromFile(imageFile);
        String fileid = null;
        TrackerServer ts = null;
        StorageServer ss = null;
        try {
            TrackerClient tc = new TrackerClient();
            ts = tc.getConnection();
            if (null == ts) {
                throw new RuntimeException("连接fastdsf失败!");
            }
            ss = tc.getStoreStorage(ts);
            StorageClient1 sc1 = new StorageClient1(ts, ss);

            // 获取文件后缀
            int prifix = imageFile.lastIndexOf(".");

            String prifixType = imageFile.substring(prifix + 1, imageFile.length());
            // 先判断后自减
            while (maxErrorRetryNum-- > 0) {
                try {

                    fileid = sc1.upload_file1(imageFile, prifixType, metaList);

                    // 获取远程文件信息
                    FileInfo fileInfo = sc1.query_file_info1(fileid);

                    long localFileSize = new File(imageFile).length();

                    // 本地文件大小和远程文件大小不一样,重新上传
                    if (localFileSize != fileInfo.getFileSize()) {
                        // 打印错误日志
                        LOG.error("local file size not equals remote file size");
                        // 删除错误文件
                        sc1.delete_file1(fileid);

                        throw new Exception("local file size not equals remote file size");
                    }

                    // 上传成功
                    break;

                }
                catch (Exception e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("上传文件失败" + ExceptionUtils.getStackTrace(e));
                    }
                    if (maxErrorRetryNum == 0) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        catch (Exception e1) {
            if (LOG.isErrorEnabled()) {
                LOG.error("上传文件失败" + ExceptionUtils.getStackTrace(e1));
            }
            throw new RuntimeException(e1);
        }
        finally {
            closeStorage(ss);
            closeTracker(ts);
        }
        String fullFilePath = PIC_PROT_HTTP + getImageIPAddress() + "/" + fileid;
        return fullFilePath;
    }

    /**
     *
     * 根据本地绝对路径 上传文件
     *
     * @param url 本地绝对路
     * @return
     */
    public static String uploadFile(String url) {
        TrackerServer ts = null;
        StorageServer ss = null;
        try {
            TrackerClient tracker = new TrackerClient();
            ts = tracker.getConnection();
            StorageClient storageClient = new StorageClient(ts, ss);
            String[] fileIds = storageClient.upload_file(url, null, null);

            return new StringBuffer().append(PIC_PROT_HTTP).append(getImageIPAddress()).append("/")
                .append(fileIds[0]).append("/").append(fileIds[1]).toString();

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            closeStorage(ss);
            closeTracker(ts);
        }
    }

    /**
     * 文件上传
     *
     * @param fileByte
     * @param fileType
     * @param metalist
     * @return
     */
    public static String uploadFile(byte[] fileByte, String fileType, NameValuePair[] metalist) {
        TrackerServer ts = null;
        StorageServer ss = null;

        try {
            TrackerClient tracker = new TrackerClient();
            ts = tracker.getConnection();
            ss = tracker.getStoreStorage(ts);
            StorageClient storageClient = new StorageClient(ts, ss);

            String[] fileIds = storageClient.upload_file(fileByte, fileType, metalist);
            return new StringBuffer().append(PIC_PROT_HTTP).append(getImageIPAddress()).append("/")
                .append(fileIds[0]).append("/").append(fileIds[1]).toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            closeStorage(ss);
            closeTracker(ts);
        }

    }

    /**
     * 删除文件
     */
    public static void delFile(String fileId) {
        fileId = getFileIdFromFile(fileId);

        String fileid = fileId;
        TrackerServer ts = null;
        StorageServer ss = null;
        try {
            TrackerClient tc = new TrackerClient();
            ts = tc.getConnection();
            if (null == ts) {
                throw new RuntimeException("连接fastdsf失败!");
            }
            ss = tc.getStoreStorage(ts);
            StorageClient1 sc1 = new StorageClient1(ts, ss);
            if (LOG.isInfoEnabled()) {
                LOG.info("delete file :" + fileid);
            }
            sc1.delete_file1(fileid);
        }
        catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("删除文件失败" + ExceptionUtils.getStackTrace(e));
            }
            throw new RuntimeException(e);
        }
        finally {
            closeStorage(ss);
            closeTracker(ts);
        }

    }

    /**
     * 获取文件
     */
    public static byte[] getFile(String fileId) {
        byte[] file = null;
        fileId = getFileIdFromFile(fileId);

        String fileid = fileId;
        TrackerServer ts = null;
        StorageServer ss = null;
        try {
            TrackerClient tc = new TrackerClient();
            ts = tc.getConnection();
            if (null == ts) {
                throw new RuntimeException("连接fastdsf失败!");
            }
            ss = tc.getStoreStorage(ts);
            StorageClient1 sc1 = new StorageClient1(ts, ss);
            file = sc1.download_file1(fileid);
            return file;
        }
        catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("获取文件失败" + ExceptionUtils.getStackTrace(e));
            }
            throw new RuntimeException(e);
        }
        finally {
            closeStorage(ss);
            closeTracker(ts);
        }

    }

    /**
     *
     * tracker server connection closeTracker.
     *
     * @autor 龙汀
     * @date 2016-04-14
     *
     * @param ts
     */
    private static void closeTracker(TrackerServer ts) {
        if (ts != null) {
            try {
                ts.close();
                ts = null;
            }
            catch (IOException e) {
                LOG.error("Close TrackerServer error" + e);
            }
        }
    }

    /**
     *
     * storage server connection closeTracker.
     *
     * @autor 龙汀
     * @date 2016-04-14
     *
     * @param ss
     */
    private static void closeStorage(StorageServer ss) {
        if (ss != null) {
            try {
                ss.close();
                ss = null;
            }
            catch (IOException e) {
                LOG.error("Close StorageServer error" + e);
            }
        }
    }
}
