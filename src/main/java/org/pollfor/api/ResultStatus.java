package org.pollfor.api;

public enum ResultStatus {

    SUCCESS("Poll job exited successfully upon meeting exit-criteria"),
    ALL_SUCCESS("Poll jobs exited successfully upon meeting exit-criteria"),
    FAILED("Poll job failed/erred during execution"),
    ONE_OR_MORE_FAILED("One or more poll jobs failed/erred during execution"),
    ALL_FAILED("All poll jobs failed/erred during execution"),
    TIMED_OUT("Poll job timed-out"),
    ONE_OR_MORE_TIMED_OUT("One or more poll jobs timed-out"),
    ALL_TIMED_OUT("All poll jobs timed-out");

    private String status;

    ResultStatus(String status){
        this.status = status;
    }

    public String getStatusDescription(){
        return this.status;
    }
}
