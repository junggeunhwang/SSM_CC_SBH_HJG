package com.sapInterface.total;

public interface StringAction {
	void onError(int channelId, String errorString, int error);
	void onReceive(int channelId, byte[] data);
	void onServiceConnectionLost(int errorCode);
}
