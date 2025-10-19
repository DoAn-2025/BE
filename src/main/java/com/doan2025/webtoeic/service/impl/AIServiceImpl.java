package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.Constants;
import com.doan2025.webtoeic.constants.enums.ERangeTopic;
import com.doan2025.webtoeic.constants.enums.EScoreScale;
import com.doan2025.webtoeic.dto.response.QuestionResponse;
import com.doan2025.webtoeic.repository.*;
import com.doan2025.webtoeic.service.AIService;
import com.doan2025.webtoeic.service.ReaderService;
import com.doan2025.webtoeic.utils.ConvertUtil;
import com.doan2025.webtoeic.utils.JwtUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final ExplanationQuestionRepository explanationQuestionRepository;
    private final RangeTopicRepository rangeTopicRepository;
    private final ScoreScaleRepository scoreScaleRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ConvertUtil convertUtil;
    private final ChatClient chatClient;
    private final ReaderService readerService;

    @Value("${GOOGLE_API_KEY}")
    private String geminiApiKey;

    public AIServiceImpl(ChatClient.Builder builder, AnswerRepository answerRepository, QuestionRepository questionRepository,
                         ExplanationQuestionRepository explanationQuestionRepository, RangeTopicRepository rangeTopicRepository,
                         ScoreScaleRepository scoreScaleRepository, UserRepository userRepository, JwtUtil jwtUtil, ConvertUtil convertUtil, ReaderService readerService) {
        this.chatClient = builder.build();
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.explanationQuestionRepository = explanationQuestionRepository;
        this.rangeTopicRepository = rangeTopicRepository;
        this.scoreScaleRepository = scoreScaleRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.convertUtil = convertUtil;
        this.readerService = readerService;
    }

    @Override
    public String checkCallAI() {
        SystemMessage systemMessage = new SystemMessage("""
                You are LearnEZ.AI
                You should response with a formal voice
                """);

        UserMessage userMessage = new UserMessage("Explain how AI works in a few words");

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }

    @Override
    public List<QuestionResponse> analysisWithAI(String url) {

        String readerText = readerService.readContentOfFile(url);
        String rangeTopic = getRangeTopicString();
        String scoreScale = getScoreScaleString();

        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0D)
                .build();

        String systemPrompt = Constants.PROMPT_AI;

        systemPrompt.replace(Constants.CATEGORY_TOPIC, rangeTopic);
        systemPrompt.replace(Constants.DIFFICULTY_LEVEL, scoreScale);

        String userPrompt = Constants.USER_PROMPT + readerText;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(chatOptions)
                .call()
                .entity(new ParameterizedTypeReference<List<QuestionResponse>>() {
                });
    }

    private String getRangeTopicString() {
        StringBuilder result = new StringBuilder();
        for (ERangeTopic rangeTopic : ERangeTopic.values()) {
            result.append(rangeTopic.name().toLowerCase()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    private String getScoreScaleString() {
        StringBuilder result = new StringBuilder();
        for (EScoreScale scoreScale : EScoreScale.values()) {
            result.append(scoreScale.name().toLowerCase()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }
}
