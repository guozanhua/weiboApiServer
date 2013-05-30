package org.cr.dao;

import org.cr.model.RelationPathBean3;

/**
 * @Description	
 * @author caorong
 * @date 2013-2-21
 * 
 */
public interface RelationPathObjDao {
	/**
	 * @Description 添加path
	 * @param relationPathBean
	 * @return
	 * @author caorong
	 */
	public int insertRelationPathBean(RelationPathBean3 relationPathBean);
	
	/**
	 * @Description 查询重复path
	 * @param relationPathBean
	 * @return
	 * @author caorong
	 */
	public int queryRelationPathBeanByBean(RelationPathBean3 relationPathBean);
}
