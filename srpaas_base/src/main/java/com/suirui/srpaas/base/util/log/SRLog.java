package com.suirui.srpaas.base.util.log;

import android.util.Log;

import com.suirui.srpaas.base.contant.Configure;

/**
 * 控制整个应用的LOG打印
 *
 * @author cui.li
 *
 */
public final class SRLog {
	private int debugType = DebugType.V;//默认不答应log

	public static final class DebugType {
		public static final int V = 0;
		public static final int D = 1;
		public static final int I = 2;
		public static final int W = 3;
		public static final int E = 4;
		public static final int N = 5; // close all debug info

		public static final void setSRLog(boolean isLog) {//控制台的log
			Configure.LOG_STATE = isLog;
		}
	}

	public void D(String s) {
		try {
			if (debugType <= DebugType.D && Configure.LOG_STATE) {
				Log.d(TAG, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String TAG = "";

	/**
	 * 设置要打印LOG的类的TAG信息
	 *
	 * @param TAG
	 *            应包含这个类的包名和类名
	 */
	public SRLog(String TAG) {
		this.TAG = TAG;
	}

	/**
	 * 设置要打印LOG的类的TAG信息
	 *
	 * @param TAG
	 *            应包含这个类的包名和类名
	 * @param debugType
	 *            设置LOG的最高级别
	 */
	public SRLog(String TAG, int debugType) {
		this.TAG = TAG;
		this.debugType = debugType;
	}

	public void E(String s) {
		try {
			if (debugType <= DebugType.E && Configure.LOG_STATE) {
				Log.e(TAG, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void I(String s) {
		try {
			if (debugType <= DebugType.I && Configure.LOG_STATE) {
				Log.i(TAG, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void W(String s) {
		try {
			if (debugType <= DebugType.W && Configure.LOG_STATE) {
				Log.w(TAG, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void V(String s) {
		try {
			if (debugType <= DebugType.V && Configure.LOG_STATE) {
				Log.v(TAG, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
