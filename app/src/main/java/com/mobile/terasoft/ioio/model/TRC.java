package com.mobile.terasoft.ioio.model;

public class TRC {
	/*
	 * TRC = TransistorCurveConstatns 
	 * AS - Activity to Service 
	 * SA - Service to Activity 
	 * BD - Bidirectional
	 */
	public static final String PrefKey = "Setting";
	
	public static final String STOP = "STOP_IOIO";	
	public static final String START = "START_IOIO";
	public static final String VCE = "VCE";
	public static final String VBE = "VBE";
	public static final String IC = "IC";
	public static final String IB = "IB";
	
	public static final int VCE_MAX = 15;		//	15V
	public static final int VCE_STEP = 1;		//	1V
	public static final int IB_MAX = 10;	   		// 	1 mA
	public static final int IB_STEP = 10; 		// 	1000uA = 1mA
	
	public static final String VCE_MAX_KEY = "VCE_MAX";
	public static final String VCE_STEP_KEY = "VCE_STEP";
	public static final String IB_MAX_KEY = "IB_MAX";
	public static final String IB_STEP_KEY = "IB_STEP";	
	
	public static final int AN1 = 31;
	public static final int AN2 = 32;
	public static final int AN3 = 33;
	public static final int AN4 = 34;
	
	public static final int PWM1_OPENDRAIN = 47;
	public static final int PWM2_OPENDRAIN = 48;
		
	public static final int misoPin = 35;
	public static final int mosiPin = 36;
	public static final int clkPin = 37;
	public static final int ssPin = 4;
	
	public static final int csPin = 12;
	public static final int ldacPin = 11;
	public static final int shdnPin = 10;
	
	public static final int RC_VALUE = 0;
	public static final int RB_VALUE = 0;

	public static final int MSG_REGISTER_CLIENT = 0;
	public static final int MSG_UNREGISTER_CLIENT = 1;

	public static final int AS_START_IOIO = 2;
	public static final int AS_RESTART_IOIO = 3;

	public static final int AS_SET_RC_VALUE = 4;
	public static final int AS_SET_RB_VALUE = 5;
	public static final int AS_SET_VCE_MAX = 6;
	public static final int AS_SET_VBE_MAX = 7;
	public static final int AS_SET_IC_MAX = 8;
	public static final int AS_SET_IB_MAX = 9;
	public static final int AS_SET_VCE_STEP = 10;
	public static final int AS_SET_IB_STEP = 11;

	public static final int SA_GET_VCE_VALUE = 12;
	public static final int SA_GET_VBE_VALUE = 13;
	public static final int SA_GET_IB_VALUE = 14;
	public static final int SA_GET_IC_VALUE = 15;
}
