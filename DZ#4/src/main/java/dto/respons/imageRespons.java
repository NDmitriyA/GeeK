package dto.respons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "data",
            "success",
            "status"
    })
   public class imageRespons {

        @JsonProperty("data")
        private Boolean data;
        @JsonProperty("success")
        private Boolean success;
        @JsonProperty("status")
        private Integer status;

    }

