package com.fpt.edu.common.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

public class ImportHelper {
	protected final Logger LOGGER = LogManager.getLogger(getClass());

	private JSONArray rawData;
	private boolean isEndOfInputData;
	private Queue<JSONObject> queue;
	public ImportHelper(JSONArray rawData) {
		this.rawData = rawData;
		this.isEndOfInputData = false;
		this.queue= new LinkedList<>();
	}

	public boolean startImport() {
		Thread getDataThread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i <rawData.length(); i++) {
					queue.add(rawData.getJSONObject(i));
				}
				isEndOfInputData=true;

			}
		});

		Thread insertDB = new Thread(new Runnable() {
			@Override
			public void run() {
					while (!queue.isEmpty() && !isEndOfInputData){
						JSONObject current= queue.poll();

						LOGGER.info(current.toString());



					}
			}
		});

		getDataThread.start();
		insertDB.start();


		return false;
	}


}
