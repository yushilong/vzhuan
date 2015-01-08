
package com.vzhuan.api;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.util.Date;

public class B5MFileHelper
{
    public static final String HISTORY_CACHE_PATH = Environment.getDownloadCacheDirectory() + "/b5mApp/";
    public static final String HISTORY_EXTERNAL_PATH = Environment.getExternalStorageDirectory() + "/b5mApp/";
    public static final String APK_FILE_DIR = "b5mApK";

    public static String getAppFilePath(final String fileName)
    {
        String path = isExistSDCard() ? HISTORY_EXTERNAL_PATH : HISTORY_CACHE_PATH;
        return path + fileName;
    }

    public static boolean isFileExpired(final File file , final long expiredTime)
    {
        Date date = new Date();
        if (file.exists())
        {
            return (date.getTime() - file.lastModified()) > expiredTime;
        }
        return false;
    }

    public static boolean checkDir(final String fileName)
    {
        final String absoulteFilePath = getAppFilePath(fileName);
        File saveDir = new File(absoulteFilePath);
        return checkDir(saveDir);
    }

    public static boolean checkDir(final File file)
    {
        if (file.isDirectory())
        {
            if (!file.exists())
                return file.mkdirs();
        }
        else
        {
            if (!file.exists())
                return file.mkdirs();
            File dir = file.getParentFile();
            if (!dir.exists())
                return dir.mkdirs();
        }
        return true;
    }

    public static int createDir(String path)
    {
        int len = path.length();
        if (len < 1)
            return -1;
        if (path.charAt(len - 1) != '/')
            path += "/";
        if (new File(path).mkdir())
            return 0;
        return -1;
    }

    public static int renameTarget(String filePath , String newName)
    {
        File src = new File(filePath);
        String ext = "";
        File dest;
        if (src.isFile())
            ext = filePath.substring(filePath.lastIndexOf("."), filePath.length());
        if (newName.length() < 1)
            return -1;
        String temp = filePath.substring(0, filePath.lastIndexOf("/"));
        dest = new File(temp + "/" + newName + ext);
        if (src.renameTo(dest))
            return 0;
        else
            return -1;
    }
    private static final int BUFFER = 2048;

    public static int copyToDirectory(String old , String newDir)
    {
        File old_file = new File(old);
        File temp_dir = new File(newDir);
        byte[] data = new byte[BUFFER];
        int read = 0;
        if (old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite())
        {
            String file_name = old.substring(old.lastIndexOf("/"), old.length());
            File cp_file = new File(newDir + file_name);
            try
            {
                BufferedOutputStream o_stream = new BufferedOutputStream(new FileOutputStream(cp_file));
                BufferedInputStream i_stream = new BufferedInputStream(new FileInputStream(old_file));
                while ((read = i_stream.read(data, 0, BUFFER)) != -1)
                    o_stream.write(data, 0, read);
                o_stream.flush();
                i_stream.close();
                o_stream.close();
            }
            catch (FileNotFoundException e)
            {
                Log.e("FileNotFoundException", e.getMessage());
                return -1;
            }
            catch (IOException e)
            {
                Log.e("IOException", e.getMessage());
                return -1;
            }
        }
        else if (old_file.isDirectory() && temp_dir.isDirectory() && temp_dir.canWrite())
        {
            String files[] = old_file.list();
            String dir = newDir + old.substring(old.lastIndexOf("/"), old.length());
            int len = files.length;
            if (!new File(dir).mkdir())
                return -1;
            for (int i = 0; i < len; i++)
                copyToDirectory(old + "/" + files[i], dir);
        }
        else if (!temp_dir.canWrite())
            return -1;
        return 0;
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     * 
     * @param fileName
     * @return
     */
    public static boolean clearHistory(String fileName)
    {
        boolean status;
        SecurityManager checker = new SecurityManager();
        if (!fileName.equals(""))
        {
            final String absoulteFilePath = getAppFilePath(fileName);
            File file = new File(absoulteFilePath);
            checker.checkDelete(file.toString());
            if (file.isDirectory())
            {
                String[] listfile = file.list();
                // delete all files within the specified directory and then
                // delete the directory
                try
                {
                    for (int i = 0; i < listfile.length; i++)
                    {
                        File deletedFile = new File(file.toString() + "/" + listfile[i].toString());
                        deletedFile.delete();
                    }
                    file.delete();
                    Log.i("DirectoryManager deleteDirectory", fileName);
                    status = true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    status = false;
                }
            }
            else
                status = false;
        }
        else
            status = false;
        return status;
    }

    public static boolean isExistSDCard()
    {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        return false;
    }
}
