package com.wuyizhiye.cmct.phone.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.model.PhoneCosDetail;
import com.wuyizhiye.cmct.phone.service.PhoneCosDetailService;
import com.wuyizhiye.cmct.utils.ExcelReader;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.framework.util.Constant;

/**
 * @ClassName PhoneCosDetailListController
 * @Description 话费明细
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCosDetail/*")
public class PhoneCosDetailListController extends ListController {

	@Autowired
	private PhoneCosDetailService phoneCosDetailService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new PhoneCosDetail();
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "cmct/phone/phoneCosDetailList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "cmct/phone/phoneCosDetailEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.cmct.phone.dao.PhoneCosDetailDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneCosDetailService;
	}

	/**
	 * 导入数据页面
	 * @return
	 */
	@RequestMapping("importData")
	public String getImportDataView(ModelMap modelMap){
		modelMap.put("orgId", SystemUtil.getCurrentControlUnit() == null ? "" : SystemUtil.getCurrentControlUnit().getId());
		modelMap.put("orgName", SystemUtil.getCurrentControlUnit() == null ? "" : SystemUtil.getCurrentControlUnit().getName());
		return "cmct/phone/phoneImportData" ;
	}
	
	/**
	 * 导入数据
	 * @throws Exception 
	 */
	@RequestMapping("saveImportData")
	public void saveImportData(@RequestParam(value="filePath")MultipartFile file,
			HttpServletRequest request,HttpServletResponse response) {
		
		
		if(!file.isEmpty()){
			int rowNum = 1 ;
			try{
				String originalFileName  = file.getOriginalFilename();

				Map<String,Object> queryMap = new HashMap<String,Object>();
				if(!originalFileName.endsWith(".xls") && !originalFileName.endsWith(".xlsx")){
					getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
					getOutputMsg().put(Constant.RESULT_MSG_KEY, "导入文件格式不对，请使用.xls或者.xlsx模板文件");
				}else{
					//读取Excel数据
					List<String[]> excelData = ExcelReader.getExcelData(file.getInputStream(), 1,originalFileName.endsWith(".xls"));
					if(excelData!=null && excelData.size()>0){				
				
						List<PhoneCosDetail> addList = new ArrayList<PhoneCosDetail>();
						List<PhoneCosDetail> updateList = new ArrayList<PhoneCosDetail>();
							for(int i = 0 ; i < excelData.size() ; i ++ ){
								String[] data =  excelData.get(i);
								
								PhoneCosDetail phoneCosDetail = new PhoneCosDetail();
								boolean modifyFlag = false ;
								/**
								 * 判断数据是否重复
								 */
								String month=data[0].replace(".0", "");
								String fixNumber=data[1];
								if(!StringUtils.isEmpty(month)){
									queryMap.put("month", month);
									queryMap.put("fixNumber", fixNumber);
									List<PhoneCosDetail> queryResult=queryExecutor.execQuery(getListMapper(), queryMap, PhoneCosDetail.class);
									if(queryResult!=null && queryResult.size()>0){
										phoneCosDetail=queryResult.get(0);
										modifyFlag=true;
									}
								}
								phoneCosDetail.setPeriod(data[0].replace(".0", ""));
								phoneCosDetail.setFixNumber(data[1]);
								phoneCosDetail.setTotalCost(new BigDecimal(data[2]==null || data[2]=="" ? "0.00":data[2]));
								phoneCosDetail.setLocalPhoneCost(new BigDecimal(data[3]==null || data[3]=="" ? "0.00":data[3]));
								phoneCosDetail.setLocalTelCost(new BigDecimal(data[4]==null || data[4]=="" ? "0.00":data[4]));
								phoneCosDetail.setLongCost(new BigDecimal(data[5]==null || data[5]=="" ? "0.00":data[5]));
								phoneCosDetail.setSosCost(new BigDecimal(data[6]==null || data[6]=="" ? "0.00":data[6]));
								phoneCosDetail.setSmsCost(new BigDecimal(data[7]==null || data[7]=="" ? "0.00":data[7]));
								phoneCosDetail.setHideOutCost(new BigDecimal(data[8]==null || data[8]=="" ? "0.00":data[8]));
								phoneCosDetail.setCostSum(new BigDecimal(data[9]==null || data[9]=="" ? "0.00":data[9]));
								phoneCosDetail.setMonthCostSum(new BigDecimal(data[10]==null || data[10]=="" ? "0.00":data[10]));
								phoneCosDetail.setOffsetSum(new BigDecimal(data[11]==null || data[11]=="" ? "0.00":data[11]));
								rowNum ++ ;
								if(modifyFlag){
									updateList.add(phoneCosDetail);
								}else{
									addList.add(phoneCosDetail);
								}
							}
							if(addList.size()>0){
								phoneCosDetailService.addBatch(addList);
								getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_SUCCESS);
								getOutputMsg().put(Constant.RESULT_MSG_KEY, "导入数据成功");
							}
							if(updateList.size()>0){
								phoneCosDetailService.updateBatch(updateList);
								getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_SUCCESS);
								getOutputMsg().put(Constant.RESULT_MSG_KEY, "更新数据成功");
							}
							if(addList.size()+ updateList.size() ==0){
								getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
								getOutputMsg().put(Constant.RESULT_MSG_KEY, "文件没有数据");
							}
					}else{
						getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
						getOutputMsg().put(Constant.RESULT_MSG_KEY, "导入数据为空");
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
				getOutputMsg().put(Constant.RESULT_MSG_KEY, "导入数据出现异常，rowNum = "+rowNum+" 信息："+e.getMessage());
			}
		}else{
			getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
			getOutputMsg().put(Constant.RESULT_MSG_KEY, "文件路径不存在");
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
		
	}
	@RequestMapping(value="getOffsetCost")
	public void getOffsetCost(HttpServletResponse response) throws ParseException{
		Map<String,Object> param = getListDataParam() ;
		Map<String,Object> queryResult=queryExecutor.execOneEntity("com.wuyizhiye.cmct.phone.dao.PhoneCosDetailDao.selectMonthCostAndOffsetCost", param, Map.class);
		getOutputMsg().put("offsetCost", (queryResult == null || queryResult.get("offsetSum") == null ) ? 0.00 : queryResult.get("offsetSum"));
		getOutputMsg().put("monthCostSum", (queryResult == null || queryResult.get("monthCostSum") == null ) ? 0.00 : queryResult.get("monthCostSum"));
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
