//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.02 at 04:08:16 PM ICT 
//


package org.opencps.thirdparty.system.nsw.vt.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Content complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Content">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}DS_Loi" minOccurs="0"/>
 *         &lt;element ref="{}KetQuaXuLy" minOccurs="0"/>
 *         &lt;element ref="{}Phanhoi_Yeucau_Sua" minOccurs="0"/>
 *         &lt;element ref="{}ResultOfExtending" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Content", propOrder = {
    "dsLoi",
    "ketQuaXuLy",
    "phanhoiYeucauSua",
    "resultOfExtending"
})
public class Content {

    @XmlElement(name = "DS_Loi")
    protected DSLoi dsLoi;
    @XmlElement(name = "KetQuaXuLy")
    protected KetQuaXuLy ketQuaXuLy;
    @XmlElement(name = "Phanhoi_Yeucau_Sua")
    protected PhanhoiYeucauSua phanhoiYeucauSua;
    @XmlElement(name = "ResultOfExtending")
    protected ResultOfExtending resultOfExtending;

    /**
     * Gets the value of the dsLoi property.
     * 
     * @return
     *     possible object is
     *     {@link DSLoi }
     *     
     */
    public DSLoi getDSLoi() {
        return dsLoi;
    }

    /**
     * Sets the value of the dsLoi property.
     * 
     * @param value
     *     allowed object is
     *     {@link DSLoi }
     *     
     */
    public void setDSLoi(DSLoi value) {
        this.dsLoi = value;
    }

    /**
     * Gets the value of the ketQuaXuLy property.
     * 
     * @return
     *     possible object is
     *     {@link KetQuaXuLy }
     *     
     */
    public KetQuaXuLy getKetQuaXuLy() {
        return ketQuaXuLy;
    }

    /**
     * Sets the value of the ketQuaXuLy property.
     * 
     * @param value
     *     allowed object is
     *     {@link KetQuaXuLy }
     *     
     */
    public void setKetQuaXuLy(KetQuaXuLy value) {
        this.ketQuaXuLy = value;
    }

    /**
     * Gets the value of the phanhoiYeucauSua property.
     * 
     * @return
     *     possible object is
     *     {@link PhanhoiYeucauSua }
     *     
     */
    public PhanhoiYeucauSua getPhanhoiYeucauSua() {
        return phanhoiYeucauSua;
    }

    /**
     * Sets the value of the phanhoiYeucauSua property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhanhoiYeucauSua }
     *     
     */
    public void setPhanhoiYeucauSua(PhanhoiYeucauSua value) {
        this.phanhoiYeucauSua = value;
    }

    /**
     * Gets the value of the resultOfExtending property.
     * 
     * @return
     *     possible object is
     *     {@link ResultOfExtending }
     *     
     */
    public ResultOfExtending getResultOfExtending() {
        return resultOfExtending;
    }

    /**
     * Sets the value of the resultOfExtending property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultOfExtending }
     *     
     */
    public void setResultOfExtending(ResultOfExtending value) {
        this.resultOfExtending = value;
    }

}
