package org.opencps.api.controller.impl;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.httpclient.util.HttpURLConnection;
import org.opencps.api.controller.ProcessPluginManagement;
import org.opencps.api.controller.exception.ErrorMsg;
import org.opencps.auth.api.BackendAuth;
import org.opencps.auth.api.BackendAuthImpl;
import org.opencps.auth.api.exception.UnauthenticationException;
import org.opencps.auth.api.exception.UnauthorizationException;
import org.opencps.dossiermgt.action.DeliverableTypesActions;
import org.opencps.dossiermgt.action.DossierFileActions;
import org.opencps.dossiermgt.action.impl.DeliverableTypesActionsImpl;
import org.opencps.dossiermgt.action.impl.DossierFileActionsImpl;
import org.opencps.dossiermgt.action.util.AutoFillFormData;
import org.opencps.dossiermgt.constants.DeliverableTypesTerm;
import org.opencps.dossiermgt.model.DeliverableType;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierAction;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.ProcessPlugin;
import org.opencps.dossiermgt.service.DeliverableTypeLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierActionLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierPartLocalServiceUtil;
import org.opencps.dossiermgt.service.ProcessPluginLocalServiceUtil;
import org.opencps.dossiermgt.service.comparator.DossierFileComparator;
import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.service.EmployeeLocalServiceUtil;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusException;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

public class ProcessPluginManagementImpl implements ProcessPluginManagement {

	@Override
	public Response getPlugins(HttpServletRequest request, HttpHeaders header, Company company, Locale locale,
			User user, ServiceContext serviceContext, String id) {

		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));

		BackendAuth auth = new BackendAuthImpl();

		try {

			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}

			Dossier dossier = getDossier(id, groupId);

			if (Validator.isNotNull(dossier)) {

				long dossierActionId = dossier.getDossierActionId();

				if (dossierActionId != 0) {

					DossierAction dossierAction = DossierActionLocalServiceUtil.getDossierAction(dossierActionId);

					String stepCode = dossierAction.getStepCode();
					_log.info("STEP CODE: " + stepCode);
					_log.info("SERVICE PROCESS ID: " + dossierAction.getServiceProcessId());
					List<ProcessPlugin> plugins = ProcessPluginLocalServiceUtil
							.getProcessPlugins(dossierAction.getServiceProcessId(), stepCode);

					JSONObject results = JSONFactoryUtil.createJSONObject();

					int total = plugins.size();

					results.put("total", total);

					JSONArray dataArr = JSONFactoryUtil.createJSONArray();

					for (ProcessPlugin plugin : plugins) {
						JSONObject elm = JSONFactoryUtil.createJSONObject();

						elm.put("processPluginId", plugin.getPrimaryKey());
						elm.put("pluginName", plugin.getPluginName());

						dataArr.put(elm);
					}

					results.put("data", dataArr);

					return Response.status(200).entity(JSONFactoryUtil.looseSerialize(results)).build();

				} else {
					throw new Exception("The dossier wasn't on process");
				}

			} else {
				throw new Exception("Cant get dossier with id_" + id);
			}

		} catch (Exception e) {
			_log.error(e);

			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}

	}

	@Override
	public Response getFormData(HttpServletRequest request, HttpHeaders header, Company company, Locale locale,
			User user, ServiceContext serviceContext, String id, long pluginid) {

		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));

		BackendAuth auth = new BackendAuthImpl();

		try {

			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}

			Dossier dossier = getDossier(id, groupId);

			if (Validator.isNotNull(dossier)) {

				long dossierActionId = dossier.getDossierActionId();

				if (dossierActionId != 0) {

					ProcessPlugin plugin = ProcessPluginLocalServiceUtil.getProcessPlugin(pluginid);

					String formData = plugin.getSampleData();

					if (formData.startsWith("#")) {
						return Response.status(200).entity(formData).build();

					} else {

						JSONObject result = JSONFactoryUtil.createJSONObject(plugin.getSampleData());

						return Response.status(200).entity(JSONFactoryUtil.looseSerialize(result)).build();
					}

				} else {
					throw new Exception("The dossier wasn't on process");
				}

			} else {
				throw new Exception("Cant get dossier with id_" + id);
			}

		} catch (Exception e) {
			_log.error(e);

			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}

	}

	@Override
	public Response getFormScript(HttpServletRequest request, HttpHeaders header, Company company, Locale locale,
			User user, ServiceContext serviceContext, String id, long pluginid) {
		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));

		BackendAuth auth = new BackendAuthImpl();

		try {

			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}

			Dossier dossier = getDossier(id, groupId);

			if (Validator.isNotNull(dossier)) {

				long dossierActionId = dossier.getDossierActionId();

				if (dossierActionId != 0) {

					ProcessPlugin plugin = ProcessPluginLocalServiceUtil.getProcessPlugin(pluginid);

					String formData = plugin.getPluginForm();

					if (formData.startsWith("#")) {
						return Response.status(200).entity(JSONFactoryUtil.looseSerialize(formData)).build();

					} else {

						JSONObject result = JSONFactoryUtil.createJSONObject(plugin.getPluginForm());

						return Response.status(200).entity(JSONFactoryUtil.looseSerialize(result)).build();
					}

				} else {
					throw new Exception("The dossier wasn't on process");
				}

			} else {
				throw new Exception("Cant get dossier with id_" + id);
			}

		} catch (Exception e) {
			_log.error(e);

			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}

	}

	private Dossier getDossier(String id, long groupId) {
		Dossier dossier = null;

		long dossierId = GetterUtil.getLong(id);

		try {
			if (dossierId != 0) {
				dossier = DossierLocalServiceUtil.getDossier(dossierId);
			} else {
				dossier = DossierLocalServiceUtil.getByRef(groupId, id);
			}
		} catch (Exception e) {
			_log.info("Cant get dossier_" + id);
		}

		return dossier;
	}

	@Override
	public Response getPreview(HttpServletRequest request, HttpHeaders header, Company company, Locale locale,
			User user, ServiceContext serviceContext, String id, long pluginid) {

		BackendAuth auth = new BackendAuthImpl();

		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));

		try {

			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}

			Dossier dossier = getDossier(id, groupId);

			if (Validator.isNotNull(dossier)) {

				long dossierActionId = dossier.getDossierActionId();

				if (dossierActionId != 0) {

					ProcessPlugin plugin = ProcessPluginLocalServiceUtil.getProcessPlugin(pluginid);

					// String formReport = plugin.getPluginForm();

					String formCode = plugin.getSampleData();

					String pluginForm = plugin.getPluginForm();

					String[] splipPluginForms = StringUtil.split(pluginForm, StringPool.AT);

					boolean original = false;

					if (splipPluginForms.length == 3 && splipPluginForms[2].contentEquals("original")) {
						original = true;
					}

					boolean autoRun = plugin.getAutoRun();

					String formData = StringPool.BLANK;
					String formReport = StringPool.BLANK;

					if (formCode.startsWith("#")) {

						if (original) {

							String strFileTemplateNo = StringUtil.replaceFirst(formCode, "#", StringPool.BLANK);

							DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFileByDID_FTNO_First(
									dossier.getDossierId(), strFileTemplateNo, false,
									new DossierFileComparator(false, "createDate", Date.class));
							
							if (Validator.isNotNull(dossierFile)) {
								formData = dossierFile.getFormData();
							} else {
								_log.info("DOSSIER_FILE_IS_NULL***** ");
							}
						} else {
							_log.info("Preview not original");
							formData = _getFormData(groupId, formCode, dossier.getDossierId(), autoRun,
									dossier.getDossierTemplateNo(), original, serviceContext);
						}

						formReport = _getFormScript(formCode, dossier.getDossierId());
					}

					_log.info("Form data to preview: " + formData + ", auto run: " + autoRun);
					Message message = new Message();

					message.put("formReport", formReport);

					message.put("formData", formData);

					message.setResponseId(String.valueOf(dossier.getPrimaryKeyObj()));
					message.setResponseDestinationName("jasper/engine/preview/callback");

					try {
						String previewResponse = (String) MessageBusUtil
								.sendSynchronousMessage("jasper/engine/preview/destination", message, 10000);

						// JSONObject jsonObject =
						// JSONFactoryUtil.createJSONObject();

						if (Validator.isNotNull(previewResponse)) {
							// jsonObject =
							// JSONFactoryUtil.createJSONObject(previewResponse);
						}

						// String fileDes = jsonObject.getString("fileDes");

						File file = new File(previewResponse);

						ResponseBuilder responseBuilder = Response.ok((Object) file);

						responseBuilder.header("Content-Disposition",
								"attachment; filename=\"" + file.getName() + "\"");
						responseBuilder.header("Content-Type", "application/pdf");

						return responseBuilder.build();

					} catch (MessageBusException e) {
						throw new Exception("Preview rendering not avariable");
					}

				} else {
					throw new Exception("The dossier wasn't on process");
				}

			} else {
				throw new Exception("Cant get dossier with id_" + id);
			}

		} catch (Exception e) {
			_log.error(e);

			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}

	}

	private boolean equalsFormData(String oldFormData, String newFormData) {
		try {
			JSONObject oldObj = JSONFactoryUtil.createJSONObject(oldFormData);
			JSONObject newObj = JSONFactoryUtil.createJSONObject(newFormData);
			if (oldObj.has("LicenceNo") && newObj.has("LicenceNo")) {
				if (oldObj.getString("LicenceNo").equals(newObj.getString("LicenceNo"))) {
					return true;
				}
			}			
			if (oldObj.has("RegistrationNumber") && newObj.has("RegistrationNumber")) {
				if (oldObj.getString("RegistrationNumber").equals(newObj.getString("RegistrationNumber"))) {
					return true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private Log _log2 = LogFactoryUtil.getLog(ProcessPluginManagementImpl.class);

	private String _getFormData(long groupId, String fileTemplateNo, long dossierId, boolean autoRun,
			String dossierTemplateNo, boolean original, ServiceContext context) {

		String formData = StringPool.BLANK;
		fileTemplateNo = StringUtil.replaceFirst(fileTemplateNo, "#", StringPool.BLANK);
		_log.info("Get form data to preview: " + fileTemplateNo);
		try {
			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);
			DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFileByDID_FTNO_First(dossierId,
					fileTemplateNo, false, new DossierFileComparator(false, "createDate", Date.class));
			List<DossierFile> lstDossierFiles = DossierFileLocalServiceUtil.getDossierFileByDID_FTNO(dossierId,
					fileTemplateNo, false);

			List<DossierPart> lstParts = DossierPartLocalServiceUtil.getByTemplateNo(groupId,
					dossier.getDossierTemplateNo());
			DossierPart dossierPart = null;

			for (DossierPart part : lstParts) {
				if (part.getFileTemplateNo().equals(fileTemplateNo)) {
					dossierPart = part;
					break;
				}
			}

			formData = AutoFillFormData.sampleDataBinding(dossierPart.getSampleData(), dossierId, context);

			_log.info(StringUtil.shorten(formData, 300));

			DeliverableTypesActions dtAction = new DeliverableTypesActionsImpl();

			if (Validator.isNotNull(dossierPart.getDeliverableType())) {
				DeliverableType dlt = DeliverableTypeLocalServiceUtil.getByCode(groupId,
						dossierPart.getDeliverableType());
				if (dlt != null) {

					_log.info("DeliverableType is NOT NULL");

					String allowKey = dtAction.getMappingKey(DeliverableTypesTerm.MAPPING_ALLOW, dlt);
					String acceptedKey = dtAction.getMappingKey(DeliverableTypesTerm.MAPPING_ALLOW, dlt);

					if (original) {

						_log.info("original is true");

						String mappingData = dlt.getMappingData();
						JSONObject mappingDataObj = JSONFactoryUtil.createJSONObject(mappingData);
						if (mappingDataObj.has(DeliverableTypesTerm.DELIVERABLES_KEY)) {
							String deliverables = mappingDataObj.getString(DeliverableTypesTerm.DELIVERABLES_KEY);
							if (Validator.isNotNull(dossierFile)) {
								formData = dossierFile.getFormData();
							}
							_log.info(
									"DeliverableTypesTerm.DELIVERABLES_KEY : " + DeliverableTypesTerm.DELIVERABLES_KEY);

							_log.info("mappingDataObj : " + mappingDataObj);

							_log.info("deliverables : " + deliverables);

							JSONObject formDataObj = JSONFactoryUtil.createJSONObject(formData);
							JSONArray deliverableListArr = JSONFactoryUtil.createJSONArray();

							if (Validator.isNull(deliverables)) {
								_log.info("deliverables is null");
							} else {
								_log.info("deliverables is NOT null");

								if (formDataObj.has(deliverables)) {
									_log.info("formDataObj has deliverables");

									JSONArray deliverablesArr = JSONFactoryUtil
											.createJSONArray(formDataObj.getString(deliverables));

									for (int i = 0; i < deliverablesArr.length(); i++) {

										JSONObject deliverableObj = deliverablesArr.getJSONObject(i);

										if (deliverableObj.has(acceptedKey) || deliverableObj.has(allowKey)) {
											String value = (deliverableObj.has(acceptedKey)
													? deliverableObj.getString(acceptedKey)
													: deliverableObj.getString(allowKey));
											if ("1".equals(value)) {
												JSONObject newFormDataObj = JSONFactoryUtil.createJSONObject();

												Iterator<?> keys = formDataObj.keys();

												while (keys.hasNext()) {
													String key = (String) keys.next();
													if (!key.equals(deliverables)) {
														newFormDataObj.put(key, formDataObj.get(key));
													}
												}

												keys = deliverableObj.keys();

												while (keys.hasNext()) {
													String key = (String) keys.next();
													newFormDataObj.put(key, deliverableObj.get(key));
												}

												deliverableListArr.put(newFormDataObj);
											}
										}
									}
								}
								formData = deliverableListArr.toJSONString();
							}
						} else {

						}

					} else {

						_log.info("DeliverableType is NULL *****");

						String employeeName = StringPool.BLANK;
						try {
							Employee employee = EmployeeLocalServiceUtil.fetchByF_mappingUserId(groupId,
									context.getUserId());
							employeeName = employee.getFullName();
						} catch (Exception e) {
							// TODO: handle exception
							_log.info(e.getMessage());
						}

						String mappingData = dlt.getMappingData();
						JSONObject mappingDataObj = JSONFactoryUtil.createJSONObject(mappingData);
						if (mappingDataObj.has(DeliverableTypesTerm.DELIVERABLES_KEY)) {
							String deliverables = mappingDataObj.getString(DeliverableTypesTerm.DELIVERABLES_KEY);
							String deliverableCodeKey = dtAction
									.getMappingKey(DeliverableTypesTerm.MAPPING_DELIVERABLE_CODE, dlt);
							String signNameKey = dtAction.getMappingKey(DeliverableTypesTerm.MAPPING_SIGNNAME, dlt);

							if (Validator.isNull(deliverables)) {
								_log.info("deliverables is NULL *****");

								JSONObject formDataObj = JSONFactoryUtil.createJSONObject(formData);
								if (dossierFile != null) {
									formDataObj.put(deliverableCodeKey, dossierFile.getDeliverableCode());									
								}
								formData = formDataObj.toJSONString();
								DossierFileActions actions = new DossierFileActionsImpl();

								if (Validator.isNull(dossierFile)) {
									if (autoRun) {
										// create DossierFile

										dossierFile = actions.addDossierFile(groupId, dossierId,
												PortalUUIDUtil.generate(), dossierTemplateNo, dossierPart.getPartNo(),
												fileTemplateNo, dossierPart.getPartName(), StringPool.BLANK, 0L, null,
												StringPool.BLANK, String.valueOf(false), context);

										actions.updateDossierFileFormData(groupId, dossierId,
												dossierFile.getReferenceUid(), formData, context);

									} else {
										// add temp File
									}

								} else {
									// formData = dossierFile.getFormData();
									actions.updateDossierFileFormData(groupId, dossierId, dossierFile.getReferenceUid(),
											formData, context);
								}
							} else {

								_log.info("deliverables is NOT NULL *****");

								JSONObject formDataObj = JSONFactoryUtil.createJSONObject(formData);
								DossierFileActions actions = new DossierFileActionsImpl();

								JSONArray deliverablesArr = JSONFactoryUtil
										.createJSONArray(formDataObj.getString(deliverables));
								JSONArray deliverableListArr = JSONFactoryUtil.createJSONArray();

								for (int i = 0; i < deliverablesArr.length(); i++) {

									JSONObject deliverableObj = deliverablesArr.getJSONObject(i);

									if (deliverableObj.has(acceptedKey) || deliverableObj.has(allowKey)) {
										String value = (deliverableObj.has(acceptedKey)
												? deliverableObj.getString(acceptedKey)
												: deliverableObj.getString(allowKey));
										if ("1".equals(value)) {
											JSONObject newFormDataObj = JSONFactoryUtil.createJSONObject();

											Iterator<?> keys = formDataObj.keys();

											while (keys.hasNext()) {
												String key = (String) keys.next();
												if (!key.equals(deliverables)) {
													newFormDataObj.put(key, formDataObj.get(key));
												}
											}

											keys = deliverableObj.keys();

											while (keys.hasNext()) {
												String key = (String) keys.next();
												newFormDataObj.put(key, deliverableObj.get(key));
											}

											deliverableListArr.put(newFormDataObj);
											boolean flag = false;
											DossierFile foundFile = null;
											for (DossierFile tmpFile : lstDossierFiles) {
												if (equalsFormData(tmpFile.getFormData(),
														newFormDataObj.toJSONString())) {
													flag = true;
													foundFile = tmpFile;
													break;
												}
											}

											if (!flag) {

												if (autoRun) {
													// create DossierFile

													dossierFile = actions.addDossierFile(groupId, dossierId,
															PortalUUIDUtil.generate(), dossierTemplateNo,
															dossierPart.getPartNo(), fileTemplateNo,
															dossierPart.getPartName(), StringPool.BLANK, 0L, null,
															StringPool.BLANK, String.valueOf(false), context);

													if (Validator.isNotNull(dossierFile.getDeliverableCode())) {
														newFormDataObj.put(deliverableCodeKey,
																dossierFile.getDeliverableCode());
													}

													actions.updateDossierFileFormData(groupId, dossierId,
															dossierFile.getReferenceUid(),
															newFormDataObj.toJSONString(), context);

												} else {
													// add temp File

												}

											} else {
												if (autoRun) {
													if (Validator.isNotNull(foundFile.getDeliverableCode())) {
														newFormDataObj.put(deliverableCodeKey,
																foundFile.getDeliverableCode());
													}

													actions.updateDossierFileFormData(groupId, dossierId,
															foundFile.getReferenceUid(), newFormDataObj.toJSONString(),
															context);

												} else {
													// add temp File

												}

											}

										}
									}
								}

								// formDataObj.put(deliverableCodeKey,
								// dossierFile.getDeliverableCode());
								// formDataObj.put(signNameKey, employeeName);
								formData = deliverableListArr.toJSONString();
							}
						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			_log.info("Cant get formdata with fileTemplateNo_" + fileTemplateNo);
		}

		try {

			JSONObject formDataObj = JSONFactoryUtil.createJSONObject(formData);

			JSONArray attachedFileList = formDataObj.getJSONArray("AttachedFile");

			if (Validator.isNotNull(attachedFileList) && attachedFileList.length() != 0) {

				_log.info("****attachedFileList.length() : " + attachedFileList.length());

				for (int i = 0; i < attachedFileList.length(); i++) {
					_log.info("****i : " + i);

					JSONObject elm = attachedFileList.getJSONObject(i);

					_log.info("elm VALUE : " + elm.toJSONString());

					String maTPHoSo = elm.getString("AttachedTypeCode");

					if (Validator.isNotNull(maTPHoSo)) {
						DossierPart dossierPartElm = DossierPartLocalServiceUtil.getByFileTemplateNo(groupId, maTPHoSo);

						if (Validator.isNotNull(dossierPartElm)) {
							elm.put("FullFileName", dossierPartElm.getPartName());
							elm.put("AttachedTypeName", dossierPartElm.getPartName());
						}
					}
					
					

					// attachedFileList.put(elm);
				}

			} else {
				_log.info("attachedFileList IS NULL : ");

			}

			formData = formDataObj.toJSONString();

		} catch (Exception e) {
			_log.error(e);
		}

		return formData;
	}

	private String _getFormScript(String fileTemplateNo, long dossierId) {
		String formData = StringPool.BLANK;

		fileTemplateNo = StringUtil.replaceFirst(fileTemplateNo, "#", StringPool.BLANK);

		try {

			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);

			List<DossierPart> lstParts = DossierPartLocalServiceUtil.getByTemplateNo(dossier.getGroupId(),
					dossier.getDossierTemplateNo());
			DossierPart part = null;
			for (DossierPart temppart : lstParts) {
				if (temppart.getFileTemplateNo().equals(fileTemplateNo)) {
					part = temppart;
					break;
				}
			}
			// DossierPart part =
			// DossierPartLocalServiceUtil.getByFileTemplateNo(dossier.getGroupId(),
			// fileTemplateNo);
			if (part != null) {
				DeliverableType dlt = DeliverableTypeLocalServiceUtil.getByCode(part.getGroupId(),
						part.getDeliverableType());
				if (dlt != null) {
					formData = dlt.getFormReport();
				} else {
					formData = part.getFormReport();
				}
			}

		} catch (Exception e) {
			_log.info("Cant get formdata with fileTemplateNo_" + fileTemplateNo);
		}

		return formData;
	}

	private String _getFormHtml(String fileTemplateNo, long dossierId) {
		String formData = StringPool.BLANK;

		fileTemplateNo = StringUtil.replaceFirst(fileTemplateNo, "#", StringPool.BLANK);

		try {

			Dossier dossier = DossierLocalServiceUtil.getDossier(dossierId);

			DossierPart part = DossierPartLocalServiceUtil.getByFileTemplateNo(dossier.getGroupId(), fileTemplateNo);

			formData = part.getFormScript();

		} catch (Exception e) {
			_log.info("Cant get formdata with fileTemplateNo_" + fileTemplateNo);
		}

		return formData;
	}

	Log _log = LogFactoryUtil.getLog(ProcessPluginManagementImpl.class);

	@Override
	public Response getPreviewHtml(HttpServletRequest request, HttpHeaders header, Company company, Locale locale,
			User user, ServiceContext serviceContext, String id, long pluginid) {
		BackendAuth auth = new BackendAuthImpl();

		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));

		try {

			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}

			Dossier dossier = getDossier(id, groupId);

			if (Validator.isNotNull(dossier)) {

				long dossierActionId = dossier.getDossierActionId();

				if (dossierActionId != 0) {

					ProcessPlugin plugin = ProcessPluginLocalServiceUtil.getProcessPlugin(pluginid);

					// String formReport = plugin.getPluginForm();

					String formCode = plugin.getSampleData();

					boolean autoRun = plugin.getAutoRun();

					String pluginForm = plugin.getPluginForm();

					String[] splipPluginForms = StringUtil.split(pluginForm, StringPool.AT);

					boolean original = false;

					if (splipPluginForms.length == 3 && splipPluginForms[2].contentEquals("original")) {
						original = true;
					}

					String formData = StringPool.BLANK;
					String formReport = StringPool.BLANK;

					if (formCode.startsWith("#")) {
						formData = _getFormData(groupId, formCode, dossier.getDossierId(), autoRun,
								dossier.getDossierTemplateNo(), original, serviceContext);

						formReport = _getFormHtml(formCode, dossier.getDossierId());
					}

					JSONObject result = JSONFactoryUtil.createJSONObject();

					String fileTemplateNo = StringUtil.replaceFirst(formCode, "#", StringPool.BLANK);

					DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFileByDID_FTNO_First(
							dossier.getDossierId(), fileTemplateNo, false,
							new DossierFileComparator(false, "createDate", Date.class));

					DossierPart dossierPart = DossierPartLocalServiceUtil.getByFileTemplateNo(groupId, fileTemplateNo);

					long dossierFileId = 0;
					String partNo = StringPool.BLANK;

					if (Validator.isNotNull(dossierFile)) {
						dossierFileId = dossierFile.getDossierFileId();
					}

					if (Validator.isNotNull(dossierPart)) {
						partNo = dossierPart.getPartNo();
					}

					result.put("partNo", partNo);
					result.put("dossierFileId", dossierFileId);
					result.put("formReport", formReport);
					result.put("formData", formData);

					return Response.status(HttpURLConnection.HTTP_OK).entity(JSONFactoryUtil.looseSerialize(result))
							.build();

				} else {
					throw new Exception("The dossier wasn't on process");
				}

			} else {
				throw new Exception("Cant get dossier with id_" + id);
			}

		} catch (Exception e) {
			_log.error(e);

			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}
	}

	@Override
	public Response getPreviewHtmlInstance(HttpServletRequest request, HttpHeaders header, Company company,
			Locale locale, User user, ServiceContext serviceContext, String id) {

		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));
		String partNo = GetterUtil.getString(header.getHeaderString("partNo"));

		try {

			Dossier dossier = getDossier(id, groupId);

			if (Validator.isNotNull(dossier)) {
				JSONObject result = JSONFactoryUtil.createJSONObject();

				DossierPart dossierPart = DossierPartLocalServiceUtil.fetchByTemplatePartNo(groupId,
						dossier.getDossierTemplateNo(), partNo);

				DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFileByDID_FTNO_First(
						dossier.getDossierId(), dossierPart.getFileTemplateNo(), false,
						new DossierFileComparator(false, "createDate", Date.class));

				long dossierFileId = 0;

				if (Validator.isNotNull(dossierFile)) {
					dossierFileId = dossierFile.getDossierFileId();
				}

				if (Validator.isNotNull(dossierPart)) {
					partNo = dossierPart.getPartNo();
				}

				result.put("partNo", partNo);
				result.put("dossierFileId", dossierFileId);
				result.put("formReport", dossierFile.getFormScript());
				result.put("formData", dossierFile.getFormData());

				return Response.status(HttpURLConnection.HTTP_OK).entity(JSONFactoryUtil.looseSerialize(result))
						.build();

			} else {
				throw new Exception("Cant get dossier with id_" + id);
			}

		} catch (Exception e) {
			_log.error(e);

			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}
	}

	@Override
	public Response getPreviewPdfInstance(HttpServletRequest request, HttpHeaders header, Company company,
			Locale locale, User user, ServiceContext serviceContext, String id, long pluginid) {
		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));
		String partNo = GetterUtil.getString(header.getHeaderString("partNo"));

		try {
			Dossier dossier = getDossier(id, groupId);

			if (Validator.isNotNull(dossier)) {

				DossierPart dossierPart = DossierPartLocalServiceUtil.fetchByTemplatePartNo(groupId,
						dossier.getDossierTemplateNo(), partNo);

				DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFileByDID_FTNO_First(
						dossier.getDossierId(), dossierPart.getFileTemplateNo(), false,
						new DossierFileComparator(false, "createDate", Date.class));

				Message message = new Message();

				message.put("formReport", dossierPart.getFormReport());

				message.put("formData", dossierFile.getFormData());

				message.setResponseId(String.valueOf(dossier.getPrimaryKeyObj()));
				message.setResponseDestinationName("jasper/engine/preview/callback");

				try {
					String previewResponse = (String) MessageBusUtil
							.sendSynchronousMessage("jasper/engine/preview/destination", message, 10000);

					if (Validator.isNotNull(previewResponse)) {
						// jsonObject =
					}

					// String fileDes = jsonObject.getString("fileDes");

					File file = new File(previewResponse);

					ResponseBuilder responseBuilder = Response.ok((Object) file);

					responseBuilder.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
					responseBuilder.header("Content-Type", "application/pdf");

					return responseBuilder.build();

				} catch (MessageBusException e) {
					throw new Exception("Preview rendering not avariable");
				}

			} else {
				throw new Exception("Cant get dossier with id_" + id);
			}

		} catch (Exception e) {

			_log.error(e);

			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}

	}

}