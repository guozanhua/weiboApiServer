package org.cr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.cr.dao.impl.RelationPathObjDaoImpl;
import org.cr.dao.impl.UserDaoImpl;
import org.cr.model.RelationPathBean3;
import org.cr.model.UserBean;
import org.cr.util.Identities;

import weibo4j.Friendships;
import weibo4j.model.Paging;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

/**
 * @Description
 * @author caorong
 * @date 2013-2-19
 * 
 */
public class ServletRelationShipThread4 implements Runnable {

	static Logger log = Logger.getLogger(ServletRelationShipThread4.class.getName());

	private String access_token;
	private UserDaoImpl userDaoImpl = new UserDaoImpl();
//	private RelationPathDaoImpl relationPathDaoImpl = new RelationPathDaoImpl();
	private RelationPathObjDaoImpl relationPathObjDaoImpl = new RelationPathObjDaoImpl();

	public ServletRelationShipThread4(String access_token) {
		this.access_token = access_token;
	}
	
	private List<UserBean> lv1UserBeans = new ArrayList<UserBean>();
	//添加的线条为实际的2倍
	private HashMap<UserBean, ArrayList<UserBean>> lv2BeanMap = new HashMap<UserBean, ArrayList<UserBean>>();
	private HashMap<UserBean, ArrayList<UserBean>> lv2SingleBeanMap = new HashMap<UserBean, ArrayList<UserBean>>();
	private List<RelationPathBean3> relationlist3s = new ArrayList<RelationPathBean3>();
	
	//Angle List 防覆盖
	private ArrayList<Integer> angleLists = new ArrayList<Integer>();

	//1层lv1i
	int lv1i = 0;
	
	@Override
	public void run() {
		//__SSSamuel  2060033304  不離岛:1302498533  yd:2854011705 仓老师:1739928273  孙燕姿：1937439635
		// 谁的uid用户关系      Lelouchcr:1057297283 szj:1880901514 ll:1796537952   发哥:1727019825 gps:2414605205 tly:1671865600 gq:1850637797 cyh:2328896060 lml:2215323930 boddhi:1880416260
		String printedUid = "1937439635";
		// 每层获取多少条
		int getRelationCount = 15;
		
		//插入树头user
		UserBean cenUser = userDaoImpl.querySingleUserByUid(printedUid);
		
		Friendships friendships = new Friendships();
		friendships.client.setToken(access_token);
		
		UserWapper userWapper = null;
		try {																	//这里需要改成200 不然此人社交网络不完整
			userWapper = friendships.getFriendsBilateral(printedUid, 0,new Paging(1, 170));
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		//第一层的users
		List<User>lv1Users = userWapper.getUsers();
		
		//遍历建立node节点 放进userTree 目的：获取第一层的参照
		for (User u : lv1Users) {
			if(u!=null){
				UserBean user = getUserBean(u);
				//加进1层全局list
				lv1UserBeans.add(user);
				//更新user数据
				this.insertUser(user);
			}
		}
		
		for(UserBean u1 : lv1UserBeans){
			//此UserBean必定存在
			userWapper = null;
			try {
				//获取第二层的时候 获取所有
				userWapper = friendships.getFriendsBilateral(u1.getUid(), 0,new Paging(1, 200));
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			List<User>lv2Users = userWapper.getUsers();
			
			//仅获取lv2User中在lv1UserBeans中存在的user
			ArrayList<UserBean> currentU1Children = new ArrayList<UserBean>();
			for(User u2 : lv2Users){
				//update user
				this.insertUser(getUserBean(u2));
				if(isUserExist(u2)){
					//获取要修改的节点
					UserBean currentUser = getChildFromList(lv1UserBeans,u2);
					currentU1Children.add(currentUser);
					
				}	
			}
			lv2BeanMap.put(u1, currentU1Children);
		}
				
		//过滤掉重复的线段
		for(UserBean u : lv2BeanMap.keySet()){
			//如果新的map里没有重复线段则加之
			//对于没有child的线段 lists的size为0
			ArrayList<UserBean> tmplists = new ArrayList<UserBean>();
			for (UserBean eachU : lv2BeanMap.get(u)) {
				if (!islv2SingleBeanMapExist(u,eachU)) {
					tmplists.add(eachU);
				}
			}
			lv2SingleBeanMap.put(u, tmplists);
		}
	
		//print
		for(UserBean u : lv2SingleBeanMap.keySet()){
			System.out.println();
			
			if(lv2SingleBeanMap.get(u).size() == 0){
				System.out.println(u.getUid());
				System.out.println(u.getName());
				System.out.println("的child为空！~");
			} else {
				System.out.println(u.getUid());
				System.out.println(u.getName());
				System.out.println("的child！~");
				System.out.println(lv2SingleBeanMap.get(u));
			}
		}
		
		
		//画图
		// canvas 属性初始化
		int pooo = 0;
		for(UserBean u : lv1UserBeans){
			pooo++;
			//有child的user，为了将2层child画在1层child的周围
			if (lv2SingleBeanMap.get(u).size() != 0) {
				relationlist3s.add(new RelationPathBean3(printedUid,printedUid,u.getUid(),1+""));
				System.out.println("first : " + "(" + cenUser.getName() + ","	+ u.getName() + ")");
				for (UserBean u2 : lv2SingleBeanMap.get(u)) {
					relationlist3s.add(new RelationPathBean3(printedUid,u.getUid(),u2.getUid(),2+""));
					System.out.println("second : " + "(" + u.getName() + ","	+ u2.getName() + ")");
				}
			} 
		}
		
		// 再画单独的 
		for (UserBean u : lv1UserBeans) {
			if (lv2SingleBeanMap.get(u).size() == 0) {
				relationlist3s.add(new RelationPathBean3(printedUid,printedUid,u.getUid(),1+""));
				System.out.println("third : " + "(" + cenUser.getName() + ","+ u.getName() + ")");

			}
		}
				
		for(RelationPathBean3 r3 : relationlist3s){
			insertRelation3PathToDb(r3);
		}
		
		//lv1UserBeans 当前cenuid的所有的UserBean
		
	}
	
	public void insertRelation3PathToDb(RelationPathBean3 r3){
		//生成唯一id
		r3.setId(Identities.create32LenUUID());
		if (relationPathObjDaoImpl.queryRelationPathBeanByBean(r3) == 0) {
			relationPathObjDaoImpl.insertRelationPathBean(r3);
		}
	}
	

	private boolean islv2SingleBeanMapExist(UserBean u1,UserBean u2){
		
		if (lv2SingleBeanMap.get(u1) != null) {
			//是否存在 (A,B)
			for (UserBean eachU : lv2SingleBeanMap.get(u1)) {
				if (u2.getUid().equals(eachU.getUid()))
					return true;
			}
		}
		if (lv2SingleBeanMap.get(u2) != null) {
			// 是否存在 (B,A)
			for (UserBean eachU : lv2SingleBeanMap.get(u2)) {
				if (u1.getUid().equals(eachU.getUid()))
					return true;
			}
		}
		return false;
	}
	
	private void insertUser(UserBean user){
		if(userDaoImpl.queryCountByUid(user.getUid())!=0){
			//update
			userDaoImpl.updateSingleUser(user);
		}else{
			//insert
			userDaoImpl.insertUser(user);
		}
	}
	
	private UserBean getChildFromList(List<UserBean> children,User u){
		for(UserBean user : children){
			if(user.getUid().equals(u.getId())){
				return user;
			}
		}
		return null;
	}
	
	private boolean isUserExist(User u){
		for(UserBean user : lv1UserBeans){
			if(user.getUid().equals(u.getId())){
				return true;
			}
		}
		return false;
	}
	
	private UserBean getUserBean(User u){
		UserBean userBean = new UserBean();

		userBean.setUid(u.getId());
		userBean.setScreenName(u.getScreenName());
		userBean.setName(u.getName());
		userBean.setProvince(u.getProvince() + "");
		userBean.setCity(u.getCity() + "");

		userBean.setLocation(u.getLocation());
		userBean.setDescription(u.getDescription());
		userBean.setUrl(u.getUrl());
		userBean.setProfileImageUrl(u.getProfileImageUrl());
		userBean.setUserDomain(u.getUserDomain());

		userBean.setGender(u.getGender());
		userBean.setFollowersCount(u.getFollowersCount() + "");
		userBean.setFriendsCount(u.getFriendsCount() + "");
		userBean.setStatusesCount(u.getStatusesCount() + "");
		userBean.setFavouritesCount(u.getFavouritesCount() + "");

		userBean.setCreatedAt(u.getCreatedAt());
		String flag = "1";
		if (!u.isFollowing()) {
			flag = "0";
		}
		userBean.setFollowing(flag);
		flag = "1";
		if (!u.isVerified()) {
			flag = "0";
		}
		userBean.setVerified(flag);
		userBean.setVerifiedType(u.getverifiedType() + "");
		flag = "1";
		if (!u.isAllowAllActMsg()) {
			flag = "0";
		}
		userBean.setAllowAllActMsg(flag);

		flag = "1";
		if (!u.isallowAllComment()) {
			flag = "0";
		}
		userBean.setAllowAllComment(flag);
		flag = "1";
		if (!u.isFollowMe()) {
			flag = "0";
		}
		userBean.setFollowMe(flag);
		userBean.setAvatarLarge(u.getAvatarLarge());
		userBean.setBiFollowersCount(u.getBiFollowersCount() + "");
		userBean.setRemark(u.getRemark());

		userBean.setLang(u.getLang());
		userBean.setVerifiedReason(u.getVerifiedReason());
		userBean.setWeihao(u.getWeihao());
		return userBean;
	}

}
