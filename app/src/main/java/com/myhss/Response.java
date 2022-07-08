package com.myhss;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by virendra on 3/10/16.
 */

public class Response {
    @SerializedName("FIRST_NAME")
    @Expose
    private String fIRSTNAME;
    @SerializedName("WEDDING_ANNIVERSARY")
    @Expose
    private String wEDDINGANNIVERSARY;
    @SerializedName("MIDDLE_NAME")
    @Expose
    private String mIDDLENAME;
    @SerializedName("BIRTH_DATE")
    @Expose
    private String bIRTHDATE;
    @SerializedName("CONTACT_NUMBER")
    @Expose
    private String cONTACTNUMBER;
    @SerializedName("LAST_NAME")
    @Expose
    private String lASTNAME;


    /**
     * @return The fIRSTNAME
     */
    public String getFIRSTNAME() {
        return fIRSTNAME;
    }

    /**
     * @param fIRSTNAME The FIRST_NAME
     */
    public void setFIRSTNAME(String fIRSTNAME) {
        this.fIRSTNAME = fIRSTNAME;
    }

    /**
     * @return The wEDDINGANNIVERSARY
     */
    public String getWEDDINGANNIVERSARY() {
        return wEDDINGANNIVERSARY;
    }

    /**
     * @param wEDDINGANNIVERSARY The WEDDING_ANNIVERSARY
     */
    public void setWEDDINGANNIVERSARY(String wEDDINGANNIVERSARY) {
        this.wEDDINGANNIVERSARY = wEDDINGANNIVERSARY;
    }

    /**
     * @return The mIDDLENAME
     */
    public String getMIDDLENAME() {
        return mIDDLENAME;
    }

    /**
     * @param mIDDLENAME The MIDDLE_NAME
     */
    public void setMIDDLENAME(String mIDDLENAME) {
        this.mIDDLENAME = mIDDLENAME;
    }

    /**
     * @return The bIRTHDATE
     */
    public String getBIRTHDATE() {
        return bIRTHDATE;
    }

    /**
     * @param bIRTHDATE The BIRTH_DATE
     */
    public void setBIRTHDATE(String bIRTHDATE) {
        this.bIRTHDATE = bIRTHDATE;
    }

    /**
     * @return The cONTACTNUMBER
     */
    public String getCONTACTNUMBER() {
        return cONTACTNUMBER;
    }

    /**
     * @param cONTACTNUMBER The CONTACT_NUMBER
     */
    public void setCONTACTNUMBER(String cONTACTNUMBER) {
        this.cONTACTNUMBER = cONTACTNUMBER;
    }

    /**
     * @return The lASTNAME
     */
    public String getLASTNAME() {
        return lASTNAME;
    }

    /**
     * @param lASTNAME The LAST_NAME
     */
    public void setLASTNAME(String lASTNAME) {
        this.lASTNAME = lASTNAME;
    }

}