package com.example.web.qna.usecase;

import com.example.web.qna.domain.Answer;
import com.example.web.qna.domain.Question;
import com.example.web.qna.dto.AnswerDTO;
import com.example.web.qna.dto.QuestionDTO;
import com.example.web.qna.repository.AnswerRepository;
import com.example.web.qna.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QnAService {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerRepository answerRepository;

    public QuestionDTO getRandomQuestion() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            return null; 
        }
        Question randomQuestion = questions.get((int) (Math.random() * questions.size()));
        return new QuestionDTO(randomQuestion.getId(), randomQuestion.getContent());
    }

    public AnswerDTO saveAnswer(Answer answer) {
        Answer savedAnswer = answerRepository.save(answer);
        return new AnswerDTO(savedAnswer.getId(), savedAnswer.getContent(), savedAnswer.getQuestion().getId());
    }
}
