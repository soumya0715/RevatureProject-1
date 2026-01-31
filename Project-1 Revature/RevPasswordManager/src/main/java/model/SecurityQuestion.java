package model;

public class SecurityQuestion {
    private int questionId;
    private int userId;
    private String question;
    private String answerHash;
    // getters and setters

    public SecurityQuestion() {
    }


    public SecurityQuestion(int questionId, int userId,
                            String question, String answerHash) {
        this.questionId = questionId;
        this.userId = userId;
        this.question = question;
        this.answerHash = answerHash;
    }


    @Override
    public String toString() {
        return "SecurityQuestion{" +
                "answerHash='" + answerHash + '\'' +
                ", question='" + question + '\'' +
                ", questionId=" + questionId +
                ", userId=" + userId +
                '}';
    }

    public String getAnswerHash() {
        return answerHash;
    }

    public void setAnswerHash(String answerHash) {
        this.answerHash = answerHash;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
