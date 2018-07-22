package com.mobile.terasoft.adk;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.microbridge.server.Client;
import org.microbridge.server.Server;
import org.microbridge.server.ServerListener;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.terasoft.ioio.graphview.GraphView.GraphViewData;
import com.mobile.terasoft.ioio.model.TRC;
import com.mobile.terasoft.ioio.model.TRModel;
import com.mobile.terasoft.ioio.view.MainActivity;

public class ArduinoADK implements ServerListener {
	public int mAnalog1;
	public int mAnalog2;
	public int mAnalog3;
	public int mAnalog4;
	public String msg;

	// *****************************************************************
	// Transistor voltage variable
	float Vbb = 0;
	float Vcc = 0;
	float Vrb = 0;
	float Vrc = 0;
	float Vce = 0;
	// Transistor current variable
	float Ic = 0;
	float Ib = 0;
	// *****************************************************************
	// Analog num variable
	float adc0_num = 0;
	float adc1_num = 0;
	float adc2_num = 0;
	float adc3_num = 0;
	// *****************************************************************
	// Analog num variable
	public float Ic_micro;
	public float Ic_pos;

	// *****************************************************************
	// Analog num variable
	public Server server = null;
	public MainActivity mainActivity;
	public TRModel model;
	public ArduinoConfig con = new ArduinoConfig();
	// *****************************************************************
	// Graph view data
	public final int graphDataNum = 15;
	public GraphViewData[] graphData = new GraphViewData[graphDataNum];
	public float gVceSample = 0.0f;
	public int gCountIndex = 0;
	// public float VbbSample = 1.0f;
	public boolean isConnect;
	public boolean isEndLine;
	public boolean isFinnish;
	public int lastMsg;
	public float lastData;
	/************************************************************
	 * 
	 * Variable SharedPreferences for Setting Curve Starting
	 * 
	 ************************************************************/
	public SharedPreferences mSharedPreferences;
	public Editor mEditor;
	public boolean isFirst;

	/************************************************************
	 * 
	 * 1.Class Contractor
	 * 
	 ************************************************************/
	public ArduinoADK(MainActivity c, TRModel m) {
		mainActivity = c;
		model = m;
		// Create TCP server (based on MicroBridge LightWeight Server)
		try {
			server = new Server(4568); // Use the same port number used in ADK
			server.start();
			server.addListener(this);

		} catch (IOException e) {
			Log.e("ADK", "Unable to start TCP server", e);
			System.exit(-1);
		}
	}

	/************************************************************
	 * 
	 * 2.Server Event Listener
	 * 
	 ************************************************************/
	@Override
	public void onServerStarted(Server server) {
		// TODO Auto-generated method stub
		display("Started");
	}

	@Override
	public void onServerStopped(Server server) {
		// TODO Auto-generated method stub
		display("Stopped");
		isConnect = false;
	}

	@Override
	public void onClientConnect(Server server, Client client) {
		// TODO Auto-generated method stub
		display("Connected");
		isConnect = true;
		SettingInit();
	}

	@Override
	public void onClientDisconnect(Server server, Client client) {
		// TODO Auto-generated method stub
		display("Disconnect");
	}

	@Override
	public void onReceive(Client client, byte[] data) {
		/*
		 * if (data.length == 2) { mAnalog1 = (data[0] & 0xff) | ((data[1] &
		 * 0xff) << 8); } else if (data.length == 8) { mAnalog1 = (data[0] &
		 * 0xff) | ((data[1] & 0xff) << 8); // Analog VCC
		 * 
		 * mAnalog2 = (data[2] & 0xff) | ((data[3] & 0xff) << 8); // Analog VRC
		 * 
		 * mAnalog3 = (data[4] & 0xff) | ((data[5] & 0xff) << 8); // Analog VBB
		 * 
		 * mAnalog4 = (data[6] & 0xff) | ((data[7] & 0xff) << 8); // Analog VRB
		 * 
		 * new UpdateData1().execute(mAnalog1, mAnalog2, mAnalog3, mAnalog4); }
		 * else { if (data.length > 1) { msg = new String(data); new
		 * UpdateData2().execute(msg); } }
		 */
		// Check line data
		if (data.length > 20) {
			try {
				mainActivity.setDialogSignalProgress(true);
				msg = new String(data);
				loadingData(msg);
				// new UpdateData().execute(msg);
				lastMsg = 0;
			} catch (Exception e) {
				display("EX: UpdateData" + e.getMessage());
			}
		} else if (data.length == 10) {
			msg = new String(data);
			mainActivity.setDialogWaitProgress(false);
			display("Pin Length: " + String.valueOf(data.length) + ":" + msg);
			model.setPinName(msg);
		} else if (data.length == 8) {
			msg = new String(data);
			display("End Line Length: " + String.valueOf(data.length) + ":"
					+ msg);
			// isEndLine = true;

			if (graphData.length > 0) {
				display("GraphLength : " + String.valueOf(graphData.length)
						+ "gCount:" + String.valueOf(gCountIndex)
						+ ":End:Last:" + lastMsg);
				try {
					Ib = Ib / graphDataNum;
					mainActivity.setDialogSignalProgress(false);
					model.setGraphViewData(graphData, Ib);
					ClearData();
				} catch (Exception e) {
					display("EX Add graph:" + e.getMessage());
				}
			}

		} else if (data.length == 4) {
			msg = new String(data);
			display("End Graph Length: " + String.valueOf(data.length) + ":"
					+ msg);

			// isFinnish = true;
			model.setGraphFinnish();
		}
		// Check end line data
		else {
			if (lastMsg < data.length && isEndLine) {
				lastMsg = data.length;
				// if (graphData.length > 0 ) {
				// display("GraphLength : " + String.valueOf(graphData.length)
				// + "gCount:" + String.valueOf(gCountIndex)
				// + ":End:Last:" + lastMsg);
				// try {
				// Ib = Ib / graphDataNum;
				// mainActivity.setDialogSignalProgress(false);
				// model.setGraphViewData(graphData, Ib);
				// ClearData();
				// } catch (Exception e) {
				// display("EX Add graph:" + e.getMessage());
				// }
				// }
			}
		}
		// Check end line data
	}

	/************************************************************
	 * 
	 * Loading Data from ADK
	 * 
	 ************************************************************/
	public void loadingData(String value) {
		try {
			String mStringTrim = value.trim();
			String[] mString = mStringTrim.split("\t");
			Float[] mVolt = new Float[4];
			mVolt[0] = Float.parseFloat(mString[0]);
			mVolt[1] = Float.parseFloat(mString[1]);
			mVolt[2] = Float.parseFloat(mString[2]);
			mVolt[3] = Float.parseFloat(mString[3]);
			if (mVolt.length > 0) {
				// Calculate VBB & IB
				Vcc = (mVolt[0] / 4095) * 15;
				Vrc = (mVolt[1] / 4095) * 15;
				Ic = (Vcc - Vrc) / con.Rc;

				// Calculate VCC & IC
				Vbb = (mVolt[2] / 4095) * 15;
				Vrb = (mVolt[3] / 4095) * 15;
				Ib += (Vbb - Vrb) / con.Rb;
				// -----------------------------//
				Ic_micro = Ic * con.microAmp;
				Ic_pos = Ic_micro / con.Ratio_mAh;

				// Sample Data
				if (gCountIndex < graphDataNum) {
					if (gCountIndex == 0) {
						graphData[gCountIndex] = new GraphViewData(gVceSample,
								0.0f);
					} else {
						graphData[gCountIndex] = new GraphViewData(gVceSample,
								Math.abs(Ic_pos));
					}
					gVceSample += 0.95f;
					gCountIndex++;
				}
			}
			model.setProgressGraphUpdate(gCountIndex);
		} catch (Exception e) {
			display("loadingData: " + e.getMessage());
		}
	}

	public void ClearData() {
		gVceSample = 0;
		gCountIndex = 0;
		msg = null;
		graphData = new GraphViewData[graphDataNum];

	}

	/************************************************************
	 * 
	 * Setting and Init
	 * 
	 ************************************************************/

	public void SettingInit() throws NullPointerException {
		try {
			mSharedPreferences = mainActivity.getSharedPreferences(TRC.PrefKey,
					mainActivity.MODE_PRIVATE);
			Map<String, ?> map = mSharedPreferences.getAll();
			if (!map.isEmpty()) {
				String vce_max = String.valueOf(map.get(TRC.VCE_MAX_KEY));
				String vce_step = String.valueOf(map.get(TRC.VCE_STEP_KEY));

				String ib_max = String.valueOf(map.get(TRC.IB_MAX_KEY));
				String ib_step = String.valueOf(map.get(TRC.IB_STEP_KEY));

				//int numIb_max = Integer.parseInt(ib_max) * 100;
				//int numIb_step = Integer.parseInt(ib_step) * 10;

				//ib_max = String.valueOf(numIb_max);
				//ib_step = String.valueOf(numIb_step);

				JSONObject jObject = new JSONObject();
				try {
					jObject.put("VCE_MAX", vce_max);
					jObject.put("VCE_STEP", vce_step);
					jObject.put("IB_MAX", ib_max);
					jObject.put("IB_STEP", ib_step);
					sendCommand("S\r" + jObject.toString());
					//sendCommandTask("S\r" + jObject.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			display("Ex:" + e.getMessage());
		}
	}

	/************************************************************
	 * 
	 * 3.SendData to Arduino ADK
	 * 
	 ************************************************************/
	public void sendCommand(String cmd) {
		try {
			server.send(cmd + "\n");
		} catch (Exception e) {
			display("ADK " + e.getMessage());
			Log.e("Seeeduino ADK", "problem sending TCP message", e);
		}
	}

	public void sendCommandTask(String cmd) {
		new CommandTask().execute(cmd);
	}

//	public void sendData(String cmd, String value) {
//		try {
//			server.send(cmd + "," + value + "\n");
//		} catch (Exception e) {
//			Log.e("Seeeduino ADK", "problem sending TCP message", e);
//		}
//	}

	/************************************************************
	 * 
	 * 4.Show dialog string
	 * 
	 ************************************************************/
	public void display(final String msg) {
		mainActivity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(mainActivity, msg, Toast.LENGTH_LONG).show();
			}
		});
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
				display("ADK " + e.getMessage());
				Log.e("Seeeduino ADK", "problem sending TCP message", e);
			}
			return null;
		}
	}

	// private class UpdateData extends AsyncTask<String, String, Float[]> {
	//
	// @Override
	// protected Float[] doInBackground(final String... sensorValue) {
	//
	// try {
	// String mStringTrim = sensorValue[0].trim();
	// String[] mString = mStringTrim.split("\t");
	// Float[] mVolt = new Float[4];
	// mVolt[0] = Float.parseFloat(mString[0]);
	// mVolt[1] = Float.parseFloat(mString[1]);
	// mVolt[2] = Float.parseFloat(mString[2]);
	// mVolt[3] = Float.parseFloat(mString[3]);
	// // Update Progress
	// publishProgress(String.valueOf(gCountIndex));
	// return mVolt;
	// } catch (Exception e) {
	// display("Background:" + e.getMessage());
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onProgressUpdate(String... values) {
	// model.setProgressGraphUpdate(Integer.parseInt(values[0]));
	// }
	//
	// @Override
	// protected void onPostExecute(Float[] result) {
	// if (result.length > 0) {
	// // Calculate VBB & IB
	// Vcc = (result[0] / 4095) * 15;
	// Vrc = (result[1] / 4095) * 15;
	// Ic = (Vcc - Vrc) / con.Rc;
	//
	// // Calculate VCC & IC
	// Vbb = (result[2] / 4095) * 15;
	// Vrb = (result[3] / 4095) * 15;
	// Ib += (Vbb - Vrb) / con.Rb;
	// // -----------------------------//
	// Ic_micro = Ic * con.microAmp;
	// Ic_pos = Ic_micro / con.Ratio_mAh;
	//
	// // Sample Data
	// if (gCountIndex < graphDataNum) {
	// graphData[gCountIndex] = new GraphViewData(gVceSample,
	// Ic_pos);
	// gVceSample += 0.2f;
	// gCountIndex++;
	// }
	// }
	// }
	// }

}
