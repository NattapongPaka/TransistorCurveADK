package com.mobile.terasoft.ioio.view;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.terasoft.ioio.model.TRC;

public class SettingActivity extends Activity implements
		OnSeekBarChangeListener, OnClickListener {

	public SharedPreferences mSharedPreferences;
	public Editor mEditor;

	public TextView txtVCE_STEP, txtIB_STEP, txtVCE_MAX, txtIB_MAX;
	public SeekBar sbVCE_STEP, sbIB_STEP, sbVCE_MAX, sbIB_MAX;
	public Button btnOK, btnDefault, btnCancel;
	public Button btnVCE_MAX_PLUS, btnVCE_MAX_MINUS;
	public Button btnIB_MAX_PLUS, btnIB_MAX_MINUS;
	public Button btnVCE_STEP_PLUS, btnVCE_STEP_MINUS;
	public Button btnIB_STEP_PLUS, btnIB_STEP_MINUS;

	public static final int PLUS = 1;
	public static final int MINUS = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting);
		init();
		initSettings();
	}

	private void initSettings() {
		Map<String, ?> map = mSharedPreferences.getAll();
		if (!map.isEmpty()) {
			String objVceMax = String.valueOf(map.get(TRC.VCE_MAX_KEY));
			String objVceStep = String.valueOf(map.get(TRC.VCE_STEP_KEY));
			String objIbMax = String.valueOf(map.get(TRC.IB_MAX_KEY));
			String objIbStep = String.valueOf(map.get(TRC.IB_STEP_KEY));

			int vce_max = Integer.parseInt(objVceMax);
			int vce_step = Integer.parseInt(objVceStep);
			int ib_max = Integer.parseInt(objIbMax);
			int ib_step = Integer.parseInt(objIbStep);

			sbVCE_MAX.setProgress(vce_max);
			sbIB_MAX.setProgress(ib_max);
			sbVCE_STEP.setProgress(vce_step);
			sbIB_STEP.setProgress(ib_step);
		}
	}

	private void init() {

		mSharedPreferences = getSharedPreferences(TRC.PrefKey, MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();

		txtVCE_MAX = (TextView) findViewById(R.id.txtVCE_MAX);
		txtIB_MAX = (TextView) findViewById(R.id.txtIB_MAX);
		txtVCE_STEP = (TextView) findViewById(R.id.txtVCE_STEP);
		txtIB_STEP = (TextView) findViewById(R.id.txtIB_STEP);

		sbVCE_MAX = (SeekBar) findViewById(R.id.sbVCE_MAX);
		sbIB_MAX = (SeekBar) findViewById(R.id.sbIB_MAX);
		sbVCE_STEP = (SeekBar) findViewById(R.id.sbVCE_STEP);
		sbIB_STEP = (SeekBar) findViewById(R.id.sbIB_STEP);

		btnOK = (Button) findViewById(R.id.btnOK);
		btnDefault = (Button) findViewById(R.id.btnDefault);
		btnCancel = (Button) findViewById(R.id.btnCalcel);

		btnVCE_MAX_PLUS = (Button) findViewById(R.id.btnVCE_MAX_PLUS);
		btnVCE_MAX_MINUS = (Button) findViewById(R.id.btnVCE_MAX_MINUS);

		btnIB_MAX_PLUS = (Button) findViewById(R.id.btnIB_MAX_PLUS);
		btnIB_MAX_MINUS = (Button) findViewById(R.id.btnIB_MAX_MINUS);

		btnVCE_STEP_PLUS = (Button) findViewById(R.id.btnVCE_STEP_PLUS);
		btnVCE_STEP_MINUS = (Button) findViewById(R.id.btnVCE_STEP_MINUS);

		btnIB_STEP_PLUS = (Button) findViewById(R.id.btnIB_STEP_PLUS);
		btnIB_STEP_MINUS = (Button) findViewById(R.id.btnIB_STEP_MINUS);

		btnOK.setOnClickListener(this);
		btnDefault.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnVCE_MAX_PLUS.setOnClickListener(this);
		btnVCE_MAX_MINUS.setOnClickListener(this);
		btnIB_MAX_PLUS.setOnClickListener(this);
		btnIB_MAX_MINUS.setOnClickListener(this);
		btnVCE_STEP_PLUS.setOnClickListener(this);
		btnVCE_STEP_MINUS.setOnClickListener(this);
		btnIB_STEP_PLUS.setOnClickListener(this);
		btnIB_STEP_MINUS.setOnClickListener(this);

		sbVCE_MAX.setOnSeekBarChangeListener(this);
		sbIB_MAX.setOnSeekBarChangeListener(this);
		sbVCE_STEP.setOnSeekBarChangeListener(this);
		sbIB_STEP.setOnSeekBarChangeListener(this);
		// Vce max
		sbVCE_MAX.setMax(TRC.VCE_MAX);
		sbVCE_MAX.setProgress(TRC.VCE_MAX);
		// Ib max
		sbIB_MAX.setMax(TRC.IB_MAX);
		sbIB_MAX.setProgress(TRC.IB_MAX);
		// Vce step
		sbVCE_STEP.setMax(TRC.VCE_STEP);
		sbVCE_STEP.setProgress(TRC.VCE_STEP);
		// Ib step
		sbIB_STEP.setMax(TRC.IB_STEP);
		sbIB_STEP.setProgress(TRC.IB_STEP);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnVCE_MAX_PLUS:
			sbIncrement(sbVCE_MAX, 1, PLUS);
			break;
		case R.id.btnVCE_MAX_MINUS:
			sbIncrement(sbVCE_MAX, 1, MINUS);
			break;
		case R.id.btnIB_MAX_PLUS:
			sbIncrement(sbIB_MAX, 1, PLUS);
			break;
		case R.id.btnIB_MAX_MINUS:
			sbIncrement(sbIB_MAX, 1, MINUS);
			break;
		case R.id.btnVCE_STEP_PLUS:
			sbIncrement(sbVCE_STEP, 1, PLUS);
			break;
		case R.id.btnVCE_STEP_MINUS:
			sbIncrement(sbVCE_STEP, 1, MINUS);
			break;
		case R.id.btnIB_STEP_PLUS:
			sbIncrement(sbIB_STEP, 1, PLUS);
			break;
		case R.id.btnIB_STEP_MINUS:
			sbIncrement(sbIB_STEP, 1, MINUS);
			break;

		case R.id.btnOK:
			if (checkSetting()) {
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				finish();
			}
			break;

		case R.id.btnDefault:
			setDefault();
			break;

		case R.id.btnCalcel:
			Intent ii = new Intent(this, MainActivity.class);
			// ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ii);
			finish();
			break;
		}
	}

	public boolean checkSetting() {
		boolean b = false;
		if (sbIB_MAX.getProgress() != 0 && sbIB_STEP.getProgress() != 0
				&& sbVCE_MAX.getProgress() != 0
				&& sbVCE_STEP.getProgress() != 0) {
			if (setValueSetting())
				b = true;
		}else{
			Toast.makeText(getApplicationContext(), "��سҵ�駤�����١��ͧ", Toast.LENGTH_SHORT).show();
		}
		return b;
	}

	public boolean setValueSetting() {
		mEditor.putInt(TRC.VCE_MAX_KEY, sbVCE_MAX.getProgress());
		mEditor.putInt(TRC.VCE_STEP_KEY, sbVCE_STEP.getProgress());
		mEditor.putInt(TRC.IB_MAX_KEY, sbIB_MAX.getProgress());
		mEditor.putInt(TRC.IB_STEP_KEY, sbIB_STEP.getProgress());
		return mEditor.commit();
	}

	public void setDefault() {
		sbIB_MAX.setProgress(TRC.IB_MAX);
		sbIB_STEP.setProgress(TRC.IB_STEP);
		sbVCE_MAX.setProgress(TRC.VCE_MAX);
		sbVCE_STEP.setProgress(TRC.VCE_STEP);
	}

	public void sbIncrement(SeekBar sbView, int step, int op) {
		int num = sbView.getProgress();
		if (op == PLUS) {
			sbView.setProgress(num + step);
		}
		if (op == MINUS) {
			sbView.setProgress(num - step);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.sbVCE_MAX:
			txtVCE_MAX.setText(Integer.toString(progress) + " V ");
			break;
		case R.id.sbVCE_STEP:
			txtVCE_STEP.setText(String.valueOf(progress) + " V ");
			break;
		case R.id.sbIB_MAX:
			txtIB_MAX.setText(Integer.toString(progress * 100) + " uA ");
			break;
		case R.id.sbIB_STEP:
			txtIB_STEP.setText(Integer.toString(progress * 10) + " uA ");
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

}