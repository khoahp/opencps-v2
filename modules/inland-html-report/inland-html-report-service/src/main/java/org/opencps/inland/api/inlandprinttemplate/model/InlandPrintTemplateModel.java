//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.19 at 02:38:24 PM ICT 
//


package org.opencps.inland.api.inlandprinttemplate.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="printTemplateId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="createDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modifiedDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dossierPartNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileTemplateNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="templateNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="createUserId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="employeeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="formTemplate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defaultCss" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="originalDocumentURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "printTemplateId",
    "createDate",
    "modifiedDate",
    "serviceCode",
    "dossierPartNo",
    "fileTemplateNo",
    "templateNo",
    "createUserId",
    "employeeId",
    "formTemplate",
    "defaultCss",
    "originalDocumentURL"
})
@XmlRootElement(name = "InlandPrintTemplateModel")
public class InlandPrintTemplateModel {

    protected Long printTemplateId;
    protected String createDate;
    protected String modifiedDate;
    protected String serviceCode;
    protected String dossierPartNo;
    protected String fileTemplateNo;
    protected String templateNo;
    protected Long createUserId;
    protected Long employeeId;
    protected String formTemplate;
    protected String defaultCss;
    protected String originalDocumentURL;

    /**
     * Gets the value of the printTemplateId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPrintTemplateId() {
        return printTemplateId;
    }

    /**
     * Sets the value of the printTemplateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPrintTemplateId(Long value) {
        this.printTemplateId = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateDate(String value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the modifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiedDate() {
        return modifiedDate;
    }

    /**
     * Sets the value of the modifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedDate(String value) {
        this.modifiedDate = value;
    }

    /**
     * Gets the value of the serviceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * Sets the value of the serviceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceCode(String value) {
        this.serviceCode = value;
    }

    /**
     * Gets the value of the dossierPartNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDossierPartNo() {
        return dossierPartNo;
    }

    /**
     * Sets the value of the dossierPartNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDossierPartNo(String value) {
        this.dossierPartNo = value;
    }

    /**
     * Gets the value of the fileTemplateNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileTemplateNo() {
        return fileTemplateNo;
    }

    /**
     * Sets the value of the fileTemplateNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileTemplateNo(String value) {
        this.fileTemplateNo = value;
    }

    /**
     * Gets the value of the templateNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateNo() {
        return templateNo;
    }

    /**
     * Sets the value of the templateNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateNo(String value) {
        this.templateNo = value;
    }

    /**
     * Gets the value of the createUserId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCreateUserId() {
        return createUserId;
    }

    /**
     * Sets the value of the createUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCreateUserId(Long value) {
        this.createUserId = value;
    }

    /**
     * Gets the value of the employeeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the value of the employeeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEmployeeId(Long value) {
        this.employeeId = value;
    }

    /**
     * Gets the value of the formTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormTemplate() {
        return formTemplate;
    }

    /**
     * Sets the value of the formTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormTemplate(String value) {
        this.formTemplate = value;
    }

    /**
     * Gets the value of the defaultCss property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultCss() {
        return defaultCss;
    }

    /**
     * Sets the value of the defaultCss property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultCss(String value) {
        this.defaultCss = value;
    }

    /**
     * Gets the value of the originalDocumentURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalDocumentURL() {
        return originalDocumentURL;
    }

    /**
     * Sets the value of the originalDocumentURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalDocumentURL(String value) {
        this.originalDocumentURL = value;
    }

}
