package com.sthagios.frameview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View




class FrameView @JvmOverloads constructor (context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mHeight: Float = 0f
    private var mLineLength: Float = 0f
    private val mPaintLine: Paint = Paint()
    private val mPaintRectangle: Paint = Paint()
    private var mFrameSize: Float = 0f
    private var mWidth: Float = 0f


    init {
        mPaintLine.style = Paint.Style.STROKE

        mPaintRectangle.style = Paint.Style.FILL
        mPaintRectangle.color = Color.BLACK

        val typedArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.FrameView, defStyleAttr, defStyleRes)

        try {
            val lineLength = typedArray?.getLayoutDimension(R.styleable.FrameView_line_length, dpToPx(16).toInt())
            if (lineLength != null) {
                mLineLength = lineLength.toFloat()
            }
            val lineWidth = typedArray?.getLayoutDimension(R.styleable.FrameView_line_width, dpToPx(2).toInt())
            if (lineWidth != null) {
                mPaintLine.strokeWidth = lineWidth.toFloat()
            }
            val lineColor = typedArray?.getColor(R.styleable.FrameView_line_color, Color.WHITE)
            if (lineColor != null) {
                mPaintLine.color = lineColor
            }
            val frameSize = typedArray?.getLayoutDimension(R.styleable.FrameView_frame_size, dpToPx(28).toInt())
            if (frameSize != null) {
                mFrameSize = frameSize.toFloat()
            }
            val frameAlpha = typedArray?.getInt(R.styleable.FrameView_frame_alpha, 125)
            if (frameAlpha != null) {
                mPaintRectangle.alpha = frameAlpha
            }
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

    fun setLineLength(dp: Int) {
        mLineLength = dpToPx(dp)
        requestLayout()
        invalidate()
    }

    fun setLineColor(color: Int) {
        mPaintLine.color = color
        requestLayout()
        invalidate()
    }

    fun setFrameSize(dp: Int) {
        mFrameSize = dpToPx(dp)
        requestLayout()
        invalidate()
    }

    fun setFrameAlpha(alpha: Int) {
        mPaintRectangle.alpha = alpha
        requestLayout()
        invalidate()
    }

    fun setLineWidth(dp: Int) {
        mPaintLine.strokeWidth = dpToPx(dp)
        requestLayout()
        invalidate()
    }

    private fun dpToPx(dp: Int): Float {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f)
    }

    private fun drawLowerLeftLines(canvas: Canvas) {
        canvas.drawLine(mFrameSize, mHeight - mLineLength - mFrameSize, mFrameSize, mHeight - mFrameSize, mPaintLine)
        canvas.drawLine(mFrameSize - mPaintLine.strokeWidth/2, mHeight - mFrameSize, mFrameSize + mLineLength, mHeight - mFrameSize, mPaintLine)
    }

    private fun drawLowerRightLines(canvas: Canvas) {
        canvas.drawLine(mWidth - mFrameSize, mHeight - mLineLength - mFrameSize, mWidth - mFrameSize, mHeight - mFrameSize, mPaintLine)
        canvas.drawLine(mWidth - mLineLength - mFrameSize, mHeight - mFrameSize, mWidth - mFrameSize  + mPaintLine.strokeWidth/2, mHeight - mFrameSize, mPaintLine)
    }

    private fun drawShadowRectangles(canvas: Canvas) {
        canvas.drawRect(0f, 0f, mFrameSize, mHeight, mPaintRectangle)
        canvas.drawRect(mWidth - mFrameSize, 0f, mWidth, mHeight, mPaintRectangle)
        canvas.drawRect(mFrameSize, 0f, mWidth - mFrameSize, mFrameSize, mPaintRectangle)
        canvas.drawRect(mFrameSize, mHeight - mFrameSize, mWidth - mFrameSize, mHeight, mPaintRectangle)
    }

    private fun drawUpperLeftLines(canvas: Canvas) {
        canvas.drawLine(mFrameSize, mFrameSize, mFrameSize, mFrameSize + mLineLength, mPaintLine)
        canvas.drawLine(mFrameSize - mPaintLine.strokeWidth/2, mFrameSize, mFrameSize + mLineLength, mFrameSize, mPaintLine)
    }

    private fun drawUpperRightLines(canvas: Canvas) {
        canvas.drawLine(mWidth - mFrameSize, mFrameSize, mWidth - mFrameSize, mFrameSize + mLineLength, mPaintLine)
        canvas.drawLine(mWidth - mLineLength - mFrameSize, mFrameSize, mWidth - mFrameSize  + mPaintLine.strokeWidth/2, mFrameSize, mPaintLine)
    }
}