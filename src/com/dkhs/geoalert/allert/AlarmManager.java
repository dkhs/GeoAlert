package com.dkhs.geoalert.allert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class AlarmManager {
	
	private final static String DATA_KEY = "serializied.alarms";
	
	private final static String DATA_FILE = "GeoAlert.alarms.file";
	
	private ArrayList<Alarm> currentAlarmsList=null;
	
	protected static AlarmManager manager = new AlarmManager();
	
	protected AlarmManager(){}
	
	public static AlarmManager getInstance(Bundle savedState,Context context){
		try {
			if(savedState!=null){
			manager.currentAlarmsList= (ArrayList<Alarm>)savedState.getSerializable(DATA_KEY);}
			if(manager.currentAlarmsList==null && context!=null){
				FileInputStream fis = context.openFileInput(DATA_FILE);
				ObjectInputStream ois= new ObjectInputStream(fis);
				manager.currentAlarmsList = (ArrayList<Alarm>)ois.readObject();
				ois.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(manager.currentAlarmsList==null) manager.currentAlarmsList= new ArrayList<Alarm>();
		return manager;
	}
	
	public void addAlarm(Alarm a){
		currentAlarmsList.add(a);
	}
	public void removeAlarm(Alarm a){
		currentAlarmsList.remove(a);
	}
	
	public Alarm getAlarm(int id){
		Alarm a=new Alarm();
		a.setUniqueID(id);
		if(currentAlarmsList!=null && currentAlarmsList.contains(a)){
			for(Alarm al : currentAlarmsList){
				if(al.getUniqueID()==a.getUniqueID()) return al;
			}
		}
		return null;
	}
	
	public List<Alarm> getAlarms(){
		return currentAlarmsList;
	}
	
	public void store(Bundle state, Context context){
		if(currentAlarmsList!=null ){
			if (state!=null) state.putSerializable(DATA_KEY, currentAlarmsList);
			if(context!=null){
				try {
					FileOutputStream fos = context.openFileOutput(DATA_FILE, Context.MODE_PRIVATE);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(currentAlarmsList);
					oos.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	public int getNxtUniqueID(){
		Alarm a = new Alarm();
		a.setUniqueID(1);
		do{
			if(!currentAlarmsList.contains(a)) return a.getUniqueID();
			a.setUniqueID(a.getUniqueID()+1);
		}while(true); 
	}
	
	
}
