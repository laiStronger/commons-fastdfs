package com.youanmi.img.dto;

/**
 * 图片地址信息
 * <p>
 * 详细描述
 *
 * @author liubing on 2017/2/20
 */
public class ImageUrlDto {
    /** 原图 */
    private String originImageUrl;

    /** 缩略图 */
    private String thumImageUrl;


    public String getOriginImageUrl() {
        return originImageUrl;
    }


    public void setOriginImageUrl(String originImageUrl) {
        this.originImageUrl = originImageUrl;
    }


    public String getThumImageUrl() {
        return thumImageUrl;
    }


    public void setThumImageUrl(String thumImageUrl) {
        this.thumImageUrl = thumImageUrl;
    }

}