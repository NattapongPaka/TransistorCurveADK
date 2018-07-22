package com.mobile.terasoft.adk;

public class ArduinoConfig {
	// String command
	//*****************************************************************
	//String command
	//Set command
	public final String CMD_START			= "start"; 
	public final String CMD_START_NPN		= "start_npn"; 
	public final String CMD_START_PNP		= "start_pnp"; 
	public final String CMD_SET_VCE_MAX		= "set_vce_max";
	public final String CMD_SET_VCE_STEP	= "set_vce_step";
	public final String CMD_SET_IB_MAX		= "set_ib_max";
	public final String CMD_SET_IB_STEP		= "set_ib_step";
	public final String CMD_SET_VBB			= "set_vbb";
	public final String CMD_SET_VCC			= "set_vcc";
	public final String CMD_SET_PIN_NPN		= "set_pin_npn";
	public final String CMD_SET_PIN_PNP		= "set_pin_pnp";
	public final String CMD_SETTING			= "setting";
	public final String CMD_NEXT_LINE		= "next_line";
	public final String CMD_END				= "end";
	public final String CMD_END_LINE		= "endline";
	//Get command
	public final String CMD_GET_VBB_VALUE = "get_vbb_value";
	public final String CMD_GET_VCC_VALUE = "get_vcc_value";
	public final String CMD_GET_VBB_ANALOG = "get_vbb_analog";
	public final String CMD_GET_VCC_ANALOG = "get_vcc_analog";
	public final String CMD_GET_VRB_ANALOG = "get_vrb_analog";
	public final String CMD_GET_VRC_ANALOG = "get_vrc_analog";
	//Checking command
	public final String CMD_CHK_PIN	= "check_pin";
	public final String CMD_SEND_DATA	= "send_data";
	public final String CMD_RECONNECT	= "reconnect";
	public final String mKey_IB = "IB_KEY";
	public final String mKey_GraphData = "GraphData_KEY";
	/************************************************************/
	// Divider constant variable
	public final int R1 = 2000000;
	public final int R2 = 1000000;
	public final float DivRatio1 = 0.2855f;
	public final float DivRatio2 = 0.2755f;
	// *****************************************************************
	public final int microAmp = 1000000;
	public final int Ratio_mAh = 1000; // 1 Rect / 1000 mA
	// Transistor constant variable
	public final float Vbe = 0.7f;
	public final float Rc = 100;
	public final float Rb = 10000;
	public float simpleVbb = 1.0f;
}
