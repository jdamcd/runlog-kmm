@testable import Kilometer
import RunLogShared
import XCTest

@MainActor
final class ProfileViewModelTest: XCTestCase {
    var viewModel: ProfileViewModel!
    var mockProfile: ProfileMock!
    var mockUserState: UserStateMock!

    override func setUp() {
        mockProfile = ProfileMock()
        mockUserState = UserStateMock()
        viewModel = ProfileViewModel(user: mockUserState, stravaProfile: mockProfile)
    }

    func testLoadSuccessSetsLoadingThenData() {
        let profile = AthleteProfile.with(id: 1)
        mockProfile.profile = ResultData(data: profile)

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data(profile))
    }

    func testLoadFailureSetsLoadingThenError() {
        mockProfile.profile = ResultError(error: KotlinThrowable(), recoverable: true)

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .error)
    }

    func testLogOutOnUnrecoverableError() {
        mockProfile.profile = ResultError(error: KotlinThrowable(), recoverable: false)

        viewModel.load()
        XCTAssertEqual(viewModel.state, .loading)

        waitUntil(viewModel.$state, equals: .error)
        XCTAssertFalse(mockUserState.isLoggedIn())
    }
}
