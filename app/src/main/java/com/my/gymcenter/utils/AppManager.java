package com.my.gymcenter.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;
import java.util.Stack;

public class AppManager {
	private static Stack<Activity> mActivityStack;
	private static AppManager mAppManager;

	public static AppManager getInstance() {
		if (mAppManager == null)
			mAppManager = new AppManager();
		return mAppManager;
	}

	@SuppressWarnings("deprecation")
	public void AppExit(Context paramContext) {
		try {
			killAllActivity();
			((ActivityManager) paramContext.getSystemService(Context.ACTIVITY_SERVICE))
					.restartPackage(paramContext.getPackageName());
			System.exit(0);
		} catch (Exception ignored) {
		}
	}

	public void addActivity(Activity paramActivity) {
		if (mActivityStack == null)
			mActivityStack = new Stack<Activity>();
		mActivityStack.add(paramActivity);
	}

	public Activity getTopActivity() {
		return (Activity) mActivityStack.lastElement();
	}

	public void killActivity(Activity paramActivity) {
		if (paramActivity != null) {
			mActivityStack.remove(paramActivity);
			paramActivity.finish();
		}
	}

	public void killActivity(Class<?> paramClass) {
		Iterator<Activity> localIterator = mActivityStack.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return;
			Activity localActivity = localIterator.next();
			if (localActivity.getClass().equals(paramClass))
				killActivity(localActivity);
		}
	}

	public void killAllActivity() {
		mActivityStack = new Stack<Activity>();
		int i = 0;
		int j = mActivityStack.size();
		while (true) {
			if (i >= j) {
				mActivityStack.clear();
				return;
			}
			if (mActivityStack.get(i) != null)
				((Activity) mActivityStack.get(i)).finish();
			i++;
		}
	}

	public void killTopActivity() {
		killActivity((Activity) mActivityStack.lastElement());
	}
}
