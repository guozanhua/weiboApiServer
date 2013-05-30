package org.cr.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.cr.model.UserBean;

/**
 * @Description
 * @author caorong
 * @date 2013-2-19
 * 
 */
public class ListenerTest {
	class Listener{
		UserBean userBean;
		ArrayList<Listener> children;
	}
	public static void main(String[] args) {
//		ArrayList<Listener> testlist = new ArrayList<ListenerTest.Listener>();
		
//		System.out.println(testlist.size()==0);
		HashMap<UserBean, UserBean> map = new HashMap<UserBean, UserBean>();
		UserBean user1 = new UserBean();
		UserBean user2 = new UserBean();
		user1.setUid("1");
		user2.setUid("2");
		map.put(user1, user2);
		map.put(user2, user2);
//		System.out.println(map.size());
		
		ArrayList<UserBean> lists = new ArrayList<UserBean>();
		lists.add(user2);
		lists.add(user1);
//		System.out.println(lists);
		UserBean user3 = new UserBean();
//		user3 = user2;
//		user3.setUid("3");
//		System.out.println(user2.getUid());
		user3.setUid("3");
		lists.add(user3);
		boolean flag = true;
		
//		for(Iterator i = lists.iterator(); i.hasNext();){
////			lists.remove(0);
//
//			System.out.println(u.getUid());
//			if(flag){
//				flag = false;
//				lists.remove(u);
//				lists.trimToSize();
//			}
//			System.out.println(lists.size( )); 
//		}
		HashMap<String, String> map2 = new HashMap<String, String>();
//		map2.put("1", "2");
//		map2.put("2", "2");
//		map2.put("3", "2");
		for(String s : map2.keySet()){
//			System.out.println("123");
		}
//		System.out.println("321");
		
//		System.out.println(Math.sin(450 * (Math.PI / 180)) == Math.sin(90 * (Math.PI / 180)));
		ArrayList<Integer> llists = new ArrayList<Integer>();
		llists.add(2);
		llists.add(3);
		System.out.println(llists.contains(1));
	}
}
