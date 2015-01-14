package com.ypw.shuaidao;

import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends Activity {

	
	

	long uiId;
	private SensorManager sensorManager;
	private Sensor accelerometerSensor;
	private float gravity [] = new float[3];
	private float linear_acceleration [] = new float[3];
	long now,last;
	int fcall=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		last=SystemClock.elapsedRealtime();
		
		 sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        sensorManager.registerListener(new SensorEventListener() {
				
				public void onSensorChanged(SensorEvent event) {
					
					  final float alpha = 0.8f;
				      TextView textView1 =(TextView)findViewById(R.id.textView1);
					  gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
					  gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
					  gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

					  linear_acceleration[0] = event.values[0] - gravity[0];
					  linear_acceleration[1] = event.values[1] - gravity[1];
					  linear_acceleration[2] = event.values[2] - gravity[2];
				

					  double x=gravity[0],y=gravity[1],z=gravity[2],cos=0,theta=0;
					  
					  cos=Math.sqrt((x*x+z*z)/(x*x+y*y+z*z));
					  theta=Math.acos(cos)/Math.PI*180;
					  
					  String output = "";
					  output+="x=\t"+String.valueOf(x)+"\r\n";
					  output+="y=\t"+String.valueOf(y)+"\r\n";
					  output+="z=\t"+String.valueOf(z)+"\r\n";
					  output+="cosθ=\t"+String.valueOf(cos)+"\r\n";
					  output+="θ=\t"+String.valueOf(theta)+"\r\n";
					  
					  now=SystemClock.elapsedRealtime();
					  if(theta<30)
					  {
						  if(now-last>5000)
						  {
							  
							  if(fcall==0)
							  {
							  show("Call");
							  call();
							  fcall=1;
							  }
						  }
						  else {
							  beep();
						  }
					  }
					  else 
					  {
						  last=now;
						  fcall=0;
					  }
					  
					  output+="deltatime=\t"+String.valueOf(now-last)+"\r\n";
					  
					  textView1.setText(output);
				}
				
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
					
				}
			}, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
	        
	        
			//EditText editText1 =(EditText)findViewById(R.id.editText1);
			//editText1.setText("192.168.1.102");
			uiId=Thread.currentThread().getId();
			//获取主线程ID
			}
	
	public void call()
	{
		EditText editText1 =(EditText)findViewById(R.id.editText1);
		String str = editText1.getText().toString();
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+str));
	    this.startActivity(intent);
	}
	public void beep()
	{
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(100);
	}
    public void show(String str){
    	if(Thread.currentThread().getId()!=uiId)
    	{
    	Looper.prepare();
    	Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    	Looper.loop();
        //非主线程中显示Toast必须写这个才不强退
    	}else Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE,Menu.FIRST,0,"关于").setIcon(R.drawable.ic_launcher);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	try{
    	if(item.getItemId()==Menu.FIRST)
    	{
    		Dialog alertDialog = new AlertDialog.Builder(this). 
                    setTitle("开发者"). 
                    setMessage("杨培文\tQQ:0x369181DA"). 
                    setIcon(R.drawable.ic_launcher).
                    setPositiveButton("确定", new DialogInterface.OnClickListener() { 
                        @Override 
                        public void onClick(DialogInterface dialog, int which) { 
                            // TODO Auto-generated method stub  
                        } 
                    }). 
                    create(); 
            alertDialog.show(); 
    	}
    	}catch(Exception ex)
    	{
    		show(ex.toString());
    	}
		return false;
    }
}
