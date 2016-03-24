package com.wuyizhiye.basedata.redis.serialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TestSerializerTranscoder
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class TestSerializerTranscoder implements Serializable {
	private static final long serialVersionUID = -1941046831377985500L;

	  public TestSerializerTranscoder() {
	    
	  }

	  
	  public void testObject() {
	    List<TestUser> lists = buildTestData();
	    
	    TestUser userA = lists.get(0);
	    
	    ObjectsTranscoder<TestUser> objTranscoder =  new ObjectsTranscoder<TestUser>();
	    
	    byte[] result1 = objTranscoder.serialize(userA);
	    
	    TestUser userA_userA = objTranscoder.deserialize(result1);
	    
//	    System.out.println(userA_userA.getName() + "\t" + userA_userA.getAge());
	  }
	  
	  
	  public void testList() {
	    List<TestUser> lists = buildTestData();
	    

	    ListTranscoder<TestUser> listTranscoder = new ListTranscoder<TestUser>();
	    
	    byte[] result1 = listTranscoder.serialize(lists);
	    
	    List<TestUser> results = listTranscoder.deserialize(result1);
	  
	    for (TestUser user : results) {
//	      System.out.println(user.getName() + "\t" + user.getAge());
	    }
	    
	  }
	  
	  private static List<TestUser> buildTestData() {
	    TestSerializerTranscoder tst = new TestSerializerTranscoder();
	    TestUser userA =  tst.new TestUser();
	    userA.setName("lily"); 
	        userA.setAge(25);
	    
	        
	        TestUser userB = tst.new TestUser();  
	        
	        
	        userB.setName("Josh Wang"); 
	    userB.setAge(28);
	        
	        List<TestUser> list = new ArrayList<TestUser>();  
	        list.add(userA);  
	        list.add(userB);  
	        
	        return list;  
	    }
	  
	  class TestUser implements Serializable {
	    private static final long serialVersionUID = 1L;

	      private String name;
	      
	      private int age;
	      

	      public String getName() {
	        return name;
	      }

	      public void setName(String name) {
	        this.name = name;
	      }

	      public int getAge() {
	        return age;
	      }

	      public void setAge(int age) {
	        this.age = age;
	      }
	      
	    }
	  
	  public static void main(String[] args){
		  TestSerializerTranscoder tt = new TestSerializerTranscoder();
		  tt.testList();
	  }
}
