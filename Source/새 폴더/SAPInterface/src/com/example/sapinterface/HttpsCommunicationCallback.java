package com.example.sapinterface;

public interface HttpsCommunicationCallback {
	void onResponseSuccess(HttpsCommunication hcn);
	void onResponseFailure(String errMsg);
}
