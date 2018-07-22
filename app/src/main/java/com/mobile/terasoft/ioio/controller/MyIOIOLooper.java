//package com.mobile.terasoft.ioio.controller;
//
//import ioio.lib.api.AnalogInput;
//
//import ioio.lib.api.DigitalInput;
//import ioio.lib.api.DigitalOutput;
//import ioio.lib.api.DigitalOutput.Spec;
//import ioio.lib.api.DigitalOutput.Spec.Mode;
//import ioio.lib.api.IOIO;
//import ioio.lib.api.PwmOutput;
//import ioio.lib.api.SpiMaster;
//import ioio.lib.api.SpiMaster.Rate;
//import ioio.lib.api.TwiMaster;
//import ioio.lib.api.exception.ConnectionLostException;
//import ioio.lib.api.exception.OutOfResourceException;
//import ioio.lib.util.BaseIOIOLooper;
//
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.Toast;
//
//import com.mobile.terasoft.ioio.model.TRC;
//import com.mobile.terasoft.ioio.model.TRModel;
//import com.mobile.terasoft.ioio.view.MainActivity;
//
//public class MyIOIOLooper {
//
//	public DigitalOutput CS, LDAC, SHDN;
//	public DigitalOutput led_;
//	public AnalogInput AN1, AN2, AN3, AN4;
//	public PwmOutput PWMO1, PWMO2;
//	public SpiMaster SPI;
//
//	// public static final int[] ssPins = new int[] { 4, 5, 6, 7, 8 };
//	public byte[] wrieData;
//	public byte[] request = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
//	public byte[] readData = new byte[4];
//	/**************************************************************************************/
//	public Waveforms mWaveforms = new Waveforms();
//	public MainActivity mainActivity;
//	public TRModel model;
//	/**************************************************************************************/
//	public boolean isRunning;
//	public float vce, vbe, ib, ic;
//	public int voltage_max = 15;
//	public float current_pwm1, current_pwm2, current_voltage;
//	public int freq;
//
//	/***************************************************************************************
//	 * 
//	 * Override Method
//	 * 
//	 ***************************************************************************************/
//
//	public MyIOIOLooper(MainActivity c, TRModel m) {
//		mainActivity = c;
//		model = m;
//		wrieData = new byte[mWaveforms.maxSamplesNum];
//		wrieData = ConvertArray2D(mWaveforms.waveformsTable);
//	}
//
//	@Override
//	protected void setup() throws ConnectionLostException, InterruptedException {
//		led_ = ioio_.openDigitalOutput(IOIO.LED_PIN);
//
//		CS = ioio_.openDigitalOutput(TRC.csPin);
//		LDAC = ioio_.openDigitalOutput(TRC.ldacPin);
//		SHDN = ioio_.openDigitalOutput(TRC.shdnPin);
//
//		AN1 = ioio_.openAnalogInput(TRC.AN1);
//		AN2 = ioio_.openAnalogInput(TRC.AN2);
//		AN3 = ioio_.openAnalogInput(TRC.AN3);
//		AN4 = ioio_.openAnalogInput(TRC.AN4);
//
//		AN1.setBuffer(256);
//		AN2.setBuffer(256);
//		AN3.setBuffer(256);
//		AN4.setBuffer(256);
//
//		PWMO1 = ioio_.openPwmOutput(new Spec(TRC.PWM1_OPENDRAIN,
//				Mode.OPEN_DRAIN), 1);
//		PWMO2 = ioio_.openPwmOutput(new Spec(TRC.PWM2_OPENDRAIN,
//				Mode.OPEN_DRAIN), 10);
//
//		// SPI = ioio_.openSpiMaster(new Spec(MISO) , new Spec(MOSI), new
//		// Spec(CLK), new Spec(new Spec(ssPin)), new
//		// SpiMaster.Config(Rate.RATE_125K,true,true));
//
//		SPI = ioio_.openSpiMaster(new DigitalInput.Spec(TRC.misoPin,
//				ioio.lib.api.DigitalInput.Spec.Mode.PULL_UP),
//				new DigitalOutput.Spec(TRC.mosiPin), new DigitalOutput.Spec(
//						TRC.clkPin),
//				new DigitalOutput.Spec[] { new DigitalOutput.Spec(TRC.ssPin) },
//				new SpiMaster.Config(Rate.RATE_125K));
//
//		showLabel("Connecting Success");
//	}
//
//	@Override
//	public void loop() {
//		try {
//
//			if (isRunning) {
//				freq++;
//
//				// ioio_.beginBatch();
//				// PWMO1.setDutyCycle(freq * 0.01f);
//				// PWMO2.setDutyCycle(freq * 0.01f);
//				// ioio_.endBatch();
//
//				LedBlick();
//
//				// Update Model
//				//model.setDUTY_CYCLE1((float) freq * 0.01f);
//				//model.setVCE(AN1.getVoltageBuffered());
//
//				if (freq >= 100)
//					isRunning = false;
//			} else {
//				led_.write(false);
//				freq = 0;
//			}
//			Thread.sleep(100);
//		} catch (InterruptedException ex) {
//			ex.printStackTrace();
//		} catch (ConnectionLostException e) {
//			e.printStackTrace();
//		} catch (OutOfResourceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void disconnected() {
//		// TODO Auto-generated method stub
//		super.disconnected();
//	}
//
//	public void LedBlick() {
//		try {
//			led_.write(false);
//			Thread.sleep(50);
//			led_.write(true);
//			Thread.sleep(50);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ConnectionLostException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/***************************************************************************************
//	 * 
//	 * Public Method
//	 * 
//	 ****************************************************************************************/
//	public void showLabel(final String msg) {
//		mainActivity.runOnUiThread(new Runnable() {
//			public void run() {
//				Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
//
//	public byte[] ConvertArray2D(final int[][] array) {
//		int rows = array.length, cols = array[0].length;
//		byte[] mono = new byte[(rows * cols)];
//		for (int i = 0; i < rows; i++)
//			System.arraycopy(array[i], 0, mono, (i * cols), cols);
//		return mono;
//	}
//
//	public void SetupMCP4922() throws ConnectionLostException {
//		CS.write(true);
//		LDAC.write(true);
//		SHDN.write(true);
//	}
//
//	public void WriteMCP4922(char DAC_Channel, int DAC_Data)
//			throws ConnectionLostException {
//		CS.write(false);
//
//		switch (DAC_Channel) {
//		case 0x00:
//			DAC_Data |= 0x3000;
//			break;
//
//		case 0x01:
//			DAC_Data |= 0xB000;
//			break;
//		}
//		
//		CS.write(true);
//		LDAC.write(false);
//		LDAC.write(true);
//	}
//
//	/***************************************************************************************
//	 * 
//	 * Getter Method
//	 * 
//	 ****************************************************************************************/
//	public IOIO getIOIOBoardInstance() {
//		return ioio_;
//	}
//
//	/***************************************************************************************
//	 * 
//	 * Setter Method
//	 * 
//	 ****************************************************************************************/
//	public void setRunning(boolean b) {
//		this.isRunning = b;
//	}
//}
