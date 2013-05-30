/**
 * @description 
 * @author caorong
 * @date 2012-12-30
 * 
 */
package org.cr.test;

import java.util.Calendar;

import org.cr.dao.impl.StatusDaoImpl;
import org.cr.dao.impl.UserDaoImpl;
import org.cr.model.StatusBean;
import org.cr.model.UserBean;

/**
 * @description 
 * @author caorong
 * @date 2012-12-30
 */
public class DbTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// user test
		UserBean user = new UserBean();
		user.setUid("1");
		user.setScreenName("screen");
		user.setCity("c");
		user.setUrl("www.bdfdafbababa.com");
		user.setCreatedAt(Calendar.getInstance().getTime());
		user.setAvatarLarge("avatarl");
		user.setType("123");
		String a = "🐱☀正能量不是没心没肺，不是强";
		System.out.println(a);
		a = a.replaceAll( "\\\\ ",   "\\\\\\\\ ");
		System.out.println(a);
		user.setDescription(a);
//		user.setDescription("\xF0\x9F\x90\xB1\xE2\x98");
		UserDaoImpl userDaoImpl = new UserDaoImpl();
//		for(int i=0;i<10;i++){
//			user.setUid(i+"");
//			userDaoImpl.insertUser(user);
		UserBean uuu = userDaoImpl.querySingleUserByUid("1");
		System.out.println(uuu);
//		}
//		System.out.println(userDaoImpl.queryCountByUid("12"));
		
		
		// status test
//		StatusBean statusBean = new StatusBean();
//		statusBean.setCreatedAt(Calendar.getInstance().getTime());
//		statusBean.setLatitude(12.22);
//		statusBean.setCommentBadCount("10");
//		StatusDaoImpl statusDaoImpl = new StatusDaoImpl();
//		for (int i = 0; i < 10; i++) {
//			statusBean.setWid(i+"");
//			statusDaoImpl.insertStatus(statusBean);
//		}
	}

}
