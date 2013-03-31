var sokuClient = {
		
		loadSearchPage : function() {
			appletv.showInputTextPage('关键字', '搜索', youkuClient.loadKeywordsPage,
					'sokuClient.loadKeywordsPage', '');
		},

		loadKeywordsPage : function(q) {
			appletv.showLoading();
			var queryUrl = 'http://tip.tudou.soku.com/hint?q=' + q;
			appletv.makeRequest(queryUrl, function(result) {
				appletv.logToServer(result);
				var keywords = JSON.parse(result);
				var data = {
					keywords : keywords,
					serverurl : appletv.serverurl
				};
				var xml = new EJS({
					url : appletv.serverurl + '/template/soku/keywords.ejs'
				}).render(data);
				appletv.loadAndSwapXML(xml);
			});
		},
		
		loadIndexPage:function(keywrod,page){
			var url1 = "http://api.3g.youku.com/layout/phone2/ios/search/"+encodeURIComponent(keyword)+"?pg="+page+"&pid=69b81504767483cf&pz=30";
			var url2 = "http://api.3g.youku.com/videos/search/"+encodeURIComponent(keyword)+"?pg="+page+"&pid=69b81504767483cf&pz=30";
			var videos = [];
			appletv.makeRequest(url1, function(jsonContent1) {
				var results1 = JSON.parse(jsonContent1)['results'];
				for(var i=0;i<results1.length;i++){
					var result1 = results1[i];
					var video = {
							"title" : result1['showname'],
							"id" : result1['showid'],
							"pic" : result1['show_vthumburl'],
							"album": true
						};
					videos.push(video);
				}
				appletv.makeRequest(url2,function(jsonContent2){
					var results2 = JSON.parse(jsonContent2)['results'];
					for(var i=0;i<results2.length;i++){
						var result2 = results2[i];
						var video = {
								"title" : result2['showname'],
								"id" : result2['showid'],
								"pic" : result2['img'],
								"album": false
							};
						videos.push(video);
					}
				});
			}
		},
		
		generateIndexPage : function(keyword, page, videos) {
			var begin = 1;
			var end = 1;
			if (page < 92) {
				begin = page;
				end = page + 7;
			} else {
				end = 99;
				begin = 92;
			}
			var data = {
				'page' : page,
				'keyword' : keyword,
				'begin' : begin,
				'end' : end,
				'serverurl' : appletv.serverurl,
				'videos' : videos,
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/soku/index.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadVideoPage : function(id,album) {
			appletv.showLoading();
			var url;
			if(album){
				url = "http://api.3g.youku.com/layout/phone2/ios/searchdetail?pid=69b81504767483cf&id="+id;
			}else{
				url = "http://api.3g.youku.com/layout/phone2_1/detail?pid=69b81504767483cf&id="+id;
			}

			appletv.makeRequest(url, function(jsonContent) {
				detail = JSON.parse(jsonContent);
				var title;
				var detail;
				var actor;
				var dctor;
				var area;
				var score;
				var year;
				var desc;
				var script;
				var pic;
				var vcode;
				var sites = [];
				if(album){
					title = detail['title'];
					actor = JSON.stringify(detail['performer']);
					dctor = JSON.stringify(detail['director']);
					area = JSON.stringify(detail['area']);
					score = detail['reputation'];
					year = detail['showdate'];
					desc = detail['desc'];
					pic =  detail['img'];
					var siteItems = detail['site_items'];
					for(var i=0;i<siteItems.length;i++){
						var site = {"title":siteItems[i]['title'],"id":siteItems[i]["id"]};
						sites.push(site);
					}
				}else{
					title = detail['title'];
					desc = '-';
					pic =  detail['img'];
					vcode = detail['videoid'];
				}
				
				script = appletv.encode("sokuClient.loadVideoPage('"+id+"',"+album+",'');");
				var video = {
						'serverurl' : appletv.serverurl,
						album : album,
						id: id,
						script : script,
						video : {
							'id' : id,
							'actor' : actor,
							'area' : area,
							'dctor' : dctor,
							'pic' : pic,
							'score' : score,
							'title' : title,
							'year' : year,
							'desc' : desc,
							'vcode' : vcode,
						},
						sites : sites
					};
					var xml = new EJS({
						url : appletv.serverurl
								+ '/template/soku/video.ejs'
					}).render(video);
					appletv.loadAndSwapXML(xml);
			});
		},
		
		loadItemsPage : function(id,site) {
			appletv.showLoading();
			var url =  "http://api.3g.youku.com/layout/phone2/ios/searchdetail?pid=69b81504767483cf&id="+id+"&site="+site;
			appletv.makeRequest(url, function(jsonContent) {
				detail = JSON.parse(jsonContent);
				var title = detail['title'];
				var pic =  detail['img'];
				var items = [];
				
				var series = detail['series']['data'];
				for(var i=0;i<series.length;i++){
					var serie = series[i];
					var item = {"title":serie['title'],"vcode":serie['videoid'],"url":serie['url']};
					items.push(item);
				}
				var video = {
						'serverurl' : appletv.serverurl,
						video : {
							'id' : id,
							'pic' : pic,
							'title' : title,
						},
						items: items;
					};
				
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/souku/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		},
		openUrl : function(url){
			if(url.indexOf('56.com')!=-1){
				
			}else if(url.indexOf('tudou.com')!=-1){
				
			}else if(url.indexOf('sohu.com')!=-1){
				
			}else{
				appletv.showDialog('无法播放','');
			}
		},
		
		play : function(vcode){
			appletv.showLoading();
			var url = 'http://v.youku.com/player/getRealM3U8/vid/' + vcode + '/type/hd2/video.m3u8';
			appletv.playM3u8(url, '');
		},
}