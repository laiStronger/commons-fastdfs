package com.youanmi.img.dto;

/**
 * 图片上传裁剪参数
 * <p>
 * 详细描述
 *
 * @author liubing on 2017/2/15
 */
public class ImageCutDto {

    private String url;//图片url

    private int positionx;// x坐标

    private int positiony;// y坐标

    private int width; //宽

    private int height;//长

    private String base64; //图片base64编码，与url二选一

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPositionx() {
        return positionx;
    }

    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    public int getPositiony() {
        return positiony;
    }

    public void setPositiony(int positiony) {
        this.positiony = positiony;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
