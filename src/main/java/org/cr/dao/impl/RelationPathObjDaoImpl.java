package org.cr.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.cr.dao.RelationPathDao;
import org.cr.dao.RelationPathObjDao;
import org.cr.model.RelationPathBean3;
import org.cr.util.DBUtil;

/**
 * @Description	
 * @author caorong
 * @date 2013-2-21
 * 
 */
public class RelationPathObjDaoImpl implements RelationPathObjDao{

	static Logger log = Logger.getLogger(RelationPathObjDaoImpl.class.getName());
	
	private SqlSession session = null;

	private SqlSession getSqlSession() {
		session = DBUtil.getSessionFactory().openSession();
		return session;
	}
	
	@Override
	public int insertRelationPathBean(RelationPathBean3 relationPathBean) {
		int ans = 0;
		try {
			session = this.getSqlSession();
			RelationPathObjDao RelationPathObjDao = session.getMapper(RelationPathObjDao.class);
			ans = RelationPathObjDao.insertRelationPathBean(relationPathBean);
			log.info("insert relationPathObjBean"+relationPathBean.toString());
			session.commit();
			log.info("insert relationPathObjBean Success!!! ");
		}catch (Exception e) {
			log.error("insert relationPathObjBean Failed!!! ---> "+relationPathBean.toString());
			e.printStackTrace();
		} finally{
			session.close();
		}
		return ans;
	}

	@Override
	public int queryRelationPathBeanByBean(RelationPathBean3 relationPathBean) {
		int ans = 0;
		try {
			session = this.getSqlSession();
			RelationPathObjDao relationPathObjDao = session.getMapper(RelationPathObjDao.class);
			ans = relationPathObjDao.queryRelationPathBeanByBean(relationPathBean);
		} catch (Exception e) {
			log.error("query Failed!!! ");
			e.printStackTrace();
		}finally{
			session.close();
		}
		return ans;
	}

}
