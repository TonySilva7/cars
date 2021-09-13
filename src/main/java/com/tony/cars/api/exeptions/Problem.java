package com.tony.cars.api.exeptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Problem {

    private Integer status;
    private OffsetDateTime timestamp;
    private String title;
    private String path;

    private List<FieldException> fieldExceptions;
}

