package com.jdamcd.runlog.shared.login

import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.AthleteDao
import com.jdamcd.runlog.shared.profile.AthleteMapper
import com.jdamcd.runlog.shared.testutil.MockClock
import com.jdamcd.runlog.shared.testutil.athleteModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest

@ExperimentalCoroutinesApi
class LoginInteractorTest {

    private lateinit var interactor: LoginInteractor

    private val api: StravaApi = mockk()
    private val dao: AthleteDao = mockk()
    private val mapper = AthleteMapper(MockClock)

    @BeforeTest
    fun setUp() {
        interactor = LoginInteractor(api, dao, mapper)
    }

    @Test
    fun `authenticate success saves user to DB`() = runTest {
        coEvery { api.tokenExchange("123") } returns athleteModel()
        every { dao.insert(any()) } just runs

        interactor.authenticate("123") shouldBe LoginResult.Success
        verify { dao.insert(any()) }
    }

    @Test
    fun `authenticate failure returns error`() = runTest {
        val error = Throwable()
        coEvery { api.tokenExchange("123") } throws error

        interactor.authenticate("123") shouldBe LoginResult.Error(error)
        verify(exactly = 0) { dao.insert(any()) }
    }
}
