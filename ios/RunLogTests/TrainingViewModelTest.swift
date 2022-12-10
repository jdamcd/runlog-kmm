@testable import Kilometer
import RunLogShared
import XCTest

@MainActor
final class TrainingViewModelTest: XCTestCase {
    var viewModel: TrainingViewModel!
    var mockStrava: StravaMock!
    var mockUserState: UserStateMock!

    override func setUp() {
        mockStrava = StravaMock()
        mockUserState = UserStateMock()
        viewModel = TrainingViewModel(user: mockUserState, strava: mockStrava)
    }

    func testLoadSuccessSetsLoadingThenData() {
        let activity = ActivityCard.with(id: 1)
        mockStrava.activities = ResultData(data: NSArray(array: [activity]))

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data([activity]))
    }

    func testLoadFailureSetsLoadingThenError() {
        mockStrava.activities = ResultError(error: KotlinThrowable(), recoverable: true)

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .error)
        XCTAssertTrue(mockUserState.isLoggedIn())
    }

    func testLogOutOnUnrecoverableError() {
        mockStrava.activities = ResultError(error: KotlinThrowable(), recoverable: false)

        viewModel.load()
        XCTAssertEqual(viewModel.state, .loading)

        waitUntil(viewModel.$state, equals: .error)
        XCTAssertFalse(mockUserState.isLoggedIn())
    }

    func testRefreshUpdatesDataWithoutLoadingState() {
        // First load
        let activity1 = ActivityCard.with(id: 1)
        mockStrava.activities = ResultData(data: NSArray(array: [activity1]))

        viewModel.load()

        XCTAssertEqual(viewModel.state, TrainingState.loading)
        waitUntil(viewModel.$state, equals: .data([activity1]))

        // Refresh
        let activity2 = ActivityCard.with(id: 2)
        mockStrava.activities = ResultData(data: NSArray(array: [activity2]))

        viewModel.refresh()

        XCTAssertEqual(viewModel.state, .data([activity1]))
        waitUntil(viewModel.$state, equals: .data([activity2]))
    }

    func testRefreshLoadsIfNotLoaded() {
        let activity = ActivityCard.with(id: 1)
        mockStrava.activities = ResultData(data: NSArray(array: [activity]))

        viewModel.refresh()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data([activity]))
    }

    func testSetDarkModeUpdatesInteractor() {
        viewModel.setDarkMode(true)

        XCTAssertTrue(mockStrava.darkModeImages)
    }
}
