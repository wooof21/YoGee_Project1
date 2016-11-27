package tools;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import application.MyApplication;

public class Exit extends Application{

	private List<Activity> activityList = new LinkedList<Activity>();

	private static Exit instance;

	private Exit(){

	}

	public static Exit getInstance(){
		if (instance == null){
			instance = new Exit();
		}
		return instance;
	}

	public void addActivity(Activity activity){
		if (activityList != null && activityList.size() > 0){
			if (!activityList.contains(activity)){
				activityList.add(activity);
			}
		}else{
			activityList.add(activity);
		}

	}

	public void exit(){
		try{
			MyApplication.getApplication().stopTrace();
		}catch(Exception e){
			// TODO: handle exception
			Log.e("exception", e.toString());
		}
		if (activityList != null && activityList.size() > 0){
			for(Activity activity : activityList){
				activity.finish();
			}
		}
		System.exit(0);
	}
}
