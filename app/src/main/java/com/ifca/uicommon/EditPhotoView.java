package com.ifca.uicommon;

import android.view.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rex on 2017/6/9.
 * 功能：自定义截屏，涂鸦图片控件
 */

public class EditPhotoView extends View {
    /**
     * 原图
     */
    private Bitmap baseBitmap = null;
    /**
     * 进行修改的图
     */
    private Bitmap tempBitmap = null;
    /**
     * 控件宽
     */
    private int viewWidth = 0;
    /**
     * 控件高
     */
    private int viewHeight = 0;
    /**
     * 是否划线涂鸦
     */
    private boolean isMove = false;
    /**
     * 是否进行截图
     */
    private boolean isCrop = false;
    /**
     * 是否可编辑状态
     */
    private boolean isEdit = false;
    /**
     * 涂鸦画笔
     */
    private Paint mLinePaint = null;
    /**
     * 涂鸦的画笔颜色 和笔宽
     */
    private static final int paintColor = Color.RED;
    private static final float strokeWidth = 2.0f;
    /**
     * 绘制线的起点 、终点
     */
    private float startX = 0, endX, startY = 0, endY = 0;
    /**
     * 图片与空间宽 高的缩放比例
     */
    private float scale = 0f;

    /**
     * 截屏框画笔
     */
    private Paint mBorderPaint;
    private static final float mBorderPaintWidth =2f;
    /**
     * 背景画笔
     */
    private Paint mBgPaint;
    /**
     * 截屏框矩形
     */
    private RectF mBorderRectF;
    /**
     * 控件框矩形
     */
    private RectF mBmpRectF;

    public EditPhotoView(Context context) {
        this(context, null);
    }

    public EditPhotoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditPhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setDrawingCacheEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditPhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //关闭硬件加速

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (tempBitmap != null) {
            editBitmap(tempBitmap,canvas);
        }
    }

    public void editBitmap(Bitmap bitmap,Canvas canvas) {
        canvas.drawBitmap(bitmap,null,mBmpRectF,null);
        if (isMove) {
            Canvas canvas1=new Canvas(bitmap);
            mLinePaint = new Paint();
            mLinePaint.setColor(paintColor);
            mLinePaint.setStrokeWidth(strokeWidth);
            mLinePaint.setAntiAlias(true);
            mLinePaint.setStrokeJoin(Paint.Join.ROUND);//线段圆滑
            mLinePaint.setStyle(Paint.Style.STROKE);
            canvas1.drawLine(startX, startY, endX, endY, mLinePaint);
        }
        if (isCrop) {
            canvas.drawBitmap(bitmap,null,mBmpRectF,null);
            if (mBorderPaint==null) {
                mBorderPaint = new Paint();
                mBorderPaint.setStyle(Paint.Style.STROKE);
                mBorderPaint.setColor(Color.parseColor("#AAFFFFFF"));
                mBorderPaint.setStrokeWidth(mBorderPaintWidth);
            }
            canvas.drawRect(mBorderRectF, mBorderPaint);
            drawBackgroundRectF(canvas);
        }

    }

    /**
     * 绘制灰色背景矩形
     */
    public void drawBackgroundRectF(Canvas canvas) {
        if (mBgPaint==null) {
            mBgPaint = new Paint();
            mBgPaint.setColor(Color.parseColor("#b0000000"));
            mBgPaint.setAlpha(150);
        }
        canvas.drawRect(mBmpRectF.left, mBmpRectF.top, mBmpRectF.right, mBorderRectF.top, mBgPaint);
        canvas.drawRect(mBmpRectF.left, mBorderRectF.top, mBorderRectF.left, mBorderRectF.bottom, mBgPaint);
        canvas.drawRect(mBmpRectF.left, mBorderRectF.bottom, mBmpRectF.right, mBmpRectF.bottom, mBgPaint);
        canvas.drawRect(mBorderRectF.right, mBorderRectF.top, mBmpRectF.right, mBorderRectF.bottom, mBgPaint);
    }



    /*-
             -------------------------------------
             |                top                |
             -------------------------------------
             |      |                    |       |<——————————mBmpBound
             |      |                    |       |
             | left |                    | right |
             |      |                    |       |
             |      |                  <─┼───────┼────mBorderBound
             -------------------------------------
             |              bottom               |
             -------------------------------------
            */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEdit) {
            startX = endX;
            startY = endY;
            endX = event.getX();
            endY = event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isMove = false;
                invalidate();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                isMove = true;
                invalidate();
                return true;
            }
        }
        if (isCrop) {
            startX = endX;
            startY = endY;
            endX = event.getX();
            endY = event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float moveX = endX - startX;
                float moveY = endY - startY;
                if (mBorderRectF.left <= endX && endX <= mBorderRectF.right && mBmpRectF.top <= endY && endY <= mBorderRectF.top + 10) {
                    //当手指触碰截屏框上部区域，设置截屏框上边线上移或下滑

                    mBorderRectF.top = mBorderRectF.top + moveY;
                    if (mBorderRectF.top < mBmpRectF.top) {
                        mBorderRectF.top = mBmpRectF.top;
                    }
                } else if (endX <= (mBorderRectF.left + 10) && endX >= mBmpRectF.left && endY <= mBorderRectF.bottom && endY >= mBorderRectF.top) {
                    //当手指触碰截屏框左侧的区域，设置截屏框左边线  左右移动
                    mBorderRectF.left = mBorderRectF.left + moveX;
                    if (mBorderRectF.left < mBmpRectF.left) {
                        mBorderRectF.left = mBmpRectF.left;
                    }
                } else if (endY <= mBmpRectF.bottom && endY >= mBorderRectF.bottom - 10 && endX <= mBorderRectF.right && endX >= mBorderRectF.left) {
                    //下方区域
                    mBorderRectF.bottom = mBorderRectF.bottom + moveY;
                    if (mBorderRectF.bottom > mBmpRectF.bottom) {
                        mBorderRectF.bottom = mBmpRectF.bottom;
                    }
                } else if (endX >= mBorderRectF.right - 10 && endX <= mBmpRectF.right && endY <= mBorderRectF.bottom && endY >= mBorderRectF.top) {
                    //右边区域
                    mBorderRectF.right = mBorderRectF.right + moveX;
                    if (mBorderRectF.right > mBmpRectF.right) {
                        mBorderRectF.right = mBmpRectF.right;
                    }
                } else if (endX <= mBorderRectF.left && endX > mBmpRectF.left && endY >= mBmpRectF.top && endY < mBorderRectF.top) {
                    //左上角区域
                    mBorderRectF.left = mBorderRectF.left + moveX;
                    mBorderRectF.top = mBorderRectF.top + moveY;
                    if (mBorderRectF.left <= mBmpRectF.left) {
                        mBorderRectF.left = mBmpRectF.left;
                    }
                    if (mBorderRectF.top <= mBmpRectF.top) {
                        mBorderRectF.top = mBmpRectF.top;
                    }
                } else if (endX <= mBorderRectF.left && endX > mBmpRectF.left && endY <= mBmpRectF.bottom && endY > mBorderRectF.bottom) {
                    //左下角区域
                    mBorderRectF.bottom = mBorderRectF.bottom + moveY;
                    mBorderRectF.left = mBorderRectF.left + moveX;
                    if (mBorderRectF.left <= mBmpRectF.left) {
                        mBorderRectF.left = mBmpRectF.left;
                    }
                    if (mBorderRectF.bottom >= mBmpRectF.bottom) {
                        mBorderRectF.bottom = mBmpRectF.bottom;
                    }
                } else if (endY >= mBorderRectF.bottom && endY < mBmpRectF.bottom && endX > mBorderRectF.right && endX <= mBmpRectF.right) {
                    //右下角区域
                    mBorderRectF.bottom = mBorderRectF.bottom + moveY;
                    mBorderRectF.right = mBorderRectF.right + moveX;
                    if (mBorderRectF.bottom >= mBmpRectF.bottom) {
                        mBorderRectF.bottom = mBmpRectF.bottom;
                    }
                    if (mBorderRectF.right >= mBmpRectF.right) {
                        mBorderRectF.right = mBmpRectF.right;
                    }
                } else if (endX <= mBmpRectF.right && endX > mBorderRectF.right && endY <= mBorderRectF.top && endY > mBmpRectF.top) {
                    //右上角区域
                    mBorderRectF.top = mBorderRectF.top + moveY;
                    mBorderRectF.right = mBorderRectF.right + moveX;
                    if (mBorderRectF.top <= mBmpRectF.top) {
                        mBorderRectF.top = mBmpRectF.top;
                    }
                    if (mBorderRectF.right >= mBmpRectF.right) {
                        mBorderRectF.right = mBmpRectF.right;
                    }
                } else if (endX < mBorderRectF.right - 10 && endX > mBorderRectF.left + 10 && endY > mBorderRectF.top + 10 && endY <= mBorderRectF.bottom - 10) {
                    mBorderRectF.bottom = mBorderRectF.bottom + moveY;
                    mBorderRectF.top = mBorderRectF.top + moveY;
                    mBorderRectF.right = mBorderRectF.right + moveX;
                    mBorderRectF.left = mBorderRectF.left + moveX;
                    if (mBorderRectF.bottom >= mBmpRectF.bottom) {
                        mBorderRectF.bottom = mBmpRectF.bottom;
                    }
                    if (mBorderRectF.top <= mBmpRectF.top) {
                        mBorderRectF.top = mBmpRectF.top;
                    }
                    if (mBorderRectF.left <= mBmpRectF.left) {
                        mBorderRectF.left = mBmpRectF.left;
                    }
                    if (mBorderRectF.right > mBmpRectF.right) {
                        mBorderRectF.right = mBmpRectF.right;
                    }
                }
                invalidate();
                return true;
            }


        }

        return super.onTouchEvent(event);

    }
    /**
     * 重置回原图
     */
    public void clearDraw() {
        isCrop=false;
        isEdit=false;
        if (tempBitmap != null && baseBitmap != null) {
            tempBitmap.recycle();
            int width = (int) (baseBitmap.getWidth() * scale);
            int height = (int) (baseBitmap.getHeight() * scale);
            tempBitmap = Bitmap.createScaledBitmap(baseBitmap, width, height, true);
            invalidate();
        }
    }

    /**
     * 获取编辑后的图
     */
    public Bitmap getEditBitmap() {
        Bitmap saveBitmap=null;
        if (isCrop){
            saveBitmap= Bitmap.createBitmap(tempBitmap,(int) mBorderRectF.left,(int)mBorderRectF.top,(int)mBorderRectF.width(),(int) mBorderRectF.height());
            isCrop=false;
        }else {
            saveBitmap = tempBitmap;
        }
        return Bitmap.createBitmap(Bitmap.createScaledBitmap(saveBitmap, (int) (saveBitmap.getWidth() / scale), (int) (saveBitmap.getHeight() / scale), true));


    }

    /**
     * 设置编辑的图片
     */
    public void setBaseBitmap(Bitmap bitmap, int mViewWidth, int mViewHeight) {
        baseBitmap = bitmap;
        viewWidth = mViewWidth;
        viewHeight = mViewHeight;
        float scaleWidth = (float) viewWidth / bitmap.getWidth();
        float scaleHeight = (float) viewHeight / bitmap.getHeight();
        scale = Math.min(scaleHeight, scaleWidth);
        int width = (int) (bitmap.getWidth() * scale);
        int height = (int) (bitmap.getHeight() * scale);
        tempBitmap = Bitmap.createScaledBitmap(baseBitmap, width, height, true);
        //图片外框矩形
        mBmpRectF = new RectF();
        mBmpRectF.left = getLeft();
        mBmpRectF.right = mBmpRectF.left+width;
        mBmpRectF.top = getTop();
        mBmpRectF.bottom = mBmpRectF.top+height;
        //使剪切框一开始出现在图片中心位置，宽度为控件的一半
        mBorderRectF = new RectF();
        mBorderRectF.left = (mBmpRectF.right + mBmpRectF.left - width / 2) / 2;
        mBorderRectF.right = mBorderRectF.left + width / 2;
        mBorderRectF.top = mBmpRectF.top + (height / 4);
        mBorderRectF.bottom = mBmpRectF.bottom - height / 4;
        invalidate();
    }
    /**
     * 设置图片可涂鸦
     * */
    public void setEditable(boolean b) {
        isEdit = b;
    }
    /**
     * 设置图片裁剪*/
    public void setCropable(boolean b) {
        isCrop = b;
        isEdit=!b;
        isMove=!b;
        invalidate();
    }
    /**
     *释放资源
     * */
    public void recycleRes(){
        tempBitmap.recycle();
        baseBitmap.recycle();
    }
}
