package com.cyanogenmod.compasscalibrator.activities;

import com.cyanogenmod.compasscalibrator.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.cyanogenmod.compasscalibrator.utils.CMDProcessor;
import com.cyanogenmod.compasscalibrator.utils.Helpers;
import com.cyanogenmod.compasscalibrator.R;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassCalibrator extends Activity {

        //Sensors
        SensorManager sm;
        SensorListener o;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        o = new SensorListener();

        Button compasscal = (Button)findViewById(R.id.compasscal);
        compasscal.setOnClickListener(new View.OnClickListener(){
    public void onClick(View v) {
        final CMDProcessor cmd = new CMDProcessor();
    	cmd.su.runWaitFor("busybox echo 0,0,0 > /sys/devices/virtual/accelerometer/accelerometer/acc_file");
        cmd.su.runWaitFor("busybox echo 1 > /sys/devices/virtual/accelerometer/accelerometer/calibrate");
        alertbox.setTitle(R.string.compcalibration);
        alertbox.setMessage(R.string.compcalibrationdialog);
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface arg0, int arg1) {
    	     cmd.su.runWaitFor("busybox echo 0,0,0 > /sys/devices/virtual/accelerometer/accelerometer/acc_file");
             cmd.su.runWaitFor("busybox echo 1 > /sys/devices/virtual/accelerometer/accelerometer/calibrate");
           }

        });
        alertbox.show();
    }
    });
    }

    @Override
    protected void onResume() {
        super.onStart();
        sm.registerListener(o, sm.getSensorList(Sensor.TYPE_ORIENTATION).get(0), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onStop();
        sm.unregisterListener(o);
    }
}
