package com.wuyizhiye.basedata.topic.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.topic.enums.TopicLevelEnum;
import com.wuyizhiye.basedata.topic.enums.TopicStatusEnum;
import com.wuyizhiye.basedata.topic.enums.TopicTypeEnum;
import com.wuyizhiye.basedata.topic.model.DealHistory;
import com.wuyizhiye.basedata.topic.model.Topic;
import com.wuyizhiye.basedata.topic.model.TopicComment;
import com.wuyizhiye.basedata.topic.service.TopicService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName TopicNewListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/topicNewList/*")
public class TopicNewListController extends ListController {
	@Autowired
	private TopicService topicService;

	@Override
	protected CoreEntity createNewEntity() {
		Topic topic = new Topic();
		topic.setStatus(TopicStatusEnum.STATE_1);
		return topic;
	}

	@Override
	protected String getListView() {
		Map<String, Object> param = new HashMap<String, Object>();
		List<Menu> modules = this.queryExecutor.execQuery(
				"com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param,
				Menu.class);
		getRequest().setAttribute("modules", modules);
		param.put("parent", modules.get(0).getId());
		List<Menu> functions = queryExecutor.execQuery(
				"com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param,
				Menu.class);
		getRequest().setAttribute("functions", functions);
		return "basedata/topic/topicNewList";
	}

	/**
	 * 列表数据
	 * 
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value = "listData")
	@Dependence(method = "list")
	public void listData(Pagination<?> pagination, HttpServletResponse response) {
		pagination = queryExecutor.execQuery(getListMapper(), pagination,
				getListDataParam());
		for (int i = 0; i < pagination.getItems().size(); i++) {
			Topic topic = (Topic) pagination.getItems().get(i);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("belong", topic.getId());
			List<PhotoAlbum> albums = queryExecutor.execQuery(
					"com.wuyizhiye.basedata.images.dao.PhotoAlbumDao.select",
					param, PhotoAlbum.class);
			if (albums.size() > 0) {
				param.clear();
				param.put("album", albums.get(0).getId());
				List<Photo> photos = queryExecutor.execQuery(
						"com.wuyizhiye.basedata.images.dao.PhotoDao.select",
						param, Photo.class);
				if (photos.size() > 0) {
					topic.setHasPhoto(true);
				}
			}
		}
		afterFetchListData(pagination);
		outPrint(response,
				JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}

	@Override
	public String add(ModelMap model) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("emptyLink", true);
		List<Menu> modula = queryExecutor.execQuery(
				"com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param,
				Menu.class);
		getRequest().setAttribute("modula", modula);
		param.clear();
		param.put("parent", modula.get(0).getId());
		List<Menu> functions = queryExecutor.execQuery(
				"com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param,
				Menu.class);
		getRequest().setAttribute("functions", functions);
		getRequest().setAttribute("types", TopicTypeEnum.values());
		getRequest().setAttribute("levels", TopicLevelEnum.values());
		model.put("topicId", UUID.randomUUID().toString());
		return super.add(model);
	}

	@Override
	public String edit(ModelMap model, String id) {
		Topic topic = getService().getEntityById(id);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("emptyLink", true);
		List<Menu> modula = queryExecutor.execQuery(
				"com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param,
				Menu.class);
		getRequest().setAttribute("modula", modula);
		param.clear();
		param.put("parent", topic.getBelongFunction().getParent().getId());
		List<Menu> functions = queryExecutor.execQuery(
				"com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param,
				Menu.class);
		getRequest().setAttribute("functions", functions);
		model.put("data", topic);
		getRequest().setAttribute("types", TopicTypeEnum.values());
		getRequest().setAttribute("levels", TopicLevelEnum.values());
		param.clear();
		param.put("belong", id);
		List<PhotoAlbum> albums = queryExecutor.execQuery(
				"com.wuyizhiye.basedata.images.dao.PhotoAlbumDao.select", param,
				PhotoAlbum.class);
		if (albums.size() > 0) {
			param.clear();
			param.put("album", albums.get(0).getId());
			List<Photo> photos = queryExecutor.execQuery(
					"com.wuyizhiye.basedata.images.dao.PhotoDao.select", param,
					Photo.class);
			getRequest().setAttribute("photos", photos);
		}

		// 将当前状态放入到界面 addded by taking.wang 2013-02-22
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.topic.dao.TopicDao.selectAllTopic";
	}

	@Override
	protected BaseService<Topic> getService() {
		return topicService;
	}

	@Override
	protected String getEditView() {
		return "basedata/topic/topicNewEdit";
	}

	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String, Object> param = super.getListDataParam();
		// 问题状态
		if (param.get("status") != null && !"".equals(param.get("status"))) {
			String status = param.get("status").toString();
			status = "'" + status + "'";
			status = status.replaceAll(";", "','");
			param.put("status", status);
		}
		if (param.get("tabkey") != null && !"".equals(param.get("tabkey"))) {
			if (param.get("tabkey").equals("MYQ")) {
				param.put("curuser", SystemUtil.getCurrentUser().getId());
			}
		}
		return param;
	}

	@RequestMapping(value = "deal")
	public String deal(@RequestParam(value = "id", required = true) String id,
			ModelMap model) {
		Topic topic = getService().getEntityById(id);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("topic", id);
		List<DealHistory> his = queryExecutor
				.execQuery(
						"com.wuyizhiye.basedata.topic.dao.DealHistoryDao.select",
						param, DealHistory.class);
		model.put("topic", topic);
		model.put("history", his);
		param.clear();
		param.put("belong", id);
		List<PhotoAlbum> albums = queryExecutor.execQuery(
				"com.wuyizhiye.basedata.images.dao.PhotoAlbumDao.select", param,
				PhotoAlbum.class);
		if (albums.size() > 0) {
			param.clear();
			param.put("album", albums.get(0).getId());
			List<Photo> photos = queryExecutor.execQuery(
					"com.wuyizhiye.basedata.images.dao.PhotoDao.select", param,
					Photo.class);
			getRequest().setAttribute("photos", photos);
		}
		if (hasPermission("bb06d96b-c94b-4f1b-b37b-ff49607df0b8")) {
			getRequest().setAttribute("hasPermission", true);
		}
		return "basedata/topic/topicView";
	}

	@RequestMapping(value = "getTopicCommentData")
	@ResponseBody
	public Map<String, Object> getTopicCommentData(HttpServletResponse response) {
		// 获取问题反馈的评论
		Map<String, Object> tcParam = new HashMap<String, Object>();
		tcParam.put("topicid", getString("topicid"));
		List<TopicComment> topicCommentList = this.queryExecutor.execQuery(
				"com.wuyizhiye.basedata.topic.dao.TopicCommentDao.getByTopic",
				tcParam, TopicComment.class);
		tcParam.put("items", topicCommentList);
		SimpleDateFormat   sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < topicCommentList.size(); i++) {
			TopicComment  tc= topicCommentList.get(i);
			tc.setFormatDate(sdf.format(tc.getCreateTime()));
		}
		return tcParam;
	}

	@RequestMapping(value = "childMenu")
	public void getChildMenu(@RequestParam(value = "parent") String parent,
			HttpServletResponse response) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parent", parent);
		List<Menu> modula = queryExecutor.execQuery(
				"com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param,
				Menu.class);
		outPrint(response, JSONArray.fromObject(modula, getDefaultJsonConfig()));
	}

	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(TopicTypeEnum.class,
				new JsonValueProcessor() {
					public Object processObjectValue(String key, Object value,
							JsonConfig cfg) {
						if (value != null && value instanceof TopicTypeEnum) {
							JSONObject json = new JSONObject();
							json.put("label",
									((TopicTypeEnum) value).getLabel());
							json.put("value",
									((TopicTypeEnum) value).getValue());
							return json;
						}
						return null;
					}

					public Object processArrayValue(Object value, JsonConfig cfg) {
						return null;
					}
				});
		config.registerJsonValueProcessor(TopicLevelEnum.class,
				new JsonValueProcessor() {
					public Object processObjectValue(String key, Object value,
							JsonConfig cfg) {
						if (value != null && value instanceof TopicLevelEnum) {
							JSONObject json = new JSONObject();
							json.put("label",
									((TopicLevelEnum) value).getLabel());
							json.put("value",
									((TopicLevelEnum) value).getValue());
							return json;
						}
						return null;
					}

					public Object processArrayValue(Object value, JsonConfig cfg) {
						return null;
					}
				});
		config.registerJsonValueProcessor(TopicStatusEnum.class,
				new JsonValueProcessor() {
					public Object processObjectValue(String key, Object value,
							JsonConfig cfg) {
						if (value != null && value instanceof TopicStatusEnum) {
							JSONObject json = new JSONObject();
							json.put("label",
									((TopicStatusEnum) value).getLabel());
							json.put("value",
									((TopicStatusEnum) value).getValue());
							return json;
						}
						return null;
					}

					public Object processArrayValue(Object value, JsonConfig cfg) {
						return null;
					}
				});
		return config;
	}
}
