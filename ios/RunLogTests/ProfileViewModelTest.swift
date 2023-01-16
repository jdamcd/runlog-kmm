@testable import Kilometer
import RunLogShared
import XCTest

@MainActor
final class ProfileViewModelTest: XCTestCase {
    var viewModel: ProfileViewModel!
    var mockProfile: ProfileMock!

    override func setUp() {
        mockProfile = ProfileMock()
        viewModel = ProfileViewModel(stravaProfile: mockProfile)
    }

    func testLoadSuccessWithRefreshSuccess() {
        let profile = AthleteProfile.with(id: 1)
        mockProfile.profile = [ResultData(value: profile), ResultData(value: profile)]
        mockProfile.refreshState = RefreshState.success

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data(profile))
    }

    func testLoadSuccessWithRefreshError() {
        let profile = AthleteProfile.with(id: 1)
        mockProfile.profile = [ResultData(value: profile), ResultError(error: KotlinThrowable())]
        mockProfile.refreshState = RefreshState.error

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data(profile))
    }

    func testLoadErrorWithRefreshError() {
        mockProfile.profile = [ResultError(error: KotlinThrowable()), ResultError(error: KotlinThrowable())]
        mockProfile.refreshState = RefreshState.error

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .error)
    }

    func testLoadErrorWithRefreshSuccess() {
        let profile = AthleteProfile.with(id: 1)
        mockProfile.profile = [ResultError(error: KotlinThrowable()), ResultData(value: profile)]
        mockProfile.refreshState = RefreshState.success

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data(profile))
    }
}
