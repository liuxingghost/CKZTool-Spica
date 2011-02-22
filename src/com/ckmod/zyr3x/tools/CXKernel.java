package com.ckmod.zyr3x.tools;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Olex
 * Date: 24.01.11
 * Time: 0:15
 * To change this template use File | Settings | File Templates.
 */
public class CXKernel {

    private CSystem cSystem;
    public String buffer = "16";
    public String soundfix = "0";
    public String undervolt = "0";

    private String xkernelDir = "/proc/xmister/";

    public CXKernel()
    {
        cSystem = new CSystem();
        this.buffer = this.loadXkernelSetting("buffer");
        this.soundfix = this.loadXkernelSetting("soundfix");
        this.undervolt = this.loadXkernelSetting("undervolt");
    }

    public void save()
    {
        cSystem.rootCommand("chmod 777 /proc/xmister/buffer");
        cSystem.rootCommand("chmod 777 /proc/xmister/soundfix");
        cSystem.rootCommand("chmod 777 /proc/xmister/undervolt");
        cSystem.rootCommand("echo "+ this.buffer +" > /proc/xmister/buffer");
        cSystem.rootCommand("echo "+ this.soundfix +" > /proc/xmister/soundfix");
        cSystem.rootCommand("echo "+ this.undervolt +" > /proc/xmister/undervolt");
    }

    public void setAVProfile(int id_profile)
    {
        switch (id_profile)
        {
            case 0:
                this.buffer = "16";
                this.soundfix = "0";
                break;
            case 1:
                this.buffer = "32";
                this.soundfix = "1";
                break;
            case 2:
                this.buffer = "8";
                this.soundfix = "0";
                break;
        }
    }

    public int getAVProfile()
    {
        int id_profile = 0;
        if((this.buffer + this.soundfix).equals("160")) id_profile = 0;
        if((this.buffer+ this.soundfix).equals("321"))id_profile = 1;
        if((this.buffer+ this.soundfix).equals("80")) id_profile = 2;

        return id_profile;
    }

    public void reboot()
    {
        cSystem.rootCommand("reboot");
    }

    private String loadXkernelSetting(String name) {
        String line = null;
        FileReader f = null;
        BufferedReader br = null;
        try {
            if ((new File(this.xkernelDir + name).exists())) {
                f = new FileReader(this.xkernelDir + name);
                br = new BufferedReader(f);

                try {
                    line = br.readLine();
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return line;

    }

    private void saveXkernelSetting(String name,String val)
    {
        FileWriter f = null;


        try {
            f = new FileWriter(this.xkernelDir + name);
            f.write(val);
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
