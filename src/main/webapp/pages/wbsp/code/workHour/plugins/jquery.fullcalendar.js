/**
 * calendar - jQuery EasyUI
 *
 * Copyright (c) 2009-2013 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the GPL or commercial licenses
 * To use it on other terms please contact us: jeasyui@gmail.com
 * http://www.gnu.org/licenses/gpl.txt
 * http://www.jeasyui.com/license_commercial.php
 * 二次开发 ____′↘夏悸
 * http://bbs.btboys.com Easyui中文社区
 *
 * 2017-12-19 中国瑞林 信息中心 www.nerin.com
 * 本地化开发
 * 张万川 zhangwanchuan@nerin.com
 */
(function ($) {
	
	/*****************************************************************************
	日期资料
	 *****************************************************************************/
	var detail;
	var hideTimer;
	var Today = new Date();
	var tY = Today.getFullYear();
	var tM = Today.getMonth();
	var tD = Today.getDate();
	
	var tInfo = [
			0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
			0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
			0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
			0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
			0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0,
			0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
			0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
			0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
			0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
			0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
			0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
			0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
			0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0,
			0x14b63];
	
	var solarMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
	var Gan = ["甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"];
	var Zhi = ["子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"];
	var Animals = ["鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"];
	var solarTerm = [
        "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨",
        "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑",
        "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"];
	var sTermInfo = [
        0, 21208, 42467, 63836, 85337, 107014, 128867, 150921,
        173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033,
        353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758];
	var nStr1 = ['日', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十'];
	var nStr2 = ['初', '十', '廿', '卅', '□'];
	var monthName = ["正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊"];
	
	//国历节日 *表示放假日
	var sFtv = [
        "0101*元旦节",
        "0308 妇女节",
        "0312 植树节 孙中山逝世纪念日",
        "0315 消费者权益日",
        "0501*劳动节",
        "0504 青年节",
        "0531 世界无烟日",
        "0701 香港回归纪念日 中共诞辰",
        "0707 抗日战争纪念日",
        "0801 建军节",
        "0815 抗日战争胜利纪念",
        "0909 毛泽东逝世纪念",
        "0910 中国教师节",
        "0918 九·一八事变纪念日",
        "1001*国庆节",
        "1002*国庆节假日",
        "1003*国庆节假日",
        "1010 辛亥革命纪念日 世界精神卫生日",
        "1014 世界标准日",
        "1107 十月社会主义革命纪念日",
        "1109 全国消防安全宣传教育日",
        "1112 孙中山诞辰纪念日",
        "1201 世界艾滋病日",
        "1212 西安事变纪念日",
        "1213 南京大屠杀(1937年)纪念日！谨记血泪史！",
        "1220 澳门回归纪念",
        "1226 毛泽东诞辰纪念"];

    //农历节日 *表示放假日
    var lFtv = [
        "0101*春节",
        "0102*初二",
        "0115 元宵节",
        "0505*端午节",
        "0707 七夕节",
        "0715 中元节",
        "0815*中秋节",
        "0909 重阳节",
        "1208 腊八节",
        "1223 小年",
        "0100*除夕"];

    //某月的第几个星期几
    //一月的最后一个星期日（月倒数第一个星期日）
    var wFtv = [
        "0150 世界麻风日",
        "0520 国际母亲节",
        "0630 父亲节",
        "1144 感恩节"];

    /*************************日期计算********************************************/
    //====================================== 返回农历 y年的总天数
	function lYearDays(y) {
		var i,
		sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1)
			sum += (tInfo[y - 1900] & i) ? 1 : 0;
		return (sum + leapDays(y));
	}
	
	//====================================== 返回农历 y年闰月的天数
	function leapDays(y) {
		if (leapMonth(y))
			return ((tInfo[y - 1900] & 0x10000) ? 30 : 29);
		else
			return (0);
	}
	
	//====================================== 返回农历 y年闰哪个月 1-12 , 没闰返回 0
	function leapMonth(y) {
		return (tInfo[y - 1900] & 0xf);
	}
	
	//====================================== 返回农历 y年m月的总天数
	function monthDays(y, m) {
		return ((tInfo[y - 1900] & (0x10000 >> m)) ? 30 : 29);
	}
	
	//====================================== 算出农历, 传入日期控件, 返回农历日期控件
	//                                       该控件属性有 .year .month .day .isLeap
	function Lunar(objDate) {
		
		var i, leap, temp;
		var offset = (Date.UTC(objDate.getFullYear(), objDate.getMonth(), objDate.getDate()) - Date.UTC(1900, 0, 31)) / 86400000;
		
		for (i = 1900; i < 2050 && offset > 0; i++) {
			temp = lYearDays(i);
			offset -= temp;
		}
		
		if (offset < 0) {
			offset += temp;
			i--;
		}
		
		this.year = i;
		
		leap = leapMonth(i); //闰哪个月
		this.isLeap = false;
		
		for (i = 1; i < 13 && offset > 0; i++) {
			//闰月
			if (leap > 0 && i == (leap + 1) && this.isLeap == false) {
				--i;
				this.isLeap = true;
				temp = leapDays(this.year);
			} else {
				temp = monthDays(this.year, i);
			}
			
			//解除闰月
			if (this.isLeap == true && i == (leap + 1))
				this.isLeap = false;
			
			offset -= temp;
		}
		
		if (offset == 0 && leap > 0 && i == leap + 1)
			if (this.isLeap) {
				this.isLeap = false;
			} else {
				this.isLeap = true;
				--i;
			}
		
		if (offset < 0) {
			offset += temp;
			--i;
		}
		
		this.month = i;
		this.day = offset + 1;
	}
	
	//==============================返回公历 y年某m+1月的天数
	function solarDays(y, m) {
		if (m == 1)
			return (((y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0)) ? 29 : 28);
		else
			return (solarMonth[m]);
	}
	//============================== 传入 offset 返回干支, 0=甲子
	function cyclical(num) {
		return (Gan[num % 10] + Zhi[num % 12]);
	}
	
	//============================== 阴历属性
	function CalElement(sYear, sMonth, sDay, week, lYear, lMonth, lDay, isLeap, cYear, cMonth, cDay) {
		
		this.isToday = false;
		//瓣句
		this.sYear = sYear; //公元年4位数字
		this.sMonth = sMonth; //公元月数字
		this.sDay = sDay; //公元日数字
		this.week = week; //星期, 1个中文
		//农历
		this.lYear = lYear; //公元年4位数字
		this.lMonth = lMonth; //农历月数字
		this.lDay = lDay; //农历日数字
		this.isLeap = isLeap; //是否为农历闰月?
		//八字
		this.cYear = cYear; //年柱, 2个中文
		this.cMonth = cMonth; //月柱, 2个中文
		this.cDay = cDay; //日柱, 2个中文
		
		this.color = '';
		
		this.lunarFestival = ''; //农历节日
		this.solarFestival = ''; //公历节日
		this.solarTerms = ''; //节气
	}
	
	//===== 某年的第n个节气为几日(从0小寒起算)
	function sTerm(y, n) {
		if (y == 2009 && n == 2) {
			sTermInfo[n] = 43467
		}
		var offDate = new Date((31556925974.7 * (y - 1900) + sTermInfo[n] * 60000) + Date.UTC(1900, 0, 6, 2, 5));
		return (offDate.getUTCDate());
	}
	
	//============================== 返回阴历控件 (y年,m+1月)
	/*
	功能说明: 返回整个月的日期资料控件
	
	使用方式: OBJ = new calendar(年,零起算月);
	
	OBJ.length      返回当月最大日
	OBJ.firstWeek   返回当月一日星期
	
	由 OBJ[日期].属性名称 即可取得各项值
	
	OBJ[日期].isToday  返回是否为今日 true 或 false
	
	其他 OBJ[日期] 属性参见 CalElement() 中的注解
	 */
	function Calendar(y, m) {
		
		var sDObj,
		lDObj,
		lY,
		lM,
		lD = 1,
		lL,
		lX = 0,
		tmp1,
		tmp2,
		tmp3;
		var cY, cM, cD; //年柱,月柱,日柱
		var lDPOS = new Array(3);
		var n = 0;
		var firstLM = 0;
		
		sDObj = new Date(y, m, 1, 0, 0, 0, 0); //当月一日日期
		
		this.length = solarDays(y, m); //公历当月天数
		this.firstWeek = sDObj.getDay(); //公历当月1日星期几
		
		////////年柱 1900年立春后为庚子年(60进制36)
		if (m < 2)
			cY = cyclical(y - 1900 + 36 - 1);
		else
			cY = cyclical(y - 1900 + 36);
		
		var term2 = sTerm(y, 2); //立春日期
		
		////////月柱 1900年1月小寒以前为 丙子月(60进制12)
		var firstNode = sTerm(y, m * 2); //返回当月「节」为几日开始
        cM = cyclical((y - 1900) * 12 + m + 12);
		
		//当月一日与 1900/1/1 相差天数
		//1900/1/1与 1970/1/1 相差25567日, 1900/1/1 日柱为甲戌日(60进制10)
		var dayCyclical = Date.UTC(y, m, 1, 0, 0, 0, 0) / 86400000 + 25567 + 10;
		
		for (var i = 0; i < this.length; i++) {
			
			if (lD > lX) {
				sDObj = new Date(y, m, i + 1); //当月一日日期
				lDObj = new Lunar(sDObj); //农历
				lY = lDObj.year; //农历年
				lM = lDObj.month; //农历月
				lD = lDObj.day; //农历日
				lL = lDObj.isLeap; //农历是否闰月
				lX = lL ? leapDays(lY) : monthDays(lY, lM); //农历当月最后一天
				
				if (n == 0)
					firstLM = lM;
				lDPOS[n++] = i - lD + 1;
			}
			
			//依节气调整二月分的年柱, 以立春为界
			/*
			//PM提出线上2月3日，初一不是庚寅年，应该是辛卯年。
			by yuji
			
			这里firstNode是指农历每月的节气所在的日期，用这个标志判断
			农历每月起始日天干地支是错误的，应当用每月的农历初一所在日确定
			下月的天干地支。农历每月初一都要重新计算一下天干地支。
			 */
			if (m == 1 && ((i + 1) == term2 || lD == 1))
				cY = cyclical(y - 1900 + 36);
			
			//依节气月柱, 以「节」为界
			//if((i+1)==firstNode) cM = cyclical((y-1900)*12+m+13);
			
			/*
			by yuji
			
			这里firstNode是指农历每月的节气所在的日期，用这个标志判断
			农历每月起始日天干地支是错误的，应当用每月的农历初一所在日确定
			下月的天干地支。农历每月初一都要重新计算一下天干地支。
			 */
			if (lD == 1) {
				cM = cyclical((y - 1900) * 12 + m + 13);
				
			}
			
			//日柱
			cD = cyclical(dayCyclical + i);
			
			//sYear,sMonth,sDay,week,
			//lYear,lMonth,lDay,isLeap,
			//cYear,cMonth,cDay
			this[i] = new CalElement(y, m + 1, i + 1, (i + this.firstWeek) % 7,
					lY, lM, lD++, lL,
					cY, cM, cD);
		}
		
		//节气
		tmp1 = sTerm(y, m * 2) - 1;
		tmp2 = sTerm(y, m * 2 + 1) - 1;
		this[tmp1].solarTerms = solarTerm[m * 2];
		this[tmp2].solarTerms = solarTerm[m * 2 + 1];
		//guohao
		if (y == 2009 && m == 1) {
			if (tD == 3) {
				this[tmp1].solarTerms = '';
                //this[tmp2].solarTerms = '';
			} else if (tD == 4) {
				this[tmp1].solarTerms = '立春';
                //this[tmp2].solarTerms = '';
			}
		}
		if (m == 3)
			this[tmp1].color = 'red'; //清明颜色
		
		//公历节日
		for (i in sFtv)
			if (sFtv[i].match(/^(\d{2})(\d{2})([\s\*])(.+)$/))
				if (Number(RegExp.$1) == (m + 1)) {
					this[Number(RegExp.$2) - 1].solarFestival += RegExp.$4 + ' ';
					if (RegExp.$3 == '*')
						this[Number(RegExp.$2) - 1].color = 'red';
				}
		
		//月周节日
		for (i in wFtv)
			if (wFtv[i].match(/^(\d{2})(\d)(\d)([\s\*])(.+)$/))
				if (Number(RegExp.$1) == (m + 1)) {
					tmp1 = Number(RegExp.$2);
					tmp2 = Number(RegExp.$3);
					if (tmp1 < 5)
						this[((this.firstWeek > tmp2) ? 7 : 0) + 7 * (tmp1 - 1) + tmp2 - this.firstWeek].solarFestival += RegExp.$5 + ' ';
					else {
						tmp1 -= 5;
						tmp3 = (this.firstWeek + this.length - 1) % 7; //当月最后一天星期?
						this[this.length - tmp3 - 7 * tmp1 + tmp2 - (tmp2 > tmp3 ? 7 : 0) - 1].solarFestival += RegExp.$5 + ' ';
					}
				}
		
		//农历节日
		for (i in lFtv)
			if (lFtv[i].match(/^(\d{2})(.{2})([\s\*])(.+)$/)) {
				tmp1 = Number(RegExp.$1) - firstLM;
				if (tmp1 == -11)
					tmp1 = 1;
				if (tmp1 >= 0 && tmp1 < n) {
					tmp2 = lDPOS[tmp1] + Number(RegExp.$2) - 1;
					if (tmp2 >= 0 && tmp2 < this.length && this[tmp2].isLeap != true) {
						this[tmp2].lunarFestival += RegExp.$4 + ' ';
						if (RegExp.$3 == '*')
							this[tmp2].color = 'red';
					}
				}
			}

		//今日
		if (y == tY && m == tM)
			this[tD - 1].isToday = true;
	}
	
	//====================== 中文日期
	function cDay(d, m,dt) {
		var s;
		switch (d) {
		case 1:
			s = monthName[m - 1] + '月';
			if(dt){
				s = '初一';
			}
			break;
		case 10:
			s = '初十';
			break;
		case 20:
			s = '二十';
			break;
		case 30:
			s = '三十';
			break;
		default:
			s = nStr2[Math.floor(d / 10)];
			s += nStr1[d % 10];
		}
		return (s);
	}
	
	function setSize(target) {
		var opts = $.data(target, 'fullCalendar').options;
		var t = $(target);
		if (opts.fit == true) {
			var p = t.parent();
			opts.width = p.width();
			opts.height = p.height();
		}
		var header = t.find('.calendar-header');
		t._outerWidth(opts.width);
		t._outerHeight(opts.height);
		t.find('.calendar-body')._outerHeight(t.height() - header._outerHeight());
	}
	
	function init(target) {
		$(target).addClass('calendar').wrapInner(
			'<div class="calendar-header">' +
			'<div class="calendar-prevmonth"></div>' +
			'<div class="calendar-nextmonth"></div>' +
			'<div class="calendar-prevyear"></div>' +
			'<div class="calendar-nextyear"></div>' +
			'<div class="calendar-title">' +
			'<span>Year Month</span>' +
			'</div>' +
			'</div>' +
			'<div class="calendar-body fullcalendar-body">' +
			'<div class="calendar-menu">' +
			'<div class="calendar-menu-year-inner">' +
			'<span class="calendar-menu-prev"></span>' +
			'<span><input class="calendar-menu-year" type="text"/></span>' +
			'<span class="calendar-menu-next"></span>' +
			'</div>' +
			'<div class="calendar-menu-month-inner">' +
			'</div>' +
			'</div>' +
			'</div>');
		detail = $('div.fullcalendar-detail');
		if (!detail.length) {
			detail = $('<div class="fullcalendar-detail"/>').appendTo('body');
			detail.hover(function () {
				clearTimeout(hideTimer);
			}, function () {
				$(this).hide();
			});
		}
		
		$(target).find('.calendar-title span')
			.hover(function () {
				$(this).addClass('calendar-menu-hover');
			}, function () {
				$(this).removeClass('calendar-menu-hover');
			})
			.click(function () {
				var menu = $(target).find('.calendar-menu');
				if (menu.is(':visible')) {
					menu.hide();
				} else {
					showSelectMenus(target);
				}
			});
		
		$('.calendar-prevmonth,.calendar-nextmonth,.calendar-prevyear,.calendar-nextyear', target)
			.hover(function () {
				$(this).addClass('calendar-nav-hover');
			}, function () {
				$(this).removeClass('calendar-nav-hover');
			});
		$(target).find('.calendar-nextmonth').click(function () {
			showMonth(target, 1);
		});
		$(target).find('.calendar-prevmonth').click(function () {
			showMonth(target, -1);
		});
		$(target).find('.calendar-nextyear').click(function () {
			showYear(target, 1);
		});
		$(target).find('.calendar-prevyear').click(function () {
			showYear(target, -1);
		});
		
		$(target).bind('_resize', function () {
			var opts = $.data(target, 'fullCalendar').options;
			if (opts.fit == true) {
				setSize(target);
			}
			return false;
		});
	}
	
	/**
	 * show the calendar corresponding to the current month.
	 */
	function showMonth(target, delta) {
		var opts = $.data(target, 'fullCalendar').options;
		opts.month += delta;
		if (opts.month > 12) {
			opts.year++;
			opts.month = 1;
		} else if (opts.month < 1) {
			opts.year--;
			opts.month = 12;
		}
		show(target);
		
		var menu = $(target).find('.calendar-menu-month-inner');
		menu.find('td.calendar-selected').removeClass('calendar-selected');
		menu.find('td:eq(' + (opts.month - 1) + ')').addClass('calendar-selected');
	}
	
	/**
	 * show the calendar corresponding to the current year.
	 */
	function showYear(target, delta) {
		var opts = $.data(target, 'fullCalendar').options;
		opts.year += delta;
		show(target);
		
		var menu = $(target).find('.calendar-menu-year');
		menu.val(opts.year);
	}
	
	/**
	 * show the select menu that can change year or month, if the menu is not be created then create it.
	 */
	function showSelectMenus(target) {
		var opts = $.data(target, 'fullCalendar').options;
		$(target).find('.calendar-menu').show();
		
		if ($(target).find('.calendar-menu-month-inner').is(':empty')) {
			$(target).find('.calendar-menu-month-inner').empty();
			var t = $('<table></table>').appendTo($(target).find('.calendar-menu-month-inner'));
			var idx = 0;
			for (var i = 0; i < 3; i++) {
				var tr = $('<tr></tr>').appendTo(t);
				for (var j = 0; j < 4; j++) {
					$('<td class="calendar-menu-month"></td>').html(opts.months[idx++]).attr('abbr', idx).appendTo(tr);
				}
			}
			
			$(target).find('.calendar-menu-prev,.calendar-menu-next').hover(
				function () {
				$(this).addClass('calendar-menu-hover');
			},
				function () {
				$(this).removeClass('calendar-menu-hover');
			});
			$(target).find('.calendar-menu-next').click(function () {
				var y = $(target).find('.calendar-menu-year');
				if (!isNaN(y.val())) {
					y.val(parseInt(y.val()) + 1);
				}
			});
			$(target).find('.calendar-menu-prev').click(function () {
				var y = $(target).find('.calendar-menu-year');
				if (!isNaN(y.val())) {
					y.val(parseInt(y.val() - 1));
				}
			});
			
			$(target).find('.calendar-menu-year').keypress(function (e) {
				if (e.keyCode == 13) {
					setDate();
				}
			});
			
			$(target).find('.calendar-menu-month').hover(
				function () {
				$(this).addClass('calendar-menu-hover');
			},
				function () {
				$(this).removeClass('calendar-menu-hover');
			}).click(function () {
				var menu = $(target).find('.calendar-menu');
				menu.find('.calendar-selected').removeClass('calendar-selected');
				$(this).addClass('calendar-selected');
				setDate();
			});
		}
		
		function setDate() {
			var menu = $(target).find('.calendar-menu');
			var year = menu.find('.calendar-menu-year').val();
			var month = menu.find('.calendar-selected').attr('abbr');
			if (!isNaN(year)) {
				opts.year = parseInt(year);
				opts.month = parseInt(month);
				show(target);
			}
			menu.hide();
		}
		
		var body = $(target).find('.calendar-body');
		var sele = $(target).find('.calendar-menu');
		var seleYear = sele.find('.calendar-menu-year-inner');
		var seleMonth = sele.find('.calendar-menu-month-inner');
		
		seleYear.find('input').val(opts.year).focus();
		seleMonth.find('td.calendar-selected').removeClass('calendar-selected');
		seleMonth.find('td:eq(' + (opts.month - 1) + ')').addClass('calendar-selected');
		
		sele._outerWidth(body._outerWidth());
		sele._outerHeight(body._outerHeight());
		seleMonth._outerHeight(sele.height() - seleYear._outerHeight());
	}
	
	/**
	 * get weeks data.
	 */
	function getWeeks(target, year, month) {
		var opts = $.data(target, 'fullCalendar').options;
		var dates = [];
		var lastDay = new Date(year, month, 0).getDate();
        var i;
		for (i = 1; i <= lastDay; i++)
			dates.push([year, month, i]);
		
		// group date by week
		var weeks = [],
		week = [];
		// var memoDay = 0;
		var memoDay = -1;
		var date, firstDate, lastDate;
		while (dates.length > 0) {
			date = dates.shift();
			week.push(date);
			var day = new Date(date[0], date[1] - 1, date[2]).getDay();
			if (memoDay == day) {
				day = 0;
			} else if (day == (opts.firstDay == 0 ? 7 : opts.firstDay) - 1) {
				weeks.push(week);
				week = [];
			}
			memoDay = day;
		}
		if (week.length) {
			weeks.push(week);
		}
		
		var firstWeek = weeks[0];
		if (firstWeek.length < 7) {
			while (firstWeek.length < 7) {
				firstDate = firstWeek[0];
				date = new Date(firstDate[0], firstDate[1] - 1, firstDate[2] - 1);
				firstWeek.unshift([date.getFullYear(), date.getMonth() + 1, date.getDate()]);
			}
		} else {
			firstDate = firstWeek[0];
			week = [];
			for (i = 1; i <= 7; i++) {
				date = new Date(firstDate[0], firstDate[1] - 1, firstDate[2] - i);
				week.unshift([date.getFullYear(), date.getMonth() + 1, date.getDate()]);
			}
			weeks.unshift(week);
		}
		
		var lastWeek = weeks[weeks.length - 1];
		while (lastWeek.length < 7) {
			lastDate = lastWeek[lastWeek.length - 1];
			date = new Date(lastDate[0], lastDate[1] - 1, lastDate[2] + 1);
			lastWeek.push([date.getFullYear(), date.getMonth() + 1, date.getDate()]);
		}
		if (weeks.length < 6) {
			lastDate = lastWeek[lastWeek.length - 1];
			week = [];
			for (i = 1; i <= 7; i++) {
				date = new Date(lastDate[0], lastDate[1] - 1, lastDate[2] + i);
				week.push([date.getFullYear(), date.getMonth() + 1, date.getDate()]);
			}
			weeks.push(week);
		}

		return weeks;
	}

	/**
	 * chack out if date between start and end
	 * @param date
	 * @param start
     * @param end
     */
	function betweenDate(date, start, end) {
		function compare(A, B){
			A = A.split(",");
            B = B.split(",");
            if (parseInt(A[0]) > parseInt(B[0]))
                return 1;
            if (parseInt(A[0]) < parseInt(B[0]))
                return -1;
            if (parseInt(A[0]) == parseInt(B[0])) {
                if (parseInt(A[1]) > parseInt(B[1]))
                    return 1;
                if (parseInt(A[1]) < parseInt(B[1]))
                    return -1;
                if (parseInt(A[1]) == parseInt(B[1])) {
                    if (parseInt(A[2]) > parseInt(B[2]))
                        return 1;
                    if (parseInt(A[2]) < parseInt(B[2]))
                        return -1;
                    if (parseInt(A[2]) == parseInt(B[2]))
                        return 0;
                }
            }
		}
        if (compare(start, end) == 0){
            if (compare(start, date) == 0)
                return true;
        } else {
            return compare(date, start) * compare(date, end) < 1;
        }
	}
	/**
	 * show the calendar day.
	 */
	function show(target) {
		var opts = $.data(target, 'fullCalendar').options;
		//cyclicalYear = ' 农历 ' + cyclical(opts.year - 1900 + 36) + '年 【' + Animals[(opts.year - 4) % 12] + '年】';
		$(target).find('.calendar-title span').html(opts.year + '年 ' + opts.months[opts.month - 1]);
		
		var body = $(target).find('div.calendar-body');
		body.find('>table').remove();
		
		var t = $('<table cellspacing="0" cellpadding="0" border="0"><thead></thead><tbody></tbody></table>').prependTo(body);
		var tr;
        var i,j;
        tr = $('<tr></tr>').appendTo(t.find('thead'));
		for (i = opts.firstDay; i < opts.weeks.length; i++) {
			tr.append('<th>' + opts.weeks[i] + '</th>');
		}
		for (i = 0; i < opts.firstDay; i++) {
			tr.append('<th>' + opts.weeks[i] + '</th>');
		}
		
		var weeks = getWeeks(target, opts.year, opts.month);
		var currentCa = new Calendar(opts.year, opts.month - 1);
		for (i = 0; i < weeks.length; i++) {
			var week = weeks[i];
			tr = $('<tr></tr>').appendTo(t.find('tbody'));
			for (j = 0; j < week.length; j++) {
				var day = week[j];
				var info = null;
				var dayGrid = $('<td class="calendar-day fullcalendar-day calendar-other-month"></td>');
				var dayNum = $('<div></div>').html(day[2]).addClass('day-Number').appendTo(dayGrid);
				var dayLunar = $('<div></div>');
				var dayWorkHour = $('<div></div>');
				if (opts.month == day[1]) {
					info = currentCa[day[2] - 1];
					if (info.color) {
						dayNum.addClass('fullcalendar-' + info.color);
					}
					if (opts.lunarDay) {
						dayLunar.html((opts.solarTerms && info.solarTerms) ? info.solarTerms : cDay(info.lDay, info.lMonth))
							.addClass((opts.solarTerms && info.solarTerms) ? 'day-solarTerms' : 'day-lunar')
							.appendTo(dayGrid);
					}
					if (opts.workHour) {
						dayWorkHour.html((info.week == 0 || info.week == 6) ? "0 Hour" : "7 Hours")
							.addClass('day-work-hour')
							.appendTo(dayGrid);
					}
				}
				if (info && (info.lunarFestival || info.solarFestival)) {
					dayGrid.addClass('festival');
				}
				dayGrid.data('info', info)
					.attr('abbr', day[0] + ',' + day[1] + ',' + day[2])
					.appendTo(tr)
					.hover(function (e) {
                        if (e.which > 0) {
                            return false;
                        }
						clearTimeout(hideTimer);
						var inf = $(this).data('info');
						if (inf) {
							detail.html("");
							var date = $('<div></div>').html(inf.sYear + ' 年 ' + inf.sMonth + ' 月 ' + inf.sDay + ' 日').addClass('info');
							var week = $('<div></div>').html('星期' + nStr1[inf.week]).addClass('info');
							var lunar = $('<div></div>')
								.html('农历 ' + inf.cYear + '年 ' + monthName[inf.lMonth - 1] + '月 ' + cDay(inf.lDay, inf.lMonth,true))
								.addClass('info lunar');
							detail.append(date).append(week).append(lunar);
							detail.css(calculatePos.call(target, detail, e.currentTarget)).fadeIn();
							if (inf.lunarFestival) {
								detail.append('<div class="detailFestival lunarFestival">' + inf.lunarFestival + '</div>');
							}
							if (inf.solarFestival) {
								detail.append('<div class="detailFestival solarFestival">' + inf.solarFestival + '</div>');
							}
						} else {
							detail.hide();
						}
					}, function () {
						hideTimer = setTimeout(function () {
								$('div.fullcalendar-detail').hide();
							}, 500);
					});
			}
		}
		t.find('td[abbr^="' + opts.year + ',' + opts.month + '"]').removeClass('calendar-other-month');
		
		var now = new Date();
		var today = now.getFullYear() + ',' + (now.getMonth() + 1) + ',' + now.getDate();
		t.find('td[abbr="' + today + '"]').addClass('calendar-today');
		
		if (opts.current) {
			t.find('.calendar-selected').removeClass('calendar-selected').css('backgroundColor', '');
			var current = opts.current.getFullYear() + ',' + (opts.current.getMonth() + 1) + ',' + opts.current.getDate();
			t.find('td[abbr="' + current + '"]').addClass('calendar-selected').css('backgroundColor', '#FBEC88');
		}
		
		// calulate the saturday and sunday index
		var saIndex = 6 - opts.firstDay;
		var suIndex = saIndex + 1;
		if (saIndex >= 7)
			saIndex -= 7;
		if (suIndex >= 7)
			suIndex -= 7;
		t.find('tr').find('td:eq(' + saIndex + ')').addClass('calendar-saturday');
		t.find('tr').find('td:eq(' + suIndex + ')').addClass('calendar-sunday');
		t.find('td')
            .hover(function (e) {
				// todo: IE浏览器适配
				$(this).addClass('calendar-hover');
				if (opts.multiSelect && e.which == 1) {
                    var start = t.data('selectedStart');
                    var end = $(this).attr('abbr');
					t.find('td').each(function(){
						if(betweenDate($(this).attr('abbr'), start, end)){
							$(this).addClass('calendar-hover');
						}else{
                            $(this).removeClass('calendar-hover');
                        }
					});
					return false;
				}
			},function (e) {
				if (!opts.multiSelect || e.which == 0) {
					$(this).removeClass('calendar-hover');
				}
			})
			.mousedown(function (e) {
				t.data('selectedStart', $(this).attr("abbr"));
			})
			.mouseup(function (e) {
				if(t.find("td.calendar-hover").length > 1) {
					t.find('.calendar-selected').removeClass('calendar-selected').css('backgroundColor', '');
					t.find("td.calendar-hover").each(function(){
						$(this).removeClass('calendar-hover').addClass('calendar-selected').css('backgroundColor', '#FBEC88');
					});
                    var dates = [];
                    $.each(t.find("td.calendar-selected"), function(k, td){
                        dates.push($(td).attr("abbr"));
                    });
                    opts.onMultiSelect.call(target, dates[0], dates[dates.length-1], dates, t);
				}
			})
			.click(function (e) {
                // console.log(e);
                t.find('.calendar-selected').removeClass('calendar-selected').css('backgroundColor', '');
                $(this).addClass('calendar-selected').css('backgroundColor', '#FBEC88');

                var parts = $(this).attr('abbr').split(',');
                opts.current = new Date(parts[0], parseInt(parts[1]) - 1, parts[2]);
                opts.onSelect.call(target, opts.current, this);
                // 非当前月不做任何操作
                // if (opts.month != parts[1]) {
                // 	opts.year = parts[0];
                // 	opts.month = parts[1];
                // 	show(target);
                // }
			});
		opts.onChange.call(target, opts.year, opts.month);
	}
	
	function calculatePos(target, currentTarget) {
		var w = $(this).width(),
		h = $(this).height();
		var x = getElementLeft(currentTarget) + currentTarget.offsetWidth,
		y = getElementTop(currentTarget) + currentTarget.offsetHeight;
		if (x + $(target).width() > w) {
			x = x - $(target).width() - currentTarget.offsetWidth - 9;
		}
		if (y + $(target).height() > h) {
			y = y - $(target).height() - 9;
		}
		return {
			left : x,
			top : y
		};
	}
	
	function getElementLeft(element) {
		var actualLeft = element.offsetLeft;
		var current = element.offsetParent;
		while (current !== null) {
			actualLeft += current.offsetLeft;
			current = current.offsetParent;
		}
		return actualLeft;
	}
	
	function getElementTop(element) {
		var actualTop = element.offsetTop;
		var current = element.offsetParent;
		while (current !== null) {
			actualTop += current.offsetTop;
			current = current.offsetParent;
		}
		return actualTop;
	}
	
	$.fn.fullCalendar = function (options, param) {
		if (typeof options == 'string') {
			return $.fn.fullCalendar.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function () {
			var state = $.data(this, 'fullCalendar');
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'fullCalendar', {
						options : $.extend({}, $.fn.calendar.defaults, $.fn.fullCalendar.defaults, $.fn.fullCalendar.parseOptions(this), options)
					});
				init(this);
			}
			if (state.options.border == false) {
				$(this).addClass('calendar-noborder');
			}
			setSize(this);
			show(this);
			$(this).find('div.calendar-menu').hide(); // hide the calendar menu
		});
	};
	
	$.fn.fullCalendar.methods = {
		options : function (jq) {
			return $.data(jq[0], 'fullCalendar').options;
		},
		resize : function (jq) {
			return jq.each(function () {
				setSize(this);
			});
		},
		moveTo : function (jq, date) {
			return jq.each(function () {
				$(this).fullCalendar({
					year : date.getFullYear(),
					month : date.getMonth() + 1,
					current : date
				});
			});
		},
        getDateRange : function(jq) {
            return {
                first: $(jq).find('td:first').attr('abbr'),
                last: $(jq).find('td:last').attr('abbr')
            };
        },
        showWorkHour : function(jq, data) {
            var date = '2017,12,1';
            var workHour = 8;
            $(jq).find('td[abbr="' + date + '"] div.day-work-hour').remove();
            $(jq).find('td[abbr="' + date + '"]').append($("<div></div>").addClass("day-work-hour").html(workHour + " Hour(s)"))
        }
	};
	
	$.fn.fullCalendar.parseOptions = function (target) {
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [
			'width', 'height', {
				firstDay : 'number',
				fit : 'boolean',
				border : 'boolean',
				history : 'boolean',
                multiSelect: 'boolean',
                onSelect: 'function',
                onMultiSelect: 'function'
			}
		]));
	};
	
	$.fn.fullCalendar.defaults = {
		width : 180,
		height : 180,
		fit : false,
		border : false,
		history : true, //配置左右日历不同
        workHour : true,
        multiSelect : false,
		firstDay : 1,
		weeks : ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],// thead title
		months : ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],//select menu month
		year : new Date().getFullYear(),
		month : new Date().getMonth() + 1,
		current : new Date(),
		solarTerms : false, //显示二十四节气
		lunarDay : false, //显示农历
		onSelect : function (date, target) {},
        onMultiSelect : function(start, end, dates, target) {},
		onChange : function (year, month) {}
	};
	$.parser.plugins.push('fullCalendar');
})(jQuery);
