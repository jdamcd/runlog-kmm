@testable import Kilometer
import RunLogShared
import XCTest

@MainActor
final class TrainingViewModelTest: XCTestCase {
    var viewModel: TrainingViewModel!
    var mockActivity: ActivityMock!

    override func setUp() {
        mockActivity = ActivityMock()
        viewModel = TrainingViewModel(stravaActivity: mockActivity)
    }

    func testLoadSuccessSetsLoadingThenData() {
        let activity = ActivityCard.with(id: 1)
        mockActivity.activities = ResultData(value: NSArray(array: [activity]))

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .data([activity]))
        XCTAssertEqual(mockActivity.callCount, 1)
    }

    func testLoadFailureSetsLoadingThenError() {
        mockActivity.activities = ResultError(error: KotlinThrowable())

        viewModel.load()

        XCTAssertEqual(viewModel.state, .loading)
        waitUntil(viewModel.$state, equals: .error)
        XCTAssertEqual(mockActivity.callCount, 1)
    }

    func testRefreshUpdatesDataWithoutLoadingState() {
        // First load
        let activity1 = ActivityCard.with(id: 1)
        mockActivity.activities = ResultData(value: NSArray(array: [activity1]))

        viewModel.load()

        XCTAssertEqual(viewModel.state, TrainingState.loading)
        waitUntil(viewModel.$state, equals: .data([activity1]))

        // Refresh
        let activity2 = ActivityCard.with(id: 2)
        mockActivity.activities = ResultData(value: NSArray(array: [activity2]))

        viewModel.refresh()

        XCTAssertEqual(viewModel.state, .data([activity1]))
        waitUntil(viewModel.$state, equals: .data([activity2]))
        XCTAssertEqual(mockActivity.callCount, 2)
    }

    func testRefreshDoesNothingIfNotLoaded() {
        XCTAssertEqual(viewModel.state, .loading)

        viewModel.refresh()

        XCTAssertEqual(viewModel.state, .loading)
        XCTAssertEqual(mockActivity.callCount, 0)
    }
}
