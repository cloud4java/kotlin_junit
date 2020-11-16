package com.rocksolidknowledge.stackunderflow

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

class UnderflowServiceTest {

    var mockQuestionRepository = mockk<IQuestionRepository>()
    var mockUserRepository = mockk<IUserRepository>()

    val service = UnderflowService(mockQuestionRepository, mockUserRepository)

    val voterId = 10
    val questionId = 20

    @Test
    fun `should be able to initialise service`(){
        service.shouldNotBeNull()
    }

    @Test
    fun `should be able to vote up question` () {
        val userOwner = User(1, "Erick")
        val userVoter = User(1, "Angelica")
        userVoter.changeReputation(500)

        val question = Question(questionId, userOwner, "title", "question")
        question.voteUp()
        question.voteUp()

        every { mockQuestionRepository.findQuestion(questionId) } returns question
        every { mockUserRepository.findUser(voterId) } returns userVoter
        every { mockUserRepository.findUser(question.userId) } returns userOwner
        every { mockQuestionRepository.update(question) } just Runs
        every { mockUserRepository.update(userOwner) } just Runs

        val voteUpQuestion = service.voteUpQuestion(questionId, voterId)

        voteUpQuestion.shouldBeGreaterOrEqualTo(3)
    }
}