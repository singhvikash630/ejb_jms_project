package com.jspider;

import javax.ejb.Stateless;

/**
 * Session Bean implementation class FirstEjbBean
 */
//@Stateless(name="FirstEjbBean/vikash")
@Stateless
public class FirstEjbBean implements FirstEjbBeanRemote {

    /**
     * Default constructor. 
     */
    public FirstEjbBean() {
        // TODO Auto-generated constructor stub
    }

	public String testDemo() {
		// TODO Auto-generated method stub
		return "Welcome to forst session bean";
	}

}
