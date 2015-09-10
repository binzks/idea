package com.idea.cms.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idea.cms.controller.AbstractController;
import com.idea.cms.support.ModulePermission;
import com.idea.common.view.View;
import com.idea.framework.jdbc.support.model.Filter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends AbstractController {

//	private String modulePrefix = "@m@";
//	private String columnPrefix = "@c@";
//	private String actionPrefix = "@a@";
//
//	@RequestMapping(value = "/empower{mid}-{id}")
//	public String authorization(@PathVariable String mid, @PathVariable String id, Model model,
//			HttpServletRequest request, HttpServletResponse response) {
//		try {
//			ModulePermission modulePermission = getModulePermission(mid, request);
//			View view = modulePermission.getView();
//			List<Filter> filters = new ArrayList<Filter>();
//			filters.add(new Filter(view.getPkName(), "=", id));
//			Map<String, Object> map = view.selectMap(filters);
//			model.addAttribute("mid", mid);
//			model.addAttribute("id", id);
//			model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
//			model.addAttribute("code", map.get("code"));
//			model.addAttribute("name", map.get("name"));
//			return "/system/role/empower";
//		} catch (Exception e) {
//			model.addAttribute("msg", "错误[" + e + "]");
//			return "/system/error";
//		}
//	}
//
//	@RequestMapping(value = "/savepower{mid}-{id}")
//	public String savePower(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
//			HttpServletResponse response) {
//		try {
//			String moduleChecked = request.getParameter("moduleChecked");
//			String columnUnChecked = request.getParameter("columnUnChecked");
//			String actionUnChecked = request.getParameter("actionUnChecked");
//			String moduleIds = moduleChecked.replace(modulePrefix, "");
//			// 更新角色模块权限
//			if (StringUtils.isNotBlank(moduleIds)) {
//				Map<String, Object> roleMap = new HashMap<>();
//				roleMap.put("id", id);
//				roleMap.put("moduleids", moduleIds);
//				roleMap.put("modifytime", System.currentTimeMillis() / 1000);
//				Think.getModel("model_sys_role").update(roleMap);
//			}
//			// 删除原先行列权限
//			org.thinkframework.model.Model columnModel = Think.getModel("model_sys_role_column");
//			org.thinkframework.model.Model actionModel = Think.getModel("model_sys_role_action");
//			List<Filter> filters = new ArrayList<Filter>();
//			filters.add(new Filter("parentid", "=", id));
//			columnModel.delete(filters);
//			actionModel.delete(filters);
//			// 保存角色列权限
//			if (StringUtils.isNotBlank(columnUnChecked)) {
//				List<Map<String, Object>> roleColumns = new ArrayList<>();
//				String[] columns = columnUnChecked.split(",");
//				for (int i = 0; i < columns.length; i++) {
//					String[] one = columns[i].split(columnPrefix);
//					if (one.length == 2) {
//						String moduleId = one[0];
//						if (StringUtils.contains(moduleIds, "," + moduleId + ",")) {
//							Map<String, Object> roleColumn = new HashMap<>();
//							roleColumn.put("parentid", id);
//							roleColumn.put("moduleid", moduleId);
//							roleColumn.put("value", one[1]);
//							roleColumns.add(roleColumn);
//						}
//					}
//				}
//				columnModel.batchInsert(roleColumns);
//			}
//			// 保存角色操作权限
//			if (StringUtils.isNotBlank(actionUnChecked)) {
//				List<Map<String, Object>> roleActions = new ArrayList<>();
//				String[] actions = actionUnChecked.split(",");
//				for (int i = 0; i < actions.length; i++) {
//					String[] one = actions[i].split(actionPrefix);
//					if (one.length == 2) {
//						String moduleId = one[0];
//						if (StringUtils.contains(moduleIds, "," + moduleId + ",")) {
//							Map<String, Object> roleAction = new HashMap<>();
//							roleAction.put("parentid", id);
//							roleAction.put("moduleid", moduleId);
//							roleAction.put("value", one[1]);
//							roleActions.add(roleAction);
//						}
//					}
//				}
//				actionModel.batchInsert(roleActions);
//			}
//			String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
//			return "redirect:" + baseUrl + "/list" + mid + ".html";
//		} catch (Exception e) {
//			model.addAttribute("msg", "错误[" + e + "]");
//			return "/system/error";
//		}
//	}
//
//	@RequestMapping(value = "/getTree.do")
//	public void getModuleTree(HttpServletRequest request, HttpServletResponse response) {
//		// JSON树
//		String roleId = request.getParameter("id");
//		List<Filter> filters = new ArrayList<Filter>();
//		filters.add(new Filter("status", "=", "1"));
//		List<Map<String, Object>> moduleMaps = Think.getModel("model_sys_module").selectMaps(filters);
//		List<Map<String, Object>> returnModules = initModuleTree(moduleMaps, roleId);
//		Gson gson = new Gson();
//		writeResponse(response, gson.toJson(returnModules));
//	}
//
//	private List<Map<String, Object>> initModuleTree(List<Map<String, Object>> data, String roleId) {
//		if (null == data) {
//			return null;
//		}
//		// 获取原先模块权限
//		List<Filter> filters = new ArrayList<Filter>();
//		filters.add(new Filter("id", "=", roleId));
//		Map<String, Object> roleMap = Think.getModel("model_sys_role").selectMap(filters);
//		String moduleIds = (String) roleMap.get("moduleids");
//		if (null == moduleIds) {
//			moduleIds = "";
//		}
//		// 获取原先列权限
//		filters.clear();
//		filters.add(new Filter("parentid", "=", roleId));
//		List<Map<String, Object>> roleColumns = Think.getModel("model_sys_role_column").selectMaps(filters);
//		// 获取原先按钮权限
//		List<Map<String, Object>> roleActions = Think.getModel("model_sys_role_action").selectMaps(filters);
//		List<Map<String, Object>> mapList = new ArrayList<>();
//		for (Map<String, Object> map : data) {
//			String mid = map.get("id").toString();
//			// 模块权限MAP
//			boolean moduleChecked = false;
//			if (StringUtils.contains(moduleIds, "," + mid + ",")) {
//				moduleChecked = true;
//			}
//			mapList.add(getTreeNode(modulePrefix + mid, modulePrefix + map.get("parentid").toString(),
//					map.get("name").toString(), moduleChecked));
//			View view = Think.getView((String) map.get("viewcode"));
//			if (null == view) {
//				continue;
//			}
//			// 获取COLUMN
//			List<ViewColumn> columns = view.getColumns();
//			if (null != columns) {
//				boolean columnChecked = false;
//				String columnId = columnPrefix + mid;
//				// 循环所有列，如果有权限则勾选，如果没有权限则不勾选
//				for (ViewColumn column : columns) {
//					String columnName = column.getName();
//					if (getChecked(roleColumns, columnName, mid) && moduleChecked) {
//						mapList.add(getTreeNode(mid + columnPrefix + columnName, columnId,
//								column.getDescribe() + "(" + column.getName() + ")", true));
//						columnChecked = true;
//					} else {
//						mapList.add(getTreeNode(mid + columnPrefix + columnName, columnId,
//								column.getDescribe() + "(" + column.getName() + ")", false));
//					}
//				}
//				mapList.add(getTreeNode(columnId, modulePrefix + mid, "列权限", columnChecked));
//			}
//			// 获取ACTION
//			List<ViewAction> actions = view.getActions();
//			if (null != actions && moduleChecked) {
//				String actionId = this.actionPrefix + mid;
//				boolean actionChecked = false;
//				for (ViewAction action : actions) {
//					String actionName = action.getName();
//					if (getChecked(roleActions, actionName, mid) && moduleChecked) {
//						mapList.add(getTreeNode(mid + actionPrefix + actionName, actionId, action.getDescribe(), true));
//						actionChecked = true;
//					} else {
//						mapList.add(
//								getTreeNode(mid + actionPrefix + actionName, actionId, action.getDescribe(), false));
//					}
//				}
//				mapList.add(getTreeNode(actionId, modulePrefix + mid, "按钮权限", actionChecked));
//			}
//		}
//		return mapList;
//	}
//
//	/**
//	 * 判断权限的列和按钮是否需要勾选，如果权限中有值表示没有权限，不要勾选
//	 *
//	 * @param list
//	 * @param value
//	 * @param id
//	 * @return
//	 */
//	private boolean getChecked(List<Map<String, Object>> list, String value, String id) {
//		boolean result = true;
//		for (Map<String, Object> map : list) {
//			if (id.equals(map.get("moduleid").toString())) {
//				Object v = map.get("value");
//				if (null != v && v.toString().equals(value)) {
//					result = false;
//					break;
//				}
//			}
//		}
//		return result;
//	}

}
