package com.rocksolidknowledge.stackunderflow

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

class UnderflowServiceTest {

    @Nested
    inner class UnderflowServiceInstanciacao {

        var mockQuestionRepository = mockk<IQuestionRepository>()
        /**
         * Irá ignorar erros nessa interface caso chame algum método que nao foi
         */
        var mockUserRepository = mockk<IUserRepository>(relaxUnitFun = true)

        val service = UnderflowService(mockQuestionRepository, mockUserRepository)

        val voterId = 10
        val questionId = 20

        @Test
        fun `should be able to initialise service`() {
            service.shouldNotBeNull()

        }

        @Test
        fun `should be able to vote up question`() {
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

    @Nested
    inner class UnderflowServiceComAnotacao {

        @MockK
        lateinit var mockQuestionRepository: IQuestionRepository

        @MockK
        lateinit var mockUserRepository: IUserRepository

        init {
            MockKAnnotations.init(this)
        }

        val service = UnderflowService(mockQuestionRepository, mockUserRepository)

        val voterId = 10
        val questionId = 20

        @Test
        fun `should be able to initialise service`() {
            service.shouldNotBeNull()

        }

        @Test
        fun `should be able to vote up question`() {
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

    @Nested
    /**
     * @ExtendWith(MockKExtension::class) irá realizar a inicializacao do mockQuestionRepository e mockUserRepository sem a
     * necessidade do bloco init{}, mas precisa-se chamar o objeto que utiliza ese mock no método, para dar tempo de essas
     * variáveis terem sido injetadas.
     */
    @ExtendWith(MockKExtension::class)
    inner class UnderflowServiceComAnotacaoEInicializacaoJUnit {

        @MockK
        lateinit var mockQuestionRepository: IQuestionRepository

        /**
         * Anotação utlizada para ignorar a necessidade de ensinar ao mock o que fazer se um determinado método for chamado.
         * Desta forma pode-se nao implementar o mock para alguma chamada que nao tenha relevancia no teste
         * Nesse caso, utilzei essa estratégia para a chamada do método mockUserRepository.update, onde eu
         * comentei a configuração do mock para sua chamada.
         * Sem essa anotação, o Mockk iria reclamar que para o método mockUserRepository.update nao foi implementado um mock.
         */
        @RelaxedMockK
        lateinit var mockUserRepository: IUserRepository

        val voterId = 10
        val questionId = 20

        @Test
        fun `should be able to vote up question`() {
            //inicializacao do UnderflowService mudou para dentro do método.
            val service = UnderflowService(mockQuestionRepository, mockUserRepository)

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
            //every { mockUserRepository.update(userOwner) } just Runs

            val voteUpQuestion = service.voteUpQuestion(questionId, voterId)

            voteUpQuestion.shouldBeGreaterOrEqualTo(3)
        }
    }
}