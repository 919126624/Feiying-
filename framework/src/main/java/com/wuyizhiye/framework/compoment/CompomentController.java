package com.wuyizhiye.framework.compoment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName CompomentController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="framework/normal/*")
public class CompomentController extends BaseController{
	
	@RequestMapping(value="compoment")
	public String compoment(){
		return "common/compoments";
	}
	
	@RequestMapping(value="page")
	public String showpage(){
		return "common/normalPage";
	}
	
	@RequestMapping(value="listPage")
	public String listPage(){
		return "normal/listPage";
	}
	
	@RequestMapping(value="editPage")
	public String editPage(){
		return "normal/editPage";
	}
	
	@RequestMapping(value="editPageNormal")
	public String editPageNormal(){
		return "normal/editPageNormal";
	}
	
	@RequestMapping(value="treeListPage")
	public String treeListPage(){
		return "normal/treeListPage";
	}
	
	@RequestMapping(value="weiboPage")
	public String weiboPage(){
		return "normal/weiboPage";
	}
	
	@RequestMapping(value="zsybPage")
	public String zsybPage(){
		return "normal/zsybPage";
	}
	
	@RequestMapping(value="indexPage")
	public String indexPage(){
		return "normal/indexPage";
	}
	
	@RequestMapping(value="commentPage")
	public String commentPage(){
		return "normal/commentPage";
	}
	
	@RequestMapping(value="phone")
	public String phone(){
		return "normal/phone";
	}
	
	@RequestMapping(value="customCompPage")
	public String customComp(){
		return "normal/customCompPage";
	}
	
	@RequestMapping(value="jscompress")
	public String jscompress(){
		return "normal/jscompress";
	}
	
	@RequestMapping(value="videodemo")
	public String videodemo(){
		return "normal/videodemo";
	}
	
	@RequestMapping(value="phoneCustomCompPage")
	public String phoneCustomCompPage(){
		return "normal/phoneCustomCompPage";
	}
	
	@RequestMapping(value="demo1")
	public String demo1(){
		return "mobile/demo/demo1";
	}
	
	@RequestMapping(value="demo2")
	public String demo2(){
		return "mobile/demo/demo2";
	}
	
	@RequestMapping(value="demo3")
	public String demo3(){
		return "mobile/demo/demo3";
	}
}
