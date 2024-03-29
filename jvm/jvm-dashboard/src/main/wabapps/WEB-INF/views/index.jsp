<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html >
<html lang="zh-CN">
<head>
    <title>Dashboard</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <link rel="stylesheet" type="text/css" href="/css/tga.css">
    <link rel="stylesheet" type="text/css" href="/js/layui/css/layui.css">
</head>
<body class="main-body">

<div class="topbar" id="Tg3TopNav">
    <div class="topbar" id="Tg3TopNavFull">
        <div class="topbar-logo"><i class="tg3-logo tg3-white-logo"></i>
            <div class="bs_drop_project tg3-selected-drop">
                <div class="tg3-search-drop">
                    <p class="tg3-drop-text-part bd_click_main">Dashboard</p>
                </div>
            </div>
        </div>
        <ul class="top-tab-change">
            <li class="selected" id="tm_panel"><i class="tg3-icon tab-icon ti1"></i><span>数据仪表盘</span></li>
            <li><i class="tg3-icon tab-icon ti2"></i><span>数据分析</span>
                <ul class="sub-menu">
                    <li id="tm_event"><i class="tg3-icon subi1"></i><span>事件分析</span></li>
                    <li id="tm_retention"><i class="tg3-icon subi2"></i><span>留存分析</span></li>
                    <li id="tm_funnel"><i class="tg3-icon subi3"></i><span>漏斗分析</span></li>
                </ul>
            </li>
            <li id="tm_group"><i class="tg3-icon tab-icon ti4"></i><span>用户分群</span></li>
            <li><i class="tg3-icon tab-icon ti3"></i><span>元数据管理</span>
                <ul class="sub-menu">
                    <li id="tm_source"><i class="tg3-icon subi4"></i><span>元事件管理</span></li>
                    <li id="tm_sourceEvent"><i class="tg3-icon subi5"></i><span>事件属性管理</span></li>
                    <li id="tm_sourceUser"><i class="tg3-icon subi6"></i><span>用户属性管理</span></li>
                </ul>
            </li>
        </ul>
        <div class="user-list">
            <ul>
                <li><i class="tg3-icon tg3-top-tab3"><b class="border-animation"></b></i></li>
                <li><i class="tg3-icon tg3-top-tab2"><b class="border-animation"></b></i><span>张志君</span>
                    <ul class="sub-menu">
                        <li class="top_drop_info"><i class="tg3-icon subi7"></i><span>账户信息</span></li>
                        <li class="top_drop_quit"><i class="tg3-icon subi10"></i><span>退出登录</span></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="tg3-maincontent">
    <div class="left-sidebar left-sidebar2 boxshadow">
        <div class="tg3-leftsidebar-op"><span>数据仪表盘</span>
            <ul>
                <li><i class="tg3-icon left-title-icon2 tg3-creat-new-kanban" id="bs_panel_add"></i></li>
            </ul>
        </div>
        <div class="kind-of-kanban">
            <div><span>共享仪表盘</span><b class="spread-or-hide bw_fold">收起</b></div>
            <ul class="nav nav-sidebar bm_share_panel">
                <li data-i="263" class="active"><i class="tg3-icon tg3-side-icon icon1-1"></i><a
                        href="panel.html#/panel/101_263" title="核心指标">核心指标</a></li>
                <li data-i="68"><i class="tg3-icon tg3-side-icon icon1-1"></i><a href="javascript:void(0);"
                                                                                 title="基础仪表盘">基础数据</a>
                </li>
                <li data-i="69"><i class="tg3-icon tg3-side-icon icon1-1"></i><a href="javascript:void(0);"
                                                                                 title="用户转化">用户转化</a>
                </li>
                <li data-i="82"><i class="tg3-icon tg3-side-icon icon1-1"></i><a href="javascript:void(0);"
                                                                                 title="客服支持">客服支持</a>
                </li>
                <li data-i="83"><i class="tg3-icon tg3-side-icon icon1-1"></i><a href="javascript:void(0);"
                                                                                 title="运营仪表盘">运营数据</a>
                </li>

            </ul>
        </div>
        <div class="kind-of-kanban">
            <div><span>我的仪表盘</span><b class="spread-or-hide bw_fold">收起</b></div>
            <ul class="nav nav-sidebar bm_owner_panel">
                <li class="op-selected bw_add_dom" style="display: none"><i
                        class="tg3-icon tg3-side-icon icon3-1"></i><input type="text" maxlength="100"
                                                                          placeholder="请输入仪表盘名称">
                    <div class="op"><i class="tg3-icon edit navside-edit bp_panel_save"></i><i
                            class="tg3-icon delete bp_panel_del"></i></div>
                </li>
            </ul>
        </div>
    </div><!-- uiView:  -->
    <div ui-view="" class="ng-scope">
        <div id="mainContent" class="ng-scope">
            <div id="Tg3HeaderTop" class="boxshadow"><span class="title bt_panel_name">核心指标</span>
                <div class="operation-union fr">
                    <li class="bp_panel_refresh"><i class="tg3-icon tg3-head-icon hi4"></i><span>刷新</span></li>
                    <li class="bp_panel_share"><i class="tg3-icon tg3-head-icon hi2"></i><span>共享</span></li>
                    <li class="bp_saved_list"><i class="tg3-icon tg3-head-icon hi1"></i><span>编辑</span></li>
                </div>
            </div>
            <div class="container-fluid tg3-auto-system tg3-datakanban-new tg3-kanban2" id="contentPart">
                <div class="blockpart col-lg-3 col-md-3 col-sm-6 col-xs-6" id="bl_report_254" data-o="0" data-e="1"><i
                        class="remove">×</i>
                    <div class="boxshadow min-kanban ba_inner_254">
                        <div class="top-des"><span class="fl title">DAU（日活跃用户数量）</span> <span class="icon-union fr" data-i="254">                                <!--i class="tg3-icon tg3-download-icon bd_download_254"></i-->                            </span>
                        </div>
                        <div id="bs_chart_254" class="data-show bs_total_254"><span>${dau_day_str}</span>
                            <p>${dau_day}</p></div>
                        <ul class="rate bs_rate_254">
                            <script type="text/javascript">
                                var day = ${dau_day}; // 今天的数据
                                var dau_day_before = ${dau_day_before}; //前一天数据
                                var dau_day_before_str = "${dau_day_before_str}"; //前一天日期值
                                if(dau_day_before != 0){
                                    // 计算环比，环比增长率=（本期数-上期数）/上期数×100%。
                                    var num = (day-dau_day_before)/dau_day_before * 100;
                                    if(num >= 0){
                                        document.write('<li title="对比'+dau_day_before_str+'，增长了'+num+'%"><span>较环比</span><p><i class="triangle green-triangle"></i><b>'+num+'%</b></p></li>');
                                    }else{
                                        num = Math.abs(num);
                                        document.write('<li title="对比'+dau_day_before_str+'，下降了'+num+'%"><span>较环比</span><p><i class="triangle red-triangle"></i><b>'+num+'%</b></p></li>');
                                    }
                                }

                                var dau_day_month = ${dau_day_month}; // 上月的这一天的值
                                var dau_day_month_str = "${dau_day_month_str}"; //上月的这一天的日期值
                                if(dau_day_month>0){
                                    // 计算同比，同比增长率=（本期数-同期数）/|同期数|×100%。
                                    var num = (day-dau_day_month)/dau_day_month * 100;
                                    if(num >= 0){
                                        document.write('<li title="对比'+dau_day_month_str+'，增长了'+num+'%"><span>较同比</span><p><i class="triangle green-triangle"></i><b>'+num+'%</b></p></li>');
                                    }else{
                                        num = Math.abs(num);
                                        document.write('<li title="对比'+dau_day_month_str+'，下降了'+num+'%"><span>较同比</span><p><i class="triangle red-triangle"></i><b>'+num+'%</b></p></li>');
                                    }
                                }
                            </script>
                        </ul>
                    </div>
                </div>
                <div class="blockpart col-lg-3 col-md-3 col-sm-6 col-xs-6" id="bl_report_252" data-o="0" data-e="1"><i
                        class="remove">×</i>
                    <div class="boxshadow min-kanban ba_inner_252">
                        <div class="top-des"><span class="fl title">新增用户</span> <span class="icon-union fr"
                                                                                      data-i="252">                                <!--i class="tg3-icon tg3-download-icon bd_download_252"></i-->                            </span>
                        </div>
                        <div id="bs_chart_252" class="data-show bs_total_252"><span>2018-07-23</span>
                            <p>936</p></div>
                        <ul class="rate bs_rate_252">
                            <li title="对比2018-07-22，下降了46.8%"><span>较环比</span>
                                <p><i class="triangle red-triangle"></i><b>46.8%</b></p></li>
                            <li title="对比2018-06-23，下降了36.8%"><span>较同比</span>
                                <p><i class="triangle red-triangle"></i><b>36.8%</b></p></li>
                        </ul>
                    </div>
                </div>
                <div class="blockpart col-lg-3 col-md-3 col-sm-6 col-xs-6" id="bl_report_255" data-o="0" data-e="1"><i
                        class="remove">×</i>
                    <div class="boxshadow min-kanban ba_inner_255">
                        <div class="top-des"><span class="fl title">订单总额</span> <span class="icon-union fr"
                                                                                      data-i="255">                                <!--i class="tg3-icon tg3-download-icon bd_download_255"></i-->                            </span>
                        </div>
                        <div id="bs_chart_255" class="data-show bs_total_255"><span>2018-07-23</span>
                            <p>2992496</p></div>
                        <ul class="rate bs_rate_255">
                            <li title="对比2018-07-22，下降了46.9%"><span>较环比</span>
                                <p><i class="triangle red-triangle"></i><b>46.9%</b></p></li>
                            <li title="对比2018-06-23，下降了39.9%"><span>较同比</span>
                                <p><i class="triangle red-triangle"></i><b>39.9%</b></p></li>
                        </ul>
                    </div>
                </div>
                <div class="blockpart col-lg-3 col-md-3 col-sm-6 col-xs-6" id="bl_report_769" data-o="0" data-e="1"><i
                        class="remove">×</i>
                    <div class="boxshadow min-kanban ba_inner_769">
                        <div class="top-des"><span class="fl title">支付订单人数</span> <span class="icon-union fr"
                                                                                        data-i="769">                                <!--i class="tg3-icon tg3-download-icon bd_download_769"></i-->                            </span>
                        </div>
                        <div id="bs_chart_769" class="data-show bs_total_769"><span>2018-06-21</span>
                            <p>4784</p></div>
                        <ul class="rate bs_rate_769">
                            <li title="对比2018-06-20，增长了0.5%"><span>较环比</span>
                                <p><i class="triangle green-triangle"></i><b>0.5%</b></p></li>
                            <li title="对比2018-05-22，增长了13.2%"><span>较同比</span>
                                <p><i class="triangle green-triangle"></i><b>13.2%</b></p></li>
                        </ul>
                    </div>
                </div>
                <div class="blockpart col-lg-6 col-md-6 col-sm-12 col-xs-12" id="bl_report_297" data-o="0" data-e="1"><i
                        class="remove">×</i>
                    <div class="boxshadow ba_inner_297"><h3 class="title">订单支付次数</h3>                        <span
                            class="op-icon-union" data-i="297">                            <i
                            class="tg3-icon tg3-download-icon bd_download_297"></i>
                        <!--i class="tg3-icon tg3-zoom-icon bd_resize_297"></i-->                        </span>
                        <ul class="tg3-selectmore-fill tg3-stable-fill">
                            <li class="tg3-select-drop-union">
                                <div class="tg3-date-selected-drop fl"><p class="tg3-drop-text-part"><span id="dc297" class="dateTime">2018-6-24 至 2018-7-23</span>
                                    <i class="tg3-icon tg3-date-icon"></i></p></div>
                            </li>
                            <li data-i="297" class="graph-style-union bd_charttype_297" data-f="event"><i data-i="line"
                                                                                                          class="table-style-icon2 selected tg3-icon"></i><i
                                    data-i="bar" class="table-style-icon1 tg3-icon"></i><i data-i="lineSum"
                                                                                           class="table-style-icon3 tg3-icon"></i><i
                                    data-i="table" class="table-style-icon4 tg3-icon"></i></li>
                        </ul>

                        <div class="tg3-table-wrap">
                            <div class="content">
                                <div class="graph-part" id="bs_graph_297">
                                    <div class="graph-data-scroll"><p class="scroll-title">2018-07-23</p>
                                        <div class="arrow" style="display: none;"><i class="pre"></i></div>
                                        <div class="date-show-content scroll-content">
                                            <ul style="width: 130px; margin-left: auto; margin-right: auto; transition: all 0s;">
                                                <li><b style="color: #3daae3">582</b><span>总体</span></li>
                                            </ul>
                                        </div>
                                        <div class="arrow" style="display: none;"><i class="next"></i></div>
                                    </div>
                                    <div id="bs_chart_297" style="width: 100%; height: 100%; -webkit-tap-highlight-color: transparent; user-select: none; position: relative; background: transparent;">

                                    </div>
                                </div>
                                <div class="table22 bt_list_297 tableDiv" style="display: none"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="blockpart col-lg-6 col-md-6 col-sm-12 col-xs-12" id="bl_report_447" data-o="1" data-e="1"><i
                        class="remove">×</i>
                    <div class="boxshadow ba_inner_447"><h3 class="title">注册用户城市分布情况</h3>                        <span
                            class="op-icon-union" data-i="447">                            <i
                            class="tg3-icon tg3-download-icon bd_download_447"></i>
                        <!--i class="tg3-icon tg3-zoom-icon bd_resize_447"></i-->                        </span>
                        <ul class="tg3-selectmore-fill tg3-stable-fill">
                            <li class="tg3-select-drop-union">
                                <div class="tg3-date-selected-drop fl"><p class="tg3-drop-text-part"><span id="dc447" class="dateTime">2018-4-20 至 2018-5-19</span>
                                    <i class="tg3-icon tg3-date-icon"></i></p></div>
                                <div class="tg3-selected-drop fl bd_select_date_447"><p
                                        class="bd_common_main tg3-drop-text-part bd_selected_date_447"><i
                                        class="tg3-graph tg3-triangle-gray-bottom"></i><span data-i="T1">按天</span></p>
                                    <ul class="bd_common bd_list_date_447">
                                        <li><a data-i="T1">按天</a></li>
                                        <li><a data-i="T4">按分钟</a></li>
                                        <li><a data-i="T0">按小时</a></li>
                                        <li><a data-i="T2">按周</a></li>
                                        <li><a data-i="T3">按月</a></li>
                                        <li><a data-i="T5">合计</a></li>
                                    </ul>
                                </div>
                                <div class="tg3-selected-drop fl long-tg3-selected-drop bd_select_step_447"
                                     style="display: none"></div>
                                <div class="tg3-selected-drop tg3-more-select fl bd_select_group_447" style="">
                                    <div class="tg3-drop-text-part min-height bd_click_main">
                                        <div class="newWrap">分组详情</div>
                                        <div class="wrap"><span data-i="荣耀京东自营旗舰店"
                                                                class="g_447_0">荣耀京东自营旗舰店<i>×</i></span><span
                                                data-i="1号店" class="g_447_1">1号店<i>×</i></span><span data-i="三星家电自营旗舰店"
                                                                                                     class="g_447_2">三星家电自营旗舰店<i>×</i></span>
                                        </div>
                                        <i class="tg3-graph tg3-triangle-gray-bottom"></i></div>
                                    <ul class="bd_click" style="display: none">
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_447" id="g_447_0"
                                                                                  type="checkbox" value="荣耀京东自营旗舰店"
                                                                                  checked=""><label for="g_447_0">荣耀京东自营旗舰店</label></span>
                                        </li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_447" id="g_447_1"
                                                                                  type="checkbox" value="1号店"
                                                                                  checked=""><label
                                                for="g_447_1">1号店</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_447" id="g_447_2"
                                                                                  type="checkbox" value="三星家电自营旗舰店"
                                                                                  checked=""><label for="g_447_2">三星家电自营旗舰店</label></span>
                                        </li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_447" id="g_447_3"
                                                                                  type="checkbox" value=""><label
                                                for="g_447_3"></label></span></li>
                                    </ul>
                                </div>
                                <div class="clear"></div>
                            </li>
                            <li data-i="447" class="graph-style-union bd_charttype_447" data-f="event"><i data-i="line"
                                                                                                          class="table-style-icon2 selected tg3-icon"></i><i
                                    data-i="bar" class="table-style-icon1 tg3-icon"></i><i data-i="lineSum"
                                                                                           class="table-style-icon3 tg3-icon"></i><i
                                    data-i="table" class="table-style-icon4 tg3-icon"></i></li>
                        </ul>
                        <div class="tg3-table-wrap">
                            <div class="content">
                                <div class="graph-part" id="bs_graph_447">
                                    <div id="bs_chart_447"
                                         style="width: 100%; height: 100%; -webkit-tap-highlight-color: transparent; user-select: none; position: relative; background: transparent;">
                                    </div>
                                </div>
                                <div class="stable-date-scroll graph-data-scroll bt_change_447" style="display: none">
                                    <div class="arrow"><i class="pre"></i></div>
                                    <div class="date-show-content scroll-content"><span></span></div>
                                    <div class="arrow"><i class="next"></i></div>
                                </div>
                                <div class="table22 bt_list_447" style="display: none"></div>
                                <ul class="tg3-page-list bt_page_447" style="display: none;"></ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="blockpart col-lg-6 col-md-6 col-sm-12 col-xs-12" id="bl_report_304" data-o="1" data-e="1"><i
                        class="remove">×</i>
                    <div class="boxshadow ba_inner_304"><h3 class="title">搜索关键词</h3>                        <span
                            class="op-icon-union" data-i="304">                            <i
                            class="tg3-icon tg3-download-icon bd_download_304"></i>
                        <!--i class="tg3-icon tg3-zoom-icon bd_resize_304"></i-->                        </span>
                        <ul class="tg3-selectmore-fill tg3-stable-fill">
                            <li class="tg3-select-drop-union">
                                <div class="tg3-date-selected-drop fl"><p class="tg3-drop-text-part"><span id="dc304" class="dateTime">2018-6-24 至 2018-7-23</span>
                                    <i class="tg3-icon tg3-date-icon"></i></p></div>
                                <div class="tg3-selected-drop fl bd_select_date_304"><p
                                        class="bd_common_main tg3-drop-text-part bd_selected_date_304"><i
                                        class="tg3-graph tg3-triangle-gray-bottom"></i><span data-i="T1">按天</span></p>
                                    <ul class="bd_common bd_list_date_304">
                                        <li><a data-i="T1">按天</a></li>
                                        <li><a data-i="T4">按分钟</a></li>
                                        <li><a data-i="T0">按小时</a></li>
                                        <li><a data-i="T2">按周</a></li>
                                        <li><a data-i="T3">按月</a></li>
                                        <li><a data-i="T5">合计</a></li>
                                    </ul>
                                </div>
                                <div class="tg3-selected-drop fl long-tg3-selected-drop bd_select_step_304"
                                     style="display: none"></div>
                                <div class="tg3-selected-drop tg3-more-select fl bd_select_group_304" style="">
                                    <div class="tg3-drop-text-part min-height bd_click_main">
                                        <div class="newWrap">分组详情</div>
                                        <div class="wrap"><span data-i="男装女装" class="g_304_0">男装女装<i>×</i></span><span
                                                data-i="家用电器" class="g_304_1">家用电器<i>×</i></span><span data-i="汽车用品"
                                                                                                       class="g_304_2">汽车用品<i>×</i></span><span
                                                data-i="美妆个护" class="g_304_3">美妆个护<i>×</i></span><span data-i="手机数码"
                                                                                                       class="g_304_4">手机数码<i>×</i></span><span
                                                data-i="家具厨具" class="g_304_5">家具厨具<i>×</i></span><span data-i="母婴用品"
                                                                                                       class="g_304_6">母婴用品<i>×</i></span><span
                                                data-i="运动户外" class="g_304_7">运动户外<i>×</i></span><span data-i="食品酒类"
                                                                                                       class="g_304_8">食品酒类<i>×</i></span>
                                        </div>
                                        <i class="tg3-graph tg3-triangle-gray-bottom"></i></div>
                                    <ul class="bd_click" style="display: none">
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_0"
                                                                                  type="checkbox" value="男装女装"
                                                                                  checked=""><label
                                                for="g_304_0">男装女装</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_1"
                                                                                  type="checkbox" value="家用电器"
                                                                                  checked=""><label
                                                for="g_304_1">家用电器</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_2"
                                                                                  type="checkbox" value="汽车用品"
                                                                                  checked=""><label
                                                for="g_304_2">汽车用品</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_3"
                                                                                  type="checkbox" value="美妆个护"
                                                                                  checked=""><label
                                                for="g_304_3">美妆个护</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_4"
                                                                                  type="checkbox" value="手机数码"
                                                                                  checked=""><label
                                                for="g_304_4">手机数码</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_5"
                                                                                  type="checkbox" value="家具厨具"
                                                                                  checked=""><label
                                                for="g_304_5">家具厨具</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_6"
                                                                                  type="checkbox" value="母婴用品"
                                                                                  checked=""><label
                                                for="g_304_6">母婴用品</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_7"
                                                                                  type="checkbox" value="运动户外"
                                                                                  checked=""><label
                                                for="g_304_7">运动户外</label></span></li>
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_304" id="g_304_8"
                                                                                  type="checkbox" value="食品酒类"
                                                                                  checked=""><label
                                                for="g_304_8">食品酒类</label></span></li>
                                    </ul>
                                </div>
                                <div class="clear"></div>
                            </li>
                            <li data-i="304" class="graph-style-union bd_charttype_304" data-f="event"><i data-i="line"
                                                                                                          class="table-style-icon2 selected tg3-icon"></i><i
                                    data-i="bar" class="table-style-icon1 tg3-icon"></i><i data-i="lineSum"
                                                                                           class="table-style-icon3 tg3-icon"></i><i
                                    data-i="table" class="table-style-icon4 tg3-icon"></i></li>
                        </ul>
                        <div class="tg3-table-wrap">
                            <div class="content">
                                <div class="graph-part" id="bs_graph_304">
                                    <div class="graph-data-scroll"><p class="scroll-title">2018-07-23</p>
                                        <div class="arrow"><i class="pre"></i></div>
                                        <div class="date-show-content scroll-content">
                                            <ul>
                                                <li><b style="color: #3daae3">302</b><span>男装女装</span></li>
                                                <li><b style="color: #2ed383">286</b><span>家用电器</span></li>
                                                <li><b style="color: #8b5c9b">295</b><span>汽车用品</span></li>
                                                <li><b style="color: #f3d049">307</b><span>美妆个护</span></li>
                                                <li><b style="color: #476cd0">274</b><span>手机数码</span></li>
                                                <li><b style="color: #3fa553">285</b><span>家具厨具</span></li>
                                                <li><b style="color: #524a89">319</b><span>母婴用品</span></li>
                                                <li><b style="color: #ec644c">284</b><span>运动户外</span></li>
                                                <li><b style="color: #4ECDC4">277</b><span>食品酒类</span></li>
                                            </ul>
                                        </div>
                                        <div class="arrow"><i class="next"></i></div>
                                    </div>
                                    <div id="bs_chart_304"
                                         style="width: 100%; height: 100%; -webkit-tap-highlight-color: transparent; user-select: none; position: relative; background: transparent;">

                                    </div>
                                </div>
                                <div class="stable-date-scroll graph-data-scroll bt_change_304" style="display: none">
                                    <div class="arrow"><i class="pre"></i></div>
                                    <div class="date-show-content scroll-content"><span></span></div>
                                    <div class="arrow"><i class="next"></i></div>
                                </div>
                                <div class="table22 bt_list_304" style="display: none"></div>
                                <ul class="tg3-page-list bt_page_304" style="display: none;"></ul>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="blockpart col-lg-6 col-md-6 col-sm-12 col-xs-12" id="bl_report_302"><i class="remove">×</i>
                    <div class="boxshadow ba_inner_302"><h3 class="title">优惠券使用情况</h3>                        <span
                            class="op-icon-union" data-i="302">                            <i
                            class="tg3-icon tg3-download-icon bd_download_302"></i>
                        <!--i class="tg3-icon tg3-zoom-icon bd_resize_302"></i-->                        </span>
                        <ul class="tg3-selectmore-fill tg3-stable-fill">
                            <li class="tg3-select-drop-union">
                                <div class="tg3-date-selected-drop fl"><p class="tg3-drop-text-part"><span id="dc302" class="dateTime">2018-6-24 至 2018-7-23</span>
                                    <i class="tg3-icon tg3-date-icon"></i></p></div>
                                <div class="tg3-selected-drop fl long-tg3-selected-drop bd_select_step_302" style=""><p
                                        class="bd_common_main tg3-drop-text-part bd_selected_step_302"><i
                                        class="tg3-graph tg3-triangle-gray-bottom"></i><span data-i="302-0-0">全步骤</span>
                                </p>
                                    <ul class="bd_common bd_list_step_302 tg3-more-drop-menu">
                                        <li data-w="全步骤" data-i="302-0-0"><a>全步骤</a></li>
                                        <li><a>从步骤1</a><i class="triangle-right"></i>
                                            <ul>
                                                <li data-w="从步骤1到步骤2" data-i="302-1-2"><a>到步骤2</a></li>
                                            </ul>
                                        </li>
                                    </ul>
                                </div>
                                <div class="tg3-selected-drop tg3-more-select fl bd_select_group_302" style="">
                                    <div class="tg3-drop-text-part min-height bd_click_main">
                                        <div class="newWrap">分组详情</div>
                                        <div class="wrap"><span data-i="总体" class="g_302_0">总体<i>×</i></span></div>
                                        <i class="tg3-graph tg3-triangle-gray-bottom"></i></div>
                                    <ul class="bd_click" style="display: none">
                                        <li><span class="tg3-checkbox-btn"><input name="cbg_302" id="g_302_0"
                                                                                  type="checkbox" value="总体" checked=""><label
                                                for="g_302_0">总体</label></span></li>
                                    </ul>
                                </div>
                                <div class="clear"></div>
                            </li>
                            <li data-i="302" class="graph-style-union bd_charttype_302" data-f="funnel"><i data-i="bar"
                                                                                                           class="table-style-icon1 selected tg3-icon"></i><i
                                    data-i="line" class="table-style-icon2 tg3-icon"></i><i data-i="table"
                                                                                            class="table-style-icon4 tg3-icon"></i>
                            </li>
                        </ul>
                        <div class="tg3-table-wrap">
                            <div class="content">
                                <div class="graph-part" id="bs_graph_302"><h3>全步骤（共2步）的累计转化漏斗</h3>
                                    <div id="bs_chart_302"
                                         style="width: 100%; height: 100%; -webkit-tap-highlight-color: transparent; user-select: none; position: relative; background: transparent;"
                                         _echarts_instance_="ec_1532327057224">
                                        <div style="position: relative; overflow: hidden; width: 525px; height: 330px; padding: 0px; margin: 0px; border-width: 0px;">
                                            <canvas width="525" height="330" data-zr-dom-id="zr_0"
                                                    style="position: absolute; left: 0px; top: 0px; width: 525px; height: 330px; user-select: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); padding: 0px; margin: 0px; border-width: 0px;"></canvas>
                                        </div>
                                        <div></div>
                                    </div>
                                </div>
                                <div class="table22 bt_list_302" style="display: none"></div>
                                <ul class="tg3-page-list bt_page_302" style="display: none;"></ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="pop-wrap ng-scope" style="display: none">
            <div class="tg3-pop-case">
                <div class="title"><span>保存报表</span> <i class="close-pop bp_cancel">x</i></div>
                <ul class="tg3-selectmore-fill show-content">
                    <li class="report-edit-infor">
                        <div><span>报表名称：</span> <input maxlength="100" type="text" placeholder="报表名称"></div>
                        <div><span>报表备注：</span> <textarea maxlength="30" placeholder="输入30个字内的报表描述"></textarea></div>
                    </li>
                </ul>
                <div class="btn-union">
                    <button class="tg3-btn tg3-purple-btn bp_save">保存</button>
                    <button class="tg3-btn tg3-border-purple-btn bp_cancel">取消</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/echarts.min.js"></script>
<script type="text/javascript" src="/js/layui/layui.js"></script>
<script type="text/javascript" src="/js/chars.option.js"></script>
<script type="text/javascript" src="/js/china.js"></script>
<script type="text/javascript" src="/js/my-dashboard.js"></script>
</body>
</html>
