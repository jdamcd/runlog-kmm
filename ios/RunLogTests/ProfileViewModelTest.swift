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

    func testLoadSuccessSetsLoadingThenData() {
        let profile = AthleteProfile.with(id: 1)
        mockProfile.profile = ResultData(value: profile)

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data(profile))
    }

    func testLoadFailureSetsLoadingThenError() {
        mockProfile.profile = ResultError(error: KotlinThrowable())

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .error)
    }
}
