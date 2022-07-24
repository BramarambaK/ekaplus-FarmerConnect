package com.eka.farmerconnect.model;

public class UserRoleInfo {

	private Integer userId;
	private String firstName;
	private String lastName;
	private Integer clientId;
	private String username;
	private String roleIds;
	private String roleNames;
	private Character isSystemRole;

	public UserRoleInfo(Integer userId, String firstName, String lastName, Integer clientId, String username,
			String roleIds, String roleNames, Character isSystemRole) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.clientId = clientId;
		this.username = username;
		this.roleIds = roleIds;
		this.roleNames = roleNames;
		this.isSystemRole = isSystemRole;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public Character getIsSystemRole() {
		return isSystemRole;
	}

	public void setIsSystemRole(Character isSystemRole) {
		this.isSystemRole = isSystemRole;
	}

	@Override
	public String toString() {
		return "UserRoleInfo [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", clientId="
				+ clientId + ", username=" + username + ", roleIds=" + roleIds + ", roleNames=" + roleNames
				+ ", isSystemRole=" + isSystemRole + "]";
	}
}

