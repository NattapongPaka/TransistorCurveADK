package com.mobile.terasoft.ioio.view;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.microbridge.server.AbstractServerListener;
import org.microbridge.server.Client;
import org.microbridge.server.Server;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Testing extends Activity {

	public Server server = null;
	// String command
	// Set command
	public String CMD_START = "start";
	public String CMD_SET_VBB = "set_vbb";
	public String CMD_SET_VCC = "set_vcc";
	// Get command
	public String CMD_GET_VBB_VALUE = "get_vbb_value";
	public String CMD_GET_VCC_VALUE = "get_vcc_value";
	public String CMD_GET_VBB_ANALOG = "get_vbb_analog";
	public String CMD_GET_VCC_ANALOG = "get_vcc_analog";
	public String CMD_GET_VRB_ANALOG = "get_vrb_analog";
	public String CMD_GET_VRC_ANALOG = "get_vrc_analog";
	// Checking command
	public String CMD_CHK_PIN = "check_pin";
	public String CMD_SEND_DATA = "send_data";
	public String CMD_RECONNECT = "reconnect";
	/************************************************************/
	public Button btnStart;
	public Button btnTestJson;
	public Button btnTestPin;
	public TextView txtResult;

	public int mAnalog1;
	public int mAnalog2;
	public int mAnalog3;
	public int mAnalog4;
	public String msg;

	// *****************************************************************
	// Transistor constant variable
	final float Vbe = 0.7f;
	final float Rc = 1000;
	final float Rb = 10000;
	// Transistor voltage variable
	float Vbb = 0;
	float Vcc = 0;
	float Vrb = 0;
	float Vrc = 0;
	float Vce = 0;
	// Transistor current variable
	float Ic = 0;
	float Ib = 0;

	float Ic_micro;
	float Ic_pos;
	static final int microAmp = 1000000;
	static final int Ratio_mAh = 1000; // 1 Rect / 1000 mA

	public void sendData(String cmd) {
		new CommandTask().execute(cmd);
	}

	/************************************************************
	 * 
	 * 5.Class background thread
	 * 
	 ************************************************************/
	private class CommandTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				server.send(params[0] + "\n");
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),	"ADK " + e.getMessage(), Toast.LENGTH_LONG).show();
				Log.e("Seeeduino ADK", "problem sending TCP message", e);
			}
			return null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_testing);
		// **************************************************************
		btnTestPin = (Button) findViewById(R.id.btnTestPin);
		txtResult = (TextView) findViewById(R.id.txtResult);
		btnTestJson = (Button) findViewById(R.id.btnTestJson);
		btnStart = (Button) findViewById(R.id.btnStart);
		// **************************************************************
		btnTestPin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendData(CMD_CHK_PIN);
			}
		});
		// **************************************************************
		btnTestJson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject jObject = new JSONObject();
				try {
					jObject.put("VCE_MAX", 15);
					jObject.put("VCE_STEP", 1);
					jObject.put("IB_MAX",1000);
					jObject.put("IB_STEP",100);
					sendData("S\r"+jObject.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// **************************************************************
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendData(CMD_START);
			}
		});
		// **************************************************************
		// Create TCP server (based on MicroBridge LightWeight Server)
		try {
			
			server = new Server(4568); // Use the same port number used in ADK
			server.start();

		} catch (IOException e) {
			Log.e("ADK", "Unable to start TCP server", e);
			System.exit(-1);
		}
		// **************************************************************
		server.addListener(new AbstractServerListener() {
			
			@Override
			public void onClientConnect(Server server, Client client) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), "Connected...", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onReceive(Client client, final byte[] data) {
				// TODO Auto-generated method stub
				//
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							if (data.length > 1) {
								msg = new String(data);
								Toast.makeText(
										getApplicationContext(),
										"Data length:" + data.length + "\n"
												+ msg, Toast.LENGTH_LONG)
										.show();
								loadingData(msg);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
			}
		});
		// **************************************************************
	}

	public void loadingData(String value) {
		try {
			txtResult.setText(value);
		} catch (Exception e) {

		}

	}

}
