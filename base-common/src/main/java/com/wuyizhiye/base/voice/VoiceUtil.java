package com.wuyizhiye.base.voice;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 音频文件工具类
 * @author li.biao 
 * @since 2014-4-21
 */
public class VoiceUtil {
	private final static Logger logger = Logger.getLogger(VoiceUtil.class);
	private static final int EXTERNAL_BUFFER_SIZE = 10000;//128000;

	public void instance(){

	}
	
	/**
	 * 音频文件复制
	 * @param surl 源路径
	 * @param ourl 目标路径
	 * @throws Exception
	 */
	public static void copy(String surl,String ourl) throws Exception{
		if(StringUtils.isEmpty(surl)){
			throw new Exception("surl.empty");
		}
		if(StringUtils.isEmpty(ourl)){
			throw new Exception("ourl.empty");
		}
		URL swwwurl = new URL(surl);
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(swwwurl);
		if(audioInputStream == null){
			throw new Exception("audio.inputstream.null");
		}
		// 写入文件
		File file = new File(ourl);
		AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);
	}
	
	/**
	 * 音频文件播放
	 * @param surl 文件路径
	 * @param pathtype 路径类型{local表示本地文件，url表示互联网文件}
	 */
	public static void play(String surl,String pathtype) throws Exception{
		
		//数据缓冲区
		byte tempBuffer[] = new byte[EXTERNAL_BUFFER_SIZE];
		
		//从文件路径取得音频文件流信息
		AudioInputStream audioInputStream = null;
		if("local".equals(pathtype)){
			File soundFile = new File(surl);
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		}else{
			URL soundUrl = new URL(surl);
			audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
		}
		
		SourceDataLine line = null;
			
		//通过上述取得的音频信息实例数据播放线路
		AudioFormat audioFormat = audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		line = (SourceDataLine) AudioSystem.getLine(info);
		
		//打开线路，接收音频数据
		line.open(audioFormat);
		
		//激活，将音频输出到声卡设备
		line.start();
		
		//将音频文件流中写入缓冲数据
		int restBytes = 0;
		while (restBytes != -1) {
			try {
				restBytes = audioInputStream.read(tempBuffer, 0, tempBuffer.length);
			} catch (IOException e) {
				restBytes = -1 ;
//				e.printStackTrace();
				logger.error("", e);
			}
			if (restBytes >= 0) {
				line.write(tempBuffer, 0, restBytes);
			}
		}
		
		//等待数据播放，如果不等待，会打断播放过早关闭和退出虚拟机
		line.drain();
		
		//所有数据播放完，关闭
		line.close();
	}

	/**
	 *  取得 AudioFormat 默认设置
	 * @return
	 */
	private AudioFormat getAudioFormat() {
		//每秒样本数。即取样率，表示每一秒钟取样的频率，可选值有8000、11025、16000、22050、44100。
		//比如对于8000，表示每一秒钟会取样8000次，也就是采集8000次声音。
		float sampleRate = 8000.0F;
		//每个样本中的位数。可以为8bit和16bit，即每一个声音样本使用8bit或16bit数据表示。
		int sampleSizeInBits = 16;
		//声道数（单声道为 1，立体声为2……）。
		int channels = 2;
		//
		boolean signed = true;
		//指是否以big-endian字节顺序存储数据（false意味着little-endian）。
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	public static void main(String args[]) {
		//http://110.84.128.78:8088/voice/Dial/common_djbh/Dial_20140421151202_8618128810580_8618128810567_59997134449833202771929018240065.wav 
		String surl = "http://110.84.128.78:8088/voice/Dial/common_djbh/Dial_20140421151202_8618128810580_8618128810567_59997134449833202771929018240065.wav" ;
		String ourl = "D:/temp/test.wav" ;
		VoiceUtil vutil = new VoiceUtil();
		try {
			//vutil.copy(surl, ourl);
			vutil.play(surl, "url");
			//vutil.play(ourl, "local");
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("", e);
		}
	}

}
