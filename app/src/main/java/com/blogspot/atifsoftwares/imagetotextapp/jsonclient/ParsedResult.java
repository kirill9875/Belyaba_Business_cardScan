
package com.blogspot.atifsoftwares.imagetotextapp.jsonclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParsedResult {

    @SerializedName("TextOrientation")
    @Expose
    private String textOrientation;
    @SerializedName("FileParseExitCode")
    @Expose
    private Integer fileParseExitCode;
    @SerializedName("ParsedText")
    @Expose
    private String parsedText;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("ErrorDetails")
    @Expose
    private String errorDetails;

    public String getTextOrientation() {
        return textOrientation;
    }

    public void setTextOrientation(String textOrientation) {
        this.textOrientation = textOrientation;
    }

    public Integer getFileParseExitCode() {
        return fileParseExitCode;
    }

    public void setFileParseExitCode(Integer fileParseExitCode) {
        this.fileParseExitCode = fileParseExitCode;
    }

    public String getParsedText() {
        return parsedText;
    }

    public void setParsedText(String parsedText) {
        this.parsedText = parsedText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

}
