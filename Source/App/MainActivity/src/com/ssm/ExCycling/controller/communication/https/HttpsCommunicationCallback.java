package com.ssm.ExCycling.controller.communication.https;

public interface HttpsCommunicationCallback {
	void onResponseSuccess(HttpsCommunication hcn);
	void onResponseFailure(String errMsg);
}
