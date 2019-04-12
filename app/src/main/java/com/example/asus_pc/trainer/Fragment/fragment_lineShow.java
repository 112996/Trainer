package com.example.asus_pc.trainer.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus_pc.trainer.bean.MyUsers;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.bean.User_Args;
import com.example.asus_pc.trainer.bean.User_Message;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.annotations.Nullable;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class fragment_lineShow extends Fragment implements View.OnClickListener {
    private View mView;

    private LineChartView lineChart;
    String[] date = {"10-22", "11-22", "12-22", "1-22", "6-22", "5-23", "5-22", "6-22", "5-23", "5-22"};//X轴的标注

    private List<PointValue> mPointValues_Weight = new ArrayList<PointValue>();
    private List<PointValue> mPointValues_BMI = new ArrayList<PointValue>();
    private List<PointValue> mPointValues_BFR = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List list_Weight = new ArrayList();
    private List list_BMI = new ArrayList();
    private List list_BMR = new ArrayList();
    private List list_BFR = new ArrayList();
    private List list_Whtr = new ArrayList();
    private boolean isFirst = true, isSecond = true;
    private float minY = 0f, maxY = 100f;
    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragmnet_lineshow_layout, container, false);
        }
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        lineChart = mView.findViewById(R.id.lineChartView);

        getAxisXLables();//获取x轴的标注
        query(); //查询以获取点的坐标
        initLineChart();//初始化

    }


    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    private void query() {
        //查询体重
        BmobQuery<User_Message> query = new BmobQuery<>();
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(MyUsers.class));
        query.order("-updatedAt");
        //包含作者信息
        query.include("author");
        query.findObjects(new FindListener<User_Message>() {

            @Override
            public void done(List<User_Message> object, BmobException e) {
                if (e == null) {
                    for (User_Message user_message : object) {
                        list_Weight.add(user_message.getWeight());
                    }
                    if (isFirst) {
                        for (int i = 0; i < list_Weight.size(); i++) {
                            mPointValues_Weight.add(new PointValue(i, Float.parseFloat((String) list_Weight.get(i))));
                        }
                    }
                    isFirst = false;
                } else {
                    Log.e("FromBmob", e.toString());
                }
            }

        });

        //BMI ,BFR, BMR, Whtr

        BmobQuery<User_Args> query_other = new BmobQuery<>();
        query_other.addWhereEqualTo("author", BmobUser.getCurrentUser(MyUsers.class));
        query_other.order("-updatedAt");
        //包含作者信息
        query_other.include("author");
        query_other.findObjects(new FindListener<User_Args>() {

            @Override
            public void done(List<User_Args> object, BmobException e) {
                if (e == null) {
                    for (User_Args user_Args : object) {
                        if (user_Args.getBmi().isEmpty() && user_Args.getBfr().isEmpty() && user_Args.getWhtr().isEmpty() && user_Args.getBmr().isEmpty()) {
                            Log.e("数据不全", "数据不全");
                        } else {
                            list_BMI.add(user_Args.getBmi());
                            list_BFR.add(user_Args.getBfr());
                            list_BMR.add(user_Args.getBmr());
                            list_Whtr.add(user_Args.getWhtr());
                            Log.d("BMI", list_BMI.toString());
                        }
                    }
                    if (isSecond) {
                        for (int i = 0; i < list_BMI.size(); i++) {
                            mPointValues_BMI.add(new PointValue(i, Float.parseFloat((String) list_BMI.get(i))));
                        }
                        for (int j = 0; j < list_BFR.size(); j++) {
                            mPointValues_BFR.add(new PointValue(j, Float.parseFloat((String) list_BFR.get(j))));
                        }
                    }
                    isSecond = false;

                } else {
                    Log.e("FromBmob", e.toString());
                }
            }

        });

    }

    private void initLineChart() {
        Line line = new Line(mPointValues_Weight).setColor(Color.parseColor("#FFCD41")); //折线的颜色（橘黄色）
        List<Line> lines = new ArrayList<Line>();
        LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(1);
        line.setFormatter(chartValueFormatter);//显示小数点
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状 这里是圆形 （有三种 ：ValueShape.SQUARE ValueShape.CIRCLE ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注 //
        //line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);


        Line line_bmi = new Line(mPointValues_BMI).setColor(Color.parseColor("#8E2323")); //折线的颜色（火砖色）
        line_bmi.setFormatter(chartValueFormatter);//显示小数点
        line_bmi.setShape(ValueShape.DIAMOND);//折线图上每个数据点的形状 这里是圆形 （有三种 ：ValueShape.SQUARE ValueShape.CIRCLE ValueShape.DIAMOND）
        line_bmi.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line_bmi.setFilled(false);//是否填充曲线的面积
        line_bmi.setHasLabels(true);//曲线的数据坐标是否加上备注 //
        //line_bmi.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line_bmi.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line_bmi.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line_bmi);

        Line line_bfr = new Line(mPointValues_BFR).setColor(Color.parseColor("#000000")); //折线的颜色（黑色）
        line_bfr.setShape(ValueShape.DIAMOND);//折线图上每个数据点的形状 这里是圆形 （有三种 ：ValueShape.SQUARE ValueShape.CIRCLE ValueShape.DIAMOND）
        line_bfr.setFormatter(chartValueFormatter);//显示小数点
        line_bfr.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line_bfr.setFilled(false);//是否填充曲线的面积
        line_bfr.setHasLabels(true);//曲线的数据坐标是否加上备注 //
        //line_bfr.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line_bfr.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line_bfr.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line_bfr);


        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true); //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE); //设置字体颜色 //
        axisX.setName("date"); //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues); //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部  //data.setAxisXTop(axisX); //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisYY = new Axis().setHasLines(false);
        axisYY.setName("数据");//y轴标注
        axisYY.setTextSize(10);//设置字体大小
        axisYY.setMaxLabelChars(6);

        for (float i = minY; i < maxY; i += 10) {
            mAxisYValues.add(new AxisValue(i).setLabel(i + ""));
        }
        axisYY.setValues(mAxisYValues);
        data.setAxisYLeft(axisYY);


        // 设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE); /**注：下面的7，10只是代表一个数字去类比而已 * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;*/

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.bottom = minY;
        v.top = maxY;
        lineChart.setMaximumViewport(v);  //必须设置在左右前，否则不生效。

        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
