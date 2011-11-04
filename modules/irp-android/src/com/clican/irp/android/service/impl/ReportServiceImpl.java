package com.clican.irp.android.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.clican.irp.android.enumeration.ApplicationUrl;
import com.clican.irp.android.enumeration.ReportScope;
import com.clican.irp.android.http.HttpGateway;
import com.clican.irp.android.service.ReportService;
import com.clican.irp.android.util.JSONUtil;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ReportServiceImpl implements ReportService {

	@Inject
	private HttpGateway httpGateway;

	@Inject
	private Context context;

	public List<Map<String, Object>> queryReport(String query,
			ReportScope scope, Date start, Date end, int page, int pageSize) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String url = ApplicationUrl.QUERY_REPORT.getUrl() + "?";
		if (query != null && query.trim().length() > 0) {
			url += "query=" + query + "&";
		}
		if (scope != null) {
			url += "scope=" + scope.getScope() + "&";
		}
		if (start != null) {
			url += "startDate=" + sdf.format(start) + "&";
		}
		if (end != null) {
			url += "endDate=" + sdf.format(end) + "&";
		}
		url += "page=" + page + "&";
		url += "pageSize=" + pageSize;
		try {
			JSONObject result = httpGateway.invokeBySession(url);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			JSONArray array = result.getJSONArray("reports");
			for (int i = 0; i < array.length(); i++) {
				list.add(JSONUtil.convertJSON2Map(array.getJSONObject(i)));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, Object> readReport(Long reportId) {
		String url = ApplicationUrl.READ_REPORT.getUrl() + "?reportId="
				+ reportId;
		try {
			JSONObject jsonObject = httpGateway.invokeBySession(url);
			if (jsonObject == null) {
				return null;
			}
			Map<String, Object> report = JSONUtil.convertJSON2Map(jsonObject
					.getJSONObject("report"));
			return report;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public File downloadAttachement(Long reportId) {
		String url = ApplicationUrl.DOWNLOAD_REPORT.getUrl() + "?reportId="
				+ reportId;
		FileOutputStream fos = null;
		try {
			byte[] data = httpGateway.downloadConentBySession(url);
			if (data == null || data.length == 0) {
				return null;
			}
			
			fos = context.openFileOutput(reportId.toString()+".pdf",
					Context.MODE_WORLD_READABLE);
			fos.write(data);
			fos.flush();
			return  context.getFileStreamPath(reportId.toString()+".pdf");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
