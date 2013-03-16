var youkuClient = {
	youkuChannelMap : {
		"1001" : {
			label : "搜索",
			value : 1001
		},
		"97" : {
			label : "电视剧",
			value : 97,
			album : true
		},
		"96" : {
			label : "电影",
			value : 96,
			album : true
		},
		"85" : {
			label : "综艺",
			value : 85,
			album : true
		},
		"100" : {
			label : "动漫",
			value : 100,
			album : true
		},

		"87" : {
			label : "教育",
			value : 87,
			album : true
		},
		"84" : {
			label : "纪录片",
			value : 84,
			album : true
		},
		"91" : {
			label : "资讯",
			value : 91,
			album : false
		},
		"86" : {
			label : "娱乐",
			value : 86,
			album : false
		},
		"92" : {
			label : "原创",
			value : 92,
			album : false
		},
		"98" : {
			label : "体育",
			value : 98,
			album : false
		},
		"104" : {
			label : "汽车",
			value : 104,
			album : false
		},
		"105" : {
			label : "科技",
			value : 105,
			album : false
		},
		"99" : {
			label : "游戏",
			value : 99,
			album : false
		},
		"103" : {
			label : "生活",
			value : 103,
			album : false
		},
		"89" : {
			label : "时尚",
			value : 89,
			album : false
		},
		"88" : {
			label : "旅游",
			value : 88,
			album : false
		},
		"90" : {
			label : "母婴",
			value : 90,
			album : false
		},
		"94" : {
			label : "搞笑",
			value : 94,
			album : false
		},
		"102" : {
			label : "广告",
			value : 102,
			album : false
		}
	},

	youkuChannels : [ {
		label : "搜索",
		value : 1001
	}, {
		label : "电视剧",
		value : 97,
		album : true
	}, {
		label : "电影",
		value : 96,
		album : true
	}, {
		label : "综艺",
		value : 85,
		album : true
	}, {
		label : "动漫",
		value : 100,
		album : true
	}, {
		label : "教育",
		value : 87,
		album : true
	}, {
		label : "纪录片",
		value : 84,
		album : true
	}, {
		label : "资讯",
		value : 91,
		album : false
	}, {
		label : "娱乐",
		value : 86,
		album : false
	}, {
		label : "原创",
		value : 92,
		album : false
	}, {
		label : "体育",
		value : 98,
		album : false
	}, {
		label : "汽车",
		value : 104,
		album : false
	}, {
		label : "科技",
		value : 105,
		album : false
	}, {
		label : "游戏",
		value : 99,
		album : false
	}, {
		label : "生活",
		value : 103,
		album : false
	}, {
		label : "时尚",
		value : 89,
		album : false
	}, {
		label : "旅游",
		value : 88,
		album : false
	}, {
		label : "母婴",
		value : 90,
		album : false
	}, {
		label : "搞笑",
		value : 94,
		album : false
	}, {
		label : "广告",
		value : 102,
		album : false
	} ],
	
	loadIndexPage : function(keyword, page, channelId) {
		appletv.showLoading();
		var channel = this.youkuChannelMap[channelId];
		var videos = [];
		var queryUrl;
		if (channelId == 1001) {
			
		} else {
			if(channel.album){
				queryUrl = 'http://www.youku.com/v_olist/c_'+channelId+'_a__s__g__r__lg__im__st__mt__tg__d_1_et_0_fv_0_fl__fc__fe__o_7_p_'+page+'.html';
			}else{
				queryUrl = 'http://www.youku.com/v_showlist/t1c'+channelId+'g0d4p'+page+'.html'
			}
			appletv.makeRequest(queryUrl, function(content) {
				if (content != null && content.length > 0) {
					var itemscontent = appletv.substringByTag(content,'<div class="items">', '</div>', 'div');
					var items = appletv.getSubValuesByTag(itemscontent,
							'<ul class="p pv">', '</ul>', 'ul');
					for (i = 0; i < items.length; i++) {
						var item = items[i];
						var pic = appletv.substring(item,
								'<img src="', '"');
						var title = appletv.substring(item, 'title="', '"');
						var id = appletv.substring(item, '<a href="', '"');
						id = appletv.substring(id, 'id_', '.html');
						var video = {
							"title" : title,
							"id" : id,
							"pic" : pic,
							"album" : channel.album
						};
						videos.push(video);
					}
					youkuClient.generateIndexPage(keyword, page, channel,
							videos);
				} else {
					atv.loadXML(appletv.makeDialog('加载失败', ''));
				}
			});
		
		}

	},

	generateIndexPage : function(keyword, page, channel, videos) {
		var begin = 1;
		var end = 1;
		if (page < 90) {
			begin = page;
			end = page + 9;
		} else {
			end = 99;
			begin = 90;
		}
		var data = {
			'channel' : channel,
			'keyword' : keyword,
			'begin' : begin,
			'end' : end,
			'channels' : youkuClient.youkuChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/youku/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadVideoPage : function(code, channelId, isalbum,pic) {
		appletv.showLoading();
		var url;
		if(isalbum){
			url = 'http://www.youku.com/show_page/id_'+code+'.html';
		}else{
			url = 'http://v.youku.com/v_show/id_'+code+'.html';
		}
		appletv.makeRequest(url, function(htmlContent) {
			if (htmlContent == null) {
				return;
			}
			var actor = '-';
			var dctor = '-';
			var area = '-';
			var score = '-';
			var year = '-';
			var shareurl = url;
			var desc;
			if(isalbum){
				pic = appletv.substring(htmlContent,'<li class="thumb">','</li>');
				pic = appletv.substring(pic,'src=\'','\'');
				title = appletv.substring(htmlContent,'<span class="name">','</span>');
				area = appletv.substring(htmlContent,'<span class="area">','</span>');
				area = appletv.getSubValues(area,'target="_blank">', '</a>');
				year = appletv.substring(htmlContent,'<span class="pub">','</span>');
				score = appletv.substring(htmlContent,'<em class="num">','</em>');
				if(channelId==97){
					//电视剧
					actor = appletv.substring(htmlContent,'<span class="actor">','</span>');
					actor = appletv.getSubValues(actor,'target="_blank">', '</a>');
					desc = appletv.substring(htmlContent,'<span class="short" id="show_info_short" style="display: inline;">','</span>');
				}else if(channelId==96){
					//电影
					actor = appletv.substring(htmlContent,'<span class="actor">','</span>');
					actor = appletv.getSubValues(actor,'target="_blank">', '</a>');
					dctor = appletv.substring(htmlContent,'<span class="director">','</span>');
					dctor = appletv.getSubValues(dctor,'target="_blank">', '</a>');
					desc = appletv.substring(htmlContent,'<span class="long" style="display:none;">','</span>');
				}
			}else{
				title =  appletv.substring(htmlContent,'<meta name="title" content="','"');
				desc =  appletv.substring(htmlContent,'<meta name="description" content="','"');
			}
			if(channelId==96){
				isalbum = false;
				code = appletv.substring(htmlContent,'id_','.html');
			}
			var items = [];
			if(isalbum){
				var itemscontent = appletv.substringByTag(htmlContent,'<div class="items"','</div>','div');
				var urls = appletv.getSubValues(itemscontent,'<a','</a>');
				for(i=0;i<urls.length;i++){
					url = urls[i];
					var t = appletv.substring(url,'title="','"');
					var c = appletv.substring(url,'id_','.html');
					var item = {
							'title' : t,
							'id' : c
						};
					items.push(item);
				}
			}else{
				var item = {
						'title' : title,
						'id' : code
					};
				items.push(item);
			}
			
			
			var video = {
					'serverurl' : appletv.serverurl,
					album : isalbum,
					video : {
						'id' : code,
						'actor' : actor,
						'area' : area,
						'dctor' : dctor,
						'pic' : pic,
						'score' : score,
						'title' : title,
						'year' : year,
						'desc' : desc,
						'shareurl':shareurl
					},
					items : items
				};
				if(isalbum){
					appletv.setValue('youkuVideo',video);
				}
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/youku/video.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
		});
	},
	
	loadItemsPage : function() {
		appletv.showLoading();
		appletv.getValue('youkuVideo',function(video){
			var xml = new EJS({
				url : appletv.serverurl
						+ '/template/youku/videoItems.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	loadSearchPage : function() {
		appletv.showInputTextPage('关键字', '搜索', tudouClient.loadKeywordsPage,
				'youkuClient.loadKeywordsPage', '');
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
				url : appletv.serverurl + '/template/youku/keywords.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	play : function(vcode) {
		var url = 'http://v.youku.com/player/getRealM3U8/vid/' + vcode + '/type/flv/sc/2/video.m3u8';
		appletv.playM3u8(url, '');
	},

}