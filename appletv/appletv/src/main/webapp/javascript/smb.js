var smbClient = {
		loadIndexPage:function(smbUrl){
			var url = appletv.serverurl+'/notcl/smb/resource';
			if(smbUrl!=null&&smbUrl.length>0){
				url = smbUrl;
			}
			appletv.makeRequest(url,function(result){
				var json = JSON.parse(result);
				var title = json['title'];
				if(title!=null){
					appletv.showDialog(json['title'],json['description']);
				}else{
					var data = {
							'items' : json['itmes'],
							'smbPath' : json['smbPath']
						};
					var xml = new EJS({
						url : appletv.serverurl + '/template/smb/index.ejs'
					}).render(data);
					appletv.loadAndSwapXML(xml);
				}
			});
		},
		
}