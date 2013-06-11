package com.dkhs.geoalert.view;

import android.R.drawable;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.geoalert.AlarmIntentManager;
import com.dkhs.geoalert.allert.Alarm;

public class AlertView extends LinearLayout{

	public AlertView(Context context,final Alarm alarm, final LinearLayout container,final AlarmIntentManager man) {
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
	    setGravity(Gravity.LEFT);
	    setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    
	    RelativeLayout layout = new RelativeLayout(context);
	    layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    layout.setGravity(Gravity.CENTER_VERTICAL);
	    //layout.setBackgroundColor(Color.DKGRAY);
	    
	    
	    TextView text = new TextView(context);
    	text.setTextSize(16);
    	text.setText(alarm.getTitle());
    	RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	
    	pa.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	pa.addRule(RelativeLayout.CENTER_VERTICAL);
    	
    	
    	text.setLayoutParams(pa);
    	text.setGravity(Gravity.BOTTOM);
    	//text.setBackgroundColor(Color.BLUE);
    	
    	layout.addView(text);
    	
    	
    	
    	//addView(text);
    	
    	
    	ImageButton button = new ImageButton(context);
    	button.setImageResource(drawable.ic_delete);
    	pa = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	
    	pa.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	button.setLayoutParams(pa);
    	
    	
    	layout.addView(button);
    	
    	
    	button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//cleaning alarm and then removing the view
				man.removeAlarmAndPendingIntent(alarm);
				container.removeView(AlertView.this);
			}
		});
    	
    	addView(layout);
	}



}
