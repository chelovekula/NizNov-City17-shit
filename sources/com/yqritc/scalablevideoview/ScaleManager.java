package com.yqritc.scalablevideoview;

import android.graphics.Matrix;

public class ScaleManager {
    private Size mVideoSize;
    private Size mViewSize;

    public ScaleManager(Size size, Size size2) {
        this.mViewSize = size;
        this.mVideoSize = size2;
    }

    public Matrix getScaleMatrix(ScalableType scalableType) {
        switch (scalableType) {
            case NONE:
                return getNoScale();
            case FIT_XY:
                return fitXY();
            case FIT_CENTER:
                return fitCenter();
            case FIT_START:
                return fitStart();
            case FIT_END:
                return fitEnd();
            case LEFT_TOP:
                return getOriginalScale(PivotPoint.LEFT_TOP);
            case LEFT_CENTER:
                return getOriginalScale(PivotPoint.LEFT_CENTER);
            case LEFT_BOTTOM:
                return getOriginalScale(PivotPoint.LEFT_BOTTOM);
            case CENTER_TOP:
                return getOriginalScale(PivotPoint.CENTER_TOP);
            case CENTER:
                return getOriginalScale(PivotPoint.CENTER);
            case CENTER_BOTTOM:
                return getOriginalScale(PivotPoint.CENTER_BOTTOM);
            case RIGHT_TOP:
                return getOriginalScale(PivotPoint.RIGHT_TOP);
            case RIGHT_CENTER:
                return getOriginalScale(PivotPoint.RIGHT_CENTER);
            case RIGHT_BOTTOM:
                return getOriginalScale(PivotPoint.RIGHT_BOTTOM);
            case LEFT_TOP_CROP:
                return getCropScale(PivotPoint.LEFT_TOP);
            case LEFT_CENTER_CROP:
                return getCropScale(PivotPoint.LEFT_CENTER);
            case LEFT_BOTTOM_CROP:
                return getCropScale(PivotPoint.LEFT_BOTTOM);
            case CENTER_TOP_CROP:
                return getCropScale(PivotPoint.CENTER_TOP);
            case CENTER_CROP:
                return getCropScale(PivotPoint.CENTER);
            case CENTER_BOTTOM_CROP:
                return getCropScale(PivotPoint.CENTER_BOTTOM);
            case RIGHT_TOP_CROP:
                return getCropScale(PivotPoint.RIGHT_TOP);
            case RIGHT_CENTER_CROP:
                return getCropScale(PivotPoint.RIGHT_CENTER);
            case RIGHT_BOTTOM_CROP:
                return getCropScale(PivotPoint.RIGHT_BOTTOM);
            case START_INSIDE:
                return startInside();
            case CENTER_INSIDE:
                return centerInside();
            case END_INSIDE:
                return endInside();
            default:
                return null;
        }
    }

    private Matrix getMatrix(float f, float f2, float f3, float f4) {
        Matrix matrix = new Matrix();
        matrix.setScale(f, f2, f3, f4);
        return matrix;
    }

    private Matrix getMatrix(float f, float f2, PivotPoint pivotPoint) {
        switch (pivotPoint) {
            case LEFT_TOP:
                return getMatrix(f, f2, 0.0f, 0.0f);
            case LEFT_CENTER:
                return getMatrix(f, f2, 0.0f, ((float) this.mViewSize.getHeight()) / 2.0f);
            case LEFT_BOTTOM:
                return getMatrix(f, f2, 0.0f, (float) this.mViewSize.getHeight());
            case CENTER_TOP:
                return getMatrix(f, f2, ((float) this.mViewSize.getWidth()) / 2.0f, 0.0f);
            case CENTER:
                return getMatrix(f, f2, ((float) this.mViewSize.getWidth()) / 2.0f, ((float) this.mViewSize.getHeight()) / 2.0f);
            case CENTER_BOTTOM:
                return getMatrix(f, f2, ((float) this.mViewSize.getWidth()) / 2.0f, (float) this.mViewSize.getHeight());
            case RIGHT_TOP:
                return getMatrix(f, f2, (float) this.mViewSize.getWidth(), 0.0f);
            case RIGHT_CENTER:
                return getMatrix(f, f2, (float) this.mViewSize.getWidth(), ((float) this.mViewSize.getHeight()) / 2.0f);
            case RIGHT_BOTTOM:
                return getMatrix(f, f2, (float) this.mViewSize.getWidth(), (float) this.mViewSize.getHeight());
            default:
                throw new IllegalArgumentException("Illegal PivotPoint");
        }
    }

    private Matrix getNoScale() {
        return getMatrix(((float) this.mVideoSize.getWidth()) / ((float) this.mViewSize.getWidth()), ((float) this.mVideoSize.getHeight()) / ((float) this.mViewSize.getHeight()), PivotPoint.LEFT_TOP);
    }

    private Matrix getFitScale(PivotPoint pivotPoint) {
        float width = ((float) this.mViewSize.getWidth()) / ((float) this.mVideoSize.getWidth());
        float height = ((float) this.mViewSize.getHeight()) / ((float) this.mVideoSize.getHeight());
        float min = Math.min(width, height);
        return getMatrix(min / width, min / height, pivotPoint);
    }

    private Matrix fitXY() {
        return getMatrix(1.0f, 1.0f, PivotPoint.LEFT_TOP);
    }

    private Matrix fitStart() {
        return getFitScale(PivotPoint.LEFT_TOP);
    }

    private Matrix fitCenter() {
        return getFitScale(PivotPoint.CENTER);
    }

    private Matrix fitEnd() {
        return getFitScale(PivotPoint.RIGHT_BOTTOM);
    }

    private Matrix getOriginalScale(PivotPoint pivotPoint) {
        return getMatrix(((float) this.mVideoSize.getWidth()) / ((float) this.mViewSize.getWidth()), ((float) this.mVideoSize.getHeight()) / ((float) this.mViewSize.getHeight()), pivotPoint);
    }

    private Matrix getCropScale(PivotPoint pivotPoint) {
        float width = ((float) this.mViewSize.getWidth()) / ((float) this.mVideoSize.getWidth());
        float height = ((float) this.mViewSize.getHeight()) / ((float) this.mVideoSize.getHeight());
        float max = Math.max(width, height);
        return getMatrix(max / width, max / height, pivotPoint);
    }

    private Matrix startInside() {
        if (this.mVideoSize.getHeight() > this.mViewSize.getWidth() || this.mVideoSize.getHeight() > this.mViewSize.getHeight()) {
            return fitStart();
        }
        return getOriginalScale(PivotPoint.LEFT_TOP);
    }

    private Matrix centerInside() {
        if (this.mVideoSize.getHeight() > this.mViewSize.getWidth() || this.mVideoSize.getHeight() > this.mViewSize.getHeight()) {
            return fitCenter();
        }
        return getOriginalScale(PivotPoint.CENTER);
    }

    private Matrix endInside() {
        if (this.mVideoSize.getHeight() > this.mViewSize.getWidth() || this.mVideoSize.getHeight() > this.mViewSize.getHeight()) {
            return fitEnd();
        }
        return getOriginalScale(PivotPoint.RIGHT_BOTTOM);
    }
}
