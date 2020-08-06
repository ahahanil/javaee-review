$(function(){
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        $(".dateTime").each(function (i, e) {
            //执行一个laydate实例
            laydate.render({
                elem: $(e).get(0), //指定元素
                range: "至",
                max:0,
                min:-90,
                change:function (value, date, endDate) {

                }
            });
        });
    });
});

// 退货
$(function () {
    var data = null;
    var ele = $("#bs_chart_297");
    var myChart = echarts.init(ele.get(0));
    myChart.showLoading();

    // 异步加载数据
    $.get('/json/297.json', function (_data) {
        data = _data;
        myChart.hideLoading();
        // 填入数据
        option_297.xAxis[0].data = _data.x;
        option_297.series[0].data = _data.y;
        option_297.series[0].name = '总体';
        myChart.setOption(option_297);
    });

    $("[data-i='297'] i").click(function(){
        var $e = $(this);
        var type = $e.attr("data-i");
        $e.addClass("selected").siblings("i").removeClass("selected");
        if(type == "table"){
            //隐藏图表显示区
            ele.parent().hide();
            var html = "<table><thead><tr><th>日期</th><th>退货申请的总次数</th></tr></thead><tbody>";
            for(var i in data.y){
                html += "<tr><td>"+data.x[i]+"</td><td>"+data.y[i]+"</td></tr>";
            }
            html += "</tbody></table>";
            ele.parentsUntil(".content").parent().find(".tableDiv").html(html).show();
            return ;
        }
        ele.parentsUntil(".content").parent().find(".tableDiv").hide();
        ele.parent().show();

        myChart.dispose();// 销毁图表实例
        var option,option_data;
        if(type == "line"){
            option = option_297;
            option.xAxis[0].data = data.x;
            option.series[0].data = data.y;
            option.series[0].name = '总体';
        }else if(type == "bar"){
            option = option_297_2;
            var total = 0;
            for(var i in data.y){
                total += eval(data.y[i]);
            }
            option.xAxis[0].data = ['总体'];
            option.series[0].data = [total];
            option.series[0].name = '总量';
        }else if(type == "lineSum"){
            option = option_297_3;
            option.xAxis[0].data = data.x;
            option.series[0].data = data.y;
            option.series[0].name = '总体';
        }
        myChart = echarts.init(ele.get(0));
        // 设置属性和数据
        myChart.setOption(option);
    });
});

// 注册人所在地情况
$(function () {
    var ele = $("#bs_chart_447");
    var myChart = echarts.init(ele.get(0));
    myChart.showLoading();

    // 异步加载数据
    $.get('/json/447.json', function (data) {
        // 填入数据
        option_447.series[0].data = convertData(data);
        option_447.series[1].data = convertData(data.sort(function (a, b) {
            return b.value - a.value;
        }).slice(0, 6));
        myChart.hideLoading();
        myChart.setOption(option_447);
    });
});

// 搜索
$(function () {

    var ele = $("#bs_chart_304");
    var myChart = echarts.init(ele.get(0));
    myChart.setOption(option_304);
    // myChart.showLoading();

    // 异步加载数据
    // $.get('/json/297.json', function (data) {
    //     myChart.hideLoading();
    //     // 填入数据
    //     myChart.setOption({
    //         xAxis: {
    //             data: data.x
    //         },
    //         series: [{
    //             name: '总体',
    //             data: data.y
    //         }]
    //     });
    // });


});

// 店铺加入收藏情况
$(function () {

    var ele = $("#bs_chart_302");
    var myChart = echarts.init(ele.get(0));
    myChart.setOption(option_302);
    // myChart.showLoading();

    // 异步加载数据
    // $.get('/json/297.json', function (data) {
    //     myChart.hideLoading();
    //     // 填入数据
    //     myChart.setOption({
    //         xAxis: {
    //             data: data.x
    //         },
    //         series: [{
    //             name: '总体',
    //             data: data.y
    //         }]
    //     });
    // });


});