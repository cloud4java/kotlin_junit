import com.rocksolidknowledge.stackunderflow.Answer
import com.rocksolidknowledge.stackunderflow.Question
import com.rocksolidknowledge.stackunderflow.QuestionException
import com.rocksolidknowledge.stackunderflow.User
import org.amshove.kluent.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource

class QuestionTest {

    val user = User(id = 1, name = "Erick")

    @Nested
    inner class `Construcao de question` {

        @Test
        fun `deve lancar uma excecao se o titulo for vazio`() {

            invoking { Question(1, user, "", "Teste") } `should throw` QuestionException::class
        }

        @Test
        fun `deve lancar uma excecao se o pergunta for vazia`() {
            Assertions.assertThrows(QuestionException::class.java) {
                Question(1, user, "Teste", "")
            }
        }

        @Test
        fun `nao deve lancar uma excecao se titulo e pergunta forem validos`() {
            Assertions.assertDoesNotThrow {
                Question(1, user, "Teste", "Teste")
            }
        }

        @ParameterizedTest
        @CsvSource(
            "' ', question",
            "'', question",
            "title, ' '",
            "title, ''"
        )
        fun `deve lancar uma excecao se o titulo ou a pergunta for invalida`(title: String, question: String) {
            Assertions.assertThrows(QuestionException::class.java) {
                Question(1, user, title, question)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class `Construcao de question com metodosource` {

        @Suppress("unused")
        fun parametrosParaQuestao() = listOf(
            Arguments.of("", "question"),
            Arguments.of(" ", "question"),
            Arguments.of("title", ""),
            Arguments.of("title", " "),
        )

        @ParameterizedTest
        @MethodSource("parametrosParaQuestao")
        fun `deve lancar uma excecao se o titulo ou a pergunta for invalida 2`(title: String, question: String) {
            Assertions.assertThrows(QuestionException::class.java) {
                Question(1, user, title, question)
            }
        }
    }

    @Nested
    inner class Answers{

        val user = User(1, "Erick")
        val question = Question(1, user, "title", "question")

        @Test
        fun `should have an no answer`(){
            question.answers.shouldBeEmpty()
        }

        @Test
        fun `should have an answer`(){
            question.addAnswer(Answer(1, user, "answer"))
            question.answers.shouldNotBeEmpty()
        }

        @Test
        fun `should contain an answer`(){

            val answer1 = Answer(1, user, "answer")
            val answer2 = Answer(2, user, "answer2")

            question.addAnswer(answer1)
            question.addAnswer(answer2)


            question.answers `should contain` answer2
        }

        @Test
        fun `should not contain an answer that was not added`(){

            val answer1 = Answer(1, user, "answer")
            val answer2 = Answer(2, user, "answer2")

            question.addAnswer(answer1)


            question.answers `should not contain` answer2
        }
    }
}