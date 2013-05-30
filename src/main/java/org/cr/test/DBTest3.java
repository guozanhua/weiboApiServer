/**
 * 
 */
package org.cr.test;
import org.cr.dao.impl.RelationPathObjDaoImpl;
import org.cr.model.*;
import org.cr.util.Identities;
/**
 * @Description	
 * @author caorong
 * @date 2013-1-6
 * 
 */
public class DBTest3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		RelationPathDaoImpl relationPathDaoImpl = new RelationPathDaoImpl();
//		RelationPathBean relationPathBean = new RelationPathBean("cuid","u","x","y","ex","ey","name","n","d");
//		
//		for(int i=0;i<10;i++){
//			relationPathBean.setId(Identities.create32LenUUID());
//			if(relationPathDaoImpl.queryRelationPathBeanByBean(relationPathBean)==0){
//				relationPathDaoImpl.insertRelationPathBean(relationPathBean);
//			}
//		}
		
		RelationPathObjDaoImpl relationPathObjDaoImpl = new RelationPathObjDaoImpl();
		RelationPathBean3 relationPathBean3 = new RelationPathBean3("test", "test", "test", "1");
		
		for(int i=0;i<10;i++){
			relationPathBean3.setId(Identities.create32LenUUID());
			relationPathBean3.setDeep(i+"");
			if(relationPathObjDaoImpl.queryRelationPathBeanByBean(relationPathBean3)==0){
				relationPathObjDaoImpl.insertRelationPathBean(relationPathBean3);
			}
		}
		
	}

}
