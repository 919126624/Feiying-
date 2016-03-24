package com.wuyizhiye.basedata.util;
  
import java.awt.image.BufferedImage;

import jp.sourceforge.qrcode.data.QRCodeImage;
 
/**
 * @ClassName TwoDimensionCodeImage
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class TwoDimensionCodeImage implements QRCodeImage {  
  
    BufferedImage bufImg;  
      
    public TwoDimensionCodeImage(BufferedImage bufImg) {  
        this.bufImg = bufImg;  
    }  
      
    @Override  
    public int getHeight() {  
        return bufImg.getHeight();  
    }  
  
    @Override  
    public int getPixel(int x, int y) {  
        return bufImg.getRGB(x, y);  
    }  
  
    @Override  
    public int getWidth() {  
        return bufImg.getWidth();  
    }  
  
}  