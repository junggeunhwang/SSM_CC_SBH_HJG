package com.sapInterface.total;

public interface FileAction 
{
	void onError(String errorMsg, int errorCode);
    void onProgress(long progress);
    void onTransferComplete(String path);
    void onTransferRequested(int id, String path);
}
