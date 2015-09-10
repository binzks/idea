package com.idea.cms.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.idea.cms.controller.BaseController;
import com.idea.framework.jdbc.support.model.Filter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/sys")
public class LoginController extends BaseController {

	@RequestMapping(value = "/index.do")
	public String index() {
		return "login";
	}

	@RequestMapping(value = "/home")
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/init.do")
	public String init() {
	//	CmsConfig.init();
		return "redirect:/sys/index.do";
	}

//	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
//	public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
//		String code = request.getParameter("loginCode");
//		String password = request.getParameter("loginPsw");
//		HttpSession session = request.getSession();
//		try {
//			List<Filter> filterList = new ArrayList<Filter>();
//			filterList.add(new Filter("code", "=", code));
//			org.thinkframework.model.Model userModel = Think.getModel("model_login_user");
//			List<Map<String, Object>> userMaps = userModel.selectMaps(filterList);
//			if (userMaps.size() == 0) {
//				model.addAttribute("errmsg", "用户不存在");
//				return "login";
//			}
//			// 密码MD5后再MD5
//			Map<String, Object> userMap = userMaps.get(0);
//			if (!userMap.get("password").equals(EncryptUtils.md5(EncryptUtils.md5(password)))) {
//				model.addAttribute("errmsg", "密码错误");
//				return "login";
//			}
//			// 用户模块
//			String mids = null;
//			Object moduleids = userMap.get("moduleids");
//			if (null == moduleids || moduleids.toString().equals("")) {
//				model.addAttribute("errmsg", "用户没有任何模块权限，请联系管理员！");
//				return "login";
//			} else {
//				mids = moduleids.toString();
//				if (mids.substring(0, 1).equals(",")) {
//					mids = mids.substring(1, mids.length());
//				}
//			}
//			String userId = userMap.get("id").toString();
//			UserSession userSession = getUserSession(userId, code, userMap.get("name").toString(),
//					userMap.get("roleid").toString(), userMap.get("rolename").toString(), mids);
//			/* 记录用户登录信息 */
//			String ip = IpUtils.getRequestIpAddress(request);
//			userMap.put("lastip", ip);
//			userMap.put("lasttime", System.currentTimeMillis() / 1000);
//			userModel.update(userMap);
//			// 把用户信息保存到session
//			session.setAttribute("session_user", userSession);
//			LogUtils.saveCMSLog(ip, userId, "登录，用户[" + code + "][" + userSession.getName() + "]已成功登录");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "redirect:/sys/home.html";
//	}
//
//	private UserSession getUserSession(String id, String code, String name, String roleid, String roleName, String mids)
//			throws Exception {
//		UserSession userSession = new UserSession();
//		// 模块
//		List<Filter> filters = new ArrayList<>();
//		filters.add(new Filter("id", "in", mids));
//		List<Map<String, Object>> moduleMaps = Think.getModel("model_login_module").selectMaps(filters);
//		// 按钮
//		filters.clear();
//		filters.add(new Filter("parentid", "=", roleid));
//		List<Map<String, Object>> actionMaps = Think.getModel("model_sys_role_action").selectMaps(filters);
//		// 列
//		List<Map<String, Object>> columnMaps = Think.getModel("model_sys_role_column").selectMaps(filters);
//		// 行级权限
//		filters.clear();
//		filters.add(new Filter("parentid", "=", id));
//		List<Order> orders = new ArrayList<>();
//		orders.add(new Order("column", "desc"));
//		List<Map<String, Object>> rowFilterMaps = Think.getModel("model_sys_user_rowfilter").selectMaps(filters);
//		// 生成权限
//		Map<String, ModulePermission> permission = new HashMap<String, ModulePermission>();
//		for (Map<String, Object> module : moduleMaps) {
//			String moduleId = module.get("id").toString();
//			permission.put(moduleId, getModulePermission(id, moduleId, (String) module.get("name"),
//					(String) module.get("viewcode"), actionMaps, columnMaps, rowFilterMaps));
//		}
//		userSession.setId(id);
//		userSession.setCode(code);
//		userSession.setName(name);
//		userSession.setRoleName(roleName);
//		userSession.setPermission(permission);
//		Gson gson = new Gson();
//		userSession.setModule(gson.toJson(moduleMaps));
//		return userSession;
//	}
//
//	private ModulePermission getModulePermission(String userId, String moduleId, String moduleName, String viewCode,
//			List<Map<String, Object>> actionMaps, List<Map<String, Object>> columnMaps,
//			List<Map<String, Object>> rowFilterMaps) {
//		ModulePermission modulePermission = new ModulePermission();
//		modulePermission.setModelName(moduleName);
//		if (StringUtils.isBlank(viewCode)) {
//			return modulePermission;
//		}
//		View view = Think.getView(viewCode);
//		modulePermission.setView(view);
//		// 生成模块按钮的权限
//		if (null != actionMaps) {
//			Map<String, String> actions = new HashMap<>();
//			for (Map<String, Object> map : actionMaps) {
//				if (moduleId.equals(map.get("moduleid").toString())) {
//					actions.put(map.get("value").toString(), map.get("value").toString());
//				}
//			}
//			modulePermission.setActions(actions);
//		}
//		// 生成模块列的权限
//		if (null != columnMaps) {
//			Map<String, String> columns = new HashMap<>();
//			for (Map<String, Object> map : columnMaps) {
//				if (moduleId.equals(map.get("moduleid").toString())) {
//					columns.put(map.get("value").toString(), map.get("value").toString());
//				}
//			}
//			modulePermission.setColumns(columns);
//		}
//		// 生成模块行级过滤权限
//		if (null != rowFilterMaps) {
//			Map<String, String> rowFilters = new HashMap<>();
//			for (Map<String, Object> map : rowFilterMaps) {
//				if (moduleId.equals(map.get("moduleid").toString())) {
//					rowFilters.put(map.get("column").toString(), map.get("value").toString().substring(1));
//				}
//			}
//			modulePermission.setRowFilters(rowFilters);
//		}
//		// 生成模块工作流权限
//		String workFlow = view.getWorkflow();
//		if (StringUtils.isBlank(workFlow)) {
//			return modulePermission;
//		}
//		List<Filter> filters = new ArrayList<>();
//		filters.clear();
//		filters.add(new Filter("code", "=", workFlow));
//		Map<String, Object> workFlowMap = Think.getModel("model_sys_workflow").selectMap(filters);
//		if (null == workFlowMap) {
//			return modulePermission;
//		}
//		modulePermission.setWorkFlowField((String) workFlowMap.get("field"));
//		modulePermission.setWorkFlowUserField((String) workFlowMap.get("userfield"));
//		modulePermission.setWorkFlowTimeField((String) workFlowMap.get("timefield"));
//		// 获取工作流节点，并分类添加到map中，已value为key，type为value
//		filters.clear();
//		filters.add(new Filter("parentid", "=", workFlowMap.get("id")));
//		filters.add(new Filter("userids", "like", "," + userId + ","));
//		List<Map<String, Object>> list = Think.getModel("model_login_workflow_step").selectMaps(filters);
//		if (null == list) {
//			return modulePermission;
//		}
//		Map<String, String> stepMap = new HashMap<>();
//		StringBuffer workFlowValues = new StringBuffer(); // 工作流节点值，用于页面查询过滤
//		for (Map<String, Object> map : list) {
//			String value = map.get("value").toString();
//			workFlowValues.append(value);
//			stepMap.put(value, map.get("type").toString());
//		}
//		modulePermission.setWorkFlowValues(workFlowValues.toString());
//		modulePermission.setWorkFlowStep(stepMap);
//		return modulePermission;
//	}
//
//	@RequestMapping(value = "/getMenu.do")
//	public void getMenu(HttpServletRequest request, HttpServletResponse response) {
//		HttpSession session = request.getSession();
//		UserSession userSession = (UserSession) session.getAttribute("session_user");
//		writeResponse(response, userSession.getModule());
//	}
//
//	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
//	public String logout(HttpServletRequest request, HttpServletResponse response) {
//		HttpSession session = request.getSession();
//		UserSession userSession = (UserSession) session.getAttribute("session_user");
//		String userId = userSession.getId();
//		String code = userSession.getCode();
//		String name = userSession.getName();
//		// 清空session
//		session.removeAttribute("session_user");
//		LogUtils.saveCMSLog(IpUtils.getRequestIpAddress(request), userId, "登出，用户[" + code + "][" + name + "]已成功登出");
//		return "login";
//	}

}
