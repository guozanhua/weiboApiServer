package org.cr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.cr.dao.impl.RelationPathDaoImpl;
import org.cr.dao.impl.RelationPathObjDaoImpl;
import org.cr.dao.impl.UserDaoImpl;
import org.cr.model.RelationPathBean;
import org.cr.model.RelationPathBean2;
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
public class ServletRelationShipThread3 implements Runnable {

	static Logger log = Logger.getLogger(ServletRelationShipThread3.class.getName());

	private String access_token;
	private UserDaoImpl userDaoImpl = new UserDaoImpl();
//	private RelationPathDaoImpl relationPathDaoImpl = new RelationPathDaoImpl();
	private RelationPathObjDaoImpl relationPathObjDaoImpl = new RelationPathObjDaoImpl();

	public ServletRelationShipThread3(String access_token) {
		this.access_token = access_token;
	}
	
//	UserRelationNode userTree;
	private List<UserBean> lv1UserBeans = new ArrayList<UserBean>();
	//添加的线条为实际的2倍
	private HashMap<UserBean, ArrayList<UserBean>> lv2BeanMap = new HashMap<UserBean, ArrayList<UserBean>>();
	private HashMap<UserBean, ArrayList<UserBean>> lv2SingleBeanMap = new HashMap<UserBean, ArrayList<UserBean>>();
	private List<RelationPathBean2> relationlists = new ArrayList<RelationPathBean2>();
	private List<RelationPathBean3> relationlist3s = new ArrayList<RelationPathBean3>();
	
	//Angle List 防覆盖
	private ArrayList<Integer> angleLists = new ArrayList<Integer>();
//	private UserRelationNode lv2SingleBeanMap

	//1层lv1i
	int lv1i = 0;
	
	@Override
	public void run() {
		//__SSSamuel  2060033304
		// 谁的uid用户关系      Lelouchcr:1057297283 szj:1880901514 ll:1796537952   发哥:1727019825 gps:2414605205 tly:1671865600 gq:1850637797 cyh:2328896060 lml:2215323930 boddhi:1880416260
		String printedUid = "2215323930";
		// 每层获取多少条
		int getRelationCount = 15;
		
		//插入树头user
		UserBean cenUser = userDaoImpl.querySingleUserByUid(printedUid);
//		userTree  = new UserRelationNode();
//		userTree.setUserBean(cenUser);
//		userTree.setUserChildren(new ArrayList<UserRelationNode>());
		
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
//				UserRelationNode lv1Listener = new UserRelationNode();
//				lv1Listener.setUserBean(user);
//				lv1Listener.setUserChildren(null);
				//更新user数据
				this.insertUser(user);
				//在父节点里插入子孩子
//				userTree.getUserChildren().add(lv1Listener);
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
			
//			ArrayList<UserRelationNode> authorChildren = userTree.getUserChildren();
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
		
		//print
//		Map m = new HashMap(); 
/*		for (UserBean u : lv2BeanMap.keySet()) {
			System.out.println();
			
			if (lv2BeanMap.get(u).size() == 0) {
				System.out.println(u.getUid());
				System.out.println(u.getName());
				System.out.println("的child为空！~");
			} else {
				System.out.println(u.getUid());
				System.out.println(u.getName());
				System.out.println("的child！~");
				System.out.println(lv2BeanMap.get(u));
			}
			System.out.println();
		}
*/		
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
		
//		System.out.println("--------------");
//		System.out.println(lv2SingleBeanMap);
//		System.out.println("--------------");
		
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
		
		// lv1层坐标 start
		int xlv1s = 660;
		int ylv1s = 440;
		// end
		int xlv1e = 660;
		int ylv1e = 440;
		// lv2层坐标
		int xlv2s = 0;
		int ylv2s = 0;
		// end
		int xlv2e = 0;
		int ylv2e = 0;
		
		//R len
		int baseRlen = 200;
		
		//父间隔
		int interval = (int) (360/(lv1UserBeans.size()*1.2));
		
		for(UserBean u : lv1UserBeans){
			//有child的user，为了将2层child画在1层child的周围
			if (lv2SingleBeanMap.get(u).size() != 0) {
				int childCount = lv2SingleBeanMap.get(u).size();
				int fatherRlen = 0;
				//1层长度与 child数量成正比
				if (childCount == 1) {
					fatherRlen = (int) (baseRlen * 1.5);
				} else {
					fatherRlen = (int) (baseRlen * (1.5+ (new Random().nextInt(10))/11 )) ;
				}
				//2层长度与 child数量成反比
				int childRlen = 0;
				if (childCount == 1) {
					childRlen = (int) (baseRlen * 0.8);
				} else {
					childRlen = (int) (baseRlen * (1.65 - (new Random().nextInt(10))/11 )) ;
				}
				
//				xlv1e = (int) (xlv1s + rlv1* Math.cos(ilv1 * (360 / lengthlv1) * (Math.PI / 180)));
//				ylv1e = (int) (ylv1s + rlv1* Math.sin(ilv1 * (360 / lengthlv1) * (Math.PI / 180)));
				// 画父线
				xlv1s = 660;
				ylv1s = 440;
//				xlv1e = (int) (xlv1s + fatherRlen * Math.cos(( (0 + (lv1i/4 + 1) * (360 / interval))) * (Math.PI / 180)));
				// ylv1e = (int) (ylv1s + fatherRlen * Math.sin(( (0 + (lv1i/4 +
				// 1) * (360 / interval))) * (Math.PI / 180)));
				int ranAngle = this.getRandomFatherAngle();
				angleLists.add(ranAngle);
				xlv1e = this.getFatherEndx(xlv1s, fatherRlen, ranAngle);
				ylv1e = this.getFatherEndy(ylv1s, fatherRlen, ranAngle);
				relationlists.add(new RelationPathBean2(printedUid, printedUid,
						u.getUid(), xlv1s + "", ylv1s + "", xlv1e + "", ylv1e + "", u.getName(), (int) (childCount * 1.5)+ "", 1 + ""));
				relationlist3s.add(new RelationPathBean3(printedUid,printedUid,u.getUid(),1+""));
				System.out.println("first : " + "(" + "Lelouchcr" + ","	+ u.getName() + ")");
				for (UserBean u2 : lv2SingleBeanMap.get(u)) {
					//将每个user画在lv1center 的周围
					xlv2s = xlv1e;
					ylv2s = ylv1e;
					int childInterval = 360 / lv2SingleBeanMap.get(u).size();
					int tmpi = 0;
					//检测是否存在(A,C)(B,C)的情况  
					if(this.getRelationListsChildsFather(u2) != null){
						//如果childUid存在的话   
						// TODO 还可以扩展 将原来的lv1线去掉 重新画在新的线旁边
						
						RelationPathBean2 relationPathBean = this.getRelationListsChildsFather(u2);
						xlv2e = Integer.parseInt(relationPathBean.getXend()); 
						ylv2e = Integer.parseInt(relationPathBean.getYend());
					} else {
						xlv2e = this.getChildEndx(xlv2s, childRlen, 50 + childInterval * tmpi);
						ylv2e = this.getChildEndy(ylv2s, childRlen, 50 + childInterval * tmpi);
						tmpi++;
					}
//					if()
					relationlists.add(new RelationPathBean2(printedUid, u.getUid(), u2.getUid(), xlv2s + "", ylv2s + "",
							xlv2e + "", ylv2e + "", u2.getName(), childCount+ "", 2 + ""));
					relationlist3s.add(new RelationPathBean3(printedUid,u.getUid(),u2.getUid(),2+""));
					System.out.println("second : " + "(" + u.getName() + ","	+ u2.getName() + ")");
				}
			} 
		}
		
		// 再画单独的 
		for (UserBean u : lv1UserBeans) {
			if (lv2SingleBeanMap.get(u).size() == 0) {
				xlv1s = 660;
				ylv1s = 440;

				// 存在bean是别的bean的child 已经在relationLists里了
				if (this.getRelationListsChildsFather(u) != null) {
					RelationPathBean2 relationPathBean2 = this
							.getRelationListsChildsFather(u);
					xlv1e = Integer.parseInt(relationPathBean2.getXend());
					ylv1e = Integer.parseInt(relationPathBean2.getYend());
				} else {
					// 对于没有任何限制和child的lv1 层
					int fatherRlen = (int) (baseRlen);
					if (new Random().nextInt(10) > 5) {
						fatherRlen = baseRlen;
					} else {
						fatherRlen = (int) (baseRlen * 1.3);
					}
					int ranAngle = this.getRandomFatherAngle();
					angleLists.add(ranAngle);
					xlv1e = this.getFatherEndx(xlv1s, fatherRlen, ranAngle);
					ylv1e = this.getFatherEndy(ylv1s, fatherRlen, ranAngle);
				}
				// 插入没有孩子的path
				relationlists.add(new RelationPathBean2(printedUid, printedUid,u.getUid(), xlv1s + "", ylv1s + "", xlv1e + "", ylv1e
								+ "", u.getName(), 5 + "", 1 + ""));
				relationlist3s.add(new RelationPathBean3(printedUid,printedUid,u.getUid(),1+""));
				System.out.println("third : " + "(" + "Lelouchcr" + ","+ u.getName() + ")");

			}
		}
		
		//入db
//		for(RelationPathBean2 r2 : relationlists){
//			insertRelationPathToDb(r2);
//		}
//		无意义了
//		insertRelationPathToDb(new RelationPathBean2(printedUid, printedUid, printedUid, "660", "440", "660", "440", cenUser.getName(), "20", "0"));
		
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
	
//	public void insertRelationPathToDb(RelationPathBean2 r2){
//		RelationPathBean r1 = new RelationPathBean(r2.getCenteruid(),r2.getUid(),r2.getXstart(),r2.getYstart(),r2.getXend(),r2.getYend(),r2.getName(),r2.getNoder(),r2.getDeep());
//		//生成唯一id
//		r1.setId(Identities.create32LenUUID());
//		if (relationPathDaoImpl.queryRelationPathBeanByBean(r1) == 0) {
//			relationPathDaoImpl.insertRelationPathBean(r1);
//		}
//	}
	
//	private 
	
	//检测待插入的(A,C)是否lists里已经存在(B,C)
	private RelationPathBean2 getRelationListsChildsFather(UserBean u2){
//		ArrayList<UserBean> tmplists = new ArrayList<UserBean>();
		for(RelationPathBean2 r2 : relationlists){
			if(r2.getUid().equals(u2.getUid())){
				return r2; 
			}
		}
		return null;
	}
	
	//获取随机角度（不重复）
	private int getRandomFatherAngle(){
		int reAngel = 0;
		while(true){
			reAngel = new Random().nextInt(360);
			if(!checkAngelExist(reAngel)){
				break;
			}
		}
		return reAngel;
	}
	
	//验证该角度/或者该角度周围x度的度数是否存在
	private boolean checkAngelExist(int reAngel){
		int reAgelInterval = 3;
		
		for (int i = reAngel - reAgelInterval; i < reAngel + reAgelInterval; i++) {
			if (angleLists.contains(i)) {
				return true;
			}
		}
		return false;
	}
	
	private int getChildEndx(int xlv2s, int childRlen, int angle) {
		// 随机生成子转角
		return (int) (xlv2s + childRlen * Math.sin(angle * (Math.PI / 180)));
	}
	
	private int getChildEndy(int ylv2s, int childRlen, int angle) {
		// 随机生成子转角
		return (int) (ylv2s + childRlen * Math.sin(angle * (Math.PI / 180)));
	}
	
	private int getFatherEndx(int xlv1s,int fatherRlen,int angle){
//		if()  										随机生成父转角
		return (int) (xlv1s + fatherRlen * Math.cos(angle * (Math.PI / 180)));
	}
	private int getFatherEndy(int ylv1s,int fatherRlen,int angle){
								//		随机生成父转角
		return (int) (ylv1s + fatherRlen * Math.sin(angle * (Math.PI / 180)));
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
	
//	private ArrayList<UserBean> getlv1Children(UserBean u1){
//		ArrayList<UserBean> childrenLists = new ArrayList<UserBean>();
//		for (UserBean u : lv2BeanMap.keySet()) {
//			if(lv2BeanMap.get(u))
//			
//		}
//	}
	
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

//	private UserBean getFatherUser(User u){
//		for(UserBean user : lv1UserBeans){
//			if(user.getUid().equals(u.getId())){
//				return user;
//			}
//		}
//		return null;
//	}
	
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
