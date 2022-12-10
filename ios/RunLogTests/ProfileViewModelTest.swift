@testable import Kilometer
import RunLogShared
import XCTest

@MainActor
final class ProfileViewModelTest: XCTestCase {
    var viewModel: ProfileViewModel!
    var mockStrava: StravaMock!
    var mockUserState: UserStateMock!

    override func setUp() {
        mockStrava = StravaMock()
        mockUserState = UserStateMock()
        viewModel = ProfileViewModel(user: mockUserState, strava: mockStrava)
    }

    func testLoadSuccessSetsLoadingThenData() {
        let profile = AthleteProfile.with(id: 1)
        mockStrava.profile = ResultData(data: profile)

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data(profile))
    }

    func testLoadFailureSetsLoadingThenError() {
        mockStrava.profile = ResultError(error: KotlinThrowable(), recoverable: true)

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .error)
    }

    func testLogOutOnUnrecoverableError() {
        mockStrava.activities = ResultError(error: KotlinThrowable(), recoverable: false)

        viewModel.load()
        XCTAssertEqual(viewModel.state, .loading)

        waitUntil(viewModel.$state, equals: .error)
        XCTAssertFalse(mockUserState.isLoggedIn())
    }
}
