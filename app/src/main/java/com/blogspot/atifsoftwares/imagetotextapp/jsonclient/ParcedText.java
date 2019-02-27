
package com.blogspot.atifsoftwares.imagetotextapp.jsonclient;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParcedText {

    @SerializedName("ParsedResults")
    @Expose
    private List<ParsedResult> parsedResults = null;
    @SerializedName("OCRExitCode")
    @Expose
    private Integer oCRExitCode;
    @SerializedName("IsErroredOnProcessing")
    @Expose
    private Boolean isErroredOnProcessing;
    @SerializedName("ProcessingTimeInMilliseconds")
    @Expose
    private String processingTimeInMilliseconds;
    @SerializedName("SearchablePDFURL")
    @Expose
    private String searchablePDFURL;

    public List<ParsedResult> getParsedResults() {
        return parsedResults;
    }

    public void setParsedResults(List<ParsedResult> parsedResults) {
        this.parsedResults = parsedResults;
    }

    public Integer getOCRExitCode() {
        return oCRExitCode;
    }

    public void setOCRExitCode(Integer oCRExitCode) {
        this.oCRExitCode = oCRExitCode;
    }

    public Boolean getIsErroredOnProcessing() {
        return isErroredOnProcessing;
    }

    public void setIsErroredOnProcessing(Boolean isErroredOnProcessing) {
        this.isErroredOnProcessing = isErroredOnProcessing;
    }

    public String getProcessingTimeInMilliseconds() {
        return processingTimeInMilliseconds;
    }

    public void setProcessingTimeInMilliseconds(String processingTimeInMilliseconds) {
        this.processingTimeInMilliseconds = processingTimeInMilliseconds;
    }

    public String getSearchablePDFURL() {
        return searchablePDFURL;
    }

    public void setSearchablePDFURL(String searchablePDFURL) {
        this.searchablePDFURL = searchablePDFURL;
    }

}
