package com.ckmod.zyr3x.tools;

import com.ckmod.zyr3x.tools.FileManagerIntents;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;

public class CKZTools extends Activity
{
    private ListView lView;
    private ListView listViewTitle;
    private ListView listViewOptions;
    private ListView lvIO;
    private CSSwitchConfig cfg = new CSSwitchConfig();
    private CXKernel xkn = new CXKernel();
    private CSystem cSystem  =  new CSystem();

    private DialogInterface dlgLoad;
    private CheckBox cbEnable;
    public int kernel_type = 0;

    protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
	protected static final int REQUEST_CODE_GET_CONTENT = 2;

	protected EditText mEditText;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.initMain();
    }

    private void initMain()
    {
         setContentView(R.layout.main);
         SharedPreferences settings = getPreferences(0);
         SharedPreferences.Editor editor = settings.edit();
         this.kernel_type =  settings.getInt("KERNEL",0);
         switch(this.kernel_type)
         {
             case 0:
                 this.getXMListOptions();
                 break;
             case 1:
                     this.getListView();
                     this.getListOptions();
                 break;
             case 2:
                  this.getListView();
                 this.getLukiqqListOptions();
                 break;
         }

    }

    private void getListView()
    {
          lView = (ListView) findViewById(R.id.ListView01);
          lView.setAdapter(new ArrayAdapter<String>(this,
                  android.R.layout.simple_list_item_multiple_choice,
                  new String[]{getString(R.string.sss_enable)}));

          lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
          boolean  enable = cfg.getValue("ENABLED").equals("1");
          lView.setItemChecked(0,enable);

          lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  CheckedTextView v = (CheckedTextView)view;
                  boolean enable =  v.isChecked();

                   boolean val = (enable == true)? false : true;
                   lView.setItemChecked(0,val);
                   CKZTools.this.enable(val);
              }
          });

    }

    public void enable(final boolean enable)
    {
        dlgLoad = ProgressDialog.show(this, "", getString(R.string.sss_update), true, false);
        new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (Exception e) {
                    dlgLoad.dismiss();
                }
                cfg.stop();

                if (enable == false) {
                    cfg.setValue("ENABLED", "0");
                    cfg.update();
                } else {
                    cfg.setValue("ENABLED", "1");
                    cfg.update();
                    cfg.start();
                }
                xkn.save();
                dlgLoad.dismiss();
            }
        }.start();
    }

    private void getListOptions()
    {
        listViewOptions = (ListView) findViewById(R.id.lvMainOptions);
        listViewOptions.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                          new String[]{
                                  getString(R.string.main_display_info),
                                  getString(R.string.main_customize),
                                  getString(R.string.main_load_profile),
                                  getString(R.string.main_av_profile),
                                  getString(R.string.main_xm_setting),
                          }));
        listViewOptions.setChoiceMode(ListView.CHOICE_MODE_NONE);

        listViewOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean enable = false;
                switch(i)
                {
                    case 0:
                        CKZTools.this.getSSSCfgInfo();
                        break;
                    case 1:
                        CKZTools.this.initSetting();
                        break;
                    case 2:
                        CKZTools.this.loadBattaryProfile();
                        break;
                    case 3:
                        CKZTools.this.initAvProfiles();
                        break;
                    case 4:
                        CKZTools.this.initXKernel();
                        break;
                }
            }
        });
    }

    private void getXMListOptions()
    {
        listViewOptions = (ListView) findViewById(R.id.lvMainOptions);
        listViewOptions.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                          new String[]{
                                  getString(R.string.main_av_profile),
                                  getString(R.string.main_xm_setting),
                          }));
        listViewOptions.setChoiceMode(ListView.CHOICE_MODE_NONE);

        listViewOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean enable = false;
                switch(i)
                {
                    case 0:
                        CKZTools.this.initAvProfiles();
                        break;
                    case 1:
                        CKZTools.this.initXKernel();
                        break;
                }
            }
        });
    }

    private void getLukiqqListOptions()
    {
        listViewOptions = (ListView) findViewById(R.id.lvMainOptions);
        listViewOptions.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                          new String[]{
                                  getString(R.string.main_customize),
                                  getString(R.string.main_av_profile),
                                  getString(R.string.main_xm_setting),
                          }));
        listViewOptions.setChoiceMode(ListView.CHOICE_MODE_NONE);

        listViewOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean enable = false;
                switch(i)
                {
                    case 0:
                        CKZTools.this.initLukiqqSetting();
                        break;
                    case 1:
                        CKZTools.this.initAvProfiles();
                        break;
                    case 2:
                        CKZTools.this.initXKernel();
                        break;
                }
            }
        });
    }

    public void initAvProfiles()
    {
       CharSequence[] items = new CharSequence[]{getString(R.string.nrml), getString(R.string.music), getString(R.string.movie),};
       CKZTools.this.initAVAlertDialog(getString(R.string.main_av_profile), items, getString(R.string.main_av_profile));

    }

    public void getSSSCfgInfo()
    {
            Context context = getApplicationContext();
            ArrayList<String> val = cfg.getValues();
            ArrayList<String> opt = cfg.getOptions();

        String info = getString(R.string.io_scheduler)+": " + val.get(opt.indexOf("IO_SCHEDULER")) + "\n" +
                     "\n"+
                      getString(R.string.awake)+"\n"+
                      "-----------------------------\n"+
                      getString(R.string.undervolt)+": "+ val.get(opt.indexOf("AWAKE_UV")) + "\n" +
                      getString(R.string.cpu_governor)+": "+ val.get(opt.indexOf("AWAKE_GOVERNOR")) + "\n"
                    + getString(R.string.cpu_max_frequency)+": "+ val.get(opt.indexOf("AWAKE_MAX_FREQ")) + "\n"
                    + getString(R.string.cpu_up_threshold)+": "+ val.get(opt.indexOf("AWAKE_UP_THRESHOLD")) + "\n"
                    +"\n"
                     + getString(R.string.sleep)+"\n"
                    + "-----------------------------\n"
                    + getString(R.string.undervolt)+": "+ val.get(opt.indexOf("SLEEP_UV")) + "\n"
                    + getString(R.string.cpu_governor)+": "+ val.get(opt.indexOf("SLEEP_GOVERNOR")) + "\n"
                    + getString(R.string.cpu_max_frequency)+": "+ val.get(opt.indexOf("SLEEP_MAX_FREQ")) + "\n"
                    + getString(R.string.cpu_up_threshold)+": "+ val.get(opt.indexOf("SLEEP_UP_THRESHOLD")) + "\n";
            Toast.makeText(context, info,Toast.LENGTH_LONG).show();
    }

    private void getListIO()
    {

        lvIO = (ListView) findViewById(R.id.lvIO);
        lvIO.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[]{
                        getString(R.string.io_scheduler),
                }));
        lvIO.setChoiceMode(ListView.CHOICE_MODE_NONE);

        lvIO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence[] items = null;
                items = new CharSequence[]{"noop", "anticipatory", "deadline", "cfq", "bfq"};
                CKZTools.this.initAlertDialog(getString(R.string.io_scheduler), items, "IO_SCHEDULER");
            }
        });
    }

    private void getListAwake()
    {

        lvIO = (ListView) findViewById(R.id.lvAwake);
        lvIO.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[]{
                        getString(R.string.undervolt),
                        getString(R.string.cpu_governor),
                        getString(R.string.cpu_max_frequency),
                        getString(R.string.cpu_up_threshold),
                }));
        lvIO.setChoiceMode(ListView.CHOICE_MODE_NONE);

        lvIO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence[] items = null;
                switch(i)
                {
                     case 0:
                        items = new CharSequence[]{"0", "50", "100", "150", "200", "250",getString(R.string.custom)};
                        CKZTools.this.initAlertDialog(getString(R.string.undervolt),items,"AWAKE_UV");
                        break;
                     case 1:
                        items = new CharSequence[]{"ondemand", "conservative", "performance"};
                        CKZTools.this.initAlertDialog( getString(R.string.cpu_governor),items,"AWAKE_GOVERNOR");
                        break;
                     case 2:
                        items = new CharSequence[]{"800000", "400000", "266000", "133000", "66000",getString(R.string.custom)};
                        CKZTools.this.initAlertDialog(getString(R.string.cpu_max_frequency),items,"AWAKE_MAX_FREQ");
                        break;
                     case 3:
                        items = new CharSequence[101];
                         for(int j=0;j<items.length;j++) items[j] = (CharSequence)String.valueOf(j+1);
                         items[100] = getString(R.string.custom);
                        CKZTools.this.initAlertDialog(getString(R.string.cpu_up_threshold),items,"AWAKE_UP_THRESHOLD");
                        break;
                }
            }
        });
    }

    private void getListSleep()
    {

        lvIO = (ListView) findViewById(R.id.lvSleep);
        lvIO.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[]{
                    getString(R.string.undervolt),
                    getString(R.string.cpu_governor),
                    getString(R.string.cpu_max_frequency),
                    getString(R.string.cpu_up_threshold),
                }));
        lvIO.setChoiceMode(ListView.CHOICE_MODE_NONE);

        lvIO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence[] items = null;
                switch(i)
                {
                     case 0:
                        items = new CharSequence[]{"0", "50", "100", "150", "200", "250",getString(R.string.custom)};
                        CKZTools.this.initAlertDialog(getString(R.string.undervolt),items,"SLEEP_UV");
                        break;
                     case 1:
                        items = new CharSequence[]{"ondemand", "conservative", "performance",};
                        CKZTools.this.initAlertDialog(getString(R.string.cpu_governor),items,"SLEEP_GOVERNOR");
                        break;
                    case 2:
                        items = new CharSequence[]{"800000", "400000", "266000", "133000", "66000",getString(R.string.custom)};
                        CKZTools.this.initAlertDialog(getString(R.string.cpu_max_frequency),items,"SLEEP_MAX_FREQ");
                        break;
                    case 3:
                         items = new CharSequence[101];
                        for(int j=0;j<items.length;j++) items[j] = (CharSequence)String.valueOf(j+1);
                        items[100] = getString(R.string.custom);
                        CKZTools.this.initAlertDialog(getString(R.string.cpu_up_threshold),items,"SLEEP_UP_THRESHOLD");
                        break;
                }
            }
        });
    }

    public void initSetting()
    {
          setContentView(R.layout.settings);
          this.getListIO();
          this.getListAwake();
          this.getListSleep();
    }

     public void initLukiqqSetting()
    {

          CharSequence[] items = null;
          items = new CharSequence[]{"noop", "anticipatory", "deadline", "cfq", "bfq"};
          CKZTools.this.initAlertDialogAndSave(getString(R.string.io_scheduler), items, "IO_SCHEDULER");

          //Toast.makeText(getApplicationContext(), getString(R.string.sss_update), Toast.LENGTH_SHORT).show();
          //setContentView(R.layout.settings);
          //this.getListIO();
          //this.getListAwake();
          // this.getListSleep();
    }

    public void onSave(View view)
     {
         boolean enable = cfg.getValue("ENABLED").equals("1");
         CKZTools.this.enable(enable);
         Toast.makeText(getApplicationContext(), getString(R.string.sss_update), Toast.LENGTH_SHORT).show();
         this.initMain();
     }

    public void onXKSave(View view)
    {
        dlgLoad = ProgressDialog.show(this, "", getString(R.string.xkn_update), true, false);
        new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (Exception e) {
                    dlgLoad.dismiss();
                }

                xkn.save();
                dlgLoad.dismiss();

            }
        }.start();

         Toast.makeText(getApplicationContext(),getString(R.string.xkn_update), Toast.LENGTH_SHORT).show();
         this.initMain();
     }

    public void onBack(View view)
     {
         this.initMain();
     }

    public void loadBattaryProfile()
    {
       final CharSequence[] items = {getString(R.string.low),
                                     getString(R.string.nrml),
                                     getString(R.string.high),
                                     getString(R.string.def)};

       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle(getString(R.string.main_load_profile));

       builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                cfg.loadProfile(item);
                boolean enable = cfg.getValue("ENABLED").equals("1");
                CKZTools.this.enable(enable);
                Toast.makeText(getApplicationContext(), items[item] + " " +getString(R.string.performance_battery), Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void initAVAlertDialog(String title,final CharSequence[] items,final String option )
    {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle(title);
        int id =  xkn.getAVProfile();
        builder.setSingleChoiceItems(items, id, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                    xkn.setAVProfile(item);
                    xkn.save();
                    Toast.makeText(getApplicationContext(), getString(R.string.set)+" " +items[item].toString() +" " + getString(R.string.profile),  Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



    public void initAlertDialogAndSave(final String title,final CharSequence[] items,final String option)
    {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle(title);

       String val =  cfg.getValue(option);
       int select = items.length - 1;
       for(int i = 0;i<items.length;i++)
         if(items[i].equals(val))  select = i;

        builder.setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                     cfg.setValue(option,items[item].toString());
                       boolean enable = cfg.getValue("ENABLED").equals("1");
                         CKZTools.this.enable(enable);
                     Toast.makeText(getApplicationContext(), option + " = "+items[item].toString(), Toast.LENGTH_SHORT).show();
                     dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void initAlertDialog(final String title,final CharSequence[] items,final String option)
    {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle(title);

       String val =  cfg.getValue(option);
       int select = items.length - 1;
       for(int i = 0;i<items.length;i++)
         if(items[i].equals(val))  select = i;

        builder.setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                    if(items[item].toString().equals(getString(R.string.custom)))
                    {
                       CKZTools.this.initCustomDialog(title,option,option);
                       dialog.dismiss();
                    }
                    else
                    {
                     cfg.setValue(option,items[item].toString());
                     Toast.makeText(getApplicationContext(), option + " = "+items[item].toString(), Toast.LENGTH_SHORT).show();
                     dialog.dismiss();
                    }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void initXKAlertDialog(final String title,final CharSequence[] items, final int type)
    {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle(title);
         String val ="";
         switch (type)
         {
                    case 0:
                        val =  xkn.buffer;
                        break;
                    case 1:
                        val =  xkn.soundfix;
                        break;
                    case 2:
                        val =   xkn.undervolt;
                        break;
         }


       int select = items.length-1;
       for(int i = 0;i<items.length;i++)
         if(items[i].equals(val))  select = i;


        builder.setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                String name = "";
                if(items[item].toString().equals(getString(R.string.custom)))
                    {
                      String val = "";
                      switch (type)
                    {
                    case 0:
                        val =  xkn.buffer;
                        break;
                    case 1:
                        val =  xkn.soundfix;
                        break;
                    case 2:
                        val =   xkn.undervolt;
                        break;
                     }
                       CKZTools.this.initXCustomDialog(title, val, type);
                       dialog.dismiss();
                    }
                    else
                    {
                        switch (type)
                        {
                            case 0:
                                name = getString(R.string.buffer);
                                xkn.buffer = items[item].toString();
                                break;
                            case 1:
                                name = getString(R.string.soundfix);
                                xkn.soundfix = items[item].toString();
                                break;
                            case 2:
                                name = getString(R.string.undervolt);
                                xkn.undervolt = items[item].toString();
                                break;
                        }
                        Toast.makeText(getApplicationContext(),name + " = "+items[item].toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }


            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void initXKernel()
    {
       setContentView(R.layout.xkernel);
       this.getLvXKernel();

    }

    public  void getLvXKernel()
    {
        lvIO = (ListView) findViewById(R.id.lvXKernel);
        lvIO.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[]{
                      getString(R.string.buffer),
                      getString(R.string.soundfix),
                      getString(R.string.undervolt),
                }));
        lvIO.setChoiceMode(ListView.CHOICE_MODE_NONE);

        lvIO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence[] items = null;
                switch(i)
                {
                     case 0:
                        items = new CharSequence[]{"0", "4", "8", "16", "32", "48","56" ,getString(R.string.custom)};
                        CKZTools.this.initXKAlertDialog(getString(R.string.buffer),items,i);
                        break;
                     case 1:
                        items = new CharSequence[]{"0", "1"};
                        CKZTools.this.initXKAlertDialog( getString(R.string.soundfix),items,i);
                        break;
                    case 2:
                        items = new CharSequence[]{"0", "50", "100", "150", "200", "250",getString(R.string.custom)};
                        CKZTools.this.initXKAlertDialog(getString(R.string.undervolt),items,i);
                        break;
                }
            }
        });
    }

    public void initCustomDialog(String title,final String name,final String option)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        String val =  cfg.getValue(option);
         alert.setTitle(title);
        alert.setView(input);
        input.setText(val);
         input.setInputType(2);
        alert.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                      String value = input.getText().toString().trim();
                      cfg.setValue(option,value);
                      Toast.makeText(getApplicationContext(), name + " = "+value, Toast.LENGTH_SHORT).show();
                      dialog.cancel();
                  }
              });

        alert.setNegativeButton(getString(R.string.back),
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) {
                          dialog.cancel();
                      }
                  });
          alert.show();


    }

     public void initXCustomDialog(final String title,final String value,final int type)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        alert.setView(input);
        alert.setTitle(title);
        input.setText(value);
       input.setInputType(2);
        alert.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                      String value = input.getText().toString().trim();

                       switch (type)
                         {
                                    case 0:
                                        xkn.buffer = value;
                                        break;
                                    case 1:
                                        xkn.soundfix = value;
                                        break;
                                    case 2:
                                         xkn.undervolt = value;
                                        break;
                         }
                      Toast.makeText(getApplicationContext(), title  + " = "+value, Toast.LENGTH_SHORT).show();
                      dialog.cancel();
                  }
              });

        alert.setNegativeButton(getString(R.string.back),
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) {
                          dialog.cancel();
                      }
                  });
          alert.show();


    }


    public void initSettingsDialog(final String title,final CharSequence[] items)
    {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle(title);
            SharedPreferences settings = getPreferences(0);
                    SharedPreferences.Editor editor;

        builder.setSingleChoiceItems(items, settings.getInt("KERNEL",0), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                    SharedPreferences settings = getPreferences(0);
                    SharedPreferences.Editor editor;
                    switch(item)
                    {
                        case 0:
                            editor = settings.edit();
                            editor.putInt("KERNEL",item );
                            editor.commit();
                            CKZTools.this.initMain();
                            break;
                        case 1:
                            editor = settings.edit();
                            editor.putInt("KERNEL",item );
                            editor.commit();
                             CKZTools.this.initMain();
                            break;
                        case 2:
                            editor = settings.edit();
                            editor.putInt("KERNEL",item );
                            editor.commit();
                             CKZTools.this.initMain();
                            break;
                    }
                     dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

   @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.icon_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.install_zip:
             this.initRomInstall();
            return true;
        case R.id.setting:
            this.initSettingsDialog(getString(R.string.kernel),new CharSequence[]{
                    getString(R.string.xmkernel),
                    getString(R.string.ckmod),
                    getString(R.string.lukiqq)});
            return true;
        case R.id.quit:
             finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }

    }

     protected void onStop(){
       super.onStop();



    }


public void onClickOpenFile(View view) {
		openFile();
	}


    /**
     * Opens the file manager to select a file to open.
     */
    public void openFile() {
		String fileName = mEditText.getText().toString();

		Intent intent = new Intent(FileManagerIntents.ACTION_PICK_FILE);

		// Construct URI from file name.
		File file = new File(fileName);
		intent.setData(Uri.fromFile(file));

		// Set fancy title and button (optional)
		//intent.putExtra(FileManagerIntents.EXTRA_TITLE, getString(R.string.open_title));
		//intent.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, getString(R.string.open_button));

		try {
			startActivityForResult(intent, REQUEST_CODE_PICK_FILE_OR_DIRECTORY);
		} catch (ActivityNotFoundException e) {
			// No compatible file manager was found.
		/*	Toast.makeText(this, R.string.no_filemanager_installed,
					Toast.LENGTH_SHORT).show();  */
		}
	}

    /**
     * This is called after the file manager finished.
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
			if (resultCode == RESULT_OK && data != null) {
				// obtain the filename
				Uri fileUri = data.getData();
				if (fileUri != null) {
					String filePath = fileUri.getPath();
					if (filePath != null) {
						mEditText.setText(filePath);
					}
				}
			}
			break;
		case REQUEST_CODE_GET_CONTENT:
			if (resultCode == RESULT_OK && data != null) {
				String filePath = null;
				Uri uri = data.getData();
				android.database.Cursor c = getContentResolver().query(uri, null, null, null, null);
				if (c != null && c.moveToFirst()) {
					int id = c.getColumnIndex(MediaStore.Images.Media.DATA);
					if (id != -1) {
						filePath = c.getString(id);
					}
				}
				if (filePath != null) {
					mEditText.setText(filePath);
				}
			}
		}
	}


    public void initRomInstall( )
    {
        setContentView(R.layout.install);
        mEditText = (EditText) findViewById(R.id.file_path);
    }


      public void onInstall(View view)
     {
         String install = mEditText.getText().toString();
          	String item_ext = null;

    	try {
    		item_ext = install.substring(install.lastIndexOf("."), install.length());

    	} catch(IndexOutOfBoundsException e) {
    		item_ext = "";
    	}
         if(item_ext.equals(".zip"))
         {
          cSystem.install(install);
         }
         else
         {
             Toast.makeText(getApplicationContext(),getString(R.string.error_zip), Toast.LENGTH_SHORT).show();
         }
     }
}
