package com.idea.cms.support;

import java.util.Map;

public class UserSession {
	private String id;
	private String code;
	private String name;
	private String roleName;
	private String module;
	private Map<String, ModulePermission> permission;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Map<String, ModulePermission> getPermission() {
		return permission;
	}

	public void setPermission(Map<String, ModulePermission> permission) {
		this.permission = permission;
	}

	public ModulePermission getModulePermission(String id) {
		return this.permission.get(id);
	}

}
