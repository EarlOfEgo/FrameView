package com.sthagios.frameview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class FrameView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mHeight: Float = 0f
    private var mLineLength: Float = 0f
    private val mPaintLine: Paint = Paint()
    private val mPaintRectangle: Paint = Paint()
    private var mFrameSizePx: Float = 0f
    private var mWidth: Float = 0f

    /**
     * Sets the frame size in dp
     */
    var frameSize: Int = 0
        set(value) {
            field = value
            mFrameSizePx = dpToPx(value)
            requestLayout()
            invalidate()
        }

    /**
     * Sets the line length.
     */
    var lineWidth: Int = 0
        set(value) {
            field = value
            mPaintLine.strokeWidth = dpToPx(value)
            requestLayout()
            invalidate()
        }

    /**
     * Sets the line color, should be a color int.
     */
    var lineColor: Int = 0
        set(value) {
            field = value
            mPaintLine.color = value
            requestLayout()
            invalidate()
        }

    /**
     * Sets the line length.
     */
    var lineLength: Int = 0
        set(value) {
            field = value
            mLineLength = dpToPx(value)
            requestLayout()
            invalidate()
        }


    /**
     * Sets the frame color, should be between 0..255
     */
    var frameAlpha: Int = 0
        set(value) {
            field = value
            mPaintRectangle.alpha = value
            requestLayout()
            invalidate()
        }

    init {
        mPaintLine.style = Paint.Style.STROKE

        mPaintRectangle.style = Paint.Style.FILL
        mPaintRectangle.color = Color.BLACK

        val typedArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.FrameView, defStyleAttr, defStyleRes)

        try {
            typedArray?.getLayoutDimension(R.styleable.FrameView_line_length, dpToPx(16).toInt())
                    ?.let { lineLength = (it / context.resources.displayMetrics.density).toInt() }
            typedArray?.getLayoutDimension(R.styleable.FrameView_line_width, dpToPx(2).toInt())
                    ?.let { lineWidth = (it / context.resources.displayMetrics.density).toInt() }
            typedArray?.getColor(R.styleable.FrameView_line_color, Color.WHITE)
                    ?.let { lineColor = it }
            typedArray?.getLayoutDimension(R.styleable.FrameView_frame_size, dpToPx(28).toInt())
                    ?.let { frameSize = (it / context.resources.displayMetrics.density).toInt() }
            typedArray?.getInt(R.styleable.FrameView_frame_alpha, 125)
                    ?.let { frameAlpha = it }
        } finally {
            typedArray?.recycle()
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawShadowRectangles(canvas!!)
        drawUpperLeftLines(canvas)
        drawLowerLeftLines(canvas)
        drawUpperRightLines(canvas)
        drawLowerRightLines(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec).toFloat()
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    private fun dpToPx(dp: Int): Float {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f)
    }

    private fun drawLowerLeftLines(canvas: Canvas) {
        canvas.drawLine(mFrameSizePx, mHeight - mLineLength - mFrameSizePx, mFrameSizePx, mHeight - mFrameSizePx, mPaintLine)
        canvas.drawLine(mFrameSizePx - mPaintLine.strokeWidth / 2, mHeight - mFrameSizePx, mFrameSizePx + mLineLength, mHeight - mFrameSizePx, mPaintLine)
    }

    private fun drawLowerRightLines(canvas: Canvas) {
        canvas.drawLine(mWidth - mFrameSizePx, mHeight - mLineLength - mFrameSizePx, mWidth - mFrameSizePx, mHeight - mFrameSizePx, mPaintLine)
        canvas.drawLine(mWidth - mLineLength - mFrameSizePx, mHeight - mFrameSizePx, mWidth - mFrameSizePx + mPaintLine.strokeWidth / 2, mHeight - mFrameSizePx, mPaintLine)
    }

    private fun drawShadowRectangles(canvas: Canvas) {
        canvas.drawRect(0f, 0f, mFrameSizePx, mHeight, mPaintRectangle)
        canvas.drawRect(mWidth - mFrameSizePx, 0f, mWidth, mHeight, mPaintRectangle)
        canvas.drawRect(mFrameSizePx, 0f, mWidth - mFrameSizePx, mFrameSizePx, mPaintRectangle)
        canvas.drawRect(mFrameSizePx, mHeight - mFrameSizePx, mWidth - mFrameSizePx, mHeight, mPaintRectangle)
    }

    private fun drawUpperLeftLines(canvas: Canvas) {
        canvas.drawLine(mFrameSizePx, mFrameSizePx, mFrameSizePx, mFrameSizePx + mLineLength, mPaintLine)
        canvas.drawLine(mFrameSizePx - mPaintLine.strokeWidth / 2, mFrameSizePx, mFrameSizePx + mLineLength, mFrameSizePx, mPaintLine)
    }

    private fun drawUpperRightLines(canvas: Canvas) {
        canvas.drawLine(mWidth - mFrameSizePx, mFrameSizePx, mWidth - mFrameSizePx, mFrameSizePx + mLineLength, mPaintLine)
        canvas.drawLine(mWidth - mLineLength - mFrameSizePx, mFrameSizePx, mWidth - mFrameSizePx + mPaintLine.strokeWidth / 2, mFrameSizePx, mPaintLine)
    }
}