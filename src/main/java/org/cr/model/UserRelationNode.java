package org.cr.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Description 用户关注图节点
 * @author caorong
 * @date 2013-2-19
 * 
 */
public class UserRelationNode implements Serializable {

	private static final long serialVersionUID = 1424047139306329231L;

	private UserBean userBean;
	private ArrayList<UserRelationNode> userChildren;

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public ArrayList<UserRelationNode> getUserChildren() {
		return userChildren;
	}

	public void setUserChildren(ArrayList<UserRelationNode> userChildren) {
		this.userChildren = userChildren;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
