/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.sms.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import org.opencps.sms.model.SMSLookUpQueue;
import org.opencps.sms.service.SMSLookUpQueueService;
import org.opencps.sms.service.persistence.SMSGatewayLogFinder;
import org.opencps.sms.service.persistence.SMSGatewayLogPersistence;
import org.opencps.sms.service.persistence.SMSLookUpQueuePersistence;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the sms look up queue remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link org.opencps.sms.service.impl.SMSLookUpQueueServiceImpl}.
 * </p>
 *
 * @author khoa
 * @see org.opencps.sms.service.impl.SMSLookUpQueueServiceImpl
 * @see org.opencps.sms.service.SMSLookUpQueueServiceUtil
 * @generated
 */
public abstract class SMSLookUpQueueServiceBaseImpl extends BaseServiceImpl
	implements SMSLookUpQueueService, IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link org.opencps.sms.service.SMSLookUpQueueServiceUtil} to access the sms look up queue remote service.
	 */

	/**
	 * Returns the sms gateway log local service.
	 *
	 * @return the sms gateway log local service
	 */
	public org.opencps.sms.service.SMSGatewayLogLocalService getSMSGatewayLogLocalService() {
		return smsGatewayLogLocalService;
	}

	/**
	 * Sets the sms gateway log local service.
	 *
	 * @param smsGatewayLogLocalService the sms gateway log local service
	 */
	public void setSMSGatewayLogLocalService(
		org.opencps.sms.service.SMSGatewayLogLocalService smsGatewayLogLocalService) {
		this.smsGatewayLogLocalService = smsGatewayLogLocalService;
	}

	/**
	 * Returns the sms gateway log remote service.
	 *
	 * @return the sms gateway log remote service
	 */
	public org.opencps.sms.service.SMSGatewayLogService getSMSGatewayLogService() {
		return smsGatewayLogService;
	}

	/**
	 * Sets the sms gateway log remote service.
	 *
	 * @param smsGatewayLogService the sms gateway log remote service
	 */
	public void setSMSGatewayLogService(
		org.opencps.sms.service.SMSGatewayLogService smsGatewayLogService) {
		this.smsGatewayLogService = smsGatewayLogService;
	}

	/**
	 * Returns the sms gateway log persistence.
	 *
	 * @return the sms gateway log persistence
	 */
	public SMSGatewayLogPersistence getSMSGatewayLogPersistence() {
		return smsGatewayLogPersistence;
	}

	/**
	 * Sets the sms gateway log persistence.
	 *
	 * @param smsGatewayLogPersistence the sms gateway log persistence
	 */
	public void setSMSGatewayLogPersistence(
		SMSGatewayLogPersistence smsGatewayLogPersistence) {
		this.smsGatewayLogPersistence = smsGatewayLogPersistence;
	}

	/**
	 * Returns the sms gateway log finder.
	 *
	 * @return the sms gateway log finder
	 */
	public SMSGatewayLogFinder getSMSGatewayLogFinder() {
		return smsGatewayLogFinder;
	}

	/**
	 * Sets the sms gateway log finder.
	 *
	 * @param smsGatewayLogFinder the sms gateway log finder
	 */
	public void setSMSGatewayLogFinder(SMSGatewayLogFinder smsGatewayLogFinder) {
		this.smsGatewayLogFinder = smsGatewayLogFinder;
	}

	/**
	 * Returns the sms look up queue local service.
	 *
	 * @return the sms look up queue local service
	 */
	public org.opencps.sms.service.SMSLookUpQueueLocalService getSMSLookUpQueueLocalService() {
		return smsLookUpQueueLocalService;
	}

	/**
	 * Sets the sms look up queue local service.
	 *
	 * @param smsLookUpQueueLocalService the sms look up queue local service
	 */
	public void setSMSLookUpQueueLocalService(
		org.opencps.sms.service.SMSLookUpQueueLocalService smsLookUpQueueLocalService) {
		this.smsLookUpQueueLocalService = smsLookUpQueueLocalService;
	}

	/**
	 * Returns the sms look up queue remote service.
	 *
	 * @return the sms look up queue remote service
	 */
	public SMSLookUpQueueService getSMSLookUpQueueService() {
		return smsLookUpQueueService;
	}

	/**
	 * Sets the sms look up queue remote service.
	 *
	 * @param smsLookUpQueueService the sms look up queue remote service
	 */
	public void setSMSLookUpQueueService(
		SMSLookUpQueueService smsLookUpQueueService) {
		this.smsLookUpQueueService = smsLookUpQueueService;
	}

	/**
	 * Returns the sms look up queue persistence.
	 *
	 * @return the sms look up queue persistence
	 */
	public SMSLookUpQueuePersistence getSMSLookUpQueuePersistence() {
		return smsLookUpQueuePersistence;
	}

	/**
	 * Sets the sms look up queue persistence.
	 *
	 * @param smsLookUpQueuePersistence the sms look up queue persistence
	 */
	public void setSMSLookUpQueuePersistence(
		SMSLookUpQueuePersistence smsLookUpQueuePersistence) {
		this.smsLookUpQueuePersistence = smsLookUpQueuePersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name remote service.
	 *
	 * @return the class name remote service
	 */
	public com.liferay.portal.kernel.service.ClassNameService getClassNameService() {
		return classNameService;
	}

	/**
	 * Sets the class name remote service.
	 *
	 * @param classNameService the class name remote service
	 */
	public void setClassNameService(
		com.liferay.portal.kernel.service.ClassNameService classNameService) {
		this.classNameService = classNameService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.kernel.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.kernel.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return SMSLookUpQueueService.class.getName();
	}

	protected Class<?> getModelClass() {
		return SMSLookUpQueue.class;
	}

	protected String getModelClassName() {
		return SMSLookUpQueue.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = smsLookUpQueuePersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = org.opencps.sms.service.SMSGatewayLogLocalService.class)
	protected org.opencps.sms.service.SMSGatewayLogLocalService smsGatewayLogLocalService;
	@BeanReference(type = org.opencps.sms.service.SMSGatewayLogService.class)
	protected org.opencps.sms.service.SMSGatewayLogService smsGatewayLogService;
	@BeanReference(type = SMSGatewayLogPersistence.class)
	protected SMSGatewayLogPersistence smsGatewayLogPersistence;
	@BeanReference(type = SMSGatewayLogFinder.class)
	protected SMSGatewayLogFinder smsGatewayLogFinder;
	@BeanReference(type = org.opencps.sms.service.SMSLookUpQueueLocalService.class)
	protected org.opencps.sms.service.SMSLookUpQueueLocalService smsLookUpQueueLocalService;
	@BeanReference(type = SMSLookUpQueueService.class)
	protected SMSLookUpQueueService smsLookUpQueueService;
	@BeanReference(type = SMSLookUpQueuePersistence.class)
	protected SMSLookUpQueuePersistence smsLookUpQueuePersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.ClassNameLocalService.class)
	protected com.liferay.portal.kernel.service.ClassNameLocalService classNameLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.ClassNameService.class)
	protected com.liferay.portal.kernel.service.ClassNameService classNameService;
	@ServiceReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserLocalService.class)
	protected com.liferay.portal.kernel.service.UserLocalService userLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.UserService.class)
	protected com.liferay.portal.kernel.service.UserService userService;
	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
}