@file:Suppress("PackageDirectoryMismatch")

package com.rocksolidknowledge.stackunderflow

import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserTest {

    private val user: User = User(1, "Kevin")

    @Test
    fun `should be able to increase reputation`() {
        user.changeReputation(10)

        //user.reputation `should be` 10

        Assertions.assertEquals(10, user.reputation)
    }

    @Test
    fun `should be able to decrease reputation`() {
        user.changeReputation(10)
        user.changeReputation(-5)

        user.reputation `should be` 5
    }

    @Nested
    inner class `post should be able to` {

        val editReputationLimit = 2000

        @Test
        fun `edit if reputation is greater than 2000`() {
            user.changeReputation(editReputationLimit+1)
            user.canEditPost().shouldBeTrue()
        }

        @Test
        fun `edit if reputation is equal to 2000`() {
            user.changeReputation(editReputationLimit)
            user.canEditPost().shouldBeFalse()
        }

        @Test
        fun `edit if reputation is less than 2000`() {
            user.changeReputation(editReputationLimit-1)
            user.canEditPost().shouldBeFalse()
        }
    }

    @Nested
    inner class comment {

        val commentReputationLimit = 50

        @Test
        fun `should be able to add if reputation is greater than 50`() {
            user.changeReputation(commentReputationLimit+1)

            user.canComment().shouldBeTrue()

            //Assertions.assertTrue(user.canComment())
        }

        @Test
        fun `should not be able to add if reputation is equal to 50`() {
            user.changeReputation(commentReputationLimit)
            Assertions.assertFalse(user.canComment())
        }

        @Test
        fun `should not be able to add if reputation is less than 50`() {
            user.changeReputation(commentReputationLimit-1)
            Assertions.assertFalse(user.canComment())
        }
    }

}