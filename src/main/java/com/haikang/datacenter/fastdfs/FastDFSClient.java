package com.haikang.datacenter.fastdfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FastDFSClient {

    private final static Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    private static String CONFIG_FILENAME = "fast_dfs.conf";

    private static StorageClient1 storageClient1 = null;

    // 初始化FastDFS Client
    static {
        try {
//			CONFIG_FILENAME = System.getProperty("dfs");

            logger.info("fastdfs:配置文件:" + CONFIG_FILENAME);

            ClientGlobal.init(CONFIG_FILENAME);
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                throw new IllegalStateException("getConnection return null");
            } else {
                logger.info("fastdfs:trackerClient初始化成功.");
            }
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null) {
                throw new IllegalStateException("getStoreStorage return null");
            } else {
                logger.info("fastdfs:storageServer:初始化成功.");
            }
            storageClient1 = new StorageClient1(trackerServer, storageServer);

        } catch (Exception e) {
            logger.warn("fastdfs:初始化fastdfs运行环境出错。", e);
        }
    }

    /**
     * 上传文件
     *
     * @param file
     *            文件对象
     * @param fileName
     *            文件名
     * @return
     */
//	public static String uploadFile(File file, String fileName) {
//		return uploadFile(file, fileName, null);
//	}

    /**
     * 上传文件
     *
     * @param buff     文件对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return
     */
    public static String uploadFile(byte[] buff, String fileName, Map<String, String> metaList) {
        logger.debug("fastdfs:文件上传DFS:" + fileName + "." + "开始");
        String fid = null;
        try {
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;

//				logger.debug("fastdfs:开始循环:"+metaList.size());
                Iterator<Entry<String, String>> iterator = metaList.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name, value);
                }
            }
//			logger.debug("fastdfs:开始上传");
            synchronized (FastDFSClient.class) {
                fid = storageClient1.upload_file1(buff, FilenameUtils.getExtension(fileName), nameValuePairs);
            }
//			logger.debug("fastdfs:结束上传");
        } catch (Exception e) {
            logger.warn("fastdfs:出现什么问题了？", e);
            return null;

        }
        logger.debug("fastdfs:文件上传DFS:" + fileName + ",完成");
        return fid;
    }

    /**
     * 获取文件元数据
     *
     * @param fileId 文件ID
     * @return
     */
    public static Map<String, String> getFileMetadata(String fileId) {
        try {
            NameValuePair[] metaList = storageClient1.get_metadata1(fileId);
            if (metaList != null) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (NameValuePair metaItem : metaList) {
                    map.put(metaItem.getName(), metaItem.getValue());
                }
                return map;
            }
        } catch (Exception e) {
            logger.warn("fastdfs:获得文件元数据出错。", e);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 删除失败返回-1，否则返回0
     */
    public static int deleteFile(String fileId) {
        try {
            return storageClient1.delete_file1(fileId);
        } catch (Exception e) {
            logger.warn("fastdfs:删除文件出错。", e);
        }
        return -1;
    }

    /**
     * 下载文件
     *
     * @param fileId  文件ID（上传文件成功后返回的ID）
     * @param outFile 文件下载保存位置
     * @return
     */
    public static int downloadFile(String fileId, File outFile) {
        FileOutputStream fos = null;
        ByteArrayInputStream bis = null;
        try {
            byte[] content = storageClient1.download_file1(fileId);
            fos = new FileOutputStream(outFile);
            bis = new ByteArrayInputStream(content);
            IOUtils.copy(bis, fos);
            return 0;
        } catch (Exception e) {
            logger.warn("fastdfs:下载文件出错。", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.warn("fastdfs:关闭下载文件流出错。", e);
                }
            }
        }
        return -1;
    }

    /**
     * 获取文件数据
     *
     * @param fileId
     * @return
     */
    public static byte[] getFileData(String fileId) {
        FileOutputStream fos = null;
        ByteArrayInputStream bis = null;
        try {
            byte[] content = storageClient1.download_file1(fileId);
            return content;
        } catch (Exception e) {
            logger.warn("fastdfs:下载文件出错。", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.warn("fastdfs:关闭下载文件流出错。", e);
                }
            }
        }
        return null;
    }

    private static byte[] InputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    private static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 1024];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }


}
