package com.ssafy.bablog.report.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReportInsight {
    private String summary;
    private List<String> highlights;
    private List<String> improvements;
    private List<String> recommendations;
}
