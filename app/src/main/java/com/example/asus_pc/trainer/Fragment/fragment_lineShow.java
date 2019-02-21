package com.example.asus_pc.trainer.Fragment;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.asus_pc.trainer.DBHelper;
import com.example.asus_pc.trainer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.Nullable;
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
    private Button line_weight, line_BMI, line_BFR, line_BMR, line_Whtr;
    private LineChartView lineChartView;
    String[] X_values = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};//X轴的标注
    String [] Y_values = {"0, 10, 20, 30, 40, 50, 60, 70, 80, 90"};
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXvalues = new ArrayList<AxisValue>();
    private List<AxisValue>mAxisYvalues = new ArrayList<>();

    private DBHelper dbHelper;
    private SQLiteDatabase mSQL;
    private ArrayList<String> weightList = new ArrayList<String>();


    private Boolean isFirstSearch = true;

    private View mView;

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

        dbHelper = new DBHelper(getActivity().getApplicationContext());
        mSQL = dbHelper.getReadableDatabase();

        lineChartView = mView.findViewById(R.id.lineChartView);
        line_BFR = mView.findViewById(R.id.line_BFR);
        line_BMI = mView.findViewById(R.id.line_BMI);
        line_BMR = mView.findViewById(R.id.line_BMR);
        line_Whtr = mView.findViewById(R.id.line_Whtr);
        line_weight = mView.findViewById(R.id.line_weight);
        line_Whtr.setOnClickListener(this);
        line_BMR.setOnClickListener(this);
        line_BMI.setOnClickListener(this);
        line_BFR.setOnClickListener(this);
        line_weight.setOnClickListener(this);

        getAxisXlables();//获取X轴的标注
        getAxisYlables();//获取Y轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化lineChartView


    }

    /**
     * 设置X轴上的值
     */
    private void getAxisXlables() {
        for (int i = 0; i < X_values.length; i++) {
            mAxisXvalues.add(new AxisValue(i).setLabel(X_values[i]));
        }
    }

    /**
     * 设置Y轴上的值
     */
    private void getAxisYlables(){
        for (int i = 0; i < Y_values.length; i++){
            mAxisYvalues.add(new AxisValue(i).setLabel(Y_values[i]));
        }
    }

    /**
     * 图表每个点的显示
     */
    private void getAxisPoints() {

        //测试数据
        /*for (int i = 0; i < weather.length; i++) {
            mPointValues.add(new PointValue(i, weather[i]));
        }*/

        //
        if (isFirstSearch) {
            Cursor cursor = mSQL.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    String weightFromSQL = cursor.getString(cursor.getColumnIndex("Weight"));
                    weightList.add(weightFromSQL);
                }
            }
            cursor.close();
            for (int i = 0; i < weightList.size(); i++) {
                mPointValues.add(new PointValue(i, Integer.parseInt(weightList.get(i))));
            }
        }
        isFirstSearch = false;
    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXvalues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("重量");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        axisY.setValues(mAxisYvalues);
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setMaxZoom((float) 2);//最大方法比例
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChartView.setCurrentViewport(v);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mView.getParent()).removeView(mView);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_BFR:
                break;
            case R.id.line_BMI:
                break;
            case R.id.line_BMR:
                break;
            case R.id.line_Whtr:
                break;
            default:
                break;

        }
    }
}
