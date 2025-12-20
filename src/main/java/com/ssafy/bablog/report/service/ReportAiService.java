package com.ssafy.bablog.report.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.bablog.report.service.dto.ReportInsight;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReportAiService {

    private static final String DAILY_SYSTEM_PROMPT = """
        너는 영양 코치다. 제공된 JSON 데이터를 바탕으로 한국어로만 평가를 작성한다.
        반드시 JSON만 출력하고 마크다운을 사용하지 않는다.
        출력 스키마:
        {
          "score": 0-100 정수,
          "grade": "매우 우수|우수|보통|개선 필요|집중 개선 필요",
          "summary": "요약 2~3문장",
          "highlights": ["잘한 점 2~3개"],
          "improvements": ["개선점 2~3개"],
          "recommendations": ["내일 바로 할 수 있는 행동 2~3개"],
          "riskFlags": ["리스크 0~3개"],
          "nutrientScores": {"kcal": 0-25, "macroBalance": 0-20, "protein": 0-10, "sugar": 0-8, "natrium": 0-8}
        }
        recommendations는 반드시 '언제/무엇/얼마'가 들어간 실행형 문장으로 작성한다.
        예: "내일 점심에 닭가슴살 100g + 채소 2종 추가하기"
        개선점은 원인과 결과를 1문장에 포함한다.
        """;

    private static final String WEEKLY_SYSTEM_PROMPT = """
        너는 영양 코치다. 제공된 JSON 데이터를 바탕으로 한국어로만 주간 평가를 작성한다.
        반드시 JSON만 출력하고 마크다운을 사용하지 않는다.
        출력 스키마:
        {
          "score": 0-100 정수,
          "grade": "매우 우수|우수|보통|개선 필요|집중 개선 필요",
          "consistencyScore": 0-100 정수,
          "summary": "요약 2~3문장",
          "highlights": ["잘한 점 2~3개"],
          "improvements": ["개선점 2~3개"],
          "recommendations": ["다음 주 실행 계획 2~3개"],
          "riskFlags": ["리스크 0~3개"],
          "trend": {
            "patternSummary": "패턴 요약 1문장",
            "bestDay": "YYYY-MM-DD",
            "worstDay": "YYYY-MM-DD",
            "nextWeekFocus": "다음 주 집중 포인트 1문장"
          }
        }
        recommendations는 반드시 '언제/무엇/얼마'가 들어간 실행형 문장으로 작성한다.
        개선점은 원인과 결과를 1문장에 포함한다.
        """;

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public ReportAiService(ChatClient.Builder builder, ObjectMapper objectMapper) {
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
    }

    public <T> T generateDailyInsight(Map<String, Object> payload, Class<T> resultType) {
        return generateInsight(payload, DAILY_SYSTEM_PROMPT, resultType);
    }

    public <T> T generateWeeklyInsight(Map<String, Object> payload, Class<T> resultType) {
        return generateInsight(payload, WEEKLY_SYSTEM_PROMPT, resultType);
    }

    private <T> T generateInsight(Map<String, Object> payload, String systemPrompt, Class<T> resultType) {
        try {
            String userPrompt = objectMapper.writeValueAsString(payload);
            String content = chatClient.prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .call()
                    .content();
            return objectMapper.readValue(content, resultType);
        } catch (Exception e) {
            throw new IllegalStateException("AI 응답 파싱 실패", e);
        }
    }
}
