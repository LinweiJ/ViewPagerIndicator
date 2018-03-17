package com.lwj.widget.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by linWeiJia on 2017/9/8.
 */

public class ViewPagerIndicator extends View {

    private Path mPath;
    private Paint paintFill;
    private Paint paintStroke;
    private int mNum;//个数
    private float mRadius;//半径
    private float mLength;//线长
    private float mHeight;//线宽
    private float mOffset;//偏移量
    private int mSelected_color;//选中颜色
    private int mDefault_color;//默认颜色
    private int mIndicatorType;//点类型
    private int mDistanceType;//距离类型
    private float mDistance;//间隔距离
    private int mPosition;//第几张
    /**
     * 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
     */
    private static final float M = 0.551915024494f;
    private float mPercent;
    private boolean mIsLeft;
    private boolean mIsInfiniteCircle;//无限循环
    private boolean mAnimation;

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStyleable(context, attrs);
        paintStroke = new Paint();
        paintFill = new Paint();
        mPath = new Path();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //实心
        paintFill.setStyle(Paint.Style.FILL_AND_STROKE);
        paintFill.setColor(mSelected_color);
        paintFill.setAntiAlias(true);
        paintFill.setStrokeWidth(3);
        //空心
        paintStroke.setStyle(Paint.Style.FILL);
        paintStroke.setColor(mDefault_color);
        paintStroke.setAntiAlias(true);
        paintStroke.setStrokeWidth(3);
    }

    /**
     * 绘制   invalidate()后 执行
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mNum <= 0)) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.translate(width / 2, height / 2);
        //初始化画笔
        initPaint();
        //距离
        switch (mDistanceType) {
            case DistanceType.BY_DISTANCE:

                break;
            case DistanceType.BY_RADIUS://圆心到 3倍半径 只有一个半径
                mDistance = 3 * mRadius;
                break;
            case DistanceType.BY_LAYOUT://布局等分
                if (mIndicatorType == IndicatorType.CIRCLE_LINE) {
                    mDistance = width / (mNum + 1);
                } else {
                    mDistance = width / mNum;
                }
                break;
        }
        switch (mIndicatorType) {
            case IndicatorType.CIRCLE://圆
                for (int i = 0; i < mNum; i++) {//默认点 -(mNum - 1) * 0.5f * mDistance 第一个点
                    canvas.drawCircle(-(mNum - 1) * 0.5f * mDistance + i * mDistance, 0, mRadius, paintStroke);
                }
                //选中
                canvas.drawCircle(-(mNum - 1) * 0.5f * mDistance + mOffset, 0, mRadius, paintFill);
                break;
            case IndicatorType.LINE://线
                paintStroke.setStrokeWidth(mRadius);
                float startX = -(mNum - 1) * 0.5f * mDistance - mLength / 2;
                float stopX = -(mNum - 1) * 0.5f * mDistance + mLength / 2;
                //默认
                for (int i = 0; i < mNum; i++) {
                    canvas.drawLine(startX + i * mDistance, 0, stopX + i * mDistance, 0, paintStroke);
                }
                //选中
                paintFill.setStrokeWidth(mRadius);
                float startF = -(mNum - 1) * 0.5f * mDistance - mLength / 2 + mOffset;
                float stopF = -(mNum - 1) * 0.5f * mDistance + mLength / 2 + mOffset;
                canvas.drawLine(startF, 0, stopF, 0, paintFill);

                break;
            case IndicatorType.CIRCLE_LINE://圆线

                if (mPosition == mNum - 1) {//最后一个 右滑
                    //第一个 线 选中 消失
                    float leftClose = -(mNum) * 0.5f * mDistance - mRadius;
                    float rightClose = leftClose + 2 * mRadius + mOffset;
                    float topClose = -mRadius;
                    float bottomClose = mRadius;
                    RectF rectClose = new RectF(leftClose, topClose, rightClose, bottomClose);// 设置个新的长方形
                    canvas.drawRoundRect(rectClose, mRadius, mRadius, paintStroke);
                    //最后一个 线  显示
                    float rightOpen = -(mNum) * 0.5f * mDistance + mNum * mDistance + mRadius;
                    float leftOpen = rightOpen - 2 * mRadius - mDistance + mOffset;
                    float topOpen = -mRadius;
                    float bottomOpen = mRadius;
                    RectF rectOpen = new RectF(leftOpen, topOpen, rightOpen, bottomOpen);// 设置个新的长方形
                    canvas.drawRoundRect(rectOpen, mRadius, mRadius, paintStroke);
                    //圆
                    for (int i = 1; i < mNum; i++) {
                        canvas.drawCircle(rightClose - mRadius + i * mDistance, 0, mRadius, paintStroke);
                    }

                } else {
                    //第一个 线 选中 消失
                    float leftClose = -(mNum) * 0.5f * mDistance + mPosition * mDistance - mRadius;
                    float rightClose = leftClose + 2 * mRadius + mDistance - mOffset;
                    float topClose = -mRadius;
                    float bottomClose = mRadius;
                    RectF rectClose = new RectF(leftClose, topClose, rightClose, bottomClose);// 设置个新的长方形
                    canvas.drawRoundRect(rectClose, mRadius, mRadius, paintStroke);
                    //第二个 线  显示
                    if (mPosition < mNum - 1) {
                        float rightOpen = -(mNum) * 0.5f * mDistance + (mPosition + 2) * mDistance + mRadius;
                        float leftOpen = rightOpen - 2 * mRadius - mOffset;
                        float topOpen = -mRadius;
                        float bottomOpen = mRadius;
                        RectF rectOpen = new RectF(leftOpen, topOpen, rightOpen, bottomOpen);// 设置个新的长方形
                        canvas.drawRoundRect(rectOpen, mRadius, mRadius, paintStroke);
                    }
                    //圆
                    for (int i = mPosition + 3; i <= mNum; i++) {
                        canvas.drawCircle(-(mNum) * 0.5f * mDistance + i * mDistance, 0, mRadius, paintStroke);
                    }
                    for (int i = mPosition - 1; i >= 0; i--) {
                        canvas.drawCircle(-(mNum) * 0.5f * mDistance + i * mDistance, 0, mRadius, paintStroke);
                    }
                }
                break;
            case IndicatorType.BEZIER://贝塞尔
                for (int i = 0; i < mNum; i++) {//默认点 -(mNum - 1) * 0.5f * mDistance 第一个点
                    canvas.drawCircle(-(mNum - 1) * 0.5f * mDistance + i * mDistance, 0, mRadius, paintStroke);
                }
                //选中
                drawCubicBezier(canvas);
                break;
            case IndicatorType.SPRING://贝塞尔 弹性
                for (int i = 0; i < mNum; i++) {//默认点 -(mNum - 1) * 0.5f * mDistance 第一个点
                    canvas.drawCircle(-(mNum - 1) * 0.5f * mDistance + i * mDistance, 0, mRadius, paintStroke);
                }
                drawSpringBezier(canvas);
                break;
        }
    }

    private Point[] mSpringPoint = new Point[6];

    /**
     * 绘制弹性
     *
     * @param canvas
     */
    private void drawSpringBezier(Canvas canvas) {
        //右圆圆心
        float right_circle_x;
        //右圆半径
        float right_circle_radius;
        //左圆圆心
        float left_circle_x;
        //左圆半径
        float left_circle_radius;
        //最大半径
        float max_radius = mRadius;
        //最小半径
        float min_radius = mRadius / 2;
        //控制点
        if (mPosition == mNum - 1 && !mIsLeft) {//第一个 右滑  0---4
            if (mPercent <= 0.5) {
                right_circle_x = -(mNum - 1) * 0.5f * mDistance + (mNum - 1) * mDistance;
                left_circle_x = -(mNum - 1) * 0.5f * mDistance + (0.5f - mPercent) / 0.5f * (mNum - 1) * mDistance;
                right_circle_radius = min_radius + (max_radius - min_radius) * (0.5f - mPercent) / 0.5f;
            } else {
                right_circle_x = -(mNum - 1) * 0.5f * mDistance + (1f - mPercent) / 0.5f * (mNum - 1) * mDistance;
                left_circle_x = -(mNum - 1) * 0.5f * mDistance;
                right_circle_radius = min_radius;
            }
            left_circle_radius = mRadius * (mPercent);
        } else if (mPosition == mNum - 1 && mIsLeft) {//最后一个 左滑 4--0
            //0-1
            if (mPercent >= 0.5) {//左亭
                left_circle_radius = min_radius + (max_radius - min_radius) * (-0.5f + mPercent) / 0.5f;
                left_circle_x = -(mNum - 1) * 0.5f * mDistance;
                right_circle_x = -(mNum - 1) * 0.5f * mDistance + (1 - mPercent) / 0.5f * (mNum - 1) * mDistance;
            } else {//左动
                left_circle_radius = min_radius;
                left_circle_x = -(mNum - 1) * 0.5f * mDistance + (0.5f - mPercent) / 0.5f * (mNum - 1) * mDistance;
                right_circle_x = -(mNum - 1) * 0.5f * mDistance + (mNum - 1) * mDistance;
            }
            right_circle_radius = mRadius * (1 - mPercent);
        } else if (mIsLeft) {//中间的 左滑
            mOffset = (mPercent + mPosition) * mDistance;
            if (mPercent >= 0.5) {
                left_circle_x = -(mNum - 1) * 0.5f * mDistance + ((mPercent - 0.5f) / 0.5f + mPosition) * mDistance;
                right_circle_x = -(mNum - 1) * 0.5f * mDistance + (1 + mPosition) * mDistance;
                right_circle_radius = min_radius + (max_radius - min_radius) * (mPercent - 0.5f) / 0.5f;
            } else {
                right_circle_x = -(mNum - 1) * 0.5f * mDistance + ((mPercent) / 0.5f + mPosition) * mDistance;
                left_circle_x = -(mNum - 1) * 0.5f * mDistance + mPosition * mDistance;
                right_circle_radius = min_radius;
            }
            left_circle_radius = mRadius * (1 - mPercent);
        } else {//右滑
            mOffset = (mPercent + mPosition) * mDistance;
            if (mPercent <= 0.5) {
                left_circle_x = -(mNum - 1) * 0.5f * mDistance + (mPosition) * mDistance;
                right_circle_x = -(mNum - 1) * 0.5f * mDistance + ((mPercent) / 0.5f + mPosition) * mDistance;
                left_circle_radius = min_radius + (max_radius - min_radius) * (0.5f - mPercent) / 0.5f;
            } else {
                left_circle_x = -(mNum - 1) * 0.5f * mDistance + ((mPercent - 0.5f) / 0.5f + mPosition) * mDistance;
                right_circle_x = -(mNum - 1) * 0.5f * mDistance + (mPosition + 1) * mDistance;
                left_circle_radius = min_radius;
            }
            right_circle_radius = mRadius * (mPercent);
        }
        //右圆
        canvas.drawCircle(right_circle_x, 0, right_circle_radius, paintFill);
        //左圆
        canvas.drawCircle(left_circle_x, 0, left_circle_radius, paintFill);
        //贝塞尔
        //控制点
        mSpringPoint[0].x = left_circle_x;
        mSpringPoint[0].y = -left_circle_radius;
        mSpringPoint[5].x = mSpringPoint[0].x;
        mSpringPoint[5].y = left_circle_radius;
        //
        mSpringPoint[1].x = (left_circle_x + right_circle_x) / 2;
        mSpringPoint[1].y = -left_circle_radius / 2;
        mSpringPoint[4].x = mSpringPoint[1].x;
        mSpringPoint[4].y = left_circle_radius / 2;
        //
        mSpringPoint[2].x = right_circle_x;
        mSpringPoint[2].y = -right_circle_radius;
        mSpringPoint[3].x = mSpringPoint[2].x;
        mSpringPoint[3].y = right_circle_radius;

        mPath.reset();
        mPath.moveTo(mSpringPoint[0].x, mSpringPoint[0].y);
        mPath.quadTo(mSpringPoint[1].x, mSpringPoint[1].y, mSpringPoint[2].x, mSpringPoint[2].y);
        mPath.lineTo(mSpringPoint[3].x, mSpringPoint[3].y);
        mPath.quadTo(mSpringPoint[4].x, mSpringPoint[4].y, mSpringPoint[5].x, mSpringPoint[5].y);
        canvas.drawPath(mPath, paintFill);
    }

    /**
     * 绘制贝塞尔曲线
     *
     * @param canvas
     */
    private void drawCubicBezier(Canvas canvas) {
        //更换控制点
        changePoint();

        /** 清除Path中的内容
         reset不保留内部数据结构，但会保留FillType.
         rewind会保留内部的数据结构，但不保留FillType */
        mPath.reset();

        //0
        mPath.moveTo(mControlPoint[0].x, mControlPoint[0].y);
        //0-3
        mPath.cubicTo(mControlPoint[1].x, mControlPoint[1].y, mControlPoint[2].x, mControlPoint[2].y, mControlPoint[3].x, mControlPoint[3].y);
        //3-6
        mPath.cubicTo(mControlPoint[4].x, mControlPoint[4].y, mControlPoint[5].x, mControlPoint[5].y, mControlPoint[6].x, mControlPoint[6].y);
        //6-9
        mPath.cubicTo(mControlPoint[7].x, mControlPoint[7].y, mControlPoint[8].x, mControlPoint[8].y, mControlPoint[9].x, mControlPoint[9].y);
        //9-0
        mPath.cubicTo(mControlPoint[10].x, mControlPoint[10].y, mControlPoint[11].x, mControlPoint[11].y, mControlPoint[0].x, mControlPoint[0].y);

        canvas.drawPath(mPath, paintFill);
    }

    /**
     * 控制点
     */
    private void changePoint() {
        mCenterPoint.y = 0;
        float mc = M;
        mControlPoint[2].y = mRadius;//底部
        mControlPoint[8].y = -mRadius;//顶部

        //圆心位置
        if (mPosition == mNum - 1 && !mIsLeft) {//第一个 右滑  0-->4

            if (mPercent <= 0.2) { //回弹 圆心到达
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + (mNum - 1) * mDistance;//最后一个
            } else if (mPercent <= 0.8) {//加速 左凸起 扁平化M 最右端固定不变  圆心移动
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + (1 - (mPercent - 0.2f) / 0.6f) * (mNum - 1) * mDistance;
            } else if (mPercent > 0.8 && mPercent < 1) {//
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance;//第一个
            } else if (mPercent == 1) {//圆
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance;
            }
            //控制点位置
            if (mPercent > 0.8 && mPercent <= 1) {//右凸起 圆心不变
                mControlPoint[5].x = mCenterPoint.x + mRadius * (2 - (mPercent - 0.8f) / 0.2f);//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius;//左半圆
            } else if (mPercent > 0.5 && mPercent <= 0.8) {//加速 左凸起 扁平化M 最右端固定不变  圆心移动
                mControlPoint[5].x = mCenterPoint.x + 2 * mRadius;//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius * (1 + (0.8f - mPercent) / 0.3f);//左半圆
                mControlPoint[2].y = mRadius * (1 + (mPercent - 0.8f) / 0.3f * 0.1f);//底部
                mControlPoint[8].y = -mRadius * (1 + (mPercent - 0.8f) / 0.3f * 0.1f);//顶部
                mc = mc * (1 + (-mPercent + 0.8f) / 0.3f * 0.3f);
            } else if (mPercent > 0.2 && mPercent <= 0.5) {//左右恢复 变圆M逐渐重置为原来大小  圆心移动
                mControlPoint[5].x = mCenterPoint.x + mRadius * (1 + (mPercent - 0.2f) / 0.3f);//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius * (1 + (mPercent - 0.2f) / 0.3f);//左半圆
                mControlPoint[2].y = mRadius * (1 - (mPercent - 0.2f) / 0.3f * 0.1f);//底部
                mControlPoint[8].y = -mRadius * (1 - (mPercent - 0.2f) / 0.3f * 0.1f);//顶部
                mc = mc * (1 + (mPercent - 0.2f) / 0.3f * 0.3f);
            } else if (mPercent > 0.1 && mPercent <= 0.2) {//左凹 圆心到达.0
                mControlPoint[5].x = mCenterPoint.x + mRadius;//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius * (1 - (0.2f - mPercent) / 0.1f * 0.5f);//左半圆
            } else if (mPercent >= 0 && mPercent <= 0.1) {//回弹 圆心到达
                mControlPoint[5].x = mCenterPoint.x + mRadius;//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius * (1 - (mPercent) / 0.1f * 0.5f);//左半圆
            }

        } else if (mPosition == mNum - 1 && mIsLeft) {//最后一个 左滑  4-->0
            if (mPercent <= 0.2) {//圆
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + (mNum - 1) * mDistance;
            } else if (mPercent <= 0.8) {//加速 左凸起 扁平化M 最右端固定不变  圆心移动
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + (1 - (mPercent - 0.2f) / 0.6f) * (mNum - 1) * mDistance;
            } else if (mPercent > 0.8 && mPercent < 1) {//回弹 圆心到达
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance;//第一个
            } else if (mPercent == 1) {//圆
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + mPosition * mDistance;
            }

            if (mPercent <= 0) {//圆

            } else if (mPercent <= 0.2 && mPercent >= 0) {//左凸起 圆心不变
                mControlPoint[5].x = mCenterPoint.x + mRadius;//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius * (1 + (mPercent) / 0.2f);//左半圆
            } else if (mPercent > 0.2 && mPercent <= 0.5) {//加速 右凸起 扁平化M 最左端固定不变  圆心移动
                mControlPoint[5].x = mCenterPoint.x + mRadius * (1 + (mPercent - 0.2f) / 0.3f);//右半圆
                mControlPoint[0].x = mCenterPoint.x - 2 * mRadius;//左半圆
                mControlPoint[2].y = mRadius * (1 - (mPercent - 0.2f) / 0.3f * 0.1f);//底部
                mControlPoint[8].y = -mRadius * (1 - (mPercent - 0.2f) / 0.3f * 0.1f);//顶部
                mc = mc * (1 + (mPercent - 0.2f) / 0.3f * 0.3f);
            } else if (mPercent > 0.5 && mPercent <= 0.8) {//左右恢复 变圆M逐渐重置为原来大小  圆心移动
                mControlPoint[5].x = mCenterPoint.x + mRadius * (1 + (0.8f - mPercent) / 0.3f);//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius * (1 + (0.8f - mPercent) / 0.3f);//左半圆
                mControlPoint[2].y = mRadius * (1 + (mPercent - 0.8f) / 0.3f * 0.1f);//底部
                mControlPoint[8].y = -mRadius * (1 + (mPercent - 0.8f) / 0.3f * 0.1f);//顶部
                mc = mc * (1 + (0.8f - mPercent) / 0.3f * 0.3f);
            } else if (mPercent > 0.8 && mPercent <= 0.9) {//右凹 圆心到达
                mControlPoint[5].x = mCenterPoint.x + mRadius * (1 - (mPercent - 0.8f) / 0.1f * 0.5f);//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius;//左半圆
            } else if (mPercent > 0.9 && mPercent <= 1) {//回弹 圆心到达
                mControlPoint[5].x = mCenterPoint.x + mRadius * (1 - (mPercent - 0.9f) / 0.1f * 0.5f);//右半圆
                mControlPoint[0].x = mCenterPoint.x - mRadius;//左半圆
            }


        } else {
            if (mPercent <= 0.2) {//圆
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + mPosition * mDistance;
            } else if (mPercent <= 0.8) {//加速 左凸起 扁平化M 最右端固定不变  圆心移动
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + (mPosition + mPercent) * mDistance;
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + (mPosition + (mPercent - 0.2f) / 0.6f) * mDistance;
            } else if (mPercent > 0.8 && mPercent < 1) {//回弹 圆心到达
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + (mPosition + 1) * mDistance;
            } else if (mPercent == 1) {//圆
                mCenterPoint.x = -(mNum - 1) * 0.5f * mDistance + mPosition * mDistance;
            }
            //控制点位置
            if (mIsLeft)//左滑
            {
                if (mPercent >= 0 && mPercent <= 0.2) {//右凸起 圆心不变
                    mControlPoint[5].x = mCenterPoint.x + mRadius * (2 - (0.2f - mPercent) / 0.2f);//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius;//左半圆
                } else if (mPercent > 0.2 && mPercent <= 0.5) {//加速 左凸起 扁平化M 最右端固定不变  圆心移动
                    mControlPoint[5].x = mCenterPoint.x + 2 * mRadius;//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius * (1 + (mPercent - 0.2f) / 0.3f);//左半圆
                    mControlPoint[2].y = mRadius * (1 - (mPercent - 0.2f) / 0.3f * 0.1f);//底部
                    mControlPoint[8].y = -mRadius * (1 - (mPercent - 0.2f) / 0.3f * 0.1f);//顶部
                    mc = mc * (1 + (mPercent - 0.2f) / 0.3f * 0.3f);
                } else if (mPercent > 0.5 && mPercent <= 0.8) {//左右恢复 变圆M逐渐重置为原来大小  圆心移动
                    mControlPoint[5].x = mCenterPoint.x + mRadius * (1 + (0.8f - mPercent) / 0.3f);//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius * (1 + (0.8f - mPercent) / 0.3f);//左半圆
                    mControlPoint[2].y = mRadius * (1 + (mPercent - 0.8f) / 0.3f * 0.1f);//底部
                    mControlPoint[8].y = -mRadius * (1 + (mPercent - 0.8f) / 0.3f * 0.1f);//顶部
                    mc = mc * (1 + (-mPercent + 0.8f) / 0.3f * 0.3f);
                } else if (mPercent > 0.8 && mPercent <= 0.9) {//左凹 圆心到达
                    mControlPoint[5].x = mCenterPoint.x + mRadius;//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius * (1 - (mPercent - 0.8f) / 0.1f * 0.5f);//左半圆
                } else if (mPercent > 0.9 && mPercent <= 1) {//回弹 圆心到达
                    mControlPoint[5].x = mCenterPoint.x + mRadius;//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius * (1 - (1.0f - mPercent) / 0.1f * 0.5f);//左半圆
                }
            } else//右滑
            {
                if (mPercent <= 1 && mPercent >= 0.8) {//左凸起 圆心不变
                    mControlPoint[5].x = mCenterPoint.x + mRadius;//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius * (2 - (mPercent - 0.8f) / 0.2f);//左半圆
                } else if (mPercent > 0.5 && mPercent <= 0.8) {//加速 右凸起 扁平化M 最左端固定不变  圆心移动
                    mControlPoint[5].x = mCenterPoint.x + mRadius * (2 - (mPercent - 0.5f) / 0.3f);//右半圆
                    mControlPoint[0].x = mCenterPoint.x - 2 * mRadius;//左半圆
                    mControlPoint[2].y = mRadius * (1 - (0.8f - mPercent) / 0.3f * 0.1f);//底部
                    mControlPoint[8].y = -mRadius * (1 - (0.8f - mPercent) / 0.3f * 0.1f);//顶部
                    mc = mc * (1 + (0.8f - mPercent) / 0.3f * 0.3f);
                } else if (mPercent > 0.2 && mPercent <= 0.5) {//左右恢复 变圆M逐渐重置为原来大小  圆心移动
                    mControlPoint[5].x = mCenterPoint.x + mRadius * (1 + (mPercent - 0.2f) / 0.3f);//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius * (1 + (mPercent - 0.2f) / 0.3f);//左半圆
                    mControlPoint[2].y = mRadius * (1 - (mPercent - 0.2f) / 0.3f * 0.1f);//底部
                    mControlPoint[8].y = -mRadius * (1 - (mPercent - 0.2f) / 0.3f * 0.1f);//顶部
                    mc = mc * (1 + (mPercent - 0.2f) / 0.3f * 0.3f);
                } else if (mPercent > 0.1 && mPercent <= 0.2) {//右凹 圆心到达
                    mControlPoint[5].x = mCenterPoint.x + mRadius * (1 - (0.2f - mPercent) / 0.1f * 0.5f);//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius;//左半圆
                } else if (mPercent >= 0 && mPercent <= 0.1) {//回弹 圆心到达
                    mControlPoint[5].x = mCenterPoint.x + mRadius * (1 - (mPercent) / 0.1f * 0.5f);//右半圆
                    mControlPoint[0].x = mCenterPoint.x - mRadius;//左半圆
                }

            }
        }

        //11 0 1
        mControlPoint[0].y = 0;
        mControlPoint[1].x = mControlPoint[0].x;
        mControlPoint[1].y = mRadius * mc;
        mControlPoint[11].x = mControlPoint[0].x;
        mControlPoint[11].y = -mRadius * mc;
        //2 3 4
        mControlPoint[2].x = mCenterPoint.x - mRadius * mc;
        mControlPoint[3].x = mCenterPoint.x;
        mControlPoint[3].y = mControlPoint[2].y;
        mControlPoint[4].x = mCenterPoint.x + mRadius * mc;
        mControlPoint[4].y = mControlPoint[2].y;
        //5 6 7
        mControlPoint[5].y = mRadius * mc;
        mControlPoint[6].x = mControlPoint[5].x;
        mControlPoint[6].y = 0;
        mControlPoint[7].x = mControlPoint[5].x;
        mControlPoint[7].y = -mRadius * mc;
        //8 9 10
        mControlPoint[8].x = mCenterPoint.x + mRadius * mc;
        mControlPoint[9].x = mCenterPoint.x;
        mControlPoint[9].y = mControlPoint[8].y;
        mControlPoint[10].x = mCenterPoint.x - mRadius * mc;
        mControlPoint[10].y = mControlPoint[8].y;
    }

    private Point[] mControlPoint = new Point[9];
    private CenterPoint mCenterPoint = new CenterPoint();

    class CenterPoint {
        float x;
        float y;
    }

    class Point {
        float x;
        float y;
    }

    /**
     * xml 参数设置  选中颜色 默认颜色  点大小 长度 距离 距离类型 类型 真实个数(轮播)
     *
     * @param context
     * @param attrs
     */
    private void setStyleable(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mSelected_color = array.getColor(R.styleable.ViewPagerIndicator_vpi_selected_color, 0xffffffff);
        mDefault_color = array.getColor(R.styleable.ViewPagerIndicator_vpi_default_color, 0xffcdcdcd);
        mRadius = array.getDimension(R.styleable.ViewPagerIndicator_vpi_radius, 20);//px
        mLength = array.getDimension(R.styleable.ViewPagerIndicator_vpi_length, 2 * mRadius);//px
        mDistance = array.getDimension(R.styleable.ViewPagerIndicator_vpi_distance, 3 * mRadius);//px
        mDistanceType = array.getInteger(R.styleable.ViewPagerIndicator_vpi_distanceType, DistanceType.BY_RADIUS);
        mIndicatorType = array.getInteger(R.styleable.ViewPagerIndicator_vpi_indicatorType, IndicatorType.CIRCLE);
        mNum = array.getInteger(R.styleable.ViewPagerIndicator_vpi_num, 0);
        mAnimation = array.getBoolean(R.styleable.ViewPagerIndicator_vpi_animation, true);
        array.recycle();
        switch (mIndicatorType) {
            case IndicatorType.BEZIER:
                mControlPoint = new Point[]{new Point(), new Point(), new Point(), new Point(), new Point(), new Point(),
                        new Point(), new Point(), new Point(), new Point(), new Point(), new Point()};
                break;
            case IndicatorType.SPRING:
                mSpringPoint = new Point[]{new Point(), new Point(), new Point(), new Point(), new Point(), new Point()};
                break;
        }
        invalidate();
    }

    /**
     * 移动指示点
     *
     * @param percent  比例
     * @param position 第几个
     * @param isLeft   是否左滑
     */
    public void move(float percent, int position, boolean isLeft) {
        mPosition = position;
        mPercent = percent;
        mIsLeft = isLeft;
        switch (mIndicatorType) {
            case IndicatorType.CIRCLE_LINE://圆线
                if (mPosition == mNum - 1 && !isLeft) {//第一个 右滑
                    mOffset = percent * mDistance;
                }
                if (mPosition == mNum - 1 && isLeft) {//最后一个 左滑
                    mOffset = percent * mDistance;
                } else {//中间
                    mOffset = percent * mDistance;
                }
                break;
            case IndicatorType.CIRCLE://圆
            case IndicatorType.LINE://线
                if (mPosition == mNum - 1 && !isLeft) {//第一个 右滑
                    mOffset = (1 - percent) * (mNum - 1) * mDistance;
                } else if (mPosition == mNum - 1 && isLeft) {//最后一个 左滑
                    mOffset = (1 - percent) * (mNum - 1) * mDistance;
                } else {//中间的
                    mOffset = (percent + mPosition) * mDistance;
                }
                break;
            case IndicatorType.BEZIER://贝塞尔

                break;
            case IndicatorType.SPRING://弹性

                break;
        }

        invalidate();
    }

    /**
     * 个数
     *
     * @param num
     */
    public ViewPagerIndicator setNum(int num) {
        mNum = num;
        invalidate();
        return this;
    }

    /**
     * 类型
     *
     * @param indicatorType
     */
    public ViewPagerIndicator setType(int indicatorType) {
        mIndicatorType = indicatorType;
        invalidate();
        return this;
    }


    /**
     * 线,圆
     */
    public interface IndicatorType {
        int LINE = 0;
        int CIRCLE = 1;
        int CIRCLE_LINE = 2;
        int BEZIER = 3;
        int SPRING = 4;
    }


    /**
     * 半径
     *
     * @param radius
     */
    public ViewPagerIndicator setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
        return this;
    }

    /**
     * 距离 在IndicatorDistanceType为BYDISTANCE下作用
     *
     * @param distance
     */
    public ViewPagerIndicator setDistance(float distance) {
        this.mDistance = distance;
        invalidate();
        return this;
    }

    /**
     * 距离类型
     *
     * @param mDistanceType
     */
    public ViewPagerIndicator setDistanceType(int mDistanceType) {
        this.mDistanceType = mDistanceType;
        invalidate();
        return this;
    }

    /**
     * 布局,距离,半径
     */
    public interface DistanceType { //
        int BY_RADIUS = 0;
        int BY_DISTANCE = 1;
        int BY_LAYOUT = 2;
    }

    /**
     * 一般 不循环 固定
     * @param viewPager 适配的viewpager
     * @return
     */
    public ViewPagerIndicator setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, viewPager.getAdapter().getCount(),false);
        return this;
    }


    /**
     *
     * @param viewpager 适配的viewpager
     * @param CycleNumber 伪无限循环 真实个数
     * @return
     */
    public ViewPagerIndicator setViewPager(ViewPager viewpager, int CycleNumber) {

        setViewPager(viewpager, CycleNumber,false);
        return this;
    }
    /**
     *
     * @param viewPager 适配的viewpager
     * @param isInfiniteCircle 真无限循环 配合BannerView 通常是true;false为一般 不循环 固定等价于{@link #setViewPager(ViewPager viewPager)}
     *
     * @return
     */
    public ViewPagerIndicator setViewPager(ViewPager viewPager, boolean isInfiniteCircle) {

        if(isInfiniteCircle){
            setViewPager(viewPager,viewPager.getAdapter().getCount()-2,isInfiniteCircle);
        }else{
            setViewPager(viewPager,viewPager.getAdapter().getCount(),isInfiniteCircle);
        }
        return this;
    }

    /**
     *
     * @param viewpager 适配的viewpager
     * @param CycleNumber 真/伪无限循环都必须输入
     * @param isInfiniteCircle 真无限循环 配合Banner
     * @return
     */
    public ViewPagerIndicator setViewPager(ViewPager viewpager, int CycleNumber, boolean isInfiniteCircle) {
        mNum = CycleNumber;
        mIsInfiniteCircle = isInfiniteCircle;
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //记录上一次滑动的positionOffsetPixels值
            private int lastValue = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(!mAnimation){
                    //不需要动画
                    return;
                }
                boolean isLeft = mIsLeft;
                if (lastValue / 10 > positionOffsetPixels / 10) {
                    //右滑
                    isLeft = false;
                } else if (lastValue / 10 < positionOffsetPixels / 10) {
                    //左滑
                    isLeft = true;
                }
                if (mNum > 0&&!mIsInfiniteCircle) {
                    move(positionOffset, position % mNum, isLeft);
                }else if(mNum>0&&mIsInfiniteCircle){
                    if(position==0){
                        position=mNum-1;
                    }else if(position==mNum+1){
                        position=0;
                    }else{
                        position--;
                    }
                    move(positionOffset, position , isLeft);
                }
                lastValue = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                if(mAnimation){
                    //需要动画
                    return;
                }
                if(mNum>0&&!mIsInfiniteCircle)
                {
                    move(0, position % mNum, false);
                }else if(mNum>0&&mIsInfiniteCircle){
                    if(position==0){
                        position=mNum-1;
                    }else if(position==mNum+1){
                        position=0;
                    }else{
                        position--;
                    }
                    move(0, position , false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return this;
    }


}
