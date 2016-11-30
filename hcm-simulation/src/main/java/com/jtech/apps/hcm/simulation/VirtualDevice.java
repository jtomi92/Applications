package com.jtech.apps.hcm.simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class VirtualDevice implements Runnable {

	private String serialNumber;
	private String host;
	private Integer port;
	private BufferedReader bufferedReader = null;
	private PrintWriter printWriter = null;
	private Socket socket = null;

	public VirtualDevice(String host, Integer port) {
		this.serialNumber = null;
		this.host = host;
		this.port = port;
	}

	public VirtualDevice(String serialNumber, String host, Integer port) {
		this.serialNumber = serialNumber;
		this.host = host;
		this.port = port;
	}

	public void run() {

		try {
			socket = new Socket(host, port);
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream());

			// GET TIME
			System.out.println(bufferedReader.readLine());

			// SEND / REQUEST SERIAL NUMBER
			if (serialNumber != null) {
				printWriter.write("#SERIAL_NUMBER;" + serialNumber + ";\n");
			} else {
				printWriter.write("#REQUEST_SERIAL_NUMBER\n");
			}
			printWriter.flush();

			// HEARTBEAT THREAD
			Thread thread = new Thread(new Runnable() {

				public void run() {
					while (!Thread.interrupted()) {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						printWriter.write("#CHECK\n");
						printWriter.flush();
					}
				}
			});
			thread.start();

			while (!Thread.interrupted()) {

				String readLine = bufferedReader.readLine();
				System.out.println(readLine);
				
				if (readLine.contains("[CFG]")) {
					printWriter.write("OK\n");
					printWriter.flush();
				}
				
				if (readLine.contains("SWITCHRELAY")) {
					String[] args = readLine.split(";");
					printWriter.write("#NOTIFICATION;SWITCH;"+args[2]+";"+args[3]+";\n");
					printWriter.flush();
				}
			}
			System.out.println("Closing VirtualDevice Thread");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
