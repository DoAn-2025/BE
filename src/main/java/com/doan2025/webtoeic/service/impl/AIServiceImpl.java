package com.doan2025.webtoeic.service.impl;

import com.doan2025.webtoeic.constants.Constants;
import com.doan2025.webtoeic.constants.enums.ERangeTopic;
import com.doan2025.webtoeic.constants.enums.EScoreScale;
import com.doan2025.webtoeic.domain.RangeTopic;
import com.doan2025.webtoeic.domain.ScoreScale;
import com.doan2025.webtoeic.dto.response.AiResponse;
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
    public AiResponse analysisWithAI(String url) {

        String readerText = readerService.readContentOfFile(url);
        String rangeTopic = getRangeTopicFromDBString();
        String scoreScale = getScoreScaleFromDBString();

        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0D)
                .build();

        String systemPrompt = Constants.PROMPT_AI;

        systemPrompt.replace(Constants.CATEGORY_TOPIC, rangeTopic);
        systemPrompt.replace(Constants.DIFFICULTY_LEVEL, scoreScale);

        String userPrompt = Constants.USER_PROMPT + readerText;

        return AiResponse.builder()
                .url(url)
                .questions(
                        chatClient.prompt()
                                .system(systemPrompt)
                                .user(userPrompt)
                                .options(chatOptions)
                                .call()
                                .entity(new ParameterizedTypeReference<List<QuestionResponse>>() {
                                })
                )
                .build();

    }

    private String getRangeTopicFromDBString() {
        StringBuilder result = new StringBuilder();
        for (RangeTopic rangeTopic : rangeTopicRepository.findAll()) {
            result.append(rangeTopic.getContent().toLowerCase()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    private String getScoreScaleFromDBString() {
        StringBuilder result = new StringBuilder();
        for (ScoreScale scoreScale : scoreScaleRepository.findAll()) {
            result.append(scoreScale.getTitle().toLowerCase()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
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
