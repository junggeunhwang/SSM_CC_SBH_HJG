package com.ssm.cyclists.controller.communication.sapinterface;

public interface StringAction {
	void onError(int channelId, String errorString, int error);
	void onReceive(int channelId, byte[] data);
	void onServiceConnectionLost(int errorCode);
}