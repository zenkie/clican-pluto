package com.peacebird.dataserver.bean;

import java.util.List;

public class ChannelStatResult {

	private List<ChannelResult> channel;
	
	private int result;
	
	private String message;

	public List<ChannelResult> getChannel() {
		return channel;
	}

	public void setChannel(List<ChannelResult> channel) {
		this.channel = channel;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
