package com.mobile.terasoft.ioio.view;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.terasoft.adk.ArduinoADK;
import com.mobile.terasoft.adk.ArduinoConfig;
import com.mobile.terasoft.ioio.graphview.GraphView;
import com.mobile.terasoft.ioio.graphview.GraphView.GraphViewData;
import com.mobile.terasoft.ioio.graphview.GraphView.LegendAlign;
import com.mobile.terasoft.ioio.graphview.GraphViewSeries;
import com.mobile.terasoft.ioio.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.mobile.terasoft.ioio.graphview.LineGraphView;
import com.mobile.terasoft.ioio.model.TRModel;
import com.mobile.terasoft.ioio.model.TRModel.OnGraphFinnishChangeListener;
import com.mobile.terasoft.ioio.model.TRModel.OnGraphViewDataChangeListener;
import com.mobile.terasoft.ioio.model.TRModel.OnICChangeListener;
import com.mobile.terasoft.ioio.model.TRModel.OnPinFoundChangeListener;
import com.mobile.terasoft.ioio.model.TRModel.OnProgressGraphUpdateChangeListener;

public class MainActivity extends Activity implements OnICChangeListener,
		OnGraphViewDataChangeListener, OnProgressGraphUpdateChangeListener,
		OnPinFoundChangeListener, OnGraphFinnishChangeListener {

	/************************************************************
	 * 
	 * WidGet View
	 * 
	 ************************************************************/
	public TextView txtPin1;
	public TextView txtPin2;
	public TextView txtPin3;
	public TextView txtType;
	public TextView txtTypeMain;
	public TextView txtPinMain;
	public TextView txtStatus;
	public TextView VCE;
	public TextView IB;
	public TextView IC;
	public Button btn1;
	public Button btn2;
	public Button btn3;
	public Button btn4;
	public Button btn5;
	private ProgressDialog mProgressDialog;
	private ProgressBar mProgressBar;
	/************************************************************
	 * 
	 * WidGet Variable
	 * 
	 ************************************************************/
	public static final int DIALOG_SIGNAL_PROGRESS = 0;
	public static final int DIALOG_WAIT_PROGRESS = 1;
	public static final int DIALOG_CHECK_PIN = 2;

	/************************************************************
	 * 
	 * Object Variable
	 * 
	 ************************************************************/
	public TRModel myModel = new TRModel();
	public ArduinoADK adk;
	/************************************************************
	 * 
	 * Object Graph
	 * 
	 ************************************************************/
	// private final Handler mHandler = new Handler();
	// private Runnable mTimer1;
	private GraphView graphView;
	private ArrayList<GraphViewSeries> mGraphViewSeriesList = new ArrayList<GraphViewSeries>();
	private double graph2LastXValue = 0.0;
	private LinearLayout layout;
	public ArduinoConfig con = new ArduinoConfig();
	/************************************************************
	 * 
	 * Variable Graph
	 * 
	 ************************************************************/
	public int countIndex = 0;
	public int lineNumber = 1;
	private static int Xview = 15;
	// toggle Button
	static boolean Lock;// whether lock the x-axis to 0-5
	static boolean AutoScrollX;// auto scroll to the last x value
	String[] tr;
	char signalPin[];
	/************************************************************
	 * 
	 * Variable PinName
	 * 
	 ************************************************************/
	public String pinName;

	@Override
	protected void onResume() {
		super.onResume();
	}

	/************************************************************
	 * 
	 * Stop Activity
	 * 
	 ************************************************************/
	@Override
	protected void onStop() {
		super.onStop();
	}

	/************************************************************
	 * 
	 * Pause Activity
	 * 
	 ************************************************************/
	@Override
	protected void onPause() {
		super.onPause();
	}

	/************************************************************
	 * 
	 * Destroy Activity
	 * 
	 ************************************************************/
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adk.server.stop();
	}

	/************************************************************
	 * 
	 * Main Activity
	 * 
	 ************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/******************************************/
		setContentView(R.layout.layout_main);
		/******************************************/
		init();
		/******************************************/
		// RegisterListener
		myModel.setOnICChangeListener(this);
		myModel.setOnGraphViewDataChangeListener(this);
		myModel.setOnProgressGraphUpdateChangeListener(this);
		myModel.setOnPinFoundChangeListener(this);
		myModel.setOnGraphFinnishChangeListener(this);
		/******************************************/
	}

	/************************************************************
	 * 
	 * Initializing
	 * 
	 ************************************************************/
	private void init() {
		ViewInit();
		GraphInit();
		adk = new ArduinoADK(this, myModel);
	}

	private void ViewInit() {
		// TextView
		IB = (TextView) findViewById(R.id.IB);
		// VCE = (TextView) findViewById(R.id.VCE);
		IC = (TextView) findViewById(R.id.IC);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
		mProgressBar.setVisibility(View.GONE);
		txtStatus = (TextView) findViewById(R.id.txtStatus);
	}

	private void GraphInit() {
		// for (int i = 0; i < lineNumber; i++) {
		// GraphViewSeries g = new GraphViewSeries("Graph",
		// new GraphViewSeriesStyle(Color.BLUE, 1),
		// new GraphViewData[] { new GraphViewData(0.0, 0.0) });
		// mGraphViewSeriesList.add(g);
		// }
//		GraphViewSeries g = new GraphViewSeries("Graph",
//				new GraphViewSeriesStyle(Color.BLUE, 1),
//				new GraphViewData[] { new GraphViewData(0.0, 0.0) });

		graphView = new LineGraphView(this, "Transistor Characteristic Curve");

		graphView.setHorizontalLabels(new String[] { "0", "1", "2", "3", "4",
				"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
				"VCE", "" });

		graphView.setVerticalLabels(new String[] { "IC(mA)", "15", "14", "13",
				"12", "11", "10", "9", "8", "7", "6", "5", "4", "3", "2", "1",
				"0" });
		
		graphView.getGraphViewStyle().setGridColor(Color.GREEN);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.RED);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.RED);
		graphView.getGraphViewStyle().setTextSize(15);

		// graphView.getGraphViewStyle().setNumHorizontalLabels(21);
		graphView.getGraphViewStyle().setNumVerticalLabels(15);

		graphView.getGraphViewStyle().setVerticalLabelsWidth(80);
		graphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);

		graphView.setViewPort(0, Xview);
		graphView.setScrollable(true);
		graphView.setScalable(false);

		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.MIDDLE);
		graphView.setManualYAxis(true);
		graphView.setManualYAxisBounds(15, 0);

		// for (GraphViewSeries g : mGraphViewSeriesList)
		// graphView.addSeries(g);
		// graphView.addSeries(g);

		layout = (LinearLayout) findViewById(R.id.graph);
		layout.setDrawingCacheEnabled(true);
		layout.addView(graphView);

		AutoScrollX = true;
		Lock = true;
	}

	/************************************************************
	 * 
	 * Create Options Menu
	 * 
	 ************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater m = getMenuInflater();
		m.inflate(R.menu.menu, menu);
		return true;
	}

	/************************************************************
	 * 
	 * Event Options Menu
	 * 
	 ************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.menu_settings:
			i = new Intent(getBaseContext(), SettingActivity.class);
			startActivity(i);
			break;

		case R.id.menu_save:
			saveToInternalSorage(layout.getDrawingCache());
			break;

		case R.id.menu_start:
			if (adk.isConnect) {
				sendCommandUI(con.CMD_CHK_PIN);
				setDialogWaitProgress(true);
				clear();
				mProgressBar.setVisibility(View.VISIBLE);
				txtStatus.setText("�ͻ����ż�");
			} else {
				display("Can not connect ADK...");
			}
			break;

		case R.id.menu_testing:
			i = new Intent(getBaseContext(), Testing.class);
			startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/************************************************************
	 * 
	 * Graph Drawing
	 * 
	 ************************************************************/
	// Append Data
	public void setGraph(final float num) {
		mGraphViewSeriesList.get(countIndex).appendData(
				new GraphViewData(graph2LastXValue, num), AutoScrollX);
		if (Lock) {
			if (graph2LastXValue >= Xview) {
				if (countIndex == lineNumber - 1) {
					for (GraphViewSeries g : mGraphViewSeriesList) {
						g.resetData(new GraphViewData[] { new GraphViewData(
								0.0, 0.0) });
					}
					countIndex = 0;
				}
				graph2LastXValue = 0;
				countIndex++;
			} else {
				graphView.setViewPort(0, Xview);
			}
		} else {
			graphView.setViewPort(graph2LastXValue - Xview, Xview);
		}
		// X Axis value
		graph2LastXValue += 0.2;
		// Refresh Graph
		layout.removeView(graphView);
		layout.addView(graphView);
	}

	// Add Serial Data
	public void setGraphViewData(GraphViewData[] gData, float Ib) {
		// Random Color
		Random rnd = new Random();
		int color = Color.argb(255, rnd.nextInt(254), rnd.nextInt(254),
				rnd.nextInt(254));
		String mIb = String.valueOf(Ib);
		GraphViewSeries seriesRnd = new GraphViewSeries("Ib " + mIb + " uA",
				new GraphViewSeriesStyle(color, 3), gData);

		// Add Graph Serial
		graphView.addSeries(seriesRnd);
		graphView.setViewPort(0, Xview);
		graphView.setScrollable(false);
		layout.removeView(graphView);
		layout.addView(graphView);
	}

	/************************************************************
	 * 
	 * Save Graph to Bitmap
	 * 
	 ************************************************************/
	private String saveToInternalSorage(Bitmap bitmapImage) {
		// 1.Check directory
		String directory = null;
		directory = Environment.getExternalStorageDirectory().toString();
		File dir = new File(directory + "/Curve");
		// if not exists
		if (!dir.exists()) {
			directory = Environment.getExternalStorageDirectory().toString();
			new File(directory + "/Curve").mkdirs();
		}
		File mypath = new File(directory, "/Curve/My_Graph.png");
		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(mypath);
			// Use the compress method on the BitMap object to write image
			// to
			// the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();

			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(mypath.getPath())),
					"image/*");
			startActivity(intent);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return directory;
	}

	/************************************************************
	 * 
	 * Create Dialog
	 * 
	 ************************************************************/
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SIGNAL_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Signal Processing...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;

		case DIALOG_WAIT_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Waiting Processing...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;

		default:
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Exit")
				.setMessage("Are you sure you want to exit?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								runOnUiThread(new Runnable() {
									public void run() {
										adk.server.stop();
									}
								});
								finish();
							}
						}).setNegativeButton("No", null).show();
	}

	/************************************************************
	 * 
	 * Show Pin Dialog
	 * 
	 ************************************************************/
	public void setDialogPin(String pin) {
		// String[] tr = null;

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.this);
		builder.setTitle("Pin Found!!!");

		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.layout_pin, null);
		builder.setView(dialogView);
		builder.setPositiveButton("Continue",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// txtPinMain = (TextView)
						// findViewById(R.id.txtPinMain);
						txtTypeMain = (TextView) findViewById(R.id.txtTypeMain);
						txtPin1 = (TextView) findViewById(R.id.txtPin1);
						txtPin2 = (TextView) findViewById(R.id.txtPin2);
						txtPin3 = (TextView) findViewById(R.id.txtPin3);
						txtTypeMain.setText(tr[0]);
						txtPin1.setText(String.valueOf(signalPin[0]));
						txtPin2.setText(String.valueOf(signalPin[1]));
						txtPin3.setText(String.valueOf(signalPin[2]));
						// txtPinMain.setText(tr[1].toUpperCase());
						sendCommandUI(con.CMD_START);
						dialog.cancel();
					}
				});
		builder.setNegativeButton("Try Again",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendCommandUI(con.CMD_CHK_PIN);
						dialog.cancel();
						setDialogWaitProgress(true);
					}
				});

		txtPin1 = (TextView) dialogView.findViewById(R.id.txtPin1);
		txtPin2 = (TextView) dialogView.findViewById(R.id.txtPin2);
		txtPin3 = (TextView) dialogView.findViewById(R.id.txtPin3);
		txtType = (TextView) dialogView.findViewById(R.id.txtType);

		try {

			tr = pin.split(":");
			String mType = tr[0];
			String mPin = tr[1].toUpperCase();
			// String mID = tr[2];
			signalPin = new char[4];
			signalPin = mPin.toCharArray();
			txtPin1.setText(String.valueOf(signalPin[0]));
			txtPin2.setText(String.valueOf(signalPin[1]));
			txtPin3.setText(String.valueOf(signalPin[2]));
			if (mType.equalsIgnoreCase("NPN") || mType.equalsIgnoreCase("PNP")) {
				txtType.setText(mType);
			} else {
				builder.setTitle("Pin Not Found!!!");
				txtType.setText("Not Found");
			}

		} catch (Exception e) {
			display("Ex:" + e.getMessage());
		}

		AlertDialog mAlertDialog = builder.create();
		mAlertDialog.show();
	}

	public void display(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	public void clear() {
		graphView.removeAllSeries();
	}

	/************************************************************
	 * 
	 * Send command ui
	 * 
	 ************************************************************/
	public void sendCommandUI(final String cmd) {
		runOnUiThread(new Runnable() {
			public void run() {
				adk.sendCommandTask(cmd);
			}
		});
	}

	/************************************************************
	 * 
	 * View update
	 * 
	 ************************************************************/
	@Override
	public void onICChange(final TRModel mymodel) {
		runOnUiThread(new Runnable() {
			public void run() {
				float value = mymodel.getIC();
				setGraph(value);
			}
		});
	}

	@Override
	public void onGraphViewDataChange(final TRModel mymodel) {
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					Object objIb = mymodel.getGraphViewData().get(con.mKey_IB);
					Object objGraphData = mymodel.getGraphViewData().get(
							con.mKey_GraphData);

					float Ib = ((Float) objIb).floatValue();
					GraphViewData[] graphData = ((GraphViewData[]) objGraphData);

					Ib = (float) (Ib * Math.pow(10, 5));

					setGraphViewData(graphData, round(Ib, 3));
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Ex:" + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	public void setDialogSignalProgress(final boolean b) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (b) {
					showDialog(DIALOG_SIGNAL_PROGRESS);
				} else {
					dismissDialog(DIALOG_SIGNAL_PROGRESS);
				}
			}
		});
	}

	public void setDialogWaitProgress(final boolean b) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (b) {
					showDialog(DIALOG_WAIT_PROGRESS);
				} else {
					dismissDialog(DIALOG_WAIT_PROGRESS);
				}
			}
		});
	}

	@Override
	public void onProgressGraphUpdateChange(final TRModel mymodel) {
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					mProgressDialog.setProgress(mymodel
							.getProgressGraphUpdate());
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Ex:" + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onPinFoundChangeListener(final TRModel mymodel) {
		runOnUiThread(new Runnable() {
			public void run() {
				// pinName = mymodel.getPin();
				setDialogPin(mymodel.getPin());
			}
		});
	}

	@Override
	public void onGraphFinnishChangeListener(TRModel mymodel) {
		runOnUiThread(new Runnable() {
			public void run() {
				mProgressBar.setVisibility(View.GONE);
				txtStatus.setText("�����Թ");
			}
		});
	}

}
