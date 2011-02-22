package com.ckmod.zyr3x.tools;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Olex
 * Date: 14.01.11
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class CSSwitchConfig {

        private CSystem cSystem;
        private ArrayList<String> options = new ArrayList<String>();
        private ArrayList<String> values = new ArrayList<String>();
        private String ssswitcherDir = "/system/etc/ssswitch.conf";

        public CSSwitchConfig()
        {
           cSystem = new CSystem();
           this.load();
        }

    private void load() {
        cSystem.mountSystem();

        String line;
        FileReader f = null;
        BufferedReader br = null;
        try {
            if ((new File(ssswitcherDir).exists())) {
                f = new FileReader(ssswitcherDir);
                br = new BufferedReader(f);

                try {
                    while ((line = br.readLine()) != null) {
                        String[] param = line.split("=");
                        options.add(param[0]);
                        values.add(param[1].replace(";", ""));
                    }
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            else
            {
                this.newConfig();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public ArrayList<String> getOptions() {
            return options;  //To change body of created methods use File | Settings | File Templates.
        }

    public ArrayList<String> getValues() {
            return values;  //To change body of created methods use File | Settings | File Templates.
        }

    public void setValues(ArrayList<String> val) {
            values = val;
    }

    public void setValue(String name, String val)
    {
          values.set(options.indexOf(name),val);
    }

    public String getValue(String name)
    {
         return values.get(options.indexOf(name));
    }

    public void update()
    {
        this.save(null);
    }

    private void save(String saveDir)
    {
        FileWriter f = null;
        if(saveDir == null) saveDir = ssswitcherDir;

        try {
            f = new FileWriter(ssswitcherDir);
            for (int i = 0; i < options.size(); i++) {
                String opt = options.get(i).toString() + "=" + values.get(i).toString()+";\n";
                f.write(opt);
            }
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void loadProfile(int id)
    {
      switch (id)
      {
          case 0:
              values.set(options.indexOf("IO_SCHEDULER"),"noop");
              values.set(options.indexOf("AWAKE_UV"),"100");
              values.set(options.indexOf("AWAKE_GOVERNOR"),"conservative");
              values.set(options.indexOf("AWAKE_MAX_FREQ"),"800000");
              values.set(options.indexOf("AWAKE_UP_THRESHOLD"),"99");
              values.set(options.indexOf("SLEEP_UV"),"200");
              values.set(options.indexOf("SLEEP_GOVERNOR"),"conservative");
              values.set(options.indexOf("SLEEP_MAX_FREQ"),"133000");
              values.set(options.indexOf("SLEEP_UP_THRESHOLD"),"99");
              break;
          case 1:
              values.set(options.indexOf("IO_SCHEDULER"),"deadline");
              values.set(options.indexOf("AWAKE_UV"),"50");
              values.set(options.indexOf("AWAKE_GOVERNOR"),"ondemand");
              values.set(options.indexOf("AWAKE_MAX_FREQ"),"800000");
              values.set(options.indexOf("AWAKE_UP_THRESHOLD"),"95");
              values.set(options.indexOf("SLEEP_UV"),"150");
              values.set(options.indexOf("SLEEP_GOVERNOR"),"conservative");
              values.set(options.indexOf("SLEEP_MAX_FREQ"),"266000");
              values.set(options.indexOf("SLEEP_UP_THRESHOLD"),"95");
              break;
          case 2:
              values.set(options.indexOf("IO_SCHEDULER"),"bfq");
              values.set(options.indexOf("AWAKE_UV"),"50");
              values.set(options.indexOf("AWAKE_GOVERNOR"),"ondemand");
              values.set(options.indexOf("AWAKE_MAX_FREQ"),"800000");
              values.set(options.indexOf("AWAKE_UP_THRESHOLD"),"80");
              values.set(options.indexOf("SLEEP_UV"),"100");
              values.set(options.indexOf("SLEEP_GOVERNOR"),"ondemand");
              values.set(options.indexOf("SLEEP_MAX_FREQ"),"400000");
              values.set(options.indexOf("SLEEP_UP_THRESHOLD"),"80");
              break;
          case 3:
              values.set(options.indexOf("IO_SCHEDULER"),"noop");
              values.set(options.indexOf("AWAKE_UV"),"100");
              values.set(options.indexOf("AWAKE_GOVERNOR"),"ondemand");
              values.set(options.indexOf("AWAKE_MAX_FREQ"),"800000");
              values.set(options.indexOf("AWAKE_UP_THRESHOLD"),"90");
              values.set(options.indexOf("SLEEP_UV"),"200");
              values.set(options.indexOf("SLEEP_GOVERNOR"),"conservative");
              values.set(options.indexOf("SLEEP_MAX_FREQ"),"266000");
              values.set(options.indexOf("SLEEP_UP_THRESHOLD"),"90");
              break;
      }

    }

    public void newConfig()
    {
        options.add("ENABLED");
        options.add("IO_SCHEDULER");
        options.add("AWAKE_UV");
        options.add("AWAKE_GOVERNOR");
        options.add("AWAKE_MAX_FREQ");
        options.add("AWAKE_UP_THRESHOLD");
        options.add("SLEEP_UV");
        options.add("SLEEP_GOVERNOR");
        options.add("SLEEP_MAX_FREQ");
        options.add("SLEEP_UP_THRESHOLD");

        values.add("1");
        values.add("deadline");
        values.add("50");
        values.add("ondemand");
        values.add("800000");
        values.add("95");
        values.add("150");
        values.add("conservative");
        values.add("266000");
        values.add("95");
    }

    public  void stop()
    {
       cSystem.kill("98ssswitch");
    }

    public  void start()
    {
       cSystem.rootCommand("nohup /etc/init.d/98ssswitch 2>/dev/nul &");
    }

    public void getUserSettings()
    {


    }




}
