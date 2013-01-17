package com.clican.appletv.core.service.qiyi;

import com.clican.appletv.core.service.BaseClient;

public class QiYiClientImpl extends BaseClient implements QiYiClient {

	@Override
	public String getPlayURL(String code) {
		String url = springProperty.getQiyiVideoApi().replace("code", code);
		String htmlContent = this.httpGet(url, null, null);
		int end = htmlContent.indexOf("hd_qqvga.mp4");
		if (end == -1) {
			return null;
		}
		end = end + "hd_qqvga.mp4".length();
		int start = htmlContent.lastIndexOf("http", end);
		if (start == -1) {
			return null;
		}
		return htmlContent.substring(start, end);
	}

}
