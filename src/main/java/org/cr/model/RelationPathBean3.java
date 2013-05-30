package org.cr.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Description sourceuid  targetuid
 * @author caorong
 * @date 2013-2-21
 * 
 */
public class RelationPathBean3 implements Serializable {

	private static final long serialVersionUID = -2465622193676236724L;
	private String id;
	// 用于给数据库表示
	private String Centeruid;
	private String sourceuid;
	private String targetuid;
	// 深度，根据深度决定颜色
	private String deep;

	public RelationPathBean3() {
		// TODO Auto-generated constructor stub
	}
	
	public RelationPathBean3(String centeruid,String sourceuid,String targetuid,String deep) {
		this.Centeruid = centeruid;
		this.sourceuid = sourceuid;
		this.targetuid = targetuid;
		this.deep = deep;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCenteruid() {
		return Centeruid;
	}

	public void setCenteruid(String centeruid) {
		Centeruid = centeruid;
	}

	public String getSourceuid() {
		return sourceuid;
	}

	public void setSourceuid(String sourceuid) {
		this.sourceuid = sourceuid;
	}

	public String getTargetuid() {
		return targetuid;
	}

	public void setTargetuid(String targetuid) {
		this.targetuid = targetuid;
	}

	public String getDeep() {
		return deep;
	}

	public void setDeep(String deep) {
		this.deep = deep;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
