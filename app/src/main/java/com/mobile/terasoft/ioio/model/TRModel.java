package com.mobile.terasoft.ioio.model;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;

import com.mobile.terasoft.adk.ArduinoConfig;
import com.mobile.terasoft.ioio.graphview.GraphView.GraphViewData;
import com.mobile.terasoft.ioio.graphview.GraphViewSeries;
import com.mobile.terasoft.ioio.graphview.GraphViewSeries.GraphViewSeriesStyle;

public class TRModel {

	private float VRB, VRC;
	private float VBB, VCC;
	private float VCE, VBE;
	private float IB, IC;
	private GraphViewSeries graphSerial;
	private GraphViewData[] graphData;
	private int progressNum;
	private String pinName;
	private Map<String, Object> mData;
	private ArduinoConfig con = new ArduinoConfig();
	private boolean GraphFinnish;
	/*******************************************************************
	 * 
	 * 1.Method Interface
	 * 
	 *******************************************************************/
	public interface OnVCEChangeListener {
		void onVCEChange(TRModel mymodel);
	}

	public interface OnVCCChangeListener {
		void onVCCChange(TRModel mymodel);
	}

	public interface OnVBBChangeListener {
		void onVBBChange(TRModel mymodel);
	}

	public interface OnICChangeListener {
		void onICChange(TRModel mymodel);
	}
	
	public interface OnGraphViewSerialChangeListener{
		void onGraphViewSerialChange(TRModel mymodel);
	}	
	
	public interface OnGraphViewDataChangeListener{
		void onGraphViewDataChange(TRModel mymodel);
	}
	
	public interface OnProgressGraphUpdateChangeListener{
		void onProgressGraphUpdateChange(TRModel mymodel);
	}
	
	public interface OnPinFoundChangeListener{
		void onPinFoundChangeListener(TRModel mymodel);
	}

	public interface OnGraphFinnishChangeListener{
		void onGraphFinnishChangeListener(TRModel mymodel);
	}
	/*******************************************************************
	 * 
	 * 2.Call back register listener
	 * 
	 *******************************************************************/
	private OnVCEChangeListener onVCEChangeListener;
	private OnVCCChangeListener onVCCChangeListener;
	private OnVBBChangeListener onVBBChangeListener;
	private OnICChangeListener onICChangeListener;
	private OnGraphViewSerialChangeListener onGraphViewSerialChangeListener;
	private OnGraphViewDataChangeListener onGraphViewDataChangeListener;
	private OnProgressGraphUpdateChangeListener onProgressGraphUpdateChangeListener;
	private OnPinFoundChangeListener onPinFoundChangeListener;
	private OnGraphFinnishChangeListener onGraphFinnishChangeListener;
	/*******************************************************************
	 * 
	 * 3.Method Listener
	 * 
	 *******************************************************************/
	public void setOnVCEChangeListener(OnVCEChangeListener onVCEChangeListener) {
		this.onVCEChangeListener = onVCEChangeListener;
	}

	public void setOnVCCChangeListener(OnVCCChangeListener onVCCChangeListener) {
		this.onVCCChangeListener = onVCCChangeListener;
	}

	public void setOnVBBChangeListener(OnVBBChangeListener onVBBChangeListener) {
		this.onVBBChangeListener = onVBBChangeListener;
	}

	public void setOnICChangeListener(OnICChangeListener onICChangeListener) {
		this.onICChangeListener = onICChangeListener;
	}

	public void setOnGraphViewSerialChangeListener(OnGraphViewSerialChangeListener onGraphViewSerialChangeListener){
		this.onGraphViewSerialChangeListener = onGraphViewSerialChangeListener;
	}
	
	public void setOnGraphViewDataChangeListener(OnGraphViewDataChangeListener onGraphViewDataChangeListener){
		this.onGraphViewDataChangeListener = onGraphViewDataChangeListener;
	}
	
	public void setOnProgressGraphUpdateChangeListener(OnProgressGraphUpdateChangeListener onProgressGraphUpdateChangeListener){
		this.onProgressGraphUpdateChangeListener = onProgressGraphUpdateChangeListener;
	}
	
	public void setOnPinFoundChangeListener(OnPinFoundChangeListener onPinFoundChangeListener){
		this.onPinFoundChangeListener = onPinFoundChangeListener;
	}
	
	public void setOnGraphFinnishChangeListener(OnGraphFinnishChangeListener onGraphFinnishChangeListener){
		this.onGraphFinnishChangeListener = onGraphFinnishChangeListener;
	}
	/*******************************************************************
	 * 
	 * Method GETTER
	 * 
	 *******************************************************************/
	public float getIB() {
		return IB;
	}

	public float getIC() {
		return IC;
	}

	public float getVCE() {
		return VCE;
	}

	public float getVRB() {
		return VRB;
	}

	public float getVRC() {
		return VRC;
	}

	public float getVBB() {
		return VBB;
	}

	public float getVCC() {
		return VCC;
	}

	public float getVBE() {
		return VBE;
	}
	
	public GraphViewSeries getGraphViewSeries(){
		return graphSerial;
	}
	
	public Map<String, Object> getGraphViewData(){
		return mData;
	}

	public int getProgressGraphUpdate(){
		return progressNum;
	}
	
	public String getPin(){
		return pinName;
	}
	
	public boolean getGraphFinnish(){
		return GraphFinnish;
	}
	/*******************************************************************
	 * 
	 * Method SETTER
	 * 
	 *******************************************************************/
	public void setVRB(float vRB) {
		VRB = vRB;
	}

	public void setVRC(float vRC) {
		VRC = vRC;
	}

	public void setVBB(float vBB) {
		VBB = vBB;
		if (onVBBChangeListener != null) {
			this.onVBBChangeListener.onVBBChange(this);
		}
	}

	public void setVCC(float vCC) {
		VCC = vCC;
		if (onVCCChangeListener != null) {
			this.onVCCChangeListener.onVCCChange(this);
		}
	}

	public void setVBE(float vBE) {
		VBE = vBE;
	}

	public void setIB(float iB) {
		IB = iB;
	}

	public void setIC(float iC) {
		IC = iC;
		if (onICChangeListener != null) {
			this.onICChangeListener.onICChange(this);
		}
	}

	public void setVCE(float vCE) {
		VCE = vCE;
		if (onVCEChangeListener != null) {
			this.onVCEChangeListener.onVCEChange(this);
		}
	}
	
	public void setGraphViewSerial(GraphViewData[] data,String name){	
		//graphSerial = new GraphViewSeries(data);
		graphSerial = new GraphViewSeries(name, new GraphViewSeriesStyle(Color.rgb(200, 50, 00), 3), data);
		if(onGraphViewSerialChangeListener != null){
			this.onGraphViewSerialChangeListener.onGraphViewSerialChange(this);
		}
	}
	
	public void setGraphViewData(GraphViewData[] data,float Ib){
		graphData = data;
		mData = new HashMap<String, Object>();
		mData.put(con.mKey_IB, Ib);
		mData.put(con.mKey_GraphData, data);
		if(onGraphViewDataChangeListener != null){
			this.onGraphViewDataChangeListener.onGraphViewDataChange(this);
		}
	}
	
	public void setProgressGraphUpdate(int num){
		progressNum = num;
		if(onProgressGraphUpdateChangeListener != null){
			this.onProgressGraphUpdateChangeListener.onProgressGraphUpdateChange(this);
		}
	}
	
	public void setPinName(String pin){
		pinName = pin;
		if(onPinFoundChangeListener != null){
			this.onPinFoundChangeListener.onPinFoundChangeListener(this);
		}
	}
	
	public void setGraphFinnish(){
		GraphFinnish = true;
		if(onGraphFinnishChangeListener != null){
			this.onGraphFinnishChangeListener.onGraphFinnishChangeListener(this);
		}
	}
}
