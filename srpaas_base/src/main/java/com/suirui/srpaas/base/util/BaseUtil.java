package com.suirui.srpaas.base.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cui.li on 2017/2/20.
 */

public class BaseUtil {
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        String path = sdDir.toString();
        return path;
    }


    /**
     * 检查是否有sdcard卡
     *
     * @return
     */
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 得到sd卡剩余的空间大小
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blocksize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blocksize * availableBlocks;
    }

    /**
     * 保存SDCard文件路徑
     *
     * @param dirName
     * @return
     */
    public static String saveSdcardFile(String dirName) {
        try {
            if (BaseUtil.checkSDCard()) {
                long available = BaseUtil.getAvailableExternalMemorySize();
                if (available < 1024 * 1024) {
                    return null;
                }
                String filePath = Environment.getExternalStorageDirectory()
                        .getPath();
                if (dirName != null && !dirName.equals("")) {
                    filePath = filePath + dirName;
                }
                if (filePath == null) {
                    return null;
                }
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                return filePath;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 獲取SDvard目錄下文件路徑
     *
     * @param dirName
     * @return
     */
    public static String getSdcardFile(String dirName) {
        try {
            String filePath = Environment.getExternalStorageDirectory()
                    .getPath();
            if (dirName != null && !dirName.equals("")) {
                filePath = filePath + dirName;
            }
            if (filePath == null) {
                return null;
            }
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 寫文件
     *
     * @param in
     * @param file
     * @return
     */
    public static boolean writeToFile(InputStream in, File file) {
        if (in == null || file == null)
            return false;
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(in);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            bis.close();
            in.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
