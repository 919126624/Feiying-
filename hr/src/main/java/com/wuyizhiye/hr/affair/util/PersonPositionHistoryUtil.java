package com.wuyizhiye.hr.affair.util;

import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * 任职历史工具类  主要用于构建任职历史里面的信息
 * @author taking.wang
 *
 */
public class PersonPositionHistoryUtil {

	/**
	 * 构建任职记录字符串
	 * @param vo
	 * @return
	 */
	public static String conbineOndutyRecord(PersonPositionHistory vo){
		String result = "<li>"+DateUtil.convertDateToStr(vo.getEffectdate())+"：</li>";
		if(PositionChangeTypeEnum.RUNDISK.toString()
				.equalsIgnoreCase(vo.getChangeType())){//新增跑盘
			return result+"<li>任职为 <b style='font-weight:bold;'><font color='red'>"+vo.getChangeOrgName()+"</b></font></li><li>部门的跑盘员</li>";
		} else if(PositionChangeTypeEnum.DELRUNDISK.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//删除跑盘
			return result+"<li>从 <b style='font-weight:bold;'><font color='red'>"+vo.getOldOrgName()+"</b></font></li><li>部门跑盘员删除</li>";
		} else if(PositionChangeTypeEnum.ENROLL.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//入职
			return result+"<li>入职 部门：<b style='font-weight:bold;'><font color='red'>"+vo.getChangeOrgName()+"</b></font></li><li>职位：<b style='font-weight:bold;'><font color='red'>"+vo.getChangePositionName()+"</b></font></li><li>职级：<b style='font-weight:bold;'><font color='red'>"+vo.getChangeJobLevelName()+"</b></font></li>";
		} else if(PositionChangeTypeEnum.TRANSFER.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//调职
			return result+"<li>由 部门：<b style='font-weight:bold;'><font color='red'>"+vo.getOldOrgName()+"</b></font></li><li>职位：<b style='font-weight:bold;'><font color='red'>"+vo.getOldPositionName()+"</b></font></li><li>职级：<b style='font-weight:bold;'><font color='red'>"+vo.getOldJobLevelName()+"</b></font></li><li>调动到部门：<b style='font-weight:bold;'><font color='red'>"+
			 vo.getChangeOrgName()+"</b></font></li><li>职位：<b style='font-weight:bold;'><font color='red'>"+vo.getChangePositionName()+"</b></font></li><li>职级：<b style='font-weight:bold;'><font color='red'>"+vo.getChangeJobLevelName()+"</b></font></li>";
		} else if(PositionChangeTypeEnum.PROMOTION.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//晋升
			return result+"<li>由 部门：<b style='font-weight:bold;'><font color='red'>"+vo.getOldOrgName()+"</b></font></li><li>职位：<b style='font-weight:bold;'><font color='red'>"+vo.getOldPositionName()+"</b></font></li><li>职级：<b style='font-weight:bold;'><font color='red'>"+vo.getOldJobLevelName()+"</b></font></li><li>晋升为 部门：<b style='font-weight:bold;'><font color='red'>"+
						 vo.getChangeOrgName()+"</b></font></li><li>职位：<b style='font-weight:bold;'><font color='red'>"+vo.getChangePositionName()+"</b></font></li><li>职级：<b style='font-weight:bold;'><font color='red'>"+vo.getChangeJobLevelName()+"</b></font></li>";
		} else if(PositionChangeTypeEnum.DEMOTION.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//降职
			return result+"<li>由 部门：<b style='font-weight:bold;'><font color='red'>"+vo.getOldOrgName()+"</b></font></li><li>职位： <b style='font-weight:bold;'><font color='red'>"+vo.getOldPositionName()+"</b></font></li><li>职级： <b style='font-weight:bold;'><font color='red'>"+vo.getOldJobLevelName()+"</b></font></li><li>降职为 部门：<b style='font-weight:bold;'><font color='red'>"+
			 vo.getChangeOrgName()+"</b></font></li><li>职位： <b style='font-weight:bold;'><font color='red'>"+vo.getChangePositionName()+"</b></font></li><li>职级： <b style='font-weight:bold;'><font color='red'>"+vo.getChangeJobLevelName()+"</b></font></li>";
		}else if(PositionChangeTypeEnum.INCREASE_PARTTIMEJOB.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//兼职
			return result+"<li>兼职 部门：<b style='font-weight:bold;'><font color='red'>"+vo.getChangeOrgName()+"</b></font></li><li>职位： <b style='font-weight:bold;'><font color='red'>"+vo.getChangePositionName()+"</b></font></li><li>职级：<b style='font-weight:bold;'><font color='red'>"+vo.getChangeJobLevelName()+"</b></font></li>";
		}else if(PositionChangeTypeEnum.DISMISS_PARTTIMEJOB.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//撤职
			return result+"<li>撤销 部门：<b style='font-weight:bold;'><font color='red'>"+vo.getOldOrgName()+"</b></font></li><li>职位： <b style='font-weight:bold;'><font color='red'>"+vo.getOldPositionName()+"</b></font></li><li>职级： <b style='font-weight:bold;'><font color='red'>"+vo.getOldJobLevelName()+"</b></font> 兼职</li>";
		}else if(PositionChangeTypeEnum.LEAVE.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//离职
			return result+"<li>从 部门：<b style='font-weight:bold;'><font color='red'>"+vo.getOldOrgName()+"</b></font>离职</li><li>职位：<b style='font-weight:bold;'><font color='red'>"+vo.getOldPositionName()+"</b></font></li><li>职级：<b style='font-weight:bold;'><font color='red'>"+vo.getOldJobLevelName()+"</b></font></li>";
		}else if(PositionChangeTypeEnum.POSITIVE.toString()
				.equalsIgnoreCase(vo.getChangeType())){	//转正
			return result+"<li>转正</li>";
		} else {		//复职
			return result+"<li>从部门：<b style='font-weight:bold;'><font color='red'>"+vo.getOldOrgName()+"</b></font>的见习员工</li><li>复职为 部门：<b style='font-weight:bold;'><font color='red'>"+vo.getChangeOrgName()+"</b></font></li><li>职位：<b style='font-weight:bold;'><font color='red'>"+vo.getChangePositionName()+"</b></font></li><li>职级： <b style='font-weight:bold;'><font color='red'>"+vo.getChangeJobLevelName()+"</b></font></li>";
		}
	}
}
