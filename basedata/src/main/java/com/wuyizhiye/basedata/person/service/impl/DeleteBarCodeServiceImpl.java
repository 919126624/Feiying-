package com.wuyizhiye.basedata.person.service.impl;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.notetemplate.service.impl.NoteTemplateServiceImpl;
import com.wuyizhiye.basedata.person.service.DeleteBarCodeService;

/**
 * @ClassName DeleteBarCodeServiceImpl
 * @author lhh
 * @date 2015-10-3
 * 每天删除生成的一维条形码
 */
@Component(value="DeleteBarCodeService")
@Transactional
public  class DeleteBarCodeServiceImpl implements DeleteBarCodeService {
	private static Logger logger=Logger.getLogger(DeleteBarCodeServiceImpl.class);

	@Override
	public void execute(){
//			System.out.println("----------------删除生成的一维条形码-----------------"); 
			String fileName=SystemConfig.getParameter("image_path")+"barCode";
		    deleteAllFile(fileName); // 删除完里面所有内容
		    java.io.File myFilePath = new java.io.File(fileName);
		    myFilePath.delete(); // 删除空文件夹
	}
	
		 public static void delFolder(String folderPath) {
			   try {
				deleteAllFile(folderPath); // 删除完里面所有内容
			    String filePath = folderPath;
			    filePath = filePath.toString();
			    java.io.File myFilePath = new java.io.File(filePath);
			    myFilePath.delete(); // 删除空文件夹
			   } catch (Exception e) {
				   logger.error("", e);
			   }
			}

			// 删除指定文件夹下所有文件
			public static boolean deleteAllFile(String path) {
			   boolean flag = false;
			   File file = new File(path);
			   if (!file.exists()) {
			    return flag;
			   }
			   if (!file.isDirectory()) {
			    return flag;
			   }
			   String[] tempList = file.list();
			   File temp = null;
			   for (int i = 0; i < tempList.length; i++) {
				   		if (path.endsWith(File.separator)) {
				   		temp = new File(path + tempList[i]);
				   	} else {
				   		temp = new File(path + File.separator + tempList[i]);
				   	}
				   	if (temp.isFile()) {
				   		temp.delete();
				   	}
				   	if (temp.isDirectory()) {
					     deleteAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
					     delFolder(path + "/" + tempList[i]);// 再删除空文件夹
					     flag = true;
				   	}
			   	}
			   return flag;
			}
	}
