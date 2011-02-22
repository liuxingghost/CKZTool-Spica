package com.ckmod.zyr3x.tools;

import android.util.Log;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: Olex
 * Date: 12.01.11
 * Time: 23:29
 * To change this template use File | Settings | File Templates.
 */
public class CSystem {


    public void mountSystem()
    {
        this.rootCommand("mount -o rw,remount -t yaffs2 /dev/block/mtdblock3 /system");
        this.rootCommand("chmod -R 777 /system");
    }

    public void unmountSystem()
    {
        this.rootCommand("mount -o remount,ro /system");
    }

    public void rootCommand(String cmd)
    {
        File wd = new File("/");
        System.out.println(wd);
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("su", null, wd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (proc != null) {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println(cmd);
            out.println("exit");
            try {
                proc.waitFor();
                out.close();
                proc.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);

        in.close();
        out.close();
    }

    public void unzip(String file, String to)
    {
        Enumeration entries;
        ZipFile zipFile;


        try {
            zipFile = new ZipFile(file);

            entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                if (entry.isDirectory()) {
                    // Assume directories are stored parents first then children.
                   // Log.d("Extracting directory: " + entry.getName());
                    // This is not robust, just for demonstration purposes.
                    (new File(to + entry.getName())).mkdirs();
                    continue;
                }

                copyInputStream(zipFile.getInputStream(entry),
                        new BufferedOutputStream(new FileOutputStream(to + entry.getName())));
            }

            zipFile.close();
        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
            return;
        }
    }

    public void kill(String name)
    {

         String kill = "kill -9 `pidof " + name + "`";
         this.rootCommand(kill);
    }
    public String rootCommandOut(String cmd)
    {
        String line = null;
        File wd = new File("/");

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("su", null, wd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (proc != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println(cmd);
            out.println("exit");
            try {

                while ((line = in.readLine()) != null) {}
                proc.waitFor();
                in.close();
                out.close();
                proc.destroy();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    public void install(String install)
    {
      install = install.replace("/sdcard/","SDCARD:");
      this.rootCommand("chmod -R 777 /cache");

       FileWriter f = null;

        try {
                f = new FileWriter("/cache/recovery/extendedcommand");
                f.write("install_zip "+install+"\n");
                f.write(" ");
                f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
         this.rootCommand("reboot recovery");
    }

}
